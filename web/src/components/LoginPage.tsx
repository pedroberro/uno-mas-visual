import { useState, type FormEvent } from 'react';
import { DEPORTES, USUARIOS, iniciales, nombreCompleto, obtenerUsuario } from '../data';
import type { DeporteId, Usuario } from '../types';
import {
  IconArrowRight,
  IconCalendar,
  IconLock,
  IconMail,
  IconPin,
  IconSparkles,
  IconUsers,
} from './icons';

interface Props {
  onIngresar: (usuario: Usuario) => void;
}

const IDS_DEMO = ['u1', 'u3', 'u2'];

const PARTIDOS_PREVIEW: { deporte: DeporteId; barrio: string; horario: string; jugadores: string; estado: string }[] = [
  { deporte: 'futbol', barrio: 'Palermo', horario: '19:00', jugadores: '15/22', estado: 'Necesitamos jugadores' },
  { deporte: 'tenis', barrio: 'Belgrano', horario: '09:00', jugadores: '1/2', estado: 'Necesitamos jugadores' },
  { deporte: 'basquet', barrio: 'Recoleta', horario: '20:30', jugadores: '10/10', estado: 'Armado' },
];

export function LoginPage({ onIngresar }: Props) {
  const [correo, setCorreo] = useState('');
  const [contrasenia, setContrasenia] = useState('');
  const [error, setError] = useState('');

  const enviar = (evento: FormEvent<HTMLFormElement>) => {
    evento.preventDefault();
    const usuario = USUARIOS.find(
      (candidato) => candidato.correo.toLowerCase() === correo.trim().toLowerCase(),
    );
    if (!usuario) {
      setError('No encontramos una cuenta con ese correo. Probá con una cuenta demo.');
      return;
    }
    if (usuario.contrasenia !== contrasenia) {
      setError('La contraseña no es correcta.');
      return;
    }
    setError('');
    onIngresar(usuario);
  };

  return (
    <main className="login">
      <section className="login__marca">
        <div className="marca">
          <span className="marca__logo">
            <IconSparkles width={20} height={20} />
          </span>
          <span className="marca__nombre">Uno Más</span>
        </div>

        <h1>
          Completá tu equipo.
          <br />
          Jugá en tu barrio.
        </h1>
        <p className="login__bajada">
          Organizá partidos de fútbol, básquet, tenis y vóley, encontrá jugadores cerca y seguí el
          estado de cada partido desde el primer cupo hasta el resultado final.
        </p>

        <ul className="login__puntos">
          <li>
            <IconPin width={18} height={18} /> Partidos filtrados por barrio y nivel
          </li>
          <li>
            <IconUsers width={18} height={18} /> Sumate con un clic y completá el equipo
          </li>
          <li>
            <IconCalendar width={18} height={18} /> Armado, confirmado, en juego y finalizado
          </li>
        </ul>

        <div className="login__preview">
          {PARTIDOS_PREVIEW.map((partido) => (
            <article key={`${partido.deporte}-${partido.barrio}`} className="mini-partido">
              <span className={`mini-partido__icono mini-partido__icono--${partido.deporte}`}>
                {DEPORTES[partido.deporte].emoji}
              </span>
              <div>
                <strong>
                  {DEPORTES[partido.deporte].nombre} · {partido.barrio}
                </strong>
                <small>
                  {partido.horario} · {partido.jugadores} jugadores · {partido.estado}
                </small>
              </div>
            </article>
          ))}
        </div>

        <p className="login__nota">Demo visual · los datos son de ejemplo</p>
      </section>

      <section className="login__panel">
        <form className="login__form" onSubmit={enviar}>
          <h2>Iniciar sesión</h2>
          <p className="login__form-bajada">Ingresá para ver los partidos de tu zona.</p>

          <label className="campo">
            <span>Correo</span>
            <span className="campo__control">
              <IconMail width={18} height={18} />
              <input
                type="email"
                value={correo}
                onChange={(evento) => setCorreo(evento.target.value)}
                placeholder="juanperez@gmail.com"
                autoComplete="email"
                required
              />
            </span>
          </label>

          <label className="campo">
            <span>Contraseña</span>
            <span className="campo__control">
              <IconLock width={18} height={18} />
              <input
                type="password"
                value={contrasenia}
                onChange={(evento) => setContrasenia(evento.target.value)}
                placeholder="••••••••"
                autoComplete="current-password"
                required
              />
            </span>
          </label>

          {error && (
            <p className="mensaje-error" role="alert">
              {error}
            </p>
          )}

          <button type="submit" className="btn btn--primario btn--bloque">
            Entrar
            <IconArrowRight width={18} height={18} />
          </button>

          <div className="login__separador">
            <span>o entrá con una cuenta demo</span>
          </div>

          <div className="cuentas-demo">
            {IDS_DEMO.map((id) => {
              const usuario = obtenerUsuario(id);
              if (!usuario) return null;
              return (
                <button
                  key={id}
                  type="button"
                  className="cuenta-demo"
                  onClick={() => onIngresar(usuario)}
                >
                  <span className="avatar">{iniciales(usuario)}</span>
                  <span className="cuenta-demo__texto">
                    <strong>{nombreCompleto(usuario)}</strong>
                    <small>{usuario.correo}</small>
                  </span>
                </button>
              );
            })}
          </div>

          <p className="login__ayuda">
            Cualquier otro usuario demo usa la contraseña <code>demo1234</code>.
          </p>
        </form>
      </section>
    </main>
  );
}
