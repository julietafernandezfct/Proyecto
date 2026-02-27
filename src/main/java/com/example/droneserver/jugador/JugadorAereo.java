package com.example.droneserver.jugador;

import com.example.droneserver.Dron;
import com.example.droneserver.PortaDrones;

public class JugadorAereo extends Jugador {

    public JugadorAereo(String sessionId, int slot) {
        super(sessionId, slot, new PortaDrones(6), crearDrones());
    }

    private static Dron[] crearDrones() {
        Dron[] arr = new Dron[12];
        for (int i = 0; i < 12; i++) {
            arr[i] = new Dron("DRON_AEREO" + (i + 1), 1, 1); // vida 1, 1 bomba
        }
        return arr;
    }
}
