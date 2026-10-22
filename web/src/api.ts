import type {
  AccionPartido,
  Catalogo,
  NuevoPartido,
  Partido,
  RegistroDatos,
  ResultadoAccion,
  Usuario,
} from './types';

export class ErrorApi extends Error {
  constructor(mensaje: string) {
    super(mensaje);
    this.name = 'ErrorApi';
  }
}

async function pedir<T>(ruta: string, init?: RequestInit): Promise<T> {
  let respuesta: Response;
  try {
    respuesta = await fetch(`/api${ruta}`, {
      headers: { 'Content-Type': 'application/json' },
      ...init,
    });
  } catch {
    throw new ErrorApi('No se pudo conectar con el servidor. ¿Está corriendo el backend?');
  }

  if (!respuesta.ok) {
    let mensaje = 'Ocurrió un error inesperado.';
    try {
      const cuerpo = (await respuesta.json()) as { mensaje?: string };
      if (cuerpo.mensaje) {
        mensaje = cuerpo.mensaje;
      }
    } catch {
      // La respuesta de error no tenía JSON.
    }
    throw new ErrorApi(mensaje);
  }

  if (respuesta.status === 204) {
    return undefined as T;
  }

  return (await respuesta.json()) as T;
}

function conCuerpo(cuerpo: unknown): RequestInit {
  return { method: 'POST', body: JSON.stringify(cuerpo) };
}

export const api = {
  catalogo: () => pedir<Catalogo>('/catalogos'),

  login: (correo: string, contrasenia: string) =>
    pedir<Usuario>('/auth/login', conCuerpo({ correo, contrasenia })),

  registrar: (datos: RegistroDatos) => pedir<Usuario>('/usuarios', conCuerpo(datos)),

  usuario: (id: string) => pedir<Usuario>(`/usuarios/${encodeURIComponent(id)}`),

  partidos: (usuarioId: string) =>
    pedir<Partido[]>(`/partidos?usuarioId=${encodeURIComponent(usuarioId)}`),

  crearPartido: (usuarioId: string, partido: NuevoPartido) =>
    pedir<Partido>('/partidos', conCuerpo({ usuarioId, ...partido })),

  accion: (partidoId: string, accion: AccionPartido, usuarioId: string) =>
    pedir<ResultadoAccion>(`/partidos/${partidoId}/${accion}`, conCuerpo({ usuarioId })),

  comentar: (partidoId: string, usuarioId: string, contenido: string) =>
    pedir<Partido>(`/partidos/${partidoId}/comentarios`, conCuerpo({ usuarioId, contenido })),

  reiniciarDemo: () => pedir<{ mensaje: string }>('/demo/reiniciar', { method: 'POST' }),
};
