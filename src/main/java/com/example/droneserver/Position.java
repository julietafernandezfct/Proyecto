package com.example.droneserver;

public class Position {
    public String sessionId;
    public int slot;

    //identifica el objeto ("PORTA", "DRON_1", etc.)
    public String objId;

    public float x;
    public float y;
    public float z;
    public float qx;
    public float qy;
    public float qz;
    public float qw;

    @Override
    public String toString() {
        return "objId=" + objId + ", x=" + x + ", y=" + y + ", z=" + z
                + ", qx=" + qx + ", qy=" + qy + ", qz=" + qz + ", qw=" + qw;
    }

    public Position() {}

    public Position(float posx, float posy, float posz) {
        x = posx;
        y = posy;
        z = posz;
        qx = 0;
        qy = 0;
        qz = 0;
        qw = 1; // hace que inicie sin rotacion paralelo al suelo
    }
}