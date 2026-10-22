package unomas.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unomas.config.DatosIniciales;
import unomas.dto.MensajeDTO;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final DatosIniciales datosIniciales;

    public DemoController(DatosIniciales datosIniciales) {
        this.datosIniciales = datosIniciales;
    }

    @PostMapping("/reiniciar")
    public MensajeDTO reiniciar() {
        datosIniciales.reiniciarPartidos();
        return new MensajeDTO("Datos de ejemplo restaurados.");
    }
}
