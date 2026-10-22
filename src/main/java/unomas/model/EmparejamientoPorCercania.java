package unomas.model;

import java.util.ArrayList;
import java.util.List;

public class EmparejamientoPorCercania implements IEstrategiaEmparejamiento {

    private static final int DISTANCIA_MAXIMA = 10;

    private final Partido partido;
    private final IMGrafoTDA mapa;

    public EmparejamientoPorCercania(Partido partido, IMGrafoTDA mapa) {
        this.partido = partido;
        this.mapa = mapa;
    }

    @Override
    public List<Usuario> emparejar(List<Usuario> candidatos) {
        List<Usuario> emparejados = new ArrayList<>();
        Barrio zonaPartido = partido.getZona();

        for (Usuario usuario : candidatos) {
            if (usuario.getZona().distancia(zonaPartido, mapa) <= DISTANCIA_MAXIMA) {
                emparejados.add(usuario);
            }
        }
        return emparejados;
    }

    @Override
    public String nombre() {
        return "Cercanía";
    }
}
