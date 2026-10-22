package unomas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import unomas.model.Comentario;
import unomas.model.Deporte;
import unomas.model.Barrio;
import unomas.model.Partido;
import unomas.model.ResultadoAccion;
import unomas.model.Usuario;

/**
 * Servicio de partidos. Mantiene el patrón Singleton y concentra las
 * operaciones del ciclo de vida de cada encuentro.
 */
public class PartidoController {

    private static PartidoController instancia;

    private final List<Partido> partidos = new ArrayList<>();

    private PartidoController() {
    }

    public static synchronized PartidoController getInstance() {
        if (instancia == null) {
            instancia = new PartidoController();
        }
        return instancia;
    }

    public synchronized Partido crearPartido(Deporte deporte, int duracionEnMinutos, Barrio zona, Date fecha,
            String horario, Usuario creador) {

        Partido partido = new Partido(deporte, duracionEnMinutos, zona, fecha);
        partido.setHorario(horario);
        partido.setCreador(creador);
        partido.agregarJugador(creador);
        partidos.add(partido);
        creador.agregarHistorial(partido);
        return partido;
    }

    public synchronized List<Partido> getTodosLosPartidos() {
        return List.copyOf(partidos);
    }

    public synchronized Optional<Partido> buscarPorId(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return partidos.stream()
                .filter(partido -> partido.getId().equals(id))
                .findFirst();
    }

    public synchronized Partido obtenerPorId(String id) {
        return buscarPorId(id)
                .orElseThrow(() -> new NoEncontradoException("No existe un partido con id " + id + "."));
    }

    public synchronized List<Partido> buscarPartidosDisponiblesPorZona(Barrio zona) {
        return partidos.stream()
                .filter(partido -> partido.getZona().equals(zona))
                .filter(partido -> partido.getJugadoresActuales() < partido.getCantidadJugadoresRequerida())
                .toList();
    }

    public synchronized ResultadoAccion unirseAPartido(Partido partido, Usuario usuario) {
        if (partido.tieneJugador(usuario)) {
            return ResultadoAccion.error("Ya estás anotado en este partido.");
        }
        if (!partido.getEstado().admiteJugadores()) {
            return ResultadoAccion.error("El partido no admite más jugadores en su estado actual.");
        }
        if (partido.getJugadoresActuales() >= partido.getCantidadJugadoresRequerida()) {
            return ResultadoAccion.error("El partido ya está completo.");
        }

        ResultadoAccion resultado = partido.agregarJugador(usuario);
        if (resultado.exito()) {
            usuario.agregarHistorial(partido);
            partido.agregarObserver(usuario);
        }
        return resultado;
    }

    public synchronized ResultadoAccion confirmarJugadores(Partido partido, Usuario usuario) {
        verificarCreador(partido, usuario);
        return partido.confirmarJugadores();
    }

    public synchronized ResultadoAccion comenzar(Partido partido, Usuario usuario) {
        verificarCreador(partido, usuario);
        return partido.comenzar();
    }

    public synchronized ResultadoAccion finalizar(Partido partido, Usuario usuario) {
        verificarCreador(partido, usuario);
        return partido.finalizar();
    }

    public synchronized ResultadoAccion cancelar(Partido partido, Usuario usuario) {
        verificarCreador(partido, usuario);
        return partido.cancelar();
    }

    public synchronized Comentario agregarComentario(Partido partido, Usuario usuario, String contenido) {
        if (contenido == null || contenido.isBlank()) {
            throw new ReglaNegocioException("El comentario no puede estar vacío.");
        }
        if (!partido.tieneJugador(usuario)) {
            throw new ReglaNegocioException("Solo los participantes del partido pueden comentar.");
        }
        Comentario comentario = new Comentario(usuario.getNombreCompleto(), contenido.trim(), new Date());
        partido.agregarComentario(comentario);
        return comentario;
    }

    public synchronized void actualizarEstadosPorFecha() {
        Date ahora = new Date();
        for (Partido partido : partidos) {
            if ("Confirmado".equals(partido.estadoActual()) && !partido.getFecha().after(ahora)) {
                partido.comenzar();
            }
        }
    }

    public synchronized void vaciar() {
        partidos.clear();
    }

    private void verificarCreador(Partido partido, Usuario usuario) {
        if (partido.getCreador() == null || !partido.getCreador().equals(usuario)) {
            throw new PermisoDenegadoException("Solo el creador del partido puede realizar esta acción.");
        }
    }
}
