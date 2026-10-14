package model;

public class DFutbol extends Deporte {

    @Override
    public String getNombre() {
        return "Fútbol";
    }

    @Override
    public int getCantidadJugadores() {
        return 22; // Fútbol se juega entre dos equipos de 11 jugadores cada uno
    }

}
