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
		
		jdbcTemplate.update(cons.insback(), dron.getCodPort(), dron.codigo(), dron.getMunicion(), dron.getPosicion().posX(), dron.getPosicion().posY(), 
				dron.getPosicion().posZ(), dron.getVida());
		 
	}
	
	public boolean empty(String cod) {
		Consultas cons = new Consultas();
		
		Integer count = jdbcTemplate.queryForObject(cons.empty(), Integer.class, cod);

        return count == null || count == 0;

	}
	
	public List<Dron> ListarDrones(String cod) {
	    Consultas cons = new Consultas();
	    List<Dron> result = jdbcTemplate.query(cons.Listar(),
	        (rs, rowNum) -> {
	            int codigo = rs.getInt("codigo");
	            String codPort = rs.getString("cod_port");  // era "codPort"
	            int municion = rs.getInt("municion");
	            float x = rs.getFloat("x");   // era "posX"
	            float y = rs.getFloat("y");   // era "posY"
	            float z = rs.getFloat("z");   // era "posZ"
	            int vivo = rs.getInt("vida"); // era "vivo"
	            Position p = new Position(x, y, z);
	            return new Dron(codigo, codPort, municion, p, vivo);
	        },
	        cod
	    );
	    
	    System.out.println("ListarDrones cod=" + cod + " resultado=" + result.size());
	    return result;
	}
	
	public void delete(String codPort) {
	    Consultas cons = new Consultas();
	    jdbcTemplate.update(cons.deleteDrones(), codPort);
	}
}
