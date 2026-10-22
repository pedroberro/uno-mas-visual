package unomas.model;

public interface IMConjuntoTDA {
    void InicializarConjunto();
    void Agregar(Barrio x);
    void Sacar(Barrio x);
    boolean Pertenece(Barrio x);
    Barrio Elegir();
    boolean ConjuntoVacio();
}
