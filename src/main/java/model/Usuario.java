package model;

import java.util.*;
import lombok.Data;
import lombok.Getter;
import view.PartidoView;
import view.UsuarioView;
import controller.PartidoController;

@Data
public class Usuario implements IParticipante, IObserver {

    @Getter
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasenia;
    private String deporteFav;
    private NivelDeJuego nivelDeJuego;
    private Barrio zona;

    private List<Partido> historial;
    private List<IObservable> observables;

    private Scanner scanner = new Scanner(System.in);

    public Usuario(String nombre, String apellido, String correo, String contrasenia, String deporteFav,
            NivelDeJuego nivelDeJuego, Barrio zona) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.deporteFav = deporteFav;
        this.nivelDeJuego = nivelDeJuego;
        this.zona = zona;
        this.observables = new ArrayList<>();
        this.historial = new ArrayList<>();
    }

    public void crearPartido() {
        Deporte deporteSeleccionado = null;
        while (deporteSeleccionado == null) {
            System.out.println("Selecciona (con el respectivo NUMERO) de qué deporte quieres crear un partido:");
            System.out.println("1. Fútbol");
            System.out.println("2. Basquet");
            System.out.println("3. Tenis");
            System.out.println("4. Voley");
            System.out.println("5. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine();
            System.out.println("");


            switch (opcion) {
                case 1:
                    deporteSeleccionado = new DFutbol();
                    break;
                case 2:
                    deporteSeleccionado = new DBasquet();
                    break;
                case 3:
                    deporteSeleccionado = new DTenis();
                    break;
                case 4:
                    deporteSeleccionado = new DVoley();
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    System.out.println("");
                    return;
                default:
                    System.out.println("Opción inválida, intente nuevamente.");
            }
        }

        // Ingreso de duración
        int duracion = -1;
        while (duracion <= 0) {
            System.out.println("Ingrese la duración del encuentro en minutos:");
            try {
                duracion = Integer.parseInt(scanner.nextLine());
                System.out.println("");
                if (duracion <= 0)
                    System.out.println("La duración debe ser mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }

        // Ingreso de fecha
        Date fecha = null;
        while (fecha == null) {
            System.out.println("Ingrese la fecha (formato yyyy-mm-dd):");
            String fechaStr = scanner.nextLine();
            System.out.println("");
            try {
                fecha = java.sql.Date.valueOf(fechaStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Formato de fecha inválido. Intente nuevamente.");
            }
        }

        // Ingreso de horario
        String horario = "";
        while (horario.isBlank()) {
            System.out.println("Ingrese el horario (ej: 19:00):");
            horario = scanner.nextLine();
            System.out.println("");
            if (horario.isBlank())
                System.out.println("El horario no puede estar vacío.");
        }

        // Selección de zona
        List<Barrio> barrios = controller.BarrioController.getBarriosDisponibles();
        int zonaIndex = -1;
        while (zonaIndex < 1 || zonaIndex > barrios.size()) {
            System.out.println("Seleccione la zona ingresando el número correspondiente:");
            for (int i = 0; i < barrios.size(); i++) {
                Barrio b = barrios.get(i);
                System.out.println((i + 1) + ". " + b.getBarrio() + " (" + b.getCiudad() + ")");
            }
            try {
                zonaIndex = Integer.parseInt(scanner.nextLine());
                System.out.println("");
                if (zonaIndex < 1 || zonaIndex > barrios.size())
                    System.out.println("Zona inválida. Intente nuevamente.");
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
        Barrio zonaSeleccionada = barrios.get(zonaIndex - 1);

        // Elegir estrategia de emparejamiento
        int opcionEstrategia = -1;
        while (opcionEstrategia < 1 || opcionEstrategia > 3) {
            System.out.println("Seleccione la estrategia de emparejamiento con el respectivo numero:");
            System.out.println("1. Por cercanía");
            System.out.println("2. Por nivel de habilidad");
            System.out.println("3. Por historial de partidos");
            try {
                opcionEstrategia = Integer.parseInt(scanner.nextLine());
                System.out.println("");
                if (opcionEstrategia < 1 || opcionEstrategia > 3) {
                    System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }

        PartidoController partidoController = PartidoController.getInstance();
        Partido nuevoPartido = partidoController.crearPartido(deporteSeleccionado, duracion, zonaSeleccionada, fecha,
                horario, this);

        // Asignar la estrategia elegida
        switch (opcionEstrategia) {
            case 1:
                nuevoPartido.setMetodoDeEmparejamiento(
                        new EmparejamientoPorCercania(nuevoPartido, controller.BarrioController.getMapaBarrios()));
                break;
            case 2:
                // Mostrar niveles disponibles
                System.out.println("Selecciona el nivel requerido para este partido con el respectivo numero:");
                NivelDeJuego[] niveles = NivelDeJuego.values();
                for (int i = 0; i < niveles.length; i++) {
                    System.out.println((i + 1) + ". " + niveles[i]);
                }
                int nivelElegido = scanner.nextInt();
                scanner.nextLine();
                System.out.println("");
                if (nivelElegido < 1 || nivelElegido > niveles.length) {
                    System.out.println("Nivel inválido. Se usará INTERMEDIO por defecto.");
                    nivelElegido = 2; // INTERMEDIO
                }
                NivelDeJuego nivelRequerido = niveles[nivelElegido - 1];
                nuevoPartido.setNivelRequerido(nivelRequerido);
                nuevoPartido.setMetodoDeEmparejamiento(
                        new EmparejamientoPorNivel(nuevoPartido, nivelRequerido));
                break;
            case 3:
                System.out.println("¿Cuántos partidos previos debe tener el usuario para ser invitado?");
                int cantidad = scanner.nextInt();
                scanner.nextLine();
                System.out.println("");
                nuevoPartido.setMetodoDeEmparejamiento(new EmparejamientoPorHistorial(nuevoPartido, cantidad));
                break;
            default:
                System.out.println("Opción inválida. Se usará emparejamiento por cercanía.");
                System.out.println("");
                nuevoPartido.setMetodoDeEmparejamiento(
                        new EmparejamientoPorCercania(nuevoPartido, controller.BarrioController.getMapaBarrios()));
        }

        System.out.println("¡Partido creado exitosamente!");
        System.out.println("");
        System.out.println("Estado actual del partido: " + nuevoPartido.estadoActual());
        System.out.println("");
    }

    public void buscarPartido(Barrio ubicacion) {
        PartidoController partidoController = PartidoController.getInstance();

        List<Partido> partidosDisponibles = partidoController.buscarPartidosDisponiblesPorZona(ubicacion);

        if (partidosDisponibles.isEmpty()) {
            System.out.println("No hay partidos disponibles en esa zona que necesiten jugadores.");
            System.out.println("");
            return;
        }

        System.out.println("Partidos disponibles en " + ubicacion.getBarrio() + ":");
        PartidoView partidoView = new PartidoView();
        partidoView.mostrarPartidosDetallados(partidosDisponibles);
        System.out.println("");

        System.out.println("¿Quieres unirte a algún partido? Ingresá el número del partido (0 para cancelar):");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        System.out.println("");

        if (opcion > 0 && opcion <= partidosDisponibles.size()) {
            Partido partidoSeleccionado = partidosDisponibles.get(opcion - 1);
            boolean exito = partidoController.unirseAPartido(partidoSeleccionado, this);
            if (exito) {
                System.out.println("¡Te uniste al partido exitosamente!");
                System.out.println("");
            } else {
                System.out.println("No fue posible unirte al partido.");
                System.out.println("");
            }
        } else {
            System.out.println("No te uniste a ningún partido.");
            System.out.println("");
        }
    }

    public void finalizarPartidoConComentario() {
        List<Partido> enJuego = new ArrayList<>();
        for (Partido p : historial) {
            if (p.estadoActual().equalsIgnoreCase("En juego") && p.getCreador().equals(this)) {
                enJuego.add(p);
            }
        }

        if (enJuego.isEmpty()) {
            System.out.println("No tenés partidos en estado 'En juego'.");
            System.out.println("");
            return;
        }

        System.out.println("Partidos en juego:");
        PartidoView partidoView = new PartidoView();
        partidoView.mostrarPartidosDetallados(enJuego);

        System.out.println("Ingrese el número del partido a finalizar (0 para cancelar):");
        int opcion = Integer.parseInt(scanner.nextLine());
        System.out.println("");

        if (opcion < 1 || opcion > enJuego.size()) {
            System.out.println("Operación cancelada.");
            System.out.println("");
            return;
        }

        Partido partido = enJuego.get(opcion - 1);
        partido.finalizar();

        System.out.println("¿Querés dejar un comentario sobre este partido? (s/n)");
        String respuesta = scanner.nextLine();
        System.out.println("");

        if (respuesta.equalsIgnoreCase("s")) {
            System.out.println("Escribí tu comentario:");
            String texto = scanner.nextLine();
            Comentario comentario = new Comentario(this.nombre, texto, new Date());
            partido.agregarComentario(comentario);
            System.out.println("Comentario guardado.");
        } else {
            System.out.println("No se agregó ningún comentario.");
        }
    }

    @Override
    public void serNotificadoPor(IObservable o) {
        if (!observables.contains(o)) {
            observables.add(o);
        }
        if (o instanceof Partido partido) {
            System.out.println("Notificación: El partido de " + partido.getDeporte().getNombre() +
                    " en " + partido.getZona().getBarrio() + " cambió de estado a: " + partido.estadoActual());
        } else {
            System.out.println("Has recibido una nueva notificación.");
        }
    }

    @Override
    public void agregarHistorial(Partido p) {
        this.historial.add(p);
    }

    public void verPartidosInvitados() {
        List<Partido> todos = controller.PartidoController.getInstance().getTodosLosPartidos();
        List<Partido> invitados = new ArrayList<>();
        for (Partido p : todos) {
            if (p.getMetodoDeEmparejamiento() != null) {
                List<Usuario> candidatos = p.getMetodoDeEmparejamiento().emparejar(List.of(this));
                if (candidatos.contains(this) && !p.tieneJugador(this)) {
                    invitados.add(p);
                }
            }
        }
        if (invitados.isEmpty()) {
            System.out.println("No tienes invitaciones pendientes.");
            System.out.println("");
            return;
        }

        System.out.println("=== Partidos a los que has sido invitado ===");
        PartidoView partidoView = new PartidoView();
        partidoView.mostrarPartidosDetallados(invitados);

        System.out.println("¿Quieres aceptar alguna invitación? Ingresa el número del partido (0 para cancelar):");
        int opcion = this.scanner.nextInt();
        this.scanner.nextLine();
        System.out.println("");
        if (opcion > 0 && opcion <= invitados.size()) {
            Partido seleccionado = invitados.get(opcion - 1);
            boolean exito = controller.PartidoController.getInstance().unirseAPartido(seleccionado, this);
            if (exito) {
                System.out.println("¡Te uniste al partido exitosamente!");
                System.out.println("");
            } else {
                System.out.println("No fue posible unirte al partido.");
                System.out.println("");
            }
        } else {
            System.out.println("No aceptaste ninguna invitación.");
            System.out.println("");
        }
    }

    public void comenzarPartidoCreado() {
        List<Partido> creados = new ArrayList<>();

        for (Partido p : historial) {
            if (p.getCreador().equals(this) && p.estadoActual().equalsIgnoreCase("Confirmado")) {
                creados.add(p);
            }
        }

        if (creados.isEmpty()) {
            System.out.println("No tenés partidos en estado 'Confirmado' para comenzar.");
            System.out.println("");
            return;
        }

        System.out.println("Partidos confirmados que podés comenzar:");
        PartidoView partidoView = new PartidoView();
        partidoView.mostrarPartidosDetallados(creados);

        System.out.println("Ingrese el número del partido a comenzar (0 para cancelar):");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        System.out.println("");

        if (opcion < 1 || opcion > creados.size()) {
            System.out.println("Operación cancelada.");
            System.out.println("");
            return;
        }

        Partido partido = creados.get(opcion - 1);
        partido.comenzar();
        System.out.println("¡Partido iniciado!");
        System.out.println("");
    }

    public void confirmarJugadoresDePartido() {
        List<Partido> armados = new ArrayList<>();

        for (Partido p : historial) {
            if (p.getCreador().equals(this) && p.estadoActual().equalsIgnoreCase("Armado")) {
                armados.add(p);
            }
        }

        if (armados.isEmpty()) {
            System.out.println("No tenés partidos en estado 'Armado' para confirmar.");
            System.out.println("");
            return;
        }

        System.out.println("Partidos armados que podés confirmar:");
        PartidoView partidoView = new PartidoView();
        partidoView.mostrarPartidosDetallados(armados);

        System.out.println("Ingrese el número del partido a confirmar (0 para cancelar):");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        System.out.println("");

        if (opcion < 1 || opcion > armados.size()) {
            System.out.println("Operación cancelada.");
            System.out.println("");
            return;
        }

        Partido partido = armados.get(opcion - 1);
        partido.confirmarJugadores();
        System.out.println("¡Partido confirmado! Ahora podés comenzarlo.");
        System.out.println("");
    }

    public void gestionarInvitaciones(PartidoView partidoView, UsuarioView usuarioView) {
        boolean volverAlMenu = false;
        do {
            List<Partido> todos = controller.PartidoController.getInstance().getTodosLosPartidos();
            List<Partido> invitados = new ArrayList<>();
            for (Partido p : todos) {
                if (p.getMetodoDeEmparejamiento() != null) {
                    List<Usuario> candidatos = p.getMetodoDeEmparejamiento().emparejar(List.of(this));
                    if (candidatos.contains(this) && !p.tieneJugador(this)) {
                        invitados.add(p);
                    }
                }
            }
            if (invitados.isEmpty()) {
                usuarioView.mostrarMensaje("No tienes invitaciones pendientes.");
                System.out.println("");
                volverAlMenu = true;
            } else {
                usuarioView.mostrarMensaje("=== Partidos a los que has sido invitado ===");
                partidoView.mostrarPartidosDetallados(invitados);
                String input = usuarioView
                        .solicitarInput("¿Quieres aceptar alguna invitación? Ingresa el número del partido (0 para cancelar):");
                System.out.println("");
                try {
                    int opcionInv = Integer.parseInt(input);
                    if (opcionInv > 0 && opcionInv <= invitados.size()) {
                        Partido seleccionado = invitados.get(opcionInv - 1);
                        boolean exito = controller.PartidoController.getInstance().unirseAPartido(seleccionado, this);
                        if (exito) {
                            usuarioView.mostrarMensaje("¡Te uniste al partido exitosamente!");
                            System.out.println("");
                        } else {
                            usuarioView.mostrarMensaje("No fue posible unirte al partido.");
                            System.out.println("");
                        }
                        volverAlMenu = true;
                    } else if (opcionInv == 0) {
                        volverAlMenu = true;
                    } else {
                        usuarioView.mostrarMensaje("No aceptaste ninguna invitación.");
                        System.out.println("");
                    }
                } catch (NumberFormatException e) {
                    usuarioView.mostrarMensaje("Por favor, ingrese un número válido.");
                    System.out.println("");
                }
            }
        } while (!volverAlMenu);
    }

}
