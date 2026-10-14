package view;

import java.util.*;
import controller.*;
import model.*;

public class InterfazGrafica {

    private static InterfazGrafica instancia;

    private UsuarioView usuarioView = new UsuarioView();
    private PartidoView partidoView = new PartidoView();
    private BarrioView barrioView = new BarrioView();
    private UsuarioController usuarioController = new UsuarioController();

    // Singleton
    private InterfazGrafica() {
        PartidoController.getInstance().setPartidoView(this.partidoView);
    }

    // Singleton
    public static InterfazGrafica getInstancia() {
        if (instancia == null) {
            instancia = new InterfazGrafica();
        }
        return instancia;
    }

    public void mensajeInicial() {
        PartidoController.getInstance().actualizarEstadosPorFecha();
        int opcion = -1;
        usuarioView.mostrarMensaje("\nBienvenido a la aplicación de deportes!");
        while (opcion != 0) {
            usuarioView.mostrarMensaje("¿Qué desea hacer?");
            usuarioView.mostrarMensaje("1. Iniciar sesión");
            usuarioView.mostrarMensaje("2. Crear usuario");
            usuarioView.mostrarMensaje("0. Salir");
            usuarioView.mostrarMensaje(""); 

            try {
                opcion = Integer.parseInt(usuarioView.solicitarInput("Seleccione una opción:"));
                usuarioView.mostrarMensaje("");
                
                switch (opcion) {
                    case 1:
                        Usuario usuario = usuarioController.iniciarSesion();
                        if (usuario != null) {
                            mostrarMenuUsuario(usuario);
                        }
                        break;
                    case 2:
                        usuarioController.registrarUsuario();
                        break;
                    case 0:
                        usuarioView.mostrarMensaje("Gracias por usar la aplicación. ¡Hasta luego!");
                        return;
                    default:
                        usuarioView.mostrarMensaje("Opción no válida. Intente de nuevo.");
                        usuarioView.mostrarMensaje("");
                }
            } catch (NumberFormatException e) {
                usuarioView.mostrarMensaje("Entrada inválida. Por favor ingrese un número.");
                usuarioView.mostrarMensaje("");
            }
        }
    }

    public void mostrarMenuUsuario(Usuario usuario) {
        int opcion = -1;
        PartidoController partidoController = PartidoController.getInstance();
        PartidoController.getInstance().actualizarEstadosPorFecha();

        usuarioView.mostrarMensaje("\nBienvenido " + usuario.getNombre() + "!");
        while (opcion != 9) {
            usuarioView.mostrarMensaje("¿Qué desea hacer?");
            usuarioView.mostrarMensaje("1. Crear partido");
            usuarioView.mostrarMensaje("2. Buscar partido");
            usuarioView.mostrarMensaje("3. Ver partidos que creaste");
            usuarioView.mostrarMensaje("4. Confirmar jugadores para partido");
            usuarioView.mostrarMensaje("5. Comenzar partido creado");
            usuarioView.mostrarMensaje("6. Ver partidos a los que te uniste");
            usuarioView.mostrarMensaje("7. Ver partidos a los que fuiste invitado");
            usuarioView.mostrarMensaje("8. Finalizar partido y dejar comentario");
            usuarioView.mostrarMensaje("9. Salir");
            usuarioView.mostrarMensaje(""); 

            try {
                opcion = Integer.parseInt(usuarioView.solicitarInput("Seleccione una opción:"));
                usuarioView.mostrarMensaje("");
                switch (opcion) {
                    case 1:
                        usuario.crearPartido();
                        break;
                    case 2:
                        usuario.buscarPartido(usuario.getZona());
                        break;
                    case 3:
                        partidoController.verPartidosCreados(usuario);
                        break;
                    case 4:
                        usuario.confirmarJugadoresDePartido();
                        break;
                    case 5:
                        usuario.comenzarPartidoCreado();
                        break;
                    case 6:
                        partidoController.verPartidosUnidos(usuario);
                        break;
                    case 7:
                        usuario.gestionarInvitaciones(partidoView, usuarioView);
                        break;
                    case 8:
                        usuario.finalizarPartidoConComentario();
                        break;
                    case 9:
                        usuarioView.mostrarMensaje("Saliendo...");
                        usuarioView.mostrarMensaje("");
                        return;
                    default:
                        usuarioView.mostrarMensaje("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                usuarioView.mostrarMensaje("Entrada inválida. Por favor ingrese un número.");
            }
        }
    }

}
