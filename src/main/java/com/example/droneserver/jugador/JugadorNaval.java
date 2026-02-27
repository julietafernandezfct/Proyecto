package com.example.droneserver.jugador;

import com.example.droneserver.Position;

public class JugadorNaval extends Jugador {

    public JugadorNaval(String sessionId, int slot) {
        this.sessionId = sessionId;
        this.slot = slot;
        this.tipo = "NAVAL";

        int n = 6;
        drones = new Position[n];
        vidas = new int[n];

        for (int i = 0; i < n; i++) {
            String objId = dronId(i + 1);        
            drones[i] = new Position(sessionId, slot, objId, tipo, 0, 0, 0, 0, 0, 0, 1);
            vidas[i] = 2;
        }
    }
}
