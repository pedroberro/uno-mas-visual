package unomas.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import unomas.controller.CredencialesInvalidasException;
import unomas.controller.NoEncontradoException;
import unomas.controller.PermisoDenegadoException;
import unomas.controller.ReglaNegocioException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorApi> reglaDeNegocio(ReglaNegocioException excepcion) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorApi(excepcion.getMessage()));
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorApi> credencialesInvalidas(CredencialesInvalidasException excepcion) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorApi(excepcion.getMessage()));
    }

    @ExceptionHandler(PermisoDenegadoException.class)
    public ResponseEntity<ErrorApi> permisoDenegado(PermisoDenegadoException excepcion) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorApi(excepcion.getMessage()));
    }

    @ExceptionHandler(NoEncontradoException.class)
    public ResponseEntity<ErrorApi> noEncontrado(NoEncontradoException excepcion) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorApi(excepcion.getMessage()));
    }
}
