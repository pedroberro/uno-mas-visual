package unomas.model;

public interface IEstadoPartido {

    ResultadoAccion agregarJugador(Partido partido);

    ResultadoAccion confirmarJugadores(Partido partido);

    ResultadoAccion comenzar(Partido partido);

    ResultadoAccion finalizar(Partido partido);

    ResultadoAccion cancelar(Partido partido);

    boolean admiteJugadores();

    String nombre();
}
