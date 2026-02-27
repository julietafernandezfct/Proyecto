package com.example.droneserver.datos;

public class DatoVida {
    public int objId;   
    public String tipo;    
    public int vida;

    public DatoVida() {}
    public DatoVida(int objId, String tipo, int vida) {
        this.objId = objId;
        this.tipo = tipo;
        this.vida = vida;
    }
}