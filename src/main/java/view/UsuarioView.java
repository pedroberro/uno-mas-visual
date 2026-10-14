package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Partido;

public class UsuarioView {
    private Scanner scanner = new Scanner(System.in);

    public String solicitarInput(String mensaje) {
        System.out.println(mensaje);
        return scanner.nextLine().trim();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarLista(String titulo, List<String> opciones) {
        System.out.println(titulo);
        for (String opcion : opciones) {
            System.out.println(". " + opcion);
        }
        System.out.println("");
    }

    public int seleccionarOpcion(String mensaje, int cantidadOpciones) {
        while (true) {
            try {
                System.out.println(mensaje);
                int opcion = Integer.parseInt(scanner.nextLine());
                if (opcion >= 1 && opcion <= cantidadOpciones) {
                    return opcion;
                }
            } catch (NumberFormatException e) {
                // Nada
            }
            System.out.println("Opción inválida. Intente de nuevo.");
        }
    }
}
