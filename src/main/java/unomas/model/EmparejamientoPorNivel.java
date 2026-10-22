package unomas.model;

import java.util.ArrayList;
import java.util.List;

public class EmparejamientoPorNivel implements IEstrategiaEmparejamiento {

    private final NivelDeJuego nivelRequerido;

    public EmparejamientoPorNivel(Partido partido, NivelDeJuego nivelRequerido) {
        this.nivelRequerido = nivelRequerido;
    }

    @Override
    public List<Usuario> emparejar(List<Usuario> candidatos) {
        List<Usuario> emparejados = new ArrayList<>();
        for (Usuario usuario : candidatos) {
            if (usuario.getNivelDeJuego() == nivelRequerido) {
                emparejados.add(usuario);
            }
        }
        return emparejados;
    }

    @Override
    public String nombre() {
        return "Nivel";
    }
}
