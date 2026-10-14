package view;

import java.util.List;
import java.util.Scanner;

import model.Partido;

public class PartidoView {
    private Scanner scanner = new Scanner(System.in);
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarPartidosDisponibles(List<Partido> partidos) {
        if (partidos.isEmpty()) {
            System.out.println("No hay partidos disponibles en esta zona.");
            System.out.println("");
        } else {
            System.out.println("Partidos encontrados:");
            for (int i = 0; i < partidos.size(); i++) {
                Partido p = partidos.get(i);
                System.out.println((i + 1) + ". " + p.getDeporte().getNombre() + " - " + p.getFecha() + " - " + p.getHorario() +
                        " | Jugadores: " + p.getJugadoresActuales() + "/" + p.getJugadoresNecesarios());
            }
            System.out.println("");
        }
    }

    public void mostrarPartidosCreados(List<Partido> partidos) {
        if (partidos.isEmpty()) {
            System.out.println("No has creado partidos.");
            System.out.println("");
            return;
        }
        System.out.println("=== Partidos que creaste ===");
        mostrarPartidosDetallados(partidos);
        System.out.println("");
    }

    public void mostrarPartidosUnidos(List<Partido> partidos) {
        if (partidos.isEmpty()) {
            System.out.println("No te has unido a ningún partido.");
            System.out.println("");
            return;
        }
        System.out.println("=== Partidos a los que te uniste ===");
        mostrarPartidosDetallados(partidos);
        System.out.println("");

    }

    public boolean preguntarSiCancelar() {
        System.out.println("¿Deseas cancelar alguno de estos partidos? (s/n)");
        String respuesta = scanner.nextLine().trim().toLowerCase();
        System.out.println("");
        return respuesta.equals("s");
    }

    public int pedirIndiceDeCancelacion(int max) {
        System.out.println("Ingrese el número del partido a cancelar (0 para cancelar):");
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion >= 0 && opcion <= max) {
                return opcion;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
        System.out.println("");
        return -1;
    }

    public void mostrarPartidosDetallados(List<Partido> partidos) {
    int contador = 1;
    for (Partido p : partidos) {
        System.out.println("--------------------------------------------------");
        System.out.println("Partido " + (contador++) + ":");
        System.out.println("Deporte: " + p.getDeporte().getNombre());
        System.out.println("Fecha: " + p.getFecha());
        System.out.println("Horario: " + p.getHorario());
        System.out.println("Zona: " + p.getZona().getBarrio() + " (" + p.getZona().getCiudad() + ")");
        System.out.println("Duración: " + p.getDuracionEnMinuntos() + " minutos");
        System.out.println("Jugadores actuales: " + p.getJugadoresActuales() + "/" + p.getJugadoresNecesarios());
        System.out.println("Nivel requerido: " + (p.getNivelRequerido() != null ? p.getNivelRequerido() : "No definido"));
        System.out.println("Estado: " + p.estadoActual());
        if (p.getComentarios() != null && !p.getComentarios().isEmpty()) {
            p.getComentarios().forEach(c -> System.out.println("Comentario: " + c.getAutor() + ": " + c.getContenido()));
        } else {
            System.out.println("Comentario: No existe comentario para este partido");
        }
        System.out.println("--------------------------------------------------");
    }
    }
    
}
