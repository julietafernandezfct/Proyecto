package com.example.droneserver;

public class PortaDrones {
    private int vida;
    private EstadoPosicion posicion; // null hasta que se coloque
    private boolean bloqueado;

    public PortaDrones(int vidaInicial) {
        this.vida = vidaInicial;
        this.bloqueado = false;
    }

    public int getVida() { 
    	return vida; 
    }
    
    public EstadoPosicion getPosicion() { 
    	return posicion; 
    }
    
    public boolean estaBloqueado() { 
    	return bloqueado; 
    }

    // se llama una sola vez (fase colocación)
    public boolean colocar(EstadoPosicion p) {
        if (bloqueado) 
        	return false;
        if (p == null) 
        	return false;
        this.posicion = p;
        this.bloqueado = true;
        return true;
    }

    public void aplicarDanio(int d) {
        if (d <= 0) return;
        vida = Math.max(0, vida - d);
    }

    public boolean estaMuerto() { 
    	return vida <= 0; 
    }
}
