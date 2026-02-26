package Controllers;

import org.springframework.web.bind.annotation.*;
import com.example.droneserver.*;

import java.util.*;

@RestController
@RequestMapping("/game")
public class GameController {

    private final Map<String, Sala> salas = new HashMap<>();

    @GetMapping("/create")
    public JoinResponse crearSala() {

        String sessionId = UUID.randomUUID().toString();

        Sala sala = new Sala();
        sala.agregarJugador(sessionId, true);

        salas.put(sala.obtenerCodigo(), sala);

        return new JoinResponse(
                sala.obtenerCodigo(),
                sessionId,
                sala.obtenerJugadores().size()
        );
    }

    @GetMapping("/join/{codigo}")
    public JoinResponse unirseSala(@PathVariable String codigo) {

        String sessionId = UUID.randomUUID().toString();

        Sala sala = salas.get(codigo);
        if (sala == null) {
            return new JoinResponse(codigo, sessionId, 0);
        }

        sala.agregarJugador(sessionId, false);

        return new JoinResponse(
                codigo,
                sessionId,
                sala.obtenerJugadores().size()
        );
    }

    @PostMapping("/placePorta/{codigo}")
    public String colocarPorta(@PathVariable String codigo,
                               @RequestBody Position posicion) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe";

        return sala.colocarPorta(posicion) ? "OK" : "NO";
    }

    @PostMapping("/moveBatch/{codigo}")
    public String moverBatch(@PathVariable String codigo,
                             @RequestBody SolicitudMovimientoBatch solicitud) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe";

        if (solicitud == null || solicitud.items == null) return "Sin datos";

        for (Position p : solicitud.items) {
            sala.actualizarPosicion(p);
        }

        return "OK";
    }

    @GetMapping("/state/{codigo}")
    public Object obtenerEstado(@PathVariable String codigo) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe";

        List<Position> listaPosiciones = new ArrayList<>();
        for (var mapa : sala.obtenerPosiciones().values()) {
            listaPosiciones.addAll(mapa.values());
        }

        List<DatoVida> listaVidas = new ArrayList<>();
        for (var entry : sala.obtenerVidas().entrySet()) {
            for (var e : entry.getValue().entrySet()) {
                listaVidas.add(new DatoVida(entry.getKey(), e.getKey(), e.getValue()));
            }
        }

        List<DatoMunicion> listaMunicion = new ArrayList<>();
        for (var entry : sala.obtenerMuniciones().entrySet()) {
            for (var e : entry.getValue().entrySet()) {
                listaMunicion.add(new DatoMunicion(entry.getKey(), e.getKey(), e.getValue()));
            }
        }

        RespuestaEstado respuesta = new RespuestaEstado();
        respuesta.posiciones = listaPosiciones.toArray(new Position[0]);
        respuesta.vidas = listaVidas.toArray(new DatoVida[0]);
        respuesta.municion = listaMunicion.toArray(new DatoMunicion[0]);
        respuesta.proyectiles = new DatoProyectil[0];

        return respuesta;
    }
}