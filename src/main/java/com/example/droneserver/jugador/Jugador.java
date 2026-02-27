package com.example.droneserver.jugador;

import com.example.droneserver.Position;

public abstract class Jugador {

    protected String sessionId;
    protected int slot;
    protected String tipo; 

    protected Position[] dronesPos;
    protected int[] vidas;

  
    protected Position portaPos;
    protected int portaVida = 10; 

    public String getSessionId() { return sessionId; }
    public int getSlot() { return slot; }
    public String getTipo() { return tipo; }

  
    protected String dronId(int idx1based) { 
    	return "DRON_" + idx1based; 
    }
    
    public String getObjIdPorta() { 
    	return "PORTA"; 
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

    public void actualizarPosicion(String objId, Position pos) {
        if (objId == null || pos == null) 
        	return;

        if (getObjIdPorta().equals(objId)) {
            if (portaPos == null) 
            	return; 
            portaPos.x = pos.x; portaPos.y = pos.y; portaPos.z = pos.z;
            portaPos.qx = pos.qx; portaPos.qy = pos.qy; portaPos.qz = pos.qz; portaPos.qw = pos.qw;
            return;
        }
        for (int i = 0; i < dronesPos.length; i++) {
            if (dronesPos[i] != null && objId.equals(dronesPos[i].objId)) {
                dronesPos[i].x = pos.x; dronesPos[i].y = pos.y; dronesPos[i].z = pos.z;
                dronesPos[i].qx = pos.qx; dronesPos[i].qy = pos.qy; dronesPos[i].qz = pos.qz; dronesPos[i].qw = pos.qw;
                return;
            }
        }
    }

    public void aplicarDanio(String objId, int danio) {
        if (danio <= 0 || objId == null) 
        	return;

        if (getObjIdPorta().equals(objId)) {
            portaVida = Math.max(0, portaVida - danio);
            return;
        }

        for (int i = 0; i < dronesPos.length; i++) {
            if (dronesPos[i] != null && objId.equals(dronesPos[i].objId)) {
                vidas[i] = Math.max(0, vidas[i] - danio);
                return;
            }
        }
    }
}