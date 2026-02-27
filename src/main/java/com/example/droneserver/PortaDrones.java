package com.example.droneserver;

import java.util.List;

public class PortaDrones {
    private int vida;
    private Position posicion; 
    private boolean bloqueado;
    private String tipo;
	private List<Dron> drones;
	private String idPartida;
    
	public PortaDrones(String partida, Position pos, int vid, String tip) {
		idPartida = partida;
		posicion = pos;
		vida = vid;
		tipo = tip;
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
    
    public String getTipo() {
		return tipo;
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
    
}
