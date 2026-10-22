package unomas.web;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import unomas.controller.BarrioController;
import unomas.controller.PartidoController;
import unomas.controller.ReglaNegocioException;
import unomas.dto.AccionPartidoDTO;
import unomas.dto.AccionRequest;
import unomas.dto.ComentarioRequest;
import unomas.dto.CrearPartidoRequest;
import unomas.dto.PartidoDTO;
import unomas.model.Barrio;
import unomas.model.Deporte;
import unomas.model.DeporteFactory;
import unomas.model.EmparejamientoPorCercania;
import unomas.model.EmparejamientoPorHistorial;
import unomas.model.EmparejamientoPorNivel;
import unomas.model.IEstrategiaEmparejamiento;
import unomas.model.NivelDeJuego;
import unomas.model.Partido;
import unomas.model.ResultadoAccion;
import unomas.model.Usuario;

@RestController
@RequestMapping("/api/partidos")
public class PartidoRestController {

    @GetMapping
    public List<PartidoDTO> listar(@RequestParam(required = false) String usuarioId) {
        PartidoController controller = PartidoController.getInstance();
        controller.actualizarEstadosPorFecha();

        Usuario solicitante = usuarioId == null || usuarioId.isBlank() ? null : Parametros.usuario(usuarioId);

        return controller.getTodosLosPartidos().stream()
                .map(partido -> DtoMapper.partido(partido, solicitante))
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartidoDTO crear(@RequestBody CrearPartidoRequest request) {
        Usuario creador = Parametros.usuario(request.usuarioId());
        Deporte deporte = DeporteFactory.crear(request.deporte());
        Barrio zona = Parametros.barrio(request.barrio());

        if (request.duracionMinutos() == null || request.duracionMinutos() <= 0) {
            throw new ReglaNegocioException("La duración debe ser mayor a 0 minutos.");
        }
        if (request.horario() == null || request.horario().isBlank()) {
            throw new ReglaNegocioException("Falta el horario del partido.");
        }

        Date fecha = Parametros.fecha(request.fecha());
        NivelDeJuego nivel = Parametros.nivel(request.nivelRequerido());

        Partido partido = PartidoController.getInstance().crearPartido(
                deporte,
                request.duracionMinutos(),
                zona,
                fecha,
                request.horario().trim(),
                creador);

        partido.setNivelRequerido(nivel);
        partido.setMetodoDeEmparejamiento(
                crearEstrategia(partido, request.estrategia(), nivel, request.partidosPrevios()));

        return DtoMapper.partido(partido, creador);
    }

    @PostMapping("/{id}/unirse")
    public AccionPartidoDTO unirse(@PathVariable String id, @RequestBody AccionRequest request) {
        PartidoController controller = PartidoController.getInstance();
        Partido partido = controller.obtenerPorId(id);
        Usuario usuario = Parametros.usuario(request.usuarioId());
        return aplicarResultado(controller.unirseAPartido(partido, usuario), partido, usuario);
    }

    @PostMapping("/{id}/confirmar")
    public AccionPartidoDTO confirmar(@PathVariable String id, @RequestBody AccionRequest request) {
        PartidoController controller = PartidoController.getInstance();
        Partido partido = controller.obtenerPorId(id);
        Usuario usuario = Parametros.usuario(request.usuarioId());
        return aplicarResultado(controller.confirmarJugadores(partido, usuario), partido, usuario);
    }

    @PostMapping("/{id}/comenzar")
    public AccionPartidoDTO comenzar(@PathVariable String id, @RequestBody AccionRequest request) {
        PartidoController controller = PartidoController.getInstance();
        Partido partido = controller.obtenerPorId(id);
        Usuario usuario = Parametros.usuario(request.usuarioId());
        return aplicarResultado(controller.comenzar(partido, usuario), partido, usuario);
    }

    @PostMapping("/{id}/finalizar")
    public AccionPartidoDTO finalizar(@PathVariable String id, @RequestBody AccionRequest request) {
        PartidoController controller = PartidoController.getInstance();
        Partido partido = controller.obtenerPorId(id);
        Usuario usuario = Parametros.usuario(request.usuarioId());
        return aplicarResultado(controller.finalizar(partido, usuario), partido, usuario);
    }

    @PostMapping("/{id}/cancelar")
    public AccionPartidoDTO cancelar(@PathVariable String id, @RequestBody AccionRequest request) {
        PartidoController controller = PartidoController.getInstance();
        Partido partido = controller.obtenerPorId(id);
        Usuario usuario = Parametros.usuario(request.usuarioId());
        return aplicarResultado(controller.cancelar(partido, usuario), partido, usuario);
    }

    @PostMapping("/{id}/comentarios")
    public PartidoDTO comentar(@PathVariable String id, @RequestBody ComentarioRequest request) {
        PartidoController controller = PartidoController.getInstance();
        Partido partido = controller.obtenerPorId(id);
        Usuario usuario = Parametros.usuario(request.usuarioId());
        controller.agregarComentario(partido, usuario, request.contenido());
        return DtoMapper.partido(partido, usuario);
    }

    private IEstrategiaEmparejamiento crearEstrategia(Partido partido, String tipo, NivelDeJuego nivel,
            Integer partidosPrevios) {

        String normalizado = tipo == null || tipo.isBlank() ? "cercania" : tipo.trim().toLowerCase();
        return switch (normalizado) {
            case "nivel" -> new EmparejamientoPorNivel(partido, nivel);
            case "historial" -> new EmparejamientoPorHistorial(partido,
                    partidosPrevios == null ? 0 : Math.max(0, partidosPrevios));
            default -> new EmparejamientoPorCercania(partido, BarrioController.getMapaBarrios());
        };
    }

    private AccionPartidoDTO aplicarResultado(ResultadoAccion resultado, Partido partido, Usuario usuario) {
        if (!resultado.exito()) {
            throw new ReglaNegocioException(resultado.mensaje());
        }
        return new AccionPartidoDTO(DtoMapper.partido(partido, usuario), resultado.mensaje());
    }
}
