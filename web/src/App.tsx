import { useCallback, useEffect, useState } from 'react';
import { DEPORTES, PARTIDOS_INICIALES, USUARIOS, capitalizar, nombreCompleto } from './data';
import { cargarPartidos, cargarSesion, guardarPartidos, guardarSesion } from './storage';
import type { AccionPartido, Comentario, Estado, NuevoPartido, Partido, Usuario } from './types';
import { LoginPage } from './components/LoginPage';
import { Toasts, type Toast } from './components/Toasts';
import { Workspace } from './components/Workspace';

const DURACION_TOAST_MS = 3400;

export default function App() {
  const [usuarioId, setUsuarioId] = useState<string | null>(() => cargarSesion());
  const [partidos, setPartidos] = useState<Partido[]>(() => cargarPartidos() ?? PARTIDOS_INICIALES);
  const [toasts, setToasts] = useState<Toast[]>([]);

  const usuario = usuarioId
    ? USUARIOS.find((candidato) => candidato.id === usuarioId) ?? null
    : null;

  useEffect(() => {
    guardarPartidos(partidos);
  }, [partidos]);

  useEffect(() => {
    guardarSesion(usuarioId);
  }, [usuarioId]);

  const notificar = useCallback((mensaje: string, tono: 'ok' | 'error' = 'ok') => {
    const id = crypto.randomUUID();
    setToasts((previos) => [...previos, { id, mensaje, tono }]);
    window.setTimeout(() => {
      setToasts((previos) => previos.filter((toast) => toast.id !== id));
    }, DURACION_TOAST_MS);
  }, []);

  const cambiarEstado = (partidoId: string, estado: Estado) => {
    setPartidos((previos) =>
      previos.map((partido) => (partido.id === partidoId ? { ...partido, estado } : partido)),
    );
  };

  const ingresar = (nuevoUsuario: Usuario) => {
    setUsuarioId(nuevoUsuario.id);
    notificar(`¡Hola, ${nuevoUsuario.nombre}!`);
  };

  const salir = () => {
    setUsuarioId(null);
  };

  const unirse = (partido: Partido) => {
    if (!usuario) return;

    const deporte = DEPORTES[partido.deporte];

    if (partido.jugadores.includes(usuario.id)) {
      notificar('Ya estás anotado en este partido.', 'error');
      return;
    }
    if (partido.estado !== 'Necesitamos jugadores' || partido.jugadores.length >= deporte.jugadores) {
      notificar('Este partido ya no acepta más jugadores.', 'error');
      return;
    }

    const seCompleta = partido.jugadores.length + 1 >= deporte.jugadores;

    setPartidos((previos) =>
      previos.map((candidato) => {
        if (candidato.id !== partido.id) return candidato;
        const jugadores = [...candidato.jugadores, usuario.id];
        return {
          ...candidato,
          jugadores,
          estado: jugadores.length >= DEPORTES[candidato.deporte].jugadores ? 'Armado' : candidato.estado,
        };
      }),
    );

    notificar(
      seCompleta
        ? '¡Te uniste! El partido quedó armado.'
        : `¡Te uniste al partido de ${deporte.nombre.toLowerCase()}!`,
    );
  };

  const gestionarAccion = (partido: Partido, accion: AccionPartido) => {
    if (!usuario) return;

    const esCreador = partido.creadorId === usuario.id;

    switch (accion) {
      case 'confirmar':
        if (!esCreador || partido.estado !== 'Armado') return;
        cambiarEstado(partido.id, 'Confirmado');
        notificar('Jugadores confirmados. ¡El partido está listo!');
        break;
      case 'comenzar':
        if (!esCreador || partido.estado !== 'Confirmado') return;
        cambiarEstado(partido.id, 'En juego');
        notificar('El partido comenzó. ¡A jugar!');
        break;
      case 'finalizar':
        if (!esCreador || partido.estado !== 'En juego') return;
        cambiarEstado(partido.id, 'Finalizado');
        notificar('Partido finalizado. Ya podés dejar tu comentario.');
        break;
      case 'cancelar':
        if (
          !esCreador ||
          (partido.estado !== 'Necesitamos jugadores' &&
            partido.estado !== 'Armado' &&
            partido.estado !== 'Confirmado')
        ) {
          return;
        }
        cambiarEstado(partido.id, 'Cancelado');
        notificar('El partido fue cancelado.');
        break;
      default:
        break;
    }
  };

  const crearPartido = (nuevo: NuevoPartido) => {
    if (!usuario) return;

    const partido: Partido = {
      id: crypto.randomUUID(),
      deporte: nuevo.deporte,
      duracionMinutos: nuevo.duracionMinutos,
      zona: nuevo.zona,
      fecha: nuevo.fecha,
      horario: nuevo.horario,
      estado: 'Necesitamos jugadores',
      creadorId: usuario.id,
      jugadores: [usuario.id],
      nivelRequerido: nuevo.nivelRequerido,
      comentarios: [],
    };

    setPartidos((previos) => [partido, ...previos]);
    notificar(
      `Partido de ${DEPORTES[nuevo.deporte].nombre.toLowerCase()} creado en ${capitalizar(nuevo.zona.barrio)}.`,
    );
  };

  const comentar = (partidoId: string, contenido: string) => {
    if (!usuario) return;

    const comentario: Comentario = {
      id: crypto.randomUUID(),
      autor: nombreCompleto(usuario),
      contenido,
      fecha: new Date().toISOString(),
    };

    setPartidos((previos) =>
      previos.map((partido) =>
        partido.id === partidoId
          ? { ...partido, comentarios: [...partido.comentarios, comentario] }
          : partido,
      ),
    );
    notificar('Comentario publicado.');
  };

  const reiniciarDemo = () => {
    setPartidos(PARTIDOS_INICIALES);
    notificar('Datos de ejemplo restaurados.');
  };

  return (
    <>
      {usuario ? (
        <Workspace
          usuario={usuario}
          partidos={partidos}
          onSalir={salir}
          onUnirse={unirse}
          onCrear={crearPartido}
          onAccion={gestionarAccion}
          onComentar={comentar}
          onReiniciar={reiniciarDemo}
        />
      ) : (
        <LoginPage onIngresar={ingresar} />
      )}
      <Toasts toasts={toasts} />
    </>
  );
}
