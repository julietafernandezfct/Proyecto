package com.example.droneserver.solicitudes;

public class SolicitudDisparo {
    public String sessionId;
    public int objIdDisparador;

    public float x, y, z;     // posición inicial
    public float dx, dy, dz;  // dirección 
    public float velocidad;
    public float rangoMax;
    public float radioExplosion;
    public int danio;

    public SolicitudDisparo() {}
}