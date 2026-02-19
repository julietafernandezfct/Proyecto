package com.example.droneserver;

public class JoinResponse {

    public String codigo;
    public String sessionId;
    public int jugadores;

    public JoinResponse(String codigo, String sessionId, int jugadores) {
        this.codigo = codigo;
        this.sessionId = sessionId;
        this.jugadores = jugadores;
    }
}
