package unomas.model;

import java.util.ArrayList;
import java.util.List;

public class EmparejamientoPorHistorial implements IEstrategiaEmparejamiento {

    private final int cantidadRequerida;

    public EmparejamientoPorHistorial(Partido partido, int cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    @Override
    public List<Usuario> emparejar(List<Usuario> candidatos) {
        List<Usuario> emparejados = new ArrayList<>();
        for (Usuario candidato : candidatos) {
            if (candidato.getHistorial().size() >= cantidadRequerida) {
                emparejados.add(candidato);
            }
        }
        return emparejados;
    }

    @Override
    public String nombre() {
        return "Historial";
    }
}
