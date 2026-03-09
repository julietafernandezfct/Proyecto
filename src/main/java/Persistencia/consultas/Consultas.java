package Persistencia.consultas;

public class Consultas {
	
	//CONSULTAS DE PORTADRON
	
	public String insert() {
		String consulta = "INSERT INTO Proyecto.Portadron VALUES (?,?,?,?,?,?)";
		return consulta;
	}
	
	public String member() {
		String consulta = "select count(*) from Proyecto.Portadron where codigo = ? AND tipo = ?";
		return consulta;
	}
	
	public String find() {
	    return "SELECT * FROM Proyecto.Portadron WHERE codigo = ? AND tipo = ?";
	}
	
	
	//CONSULTAS DE DRON
	
	//PRECONDICION: viene en orden del juego
	public String insback() {
		String consulta = "INSERT INTO Proyecto.Dron (codigo, codPort, municion, posX, posY, posZ, vivo) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
		return consulta;
	}
	
	public String Listar() {
		String consulta = "select * from Proyecto.Dron where codPort = ? ";
		return consulta;
	}
	
	public String empty() {
		String consulta = "SELECT COUNT(*) FROM dron WHERE codPort = ? ";
		return consulta;
	}
	

}
