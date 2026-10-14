package model;

public class EPEnJuego implements IEstadoPartido {

    @Override
    public void agregarJugador(Partido partido) {
        System.out.println("Partido en juego: no se pueden agregar jugadores.");
    }

    @Override
    public void confirmarJugadores(Partido partido) {
        System.out.println("Partido ya confirmado.");
    }

    @Override
    public void comenzar(Partido partido) {
        System.out.println("Ya está en juego.");
    }

    @Override
    public void finalizar(Partido partido) {
        partido.cambiarEstadoPartido(new EPFinalizado());
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("No se puede cancelar: ya comenzó.");
    }

    @Override
    public String nombre() {
        return "En juego";
    }
}
