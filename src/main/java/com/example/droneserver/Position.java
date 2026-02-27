package com.example.droneserver;

public class Position {
    public String sessionId;   // quién manda / dueño
    public int slot;           // 1 host, 2 join

    // identifica el objeto ("PORTA", "DRON_1", etc.)
    public String objId;

    // tipo del jugador/objeto ("AEREO", "NAVAL", "PORTA")
    public String tipo;

    public float x;
    public float y;
    public float z;

    public float qx;
    public float qy;
    public float qz;
    public float qw;

    public Position() {}

    public Position(String sessionId, int slot, String objId, String tipo, float x, float y, float z, float qx, float qy, float qz, float qw) {
        this.sessionId = sessionId;
        this.slot = slot;
        this.objId = objId;
        this.tipo = tipo;
        this.x = x; this.y = y; this.z = z;
        this.qx = qx; this.qy = qy; this.qz = qz; this.qw = qw;
    }
    
    //hacer un contructor q acepte solo x,y,z

    @Override
    public String toString() {
        return "objId=" + objId + ", tipo=" + tipo + ", x=" + x + ", y=" + y + ", z=" + z;
    }
}