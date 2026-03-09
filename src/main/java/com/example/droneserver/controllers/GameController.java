package com.example.droneserver.controllers;

import org.springframework.web.bind.annotation.*;
import com.example.droneserver.*;
import com.example.droneserver.datos.DatoMunicion;
import com.example.droneserver.datos.DatoProyectil;
import com.example.droneserver.datos.DatoVida;
import com.example.droneserver.jugador.Jugador;
import com.example.droneserver.solicitudes.SolicitudDisparo;
import com.example.droneserver.solicitudes.SolicitudMovimientoBatch;
import com.example.droneserver.solicitudes.SolicitudRecarga;

import java.util.*;

@RestController

@RequestMapping("/game")
public class GameController {
	private final DAOPortadron daoPorta;
	private final DAODron daoDron;
	private final Map<String, Sala> salas = new HashMap<>();
	
	public GameController(DAOPortadron daoPorta, DAODron daoD) {
        this.daoPorta = daoPorta;
        this.daoDron = daoD;
    }

	@GetMapping("/create")
	public JoinResponse CrearSala() {
	    System.out.println("ENTRE A CREATE !!!");

	    String sessionId = UUID.randomUUID().toString();
	    Sala sala = new Sala();
	    sala.CrearHost(sessionId);
	    salas.put(sala.GetCodigo(), sala);
	    
	    Jugador host = sala.GetHost();

	    return new JoinResponse(
	        sala.GetCodigo(),
	        sessionId,
	        sala.GetCantidadJugadores(),
	        host.getObjIdPorta(),
	        host.getDronesIds()
	    );
	}
		
	@GetMapping("/join/{codigo}")
	public JoinResponse UnirseSala(@PathVariable String codigo) {
		String sessionId = UUID.randomUUID().toString();
		Sala sala = salas.get(codigo);
		
		if (sala == null) {
			return new JoinResponse(codigo, sessionId, 0, 0, null);
		}
		sala.CrearJoin(sessionId);
		System.out.println("SALAS ACTUALES: " + salas.keySet());
		Jugador join = sala.GetJoin();
		
		try {
	        if (daoPorta.member(codigo, "NAVAL")) {
	            PortaDrones portaJoin = daoPorta.find(codigo, "NAVAL");
	            join.setPorta(portaJoin);
	            RestaurarEstadoJugador(join, portaJoin);
	        }
	    } catch (Exception e) {
	        System.out.println("No hay partida guardada para este código, join normal: " + e.getMessage());
	    }


		return new JoinResponse(
		    codigo,
		    sessionId,
		    sala.GetCantidadJugadores(),
		    join.getObjIdPorta(),
		    join.getDronesIds()
		);

	}
	
	//bloquea el portadron cuando es colocado en el mapa
	@PostMapping("/placePorta/{codigo}")
	public String PlacePorta(@PathVariable String codigo, @RequestBody Position pos) {
		Sala sala = salas.get(codigo);
		if (sala == null)
			return "Sala no existe.";
		if (pos == null || pos.sessionId == null)
			return "NO";
		Jugador jugador = sala.GetJugadorPorSession(pos.sessionId);
		if (jugador == null)
			return "NO";
		// validar objId porta
		int portaEsperado = jugador.getObjIdPorta();
		if (portaEsperado != pos.objId)
			return "NO";
		pos.sessionId = jugador.getSessionId();
		pos.slot = jugador.getSlot();
		pos.objId = portaEsperado;
		System.out.println("fijado el porta" );
		return jugador.colocarPorta(pos) ? "OK" : "NO";
	}
	
	//manda la posicion de todos los drones
	@PostMapping("/moveBatch/{codigo}")
	public String MoveBatch(@PathVariable String codigo, @RequestBody
		SolicitudMovimientoBatch req) {
		Sala sala = salas.get(codigo);
		if (sala == null)
			return "Sala no existe.";
		if (req == null || req.items == null)
			return "Sin items.";
		for (Position p : req.items) {
			if (p == null || p.sessionId == null)
				continue;
			Jugador jugador = sala.GetJugadorPorSession(p.sessionId);
			if (jugador == null)
				continue;
			p.sessionId = jugador.getSessionId();
			p.slot = jugador.getSlot();
			p.tipo = jugador.getTipo();
			jugador.actualizarPosicion(p.objId, p);
		}
		return "OK";
	}
	
	@GetMapping("/state/{codigo}")
	public Object State(@PathVariable String codigo) {
		Sala sala = salas.get(codigo);
		if (sala == null) 
			return "Sala no existe.";
		TickProyectiles(sala, 0.1f);
		List<Position> posiciones = new ArrayList<>();
		List<DatoVida> vidas = new ArrayList<>();
		List<DatoMunicion> municion = new ArrayList<>();
		AgregarEstadoJugador(sala.GetHost(), posiciones, vidas, municion);
		AgregarEstadoJugador(sala.GetJoin(), posiciones, vidas, municion);
		List<DatoProyectil> proyectiles = new ArrayList<>();
		if (sala.GetProyectiles() != null) {
			for (Proyectil p : sala.GetProyectiles()) {
				if (!p.activo) 
					continue;
				DatoProyectil dp = new DatoProyectil();
				dp.id = p.id;
				dp.sessionId = p.atacanteSessionId;
				dp.x = p.x; dp.y = p.y; dp.z = p.z;
				dp.dx = p.dx; dp.dy = p.dy; dp.dz = p.dz;   
				dp.velocidad = p.velocidad; 
				proyectiles.add(dp);
			}
		}
		RespuestaEstado resp = new RespuestaEstado();
		ActualizarResultado(sala);
		resp.resultado = sala.resultado;
		resp.posiciones = posiciones.toArray(new Position[0]);
		resp.vidas = vidas.toArray(new DatoVida[0]);
		resp.municion = municion.toArray(new DatoMunicion[0]);
		resp.proyectiles = proyectiles.toArray(new DatoProyectil[0]);
		return resp;
	}
	
	//guarda todos los datos de un jugador
	private void AgregarEstadoJugador(Jugador jugador,List<Position>posiciones,List<DatoVida> vidas,List<DatoMunicion> municion) {
		if (jugador == null) 
			return;
		String sid = jugador.getSessionId();
		int slot = jugador.getSlot();
		int portaId = jugador.getObjIdPorta();
		vidas.add(new DatoVida(portaId,  jugador.getPortaVida()));
		Position portaPos = jugador.getPortaPosicion();
		if (portaPos != null) {
			portaPos.sessionId = sid;
			portaPos.slot = slot;
			portaPos.objId = portaId;
			portaPos.tipo = "PORTA";
			posiciones.add(portaPos);
			}
		Position[] drones = jugador.getDronesPos();
		int[] v = jugador.getVidas();
		for (int i = 0; i < drones.length; i++) {
		    Position p = drones[i];
		    if (p == null) continue;  // ← skip undeployed drones
		    vidas.add(new DatoVida(p.objId, v[i]));
		    municion.add(new DatoMunicion(sid, p.objId, 0));
		    p.sessionId = sid;
		    p.slot = slot;
		    p.tipo = jugador.getTipo();
		    posiciones.add(p);
		}
	}
	
	
	@PostMapping("/disparar/{codigo}")
	public String Disparar(@PathVariable String codigo, @RequestBody SolicitudDisparo req)
	{
		Sala sala = salas.get(codigo);
		if (sala == null)
			return "Sala no existe.";
		if (req == null || req.sessionId == null)
			return "NO";
		Jugador atacante = sala.GetJugadorPorSession(req.sessionId);
		if (atacante == null)
			return "NO";
		Proyectil p = new Proyectil();
		//esta medio tosco esto
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
	
	//hace que el proyectil avance
	private void TickProyectiles(Sala sala, float dt) {
		if (sala == null) return;
		List<Proyectil> lista = sala.GetProyectiles();
		if (lista == null || lista.isEmpty()) return;
		for (Proyectil p : lista) {
			if (!p.activo)
				continue;
			float paso = p.velocidad * dt;
			p.x += p.dx * paso;
			p.y += p.dy * paso;
			p.z += p.dz * paso;
			p.recorrido += paso;
			Jugador host = sala.GetHost();
			Jugador join = sala.GetJoin();
			Jugador enemigo = null;
			if (host != null && !host.getSessionId().equals(p.atacanteSessionId))
				enemigo = host;
			if (join != null && !join.getSessionId().equals(p.atacanteSessionId))
				enemigo = join;
			if (enemigo == null)
				continue;
			//reviso si le pego al portadron enemigo
			Position portaPos = enemigo.getPortaPosicion();
			if (portaPos != null && enemigo.getPortaVida() > 0) {
				float dist = distancia(p.x, p.y, p.z, portaPos.x, portaPos.y, portaPos.z);
				if (dist <= 4.0f) {
					enemigo.aplicarDanio(enemigo.getObjIdPorta(), p.danio);
					p.activo = false;
					continue;
				}
			}
			//reviso si le pegué al dron
			Position[] drones = enemigo.getDronesPos();
			int[] vidas = enemigo.getVidas();
			for (int i = 0; i < drones.length; i++) {
				if (vidas[i] <= 0)
					continue;
				Position dp = drones[i];
				if (dp == null)
					continue;
				float dist = distancia(p.x, p.y, p.z, dp.x, dp.y, dp.z);
				if (dist <= 0.7f) {
					enemigo.aplicarDanio(dp.objId, p.danio);
					p.activo = false;
					break;
				}
			}
			if (p.activo && p.recorrido >= p.rangoMax) {
				p.activo = false;
			}
		}
		lista.removeIf(pr -> !pr.activo);
	}
	
	private float distancia(float x1, float y1, float z1, float x2, float y2, float z2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;
		return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	
	@PostMapping("/recargar/{codigo}")
	public String Recargar(@PathVariable String codigo, @RequestBody SolicitudRecarga req) {
	    Sala sala = salas.get(codigo);
	    if (sala == null) return "Sala no existe.";
	    if (req == null || req.sessionId == null) return "NO";

	    Jugador jugador = sala.GetJugadorPorSession(req.sessionId);
	    if (jugador == null) return "NO";
	    
	    //agarro el dron
	    int objId = req.objIdDisparador;
	    if (objId <= 0) return "NO";
	    
	    Position dp = jugador.getDronPorObjId(objId);
	    if (dp == null) return "NO";

	    //veo a que portadron tiene que apuntar
	    Position portaPos = jugador.getPortaPosicion();
	    if (portaPos == null ) {
	    	return "NO";
	    }else if(portaPos.objId == 0 && (dp.objId < 8 || dp.objId > 19)) { //si porta es aereo, dron id [8 ... 19]
	    	return "NO";
	    }else if(portaPos.objId == 1 && (dp.objId < 2 || dp.objId > 7)) { //si porta es naval, dron id [2 ... 7]
	    	return "NO";
	    }	    

	    if (distanciaRecarga(portaPos.x, portaPos.y, portaPos.z, dp.x, dp.y, dp.z)) {
	        jugador.recargaMunicion(objId);
	    }

	    return "OK";
	}
	
	//calcula como si fuera una esfera al rededor del portadron
	public boolean distanciaRecarga(float px, float py, float pz, float x, float y, float z) {
		float dx = px - x;
	    float dy = py - y;
	    float dz = pz - z;

	    float distanciaCuadrada = dx*dx + dy*dy + dz*dz;
	    float radio = 5f; 

	    return distanciaCuadrada <= radio * radio;
	}
	
	//GUARDAR PARTIDA
	@PostMapping("/save/{codigo}")
	public String guardarPartida(@PathVariable String codigo) {
	    Sala sala = salas.get(codigo);
	    if (sala == null) return "Sala no existe.";

	    Jugador host = sala.GetHost();
	    Jugador join = sala.GetJoin();

	    System.out.println("=== GUARDANDO: " + codigo + " ===");

	    guardarJugador(host, codigo);
	    guardarJugador(join, codigo);

	    return "PARTIDA GUARDADA";
	}

	private void guardarJugador(Jugador jugador, String codigo) {
	    if (jugador == null) return;

	    Position portaPos = jugador.getPortaPosicion();
	    if (portaPos == null) {
	        System.out.println("Jugador slot=" + jugador.getSlot() + " no tiene porta colocada, no se guarda");
	        return;
	    }

	    // Construir PortaDrones desde el estado actual
	    PortaDrones porta = new PortaDrones(jugador.getPortaVida());
	    porta.setIdPartida(codigo);
	    portaPos.tipo = jugador.getTipo();
	    porta.colocar(portaPos);
	    System.out.println("Porta bloqueado: " + porta.estaBloqueado() + 
	        " posicion: " + porta.getPosicion());

	    // Agregar drones vivos
	    Position[] dronesPos = jugador.getDronesPos();
	    int[] vidas = jugador.getVidas();
	    int baseId = (jugador.getSlot() == 1) ? 8 : 2;

	    for (int i = 0; i < dronesPos.length; i++) {
	        if (dronesPos[i] == null) continue;
	        if (vidas[i] <= 0) continue;
	        Dron dron = new Dron(dronesPos[i].objId, codigo, 3, dronesPos[i], vidas[i]);
	        porta.getDrones().add(dron);
	    }

	    System.out.println("Guardando jugador slot=" + jugador.getSlot() + 
	        " porta=" + portaPos + " drones=" + porta.getDrones().size());

	    porta.guardarPortadron(daoPorta, daoDron);
	}
	
	//crea la partida vieja
	@GetMapping("/loadAndCreate/{codigoGuardado}")
	public JoinResponse loadAndCreate(@PathVariable String codigoGuardado) {
	    String sessionIdHost = UUID.randomUUID().toString();
	    Sala sala = new Sala();
	    sala.CrearHost(sessionIdHost);
	    salas.put(codigoGuardado, sala);

	    Jugador host = sala.GetHost();

	    // objId 0 = AEREO = host
	    try {
	        if (daoPorta.member(codigoGuardado, "AEREO")) {
	            PortaDrones portaHost = daoPorta.find(codigoGuardado, "AEREO");
	            host.setPorta(portaHost);
	            RestaurarEstadoJugador(host, portaHost);
	        }
	    } catch (Exception e) {
	        System.out.println("No hay estado guardado para AEREO: " + e.getMessage());
	    }

	    return new JoinResponse(
	        codigoGuardado,
	        sessionIdHost,
	        sala.GetCantidadJugadores(),
	        host.getObjIdPorta(),
	        host.getDronesIds()
	    );
	}
	
	//carga la partida guardada, ya tiene que haber sido creada
	@GetMapping("/load/{codigo}")
	public String cargarPartida(@PathVariable String codigo) {

	    Sala sala = salas.get(codigo);
	    if (sala == null)
	        return "Sala no existe.";

	    Jugador host = sala.GetHost();
	    Jugador join = sala.GetJoin();

	    if (host != null) {
	        PortaDrones portaHost = host.getPorta().levantarPortadrones(daoPorta, "AEREO");
	        host.setPorta(portaHost);
	    }

	    if (join != null) {
	        PortaDrones portaJoin = join.getPorta().levantarPortadrones(daoPorta, "NAVAL");
	        join.setPorta(portaJoin);
	    }

	    return "PARTIDA CARGADA";
	}
	
	private void ActualizarResultado(Sala sala) {
	    Jugador host = sala.GetHost();
	    Jugador join = sala.GetJoin();
	    if (host == null || join == null) return;
	    if (sala.resultado.estado.equals("VICTORIA_HOST") || 
	        sala.resultado.estado.equals("VICTORIA_JOIN") || 
	        sala.resultado.estado.equals("EMPATE")) return;

	    boolean portaHostMuerto = host.getPortaVida() <= 0;
	    boolean portaJoinMuerto = join.getPortaVida() <= 0;
	    boolean dronesHostMuertos = TodosLosDronesMuertos(host);
	    boolean dronesJoinMuertos = TodosLosDronesMuertos(join);

	    // victoria directa — todos los drones muertos
	    if (dronesHostMuertos && !portaJoinMuerto && !portaHostMuerto) {
	        sala.resultado = new ResultadoPartida("VICTORIA_JOIN", 0, 0);
	        return;
	    }
	    if (dronesJoinMuertos && !portaHostMuerto && !portaJoinMuerto) {
	        sala.resultado = new ResultadoPartida("VICTORIA_HOST", 0, 0);
	        return;
	    }

	    // porta muerto — arranca cuenta regresiva solo una vez
	    if ((portaHostMuerto || portaJoinMuerto) && sala.tiempoMuertePorta == -1) {
	        sala.tiempoMuertePorta = System.currentTimeMillis();
	        System.out.println("Cuenta regresiva iniciada: " + sala.tiempoMuertePorta);
	    }

	    if (sala.tiempoMuertePorta != -1) {
	        long elapsed = (System.currentTimeMillis() - sala.tiempoMuertePorta) / 1000;
	        int restantes = (int)(10 - elapsed);
	        System.out.println("Elapsed: " + elapsed + " Restantes: " + restantes);

	        if (restantes > 0) {
	            // durante la cuenta — el equipo sin porta puede empatar
	            if (portaHostMuerto && (dronesJoinMuertos || portaJoinMuerto)) {
	                sala.resultado = new ResultadoPartida("EMPATE", 0, 0);
	                return;
	            }
	            if (portaJoinMuerto && (dronesHostMuertos || portaHostMuerto)) {
	                sala.resultado = new ResultadoPartida("EMPATE", 0, 0);
	                return;
	            }

	            if (portaHostMuerto)
	                sala.resultado = new ResultadoPartida("CUENTA_REGRESIVA", restantes, 1);
	            if (portaJoinMuerto)
	                sala.resultado = new ResultadoPartida("CUENTA_REGRESIVA", restantes, 2);

	        } else {
	            // se acabó el tiempo
	            if (portaHostMuerto) sala.resultado = new ResultadoPartida("VICTORIA_JOIN", 0, 1);
	            if (portaJoinMuerto) sala.resultado = new ResultadoPartida("VICTORIA_HOST", 0, 2);
	        }
	    }
	}
	
	private boolean TodosLosDronesMuertos(Jugador j) {
	    int[] vidas = j.getVidas();
	    for (int v : vidas) if (v > 0) return false;
	    return true;
	}
	
	private void RestaurarEstadoJugador(Jugador jugador, PortaDrones porta) {
	    // Restaurar posición del porta
	    Position portaPos = porta.getPosicion();
	    if (portaPos != null) {
	        portaPos.objId = jugador.getObjIdPorta();
	        portaPos.sessionId = jugador.getSessionId();
	        portaPos.slot = jugador.getSlot();
	        portaPos.tipo = "PORTA";
	        jugador.setPortaPos(portaPos);
	        jugador.colocarPorta(portaPos);
	    }

	    // Restaurar drones
	    List<Dron> drones = porta.getDrones();
	    if (drones == null || drones.isEmpty()) return;

	    for (Dron dron : drones) {
	        if (dron.estaMuerto()) continue;
	        Position p = dron.getPosicion();
	        if (p == null) continue;
	        p.objId = dron.codigo();
	        System.out.println("Restaurando dron codigo=" + dron.codigo() + 
	            " idx=" + (dron.codigo() - jugador.getSlot())); // ver qué índice calcula
	        jugador.actualizarPosicion(dron.codigo(), p);
	    }
	}
	
}