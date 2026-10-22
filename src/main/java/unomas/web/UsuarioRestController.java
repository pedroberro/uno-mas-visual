package unomas.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import unomas.controller.UsuarioController;
import unomas.dto.NotificacionDTO;
import unomas.dto.RegistroRequest;
import unomas.dto.UsuarioDTO;
import unomas.model.DeporteFactory;
import unomas.model.Usuario;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO registrar(@RequestBody RegistroRequest request) {
        String deporte = request.deporteFav() == null || request.deporteFav().isBlank()
                ? "futbol"
                : request.deporteFav();
        DeporteFactory.crear(deporte);

        Usuario usuario = UsuarioController.getInstance().registrar(
                request.nombre(),
                request.apellido(),
                request.correo(),
                request.contrasenia(),
                deporte,
                Parametros.nivel(request.nivel()),
                Parametros.barrio(request.barrio()));

        return DtoMapper.usuario(usuario);
    }

    @GetMapping("/{id}")
    public UsuarioDTO obtener(@PathVariable String id) {
        return DtoMapper.usuario(Parametros.usuario(id));
    }

    @GetMapping("/{id}/notificaciones")
    public List<NotificacionDTO> notificaciones(@PathVariable String id) {
        return Parametros.usuario(id).getNotificaciones().stream()
                .map(DtoMapper::notificacion)
                .toList();
    }
}
