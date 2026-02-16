package com.example.droneserver;

public class Position {
    public float x;
    public float y;
    public float z;

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", z=" + z;
    }
}