package com.example.droneserver;

public class JoinResponse {

    public String codigo;
    public String sessionId;
    public int jugadores;
    public int portaId;
    public int[] dronesIds;

    public JoinResponse(String codigo, String sessionId, int jugadores, int portaId, int[] dronesIds) {
        this.codigo = codigo;
        this.sessionId = sessionId;
        this.jugadores = jugadores;
        this.portaId = portaId;
        this.dronesIds = dronesIds;
    }
}
