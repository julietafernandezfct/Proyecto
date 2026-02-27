package com.example.droneserver;

//SACAR Y PASAR LO QUE FALTA A LA CLASE EN EL PACKAGE PERSISTENCIA
public class Dron {
    private final String objId;   // "DRON_1", "DRON_2", etc
    private int vida;
    private int municion;
    private Position posicion; // null hasta que llegue update

    public Dron(String objId, int vidaInicial, int municionInicial) {
        this.objId = objId;
        this.vida = vidaInicial;
        this.municion = municionInicial;
    }

    public String getObjId() { 
    	return objId; 
    }
    
    public int getVida() { 
    	return vida; 
    }
    
    public int getMunicion() { 
    	return municion; 
    }
    
    public Position getPosicion() { 
    	return posicion; 
    }

    public void setPosicion(Position p) { 
    	this.posicion = p; 
    }

    public void aplicarDanio(int d) {
        if (d <= 0) 
        	return;
        vida = Math.max(0, vida - d);
    }

    public boolean estaMuerto() { 
    	return vida <= 0; 
    }
}
