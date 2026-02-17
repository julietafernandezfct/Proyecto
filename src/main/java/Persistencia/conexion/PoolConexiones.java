package Persistencia.conexion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import Excepciones.PersistenciaException;


public class PoolConexiones implements IPoolConexiones {
	private String user;
	private String url;
	private String password;
	private String driver;
	private int nivelTransaccionalidad;
	private Conexion[] conexiones;
	private int tamano;
	private int creadas;
	private int tope;


    public PoolConexiones() throws PersistenciaException { //ver como manejamos estos throws en el servidor
    	creadas = 0;
		tope = 0;
		Properties p = new Properties();
		String nomArch = "properties/config.properties";

		try {
			p.load(new FileInputStream(nomArch));
			driver = p.getProperty("driver");
			Class.forName(driver);
			url = p.getProperty("url");
			user = p.getProperty("user");
			password = p.getProperty("password");
			nivelTransaccionalidad = Integer.parseInt(p.getProperty("nivelTransaccionalidad"));
			tamano = 2; //en la realidad solo van a conectarse 2 personas, se puede cambiar si se escala
			conexiones = new Conexion[tamano];
		} catch (Exception e) {
			throw new PersistenciaException ("Error obteniendo conexion");
		}
    }

    @Override
    public synchronized IConexion obtenerConexion(boolean enTransaccion) throws PersistenciaException {
        IConexion icon = null;

        while (icon == null) {
	        // Si no hay conexiones disponibles y ya se crearon todas
	        if (tope == 0 && creadas == tamano) {
	            try {
                    System.out.println("Esperando conexion libre");
	                wait();
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                throw new PersistenciaException("Espera interrumpida al obtener conexión");
	            }
	        }
	        // Si hay conexiones disponibles para reutilizar
	        if (tope > 0) {
	            tope--;
	            icon = conexiones[tope];
	        }
	        // Si no hay disponibles pero aún se pueden crear nuevas
	        else if (creadas < tamano) {
	            try {
		            // Instanciar una nueva conexión a demanda
		            Connection con = DriverManager.getConnection(url, user, password);
		
		            // Configurar el modo de autocommit si está en transacción
		            if (enTransaccion) {
		                con.setAutoCommit(false);
		                con.setTransactionIsolation(nivelTransaccionalidad);
		            } else {
		                con.setAutoCommit(true);
		            }
		
	                // Encapsular en el objeto Conexion
	                icon = new Conexion(con);
	                creadas++;
	            } catch (SQLException e) {
                    // Error de comunicación con BD
                    throw new PersistenciaException("Error al crear nueva conexion");
	            }
	        }
        }
        System.out.println("Conexion obtenida");
        return icon;
    }

    @Override
    public synchronized void liberarConexion(IConexion icon, boolean commit) throws PersistenciaException {
        if (icon == null)
            return;

        // Manejo de la transacción (Commit o Rollback)
        Connection con = ((Conexion) icon).getConnection();
        try {
            if (!con.getAutoCommit()) {
                if (commit) {
                    con.commit();
                } else {
                    con.rollback();
                }
            }
            // Devolver la conexión a autocommit=true por seguridad si el diseño lo requiere
            con.setAutoCommit(true);

        } catch (SQLException e) {
            throw new PersistenciaException("Error en commit/rollback de la BD");
        }

        // Poner la conexión devuelta en el Pool
        conexiones[tope] = (Conexion) icon;
        tope++;

        // Notifica a los hilos que están esperando una conexión
        notifyAll();
        System.out.println("Conexion liberada");
    }

    public synchronized int getTope() {
        return tope;
    }

    public int getTamano() {
        return tamano;
    }
}
