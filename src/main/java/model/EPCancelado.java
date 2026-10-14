package model;

public class EPCancelado implements IEstadoPartido {
    @Override
    public void agregarJugador(Partido partido) {
        System.out.println("No se puede: el partido fue cancelado.");
    }

    @Override
    public void confirmarJugadores(Partido partido) {
        System.out.println("El partido fue cancelado.");
    }

    @Override
    public void comenzar(Partido partido) {
        System.out.println("No se puede comenzar: el partido fue cancelado.");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("El partido fue cancelado.");
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("Ya está cancelado.");
    }

    @Override
    public String nombre() {
        return "Cancelado";
    }
}
