# Uno Más

Aplicación web para organizar partidos deportivos amateur entre vecinos. Cada
usuario se registra con su zona (barrio y ciudad), su nivel de juego y su deporte
favorito; a partir de ahí puede crear partidos de fútbol, básquet, tenis o vóley,
buscar partidos con cupos, unirse a los de otros usuarios y seguir el ciclo de
vida completo de cada encuentro desde la página.

Un partido recorre varios estados —necesitamos jugadores, armado, confirmado, en
juego, finalizado o cancelado— y el sistema valida qué acciones se permiten en
cada uno. Los barrios se modelan como un grafo con distancias entre ellos, de
modo que las invitaciones y el emparejamiento de jugadores pueden tener en cuenta
la cercanía, el nivel de juego o el historial de partidos.

> **Proyecto con fines educativos.** Fue desarrollado especialmente para practicar
> **patrones de diseño**. Cada problema del dominio se resolvió aplicando un
> patrón conocido (Strategy para el emparejamiento, State para el ciclo de vida
> del partido, Observer para avisar los cambios, etc.), con el objetivo de
> comparar cómo queda el diseño frente a una solución escrita "a mano".

## Funcionalidades

- Registro e inicio de sesión con contraseña hasheada (SHA-256).
- Creación de partidos indicando deporte, zona, fecha, horario, duración, nivel y
  estrategia de emparejamiento.
- Búsqueda de partidos con cupos, filtros por deporte, barrio y texto libre.
- Invitaciones según la estrategia elegida por el creador.
- Unión a partidos existentes, con actualización automática del estado.
- Confirmación de jugadores, comienzo, finalización y cancelación del partido.
- Comentarios de los participantes.
- Notificaciones al usuario cada vez que cambia el estado de un partido.
- Reinicio de los datos de ejemplo desde la propia interfaz.

## Arquitectura

El proyecto tiene dos aplicaciones que se comunican por HTTP:

```text
web/  (React + TypeScript + Vite)  ──HTTP/JSON──▶  API REST (Spring Boot)  ──▶  dominio Java
```

- **Backend** (`src/main/java`): la lógica del juego sigue escrita en Java con
  los patrones de diseño. Ya no hay entrada/salida por consola: el dominio
  devuelve `ResultadoAccion` y los controladores REST traducen todo a JSON.
- **Frontend** (`web/`): la interfaz completa —login, registro, listado de
  partidos, detalle, creación y comentarios— consume la API mediante `fetch`.

### Endpoints principales

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/api/catalogos` | Deportes, barrios y niveles disponibles |
| `POST` | `/api/auth/login` | Inicio de sesión |
| `POST` | `/api/usuarios` | Registro de usuario |
| `GET` | `/api/usuarios/{id}` | Datos del usuario y sus notificaciones |
| `GET` | `/api/partidos?usuarioId=...` | Listado de partidos con marca de invitación |
| `POST` | `/api/partidos` | Crear partido |
| `POST` | `/api/partidos/{id}/unirse` | Unirse a un partido |
| `POST` | `/api/partidos/{id}/confirmar` | Confirmar jugadores (solo creador) |
| `POST` | `/api/partidos/{id}/comenzar` | Comenzar partido (solo creador) |
| `POST` | `/api/partidos/{id}/finalizar` | Finalizar partido (solo creador) |
| `POST` | `/api/partidos/{id}/cancelar` | Cancelar partido (solo creador) |
| `POST` | `/api/partidos/{id}/comentarios` | Comentar (solo participantes) |
| `POST` | `/api/demo/reiniciar` | Restaurar los datos de ejemplo |

Los errores de negocio se devuelven con su código HTTP correspondiente
(`400` regla de negocio, `401` credenciales, `403` permisos, `404` inexistente) y
un cuerpo `{ "mensaje": "..." }`.

## Patrones de diseño utilizados

| Patrón | Dónde se aplica | Qué resuelve |
| --- | --- | --- |
| **Singleton** | `controller/PartidoController`, `controller/UsuarioController` | Una única instancia y un único estado compartido |
| **Builder** | `model/Barrio` | Construir objetos con muchos datos de forma legible |
| **Factory** | `model/DeporteFactory` | Crear el deporte correcto a partir de un identificador |
| **Strategy** | `IEstrategiaEmparejamiento`, `IEstrategiaNotificacion` | Intercambiar algoritmos sin tocar el contexto |
| **State** | `IEstadoPartido` y las clases `EP*` | Modelar el ciclo de vida del partido |
| **Observer** | `IObservable` / `IObserver`, `Partido` | Avisar cambios a los interesados sin acoplarlos |
| **Adapter** | `IAdapterNotificacionFireBase`, `ANAdapterFireBase` | Aislar una API externa del resto del modelo |
| **MVC / capas** | `model`, `controller`, `web` + `web/src` | Separar dominio, servicios, API y presentación |

### Singleton

**Dónde:** [PartidoController.java](src/main/java/unomas/controller/PartidoController.java)
y [UsuarioController.java](src/main/java/unomas/controller/UsuarioController.java).

Ambas clases tienen el constructor privado y exponen una instancia única mediante
`getInstance()`. El controlador de partidos concentra la lista de partidos en
memoria y el de usuarios la lista de cuentas, así todos los requests trabajan
sobre el mismo estado.

### Builder

**Dónde:** [Barrio.java](src/main/java/unomas/model/Barrio.java), con la anotación
`@Builder` de Lombok. Se usa en [DatosIniciales.java](src/main/java/unomas/config/DatosIniciales.java)
y en `BarrioController.agregarBarrio`.

Permite construir un barrio de forma explícita y legible:
`Barrio.builder().ciudad("caba").barrio("palermo").build()`, evitando
constructores con parámetros ambiguos del mismo tipo.

### Factory

**Dónde:** [DeporteFactory.java](src/main/java/unomas/model/DeporteFactory.java).

Traduce el identificador que llega desde la API (`futbol`, `basquet`, `tenis`,
`voley`) a la clase concreta (`DFutbol`, `DBasquet`, `DTenis`, `DVoley`) y expone
el catálogo para la interfaz. Antes esa selección estaba repetida en un `switch`
dentro del menú de consola; ahora hay un único punto de creación.

### Strategy

**Dónde (emparejamiento):** [IEstrategiaEmparejamiento.java](src/main/java/unomas/model/IEstrategiaEmparejamiento.java)
con las implementaciones [EmparejamientoPorCercania.java](src/main/java/unomas/model/EmparejamientoPorCercania.java),
[EmparejamientoPorNivel.java](src/main/java/unomas/model/EmparejamientoPorNivel.java) y
[EmparejamientoPorHistorial.java](src/main/java/unomas/model/EmparejamientoPorHistorial.java).

El contexto es [Partido.java](src/main/java/unomas/model/Partido.java), que expone
`setMetodoDeEmparejamiento(...)`, `emparejarCandidatos(...)` y `esInvitado(...)`.
La estrategia se elige al crear el partido desde la página
(`PartidoRestController.crear` → `crearEstrategia`) y se usa para calcular qué
partidos aparecen como invitación para cada usuario.

**Dónde (notificaciones):** [IEstrategiaNotificacion.java](src/main/java/unomas/model/IEstrategiaNotificacion.java)
con la implementación [ENNotificadorFireBase.java](src/main/java/unomas/model/ENNotificadorFireBase.java)
y el contexto [Notificador.java](src/main/java/unomas/model/Notificador.java), que
cambia de estrategia en tiempo de ejecución con `cambiarEstrategia(...)`.

Agregar una nueva forma de emparejar o de notificar consiste en crear una clase
nueva que implemente la interfaz, sin modificar `Partido` ni el notificador.

### State

**Dónde:** [IEstadoPartido.java](src/main/java/unomas/model/IEstadoPartido.java) y sus
implementaciones [EPNecesitamosJugadores.java](src/main/java/unomas/model/EPNecesitamosJugadores.java),
[EPArmado.java](src/main/java/unomas/model/EPArmado.java),
[EPConfirmado.java](src/main/java/unomas/model/EPConfirmado.java),
[EPEnJuego.java](src/main/java/unomas/model/EPEnJuego.java),
[EPFinalizado.java](src/main/java/unomas/model/EPFinalizado.java) y
[EPCancelado.java](src/main/java/unomas/model/EPCancelado.java).

`Partido` delega cada acción (`confirmarJugadores`, `comenzar`, `finalizar`,
`cancelar`) en el estado actual, y cada estado decide si la transición es válida y
cuál es el estado siguiente: por ejemplo `EPArmado.confirmarJugadores` pasa a
`EPConfirmado`, `EPEnJuego.finalizar` pasa a `EPFinalizado` y `EPFinalizado`
rechaza cualquier cambio. Cuando se agrega el último jugador,
`EPNecesitamosJugadores` cambia automáticamente el partido a `EPArmado`.

Cada acción devuelve un `ResultadoAccion` con el mensaje para el usuario, así la
API puede responder qué pasó sin que el dominio conozca la interfaz.

### Observer

**Dónde:** [IObservable.java](src/main/java/unomas/model/IObservable.java) e
[IObserver.java](src/main/java/unomas/model/IObserver.java). El sujeto observable
es [Partido.java](src/main/java/unomas/model/Partido.java) y los observadores son
[Usuario.java](src/main/java/unomas/model/Usuario.java) y
[Equipo.java](src/main/java/unomas/model/Equipo.java).

`PartidoController.unirseAPartido` registra al usuario con
`partido.agregarObserver(usuario)` y `Partido.cambiarEstadoPartido` dispara
`notificar()`, que recorre los observadores y llama a `serNotificadoPor(...)`.
Cada usuario acumula esas notificaciones y la API las expone en
`/api/usuarios/{id}` para mostrarlas en la página.

### Adapter

**Dónde:** [IAdapterNotificacionFireBase.java](src/main/java/unomas/model/IAdapterNotificacionFireBase.java)
y [ANAdapterFireBase.java](src/main/java/unomas/model/ANAdapterFireBase.java), utilizados
por [ENNotificadorFireBase.java](src/main/java/unomas/model/ENNotificadorFireBase.java).

El adaptador traduce el envío concreto (hoy simulado) al contrato que espera el
notificador, de modo que cambiar el proveedor externo no afecta al resto del
modelo.

> El adaptador de correo (`ANAdapterCorreoJavaMail`) y la dependencia
> `jakarta.mail` fueron retirados por el momento, así que el envío de correo está
> deshabilitado. La infraestructura Strategy + Adapter de notificaciones sigue en
> el código como referencia.

### MVC / separación en capas

- `model`: dominio (usuarios, partidos, estados, estrategias, deportes y los TDA
  de barrios `IMGrafoTDA`/`MGrafo` e `IMConjuntoTDA`/`MConjunto`).
- `controller`: servicios de aplicación y estado en memoria (Singleton).
- `web`: controladores REST, DTOs y manejo de errores HTTP (el "controller" del
  MVC web).
- `config`: configuración de CORS y carga de datos de ejemplo.
- `web/src` (React): vistas y componentes que consumen la API.

## Estructura del proyecto

```text
src/main/java/unomas/
  Main.java                 Arranque de Spring Boot
  model/                    Dominio y patrones (estados, estrategias, observadores, TDA)
  controller/               Servicios con estado en memoria y reglas de negocio
  web/                      API REST: controladores, DTOs y manejo de errores
  config/                   CORS y datos iniciales de la demo
src/main/resources/
  application.properties    Puerto y nombre de la aplicación
web/                        Interfaz React + TypeScript (Vite)
```

## Cómo ejecutar

Requisitos: JDK 21 o posterior, Maven y Node.js 20 o posterior.

**1. Backend** (desde la raíz del repositorio):

```powershell
mvn spring-boot:run
```

Queda disponible en `http://localhost:8080`.

**2. Frontend** (en otra terminal):

```powershell
cd web
npm install
npm run dev
```

Abrí `http://localhost:5173`. Vite redirige las llamadas `/api` al backend, así
que no hace falta configurar nada más. Para generar la versión de producción:
`npm run build` (el resultado queda en `web/dist`).

### Tema claro y oscuro

La interfaz incluye dos temas. El botón de sol/luna de la barra superior (o de la
esquina de la pantalla de acceso) alterna entre **oscuro** —el tema por
defecto— y **claro**; la preferencia se guarda en `localStorage` y se aplica
antes del primer render para evitar parpadeos.

Los colores viven como tokens CSS en [web/src/index.css](web/src/index.css):
`:root` define el tema oscuro y el bloque `[data-tema='claro']` sobrescribe los
mismos tokens con la paleta clara, así que cualquier componente nuevo que use
variables (`--fondo`, `--superficie`, `--texto`, `--borde`, etc.) funciona en
ambos temas automáticamente. La lógica del tema está en
[web/src/tema.ts](web/src/tema.ts) y el control en
[web/src/components/ThemeToggle.tsx](web/src/components/ThemeToggle.tsx).

## Cuentas de ejemplo

| Correo | Contraseña |
| --- | --- |
| `juanperez@gmail.com` | `pass123` |
| `analopez@gmail.com` | `abc456` |
| `luisgomez@gmail.com` | `qwerty` |
| `martafernandez@gmail.com` | `123456` |
| `carlossanchez@gmail.com` | `admin` |
| `matirodriguez@gmail.com` | `matirodriguez` |

También hay usuarios adicionales (`sofaramirez@gmail.com`,
`tomasduarte@gmail.com`, etc.) con la contraseña `demo1234`, y se puede crear una
cuenta nueva desde la pestaña **Crear cuenta**.

Los datos de la demo se cargan al arrancar el backend; el botón **Reiniciar demo**
de la barra superior vuelve a dejarlos como al inicio.
