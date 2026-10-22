package unomas.model;

public class EPCancelado implements IEstadoPartido {

    @Override
    public ResultadoAccion agregarJugador(Partido partido) {
        return ResultadoAccion.error("El partido fue cancelado.");
    }

    @Override
    public ResultadoAccion confirmarJugadores(Partido partido) {
        return ResultadoAccion.error("El partido fue cancelado.");
    }

    @Override
    public ResultadoAccion comenzar(Partido partido) {
        return ResultadoAccion.error("No se puede comenzar: el partido fue cancelado.");
    }

    @Override
    public ResultadoAccion finalizar(Partido partido) {
        return ResultadoAccion.error("El partido fue cancelado.");
    }

    @Override
    public ResultadoAccion cancelar(Partido partido) {
        return ResultadoAccion.error("El partido ya está cancelado.");
    }

    @Override
    public boolean admiteJugadores() {
        return false;
    }

    @Override
    public String nombre() {
        return "Cancelado";
    }
}
