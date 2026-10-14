package model;

public interface IEstadoPartido {
    void agregarJugador(Partido partido);
    void confirmarJugadores(Partido partido);
    void comenzar(Partido partido);
    void finalizar(Partido partido);
    void cancelar(Partido partido);
    String nombre();
}
