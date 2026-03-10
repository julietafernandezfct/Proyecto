package Persistencia.consultas;

public class Consultas {
	
	//CONSULTAS DE PORTADRON
	
	public String insert() {
		String consulta = "INSERT INTO porta_drones VALUES (?,?,?,?,?,?)";
		return consulta;
	}
	
	public String member() {
		String consulta = "select count(*) from porta_drones where codigo = ? AND tipo = ?";
		return consulta;
	}
	
	public String find() {
	    return "SELECT * FROM porta_drones WHERE codigo = ? AND tipo = ?";
	}
	
	public String delete() {
	    return "DELETE FROM porta_drones WHERE id_partida = ? AND tipo = ?";
	}
	
	
	//CONSULTAS DE DRON
	
	//PRECONDICION: viene en orden del juego
	public String insback() {
		String consulta = "INSERT INTO dron (codigo, codPort, municion, posX, posY, posZ, vivo) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?,  0)";
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
	
	public String deleteDrones() {
	    return "DELETE FROM dron WHERE cod_port = ?";
	}

}
