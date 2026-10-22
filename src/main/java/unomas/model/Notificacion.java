package unomas.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Notificacion {
    private String mensaje;
    private String destinatario;
    private Instant fecha;
}
