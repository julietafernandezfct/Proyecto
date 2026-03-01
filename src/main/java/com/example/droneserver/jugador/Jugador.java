package com.example.droneserver.jugador;

import com.example.droneserver.Position;

public abstract class Jugador {

    protected String sessionId;
    protected int slot;
    protected String tipo;

    protected Position[] dronesPos;
    protected int[] vidas;
    protected int municion;

    protected Position portaPos;
    protected int portaVida = 10;

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
        return 0;
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

    public boolean colocarPorta(Position pos) {

        if (portaPos != null)
            return false;

        portaPos = pos;
        portaPos.tipo = "PORTA";
        portaPos.objId = getObjIdPorta();
        portaPos.sessionId = sessionId;
        portaPos.slot = slot;

        return true;
    }

    public void actualizarPosicion(int objId, Position pos) {

        if (pos == null)
            return;
        if (objId == getObjIdPorta()) {
            if (portaPos == null)
                return;
            portaPos.x = pos.x; portaPos.y = pos.y; portaPos.z = pos.z;
            portaPos.qx = pos.qx; portaPos.qy = pos.qy; portaPos.qz = pos.qz; portaPos.qw = pos.qw;
            return;
        }
        for (int i = 0; i < dronesPos.length; i++) {
            if (i + 1 == objId && dronesPos[i] != null) {
                dronesPos[i].x = pos.x; dronesPos[i].y = pos.y; dronesPos[i].z = pos.z;
                dronesPos[i].qx = pos.qx; dronesPos[i].qy = pos.qy; dronesPos[i].qz = pos.qz; dronesPos[i].qw = pos.qw;
                return;
            }
        }
    }

    public void aplicarDanio(int objId, int danio) {
    	//se necesita?
        if (danio <= 0)
            return;
        if (objId == getObjIdPorta()) {
            portaVida = Math.max(0, portaVida - danio);
            return;
        }
        //no tiene sentido que este for este en un if?
        for (int i = 0; i < dronesPos.length; i++) {
            if (i + 1 == objId) {
                vidas[i] = Math.max(0, vidas[i] - danio);
                //conultar con jose, esta peligroso este return
                return;
            }
        }
    }
    
    public void recargaMunicion(int objId) {
    	for(int i = 0; i < dronesPos.length; i++) {
    		if(i + 1 == objId) {
    			municion = municion + 1;
    			//misma situacion 
    			return;
    		}
    	}
    }
}