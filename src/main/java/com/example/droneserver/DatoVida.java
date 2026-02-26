package com.example.droneserver;

public class DatoVida {

	public String sessionId;
    public String objId;
    public int vida;

    public DatoVida() {}

    public DatoVida(String sessionId, String objId, int vida) {
        this.sessionId = sessionId;
        this.objId = objId;
        this.vida = vida;
    }
}
