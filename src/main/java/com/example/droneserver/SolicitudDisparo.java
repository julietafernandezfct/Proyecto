package com.example.droneserver;

public class SolicitudDisparo {
    public String sessionId;
    public String objIdDisparador;

    public float x, y, z;     // posición inicial
    public float dx, dy, dz;  // dirección 
    public float velocidad;
    public float rangoMax;
    public float radioExplosion;
    public int danio;

    public SolicitudDisparo() {}
}