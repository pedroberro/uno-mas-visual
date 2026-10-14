package model;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class ANAdapterCorreoJavaMail implements IAdapterNotificacionCorreo {

    @Override
    public void enviarCorreo(Notificacion notification) {
        final String remitente = System.getenv("UNO_MAS_SMTP_EMAIL");
        final String clave = System.getenv("UNO_MAS_SMTP_PASSWORD");
        if (remitente == null || remitente.isBlank() || clave == null || clave.isBlank()) {
            System.err.println("Configura UNO_MAS_SMTP_EMAIL y UNO_MAS_SMTP_PASSWORD para enviar correos.");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notification.getDestinatario()));
            message.setSubject("Notificación de partido");
            message.setText(notification.getMensaje());

            Transport.send(message);
            System.out.println("Correo enviado a: " + notification.getDestinatario());
            System.out.println("");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar el correo.");
        }
    }
}
