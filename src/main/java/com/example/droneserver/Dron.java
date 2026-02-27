package com.example.droneserver;

//SACAR Y PASAR LO QUE FALTA A LA CLASE EN EL PACKAGE PERSISTENCIA
public class Dron {
    private int codigo;   //pasa a int
    private String codPort;
	private String tipo;
    private int vida;
    private int municion;
    private Position posicion; // null hasta que llegue update

    
    public Dron(int codigo, int vidaInicial, int municionInicial) {
        this.codigo = codigo;
        this.vida = vidaInicial;
        this.municion = municionInicial;
    }
    
    public Dron(int cod, String codP, String tip, int mun, Position posi, int vid) {
		codigo = cod;
		codPort = codP;
		municion = mun;
		tipo = tip;
		posicion = posi;
		vida = vid;
	}

    public int codigo() { 
    	return codigo; 
    }
    
    public String getCodPort() {
		return codPort;
	}
    
    public String getTipo() {
		return tipo;
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
