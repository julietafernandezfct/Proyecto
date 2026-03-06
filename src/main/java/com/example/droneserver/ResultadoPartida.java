package com.example.droneserver;

public class ResultadoPartida {
    public String estado; // "JUGANDO", "VICTORIA_HOST", "VICTORIA_JOIN", "EMPATE", "CUENTA_REGRESIVA"
    public int segundosRestantes;
    
    public ResultadoPartida(String estado, int segundos) {
        this.estado = estado;
        this.segundosRestantes = segundos;
    }
}
