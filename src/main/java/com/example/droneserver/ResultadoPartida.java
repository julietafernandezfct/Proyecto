package com.example.droneserver;

public class ResultadoPartida {
    public String estado;
    public int segundosRestantes;
    public int slotSinPorta; //slot del jugador que perdió el porta
    
    public ResultadoPartida(String estado, int segundos, int slotSinPorta) {
        this.estado = estado;
        this.segundosRestantes = segundos;
        this.slotSinPorta = slotSinPorta;
    }
}
