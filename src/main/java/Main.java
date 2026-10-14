import java.util.Arrays;
import java.util.List;

import controller.BarrioController;
import model.Barrio;
import model.IMGrafoTDA;
import model.MGrafo;
import view.InterfazGrafica;

public class Main {

    public static void main(String[] args) {

        // Crear el grafo
        IMGrafoTDA mapa = new MGrafo();
        mapa.InicializarGrafo();

        // Crear barrios con ciudad y nombre de barrio usando lombok Builder
        Barrio palermo = Barrio.builder().ciudad("caba").barrio("palermo").build();
        Barrio recoleta = Barrio.builder().ciudad("caba").barrio("recoleta").build();
        Barrio belgrano = Barrio.builder().ciudad("caba").barrio("belgrano").build();
        Barrio sanTelmo = Barrio.builder().ciudad("caba").barrio("san telmo").build();
        Barrio caballito = Barrio.builder().ciudad("caba").barrio("caballito").build();

        // Agregar vértices
        mapa.AgregarVertice(palermo);
        mapa.AgregarVertice(recoleta);
        mapa.AgregarVertice(belgrano);
        mapa.AgregarVertice(sanTelmo);
        mapa.AgregarVertice(caballito);

        // Agregar aristas (hiperconexión con pesos)
        mapa.AgregarArista(palermo, recoleta, 3);
        mapa.AgregarArista(palermo, belgrano, 4);
        mapa.AgregarArista(palermo, sanTelmo, 8);
        mapa.AgregarArista(palermo, caballito, 5);
        mapa.AgregarArista(recoleta, belgrano, 5);
        mapa.AgregarArista(recoleta, sanTelmo, 6);
        mapa.AgregarArista(recoleta, caballito, 4);
        mapa.AgregarArista(belgrano, sanTelmo, 10);
        mapa.AgregarArista(belgrano, caballito, 6);
        mapa.AgregarArista(sanTelmo, caballito, 7);


        List<Barrio> listaBarrios = Arrays.asList(palermo, recoleta, belgrano, sanTelmo, caballito);
        BarrioController.inicializarBarrios(listaBarrios, mapa);

        InterfazGrafica interfaz = InterfazGrafica.getInstancia();
        interfaz.mensajeInicial();

    }
}
