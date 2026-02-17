package Persistencia.conexion;

import java.sql.SQLException;

import Excepciones.PersistenciaException;

public interface IPoolConexiones{
	public IConexion obtenerConexion(boolean enTransaccion) throws PersistenciaException;
    public  void liberarConexion(IConexion icon, boolean commit) throws PersistenciaException;

}
