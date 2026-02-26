package com.example.droneserver;

import java.util.*;

public class Sala {

    private final String codigo = UUID.randomUUID().toString().substring(0, 6);

    private final List<String> jugadores = new ArrayList<>();

    // sessionId -> (objId -> Posicion)
    private final Map<String, Map<String, Position>> posiciones = new HashMap<>();

    // sessionId -> (objId -> vida)
    private final Map<String, Map<String, Integer>> vidas = new HashMap<>();

    // sessionId -> (objId -> municion)
    private final Map<String, Map<String, Integer>> municiones = new HashMap<>();

    private final Map<String, Boolean> portaBloqueado = new HashMap<>();

    public String obtenerCodigo() {
        return codigo;
    }

    public List<String> obtenerJugadores() {
        return jugadores;
    }

    public void agregarJugador(String sessionId, boolean esHost) {

        if (jugadores.size() >= 2) return;
        if (jugadores.contains(sessionId)) return;

        jugadores.add(sessionId);

        posiciones.put(sessionId, new HashMap<>());
        vidas.put(sessionId, new HashMap<>());
        municiones.put(sessionId, new HashMap<>());
        portaBloqueado.put(sessionId, false);

        if (esHost) {
            // AÉREO
            vidas.get(sessionId).put("PORTA", 6);

            for (int i = 1; i <= 12; i++) {
                String id = "DRON_" + i;
                vidas.get(sessionId).put(id, 1);
                municiones.get(sessionId).put(id, 1);
            }
        } else {
            // NAVAL
            vidas.get(sessionId).put("PORTA", 3);

            for (int i = 1; i <= 6; i++) {
                String id = "DRON_" + i;
                vidas.get(sessionId).put(id, 2);
                municiones.get(sessionId).put(id, 2);
            }
        }
    }

    public boolean colocarPorta(Position posicion) {

        if (!"PORTA".equals(posicion.objId)) return false;

        Boolean bloqueado = portaBloqueado.get(posicion.sessionId);
        if (bloqueado == null || bloqueado) return false;

        posiciones.get(posicion.sessionId).put("PORTA", posicion);
        portaBloqueado.put(posicion.sessionId, true);
        return true;
    }

    public void actualizarPosicion(Position posicion) {

        if (posicion == null) return;

        if ("PORTA".equals(posicion.objId)) {
            Boolean bloqueado = portaBloqueado.get(posicion.sessionId);
            if (bloqueado != null && bloqueado) return;
        }

        posiciones.get(posicion.sessionId).put(posicion.objId, posicion);
    }

    public Map<String, Map<String, Position>> obtenerPosiciones() {
        return posiciones;
    }

    public Map<String, Map<String, Integer>> obtenerVidas() {
        return vidas;
    }

    public Map<String, Map<String, Integer>> obtenerMuniciones() {
        return municiones;
    }
}