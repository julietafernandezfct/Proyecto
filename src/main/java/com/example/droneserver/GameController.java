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
    public JoinResponse crearSala(HttpSession session) {
        Sala sala = new Sala();
        sala.agregarJugador(session.getId());

        salas.put(sala.getCodigo(), sala);

        return new JoinResponse(
                sala.getCodigo(),
                session.getId(),
                sala.getJugadores().size()
        );
    }


    // Unirse a partida existente
    @GetMapping("/join/{codigo}")
    public JoinResponse unirseSala(@PathVariable("codigo") String codigo,
                                   HttpSession session) {
        Sala sala = salas.get(codigo);
        if (sala == null) {
            return new JoinResponse(codigo, session.getId(), 0);
        }
        sala.agregarJugador(session.getId());
        return new JoinResponse(
                codigo,
                session.getId(),
                sala.getJugadores().size()
        );
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