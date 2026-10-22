import { useState, type FormEvent } from 'react';
import { api, ErrorApi } from '../api';
import { capitalizar, emojiDeporte, etiquetaNivel, nombreDeporte } from '../formato';
import type { Tema } from '../tema';
import type { Catalogo, RegistroDatos, Usuario } from '../types';
import {
  IconArrowRight,
  IconCalendar,
  IconLock,
  IconMail,
  IconPin,
  IconSparkles,
  IconUsers,
} from './icons';
import { ThemeToggle } from './ThemeToggle';

interface Props {
  catalogo: Catalogo;
  tema: Tema;
  onAlternarTema: () => void;
  onIngresar: (usuario: Usuario) => Promise<void> | void;
  onRegistrarse: (datos: RegistroDatos) => Promise<void>;
}

const CUENTAS_DEMO = [
  { correo: 'juanperez@gmail.com', contrasenia: 'pass123', nombre: 'Juan Perez' },
  { correo: 'luisgomez@gmail.com', contrasenia: 'qwerty', nombre: 'Luis Gomez' },
  { correo: 'analopez@gmail.com', contrasenia: 'abc456', nombre: 'Ana Lopez' },
];

type Modo = 'login' | 'registro';

export function LoginPage({
  catalogo,
  tema,
  onAlternarTema,
  onIngresar,
  onRegistrarse,
}: Props) {
  const [modo, setModo] = useState<Modo>('login');
  const [correo, setCorreo] = useState('');
  const [contrasenia, setContrasenia] = useState('');
  const [error, setError] = useState('');
  const [enviando, setEnviando] = useState(false);

  const [registro, setRegistro] = useState<RegistroDatos>({
    nombre: '',
    apellido: '',
    correo: '',
    contrasenia: '',
    deporteFav: catalogo.deportes[0]?.id ?? 'futbol',
    nivel: 'INTERMEDIO',
    barrio: catalogo.barrios[0]?.barrio ?? 'palermo',
  });

  const enviarLogin = async (evento: FormEvent<HTMLFormElement>) => {
    evento.preventDefault();
    setEnviando(true);
    setError('');
    try {
      const usuario = await api.login(correo, contrasenia);
      await onIngresar(usuario);
    } catch (excepcion) {
      setError(excepcion instanceof ErrorApi ? excepcion.message : 'No se pudo iniciar sesión.');
    } finally {
      setEnviando(false);
    }
  };

  const enviarRegistro = async (evento: FormEvent<HTMLFormElement>) => {
    evento.preventDefault();
    setEnviando(true);
    setError('');
    try {
      await onRegistrarse(registro);
    } catch (excepcion) {
      setError(excepcion instanceof ErrorApi ? excepcion.message : 'No se pudo crear la cuenta.');
    } finally {
      setEnviando(false);
    }
  };

  return (
    <main className="login">
      <div className="login__tema">
        <ThemeToggle tema={tema} onAlternar={onAlternarTema} />
      </div>

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
          {catalogo.deportes.slice(0, 3).map((deporte) => (
            <article key={deporte.id} className="mini-partido">
              <span className={`mini-partido__icono mini-partido__icono--${deporte.id}`}>
                {emojiDeporte(deporte.id)}
              </span>
              <div>
                <strong>{nombreDeporte(catalogo, deporte.id)}</strong>
                <small>{deporte.jugadores} jugadores por partido</small>
              </div>
            </article>
          ))}
        </div>

        <p className="login__nota">
          {catalogo.barrios.length} barrios disponibles ·{' '}
          {capitalizar(catalogo.barrios[0]?.ciudad ?? 'caba')}
        </p>
      </section>

      <section className="login__panel">
        <div className="login__form">
          <div className="tabs tabs--form" role="tablist" aria-label="Acceso">
            <button
              type="button"
              role="tab"
              aria-selected={modo === 'login'}
              className={modo === 'login' ? 'tab tab--activa' : 'tab'}
              onClick={() => {
                setModo('login');
                setError('');
              }}
            >
              Iniciar sesión
            </button>
            <button
              type="button"
              role="tab"
              aria-selected={modo === 'registro'}
              className={modo === 'registro' ? 'tab tab--activa' : 'tab'}
              onClick={() => {
                setModo('registro');
                setError('');
              }}
            >
              Crear cuenta
            </button>
          </div>

          {modo === 'login' ? (
            <form className="form-apilado" onSubmit={enviarLogin}>
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

              <button type="submit" className="btn btn--primario btn--bloque" disabled={enviando}>
                {enviando ? 'Ingresando…' : 'Entrar'}
                <IconArrowRight width={18} height={18} />
              </button>

              <div className="login__separador">
                <span>o entrá con una cuenta demo</span>
              </div>

              <div className="cuentas-demo">
                {CUENTAS_DEMO.map((cuenta) => (
                  <button
                    key={cuenta.correo}
                    type="button"
                    className="cuenta-demo"
                    disabled={enviando}
                    onClick={() => {
                      setCorreo(cuenta.correo);
                      setContrasenia(cuenta.contrasenia);
                      setError('');
                    }}
                  >
                    <span className="avatar">
                      {cuenta.nombre
                        .split(' ')
                        .map((parte) => parte.charAt(0))
                        .join('')}
                    </span>
                    <span className="cuenta-demo__texto">
                      <strong>{cuenta.nombre}</strong>
                      <small>{cuenta.correo}</small>
                    </span>
                  </button>
                ))}
              </div>
            </form>
          ) : (
            <form className="form-apilado" onSubmit={enviarRegistro}>
              <p className="login__form-bajada">Creá tu cuenta para empezar a jugar.</p>

              <div className="form-grid form-grid--ajustado">
                <label className="campo">
                  <span>Nombre</span>
                  <span className="campo__control">
                    <input
                      value={registro.nombre}
                      onChange={(evento) =>
                        setRegistro((previo) => ({ ...previo, nombre: evento.target.value }))
                      }
                      required
                    />
                  </span>
                </label>

                <label className="campo">
                  <span>Apellido</span>
                  <span className="campo__control">
                    <input
                      value={registro.apellido}
                      onChange={(evento) =>
                        setRegistro((previo) => ({ ...previo, apellido: evento.target.value }))
                      }
                      required
                    />
                  </span>
                </label>
              </div>

              <label className="campo">
                <span>Correo</span>
                <span className="campo__control">
                  <IconMail width={18} height={18} />
                  <input
                    type="email"
                    value={registro.correo}
                    onChange={(evento) =>
                      setRegistro((previo) => ({ ...previo, correo: evento.target.value }))
                    }
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
                    value={registro.contrasenia}
                    onChange={(evento) =>
                      setRegistro((previo) => ({ ...previo, contrasenia: evento.target.value }))
                    }
                    minLength={6}
                    required
                  />
                </span>
              </label>

              <div className="form-grid form-grid--ajustado">
                <label className="campo">
                  <span>Deporte favorito</span>
                  <span className="campo__control">
                    <select
                      value={registro.deporteFav}
                      onChange={(evento) =>
                        setRegistro((previo) => ({ ...previo, deporteFav: evento.target.value }))
                      }
                    >
                      {catalogo.deportes.map((deporte) => (
                        <option key={deporte.id} value={deporte.id}>
                          {deporte.nombre}
                        </option>
                      ))}
                    </select>
                  </span>
                </label>

                <label className="campo">
                  <span>Nivel</span>
                  <span className="campo__control">
                    <select
                      value={registro.nivel}
                      onChange={(evento) =>
                        setRegistro((previo) => ({
                          ...previo,
                          nivel: evento.target.value as RegistroDatos['nivel'],
                        }))
                      }
                    >
                      {catalogo.niveles.map((nivel) => (
                        <option key={nivel} value={nivel}>
                          {etiquetaNivel(nivel)}
                        </option>
                      ))}
                    </select>
                  </span>
                </label>
              </div>

              <label className="campo">
                <span>Barrio</span>
                <span className="campo__control">
                  <select
                    value={registro.barrio}
                    onChange={(evento) =>
                      setRegistro((previo) => ({ ...previo, barrio: evento.target.value }))
                    }
                  >
                    {catalogo.barrios.map((barrio) => (
                      <option key={barrio.barrio} value={barrio.barrio}>
                        {capitalizar(barrio.barrio)} ({capitalizar(barrio.ciudad)})
                      </option>
                    ))}
                  </select>
                </span>
              </label>

              {error && (
                <p className="mensaje-error" role="alert">
                  {error}
                </p>
              )}

              <button type="submit" className="btn btn--primario btn--bloque" disabled={enviando}>
                {enviando ? 'Creando cuenta…' : 'Crear cuenta'}
                <IconArrowRight width={18} height={18} />
              </button>
            </form>
          )}
        </div>
      </section>
    </main>
  );
}
