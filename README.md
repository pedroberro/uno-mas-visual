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

## Configuración del correo

La aplicación necesita las variables de entorno `UNO_MAS_SMTP_EMAIL` y
`UNO_MAS_SMTP_PASSWORD` para enviar correos por SMTP. Por ejemplo, en PowerShell:

```powershell
$env:UNO_MAS_SMTP_EMAIL = "tu-correo@gmail.com"
$env:UNO_MAS_SMTP_PASSWORD = "tu-contraseña-de-aplicación"
mvn package
```

No guardes contraseñas ni claves en archivos registrados por Git.
