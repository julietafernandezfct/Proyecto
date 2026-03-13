package com.example.droneserver.jugador;

import com.example.droneserver.Position;

public class JugadorNaval extends Jugador {

		@Override
	protected int baseDronId() {
	    return 2;
	}
		
    public JugadorNaval(String sessionId, int slot, int id) {

        this.sessionId = sessionId;
        this.slot = slot;
        this.tipo = "NAVAL";
        this.objId = id;

        int n = 6;

        dronesPos = new Position[n]; //inicializamos todas las posiciones en null hasta que se spawneen
        vidas = new int[n];

        for (int i = 0; i < n; i++) {
            int objId = baseDronId() + i; // 2..7
            dronesPos[i] = null; 
            vidas[i] = 1;
        }
    }
    
    @Override
    public void actualizarPosicion(int objId, Position p) {
    	super.actualizarPosicion(objId, p);
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

        int idx = objId - 2;

        if (idx < 0 || idx >= dronesPos.length)
            return null;

        return dronesPos[idx];
    }
}