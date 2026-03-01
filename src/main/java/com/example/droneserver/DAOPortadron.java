package com.example.droneserver;

import org.springframework.jdbc.core.JdbcTemplate;


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
		
		jdbcTemplate.update(cons.insert(),
	            p.getIdPartida(),
	            p.getPosicion().posX(),
	            p.getPosicion().posY(),
	            p.getPosicion().posZ(),
	            p.getVida(),
	            p.getTipo()
	    );
	}
	
	public boolean member(String id) {
		Consultas cons = new Consultas();

		Integer count = jdbcTemplate.queryForObject(cons.member(), Integer.class, id);

	    return count != null && count > 0;
	}
	
	public PortaDrones find(String id, String tipo) {
		Consultas cons = new Consultas();

		return jdbcTemplate.queryForObject(cons.member(),
		        (rs, rowNum) -> {
		            float x = rs.getFloat("posX");
		            float y = rs.getFloat("posY");
		            float z = rs.getFloat("posZ");
		            int vida = rs.getInt("vida");

		            Position pos = new Position(x, y, z);

		            return new PortaDrones(id, pos, vida, tipo);
		        },
		        id, tipo
		    );
	}
	
}
