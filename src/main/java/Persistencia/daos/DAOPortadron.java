package Persistencia.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import Persistencia.Portadron;
import Persistencia.consultas.Consultas;

public class DAOPortadron { //hash?
	
	//por ahora lo dejo sin una lista de drones porque no tengo claro como se va a enviar al unity
	private Connection con;
	
	public DAOPortadron(Connection c) {
		con = c;
	}
	
	public void insert(Portadron p) {
		Consultas cons = new Consultas();
		
		try {
			PreparedStatement insert = con.prepareStatement(cons.insert());
			insert.setString(1, p.getIdPartida());
			insert.setFloat(2, p.getPosX());
			insert.setFloat(3, p.getPosY());
			insert.setFloat(4, p.getPosZ());
			insert.setInt(5, p.getVida());
			insert.setString(6, p.getTipo());
			insert.executeUpdate();
			
			insert.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean member(String id) {
		Consultas cons = new Consultas();
		boolean esta = false;
		
		try {
			PreparedStatement member = con.prepareStatement(cons.member());
			member.setString(1, id);
			ResultSet rs = member.executeQuery();
			
			if(rs.next()) {
				esta = true;
			}
			
			rs.close();
			member.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return esta;
	}
	
	public List<Portadron> ListarPortadrones(String id) {
		Consultas cons = new Consultas();
		List<Portadron> lista = new LinkedList<Portadron>();
		
		try {
			PreparedStatement find = con.prepareStatement(cons.member());
			find.setString(1, id);
			ResultSet rs = find.executeQuery();
			
			while(rs.next()) {
				String idPar = rs.getString("codigo");
				float x = rs.getFloat("posX");
				float y = rs.getFloat("posY");
				float z = rs.getFloat("posZ");
				int vida = rs.getInt("vida");
				String tipo = rs.getString("tipo");
				
				Portadron p = new Portadron(idPar, x, y, z, vida, tipo);
				lista.add(p);
			}
			
			rs.close();
			find.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	
}
