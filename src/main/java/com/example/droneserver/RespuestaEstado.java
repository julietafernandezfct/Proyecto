package com.example.droneserver;

import com.example.droneserver.datos.DatoMunicion;
import com.example.droneserver.datos.DatoProyectil;
import com.example.droneserver.datos.DatoVida;

public class RespuestaEstado {

    public Position[] posiciones;
    public DatoVida[] vidas;
    public DatoMunicion[] municion;
    public DatoProyectil[] proyectiles;

    public RespuestaEstado() {}
}
