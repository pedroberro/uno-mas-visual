import {
  DEPORTES,
  capitalizar,
  etiquetaNivel,
  formatearFecha,
  iniciales,
  nombreCompleto,
  obtenerUsuario,
  slugEstado,
} from '../data';
import type { Partido, Usuario } from '../types';
import { IconCalendar, IconClock, IconPin } from './icons';

interface Props {
  partido: Partido;
  usuario: Usuario;
  onAbrir: () => void;
  onUnirse: () => void;
}

const CANTIDAD_AVATARES = 4;

export function MatchCard({ partido, usuario, onAbrir, onUnirse }: Props) {
  const deporte = DEPORTES[partido.deporte];
  const ocupados = partido.jugadores.length;
  const porcentaje = Math.min(100, Math.round((ocupados / deporte.jugadores) * 100));
  const yaUnido = partido.jugadores.includes(usuario.id);
  const puedeUnirse =
    partido.estado === 'Necesitamos jugadores' && !yaUnido && ocupados < deporte.jugadores;
  const creador = obtenerUsuario(partido.creadorId);
  const avatares = partido.jugadores
    .slice(0, CANTIDAD_AVATARES)
    .map((id) => obtenerUsuario(id))
    .filter((jugador): jugador is Usuario => Boolean(jugador));

  return (
    <article className={`partido partido--${partido.deporte}`}>
      <header className="partido__cabecera">
        <span className={`deporte-icono deporte-icono--${partido.deporte}`}>{deporte.emoji}</span>
        <div className="partido__titulo">
          <h3>{deporte.nombre}</h3>
          <p>Nivel {etiquetaNivel(partido.nivelRequerido)}</p>
        </div>
        <span className={`estado estado--${slugEstado(partido.estado)}`}>{partido.estado}</span>
      </header>

      <ul className="partido__meta">
        <li>
          <IconCalendar width={16} height={16} />
          {formatearFecha(partido.fecha)}
        </li>
        <li>
          <IconClock width={16} height={16} />
          {partido.horario} · {partido.duracionMinutos} min
        </li>
        <li>
          <IconPin width={16} height={16} />
          {capitalizar(partido.zona.barrio)}
        </li>
      </ul>

      <div className="partido__jugadores">
        <div className="avatares">
          {avatares.map((jugador) => (
            <span key={jugador.id} className="avatar" title={nombreCompleto(jugador)}>
              {iniciales(jugador)}
            </span>
          ))}
          {ocupados > CANTIDAD_AVATARES && (
            <span className="avatar avatar--mas">+{ocupados - CANTIDAD_AVATARES}</span>
          )}
        </div>
        <span className="conteo">
          <strong>{ocupados}</strong>/{deporte.jugadores}
        </span>
      </div>

      <div className="progreso" aria-hidden="true">
        <span style={{ width: `${porcentaje}%` }} />
      </div>

      <footer className="partido__pie">
        <span className="creador">{creador ? `Creado por ${creador.nombre}` : 'Sin creador'}</span>
        <div className="partido__acciones">
          <button type="button" className="btn btn--fantasma btn--chico" onClick={onAbrir}>
            Ver detalle
          </button>
          {puedeUnirse && (
            <button type="button" className="btn btn--primario btn--chico" onClick={onUnirse}>
              Unirme
            </button>
          )}
        </div>
      </footer>
    </article>
  );
}
