package Persistencia.consultas;

public class Consultas {
	
	//CONSULTAS DE PORTADRON
	
	public String insert() {
	    return "INSERT INTO porta_drones (id_partida, x, y, z, vida, tipo, bloqueado) VALUES (?, ?, ?, ?, ?, ?, ?)";
	}
	
	public String member() {
		String consulta = "select count(*) from porta_drones where id_partida = ? AND tipo = ?";
		return consulta;
	}
	
	public String find() {
	    return "SELECT * FROM porta_drones WHERE id_partida = ? AND tipo = ?";
	}
	
	public String delete() {
	    return "DELETE FROM porta_drones WHERE id_partida = ? AND tipo = ?";
	}
	
	
	//CONSULTAS DE DRON
	
	//PRECONDICION: viene en orden del juego
	public String insback() {
	    return "INSERT INTO dron (cod_port, codigo, municion, x, y, z, vida) "
	         + "VALUES (?, ?, ?, ?, ?, ?, ?)";
	}
	
	public String Listar() {
		String consulta = "select * from dron where cod_port = ? ";
		return consulta;
	}
	
	public String empty() {
		String consulta = "SELECT COUNT(*) FROM dron WHERE cod_port = ? ";
		return consulta;
	}
	
	public String deleteDrones() {
	    return "DELETE FROM dron WHERE cod_port = ?";
	}
}
    
