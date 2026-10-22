import { useState } from 'react';
import {
  BARRIOS,
  DEPORTES,
  DEPORTES_LISTA,
  capitalizar,
  etiquetaNivel,
  iniciales,
  lugarDisponible,
  nombreCompleto,
} from '../data';
import type { AccionPartido, DeporteId, NuevoPartido, Partido, Usuario } from '../types';
import { CreateMatchModal } from './CreateMatchModal';
import { MatchCard } from './MatchCard';
import { MatchDetailModal } from './MatchDetailModal';
import {
  IconLogout,
  IconPlus,
  IconRefresh,
  IconSearch,
  IconSparkles,
  IconTrophy,
  IconUsers,
} from './icons';

type Vista = 'descubrir' | 'mis' | 'todos';

interface Props {
  usuario: Usuario;
  partidos: Partido[];
  onSalir: () => void;
  onUnirse: (partido: Partido) => void;
  onCrear: (nuevo: NuevoPartido) => void;
  onAccion: (partido: Partido, accion: AccionPartido) => void;
  onComentar: (partidoId: string, contenido: string) => void;
  onReiniciar: () => void;
}

const ETIQUETAS_VISTA: Record<Vista, string> = {
  descubrir: 'Descubrir',
  mis: 'Mis partidos',
  todos: 'Todos',
};

export function Workspace({
  usuario,
  partidos,
  onSalir,
  onUnirse,
  onCrear,
  onAccion,
  onComentar,
  onReiniciar,
}: Props) {
  const [vista, setVista] = useState<Vista>('descubrir');
  const [deporteFiltro, setDeporteFiltro] = useState<'todos' | DeporteId>('todos');
  const [barrioFiltro, setBarrioFiltro] = useState('todos');
  const [busqueda, setBusqueda] = useState('');
  const [partidoAbierto, setPartidoAbierto] = useState<string | null>(null);
  const [creando, setCreando] = useState(false);

  const participantes = (partido: Partido) =>
    partido.jugadores.includes(usuario.id) || partido.creadorId === usuario.id;

  const disponibles = partidos.filter(
    (partido) =>
      (partido.estado === 'Necesitamos jugadores' || partido.estado === 'Armado') &&
      lugarDisponible(partido),
  ).length;
  const misPartidos = partidos.filter(participantes);
  const jugados = misPartidos.filter((partido) => partido.estado === 'Finalizado').length;

  const textoBusqueda = busqueda.trim().toLowerCase();
  const filtrados = partidos
    .filter((partido) => {
      if (vista === 'mis') return participantes(partido);
      if (vista === 'descubrir') {
        return (
          (partido.estado === 'Necesitamos jugadores' || partido.estado === 'Armado') &&
          lugarDisponible(partido)
        );
      }
      return true;
    })
    .filter((partido) => deporteFiltro === 'todos' || partido.deporte === deporteFiltro)
    .filter((partido) => barrioFiltro === 'todos' || partido.zona.barrio === barrioFiltro)
    .filter((partido) => {
      if (!textoBusqueda) return true;
      return [
        DEPORTES[partido.deporte].nombre,
        partido.zona.barrio,
        partido.estado,
        partido.horario,
      ].some((valor) => valor.toLowerCase().includes(textoBusqueda));
    })
    .sort((a, b) => `${a.fecha}${a.horario}`.localeCompare(`${b.fecha}${b.horario}`));

  const partidoSeleccionado = partidos.find((partido) => partido.id === partidoAbierto) ?? null;
  const fechaHoy = capitalizar(
    new Intl.DateTimeFormat('es-AR', {
      weekday: 'long',
      day: 'numeric',
      month: 'long',
    }).format(new Date()),
  );

  return (
    <div className="app">
      <header className="topbar">
        <div className="topbar__contenido">
          <div className="marca marca--compacta">
            <span className="marca__logo">
              <IconSparkles width={18} height={18} />
            </span>
            <span className="marca__nombre">Uno Más</span>
          </div>

          <div className="topbar__acciones">
            <button type="button" className="btn btn--fantasma btn--chico" onClick={onReiniciar}>
              <IconRefresh width={16} height={16} /> Reiniciar demo
            </button>
            <div className="usuario-chip">
              <span className="avatar">{iniciales(usuario)}</span>
              <span className="usuario-chip__texto">
                <strong>{nombreCompleto(usuario)}</strong>
                <small>
                  {etiquetaNivel(usuario.nivel)} · {capitalizar(usuario.zona.barrio)}
                </small>
              </span>
            </div>
            <button
              type="button"
              className="boton-icono"
              onClick={onSalir}
              aria-label="Cerrar sesión"
              title="Cerrar sesión"
            >
              <IconLogout width={18} height={18} />
            </button>
          </div>
        </div>
      </header>

      <main className="contenido">
        <section className="hero">
          <div>
            <p className="hero__saludo">Hola, {usuario.nombre} 👋</p>
            <h1>Partidos en {capitalizar(usuario.zona.barrio)}</h1>
            <p className="hero__fecha">{fechaHoy}</p>
          </div>
          <button type="button" className="btn btn--primario" onClick={() => setCreando(true)}>
            <IconPlus width={18} height={18} /> Crear partido
          </button>
        </section>

        <section className="estadisticas">
          <article className="estadistica">
            <span className="estadistica__icono estadistica__icono--mint">
              <IconUsers width={18} height={18} />
            </span>
            <div>
              <strong>{disponibles}</strong>
              <span>Partidos con cupos</span>
            </div>
          </article>
          <article className="estadistica">
            <span className="estadistica__icono estadistica__icono--violeta">
              <IconSparkles width={18} height={18} />
            </span>
            <div>
              <strong>{misPartidos.length}</strong>
              <span>Mis partidos</span>
            </div>
          </article>
          <article className="estadistica">
            <span className="estadistica__icono estadistica__icono--naranja">
              <IconTrophy width={18} height={18} />
            </span>
            <div>
              <strong>{jugados}</strong>
              <span>Partidos jugados</span>
            </div>
          </article>
          <article className="estadistica">
            <span className="estadistica__icono estadistica__icono--celeste">
              <IconUsers width={18} height={18} />
            </span>
            <div>
              <strong>{etiquetaNivel(usuario.nivel)}</strong>
              <span>Tu nivel</span>
            </div>
          </article>
        </section>

        <nav className="tabs" role="tablist" aria-label="Filtro de partidos">
          {(Object.keys(ETIQUETAS_VISTA) as Vista[]).map((opcion) => (
            <button
              key={opcion}
              type="button"
              role="tab"
              aria-selected={vista === opcion}
              className={vista === opcion ? 'tab tab--activa' : 'tab'}
              onClick={() => setVista(opcion)}
            >
              {ETIQUETAS_VISTA[opcion]}
            </button>
          ))}
        </nav>

        <section className="barra-filtros">
          <label className="buscador">
            <IconSearch width={17} height={17} />
            <input
              type="search"
              value={busqueda}
              onChange={(evento) => setBusqueda(evento.target.value)}
              placeholder="Buscar por deporte, barrio o estado…"
              aria-label="Buscar partidos"
            />
          </label>

          <select
            className="selector"
            value={deporteFiltro}
            onChange={(evento) => setDeporteFiltro(evento.target.value as 'todos' | DeporteId)}
            aria-label="Filtrar por deporte"
          >
            <option value="todos">Todos los deportes</option>
            {DEPORTES_LISTA.map((item) => (
              <option key={item.id} value={item.id}>
                {item.nombre}
              </option>
            ))}
          </select>

          <select
            className="selector"
            value={barrioFiltro}
            onChange={(evento) => setBarrioFiltro(evento.target.value)}
            aria-label="Filtrar por barrio"
          >
            <option value="todos">Todos los barrios</option>
            {BARRIOS.map((item) => (
              <option key={item.barrio} value={item.barrio}>
                {capitalizar(item.barrio)}
              </option>
            ))}
          </select>
        </section>

        {filtrados.length === 0 ? (
          <section className="vacio">
            <span className="vacio__icono">
              <IconSearch width={22} height={22} />
            </span>
            <h2>No encontramos partidos</h2>
            <p>Probá cambiando los filtros o creá un partido nuevo para tu zona.</p>
            <button type="button" className="btn btn--primario" onClick={() => setCreando(true)}>
              <IconPlus width={18} height={18} /> Crear partido
            </button>
          </section>
        ) : (
          <section className="grilla">
            {filtrados.map((partido) => (
              <MatchCard
                key={partido.id}
                partido={partido}
                usuario={usuario}
                onAbrir={() => setPartidoAbierto(partido.id)}
                onUnirse={() => onUnirse(partido)}
              />
            ))}
          </section>
        )}
      </main>

      {partidoSeleccionado && (
        <MatchDetailModal
          partido={partidoSeleccionado}
          usuario={usuario}
          onCerrar={() => setPartidoAbierto(null)}
          onUnirse={() => onUnirse(partidoSeleccionado)}
          onAccion={(accion) => onAccion(partidoSeleccionado, accion)}
          onComentar={(contenido) => onComentar(partidoSeleccionado.id, contenido)}
        />
      )}

      {creando && (
        <CreateMatchModal
          usuario={usuario}
          onCerrar={() => setCreando(false)}
          onCrear={(nuevo) => {
            onCrear(nuevo);
            setCreando(false);
          }}
        />
      )}
    </div>
  );
}
