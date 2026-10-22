package unomas.model;

public class EPEnJuego implements IEstadoPartido {

    @Override
    public ResultadoAccion agregarJugador(Partido partido) {
        return ResultadoAccion.error("El partido está en juego: no se pueden agregar jugadores.");
    }

    @Override
    public ResultadoAccion confirmarJugadores(Partido partido) {
        return ResultadoAccion.error("El partido ya está confirmado.");
    }

    @Override
    public ResultadoAccion comenzar(Partido partido) {
        return ResultadoAccion.error("El partido ya está en juego.");
    }

    @Override
    public ResultadoAccion finalizar(Partido partido) {
        partido.cambiarEstadoPartido(new EPFinalizado());
        return ResultadoAccion.ok("Partido finalizado. ¡Gracias por jugar!");
    }

    @Override
    public ResultadoAccion cancelar(Partido partido) {
        return ResultadoAccion.error("No se puede cancelar: el partido ya comenzó.");
    }

    @Override
    public boolean admiteJugadores() {
        return false;
    }

    @Override
    public String nombre() {
        return "En juego";
    }
}
