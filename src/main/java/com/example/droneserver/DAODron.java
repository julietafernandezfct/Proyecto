package com.example.droneserver;

import org.springframework.jdbc.core.JdbcTemplate;


import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;

import Persistencia.consultas.Consultas;

@Repository
public class DAODron { //lista

	private final JdbcTemplate jdbcTemplate;
	
	public DAODron(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;	
	}
	
	public void insback(Dron dron) {
	    Consultas cons = new Consultas();
	    jdbcTemplate.update(cons.insback(), 
	        dron.getCodPort(),        // cod_port
	        dron.codigo(),            // codigo
	        dron.getMunicion(),       // municion
	        dron.getPosicion().posX(), // x
	        dron.getPosicion().posY(), // y
	        dron.getPosicion().posZ(), // z
	        dron.getVida()            // vida  ← sin el 0 final
	    );
	}
	
	public boolean empty(String cod) {
		Consultas cons = new Consultas();
		
		Integer count = jdbcTemplate.queryForObject(cons.empty(), Integer.class, cod);

        return count == null || count == 0;

	}
	

	public List<Dron> ListarDrones(String cod){
		Consultas cons = new Consultas();

		return jdbcTemplate.query(cons.Listar(),
                (rs, rowNum) -> {

                    int codigo = rs.getInt("codigo");
                    String codPort = rs.getString("cod_port");
                    int municion = rs.getInt("municion");
                    float x = rs.getFloat("x");
                    float y = rs.getFloat("y");
                    float z = rs.getFloat("z");
                    int vida = rs.getInt("vida");

                    Position p = new Position(x, y, z);

                    return new Dron(codigo, codPort, municion, p, vida);
                },
                cod
        );
	}
	
	public void delete(String codPort) {
	    Consultas cons = new Consultas();
	    jdbcTemplate.update(cons.deleteDrones(), codPort);
	}
}
