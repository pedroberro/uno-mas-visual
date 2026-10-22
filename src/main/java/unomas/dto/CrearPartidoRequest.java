package unomas.dto;

public record CrearPartidoRequest(
        String usuarioId,
        String deporte,
        String barrio,
        String fecha,
        String horario,
        Integer duracionMinutos,
        String nivelRequerido,
        String estrategia,
        Integer partidosPrevios) {
}
