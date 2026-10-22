package unomas.web;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import unomas.dto.BarrioDTO;
import unomas.dto.ComentarioDTO;
import unomas.dto.JugadorDTO;
import unomas.dto.NotificacionDTO;
import unomas.dto.PartidoDTO;
import unomas.dto.UsuarioDTO;
import unomas.model.Barrio;
import unomas.model.Comentario;
import unomas.model.DeporteFactory;
import unomas.model.Notificacion;
import unomas.model.Partido;
import unomas.model.Usuario;

public final class DtoMapper {

    private DtoMapper() {
    }

    public static BarrioDTO barrio(Barrio barrio) {
        return new BarrioDTO(barrio.getCiudad(), barrio.getBarrio());
    }

    public static NotificacionDTO notificacion(Notificacion notificacion) {
        return new NotificacionDTO(notificacion.getMensaje(), notificacion.getFecha().toString());
    }

    public static UsuarioDTO usuario(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo(),
                usuario.getDeporteFav(),
                usuario.getNivelDeJuego().name(),
                barrio(usuario.getZona()),
                usuario.getNotificaciones().stream().map(DtoMapper::notificacion).toList());
    }

    public static PartidoDTO partido(Partido partido, Usuario solicitante) {
        List<JugadorDTO> jugadores = partido.getJugadores().stream()
                .filter(Usuario.class::isInstance)
                .map(Usuario.class::cast)
                .map(jugador -> new JugadorDTO(
                        jugador.getId(),
                        jugador.getNombreCompleto(),
                        jugador.getIniciales(),
                        jugador.getNivelDeJuego().name(),
                        partido.getCreador() != null && partido.getCreador().equals(jugador)))
                .toList();

        Usuario creador = partido.getCreador();

        return new PartidoDTO(
                partido.getId(),
                DeporteFactory.idDe(partido.getDeporte()),
                partido.getDuracionEnMinutos(),
                barrio(partido.getZona()),
                fechaIso(partido.getFecha()),
                partido.getHorario(),
                partido.estadoActual(),
                creador == null ? null : creador.getId(),
                creador == null ? null : creador.getNombreCompleto(),
                jugadores,
                partido.getCantidadJugadoresRequerida(),
                partido.getNivelRequerido() == null ? null : partido.getNivelRequerido().name(),
                partido.nombreEstrategia(),
                partido.getComentarios().stream().map(DtoMapper::comentario).toList(),
                solicitante != null && partido.esInvitado(solicitante));
    }

    public static ComentarioDTO comentario(Comentario comentario) {
        return new ComentarioDTO(
                comentario.getAutor(),
                comentario.getContenido(),
                instantIso(comentario.getFecha()));
    }

    public static String fechaIso(Date fecha) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(fecha.getTime()), ZoneId.systemDefault()).toString();
    }

    private static String instantIso(Date fecha) {
        return Instant.ofEpochMilli(fecha.getTime()).toString();
    }
}
