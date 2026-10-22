package unomas.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unomas.controller.CredencialesInvalidasException;
import unomas.controller.UsuarioController;
import unomas.dto.LoginRequest;
import unomas.dto.UsuarioDTO;
import unomas.model.Usuario;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public UsuarioDTO login(@RequestBody LoginRequest request) {
        Usuario usuario = UsuarioController.getInstance()
                .autenticar(request.correo(), request.contrasenia())
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos."));
        return DtoMapper.usuario(usuario);
    }
}
