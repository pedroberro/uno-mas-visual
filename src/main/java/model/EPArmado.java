package model;

public class EPArmado implements IEstadoPartido {
    //FALTA: todos los jugadores aceptaron la participación

    @Override
    public void agregarJugador(Partido partido) {
        System.out.println("Partido ya armado. No se necesitan más jugadores.");
    }

    @Override
    public void confirmarJugadores(Partido partido) {
        partido.cambiarEstadoPartido(new EPConfirmado());
    }

    @Override
    public void comenzar(Partido partido) {
        System.out.println("No se puede comenzar: aún no fue confirmado.");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("No se puede finalizar: aún no comenzó.");
    }

    @Override
    public void cancelar(Partido partido) {
        partido.cambiarEstadoPartido(new EPCancelado());
    }

    @Override
    public String nombre() {
        return "Armado";
    }
}
