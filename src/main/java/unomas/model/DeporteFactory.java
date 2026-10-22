package unomas.model;

import java.util.List;

/**
 * Fábrica simple de deportes: traduce el identificador que llega desde la API
 * a la implementación concreta de {@link Deporte}.
 */
public final class DeporteFactory {

    private DeporteFactory() {
    }

    public static Deporte crear(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Falta indicar el deporte.");
        }
        return switch (id.toLowerCase()) {
            case "futbol" -> new DFutbol();
            case "basquet" -> new DBasquet();
            case "tenis" -> new DTenis();
            case "voley" -> new DVoley();
            default -> throw new IllegalArgumentException("Deporte desconocido: " + id);
        };
    }

    public static List<Deporte> catalogo() {
        return List.of(new DFutbol(), new DBasquet(), new DTenis(), new DVoley());
    }

    public static String idDe(Deporte deporte) {
        if (deporte instanceof DFutbol) {
            return "futbol";
        }
        if (deporte instanceof DBasquet) {
            return "basquet";
        }
        if (deporte instanceof DTenis) {
            return "tenis";
        }
        if (deporte instanceof DVoley) {
            return "voley";
        }
        return "desconocido";
    }
}
