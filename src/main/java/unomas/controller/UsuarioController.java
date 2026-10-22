package unomas.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import unomas.model.Barrio;
import unomas.model.NivelDeJuego;
import unomas.model.Usuario;

/**
 * Servicio de usuarios. Mantiene el patrón Singleton: es el único punto de
 * acceso a la lista de usuarios registrados.
 */
public class UsuarioController {

    private static final String CIUDAD_POR_DEFECTO = "caba";

    private static UsuarioController instancia;

    private final List<Usuario> usuarios = new ArrayList<>();

    private UsuarioController() {
        cargarUsuariosIniciales();
    }

    public static synchronized UsuarioController getInstance() {
        if (instancia == null) {
            instancia = new UsuarioController();
        }
        return instancia;
    }

    private void cargarUsuariosIniciales() {
        String[] contrasenas = { "pass123", "abc456", "qwerty", "123456", "admin", "matirodriguez" };

        String[][] datos = {
                { "Juan", "Perez", "juanperez@gmail.com", "futbol", "INTERMEDIO", "palermo" },
                { "Ana", "Lopez", "analopez@gmail.com", "basquet", "AVANZADO", "recoleta" },
                { "Luis", "Gomez", "luisgomez@gmail.com", "tenis", "PRINCIPIANTE", "belgrano" },
                { "Marta", "Fernandez", "martafernandez@gmail.com", "voley", "INTERMEDIO", "san telmo" },
                { "Carlos", "Sanchez", "carlossanchez@gmail.com", "futbol", "AVANZADO", "caballito" },
                { "Matias", "Rodriguez", "matirodriguez@gmail.com", "tenis", "PRINCIPIANTE", "palermo" },
        };

        for (int i = 0; i < datos.length; i++) {
            Barrio barrio = BarrioController.obtenerBarrioPorNombre(CIUDAD_POR_DEFECTO, datos[i][5]);
            if (barrio == null) {
                throw new IllegalStateException("No se pudo inicializar el usuario " + datos[i][0]
                        + ": el barrio " + datos[i][5] + " no está en el mapa.");
            }

            usuarios.add(new Usuario(
                    datos[i][0],
                    datos[i][1],
                    datos[i][2],
                    hashearContrasena(contrasenas[i]),
                    datos[i][3],
                    NivelDeJuego.valueOf(datos[i][4]),
                    barrio));
        }
    }

    public synchronized Usuario registrar(String nombre, String apellido, String correo, String contrasenia,
            String deporteFav, NivelDeJuego nivel, Barrio zona) {

        if (nombre == null || nombre.isBlank() || apellido == null || apellido.isBlank()) {
            throw new ReglaNegocioException("El nombre y el apellido son obligatorios.");
        }
        if (correo == null || !correo.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ReglaNegocioException("El correo no tiene un formato válido.");
        }
        if (contrasenia == null || contrasenia.length() < 6) {
            throw new ReglaNegocioException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (nivel == null) {
            throw new ReglaNegocioException("Falta indicar el nivel de juego.");
        }
        if (zona == null) {
            throw new ReglaNegocioException("Falta indicar el barrio.");
        }

        String correoNormalizado = correo.trim().toLowerCase();
        if (buscarPorCorreo(correoNormalizado).isPresent()) {
            throw new ReglaNegocioException("Ese correo ya está registrado.");
        }

        Usuario usuario = new Usuario(
                nombre.trim(),
                apellido.trim(),
                correoNormalizado,
                hashearContrasena(contrasenia),
                deporteFav,
                nivel,
                zona);
        usuarios.add(usuario);
        return usuario;
    }

    public synchronized Optional<Usuario> autenticar(String correo, String contrasenia) {
        if (correo == null || contrasenia == null) {
            return Optional.empty();
        }
        return buscarPorCorreo(correo)
                .filter(usuario -> usuario.getContrasenia().equals(hashearContrasena(contrasenia)));
    }

    public synchronized Optional<Usuario> buscarPorCorreo(String correo) {
        if (correo == null) {
            return Optional.empty();
        }
        String normalizado = correo.trim().toLowerCase();
        return usuarios.stream()
                .filter(usuario -> usuario.getCorreo().equalsIgnoreCase(normalizado))
                .findFirst();
    }

    public synchronized Optional<Usuario> buscarPorId(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return usuarios.stream()
                .filter(usuario -> usuario.getId().equals(id))
                .findFirst();
    }

    public synchronized Usuario obtenerPorId(String id) {
        return buscarPorId(id)
                .orElseThrow(() -> new NoEncontradoException("No existe un usuario con id " + id + "."));
    }

    public synchronized List<Usuario> getUsuarios() {
        return List.copyOf(usuarios);
    }

    public synchronized void limpiarHistoriales() {
        usuarios.forEach(Usuario::limpiarHistorial);
    }

    private static String hashearContrasena(String contrasena) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(contrasena.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo hashear la contraseña.", e);
        }
    }
}
