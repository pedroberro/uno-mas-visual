package unomas.dto;

public record RegistroRequest(
        String nombre,
        String apellido,
        String correo,
        String contrasenia,
        String deporteFav,
        String nivel,
        String barrio) {
}
