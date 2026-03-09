package Persistencia.consultas;

public class Consultas {
	
	//CONSULTAS DE PORTADRON
	
	public String member() {
	    return "SELECT COUNT(*) FROM portadron WHERE codigo = ? AND tipo = ?";
	}

	public String find() {
	    return "SELECT * FROM portadron WHERE codigo = ? AND tipo = ?";
	}

	public String insert() {
	    return "INSERT INTO portadron VALUES (?,?,?,?,?,?)";
	}
	
	
	//CONSULTAS DE DRON
	
	//PRECONDICION: viene en orden del juego
	public String insback() {
		String consulta = "INSERT INTO dron (codigo, codPort, municion, posX, posY, posZ, vivo) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
		return consulta;
	}
	
	public String Listar() {
		String consulta = "select * from dron where codPort = ? ";
		return consulta;
	}
	
	public String empty() {
		String consulta = "SELECT COUNT(*) FROM dron WHERE codPort = ? ";
		return consulta;
	}
	

}
