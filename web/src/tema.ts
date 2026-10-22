export type Tema = 'oscuro' | 'claro';

const CLAVE_TEMA = 'unomas:tema:v1';
const ATRIBUTO = 'data-tema';

export const TEMA_POR_DEFECTO: Tema = 'oscuro';

function esTema(valor: unknown): valor is Tema {
  return valor === 'claro' || valor === 'oscuro';
}

export function cargarTema(): Tema {
  try {
    const guardado = window.localStorage.getItem(CLAVE_TEMA);
    if (esTema(guardado)) {
      return guardado;
    }
  } catch {
    // Sin almacenamiento disponible: se usa el tema por defecto.
  }
  return TEMA_POR_DEFECTO;
}

export function aplicarTema(tema: Tema): void {
  document.documentElement.setAttribute(ATRIBUTO, tema);
}

export function guardarTema(tema: Tema): void {
  try {
    window.localStorage.setItem(CLAVE_TEMA, tema);
  } catch {
    // La preferencia queda solo en memoria.
  }
}

export function alternar(tema: Tema): Tema {
  return tema === 'claro' ? 'oscuro' : 'claro';
}
