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
		
		jdbcTemplate.update(cons.insback(), dron.codigo(), dron.getCodPort(), dron.getTipo(), dron.getMunicion(), dron.getPosicion().posX(), dron.getPosicion().posY(), 
				dron.getPosicion().posZ(), dron.getVida());
		
	}
	
	public boolean empty(String cod, String tipo) {
		Consultas cons = new Consultas();
		
		Integer count = jdbcTemplate.queryForObject(cons.empty(), Integer.class, cod, tipo);

        return count == null || count == 0;

	}
	
	public List<Dron> ListarDrones(String cod, String tipo){
		Consultas cons = new Consultas();

		return jdbcTemplate.query(cons.Listar(),
                (rs, rowNum) -> {

                    int codigo = rs.getInt("codigo");
                    String codPort = rs.getString("codPort");
                    int municion = rs.getInt("municion");
                    float x = rs.getFloat("posX");
                    float y = rs.getFloat("posY");
                    float z = rs.getFloat("posZ");
                    int vivo = rs.getInt("vivo");

                    Position p = new Position(x, y, z);

                    return new Dron(codigo, codPort, tipo, municion, p, vivo);
                },
                cod, tipo
        );
	}
	
	
}
