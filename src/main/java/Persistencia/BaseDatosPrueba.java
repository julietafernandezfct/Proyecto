package Persistencia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

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
		
		String portadron = "CREATE TABLE Proyecto.Portadron (codigo int(10) PRIMARY KEY, posX int(10), posY int(10), posZ int(10), vida int(10), tipo varchar(10))";
		PreparedStatement port = con.prepareStatement(portadron);
		port.executeUpdate(portadron);
		port.close();
		
		String dron = "CREATE TABLE Proyecto.Dron (codigo int(10) PRIMARY KEY, codPort int(10), municion int(10), posX int(10), posY int(10), posZ int(10), vivo bit, FOREIGN KEY (codPort) references Portadron(codigo))";
		PreparedStatement dr = con.prepareStatement(dron);
		dr.executeUpdate(dron);
		dr.close();
		con.close();

	}

}
