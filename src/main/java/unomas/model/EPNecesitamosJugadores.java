package unomas.model;

public class EPNecesitamosJugadores implements IEstadoPartido {

    @Override
    public ResultadoAccion agregarJugador(Partido partido) {
        if (partido.getCantidadJugadoresActual() >= partido.getCantidadJugadoresRequerida()) {
            partido.cambiarEstadoPartido(new EPArmado());
            return ResultadoAccion.ok("Se completaron los jugadores. El partido quedó armado.");
        }
        return ResultadoAccion.ok("Te uniste al partido.");
    }

    @Override
    public ResultadoAccion confirmarJugadores(Partido partido) {
        return ResultadoAccion.error("No se puede confirmar: todavía faltan jugadores.");
    }

    @Override
    public ResultadoAccion comenzar(Partido partido) {
        return ResultadoAccion.error("No se puede comenzar: el partido todavía no está confirmado.");
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
        return true;
    }

    @Override
    public String nombre() {
        return "Necesitamos jugadores";
    }
}
