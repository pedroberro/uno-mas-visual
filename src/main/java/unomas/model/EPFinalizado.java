package unomas.model;

public class EPFinalizado implements IEstadoPartido {

    @Override
    public ResultadoAccion agregarJugador(Partido partido) {
        return ResultadoAccion.error("No se puede: el partido ya finalizó.");
    }

    @Override
    public ResultadoAccion confirmarJugadores(Partido partido) {
        return ResultadoAccion.error("El partido ya finalizó.");
    }

    @Override
    public ResultadoAccion comenzar(Partido partido) {
        return ResultadoAccion.error("El partido ya finalizó.");
    }

    @Override
    public ResultadoAccion finalizar(Partido partido) {
        return ResultadoAccion.error("El partido ya finalizó.");
    }

    @Override
    public ResultadoAccion cancelar(Partido partido) {
        return ResultadoAccion.error("No se puede cancelar: el partido ya finalizó.");
    }

    @Override
    public boolean admiteJugadores() {
        return false;
    }

    @Override
    public String nombre() {
        return "Finalizado";
    }
}
