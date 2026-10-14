package view;

import model.Barrio;
import java.util.List;
import java.util.Scanner;

public class BarrioView {
    private final Scanner scanner = new Scanner(System.in);;



    public void mostrarBarriosDisponibles(List<Barrio> barrios) {
        if (barrios == null || barrios.isEmpty()) {
            System.out.println("No hay barrios disponibles para mostrar.");
            return;
        }

        System.out.println("Barrios disponibles:");
        for (Barrio b : barrios) {
            System.out.println("- " + b.getBarrio() + " (" + b.getCiudad() + ")");
        }
    }

    public void mostrarCiudadesDisponibles(List<String> ciudades) {
        if (ciudades == null || ciudades.isEmpty()) {
            System.out.println("No hay ciudades disponibles.");
            return;
        }

        System.out.println("Ciudades disponibles:");
        for (String ciudad : ciudades) {
            System.out.println("- " + ciudad);
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public String solicitarNombreCiudad() {
        System.out.print("Ingrese el nombre de la ciudad: ");
        return scanner.nextLine().trim();
    }

    public String solicitarNombreBarrio() {
        System.out.print("Ingrese el nombre del barrio: ");
        return scanner.nextLine().trim();
    }

    public void mostrarBarrioCreado(Barrio barrio) {
        System.out.println("Barrio agregado exitosamente: " +
                barrio.getBarrio() + " (" + barrio.getCiudad() + ")");
    }

    public void mostrarBarrioNoEncontrado(String ciudad, String barrio) {
        System.out.println("No se encontró el barrio '" + barrio + "' en la ciudad '" + ciudad + "'.");
    }

    public void mostrarBarrio(Barrio barrio) {
        if (barrio != null) {
            System.out.println("Barrio: " + barrio.getBarrio() + " (" + barrio.getCiudad() + ")");
        } else {
            System.out.println("Barrio no disponible.");
        }
    }
}
