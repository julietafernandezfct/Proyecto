package com.example.droneserver;

public class JoinResponse {

	//pq esta publico esto?
    public String codigo;
    public String sessionId;
    public int jugadores;
    public int portaId;


    public JoinResponse(String codigo, String sessionId, int jugadores, int porta) {
        this.codigo = codigo;
        this.sessionId = sessionId;
        this.jugadores = jugadores;
        this.portaId = porta;
    }
}
