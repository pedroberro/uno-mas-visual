import type { Partido } from './types';

const CLAVE_PARTIDOS = 'unomas:partidos:v1';
const CLAVE_SESION = 'unomas:sesion:v1';

function leer(clave: string): string | null {
  try {
    return window.localStorage.getItem(clave);
  } catch {
    return null;
  }
}

function escribir(clave: string, valor: string): void {
  try {
    window.localStorage.setItem(clave, valor);
  } catch {
    // Si el navegador bloquea el almacenamiento, la demo sigue funcionando en memoria.
  }
}

export function cargarPartidos(): Partido[] | null {
  const crudo = leer(CLAVE_PARTIDOS);
  if (!crudo) return null;
  try {
    const datos: unknown = JSON.parse(crudo);
    return Array.isArray(datos) ? (datos as Partido[]) : null;
  } catch {
    return null;
  }
}

export function guardarPartidos(partidos: Partido[]): void {
  escribir(CLAVE_PARTIDOS, JSON.stringify(partidos));
}

export function cargarSesion(): string | null {
  return leer(CLAVE_SESION);
}

export function guardarSesion(usuarioId: string | null): void {
  try {
    if (usuarioId) {
      window.localStorage.setItem(CLAVE_SESION, usuarioId);
    } else {
      window.localStorage.removeItem(CLAVE_SESION);
    }
  } catch {
    // Sin persistencia disponible: la sesión queda solo en memoria.
  }
}
