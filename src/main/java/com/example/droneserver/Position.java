package com.example.droneserver;

public class Position {
	public String sessionId;
	public int slot;
    public float x;
    public float y;
    public float z;
    public float qx;
    public float qy;
    public float qz;
    public float qw;

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", z=" + z + ", qx=" + qx + ", qy=" + qy + ", qz=" + qz + ", qw" + qw;
    }
    
    public Position() {}
}