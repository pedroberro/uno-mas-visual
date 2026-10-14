package model;

public class EPFinalizado implements IEstadoPartido {

    @Override
    public void agregarJugador(Partido partido) {
        System.out.println("No se puede: el partido ya finalizó.");
    }

    @Override
    public void confirmarJugadores(Partido partido) {
        System.out.println("Ya finalizado.");
    }

    @Override
    public void comenzar(Partido partido) {
        System.out.println("Ya finalizado.");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("Ya finalizado.");
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("No se puede cancelar: el partido ya finalizó.");
    }

    @Override
    public String nombre() {
        return "Finalizado";
    }
}
