package model;

import lombok.*;
@Data

public abstract class Deporte {
    private String nombre;
    private int cantidadJugadores;

    public String getNombre() {
        return nombre;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }
}
