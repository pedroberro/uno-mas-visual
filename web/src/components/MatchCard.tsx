import {
  capitalizar,
  emojiDeporte,
  etiquetaNivel,
  formatearFecha,
  lugarDisponible,
  nombreDeporte,
  slugEstado,
} from '../formato';
import type { Catalogo, Partido, Usuario } from '../types';
import { IconCalendar, IconClock, IconPin } from './icons';

interface Props {
  partido: Partido;
  usuario: Usuario;
  catalogo: Catalogo;
  onAbrir: () => void;
  onUnirse: () => void;
}

const CANTIDAD_AVATARES = 4;

export function MatchCard({ partido, usuario, catalogo, onAbrir, onUnirse }: Props) {
  const ocupados = partido.jugadores.length;
  const porcentaje = Math.min(100, Math.round((ocupados / partido.jugadoresRequeridos) * 100));
  const yaUnido = partido.jugadores.some((jugador) => jugador.id === usuario.id);
  const puedeUnirse =
    partido.estado === 'Necesitamos jugadores' && !yaUnido && lugarDisponible(partido);
  const avatares = partido.jugadores.slice(0, CANTIDAD_AVATARES);

  return (
    <article className={`partido partido--${partido.deporte}`}>
      <header className="partido__cabecera">
        <span className={`deporte-icono deporte-icono--${partido.deporte}`}>
          {emojiDeporte(partido.deporte)}
        </span>
        <div className="partido__titulo">
          <h3>{nombreDeporte(catalogo, partido.deporte)}</h3>
          <p>Nivel {etiquetaNivel(partido.nivelRequerido)}</p>
        </div>
        <span className={`estado estado--${slugEstado(partido.estado)}`}>{partido.estado}</span>
      </header>

      {partido.invitado && <p className="etiqueta-invitacion">Te invitaron a este partido</p>}

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
            <span key={jugador.id} className="avatar" title={jugador.nombreCompleto}>
              {jugador.iniciales}
            </span>
          ))}
          {ocupados > CANTIDAD_AVATARES && (
            <span className="avatar avatar--mas">+{ocupados - CANTIDAD_AVATARES}</span>
          )}
        </div>
        <span className="conteo">
          <strong>{ocupados}</strong>/{partido.jugadoresRequeridos}
        </span>
      </div>

      <div className="progreso" aria-hidden="true">
        <span style={{ width: `${porcentaje}%` }} />
      </div>

      <footer className="partido__pie">
        <span className="creador">
          {partido.creadorNombre ? `Creado por ${partido.creadorNombre}` : 'Sin creador'}
        </span>
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
