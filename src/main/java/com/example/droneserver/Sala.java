package com.example.droneserver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Sala {

    private String codigo = UUID.randomUUID().toString().substring(0, 6);
    private List<String> jugadores = new ArrayList<>();

    public String getCodigo() {
        return codigo;
    }

    public void agregarJugador(String sessionId) {
        if (jugadores.size() < 2 && !jugadores.contains(sessionId)) {
            jugadores.add(sessionId);
        }
    }

    public boolean estaLlena() {
        return jugadores.size() == 2;
    }

    public List<String> getJugadores() {
        return jugadores;
    }
}