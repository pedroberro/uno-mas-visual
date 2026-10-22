package model;

import java.util.*;
import lombok.*;

@Data
public class Partido implements IObservable {

    private Usuario creador;
    private Deporte deporte;
    private int duracionEnMinuntos;
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

    private List<Comentario> comentarios = new ArrayList<>();

    public Partido(Deporte deporte, int duracionEnMinuntos, Barrio zona, Date fecha) {
        this.deporte = deporte;
        this.duracionEnMinuntos = duracionEnMinuntos;
        this.zona = zona;
        this.fecha = fecha;
        this.jugadores = new ArrayList<>();
        this.observers = new ArrayList<>();

        this.jugadoresActuales = 0;
        this.jugadoresNecesarios = this.deporte.getCantidadJugadores();

        this.estado = new EPNecesitamosJugadores();

    }

    public void setMetodoDeEmparejamiento(IEstrategiaEmparejamiento estrategia) {
        this.metodoDeEmparejamiento = estrategia;
    }

    public List<Usuario> emparejarCandidatos(List<Usuario> candidatos) {
        if (metodoDeEmparejamiento != null) {
            return metodoDeEmparejamiento.emparejar(candidatos);
        } else {
            System.out.println("No se ha definido una estrategia de emparejamiento.");
            return new ArrayList<>();
        }
    }

    public void agregarJugador(IParticipante jugador) {
        jugadores.add(jugador);
        jugadoresActuales++;
        estado.agregarJugador(this);
    }

    public boolean tieneJugador(IParticipante jugador) {
        return jugadores.contains(jugador);
    }

    public void confirmarJugadores() {
        estado.confirmarJugadores(this);
    }

    public void comenzar() {
        estado.comenzar(this);
    }

    public void finalizar() {
        estado.finalizar(this);
    }

    public void cancelar() {
        estado.cancelar(this);
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
        observers.add(o);
    }

    @Override
    public void eliminarObserver(IObserver o) {
        observers.remove(o);
    }

    @Override
    public void notificar() {
        for (IObserver o : observers) {
            o.serNotificadoPor(this);
        }
        System.out.println("");
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

    public List<Comentario> getComentarios() {
        return this.comentarios;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public NivelDeJuego getNivelRequerido() {
        return nivelRequerido;
    }

    public void setNivelRequerido(NivelDeJuego nivelRequerido) {
        this.nivelRequerido = nivelRequerido;
    }

}