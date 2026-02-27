package com.example.droneserver.jugador;

import com.example.droneserver.Position;

public class JugadorAereo extends Jugador {

    public JugadorAereo(String sessionId, int slot) {

        this.sessionId = sessionId;
        this.slot = slot;
        this.tipo = "AEREO";

        int n = 12;

        dronesPos = new Position[n];
        vidas = new int[n];

        for (int i = 0; i < n; i++) {
            int objId = dronId(i + 1);
            dronesPos[i] = new Position(sessionId,slot,objId,tipo,0, 0, 0,0, 0, 0, 1);
            vidas[i] = 1;
        }
    }
}