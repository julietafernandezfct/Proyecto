package com.example.droneserver.jugador;

import com.example.droneserver.Dron;
import com.example.droneserver.PortaDrones;
import com.example.droneserver.Position;

public abstract class Jugador {

    protected String sessionId;
    protected int slot;
    protected String tipo;
    protected int objId;
    
    protected Position[] dronesPos;
    protected int[] vidas;
    protected int municion;

    protected Position portaPos;
    protected int portaVida = 10;
    protected PortaDrones porta;

    protected abstract int baseDronId();

    protected int idxFromObjId(int objId) {
        return objId - baseDronId();
    }
    
    public String getSessionId() { 
    	return sessionId; 
    }
    public int getSlot() { 
    	return slot; 
    }
    public String getTipo() { 
    	return tipo; 
    }

    protected int dronId(int id) {
        return id;
    }

    public int getObjIdPorta() {
        return objId;
    }

    public Position getPortaPosicion() {
        return portaPos;
    }

    public int getPortaVida() {
        return portaVida;
    }

    public Position[] getDronesPos() {
        return dronesPos;
    }

    public int[] getVidas() {
        return vidas;
    }
    
    public int getMunicion() {
    	return municion;
    }
    
    public PortaDrones getPorta() {
        return porta;
    }

    public void setPorta(PortaDrones porta) {
        this.porta = porta;
    }

    public boolean colocarPorta(Position pos) {

        if (portaPos != null)
            return false;

        portaPos = pos;
        portaPos.objId = getObjIdPorta();
        portaPos.sessionId = sessionId;
        portaPos.slot = slot;
        
        if (porta == null) {
            porta = new PortaDrones(portaVida);
            porta.colocar(pos);
        }

        return true;
    }
    
    public void setPortaPos(Position pos) {
        this.portaPos = pos;
    }

    public void actualizarPosicion(int objId, Position p) {
        if (objId == this.objId) { // si este objId es del porta
            this.portaPos = p;
            return;
        }

        int idx = idxFromObjId(objId);
        if (idx < 0 || idx >= dronesPos.length) return;
        
        System.out.println("actualizarPosicion: objId=" + objId + " porta=" + (porta != null ? "OK" : "null") + " dronesPos[idx]=" + (dronesPos[idx] == null ? "null(nuevo)" : "existe"));
        
        // Si el dron es nuevo, agregarlo al porta
        if (dronesPos[idx] == null && porta != null) {
            Dron dron = new Dron(objId, portaVida > 0 ? vidas[idx] : 0, municion);
            porta.getDrones().add(dron);
            System.out.println("Dron agregado al porta: objId=" + objId + " total drones=" + porta.getDrones().size());
        }

        dronesPos[idx] = p;
        dronesPos[idx].objId = objId;
        
        // Sincronizar posición en el objeto Dron
        if (porta != null) {
            for (Dron d : porta.getDrones()) {
                if (d.codigo() == objId) {
                    d.setPosicion(p);
                    System.out.println("Posicion sincronizada en dron: objId=" + objId);
                    break;
                }
            }
        }
    }

    public void aplicarDanio(int objId, int danio) {
        if (objId == this.objId) {
            portaVida = Math.max(0, portaVida - danio);
            return;
        }

        int idx = idxFromObjId(objId);
        if (idx < 0 || idx >= vidas.length) return;

        vidas[idx] = Math.max(0, vidas[idx] - danio);
    }
    
    public void recargaMunicion(int objId) {
        int idx = idxFromObjId(objId);
        if (idx < 0 || idx >= dronesPos.length) return;

        // ejemplo:
        // municion[idx] = MAX;
    }

    public abstract Position getDronPorObjId(int objId);

	public int[] getDronesIds() {
	
	    int[] ids = new int[dronesPos.length];
	
	    for (int i = 0; i < dronesPos.length; i++) {
	    	ids[i] = (dronesPos[i] != null) ? dronesPos[i].objId : -1;
	    }
	
	    return ids;
	}
}