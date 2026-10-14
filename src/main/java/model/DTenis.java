package model;

import lombok.*;
@Data

public class DTenis extends Deporte {

    @Override
    public String getNombre() {
        return "Tenis";
    }

    @Override
    public int getCantidadJugadores() {
        return 2; // Tenis se juega entre dos jugadores
    }

}
