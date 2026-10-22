package unomas.config;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import unomas.controller.BarrioController;
import unomas.controller.PartidoController;
import unomas.controller.UsuarioController;
import unomas.model.Barrio;
import unomas.model.DeporteFactory;
import unomas.model.EmparejamientoPorCercania;
import unomas.model.EmparejamientoPorNivel;
import unomas.model.IMGrafoTDA;
import unomas.model.MGrafo;
import unomas.model.NivelDeJuego;
import unomas.model.Partido;
import unomas.model.Usuario;

/**
 * Carga los barrios y los partidos de ejemplo con los que arranca la
 * aplicación web.
 */
@Component
public class DatosIniciales {

    private static final String CIUDAD = "caba";

    @PostConstruct
    public void inicializar() {
        inicializarBarrios();
        UsuarioController.getInstance();
        registrarUsuariosExtra();
        reiniciarPartidos();
    }

    /**
     * Suma jugadores de ejemplo para que los partidos tengan equipos con
     * varios integrantes sin tener que crearlos a mano.
     */
    private void registrarUsuariosExtra() {
        String[][] extras = {
                { "Sofía", "Ramírez", "palermo", "futbol", "INTERMEDIO" },
                { "Tomás", "Duarte", "belgrano", "basquet", "PRINCIPIANTE" },
                { "Valentina", "Cáceres", "recoleta", "voley", "AVANZADO" },
                { "Bruno", "Silva", "san telmo", "futbol", "PRINCIPIANTE" },
                { "Camila", "Ortiz", "caballito", "tenis", "INTERMEDIO" },
                { "Nicolás", "Ferreyra", "palermo", "basquet", "INTERMEDIO" },
                { "Lucía", "Benítez", "belgrano", "voley", "PRINCIPIANTE" },
                { "Martín", "Aguirre", "recoleta", "futbol", "AVANZADO" },
                { "Julieta", "Sosa", "san telmo", "tenis", "AVANZADO" },
                { "Facundo", "Ríos", "caballito", "basquet", "AVANZADO" },
                { "Rocío", "Paredes", "palermo", "voley", "INTERMEDIO" },
                { "Ignacio", "Molina", "san telmo", "basquet", "PRINCIPIANTE" },
                { "Agustina", "Vega", "belgrano", "futbol", "INTERMEDIO" },
                { "Damián", "Quiroga", "recoleta", "tenis", "AVANZADO" },
                { "Paula", "Medina", "caballito", "voley", "AVANZADO" },
                { "Emiliano", "Castro", "palermo", "futbol", "AVANZADO" },
        };

        for (String[] datos : extras) {
            String correo = (datos[0] + datos[1]).toLowerCase()
                    .replace("á", "a").replace("é", "e").replace("í", "i")
                    .replace("ó", "o").replace("ú", "u") + "@gmail.com";

            if (UsuarioController.getInstance().buscarPorCorreo(correo).isPresent()) {
                continue;
            }

            UsuarioController.getInstance().registrar(
                    datos[0],
                    datos[1],
                    correo,
                    "demo1234",
                    datos[3],
                    NivelDeJuego.valueOf(datos[4]),
                    BarrioController.obtenerBarrio(datos[2]));
        }
    }

    private void inicializarBarrios() {
        IMGrafoTDA mapa = new MGrafo();
        mapa.InicializarGrafo();

        Barrio palermo = Barrio.builder().ciudad(CIUDAD).barrio("palermo").build();
        Barrio recoleta = Barrio.builder().ciudad(CIUDAD).barrio("recoleta").build();
        Barrio belgrano = Barrio.builder().ciudad(CIUDAD).barrio("belgrano").build();
        Barrio sanTelmo = Barrio.builder().ciudad(CIUDAD).barrio("san telmo").build();
        Barrio caballito = Barrio.builder().ciudad(CIUDAD).barrio("caballito").build();

        mapa.AgregarVertice(palermo);
        mapa.AgregarVertice(recoleta);
        mapa.AgregarVertice(belgrano);
        mapa.AgregarVertice(sanTelmo);
        mapa.AgregarVertice(caballito);

        mapa.AgregarArista(palermo, recoleta, 3);
        mapa.AgregarArista(palermo, belgrano, 4);
        mapa.AgregarArista(palermo, sanTelmo, 8);
        mapa.AgregarArista(palermo, caballito, 5);
        mapa.AgregarArista(recoleta, belgrano, 5);
        mapa.AgregarArista(recoleta, sanTelmo, 6);
        mapa.AgregarArista(recoleta, caballito, 4);
        mapa.AgregarArista(belgrano, sanTelmo, 10);
        mapa.AgregarArista(belgrano, caballito, 6);
        mapa.AgregarArista(sanTelmo, caballito, 7);

        BarrioController.inicializarBarrios(List.of(palermo, recoleta, belgrano, sanTelmo, caballito), mapa);
    }

    /**
     * Deja los partidos en el estado inicial de la demo. Se usa al arrancar y
     * desde el botón "Reiniciar demo" de la interfaz.
     */
    public synchronized void reiniciarPartidos() {
        PartidoController partidos = PartidoController.getInstance();
        UsuarioController usuarios = UsuarioController.getInstance();

        partidos.vaciar();
        usuarios.limpiarHistoriales();

        Usuario juan = usuarios.buscarPorCorreo("juanperez@gmail.com").orElseThrow();
        Usuario ana = usuarios.buscarPorCorreo("analopez@gmail.com").orElseThrow();
        Usuario luis = usuarios.buscarPorCorreo("luisgomez@gmail.com").orElseThrow();
        Usuario marta = usuarios.buscarPorCorreo("martafernandez@gmail.com").orElseThrow();
        Usuario carlos = usuarios.buscarPorCorreo("carlossanchez@gmail.com").orElseThrow();
        Usuario matias = usuarios.buscarPorCorreo("matirodriguez@gmail.com").orElseThrow();

        crear("tenis", "belgrano", 2, "09:00", 60, NivelDeJuego.PRINCIPIANTE, luis, "cercania");

        Partido futbolPalermo = crear("futbol", "palermo", 2, "19:00", 90,
                NivelDeJuego.INTERMEDIO, juan, "nivel");
        unir(futbolPalermo, ana);
        unir(futbolPalermo, marta);
        unir(futbolPalermo, carlos);
        unir(futbolPalermo, matias);
        unirPorCorreo(futbolPalermo, "sofaramirez@gmail.com", "tomasduarte@gmail.com",
                "valentinacaceres@gmail.com", "brunosilva@gmail.com", "camilaortiz@gmail.com",
                "nicolasferreyra@gmail.com", "luciabenitez@gmail.com", "martinaguirre@gmail.com",
                "julietasosa@gmail.com", "facundorios@gmail.com", "rocioparedes@gmail.com",
                "ignaciomolina@gmail.com", "agustinavega@gmail.com");

        Partido basquetRecoleta = crear("basquet", "recoleta", 1, "20:30", 90,
                NivelDeJuego.AVANZADO, ana, "cercania");
        unir(basquetRecoleta, carlos);
        unir(basquetRecoleta, juan);
        unir(basquetRecoleta, marta);
        unirPorCorreo(basquetRecoleta, "sofaramirez@gmail.com", "tomasduarte@gmail.com",
                "valentinacaceres@gmail.com", "brunosilva@gmail.com", "camilaortiz@gmail.com",
                "nicolasferreyra@gmail.com");

        Partido voleySanTelmo = crear("voley", "san telmo", 0, "20:00", 90,
                NivelDeJuego.INTERMEDIO, marta, "nivel");
        unir(voleySanTelmo, matias);
        unir(voleySanTelmo, luis);
        unirPorCorreo(voleySanTelmo, "sofaramirez@gmail.com", "tomasduarte@gmail.com",
                "valentinacaceres@gmail.com", "brunosilva@gmail.com", "camilaortiz@gmail.com",
                "nicolasferreyra@gmail.com", "luciabenitez@gmail.com", "martinaguirre@gmail.com",
                "julietasosa@gmail.com", "facundorios@gmail.com");

        Partido tenisPalermo = crear("tenis", "palermo", 3, "17:00", 60,
                NivelDeJuego.PRINCIPIANTE, matias, "cercania");
        unir(tenisPalermo, luis);

        Partido tenisCaballito = crear("tenis", "caballito", 1, "18:00", 60,
                NivelDeJuego.AVANZADO, carlos, "cercania");
        unir(tenisCaballito, ana);
        PartidoController.getInstance().confirmarJugadores(tenisCaballito, carlos);

        Partido tenisSanTelmo = crear("tenis", "san telmo", -1, "19:00", 60,
                NivelDeJuego.INTERMEDIO, marta, "cercania");
        unir(tenisSanTelmo, juan);
        PartidoController.getInstance().confirmarJugadores(tenisSanTelmo, marta);
        PartidoController.getInstance().comenzar(tenisSanTelmo, marta);
        PartidoController.getInstance().finalizar(tenisSanTelmo, marta);
        PartidoController.getInstance().agregarComentario(tenisSanTelmo, marta,
                "¡Buen partido! La próxima jugamos la revancha.");

        Partido basquetPalermo = crear("basquet", "palermo", 5, "19:00", 90,
                NivelDeJuego.INTERMEDIO, ana, "nivel");
        unir(basquetPalermo, juan);
        PartidoController.getInstance().cancelar(basquetPalermo, ana);
    }

    private Partido crear(String deporte, String barrio, int dias, String horario, int duracion,
            NivelDeJuego nivel, Usuario creador, String estrategia) {

        Partido partido = PartidoController.getInstance().crearPartido(
                DeporteFactory.crear(deporte),
                duracion,
                BarrioController.obtenerBarrio(barrio),
                fechaEnDias(dias),
                horario,
                creador);

        partido.setNivelRequerido(nivel);
        partido.setMetodoDeEmparejamiento("nivel".equals(estrategia)
                ? new EmparejamientoPorNivel(partido, nivel)
                : new EmparejamientoPorCercania(partido, BarrioController.getMapaBarrios()));

        return partido;
    }

    private void unir(Partido partido, Usuario usuario) {
        PartidoController.getInstance().unirseAPartido(partido, usuario);
    }

    private void unirPorCorreo(Partido partido, String... correos) {
        for (String correo : correos) {
            UsuarioController.getInstance().buscarPorCorreo(correo).ifPresent(usuario -> unir(partido, usuario));
        }
    }

    private Date fechaEnDias(int dias) {
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, dias);
        calendario.set(Calendar.HOUR_OF_DAY, 12);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);
        return calendario.getTime();
    }
}
