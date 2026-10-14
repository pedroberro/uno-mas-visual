package model;

public class DBasquet extends Deporte {

    @Override
    public String getNombre() {
        return "Basquet";
    }

    @Override
    public int getCantidadJugadores() {
        return 10; 
    }
}
