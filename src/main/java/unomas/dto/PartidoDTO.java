package unomas.dto;

import java.util.List;

public record PartidoDTO(
        String id,
        String deporte,
        int duracionMinutos,
        BarrioDTO zona,
        String fecha,
        String horario,
        String estado,
        String creadorId,
        String creadorNombre,
        List<JugadorDTO> jugadores,
        int jugadoresRequeridos,
        String nivelRequerido,
        String estrategia,
        List<ComentarioDTO> comentarios,
        boolean invitado) {
}
