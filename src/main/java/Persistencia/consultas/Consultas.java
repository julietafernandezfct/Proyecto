package Persistencia.consultas;

public class Consultas {

    // CONSULTAS DE PORTADRON

    public String insert() {
        return "INSERT INTO porta_drones (id_partida, x, y, z, vida, tipo, bloqueado) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    public String member() {
        return "SELECT COUNT(*) FROM porta_drones WHERE id_partida = ? AND tipo = ?";
    }

    public String find() {
        return "SELECT * FROM porta_drones WHERE id_partida = ? AND tipo = ?";
    }
    
    public String delete() {
        return "DELETE FROM porta_drones WHERE id_partida = ? AND tipo = ?";
    }

    // CONSULTAS DE DRON

    public String insback() {
        return "INSERT INTO dron (cod_port, codigo, municion, x, y, z, vida) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    public String Listar() {
        return "SELECT * FROM dron WHERE cod_port = ?";
    }

    public String deleteDrones() {
        return "DELETE FROM dron WHERE cod_port = ?";
    }
    

    public String empty() {
        return "SELECT COUNT(*) FROM dron WHERE cod_port = ?";
    }
    
}