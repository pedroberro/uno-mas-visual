package unomas.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements IParticipante, IObserver {

    private static final int MAXIMO_NOTIFICACIONES = 30;

    @EqualsAndHashCode.Include
    private String id;

    private String nombre;
    private String apellido;
    private String correo;
    private String contrasenia;
    private String deporteFav;
    private NivelDeJuego nivelDeJuego;
    private Barrio zona;

    private List<Partido> historial;
    private List<IObservable> observables;
    private List<Notificacion> notificaciones;

    public Usuario(String nombre, String apellido, String correo, String contrasenia, String deporteFav,
            NivelDeJuego nivelDeJuego, Barrio zona) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.deporteFav = deporteFav;
        this.nivelDeJuego = nivelDeJuego;
        this.zona = zona;
        this.historial = new ArrayList<>();
        this.observables = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public String getIniciales() {
        String inicialNombre = nombre.isEmpty() ? "" : nombre.substring(0, 1);
        String inicialApellido = apellido.isEmpty() ? "" : apellido.substring(0, 1);
        return (inicialNombre + inicialApellido).toUpperCase();
    }

    @Override
    public void serNotificadoPor(IObservable o) {
        if (!observables.contains(o)) {
            observables.add(o);
        }

        String mensaje = o instanceof Partido partido
                ? "El partido de " + partido.getDeporte().getNombre() + " en " + partido.getZona().getBarrio()
                        + " cambió a: " + partido.estadoActual()
                : "Recibiste una nueva notificación.";

        notificaciones.add(new Notificacion(mensaje, getNombreCompleto(), Instant.now()));
        if (notificaciones.size() > MAXIMO_NOTIFICACIONES) {
            notificaciones.remove(0);
        }
    }

    @Override
    public void agregarHistorial(Partido partido) {
        this.historial.add(partido);
    }

    public void limpiarHistorial() {
        this.historial.clear();
        this.observables.clear();
        this.notificaciones.clear();
    }
}
