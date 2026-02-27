package com.example.droneserver;

import java.util.UUID;

import com.example.droneserver.jugador.Jugador;
import com.example.droneserver.jugador.JugadorAereo;
import com.example.droneserver.jugador.JugadorNaval;

public class Sala {

    private final String codigo = UUID.randomUUID().toString().substring(0, 6);

    private Jugador host; // aéreo
    private Jugador join; // naval
    
    private final java.util.List<Proyectil> proyectiles = new java.util.ArrayList<>();

    public java.util.List<Proyectil> GetProyectiles() {
        return proyectiles;
    }

    public String GetCodigo() { 
    	return codigo; }

    public int GetCantidadJugadores() {
        int c = 0;
        if (host != null) 
        	c++;
        if (join != null) 
        	c++;
        return c;
    }

    public boolean EstaLlena() { 
    	return host != null && join != null; 
    }

    public void CrearHost(String sessionId) {
        if (host == null) 
        	host = new JugadorAereo(sessionId, 1);
    }

    public void CrearJoin(String sessionId) {
        if (join == null) 
        	join = new JugadorNaval(sessionId, 2);
    }

    public Jugador GetHost() { 
    	return host; 
    }
    
    public Jugador GetJoin() { 
    	return join; 
    }

    public Jugador GetJugadorPorSession(String sessionId) {
        if (sessionId == null) 
        	return null;
        if (host != null && host.getSessionId().equals(sessionId)) 
        	return host;
        if (join != null && join.getSessionId().equals(sessionId)) 
        	return join;
        return null;
    }
}