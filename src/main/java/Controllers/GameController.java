package Controllers;

import org.springframework.web.bind.annotation.*;

import com.example.droneserver.JoinResponse;
import com.example.droneserver.Position;
import com.example.droneserver.Sala;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/game")

public class GameController {

    private Map<String, Sala> salas = new HashMap<>();

    // Crear partida
    @GetMapping("/create")
    public JoinResponse CrearSala() {
    	//se asigna un sessionId creado por nosotros
    	String sessionId = UUID.randomUUID().toString();
        Sala sala = new Sala();
        sala.AgregarJugador(sessionId);

        salas.put(sala.GetCodigo(), sala);

        return new JoinResponse(
                sala.GetCodigo(),
                sessionId,
                sala.GetJugadores().size()
        );
    }


    // Unirse a partida existente
    @GetMapping("/join/{codigo}")
    public JoinResponse UnirseSala(@PathVariable String codigo) {
    	String sessionId = UUID.randomUUID().toString();
        Sala sala = salas.get(codigo);
        if (sala == null) {
            return new JoinResponse(codigo, sessionId, 0);
        }
        sala.AgregarJugador(sessionId);
        return new JoinResponse(
                codigo,
                sessionId,
                sala.GetJugadores().size()
        );
    }

    
    @PostMapping("/move/{codigo}")
    public String Mover(@PathVariable("codigo") String codigo,
                        @RequestBody Position pos) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe.";

        sala.ActualizarPosicion(pos.sessionId, pos);

        return "OK";
    }
    
    @GetMapping("/state/{codigo}")
    public Object State(@PathVariable("codigo") String codigo) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe.";

        return sala.GetPosiciones();
    }
    
}