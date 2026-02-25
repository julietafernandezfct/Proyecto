package Persistencia;

public class Portadron {
	
	private String idPartida;
	private float posx;
	private float posy;
	private float posz;
	private int vida;
	private String tipo;

	
	public Portadron(String partida, float x, float y, float z, int vid, String tip) {
		idPartida = partida;
		posx = x;
		posy = y;
		posz = z;
		vida = vid;
		tipo = tip;
	}
	
	public String getIdPartida() {
		return idPartida;
	}
	
	public float getPosX() {
		return posx;
	}
	
	public float getPosY() {
		return posy;
	}
	
	public float getPosZ() {
		return posx;
	}
	
	public int getVida() {
		return vida;
	}
	
	public String getTipo() {
		return tipo;
	}
}
