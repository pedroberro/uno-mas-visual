package unomas.model;

public class EPArmado implements IEstadoPartido {

    @Override
    public ResultadoAccion agregarJugador(Partido partido) {
        return ResultadoAccion.error("El partido ya está armado: no se necesitan más jugadores.");
    }

    @Override
    public ResultadoAccion confirmarJugadores(Partido partido) {
        partido.cambiarEstadoPartido(new EPConfirmado());
        return ResultadoAccion.ok("Jugadores confirmados. El partido quedó confirmado.");
    }

    @Override
    public ResultadoAccion comenzar(Partido partido) {
        return ResultadoAccion.error("No se puede comenzar: el partido todavía no fue confirmado.");
    }

    @Override
    public ResultadoAccion finalizar(Partido partido) {
        return ResultadoAccion.error("No se puede finalizar: el partido todavía no comenzó.");
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
        return "Armado";
    }
}
