import { useCallback, useEffect, useState } from 'react';
import { api, ErrorApi } from './api';
import { cargarSesion, guardarSesion } from './storage';
import { alternar, aplicarTema, cargarTema, guardarTema, type Tema } from './tema';
import type {
  AccionPartido,
  Catalogo,
  NuevoPartido,
  Partido,
  RegistroDatos,
  Usuario,
} from './types';
import { LoginPage } from './components/LoginPage';
import { Toasts, type Toast } from './components/Toasts';
import { Workspace } from './components/Workspace';

const DURACION_TOAST_MS = 3600;

export default function App() {
  const [tema, setTema] = useState<Tema>(() => cargarTema());
  const [catalogo, setCatalogo] = useState<Catalogo | null>(null);
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [partidos, setPartidos] = useState<Partido[]>([]);
  const [toasts, setToasts] = useState<Toast[]>([]);
  const [cargando, setCargando] = useState(true);
  const [errorInicial, setErrorInicial] = useState<string | null>(null);

  useEffect(() => {
    aplicarTema(tema);
    guardarTema(tema);
  }, [tema]);

  const alternarTema = useCallback(() => {
    setTema((actual) => alternar(actual));
  }, []);

  const notificar = useCallback((mensaje: string, tono: 'ok' | 'error' = 'ok') => {
    const id = crypto.randomUUID();
    setToasts((previos) => [...previos, { id, mensaje, tono }]);
    window.setTimeout(() => {
      setToasts((previos) => previos.filter((toast) => toast.id !== id));
    }, DURACION_TOAST_MS);
  }, []);

  const refrescarPartidos = useCallback(async (usuarioId: string) => {
    const lista = await api.partidos(usuarioId);
    setPartidos(lista);
    return lista;
  }, []);

  const refrescarUsuario = useCallback(async (usuarioId: string) => {
    const actualizado = await api.usuario(usuarioId);
    setUsuario(actualizado);
    return actualizado;
  }, []);

  useEffect(() => {
    let cancelado = false;

    const iniciar = async () => {
      try {
        const datos = await api.catalogo();
        if (cancelado) return;
        setCatalogo(datos);

        const sesion = cargarSesion();
        if (sesion) {
          try {
            const recuperado = await api.usuario(sesion);
            if (cancelado) return;
            setUsuario(recuperado);
            await refrescarPartidos(recuperado.id);
          } catch {
            guardarSesion(null);
          }
        }
      } catch (error) {
        if (!cancelado) {
          setErrorInicial(
            error instanceof ErrorApi ? error.message : 'No se pudo cargar la aplicación.',
          );
        }
      } finally {
        if (!cancelado) setCargando(false);
      }
    };

    void iniciar();
    return () => {
      cancelado = true;
    };
  }, [refrescarPartidos]);

  const ingresar = async (usuarioLogueado: Usuario) => {
    setUsuario(usuarioLogueado);
    guardarSesion(usuarioLogueado.id);
    await refrescarPartidos(usuarioLogueado.id);
    notificar(`¡Hola, ${usuarioLogueado.nombre}!`);
  };

  const registrarse = async (datos: RegistroDatos) => {
    const nuevo = await api.registrar(datos);
    await ingresar(nuevo);
    notificar('¡Cuenta creada! Ya podés crear y unirte a partidos.');
  };

  const salir = () => {
    setUsuario(null);
    setPartidos([]);
    guardarSesion(null);
  };

  const unirse = async (partido: Partido) => {
    if (!usuario) return;
    try {
      const resultado = await api.accion(partido.id, 'unirse', usuario.id);
      await refrescarPartidos(usuario.id);
      await refrescarUsuario(usuario.id);
      notificar(resultado.mensaje);
    } catch (error) {
      notificar(error instanceof Error ? error.message : 'No se pudo unir al partido.', 'error');
    }
  };

  const gestionarAccion = async (partido: Partido, accion: AccionPartido) => {
    if (!usuario || accion === 'unirse') return;
    try {
      const resultado = await api.accion(partido.id, accion, usuario.id);
      await refrescarPartidos(usuario.id);
      await refrescarUsuario(usuario.id);
      notificar(resultado.mensaje);
    } catch (error) {
      notificar(error instanceof Error ? error.message : 'No se pudo completar la acción.', 'error');
    }
  };

  const crearPartido = async (nuevo: NuevoPartido) => {
    if (!usuario) return false;
    try {
      await api.crearPartido(usuario.id, nuevo);
      await refrescarPartidos(usuario.id);
      await refrescarUsuario(usuario.id);
      notificar('Partido creado con éxito.');
      return true;
    } catch (error) {
      notificar(error instanceof Error ? error.message : 'No se pudo crear el partido.', 'error');
      return false;
    }
  };

  const comentar = async (partidoId: string, contenido: string) => {
    if (!usuario) return;
    try {
      await api.comentar(partidoId, usuario.id, contenido);
      await refrescarPartidos(usuario.id);
      notificar('Comentario publicado.');
    } catch (error) {
      notificar(error instanceof Error ? error.message : 'No se pudo publicar el comentario.', 'error');
    }
  };

  const reiniciarDemo = async () => {
    try {
      await api.reiniciarDemo();
      if (usuario) {
        await refrescarPartidos(usuario.id);
        await refrescarUsuario(usuario.id);
      }
      notificar('Datos de ejemplo restaurados.');
    } catch (error) {
      notificar(error instanceof Error ? error.message : 'No se pudo reiniciar la demo.', 'error');
    }
  };

  if (cargando) {
    return (
      <div className="pantalla-centrada">
        <div className="cargador" aria-label="Cargando" />
        <p>Conectando con el servidor…</p>
      </div>
    );
  }

  if (errorInicial || !catalogo) {
    return (
      <div className="pantalla-centrada">
        <h1>No pudimos iniciar la aplicación</h1>
        <p className="texto-suave">{errorInicial ?? 'No se recibió el catálogo.'}</p>
        <p className="texto-suave">
          Verificá que el backend esté corriendo con <code>mvn spring-boot:run</code>.
        </p>
        <button type="button" className="btn btn--primario" onClick={() => window.location.reload()}>
          Reintentar
        </button>
      </div>
    );
  }

  return (
    <>
      {usuario ? (
        <Workspace
          usuario={usuario}
          catalogo={catalogo}
          partidos={partidos}
          tema={tema}
          onAlternarTema={alternarTema}
          onSalir={salir}
          onUnirse={unirse}
          onCrear={crearPartido}
          onAccion={gestionarAccion}
          onComentar={comentar}
          onReiniciar={reiniciarDemo}
        />
      ) : (
        <LoginPage
          catalogo={catalogo}
          tema={tema}
          onAlternarTema={alternarTema}
          onIngresar={ingresar}
          onRegistrarse={registrarse}
        />
      )}
      <Toasts toasts={toasts} />
    </>
  );
}
