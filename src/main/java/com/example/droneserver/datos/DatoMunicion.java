package com.example.droneserver.datos;

public class DatoMunicion {

    public String sessionId;
    public String objId;   
    public int municion;

    public DatoMunicion() {}

    public DatoMunicion(String sessionId, String objId, int municion) {
        this.sessionId = sessionId;
        this.objId = objId;
        this.municion = municion;
    }
}