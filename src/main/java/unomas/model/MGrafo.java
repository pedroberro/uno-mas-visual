package unomas.model;

import java.util.HashMap;
import java.util.Map;

public class MGrafo implements IMGrafoTDA{

    int n = 20;
    int[][] MAdy;
    int cantNodos;
    Barrio[] Etiqs;
    Map<Barrio, Integer> lugarIndice;

    @Override
    public void InicializarGrafo() {
        MAdy = new int[n][n];
        Etiqs = new Barrio[n];
        cantNodos = 0;
        lugarIndice = new HashMap<>();
    }

    @Override
    public void AgregarVertice(Barrio v) {
        if (cantNodos < n && !lugarIndice.containsKey(v)) {
            Etiqs[cantNodos] = v;
            lugarIndice.put(v, cantNodos);
            for (int i = 0; i < n; i++) {
                MAdy[cantNodos][i] = 0;
                MAdy[i][cantNodos] = 0;
            }
            cantNodos++;
        }
    }

    @Override
    public void AgregarArista(Barrio o, Barrio d, int p) {
        Integer i = lugarIndice.get(o);
        Integer j = lugarIndice.get(d);
        if (i != null && j != null) {
            MAdy[i][j] = p;
            MAdy[j][i] = p;
        }
    }

    @Override
    public void EliminarVertice(Barrio v) {
        Integer index = lugarIndice.get(v);
        if (index != null) {
            lugarIndice.remove(v);
            Etiqs[index] = Etiqs[cantNodos - 1];
            lugarIndice.put(Etiqs[index], index);
            for (int i = 0; i < cantNodos; i++) {
                MAdy[index][i] = MAdy[cantNodos - 1][i];
                MAdy[i][index] = MAdy[i][cantNodos - 1];
            }
            cantNodos--;
        }
    }

    @Override
    public void EliminarArista(Barrio o, Barrio d) {
        Integer i = lugarIndice.get(o);
        Integer j = lugarIndice.get(d);
        if (i != null && j != null) {
            MAdy[i][j] = 0;
            MAdy[j][i] = 0;
        }
    }

    @Override
    public IMConjuntoTDA Vertices() {
        IMConjuntoTDA c = new MConjunto();
        c.InicializarConjunto();
        for (int i = 0; i < cantNodos; i++)
            c.Agregar(Etiqs[i]);
        return c;
    }

    @Override
    public boolean ExisteArista(Barrio o, Barrio d) {
        Integer i = lugarIndice.get(o);
        Integer j = lugarIndice.get(d);
        return i != null && j != null && MAdy[i][j] != 0;
    }

    @Override
    public int PesoArista(Barrio o, Barrio d) {
        Integer i = lugarIndice.get(o);
        Integer j = lugarIndice.get(d);
        return i != null && j != null ? MAdy[i][j] : 0;
    }
}
