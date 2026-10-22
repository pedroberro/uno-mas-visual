package unomas.dto;

import java.util.List;

public record UsuarioDTO(
        String id,
        String nombre,
        String apellido,
        String correo,
        String deporteFav,
        String nivel,
        BarrioDTO zona,
        List<NotificacionDTO> notificaciones) {
}
