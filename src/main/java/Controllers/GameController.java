package Controllers;

import org.springframework.web.bind.annotation.*;

import com.example.droneserver.*;

import java.util.*;

@RestController
@RequestMapping("/game")
public class GameController {

    private final Map<String, Sala> salas = new HashMap<>();

    // Crear partida (HOST = AÉREO)
    @GetMapping("/create")
    public JoinResponse CrearSala() {
        String sessionId = UUID.randomUUID().toString();

        Sala sala = new Sala();
        sala.CrearHost(sessionId);

        salas.put(sala.GetCodigo(), sala);

        return new JoinResponse(
                sala.GetCodigo(),
                sessionId,
                sala.GetCantidadJugadores()
        );
    }

    // Unirse a partida (JOIN = NAVAL)
    @GetMapping("/join/{codigo}")
    public JoinResponse UnirseSala(@PathVariable String codigo) {
        String sessionId = UUID.randomUUID().toString();

        Sala sala = salas.get(codigo);
        if (sala == null) {
            return new JoinResponse(codigo, sessionId, 0);
        }

        sala.CrearJoin(sessionId);

        return new JoinResponse(
                codigo,
                sessionId,
                sala.GetCantidadJugadores()
        );
    }

    // Colocar porta (una sola vez)
    @PostMapping("/placePorta/{codigo}")
    public String PlacePorta(@PathVariable String codigo,
                             @RequestBody Position pos) {

        Sala sala = salas.get(codigo);
        if (sala == null) 
        	return "Sala no existe.";

        if (pos == null || pos.sessionId == null || pos.objId == null) 
        	return "NO";

        Jugador jugador = sala.GetJugadorPorSession(pos.sessionId);
        if (jugador == null) 
        	return "NO";

        //  Validar que el objId que manda el cliente coincida con su tipo real
        String portaEsperado = jugador.getObjIdPorta();
        if (!portaEsperado.equals(pos.objId)) 
        	return "NO";

        EstadoPosicion ep = new EstadoPosicion(pos.x, pos.y, pos.z, pos.qx, pos.qy, pos.qz, pos.qw);
        return jugador.colocarPorta(ep) ? "OK" : "NO";
    }

    // Enviar posiciones de drones 
    @PostMapping("/moveBatch/{codigo}")
    public String MoveBatch(@PathVariable String codigo,
                            @RequestBody SolicitudMovimientoBatch req) {

        Sala sala = salas.get(codigo);
        if (sala == null) 
        	return "Sala no existe.";

        if (req == null || req.items == null) 
        	return "Sin items.";

        for (Position p : req.items) {
            if (p == null || p.sessionId == null || p.objId == null) 
            	continue;
            
            Jugador jugador = sala.GetJugadorPorSession(p.sessionId);
            if (jugador == null) 
            	continue;

            EstadoPosicion ep = new EstadoPosicion(p.x, p.y, p.z, p.qx, p.qy, p.qz, p.qw);

            // si llega un objId de porta por error, Jugador lo ignora igual
            jugador.actualizarPosicion(p.objId, ep);
        }

        return "OK";
    }

 // Estado del juego
    @GetMapping("/state/{codigo}")
    public Object State(@PathVariable String codigo) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe.";

        // avanzar proyectiles + aplicar daño
        TickProyectiles(sala, 0.1f);

        List<Position> posiciones = new ArrayList<>();
        List<DatoVida> vidas = new ArrayList<>();
        List<DatoMunicion> municion = new ArrayList<>();

        AgregarEstadoJugador(sala.GetHost(), posiciones, vidas, municion);
        AgregarEstadoJugador(sala.GetJoin(), posiciones, vidas, municion);

        // devolver proyectiles actuales para que el cliente los vea
        List<DatoProyectil> proyectiles = new ArrayList<>();
        if (sala.GetProyectiles() != null) {
            for (Proyectil p : sala.GetProyectiles()) {
                // si ya lo marcás inactivo, lo podés filtrar
                if (!p.activo) continue;

                DatoProyectil dp = new DatoProyectil();
                dp.id = p.id;
                dp.x = p.x;
                dp.y = p.y;
                dp.z = p.z;

                proyectiles.add(dp);
            }
        }

        RespuestaEstado resp = new RespuestaEstado();
        resp.posiciones = posiciones.toArray(new Position[0]);
        resp.vidas = vidas.toArray(new DatoVida[0]);
        resp.municion = municion.toArray(new DatoMunicion[0]);
        resp.proyectiles = proyectiles.toArray(new DatoProyectil[0]);

        return resp;
    }

    private void AgregarEstadoJugador(Jugador jugador, List<Position> posiciones, List<DatoVida> vidas, List<DatoMunicion> municion) {

        if (jugador == null) 
        	return;

        String sid = jugador.getSessionId();
        int slot = jugador.getSlot();

        String portaId = jugador.getObjIdPorta();

        // Porta: vida siempre
        vidas.add(new DatoVida(sid, portaId, jugador.getPorta().getVida()));

        // Porta: posición si ya fue colocado
        EstadoPosicion portaPos = jugador.getPorta().getPosicion();
        if (portaPos != null) posiciones.add(ToPosition(sid, slot, portaId, portaPos));

        // Drones
        for (Dron d : jugador.getDrones()) {
            vidas.add(new DatoVida(sid, d.getObjId(), d.getVida()));
            municion.add(new DatoMunicion(sid, d.getObjId(), d.getMunicion()));

            EstadoPosicion dp = d.getPosicion();
            if (dp != null) posiciones.add(ToPosition(sid, slot, d.getObjId(), dp));
        }
    }

    private Position ToPosition(String sessionId, int slot, String objId, EstadoPosicion ep) {
        Position p = new Position();
        p.sessionId = sessionId;
        p.slot = slot;
        p.objId = objId;

        p.x = ep.x; p.y = ep.y; p.z = ep.z;
        p.qx = ep.qx; p.qy = ep.qy; p.qz = ep.qz; p.qw = ep.qw;
        return p;
    }
    
    @PostMapping("/disparar/{codigo}")
    public String Disparar(@PathVariable String codigo,
                           @RequestBody SolicitudDisparo req) {

        Sala sala = salas.get(codigo);
        if (sala == null) return "Sala no existe.";
        if (req == null || req.sessionId == null) return "NO";

        // validar que el jugador exista
        Jugador atacante = sala.GetJugadorPorSession(req.sessionId);
        if (atacante == null) return "NO";

        Proyectil p = new Proyectil();
        p.id = java.util.UUID.randomUUID().toString().substring(0, 8);
        p.atacanteSessionId = req.sessionId;
        p.objIdDisparador = req.objIdDisparador;

        p.x = req.x; p.y = req.y; p.z = req.z;
        p.dx = req.dx; p.dy = req.dy; p.dz = req.dz;

        p.velocidad = (req.velocidad <= 0) ? 20f : req.velocidad;
        p.rangoMax = (req.rangoMax <= 0) ? 30f : req.rangoMax;
        p.danio = (req.danio <= 0) ? 1 : req.danio;

        sala.GetProyectiles().add(p);
        return "OK";
    }
    
    private void TickProyectiles(Sala sala, float dt) {

        if (sala == null) return;

        List<Proyectil> lista = sala.GetProyectiles();
        if (lista == null || lista.isEmpty()) return;

        for (Proyectil p : lista) {

            if (!p.activo) continue;

            float paso = p.velocidad * dt;

            //mover proyectil
            p.x += p.dx * paso;
            p.y += p.dy * paso;
            p.z += p.dz * paso;

            p.recorrido += paso;

            //buscar jugador enemigo
            Jugador host = sala.GetHost();
            Jugador join = sala.GetJoin();

            Jugador enemigo = null;

            if (host != null && !host.getSessionId().equals(p.atacanteSessionId))
                enemigo = host;

            if (join != null && !join.getSessionId().equals(p.atacanteSessionId))
                enemigo = join;

            if (enemigo == null) continue;

            // chequeo contra PORTA enemigo
            EstadoPosicion portaPos = enemigo.getPorta().getPosicion();

            if (portaPos != null && enemigo.getPorta().getVida() > 0) {

                float dist = distancia(p.x, p.y, p.z,
                                       portaPos.x, portaPos.y, portaPos.z);

                if (dist <= 1.0f) { // radio de impacto
                    enemigo.aplicarDanio(enemigo.getObjIdPorta(), p.danio);
                    p.activo = false;
                    continue;
                }
            }

            // chequeo contra DRONES enemigos
            for (Dron d : enemigo.getDrones()) {

                if (d.getVida() <= 0) continue;

                EstadoPosicion dp = d.getPosicion();
                if (dp == null) continue;

                float dist = distancia (p.x, p.y, p.z,
                                       dp.x, dp.y, dp.z);

                if (dist <= 0.7f) { // radio impacto dron
                    enemigo.aplicarDanio(d.getObjId(), p.danio);
                    p.activo = false;
                    break;
                }
            }
            //si no impactó y llegó al rango máximo
            if (p.activo && p.recorrido >= p.rangoMax) {
                p.activo = false;
            }
        }
        // limpiar proyectiles explotados
        lista.removeIf(pr -> !pr.activo);
    }
    
    private float distancia(float x1, float y1, float z1, float x2, float y2, float z2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;
		
		return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}