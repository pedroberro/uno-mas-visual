import type { Catalogo, Estado, Nivel } from './types';

const EMOJIS_DEPORTE: Record<string, string> = {
  futbol: '⚽',
  basquet: '🏀',
  tenis: '🎾',
  voley: '🏐',
};

export function emojiDeporte(id: string): string {
  return EMOJIS_DEPORTE[id] ?? '🏅';
}

export function nombreDeporte(catalogo: Catalogo, id: string): string {
  return catalogo.deportes.find((deporte) => deporte.id === id)?.nombre ?? capitalizar(id);
}

export function etiquetaNivel(nivel: Nivel | null): string {
  if (!nivel) return 'Sin definir';
  return nivel.charAt(0) + nivel.slice(1).toLowerCase();
}

export function capitalizar(texto: string): string {
  return texto.charAt(0).toUpperCase() + texto.slice(1);
}

export function slugEstado(estado: Estado): string {
  return estado
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/\s+/g, '-');
}

const formatoFecha = new Intl.DateTimeFormat('es-AR', {
  weekday: 'short',
  day: 'numeric',
  month: 'short',
});

const formatoFechaHora = new Intl.DateTimeFormat('es-AR', {
  day: '2-digit',
  month: '2-digit',
  hour: '2-digit',
  minute: '2-digit',
});

export function formatearFecha(iso: string): string {
  return formatoFecha.format(new Date(`${iso}T00:00:00`));
}

export function formatearFechaHora(iso: string): string {
  return formatoFechaHora.format(new Date(iso));
}

export function fechaISOEnDias(dias: number): string {
  const fecha = new Date();
  fecha.setHours(12, 0, 0, 0);
  fecha.setDate(fecha.getDate() + dias);
  const mes = String(fecha.getMonth() + 1).padStart(2, '0');
  const dia = String(fecha.getDate()).padStart(2, '0');
  return `${fecha.getFullYear()}-${mes}-${dia}`;
}

export function lugarDisponible(partido: { jugadores: unknown[]; jugadoresRequeridos: number }): boolean {
  return partido.jugadores.length < partido.jugadoresRequeridos;
}
