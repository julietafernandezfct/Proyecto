package com.example.droneserver;

//SACAR Y PASAR LO QUE FALTA A LA CLASE EN EL PACKAGE PERSISTENCIA
public class Dron {
    private final String objId;   //pasa a int
    /*private String codPort;
	private String tipo;*/
    private int vida;
    private int municion;
    private EstadoPosicion posicion; // null hasta que llegue update

    
    public Dron(String objId, int vidaInicial, int municionInicial) {
        this.objId = objId;
        this.vida = vidaInicial;
        this.municion = municionInicial;
    }
    
    //copiar y pegar el mio

    public String getObjId() { 
    	return objId; 
    }
    
    public int getVida() { 
    	return vida; 
    }
    
    public int getMunicion() { 
    	return municion; 
    }
    
    public EstadoPosicion getPosicion() { 
    	return posicion; 
    }

    public void setPosicion(EstadoPosicion p) { 
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
