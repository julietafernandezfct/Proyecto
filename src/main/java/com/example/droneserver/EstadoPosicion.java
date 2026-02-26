package com.example.droneserver;

public class EstadoPosicion {
    public float x, y, z;
    public float qx, qy, qz, qw;

    public EstadoPosicion() {}

    public EstadoPosicion(float x, float y, float z, float qx, float qy, float qz, float qw) {
        this.x = x; this.y = y; this.z = z;
        this.qx = qx; this.qy = qy; this.qz = qz; this.qw = qw;
    }
}