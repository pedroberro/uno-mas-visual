import { useState, type FormEvent } from 'react';
import { capitalizar, etiquetaNivel, fechaISOEnDias } from '../formato';
import type { Catalogo, Nivel, NuevoPartido, Usuario } from '../types';
import { Modal } from './Modal';

interface Props {
  usuario: Usuario;
  catalogo: Catalogo;
  onCerrar: () => void;
  onCrear: (nuevo: NuevoPartido) => Promise<boolean>;
}

interface Errores {
  fecha?: string;
  horario?: string;
  duracion?: string;
}

type Estrategia = NuevoPartido['estrategia'];

export function CreateMatchModal({ usuario, catalogo, onCerrar, onCrear }: Props) {
  const [deporte, setDeporte] = useState(usuario.deporteFav ?? catalogo.deportes[0]?.id ?? 'futbol');
  const [barrio, setBarrio] = useState(usuario.zona.barrio);
  const [fecha, setFecha] = useState(fechaISOEnDias(1));
  const [horario, setHorario] = useState('19:00');
  const [duracion, setDuracion] = useState('90');
  const [nivel, setNivel] = useState<Nivel>(usuario.nivel);
  const [estrategia, setEstrategia] = useState<Estrategia>('cercania');
  const [partidosPrevios, setPartidosPrevios] = useState('1');
  const [errores, setErrores] = useState<Errores>({});
  const [enviando, setEnviando] = useState(false);

  const hoy = fechaISOEnDias(0);

  const enviar = async (evento: FormEvent<HTMLFormElement>) => {
    evento.preventDefault();
    const nuevosErrores: Errores = {};

    if (!fecha) {
      nuevosErrores.fecha = 'Elegí una fecha.';
    } else if (fecha < hoy) {
      nuevosErrores.fecha = 'La fecha no puede ser anterior a hoy.';
    }

    if (!horario) {
      nuevosErrores.horario = 'Elegí un horario.';
    }

    const minutos = Number(duracion);
    if (!Number.isFinite(minutos) || minutos < 30 || minutos > 180) {
      nuevosErrores.duracion = 'La duración debe estar entre 30 y 180 minutos.';
    }

    setErrores(nuevosErrores);
    if (Object.keys(nuevosErrores).length > 0) return;

    setEnviando(true);
    const creado = await onCrear({
      deporte,
      barrio,
      fecha,
      horario,
      duracionMinutos: minutos,
      nivelRequerido: nivel,
      estrategia,
      partidosPrevios: estrategia === 'historial' ? Number(partidosPrevios) || 0 : undefined,
    });
    setEnviando(false);

    if (creado) {
      onCerrar();
    }
  };

  return (
    <Modal
      titulo="Crear partido"
      subtitulo="Definí el deporte, la zona y cuándo se juega."
      onCerrar={onCerrar}
    >
      <form className="form-grid" onSubmit={enviar}>
        <label className="campo">
          <span>Deporte</span>
          <span className="campo__control">
            <select value={deporte} onChange={(evento) => setDeporte(evento.target.value)}>
              {catalogo.deportes.map((item) => (
                <option key={item.id} value={item.id}>
                  {item.nombre} · {item.jugadores} jugadores
                </option>
              ))}
            </select>
          </span>
        </label>

        <label className="campo">
          <span>Barrio</span>
          <span className="campo__control">
            <select value={barrio} onChange={(evento) => setBarrio(evento.target.value)}>
              {catalogo.barrios.map((item) => (
                <option key={item.barrio} value={item.barrio}>
                  {capitalizar(item.barrio)} ({capitalizar(item.ciudad)})
                </option>
              ))}
            </select>
          </span>
        </label>

        <label className="campo">
          <span>Fecha</span>
          <span className="campo__control">
            <input
              type="date"
              value={fecha}
              min={hoy}
              onChange={(evento) => setFecha(evento.target.value)}
            />
          </span>
          {errores.fecha && <small className="campo__error">{errores.fecha}</small>}
        </label>

        <label className="campo">
          <span>Horario</span>
          <span className="campo__control">
            <input
              type="time"
              value={horario}
              onChange={(evento) => setHorario(evento.target.value)}
            />
          </span>
          {errores.horario && <small className="campo__error">{errores.horario}</small>}
        </label>

        <label className="campo">
          <span>Duración (minutos)</span>
          <span className="campo__control">
            <input
              type="number"
              value={duracion}
              min={30}
              max={180}
              step={15}
              onChange={(evento) => setDuracion(evento.target.value)}
            />
          </span>
          {errores.duracion && <small className="campo__error">{errores.duracion}</small>}
        </label>

        <label className="campo">
          <span>Nivel requerido</span>
          <span className="campo__control">
            <select value={nivel} onChange={(evento) => setNivel(evento.target.value as Nivel)}>
              {catalogo.niveles.map((item) => (
                <option key={item} value={item}>
                  {etiquetaNivel(item)}
                </option>
              ))}
            </select>
          </span>
        </label>

        <label className="campo">
          <span>Estrategia de emparejamiento</span>
          <span className="campo__control">
            <select
              value={estrategia}
              onChange={(evento) => setEstrategia(evento.target.value as Estrategia)}
            >
              <option value="cercania">Por cercanía</option>
              <option value="nivel">Por nivel de habilidad</option>
              <option value="historial">Por historial de partidos</option>
            </select>
          </span>
        </label>

        {estrategia === 'historial' && (
          <label className="campo">
            <span>Partidos previos requeridos</span>
            <span className="campo__control">
              <input
                type="number"
                value={partidosPrevios}
                min={0}
                max={50}
                onChange={(evento) => setPartidosPrevios(evento.target.value)}
              />
            </span>
          </label>
        )}

        <div className="form-acciones">
          <button type="button" className="btn btn--fantasma" onClick={onCerrar}>
            Cancelar
          </button>
          <button type="submit" className="btn btn--primario" disabled={enviando}>
            {enviando ? 'Creando…' : 'Crear partido'}
          </button>
        </div>
      </form>
    </Modal>
  );
}
