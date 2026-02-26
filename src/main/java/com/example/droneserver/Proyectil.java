package com.example.droneserver;

public class Proyectil {
    public String id;
    public String atacanteSessionId;
    public String objIdDisparador; 

    public float x, y, z;
    public float dx, dy, dz; 
    public float velocidad;

    public float recorrido;   // cuánto ya recorrió
    public float rangoMax;    // límite
    public int danio;         

    public boolean activo = true;

    public Proyectil() {}
}
