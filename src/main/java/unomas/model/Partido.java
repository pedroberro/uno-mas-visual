package unomas.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Partido implements IObservable {

    @EqualsAndHashCode.Include
    private String id;

    private Usuario creador;
    private Deporte deporte;
    private int duracionEnMinutos;
    private Barrio zona;
    private Date fecha;
    private String horario;
    private IEstadoPartido estado;
    private List<IParticipante> jugadores;
    private IEstrategiaEmparejamiento metodoDeEmparejamiento;
    private NivelDeJuego nivelRequerido;

    private int jugadoresActuales;
    private int jugadoresNecesarios;

    private List<IObserver> observers;
    private List<Comentario> comentarios;

    public Partido(Deporte deporte, int duracionEnMinutos, Barrio zona, Date fecha) {
        this.id = UUID.randomUUID().toString();
        this.deporte = deporte;
        this.duracionEnMinutos = duracionEnMinutos;
        this.zona = zona;
        this.fecha = fecha;
        this.jugadores = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.jugadoresActuales = 0;
        this.jugadoresNecesarios = deporte.getCantidadJugadores();
        this.estado = new EPNecesitamosJugadores();
    }

    public void setMetodoDeEmparejamiento(IEstrategiaEmparejamiento estrategia) {
        this.metodoDeEmparejamiento = estrategia;
    }

    public String nombreEstrategia() {
        return metodoDeEmparejamiento == null ? null : metodoDeEmparejamiento.nombre();
    }

    public List<Usuario> emparejarCandidatos(List<Usuario> candidatos) {
        if (metodoDeEmparejamiento == null) {
            return new ArrayList<>();
        }
        return metodoDeEmparejamiento.emparejar(candidatos);
    }

    public boolean esInvitado(Usuario usuario) {
        if (metodoDeEmparejamiento == null || tieneJugador(usuario)) {
            return false;
        }
        return metodoDeEmparejamiento.emparejar(List.of(usuario)).contains(usuario);
    }

    public ResultadoAccion agregarJugador(IParticipante jugador) {
        if (!estado.admiteJugadores()) {
            return estado.agregarJugador(this);
        }
        if (jugadores.size() >= getCantidadJugadoresRequerida()) {
            return ResultadoAccion.error("El partido ya está completo.");
        }
        jugadores.add(jugador);
        jugadoresActuales = jugadores.size();
        return estado.agregarJugador(this);
    }

    public boolean tieneJugador(IParticipante jugador) {
        return jugadores.contains(jugador);
    }

    public ResultadoAccion confirmarJugadores() {
        return estado.confirmarJugadores(this);
    }

    public ResultadoAccion comenzar() {
        return estado.comenzar(this);
    }

    public ResultadoAccion finalizar() {
        return estado.finalizar(this);
    }

    public ResultadoAccion cancelar() {
        return estado.cancelar(this);
    }

    public String estadoActual() {
        return estado.nombre();
    }

    public void cambiarEstadoPartido(IEstadoPartido nuevoEstado) {
        this.estado = nuevoEstado;
        notificar();
    }

    @Override
    public void agregarObserver(IObserver o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void eliminarObserver(IObserver o) {
        observers.remove(o);
    }

    @Override
    public void notificar() {
        for (IObserver o : List.copyOf(observers)) {
            o.serNotificadoPor(this);
        }
    }

    public int getCantidadJugadoresActual() {
        return jugadores.size();
    }

    public int getCantidadJugadoresRequerida() {
        return deporte.getCantidadJugadores();
    }

    public void agregarComentario(Comentario comentario) {
        this.comentarios.add(comentario);
    }
}
