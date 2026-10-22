package unomas.model;

public interface IMGrafoTDA {
    void InicializarGrafo();
    void AgregarVertice(Barrio v);                 // Grafo inicializado y vértice no existe
    void AgregarArista(Barrio o, Barrio d, int p);  // Grafo inicializado y vértices existen
    void EliminarVertice(Barrio v);                // Grafo inicializado y vértice existe
    void EliminarArista(Barrio o, Barrio d);        // Grafo inicializado y vértices existen
    IMConjuntoTDA Vertices();                       // Grafo inicializado
    boolean ExisteArista(Barrio o, Barrio d);       // Grafo inicializado y vértices existen
    int PesoArista(Barrio o, Barrio d);
}
