package Persistencia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import Persistencia.daos.DAODron;
import Persistencia.daos.DAOPortadron;

public class BaseDatosPrueba {

	public static void main(String[] args) throws SQLException { //hay que ver dsp como manejamos estas excepciones
		String driver = "com.mysql.jdbc.Driver";
		try {
		Class.forName(driver);
		} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
		
		Properties p = new Properties();
		try {
			/* 2. una vez cargado el driver, me conecto con la base de datos */
			

			InputStream input = BaseDatosPrueba.class
			        .getClassLoader()
			        .getResourceAsStream("config.properties");
			p.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = p.getProperty("url");
		String user = p.getProperty("user");
		String password = p.getProperty("password");
		Connection con = DriverManager.getConnection(url, user, password);
		
		/*
		String portadron = "CREATE TABLE Proyecto.Portadron (codigo varchar(45), posX int(10), posY int(10), posZ int(10), vida int(10), tipo varchar(10), PRIMARY KEY (codigo, tipo))";
		PreparedStatement port = con.prepareStatement(portadron);
		port.executeUpdate(portadron);
		port.close();

		
		String dron = "CREATE TABLE Proyecto.Dron (codigo int(10) PRIMARY KEY, codPort varchar(45), municion int(10), posX float, posY float, posZ float, vivo bit, FOREIGN KEY (codPort) references Portadron(codigo))";
		PreparedStatement dr = con.prepareStatement(dron);
		dr.executeUpdate(dron);
		dr.close();*/
		
		//TESTEOOO
		DAOPortadron portadrones = new DAOPortadron(con);
		DAODron drones = new DAODron(con);
		System.out.println("cree los daos");
		Dron d1 = new Dron(1, "dskd", 1, 1, 2, 3, true);
		Dron d2 = new Dron(2, "dskd", 1, 7, 2, 3, false);
		Dron d3 = new Dron(3, "dskd", 1, 7, 2, 3, false);
		
		System.out.println("cree los objetos");

		Portadron p1 = new Portadron("dskd", 1, 2, 3, 4, "naval");
		

		
		//portadrones.insert(p1);
		//System.out.println("ingrese el portadron");
		
		//drones.insback(d1);
		//drones.insback(d2);
		//drones.insback(d3);
		
		System.out.println("info Dron");
		
		List<Dron> listaDrones = drones.ListarDrones("dskd");
		
		for (Dron d : listaDrones) {
			System.out.println("\n");
		    System.out.println("Codigo: " + d.getCodigo());
		    System.out.println("Municion: " + d.getMunicion());
		    System.out.println("PosX: " + d.getPosX());
		}
		
		List<Portadron> listaPorta = portadrones.ListarPortadrones("dskd");
		
		for(Portadron por : listaPorta) {
			System.out.println("\n");
		    System.out.println("IdPartida: " + por.getIdPartida());
		    System.out.println("Vida: " + por.getVida());
		    System.out.println("Tipo: " + por.getTipo());
		}
		
		con.close();
	}

}
