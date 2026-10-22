package unomas.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import unomas.model.Barrio;
import unomas.model.IMConjuntoTDA;
import unomas.model.IMGrafoTDA;

public final class BarrioController {

    private static List<Barrio> barriosDisponibles = new ArrayList<>();
    private static IMGrafoTDA mapaBarrios;

    private BarrioController() {
    }

    public static void inicializarBarrios(List<Barrio> barrios, IMGrafoTDA grafo) {
        barriosDisponibles = new ArrayList<>(barrios);
        mapaBarrios = grafo;
    }

    public static Barrio obtenerBarrioPorNombre(String ciudad, String barrio) {
        if (mapaBarrios == null) {
            return null;
        }
        IMConjuntoTDA vertices = mapaBarrios.Vertices();
        while (!vertices.ConjuntoVacio()) {
            Barrio candidato = vertices.Elegir();
            if (candidato.getCiudad().equalsIgnoreCase(ciudad) && candidato.getBarrio().equalsIgnoreCase(barrio)) {
                return candidato;
            }
            vertices.Sacar(candidato);
        }
        return null;
    }

    public static Barrio obtenerBarrio(String barrio) {
        if (barrio == null) {
            return null;
        }
        return barriosDisponibles.stream()
                .filter(candidato -> candidato.getBarrio().equalsIgnoreCase(barrio.trim()))
                .findFirst()
                .orElse(null);
    }

    public static List<Barrio> getBarriosDisponibles() {
        return List.copyOf(barriosDisponibles);
    }

    public static IMGrafoTDA getMapaBarrios() {
        return mapaBarrios;
    }

    public static List<String> getCiudadesDisponibles() {
        if (mapaBarrios == null) {
            return List.of();
        }
        Set<String> ciudades = new HashSet<>();
        IMConjuntoTDA vertices = mapaBarrios.Vertices();
        while (!vertices.ConjuntoVacio()) {
            Barrio barrio = vertices.Elegir();
            ciudades.add(barrio.getCiudad().toLowerCase());
            vertices.Sacar(barrio);
        }
        return new ArrayList<>(ciudades);
    }

    public static List<Barrio> getBarriosPorCiudad(String ciudad) {
        return barriosDisponibles.stream()
                .filter(barrio -> barrio.getCiudad().equalsIgnoreCase(ciudad))
                .toList();
    }

    public static Barrio agregarBarrio(String ciudad, String barrio) {
        if (mapaBarrios == null) {
            throw new ReglaNegocioException("El mapa de barrios todavía no está inicializado.");
        }
        if (obtenerBarrioPorNombre(ciudad, barrio) != null) {
            throw new ReglaNegocioException("El barrio ya existe.");
        }
        Barrio nuevo = Barrio.builder().ciudad(ciudad).barrio(barrio).build();
        mapaBarrios.AgregarVertice(nuevo);
        barriosDisponibles.add(nuevo);
        return nuevo;
    }
}
