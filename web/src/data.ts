import type {
  Barrio,
  Comentario,
  Deporte,
  DeporteId,
  Estado,
  Nivel,
  Partido,
  Usuario,
} from './types';

const CIUDAD = 'caba';

export const DEPORTES: Record<DeporteId, Deporte> = {
  futbol: { id: 'futbol', nombre: 'Fútbol', emoji: '⚽', jugadores: 22 },
  basquet: { id: 'basquet', nombre: 'Básquet', emoji: '🏀', jugadores: 10 },
  tenis: { id: 'tenis', nombre: 'Tenis', emoji: '🎾', jugadores: 2 },
  voley: { id: 'voley', nombre: 'Vóley', emoji: '🏐', jugadores: 12 },
};

export const DEPORTES_LISTA: Deporte[] = Object.values(DEPORTES);

export const BARRIOS: Barrio[] = ['palermo', 'recoleta', 'belgrano', 'san telmo', 'caballito'].map(
  (barrio) => ({ ciudad: CIUDAD, barrio }),
);

export const NIVELES: Nivel[] = ['PRINCIPIANTE', 'INTERMEDIO', 'AVANZADO'];

function crearUsuario(
  id: string,
  nombre: string,
  apellido: string,
  correo: string,
  contrasenia: string,
  deporteFav: DeporteId,
  nivel: Nivel,
  barrio: string,
): Usuario {
  return {
    id,
    nombre,
    apellido,
    correo,
    contrasenia,
    deporteFav,
    nivel,
    zona: { ciudad: CIUDAD, barrio },
  };
}

const CLAVE_DEMO = 'demo1234';

export const USUARIOS: Usuario[] = [
  crearUsuario('u1', 'Juan', 'Perez', 'juanperez@gmail.com', 'pass123', 'futbol', 'INTERMEDIO', 'palermo'),
  crearUsuario('u2', 'Ana', 'Lopez', 'analopez@gmail.com', 'abc456', 'basquet', 'AVANZADO', 'recoleta'),
  crearUsuario('u3', 'Luis', 'Gomez', 'luisgomez@gmail.com', 'qwerty', 'tenis', 'PRINCIPIANTE', 'belgrano'),
  crearUsuario('u4', 'Marta', 'Fernandez', 'martafernandez@gmail.com', '123456', 'voley', 'INTERMEDIO', 'san telmo'),
  crearUsuario('u5', 'Carlos', 'Sanchez', 'carlossanchez@gmail.com', 'admin', 'futbol', 'AVANZADO', 'caballito'),
  crearUsuario('u6', 'Matias', 'Rodriguez', 'matirodriguez@gmail.com', 'matirodriguez', 'tenis', 'PRINCIPIANTE', 'palermo'),
  crearUsuario('u7', 'Sofía', 'Ramírez', 'sofiaramirez@gmail.com', CLAVE_DEMO, 'futbol', 'INTERMEDIO', 'palermo'),
  crearUsuario('u8', 'Tomás', 'Duarte', 'tomasduarte@gmail.com', CLAVE_DEMO, 'basquet', 'PRINCIPIANTE', 'belgrano'),
  crearUsuario('u9', 'Valentina', 'Cáceres', 'valentinacaceres@gmail.com', CLAVE_DEMO, 'voley', 'AVANZADO', 'recoleta'),
  crearUsuario('u10', 'Bruno', 'Silva', 'brunosilva@gmail.com', CLAVE_DEMO, 'futbol', 'PRINCIPIANTE', 'san telmo'),
  crearUsuario('u11', 'Camila', 'Ortiz', 'camilaortiz@gmail.com', CLAVE_DEMO, 'tenis', 'INTERMEDIO', 'caballito'),
  crearUsuario('u12', 'Nicolás', 'Ferreyra', 'nicolasferreyra@gmail.com', CLAVE_DEMO, 'basquet', 'INTERMEDIO', 'palermo'),
  crearUsuario('u13', 'Lucía', 'Benítez', 'luciabenitez@gmail.com', CLAVE_DEMO, 'voley', 'PRINCIPIANTE', 'belgrano'),
  crearUsuario('u14', 'Martín', 'Aguirre', 'martinaguirre@gmail.com', CLAVE_DEMO, 'futbol', 'AVANZADO', 'recoleta'),
  crearUsuario('u15', 'Julieta', 'Sosa', 'julietasosa@gmail.com', CLAVE_DEMO, 'tenis', 'AVANZADO', 'san telmo'),
  crearUsuario('u16', 'Facundo', 'Ríos', 'facundorios@gmail.com', CLAVE_DEMO, 'basquet', 'AVANZADO', 'caballito'),
  crearUsuario('u17', 'Rocío', 'Paredes', 'rocioparedes@gmail.com', CLAVE_DEMO, 'voley', 'INTERMEDIO', 'palermo'),
  crearUsuario('u18', 'Ignacio', 'Molina', 'ignaciomolina@gmail.com', CLAVE_DEMO, 'basquet', 'PRINCIPIANTE', 'san telmo'),
  crearUsuario('u19', 'Agustina', 'Vega', 'agustinavega@gmail.com', CLAVE_DEMO, 'futbol', 'INTERMEDIO', 'belgrano'),
  crearUsuario('u20', 'Damián', 'Quiroga', 'damianquiroga@gmail.com', CLAVE_DEMO, 'tenis', 'AVANZADO', 'recoleta'),
  crearUsuario('u21', 'Paula', 'Medina', 'paulamedina@gmail.com', CLAVE_DEMO, 'voley', 'AVANZADO', 'caballito'),
  crearUsuario('u22', 'Emiliano', 'Castro', 'emilianocastro@gmail.com', CLAVE_DEMO, 'futbol', 'AVANZADO', 'palermo'),
];

function aISO(fecha: Date): string {
  const ano = fecha.getFullYear();
  const mes = String(fecha.getMonth() + 1).padStart(2, '0');
  const dia = String(fecha.getDate()).padStart(2, '0');
  return `${ano}-${mes}-${dia}`;
}

export function fechaISOEnDias(dias: number): string {
  const fecha = new Date();
  fecha.setHours(12, 0, 0, 0);
  fecha.setDate(fecha.getDate() + dias);
  return aISO(fecha);
}

function fechaComentario(dias: number, hora: number): string {
  const fecha = new Date();
  fecha.setDate(fecha.getDate() + dias);
  fecha.setHours(hora, 0, 0, 0);
  return fecha.toISOString();
}

function comentario(autor: string, contenido: string, dias: number, hora: number): Comentario {
  return { id: `c-${autor}-${dias}-${hora}`, autor, contenido, fecha: fechaComentario(dias, hora) };
}

interface DatosPartido {
  id: string;
  deporte: DeporteId;
  barrio: string;
  dias: number;
  horario: string;
  duracion: number;
  nivel: Nivel;
  creador: string;
  jugadores: string[];
  estado: Estado;
  comentarios?: Comentario[];
}

function crearPartido(datos: DatosPartido): Partido {
  return {
    id: datos.id,
    deporte: datos.deporte,
    duracionMinutos: datos.duracion,
    zona: { ciudad: CIUDAD, barrio: datos.barrio },
    fecha: fechaISOEnDias(datos.dias),
    horario: datos.horario,
    estado: datos.estado,
    creadorId: datos.creador,
    jugadores: datos.jugadores,
    nivelRequerido: datos.nivel,
    comentarios: datos.comentarios ?? [],
  };
}

const TODOS_LOS_IDS = USUARIOS.map((usuario) => usuario.id);

export const PARTIDOS_INICIALES: Partido[] = [
  crearPartido({
    id: 'p1',
    deporte: 'tenis',
    barrio: 'belgrano',
    dias: 2,
    horario: '09:00',
    duracion: 60,
    nivel: 'PRINCIPIANTE',
    creador: 'u3',
    jugadores: ['u3'],
    estado: 'Necesitamos jugadores',
  }),
  crearPartido({
    id: 'p2',
    deporte: 'futbol',
    barrio: 'palermo',
    dias: 2,
    horario: '19:00',
    duracion: 90,
    nivel: 'INTERMEDIO',
    creador: 'u1',
    jugadores: [
      'u1', 'u2', 'u4', 'u5', 'u6', 'u7', 'u8', 'u9', 'u10',
      'u11', 'u12', 'u13', 'u14', 'u15', 'u19',
    ],
    estado: 'Necesitamos jugadores',
  }),
  crearPartido({
    id: 'p3',
    deporte: 'basquet',
    barrio: 'recoleta',
    dias: 1,
    horario: '20:30',
    duracion: 90,
    nivel: 'AVANZADO',
    creador: 'u2',
    jugadores: ['u2', 'u8', 'u9', 'u10', 'u11', 'u12', 'u13', 'u14', 'u16', 'u18'],
    estado: 'Armado',
  }),
  crearPartido({
    id: 'p4',
    deporte: 'voley',
    barrio: 'san telmo',
    dias: 0,
    horario: '18:00',
    duracion: 90,
    nivel: 'INTERMEDIO',
    creador: 'u4',
    jugadores: ['u4', 'u1', 'u2', 'u3', 'u5', 'u6', 'u7', 'u9', 'u13', 'u17', 'u21', 'u15'],
    estado: 'Confirmado',
  }),
  crearPartido({
    id: 'p5',
    deporte: 'futbol',
    barrio: 'caballito',
    dias: -3,
    horario: '21:00',
    duracion: 90,
    nivel: 'AVANZADO',
    creador: 'u5',
    jugadores: TODOS_LOS_IDS,
    estado: 'Finalizado',
    comentarios: [
      comentario('Carlos Sanchez', '¡Gran partido! Mucha intensidad y buen nivel.', -3, 22),
      comentario('Ana Lopez', 'La próxima jugamos la revancha 💪', -3, 23),
      comentario('Sistema', 'Partido finalizado. Los jugadores ya pueden dejar su comentario.', -3, 21),
    ],
  }),
  crearPartido({
    id: 'p6',
    deporte: 'tenis',
    barrio: 'palermo',
    dias: 3,
    horario: '17:00',
    duracion: 60,
    nivel: 'PRINCIPIANTE',
    creador: 'u6',
    jugadores: ['u6', 'u3'],
    estado: 'Confirmado',
  }),
  crearPartido({
    id: 'p7',
    deporte: 'basquet',
    barrio: 'palermo',
    dias: -1,
    horario: '19:00',
    duracion: 90,
    nivel: 'INTERMEDIO',
    creador: 'u2',
    jugadores: ['u2', 'u7'],
    estado: 'Cancelado',
  }),
  crearPartido({
    id: 'p8',
    deporte: 'voley',
    barrio: 'belgrano',
    dias: 0,
    horario: '20:00',
    duracion: 90,
    nivel: 'PRINCIPIANTE',
    creador: 'u13',
    jugadores: ['u13', 'u4', 'u9', 'u21', 'u1', 'u2', 'u3', 'u5', 'u6', 'u7', 'u8', 'u10'],
    estado: 'En juego',
  }),
];

export function obtenerUsuario(id: string): Usuario | undefined {
  return USUARIOS.find((usuario) => usuario.id === id);
}

export function obtenerDeporte(id: DeporteId): Deporte {
  return DEPORTES[id];
}

export function nombreCompleto(usuario: Usuario): string {
  return `${usuario.nombre} ${usuario.apellido}`;
}

export function iniciales(usuario: Usuario): string {
  return `${usuario.nombre.charAt(0)}${usuario.apellido.charAt(0)}`.toUpperCase();
}

export function etiquetaNivel(nivel: Nivel): string {
  return `${nivel.charAt(0)}${nivel.slice(1).toLowerCase()}`;
}

export function capitalizar(texto: string): string {
  return texto.charAt(0).toUpperCase() + texto.slice(1);
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

export function slugEstado(estado: Estado): string {
  return estado
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/\s+/g, '-');
}

export function lugarDisponible(partido: Partido): boolean {
  return partido.jugadores.length < DEPORTES[partido.deporte].jugadores;
}
