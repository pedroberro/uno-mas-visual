package model;


import java.util.Date;


public class EPConfirmado implements IEstadoPartido {


    @Override
    public void agregarJugador(Partido partido) {
        System.out.println("No se pueden agregar jugadores: ya está confirmado.");
    }


    @Override
    public void confirmarJugadores(Partido partido) {
        System.out.println("Ya confirmado.");
    }


    @Override
    public void comenzar(Partido partido) {
        partido.cambiarEstadoPartido(new EPEnJuego());
        // Eliminar el envío manual de correo aquí.
    }


    @Override
    public void finalizar(Partido partido) {
        System.out.println("Aún no comenzó.");
    }


    @Override
    public void cancelar(Partido partido) {
        partido.cambiarEstadoPartido(new EPCancelado());
    }


    @Override
    public String nombre() {
        return "Confirmado";
    }
}


