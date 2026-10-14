package controller;

import view.BarrioView;
import view.UsuarioView;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import model.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class UsuarioController {
    private UsuarioView vista = new UsuarioView();
    private List<Usuario> usuarios = new ArrayList<>();


    public UsuarioController() {
        cargarUsuariosIniciales();
    }
    

    private void cargarUsuariosIniciales() {
        String[] contrasenas = {"pass123", "abc456", "qwerty", "123456", "admin", "matirodriguez"};

        String[][] datos = {
            {"Juan", "Perez", "juanperez@gmail.com", "futbol", "INTERMEDIO", "caba", "palermo"},
            {"Ana", "Lopez", "analopez@gmail.com", "basquet", "AVANZADO", "caba", "recoleta"},
            {"Luis", "Gomez", "luisgomez@gmail.com", "tenis", "PRINCIPIANTE", "caba", "belgrano"},
            {"Marta", "Fernandez", "martafernandez@gmail.com", "voley", "INTERMEDIO", "caba", "san telmo"},
            {"Carlos", "Sanchez", "carlossanchez@gmail.com", "futbol", "AVANZADO", "caba", "caballito"},
            {"Matias", "Rodriguez", "matirodriguez@gmail.com", "tenis", "PRINCIPIANTE", "caba", "palermo"},

        };

        for (int i = 0; i < datos.length; i++) {
            String nombre = datos[i][0];
            String apellido = datos[i][1];
            String correo = datos[i][2];
            String deporteFav = datos[i][3];
            NivelDeJuego nivel = NivelDeJuego.valueOf(datos[i][4]);
            String ciudad = datos[i][5];
            String barrioStr = datos[i][6];

            Barrio barrio = BarrioController.obtenerBarrioPorNombre(ciudad, barrioStr);
            if (barrio == null) {
                System.out.println("Barrio no encontrado: " + ciudad + " - " + barrioStr + ". No se creó el usuario " + nombre);
                continue;
            }

            String contrasenaHasheada = hashearContrasena(contrasenas[i]);
            Usuario usuario = new Usuario(nombre, apellido, correo, contrasenaHasheada, deporteFav, nivel, barrio);
            usuarios.add(usuario);
        }
    }


    private String hashearContrasena(String contrasena) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(contrasena.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Error al encriptar la contraseña", e);
    }
}

    public void registrarUsuario() {
        String nombre = vista.solicitarInput("Introduzca su nombre:");
        vista.mostrarMensaje("");

        String apellido = vista.solicitarInput("Introduzca su apellido:");
        vista.mostrarMensaje("");

        String correo = null;
        while (true) {
            String input = vista.solicitarInput("Introduzca su correo:");
            vista.mostrarMensaje("");
            // Validación de formato con expresión regular
            if (!input.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                vista.mostrarMensaje("Formato de correo inválido.");
                continue;
            }

            //Verificar si el correo ya está registrado
            //Esto evita usuarios duplicados
            boolean correoExiste = false;
            for (Usuario u : usuarios) {
                if (u.getCorreo().equalsIgnoreCase(input)) {
                    correoExiste = true;
                    break;
                }
            }

            if (correoExiste) {
                vista.mostrarMensaje("Este correo ya está registrado.");
            } else {
                correo = input;
                break;
            }
        }

        String contrasena = vista.solicitarInput("Introduzca su contraseña:");
        vista.mostrarMensaje("");

        // Mostrar deportes disponibles y pedir selección por nombre
        List<String> deportesDisponibles = List.of("futbol", "basquet", "tenis", "voley");
        vista.mostrarLista("Deportes disponibles:", deportesDisponibles);
        String deporteFav = null;
        while (true) {
            String input = vista.solicitarInput("Escriba su deporte favorito exactamente como aparece en la lista:");
            vista.mostrarMensaje("");
            if (deportesDisponibles.contains(input)) {
                deporteFav = input;
                break;
            } else {
                vista.mostrarMensaje("Deporte no válido.");
            }
        }

        NivelDeJuego[] niveles = NivelDeJuego.values();
        vista.mostrarMensaje("Niveles de dificultad disponibles:");
        for (int i = 0; i < niveles.length; i++) {
            vista.mostrarMensaje((i + 1) + ". " + niveles[i]);
        }
        vista.mostrarMensaje("");
        int opcionNivel = vista.seleccionarOpcion("Seleccione su nivel con el numero correspondiente:", niveles.length);
        vista.mostrarMensaje("");
        NivelDeJuego nivel = niveles[opcionNivel - 1];

        // Obtener lista de ciudades disponibles
        List<String> ciudadesDisponibles = BarrioController.getCiudadesDisponibles();

        String ciudad = null;
        while (true) {
            vista.mostrarLista("Ciudades disponibles:", ciudadesDisponibles);
            vista.mostrarMensaje("");
            ciudad = vista.solicitarInput("Ingrese su ciudad:").toLowerCase();
            vista.mostrarMensaje("");
            if (ciudadesDisponibles.contains(ciudad)) break;
            vista.mostrarMensaje("Ciudad no válida.");
        }

        // Mostrar barrios disponibles antes de pedir el barrio
        List<Barrio> barriosObjetos = BarrioController.getBarriosPorCiudad(ciudad);
        List<String> barriosDisponibles = new ArrayList<>();
        for (Barrio b : barriosObjetos) {
                barriosDisponibles.add(b.getBarrio().toLowerCase());
        }

        String barrio = null;
        if (barriosDisponibles.isEmpty()) {
            vista.mostrarMensaje("No hay barrios registrados. Ingrese uno nuevo:");
            barrio = vista.solicitarInput("Ingrese su barrio:").toLowerCase();
        } else {
            while (true) {
                vista.mostrarLista("Barrios disponibles:", barriosDisponibles);
                vista.mostrarMensaje("");
                barrio = vista.solicitarInput("Ingrese su barrio:").toLowerCase();
                vista.mostrarMensaje("");
                if (barriosDisponibles.contains(barrio)) break;
                vista.mostrarMensaje("Barrio no válido.");
            }
        }

        Barrio zona = BarrioController.obtenerBarrioPorNombre(ciudad, barrio);
        if (zona == null) {
            zona = BarrioController.agregarBarrio(ciudad, barrio);
        }

        String contrasenaHasheada = hashearContrasena(contrasena);
        Usuario nuevoUsuario = new Usuario(nombre, apellido, correo, contrasenaHasheada, deporteFav, nivel, zona);
        usuarios.add(nuevoUsuario);
        vista.mostrarMensaje("¡Usuario registrado con éxito!");
        vista.mostrarMensaje("");
    }

    public Usuario iniciarSesion() {
        String correo = vista.solicitarInput("Introduzca su correo:");
        vista.mostrarMensaje("");
        String contrasena = vista.solicitarInput("Introduzca su contraseña:");
        vista.mostrarMensaje("");
        String contrasenaHasheada = hashearContrasena(contrasena);

    // Buscar usuario por correo
    for (Usuario u : usuarios) {
        if (u.getCorreo().equalsIgnoreCase(correo)) {
            if (u.getContrasenia().equals(contrasenaHasheada)) {
                vista.mostrarMensaje("¡Inicio de sesión exitoso!");
                vista.mostrarMensaje("");
                return u; //Retorna el usuario logueado
            }
            break;
        }
    }

    vista.mostrarMensaje("Correo o contraseña incorrectos.");
        vista.mostrarMensaje("");
    return null; //Login fallido
}

}
