package unomas.model;

public class EPConfirmado implements IEstadoPartido {

    @Override
    public ResultadoAccion agregarJugador(Partido partido) {
        return ResultadoAccion.error("No se pueden agregar jugadores: el partido está confirmado.");
    }

    @Override
    public ResultadoAccion confirmarJugadores(Partido partido) {
        return ResultadoAccion.error("El partido ya está confirmado.");
    }

    @Override
    public ResultadoAccion comenzar(Partido partido) {
        partido.cambiarEstadoPartido(new EPEnJuego());
        return ResultadoAccion.ok("El partido comenzó. ¡A jugar!");
    }

    @Override
    public ResultadoAccion finalizar(Partido partido) {
        return ResultadoAccion.error("El partido todavía no comenzó.");
    }

    @Override
    public ResultadoAccion cancelar(Partido partido) {
        partido.cambiarEstadoPartido(new EPCancelado());
        return ResultadoAccion.ok("El partido fue cancelado.");
    }

    @Override
    public boolean admiteJugadores() {
        return false;
    }

    @Override
    public String nombre() {
        return "Confirmado";
    }
}
