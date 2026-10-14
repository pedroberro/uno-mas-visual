package model;

public class EPNecesitamosJugadores implements IEstadoPartido {
    public void agregarJugador(Partido partido) {
        if (partido.getCantidadJugadoresActual() >= partido.getCantidadJugadoresRequerida()) {
            partido.cambiarEstadoPartido(new EPArmado());
            System.out.println("Partido armado.");
        }
    }

    @Override
    public void confirmarJugadores(Partido partido) {
        System.out.println("No se puede confirmar. Aún faltan jugadores.");
    }

    @Override
    public void comenzar(Partido partido) {
        System.out.println("No se puede comenzar. El partido no está armado ni confirmado.");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("El partido aún no comenzó.");
    }

    @Override
    public void cancelar(Partido partido) {
        partido.cambiarEstadoPartido(new EPCancelado());
    }

    @Override
    public String nombre() {
        return "Necesitamos jugadores";
    }
}
