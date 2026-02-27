package Persistencia;

public class Dron {

	private int codigo;
	private String codPort;
	private String tipo;
	private int municion;
	private float x;
	private float y;
	private float z;
	private int vivo;
	
	
	public Dron(int cod, String codP, String tip, int mun, float posx, float posy, float posz, int viv) {
		codigo = cod;
		codPort = codP;
		municion = mun;
		tipo = tip;
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
	
	public String getTipo() {
		return tipo;
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
	
	public int getVivo() {
		return vivo;
	}
}
