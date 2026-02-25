package Persistencia;

public class Dron {

	private int codigo;
	private String codPort;
	private int municion;
	private float x;
	private float y;
	private float z;
	private boolean vivo;
	
	
	public Dron(int cod, String codP, int mun, float posx, float posy, float posz, boolean viv) {
		codigo = cod;
		codPort = codP;
		municion = mun;
		x = posx;
		y = posy;
		z = posz;
		vivo = viv;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public String getCodPort() {
		return codPort;
	}
	
	public int getMunicion() {
		return municion;
	}
	
	public float getPosX() {
		return x;
	}
	
	public float getPosY() {
		return y;
	}
	
	public float getPosZ() {
		return z;
	}
	
	public boolean getVivo() {
		return vivo;
	}
}
