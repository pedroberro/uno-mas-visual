export type Nivel = 'PRINCIPIANTE' | 'INTERMEDIO' | 'AVANZADO';

export type Estado =
  | 'Necesitamos jugadores'
  | 'Armado'
  | 'Confirmado'
  | 'En juego'
  | 'Finalizado'
  | 'Cancelado';

export interface Barrio {
  ciudad: string;
  barrio: string;
}

export interface Deporte {
  id: string;
  nombre: string;
  jugadores: number;
}

export interface Catalogo {
  deportes: Deporte[];
  barrios: Barrio[];
  niveles: Nivel[];
}

export interface Notificacion {
  mensaje: string;
  fecha: string;
}

export interface Usuario {
  id: string;
  nombre: string;
  apellido: string;
  correo: string;
  deporteFav: string | null;
  nivel: Nivel;
  zona: Barrio;
  notificaciones: Notificacion[];
}

export interface Jugador {
  id: string;
  nombreCompleto: string;
  iniciales: string;
  nivel: Nivel;
  creador: boolean;
}

export interface Comentario {
  autor: string;
  contenido: string;
  fecha: string;
}

export interface Partido {
  id: string;
  deporte: string;
  duracionMinutos: number;
  zona: Barrio;
  fecha: string;
  horario: string;
  estado: Estado;
  creadorId: string | null;
  creadorNombre: string | null;
  jugadores: Jugador[];
  jugadoresRequeridos: number;
  nivelRequerido: Nivel | null;
  estrategia: string | null;
  comentarios: Comentario[];
  invitado: boolean;
}

export type AccionPartido = 'unirse' | 'confirmar' | 'comenzar' | 'finalizar' | 'cancelar';

export interface ResultadoAccion {
  partido: Partido;
  mensaje: string;
}

export interface NuevoPartido {
  deporte: string;
  barrio: string;
  fecha: string;
  horario: string;
  duracionMinutos: number;
  nivelRequerido: Nivel;
  estrategia: 'cercania' | 'nivel' | 'historial';
  partidosPrevios?: number;
}

export interface RegistroDatos {
  nombre: string;
  apellido: string;
  correo: string;
  contrasenia: string;
  deporteFav: string;
  nivel: Nivel;
  barrio: string;
}
