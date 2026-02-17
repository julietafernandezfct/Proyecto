package Excepciones;

public class PersistenciaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mensaje;

	public PersistenciaException(String mensaje) {
		super();
		this.mensaje = mensaje;
	}
	
	public String getMensaje() {
		return mensaje;
	}
}
