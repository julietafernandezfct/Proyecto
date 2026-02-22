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
    public JoinResponse CrearSala(HttpSession session) {
        Sala sala = new Sala();
        sala.AgregarJugador(session.getId());

        salas.put(sala.GetCodigo(), sala);

        return new JoinResponse(
                sala.GetCodigo(),
                session.getId(),
                sala.GetJugadores().size()
        );
    }


    // Unirse a partida existente
    @GetMapping("/join/{codigo}")
    public JoinResponse UnirseSala(@PathVariable("codigo") String codigo,
                                   HttpSession session) {
        Sala sala = salas.get(codigo);
        if (sala == null) {
            return new JoinResponse(codigo, session.getId(), 0);
        }
        sala.AgregarJugador(session.getId());
        return new JoinResponse(
                codigo,
                session.getId(),
                sala.GetJugadores().size()
        );
    }

    
    @PostMapping("/move/{codigo}")
    public String Mover(@PathVariable("codigo") String codigo,
                        @RequestBody Position pos,
                        HttpSession session) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe.";

        sala.ActualizarPosicion(session.getId(), pos);

        return "OK";
    }
    
    @GetMapping("/state/{codigo}")
    public Object State(@PathVariable("codigo") String codigo) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe.";

        return sala.GetPosiciones();
    }
    
}