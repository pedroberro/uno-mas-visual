package model;

public class ENNotificadorCorreo implements IEstrategiaNotificacion{
    private IAdapterNotificacionCorreo adapter;
    @Override
    public void enviarNotificacion(Notificacion notification) {
        if (adapter != null) {
            adapter.enviarCorreo(notification);
        } else {
            System.out.println("⚠️ no hay adapter de correo configurado");
    }
}
    public void setAdapter(IAdapterNotificacionCorreo adapter) {
        this.adapter = adapter;
    }
}
