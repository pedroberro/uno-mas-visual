package model;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comentario {
    private String autor;     // nombre del jugador o "sistema"
    private String contenido; // mensaje
    private Date fecha;       // cuándo se hizo el comentario
}
