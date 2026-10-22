package unomas.web;

import java.util.Date;

import unomas.controller.BarrioController;
import unomas.controller.ReglaNegocioException;
import unomas.controller.UsuarioController;
import unomas.model.Barrio;
import unomas.model.NivelDeJuego;
import unomas.model.Usuario;

/**
 * Conversión y validación de los parámetros que llegan por la API.
 */
public final class Parametros {

    private Parametros() {
    }

    public static Usuario usuario(String id) {
        if (id == null || id.isBlank()) {
            throw new ReglaNegocioException("Falta indicar el usuario.");
        }
        return UsuarioController.getInstance().obtenerPorId(id);
    }

    public static Barrio barrio(String nombre) {
        Barrio barrio = BarrioController.obtenerBarrio(nombre);
        if (barrio == null) {
            throw new ReglaNegocioException("El barrio indicado no existe.");
        }
        return barrio;
    }

    public static NivelDeJuego nivel(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new ReglaNegocioException("Falta indicar el nivel de juego.");
        }
        try {
            return NivelDeJuego.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("Nivel de juego inválido: " + valor + ".");
        }
    }

    public static Date fecha(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new ReglaNegocioException("Falta la fecha del partido.");
        }
        try {
            return java.sql.Date.valueOf(valor.trim());
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("Fecha inválida. Usá el formato yyyy-MM-dd.");
        }
    }
}
