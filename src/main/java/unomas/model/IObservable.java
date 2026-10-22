package unomas.model;

public interface IObservable {
    void agregarObserver (IObserver o);
    void eliminarObserver (IObserver o);
    void notificar();
}