package model;

public class Notificador {
    private IEstrategiaNotificacion estrategiaNotificacion;

    public void cambiarEstrategia (IEstrategiaNotificacion estrategia) {
        this.estrategiaNotificacion = estrategia;
    }

    public void enviarNotificacion(Notificacion notificacion) {
        if (estrategiaNotificacion == null) {
            System.out.println("⚠️ No se ha definido una estrategia de notificación.");
            return;
        }
        estrategiaNotificacion.enviarNotificacion(notificacion);
    }
}
