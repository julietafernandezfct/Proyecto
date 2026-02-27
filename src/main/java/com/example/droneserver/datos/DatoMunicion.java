package com.example.droneserver.datos;

public class DatoMunicion {
    public String sessionId;
    public String objId;
    public int ammo;

    public DatoMunicion() {}

    public DatoMunicion(String sessionId, String objId, int ammo) {
        this.sessionId = sessionId;
        this.objId = objId;
        this.ammo = ammo;
    }
}
