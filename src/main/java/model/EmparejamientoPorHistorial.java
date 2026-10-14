package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmparejamientoPorHistorial implements IEstrategiaEmparejamiento {

    //Funcionamiento bastante simple para que funcione
    // podemos buscar otras opciones de emparejamiento por historial mas adelante

    private Partido partido;
    private int cantidadRequerida;

    public EmparejamientoPorHistorial(Partido partido, int cantidadRequerida) {
        this.partido = partido;
        this.cantidadRequerida = cantidadRequerida;
    }

    @Override
    public List<Usuario> emparejar(List<Usuario> candidatos) {
        List<Usuario> emparejados = new ArrayList<>();
        for (Usuario candidato : candidatos){
            if (candidato.getHistorial().size()>=cantidadRequerida)
                emparejados.add(candidato);
        }
        return emparejados;
    }
}