package com.example.droneserver;

public class Position {

    public String sessionId;
    public int slot;

    public int objId;

    public String tipo;

    public float x;
    public float y;
    public float z;

    public float qx;
    public float qy;
    public float qz;
    public float qw;

    public Position() {}

    public Position(String sessionId, int slot, int objId, String tipo, float x, float y, float z, float qx, float qy, float qz, float qw) {

        this.sessionId = sessionId;
        this.slot = slot;
        this.objId = objId;
        this.tipo = tipo;

        this.x = x; this.y = y; this.z = z;
        this.qx = qx; this.qy = qy; this.qz = qz; this.qw = qw;
    }

    @Override
    public String toString() {
        return "objId=" + objId + ", tipo=" + tipo + ", x=" + x + ", y=" + y + ", z=" + z;
    }
}