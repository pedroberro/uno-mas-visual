package unomas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación web. La lógica de negocio vive en el
 * paquete {@code unomas.model} y {@code unomas.controller}; la API REST en
 * {@code unomas.web}.
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
