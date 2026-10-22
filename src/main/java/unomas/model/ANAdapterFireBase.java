package unomas.model;

public class ANAdapterFireBase implements IAdapterNotificacionFireBase{
    @Override
    public void enviarFireBase(Notificacion notification) {
    System.out.println("🔥 [Firebase] simulando envio a: " 
        + notification.getDestinatario() 
        + " | mensaje: " + notification.getMensaje());
}
}
