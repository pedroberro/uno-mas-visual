package model;

import java.util.ArrayList;
import java.util.List;

public class EmparejamientoPorNivel implements IEstrategiaEmparejamiento {
    private Partido partido;
    private NivelDeJuego nivelRequerido;

    public EmparejamientoPorNivel(Partido partido, NivelDeJuego nivelRequerido) {
        this.partido = partido;
        this.nivelRequerido = nivelRequerido;
    }

    @Override
    public List<Usuario> emparejar(List<Usuario> candidatos) {
        List<Usuario> emparejados = new ArrayList<>();
        for (Usuario u : candidatos) {
            if (u.getNivelDeJuego() == nivelRequerido) {
                emparejados.add(u);
            }
        }
        return emparejados;
    }
}