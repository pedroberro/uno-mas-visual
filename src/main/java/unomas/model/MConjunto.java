package unomas.model;

public class MConjunto implements IMConjuntoTDA{

    class Nodo {
        Barrio info;
        Nodo sig;
    }

    Nodo c;

    @Override
    public void InicializarConjunto() {
        c = null;
    }

    @Override
    public void Agregar(Barrio x) {
        if (!Pertenece(x)) {
            Nodo nuevo = new Nodo();
            nuevo.info = x;
            nuevo.sig = c;
            c = nuevo;
        }
    }

    @Override
    public void Sacar(Barrio x) {
        if (c != null) {
            if (c.info.equals(x)) {
                c = c.sig;
            } else {
                Nodo viajero = c;
                while (viajero.sig != null && !viajero.sig.info.equals(x)) {
                    viajero = viajero.sig;
                }
                if (viajero.sig != null) {
                    viajero.sig = viajero.sig.sig;
                }
            }
        }
    }

    @Override
    public boolean Pertenece(Barrio x) {
        Nodo viajero = c;
        while (viajero != null && !viajero.info.equals(x)) {
            viajero = viajero.sig;
        }
        return (viajero != null);
    }

    @Override
    public Barrio Elegir() {
        if (c == null) throw new RuntimeException("El conjunto está vacío");
        return c.info;
    }

    @Override
    public boolean ConjuntoVacio() {
        return (c == null);
    }
}
