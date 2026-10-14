package model;

import java.util.*;

public class Equipo  implements IParticipante, IObserver{
    private List<Usuario> jugadores;
    private int cantidadJugadores = jugadores.size();

    private List<IObservable> observables;

    public void agregarJugador(Usuario jugador) {
        jugadores.add(jugador);
    }

    @Override
    public void serNotificadoPor(IObservable o) {
        observables.add(o);
    }

    @Override
    public void agregarHistorial(Partido p) {
        for (Usuario u : this.jugadores){
            u.agregarHistorial(p);
        }
    }
}
