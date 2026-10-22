package unomas.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unomas.controller.BarrioController;
import unomas.dto.BarrioDTO;
import unomas.dto.CatalogoDTO;
import unomas.dto.DeporteDTO;
import unomas.model.DeporteFactory;
import unomas.model.NivelDeJuego;

@RestController
@RequestMapping("/api/catalogos")
public class CatalogoController {

    @GetMapping
    public CatalogoDTO catalogo() {
        List<DeporteDTO> deportes = DeporteFactory.catalogo().stream()
                .map(deporte -> new DeporteDTO(
                        DeporteFactory.idDe(deporte),
                        deporte.getNombre(),
                        deporte.getCantidadJugadores()))
                .toList();

        List<BarrioDTO> barrios = BarrioController.getBarriosDisponibles().stream()
                .map(DtoMapper::barrio)
                .toList();

        List<String> niveles = Arrays.stream(NivelDeJuego.values())
                .map(Enum::name)
                .toList();

        return new CatalogoDTO(deportes, barrios, niveles);
    }
}
