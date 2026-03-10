package com.example.droneserver;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class PortaDrones {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int vida;
    @Embedded
    private Position posicion; 
    private boolean bloqueado;
    @OneToMany(mappedBy = "portaDrones", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dron> drones = new ArrayList<>();
	private String idPartida;
	
	public PortaDrones() {}
    
	public PortaDrones(String partida, Position pos, int vid) {
		idPartida = partida;
		posicion = pos;
		vida = vid;
	}

    public PortaDrones(int vidaInicial) {
        this.vida = vidaInicial;
        this.bloqueado = false;
    }

    public int getVida() { 
    	return vida; 
    }
    
    public Position getPosicion() { 
    	return posicion; 
    }
    
    public boolean estaBloqueado() { 
    	return bloqueado; 
    }
    
    public String getIdPartida() {
		return idPartida;
	}
    
	
	public List<Dron> getDrones(){
		return drones;
	}
	
	

    // se llama una sola vez (fase colocación)
    public boolean colocar(Position p) {
        if (bloqueado) 
        	return false;
        if (p == null) 
        	return false;
        this.posicion = p;
        this.bloqueado = true;
        return true;
    }
    
    public void setDrones(List<Dron> drons ) {
		drones = drons;
	}

    public void aplicarDanio(int d) {
        if (d <= 0) return;
        vida = Math.max(0, vida - d);
    }

    public boolean estaMuerto() { 
    	return vida <= 0; 
    }
    
    public void guardarPortadron(DAOPortadron dao, DAODron daoD) {
    	String tipo = posicion.getTipo(); // "AEREO" o "NAVAL"
	    dao.delete(idPartida, tipo);
	    daoD.delete(tipo);  // borra por "AEREO" o "NAVAL"
	    dao.insert(this);
	    for (Dron dron : getDrones()) {
	        dron.guardarDron(daoD);
	    }
    }

    public PortaDrones levantarPortadrones(DAOPortadron dao, String tipo) {
    	return dao.find(idPartida, tipo);    	
    }

	public void setIdPartida(String codigo) {
		idPartida = codigo;		
	}
    
}
