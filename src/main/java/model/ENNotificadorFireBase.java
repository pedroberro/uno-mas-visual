package model;

public class ENNotificadorFireBase implements IEstrategiaNotificacion {

    private IAdapterNotificacionFireBase adapter;

    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        if (adapter != null) {
            adapter.enviarFireBase(notificacion);
        } else {
            System.out.println("⚠️ No hay adapter configurado para Firebase.");
        }
    }

    public void setAdapter(IAdapterNotificacionFireBase adapter) {
        this.adapter = adapter;
    }
}
