package com.example.droneserver;

import java.util.*;

public class Sala {

    private String codigo = UUID.randomUUID().toString().substring(0, 6);
    private List<String> jugadores = new ArrayList<>();

    //guarda la última posición de cada jugador (sessionId -> Position)
    private Map<String, Position> posiciones = new HashMap<>();

    public String GetCodigo() {
        return codigo;
    }

    public void AgregarJugador(String sessionId) {
        if (jugadores.size() < 2 && !jugadores.contains(sessionId)) {
            jugadores.add(sessionId);
        }
    }

    public boolean EstaLlena() {
        return jugadores.size() == 2;
    }

    public List<String> GetJugadores() {
        return jugadores;
    }

    //guardar posición del jugador
    public void ActualizarPosicion(String sessionId, Position pos) {
        posiciones.put(sessionId, pos);
    }

    //devolver todas las posiciones
    public Map<String, Position> GetPosiciones() {
        return posiciones;
    }
}
