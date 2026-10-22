const CLAVE_SESION = 'unomas:sesion:v2';

export function cargarSesion(): string | null {
  try {
    return window.localStorage.getItem(CLAVE_SESION);
  } catch {
    return null;
  }
}

export function guardarSesion(usuarioId: string | null): void {
  try {
    if (usuarioId) {
      window.localStorage.setItem(CLAVE_SESION, usuarioId);
    } else {
      window.localStorage.removeItem(CLAVE_SESION);
    }
  } catch {
    // Sin almacenamiento disponible: la sesión queda solo en memoria.
  }
}
