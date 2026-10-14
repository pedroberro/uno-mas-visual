package controller;

import model.Barrio;
import model.IMGrafoTDA;
import view.BarrioView;

import java.util.*;

public class BarrioController {
    private static List<Barrio> barriosDisponibles;
    private static IMGrafoTDA mapaBarrios;
    private static BarrioView barrioView = new BarrioView();

    public static void inicializarBarrios(List<Barrio> barrios, IMGrafoTDA grafo) {
        barriosDisponibles = barrios;
        mapaBarrios = grafo;
    }

    public static Barrio obtenerBarrioPorNombre(String ciudad, String barrio) {
        var vertices = mapaBarrios.Vertices();
        while (!vertices.ConjuntoVacio()) {
            Barrio b = vertices.Elegir();
            if (b.getCiudad().equalsIgnoreCase(ciudad) && b.getBarrio().equalsIgnoreCase(barrio)) {
                return b;
            }
            vertices.Sacar(b);
        }
        return null;
    }

    public static void imprimirBarriosDisponibles() {
        var vertices = mapaBarrios.Vertices();
        List<Barrio> lista = new ArrayList<>();

        while (!vertices.ConjuntoVacio()) {
            Barrio b = vertices.Elegir();
            lista.add(b);
            vertices.Sacar(b);
        }

        barrioView.mostrarBarriosDisponibles(lista);
    }

    public static List<Barrio> getBarriosDisponibles() {
        return barriosDisponibles;
    }

    public static IMGrafoTDA getMapaBarrios() {
        return mapaBarrios;
    }

    public static List<String> getCiudadesDisponibles() {
        Set<String> ciudades = new HashSet<>();
        var vertices = mapaBarrios.Vertices();

        while (!vertices.ConjuntoVacio()) {
            Barrio b = vertices.Elegir();
            ciudades.add(b.getCiudad().toLowerCase());
            vertices.Sacar(b);
        }

        return new ArrayList<>(ciudades);
    }

    public static List<Barrio> getBarriosPorCiudad(String ciudad) {
        List<Barrio> barrios = new ArrayList<>();
        var vertices = mapaBarrios.Vertices();
        while (!vertices.ConjuntoVacio()) {
            Barrio b = vertices.Elegir();
            if (b.getCiudad().equalsIgnoreCase(ciudad)) {
                barrios.add(b);
            }
            vertices.Sacar(b);
        }
        return barrios;
    }

    public static Barrio agregarBarrio(String ciudad, String barrio) {
        Barrio nuevo = Barrio.builder().ciudad(ciudad).barrio(barrio).build();
        mapaBarrios.AgregarVertice(nuevo);
        barriosDisponibles.add(nuevo);
        barrioView.mostrarBarrioCreado(nuevo);
        return nuevo;
    }

}