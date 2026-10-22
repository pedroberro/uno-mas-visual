# Uno Mas

## Compilar y ejecutar

Se necesita un JDK 21 o posterior y Maven. Ejecuta desde la carpeta que contiene
`pom.xml`:

```powershell
mvn clean compile
java -cp target/classes Main
```

En VS Code, importa el proyecto Maven y usa **Run Java** sobre `Main.java`.
Si abres la carpeta superior del repositorio, también puedes seleccionar
**Ejecutar UnoMas** en el panel de ejecución y pulsar F5.
Si el editor conserva errores de clases que no se resuelven, ejecuta
**Java: Clean Java Language Server Workspace** desde la paleta de comandos y
permite que vuelva a importar el proyecto.

## Interfaz web (React)

En `web/` hay una demo visual en React + TypeScript (Vite) que reproduce la
experiencia de la aplicación: inicio de sesión con cuentas de ejemplo, partidos
filtrados por barrio y deporte, creación de partidos, cambio de estados
(armado, confirmado, en juego, finalizado) y comentarios.

```powershell
cd web
npm install
npm run dev
```

La página queda disponible en `http://localhost:5173`. Para generar la versión
de producción ejecuta `npm run build` (el resultado queda en `web/dist`).

Las cuentas demo son las mismas del proyecto; por ejemplo
`juanperez@gmail.com` / `pass123` o `analopez@gmail.com` / `abc456`. El resto de
las cuentas de ejemplo usa la contraseña `demo1234`. Los datos son mock, se
guardan en `localStorage` y el botón **Reiniciar demo** vuelve al estado inicial.

