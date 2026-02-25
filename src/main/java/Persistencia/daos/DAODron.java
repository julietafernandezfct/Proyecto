package Persistencia.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import java.sql.Connection;

import Persistencia.Dron;
import Persistencia.consultas.Consultas;

public class DAODron { //lista

	private Connection con;
	
	public DAODron(Connection conne) {
		con = conne;
	}
	
	public void insback(Dron dron) {
		Consultas cons = new Consultas();
		
		try {
			PreparedStatement insback = con.prepareStatement(cons.insback());
			insback.setInt(1, dron.getCodigo());
			insback.setString(2, dron.getCodPort());
			insback.setInt(3, dron.getMunicion());
			insback.setFloat(4, dron.getPosX());
			insback.setFloat(5, dron.getPosY());
			insback.setFloat(6, dron.getPosZ());
			insback.setBoolean(7, dron.getVivo());
			insback.executeUpdate();
			
			insback.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean empty(String cod) {
		Consultas cons = new Consultas();
		boolean vacio = true;
		
		try {
			PreparedStatement empty = con.prepareStatement(cons.Listar());
			empty.setString(1, cod);
			ResultSet rs = empty.executeQuery();
			
			if(rs.next()) {
				vacio = false;
			}
			
			rs.close();
			empty.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return vacio;
	}
	
	public List<Dron> ListarDrones(String cod){
		Consultas cons = new Consultas();
		List<Dron> lista = new LinkedList<Dron>();
		
		try {
			PreparedStatement listado = con.prepareStatement(cons.Listar());
			listado.setString(1, cod);
			ResultSet rs = listado.executeQuery();
			
			while(rs.next()) {
				int codigo = rs.getInt("codigo");
				String codPort = rs.getString("codPort");
				int municion = rs.getInt("municion");
				float x = rs.getFloat("posX");
				float y = rs.getFloat("posY");
				float z = rs.getFloat("posZ");
				boolean vivo = rs.getBoolean("vivo");
				
				Dron dron = new Dron(codigo, codPort, municion, x, y, z, vivo);
				lista.add(dron);
			}
			
			rs.close();
			listado.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	
}
