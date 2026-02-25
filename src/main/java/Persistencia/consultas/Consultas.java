package Persistencia.consultas;

public class Consultas {
	
	//CONSULTAS DE PORTADRON
	
	public String insert() {
		String consulta = "INSERT INTO Proyecto.Portadron VALUES (?,?,?,?,?,?)";
		return consulta;
	}
	
	public String member() {
		String consulta = "select * from Proyecto.Portadron where codigo = ?";
		return consulta;
	}
	
	
	//CONSULTAS DE DRON
	
	//PRECONDICION: viene en orden del juego
	public String insback() {
		String consulta = "INSERT INTO Proyecto.Dron VALUES (?,?,?,?,?,?,?)";
		return consulta;
	}
	
	public String Listar() {
		String consulta = "select * from Proyecto.Dron where codPort = ?";
		return consulta;
	}
	
	

}
