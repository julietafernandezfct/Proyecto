package com.example.droneserver;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

//SACAR Y PASAR LO QUE FALTA A LA CLASE EN EL PACKAGE PERSISTENCIA
@Entity
public class Dron {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "porta_id")
    private PortaDrones portaDrones;
	
    private int codigo;   //pasa a int
    private String codPort;
    private int vida;
    private int municion;
    private Position posicion; // null hasta que llegue update
    

    
    public Dron(int codigo, int vidaInicial, int municionInicial) {
        this.codigo = codigo;
        this.vida = vidaInicial;
        this.municion = municionInicial;
    }
    
    public Dron(int cod, String codP, int mun, Position posi, int vid) {
		codigo = cod;
		codPort = codP;
		municion = mun;
		posicion = posi;
		vida = vid;
	}

    public int codigo() { 
    	return codigo; 
    }
    
    public String getCodPort() {
		return codPort;
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
    
    public void guardarDron(DAODron dao) {
    	dao.insback(this);
    }
    
    public List<Dron> levantarDrones(DAODron dao){
    	return dao.ListarDrones(codPort);
    }
    
    public void setCodPort(String cod) {
        this.codPort = cod;
    }
}
