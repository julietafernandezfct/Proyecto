package com.example.droneserver.jugador;

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

        return true;
    }

    public void actualizarPosicion(int objId, Position p) {
        if (objId == this.objId) { // si este objId es del porta
            this.portaPos = p;
            return;
        }

        int idx = idxFromObjId(objId);
        if (idx < 0 || idx >= dronesPos.length) return;

        dronesPos[idx] = p;
        // por si querés asegurar el objId:
        dronesPos[idx].objId = objId;
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