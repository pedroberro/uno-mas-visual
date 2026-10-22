package unomas.model;

/**
 * Resultado de una acción del dominio. Reemplaza los mensajes por consola:
 * el servicio o la API deciden cómo mostrarlo.
 */
public record ResultadoAccion(boolean exito, String mensaje) {

    public static ResultadoAccion ok(String mensaje) {
        return new ResultadoAccion(true, mensaje);
    }

    public static ResultadoAccion error(String mensaje) {
        return new ResultadoAccion(false, mensaje);
    }
}
