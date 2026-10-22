import { useState, type FormEvent } from 'react';
import { BARRIOS, DEPORTES_LISTA, NIVELES, capitalizar, etiquetaNivel, fechaISOEnDias } from '../data';
import type { DeporteId, Nivel, NuevoPartido, Usuario } from '../types';
import { Modal } from './Modal';

interface Props {
  usuario: Usuario;
  onCerrar: () => void;
  onCrear: (nuevo: NuevoPartido) => void;
}

interface Errores {
  fecha?: string;
  horario?: string;
  duracion?: string;
}

export function CreateMatchModal({ usuario, onCerrar, onCrear }: Props) {
  const [deporte, setDeporte] = useState<DeporteId>(usuario.deporteFav);
  const [barrio, setBarrio] = useState(usuario.zona.barrio);
  const [fecha, setFecha] = useState(fechaISOEnDias(1));
  const [horario, setHorario] = useState('19:00');
  const [duracion, setDuracion] = useState('90');
  const [nivel, setNivel] = useState<Nivel>(usuario.nivel);
  const [errores, setErrores] = useState<Errores>({});

  const hoy = fechaISOEnDias(0);

  const enviar = (evento: FormEvent<HTMLFormElement>) => {
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

    const zona = BARRIOS.find((item) => item.barrio === barrio);
    if (!zona) return;

    onCrear({
      deporte,
      zona,
      fecha,
      horario,
      duracionMinutos: minutos,
      nivelRequerido: nivel,
    });
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
            <select value={deporte} onChange={(evento) => setDeporte(evento.target.value as DeporteId)}>
              {DEPORTES_LISTA.map((item) => (
                <option key={item.id} value={item.id}>
                  {item.emoji} {item.nombre} · {item.jugadores} jugadores
                </option>
              ))}
            </select>
          </span>
        </label>

        <label className="campo">
          <span>Barrio</span>
          <span className="campo__control">
            <select value={barrio} onChange={(evento) => setBarrio(evento.target.value)}>
              {BARRIOS.map((item) => (
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
            <input type="date" value={fecha} min={hoy} onChange={(evento) => setFecha(evento.target.value)} />
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
              {NIVELES.map((item) => (
                <option key={item} value={item}>
                  {etiquetaNivel(item)}
                </option>
              ))}
            </select>
          </span>
        </label>

        <div className="form-acciones">
          <button type="button" className="btn btn--fantasma" onClick={onCerrar}>
            Cancelar
          </button>
          <button type="submit" className="btn btn--primario">
            Crear partido
          </button>
        </div>
      </form>
    </Modal>
  );
}
