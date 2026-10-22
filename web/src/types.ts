export type Nivel = 'PRINCIPIANTE' | 'INTERMEDIO' | 'AVANZADO';

export type Estado =
  | 'Necesitamos jugadores'
  | 'Armado'
  | 'Confirmado'
  | 'En juego'
  | 'Finalizado'
  | 'Cancelado';

export type DeporteId = 'futbol' | 'basquet' | 'tenis' | 'voley';

export interface Deporte {
  id: DeporteId;
  nombre: string;
  emoji: string;
  jugadores: number;
}

export interface Barrio {
  ciudad: string;
  barrio: string;
}

export interface Comentario {
  id: string;
  autor: string;
  contenido: string;
  fecha: string;
}

export interface Usuario {
  id: string;
  nombre: string;
  apellido: string;
  correo: string;
  contrasenia: string;
  deporteFav: DeporteId;
  nivel: Nivel;
  zona: Barrio;
}

export interface Partido {
  id: string;
  deporte: DeporteId;
  duracionMinutos: number;
  zona: Barrio;
  fecha: string;
  horario: string;
  estado: Estado;
  creadorId: string;
  jugadores: string[];
  nivelRequerido: Nivel;
  comentarios: Comentario[];
}

export type AccionPartido = 'unirse' | 'confirmar' | 'comenzar' | 'finalizar' | 'cancelar';

export interface NuevoPartido {
  deporte: DeporteId;
  zona: Barrio;
  fecha: string;
  horario: string;
  duracionMinutos: number;
  nivelRequerido: Nivel;
}
