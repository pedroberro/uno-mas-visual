package model;

import java.util.ArrayList;
import java.util.List;

public class EmparejamientoPorCercania implements IEstrategiaEmparejamiento {
    private Partido partido;
    private IMGrafoTDA mapa;

    public EmparejamientoPorCercania(Partido partido, IMGrafoTDA mapa) {
        this.partido = partido;
        this.mapa = mapa;
    }

    @Override
    public List<Usuario> emparejar(List<Usuario> candidatos) {
        List<Usuario> emparejados = new ArrayList<>();
        Barrio zonaPartido = partido.getZona();

        for (Usuario u : candidatos) {
            boolean mismaZona = u.getZona().distancia(zonaPartido, mapa) <= 10;
            if (mismaZona) {
                emparejados.add(u);
            }
        }
        return emparejados;
    }
}