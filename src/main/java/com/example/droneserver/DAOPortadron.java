package com.example.droneserver;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Persistencia.consultas.Consultas;

@Repository
public class DAOPortadron { //hash?
	
	private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DAOPortadron(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
    public void insert(PortaDrones p) {
        Consultas cons = new Consultas();
        Position pos = p.getPosicion();
        jdbcTemplate.update(cons.insert(),
            p.getIdPartida(),
            pos.posX(),
            pos.posY(),
            pos.posZ(),
            p.getVida(),
            pos.getTipo(),
            p.estaBloqueado() ? 1 : 0
        );
    }
	
	public boolean member(String id, String tipo) {
		Consultas cons = new Consultas();

		Integer count = jdbcTemplate.queryForObject(cons.member(), Integer.class, id, tipo);

	    return count != null && count > 0;
	}
	
	public PortaDrones find(String id, String tipo) {
	    Consultas cons = new Consultas();
	    return jdbcTemplate.queryForObject(cons.find(),
	        (rs, rowNum) -> {
	            float x = rs.getFloat("x");
	            float y = rs.getFloat("y");
	            float z = rs.getFloat("z");
	            int vida = rs.getInt("vida");
	            Position pos = new Position(x, y, z);
	            pos.tipo = tipo;
	            PortaDrones p = new PortaDrones(id, pos, vida);
	            DAODron dao = new DAODron(jdbcTemplate);
	            List<Dron> drones = dao.ListarDrones(id);
	            p.setDrones(drones);
	            return p;
	        },
	        id, tipo
	    );
	}
	
}
