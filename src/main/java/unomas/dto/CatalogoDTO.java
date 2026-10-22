package unomas.dto;

import java.util.List;

public record CatalogoDTO(List<DeporteDTO> deportes, List<BarrioDTO> barrios, List<String> niveles) {
}
