package unomas.dto;

public record JugadorDTO(
        String id,
        String nombreCompleto,
        String iniciales,
        String nivel,
        boolean creador) {
}
