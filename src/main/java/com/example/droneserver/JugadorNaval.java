package com.example.droneserver;

public class JugadorNaval extends Jugador {

    public JugadorNaval(String sessionId, int slot) {
        super(sessionId, slot, new PortaDrones(3), crearDrones());
    }

    private static Dron[] crearDrones() {
        Dron[] arr = new Dron[6];
        for (int i = 0; i < 6; i++) {
            arr[i] = new Dron("DRON_NAVAL" + (i + 1), 2, 2); // vida 2, 2 misiles
        }
        return arr;
    }
}
