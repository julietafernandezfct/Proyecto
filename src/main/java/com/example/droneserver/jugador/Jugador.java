package com.example.droneserver.jugador;

import java.util.HashMap;
import java.util.Map;

import com.example.droneserver.Dron;
import com.example.droneserver.EstadoPosicion;
import com.example.droneserver.PortaDrones;

public abstract class Jugador {

    //  IDs oficiales (compatibles con Unity)
    public static final String PORTA_AEREO = "PORTADRONAEREO";
    public static final String PORTA_NAVAL = "PORTADRONNAVAL";

    protected final String sessionId;
    protected final int slot; // 1 host, 2 join

    protected final PortaDrones porta;
    protected final Dron[] drones;

    protected final Map<String, Dron> dronesPorId = new HashMap<>();

    protected Jugador(String sessionId, int slot, PortaDrones porta, Dron[] drones) {
        this.sessionId = sessionId;
        this.slot = slot;
        this.porta = porta;
        this.drones = drones;

        if (drones != null) {
            for (Dron d : drones) dronesPorId.put(d.getObjId(), d);
        }
    }

    public String getSessionId() { 
    	return sessionId; 
    }
    
    public int getSlot() { 
    	return slot; 
    }
    
    public PortaDrones getPorta() { 
    	return porta; 
    }
    
    public Dron[] getDrones() { 
    	return drones; 
    }

    public String getObjIdPorta() {
        return (this instanceof JugadorAereo) ? PORTA_AEREO : PORTA_NAVAL;
    }

    private boolean esPorta(String objId) {
        return PORTA_AEREO.equals(objId) || PORTA_NAVAL.equals(objId);
    }

    // Fase inicial: colocar porta una sola vez
    public boolean colocarPorta(EstadoPosicion p) {
        return porta.colocar(p);
    }

    // Actualizar posiciones 
    public void actualizarPosicion(String objId, EstadoPosicion p) {
        if (p == null || objId == null) return;

        if (esPorta(objId)) {
            return; // porta solo se coloca con /placePorta
        }

        Dron d = dronesPorId.get(objId);
        if (d != null) 
        	d.setPosicion(p);
    }

    // Aplicar daño a porta o dron
    public boolean aplicarDanio(String objId, int danio) {
        if (objId == null || danio <= 0) return false;

        if (esPorta(objId)) {
            porta.aplicarDanio(danio);
            return true;
        }

        Dron d = dronesPorId.get(objId);
        if (d == null) return false;

        d.aplicarDanio(danio);
        return true;
    }

  
    public int getVidaDe(String objId) {
        if (objId == null) 
        	return -1;

        if (esPorta(objId)) 
        	return porta.getVida();

        Dron d = dronesPorId.get(objId);
        if (d == null) 
        	return -1;
        
        return d.getVida();
    }
}