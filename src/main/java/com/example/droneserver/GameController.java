package com.example.droneserver;

import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {

    private Map<String, Sala> salas = new HashMap<>();

    // Crear partida
    @GetMapping("/create")
    public String crearSala(HttpSession session) {
        Sala sala = new Sala();
        sala.agregarJugador(session.getId());

        salas.put(sala.getCodigo(), sala);

        return "Sala creada. Código: " + sala.getCodigo();
    }

    // Unirse a partida existente
    @GetMapping("/join/{codigo}")
    public String unirseSala(@PathVariable("codigo") String codigo, HttpSession session) {

        Sala sala = salas.get(codigo);

        if (sala == null) {
            return "Sala no existe.";
        }

        sala.agregarJugador(session.getId());

        if (sala.estaLlena()) {
            return "Sala lista. Jugadores: " + sala.getJugadores();
        } else {
            return "Unido a sala. Esperando segundo jugador...";
        }
    }
    
    @PostMapping("/move/{codigo}")
    public String mover(@PathVariable("codigo") String codigo,
                        @RequestBody Position pos,
                        HttpSession session) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe.";

        sala.actualizarPosicion(session.getId(), pos);

        return "OK";
    }
    
    @GetMapping("/state/{codigo}")
    public Object state(@PathVariable("codigo") String codigo) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe.";

        return sala.getPosiciones();
    }


}