package controller;

import model.Partido;
import model.Barrio;
import model.Deporte;
import model.Usuario;
import view.PartidoView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PartidoController {

    private static PartidoController instancia;
    private PartidoView partidoView;
    private List<Partido> partidos;

    private PartidoController() {
        this.partidos = new ArrayList<>();
        partidoView = new PartidoView();
        // cargarPartidosIniciales();  // Carga partidos al inicio
    }

    public static PartidoController getInstance() {
        if (instancia == null) {
            instancia = new PartidoController();
        }
        return instancia;
    }

    public Partido crearPartido(Deporte deporte, int duracion, Barrio zona, Date fecha, String horario, Usuario creador) {
        Partido nuevoPartido = new Partido(deporte, duracion, zona, fecha);
        nuevoPartido.setHorario(horario);
        nuevoPartido.setCreador(creador);
        nuevoPartido.agregarJugador(creador);

        registrarPartido(nuevoPartido);
        creador.agregarHistorial(nuevoPartido);

        return nuevoPartido;
    }

    public void registrarPartido(Partido partido) {
        partidos.add(partido);
    }

    public List<Partido> buscarPartidosDisponiblesPorZona(Barrio zona) {
        List<Partido> disponibles = new ArrayList<>();
        for (Partido p : partidos) {
            if (p.getZona().equals(zona) && p.getJugadoresActuales() < p.getJugadoresNecesarios()) {
                disponibles.add(p);
            }
        }
        return disponibles;
    }

    public void mostrarPartidosDisponibles(List<Partido> partidos) {
        partidoView.mostrarPartidosDisponibles(partidos);
    }

    public boolean unirseAPartido(Partido partido, Usuario usuario) {
        if (partido.tieneJugador(usuario)) {
            partidoView.mostrarMensaje("Ya estás unido a este partido.");
            return false;
        }
        if (partido.getJugadoresActuales() < partido.getJugadoresNecesarios()) {
            partido.agregarJugador(usuario);
            usuario.agregarHistorial(partido);
            partido.agregarObserver(usuario); //agrego al usuario como observador del partido
            //partidoView.mostrarMensaje(usuario.getNombre() + " se ha unido al partido.");
            return true;
        } else {
            partidoView.mostrarMensaje("El partido ya está completo.");
            return false;
        }
    }

    public List<Partido> getTodosLosPartidos() {
        return partidos;
    }

    public void actualizarEstadosPorFecha() {
    Date ahora = new Date();
    for (Partido p : partidos) {
        if (p.estadoActual().equalsIgnoreCase("Confirmado") && !p.getFecha().after(ahora)) {
            p.comenzar();
        }
    }
}

    public void verPartidosCreados(Usuario usuario) {
        List<Partido> misPartidos = usuario.getHistorial();
        List<Partido> partidosCreados = new ArrayList<>();
        int contador = 1;

        for (Partido p : misPartidos) {
            if (p.getCreador() != null && p.getCreador().equals(usuario)) {
                partidosCreados.add(p);
            }
        }

        partidoView.mostrarPartidosCreados(partidosCreados);

        if (partidosCreados.isEmpty()) {
            return;
        }

        if (partidoView.preguntarSiCancelar()) {
            int opcion = partidoView.pedirIndiceDeCancelacion(partidosCreados.size());
            if (opcion > 0 && opcion <= partidosCreados.size()) {
                Partido partidoACancelar = partidosCreados.get(opcion - 1);
                partidoACancelar.cancelar();
                partidoView.mostrarMensaje("Partido cancelado.");
                partidoView.mostrarMensaje("");
            } else if (opcion == 0) {
                partidoView.mostrarMensaje("Operación cancelada.");
            } else {
                partidoView.mostrarMensaje("Opción inválida.");
            }
        }
    }

    public void verPartidosUnidos(Usuario usuario) {
        List<Partido> misPartidos = usuario.getHistorial();
        List<Partido> partidosUnidos = new ArrayList<>();

        for (Partido p : misPartidos) {
            if (p.tieneJugador(usuario) && (p.getCreador() == null || !p.getCreador().equals(usuario))) {
                partidosUnidos.add(p);
            }
        }

        partidoView.mostrarPartidosUnidos(partidosUnidos);
    }

    public void setPartidoView(PartidoView partidoView) {
        this.partidoView = partidoView;
}
}

