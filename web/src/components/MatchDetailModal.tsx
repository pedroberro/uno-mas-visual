import { useState, type FormEvent } from 'react';
import {
  capitalizar,
  etiquetaNivel,
  formatearFecha,
  formatearFechaHora,
  lugarDisponible,
  nombreDeporte,
} from '../formato';
import type { AccionPartido, Catalogo, Partido, Usuario } from '../types';
import {
  IconBan,
  IconCalendar,
  IconCheck,
  IconClock,
  IconFlag,
  IconPin,
  IconPlay,
  IconSend,
  IconUsers,
} from './icons';
import { Modal } from './Modal';

interface Props {
  partido: Partido;
  usuario: Usuario;
  catalogo: Catalogo;
  onCerrar: () => void;
  onUnirse: () => void;
  onAccion: (accion: AccionPartido) => void;
  onComentar: (contenido: string) => void;
}

const PASOS = ['Necesitamos jugadores', 'Armado', 'Confirmado', 'En juego', 'Finalizado'] as const;

export function MatchDetailModal({
  partido,
  usuario,
  catalogo,
  onCerrar,
  onUnirse,
  onAccion,
  onComentar,
}: Props) {
  const [comentario, setComentario] = useState('');
  const ocupados = partido.jugadores.length;
  const esCreador = partido.creadorId === usuario.id;
  const esJugador = partido.jugadores.some((jugador) => jugador.id === usuario.id);
  const participa = esCreador || esJugador;

  const puedeUnirse =
    partido.estado === 'Necesitamos jugadores' && !esJugador && lugarDisponible(partido);
  const puedeConfirmar = esCreador && partido.estado === 'Armado';
  const puedeComenzar = esCreador && partido.estado === 'Confirmado';
  const puedeFinalizar = esCreador && partido.estado === 'En juego';
  const puedeCancelar =
    esCreador &&
    (partido.estado === 'Necesitamos jugadores' ||
      partido.estado === 'Armado' ||
      partido.estado === 'Confirmado');

  const indicePaso = PASOS.findIndex((paso) => paso === partido.estado);
  const sinAcciones =
    !puedeUnirse && !puedeConfirmar && !puedeComenzar && !puedeFinalizar && !puedeCancelar;

  const enviarComentario = (evento: FormEvent<HTMLFormElement>) => {
    evento.preventDefault();
    const texto = comentario.trim();
    if (!texto) return;
    onComentar(texto);
    setComentario('');
  };

  return (
    <Modal
      titulo={`${nombreDeporte(catalogo, partido.deporte)} · ${capitalizar(partido.zona.barrio)}`}
      subtitulo={`${formatearFecha(partido.fecha)} a las ${partido.horario}`}
      onCerrar={onCerrar}
      amplio
    >
      {partido.estado === 'Cancelado' ? (
        <p className="aviso aviso--peligro">
          <IconBan width={18} height={18} /> Este partido fue cancelado.
        </p>
      ) : (
        <ol className="pasos">
          {PASOS.map((paso, indice) => (
            <li
              key={paso}
              className={indice <= indicePaso ? 'pasos__item pasos__item--activo' : 'pasos__item'}
            >
              <span className="pasos__punto">
                {indice < indicePaso ? <IconCheck width={12} height={12} /> : indice + 1}
              </span>
              <span className="pasos__texto">{paso}</span>
            </li>
          ))}
        </ol>
      )}

      <dl className="detalle-grid">
        <div>
          <dt>
            <IconCalendar width={16} height={16} /> Fecha
          </dt>
          <dd>{formatearFecha(partido.fecha)}</dd>
        </div>
        <div>
          <dt>
            <IconClock width={16} height={16} /> Horario
          </dt>
          <dd>
            {partido.horario} · {partido.duracionMinutos} min
          </dd>
        </div>
        <div>
          <dt>
            <IconPin width={16} height={16} /> Zona
          </dt>
          <dd>
            {capitalizar(partido.zona.barrio)} ({capitalizar(partido.zona.ciudad)})
          </dd>
        </div>
        <div>
          <dt>
            <IconUsers width={16} height={16} /> Jugadores
          </dt>
          <dd>
            {ocupados}/{partido.jugadoresRequeridos} · nivel{' '}
            {etiquetaNivel(partido.nivelRequerido)}
          </dd>
        </div>
      </dl>

      <section className="bloque">
        <h3>Participantes</h3>
        {partido.jugadores.length === 0 ? (
          <p className="texto-suave">Todavía no hay jugadores anotados.</p>
        ) : (
          <ul className="jugadores-lista">
            {partido.jugadores.map((jugador) => (
              <li key={jugador.id}>
                <span className="avatar avatar--chico">{jugador.iniciales}</span>
                <span className="jugadores-lista__nombre">
                  {jugador.nombreCompleto}
                  {jugador.creador && <em> · creador</em>}
                </span>
                <span className="jugadores-lista__nivel">{etiquetaNivel(jugador.nivel)}</span>
              </li>
            ))}
          </ul>
        )}
        {partido.estrategia && (
          <p className="texto-suave">Emparejamiento: {partido.estrategia}</p>
        )}
      </section>

      <section className="bloque">
        <h3>Acciones</h3>
        <div className="detalle-acciones">
          {puedeUnirse && (
            <button type="button" className="btn btn--primario" onClick={onUnirse}>
              <IconUsers width={17} height={17} /> Unirme al partido
            </button>
          )}
          {puedeConfirmar && (
            <button type="button" className="btn btn--primario" onClick={() => onAccion('confirmar')}>
              <IconCheck width={17} height={17} /> Confirmar jugadores
            </button>
          )}
          {puedeComenzar && (
            <button type="button" className="btn btn--primario" onClick={() => onAccion('comenzar')}>
              <IconPlay width={17} height={17} /> Comenzar partido
            </button>
          )}
          {puedeFinalizar && (
            <button type="button" className="btn btn--primario" onClick={() => onAccion('finalizar')}>
              <IconFlag width={17} height={17} /> Finalizar partido
            </button>
          )}
          {puedeCancelar && (
            <button type="button" className="btn btn--peligro" onClick={() => onAccion('cancelar')}>
              <IconBan width={17} height={17} /> Cancelar partido
            </button>
          )}
          {sinAcciones && (
            <p className="texto-suave">
              {participa
                ? 'No hay acciones disponibles para el estado actual del partido.'
                : 'Solo el creador puede gestionar el partido.'}
            </p>
          )}
        </div>
      </section>

      <section className="bloque">
        <h3>Comentarios</h3>
        {partido.comentarios.length === 0 ? (
          <p className="texto-suave">Todavía no hay comentarios.</p>
        ) : (
          <ul className="comentarios">
            {partido.comentarios.map((item, indice) => (
              <li key={`${item.fecha}-${indice}`} className="comentario">
                <header>
                  <strong>{item.autor}</strong>
                  <time dateTime={item.fecha}>{formatearFechaHora(item.fecha)}</time>
                </header>
                <p>{item.contenido}</p>
              </li>
            ))}
          </ul>
        )}

        {participa ? (
          <form className="comentario-form" onSubmit={enviarComentario}>
            <textarea
              value={comentario}
              onChange={(evento) => setComentario(evento.target.value)}
              placeholder="Dejá un comentario sobre el partido…"
              rows={2}
              maxLength={280}
            />
            <button
              type="submit"
              className="btn btn--primario btn--chico"
              disabled={!comentario.trim()}
            >
              <IconSend width={16} height={16} /> Comentar
            </button>
          </form>
        ) : (
          <p className="texto-suave">Solo los participantes pueden comentar.</p>
        )}
      </section>
    </Modal>
  );
}
