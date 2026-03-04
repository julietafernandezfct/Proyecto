package com.example.droneserver.jugador;

import com.example.droneserver.Position;

public class JugadorAereo extends Jugador {
	
	@Override
	protected int baseDronId() {
	    return 8;
	}

    public JugadorAereo(String sessionId, int slot, int id) {

        this.sessionId = sessionId;
        this.slot = slot;
        this.tipo = "AEREO";
        this.objId = id;

        int n = 12;

        dronesPos = new Position[n];
        vidas = new int[n];

        for (int i = 0; i < n; i++) {
            int objId = baseDronId() + i; // 8..19
            dronesPos[i] = null; //inicializamos todas las posiciones en null hasta que se spawneen 
            vidas[i] = 1;
        }
    }
    
    @Override
    public void actualizarPosicion(int objId, Position p) {
        if (objId == this.objId) { 
        	portaPos = p; 
        	return; }
        int idx = idxFromObjId(objId);
        if (idx < 0 || idx >= dronesPos.length) 
        	return;
        dronesPos[idx] = p;
        dronesPos[idx].objId = objId;
    }

    @Override
    public void aplicarDanio(int objId, int danio) {
        if (objId == this.objId) { portaVida = Math.max(0, portaVida - danio); return; }
        int idx = idxFromObjId(objId);
        if (idx < 0 || idx >= vidas.length) return;
        vidas[idx] = Math.max(0, vidas[idx] - danio);
    }

    @Override
    public void recargaMunicion(int objId) {
        int idx = idxFromObjId(objId);
        if (idx < 0 || idx >= dronesPos.length) return;
        // tu lógica real de recarga acá
    }
    
    @Override
    public Position getDronPorObjId(int objId) {

        int idx = objId - 8;

        if (idx < 0 || idx >= dronesPos.length)
            return null;

        return dronesPos[idx];
    }
}