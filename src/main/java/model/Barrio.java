package model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Barrio {
    private String ciudad;
    private String barrio;
    public int distancia (Barrio b, IMGrafoTDA mapa) {
        return mapa.PesoArista(this, b);
    }
}
