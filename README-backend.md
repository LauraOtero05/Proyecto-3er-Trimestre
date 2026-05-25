# Programación

Este módulo del proyecto contiene la aplicación de escritorio del **CRM en Java** diseñada para gestionar la tienda **74 Minutes**. La aplicación se conecta mediante JDBC a una base de datos MySQL local para persistir toda la información en tiempo real.

## Tecnologías utilizadas 

* **Java 21**: Versión base del lenguaje de programación utilizado para el desarrollo del núcleo del CRM.
* **MySQL Server**: Motor de base de datos relacional para el almacenamiento de datos.
* **JDBC (Java DataBase Connectivity)**: API estándar para conectar la aplicación Java con la base de datos.
* **Maven**: Gestor de dependencias utilizado para automatizar la inclusión del driver de base de datos sin necesidad de descargas manuales.
*   *Dependencia clave (`pom.xml`):* `mysql-connector-j` (versión 8.4.0) de MySQL.

## Arquitectura del Proyecto 

El sistema está estructurado siguiendo una **arquitectura limpia y desacoplada en capas**, facilitando el mantenimiento y la escalabilidad del código:

```bash
    src/main/java/com/m74/proyecto_crm/
    │
    ├── Main.java                 # Punto de entrada de la aplicación
    │
    ├── controllers/
    ├── services/
    ├── repositories/
    ├── entities/
    ├── enums/
    ├── graphicInterfaces/
    └── util/
```
### Explicación de las Capas:
* **controllers**: Recibe las acciones del usuario, invoca a los servicios correspondientes y devuelve los resultados a la interfaz.
* **services**: Ejecuta las reglas de negocio (por ejemplo, verificar si un cliente ya existe antes de añadirlo).
* **repositories**: Contiene las sentencias SQL y es la única capa que interactúa directamente con la base de datos a través de JDBC.
* **entities**: Modelado de los objetos reales del negocio.
* **enums**: Enumeradores que tipifican datos del dominio
* **graphicInterfaces**: Renderiza el menú interactivo para el usuario.
* **util**: Alberga componentes transversales como el gestor de la conexión (DataBaseConnection.java).

## Requisitos previos
Para poder ejecutar este proyecto sin inconvenientes, asegúrate de tener instalado y configurado:
* **JDK 21** (Java Development Kit) instalado en el sistema.
* **MySQL Server** instalado y activo en tu equipo local.
* **Los scripts de Base de Datos ejecutados previamente** (asegúrate de haber ejecutado los scripts DDL y DML de creación de tablas e inserción de datos iniciales **de MySQL**).
* Un **IDE de desarrollo Java** (recomendamos **IntelliJ IDEA**, compatible con la importación automática de proyectos Maven).

## Instrucciones para Configuración
Sigue estos sencillos pasos para dejar listo el entorno de ejecución:
1. Clonar el repositorio

Descarga el proyecto a tu máquina local mediante Git o descargando el archivo .ZIP
   ```bash
       git clone https://github.com/LauraOtero05/Proyecto-3er-Trimestre.git
   ```

2. Abrir el proyecto como proyecto Maven
    * Abre tu IDE (ej. **IntelliJ IDEA**).
    * Selecciona la opción **Open (Abrir)** y busca la carpeta raíz `Proyecto_CRM`.
    * Asegúrate de que el IDE detecte el archivo pom.xml y haya **importado el proyecto como un Proyecto Maven.**
> **Nota importante:** Al importarlo como proyecto Maven, el IDE descargará y configurará automáticamente el Driver JDBC (mysql-connector-j.jar) por ti de forma automática.

3. Verificar las dependencias de Maven
Si por algún motivo el IDE no resuelve automáticamente las librerías:
    * En IntelliJ, haz clic derecho sobre el proyecto en la barra lateral.
    * Selecciona Maven > Reload Project (o haz clic en el icono del "refresco" en la pestaña lateral de Maven).

4. Personalizar el Nombre de la Base de Datos (Opcional)
Por defecto, el código busca conectarse a una base de datos llamada **74_minutes**. Si en tu sistema local has creado la base de datos con otro nombre, puedes modificarlo:
    1. Dirígete a la clase DataBaseConnection.java ubicada en src/main/java/com/m74/proyecto_crm/util/.
    2. En la **línea 14**, modifica la cadena de conexión sustituyendo 74_minutes por el nombre de tu base de datos:
        ```bash
       String url = "jdbc:mysql://localhost:" + port + "/tu_base_de_datos";
        ```

## Instrucciones para Ejecutar
Una vez que MySQL está en ejecución y el proyecto importado:
1. Abre y ejecuta la clase **Main.java** (src/main/java/com/m74/proyecto_crm/Main.java).
2. Al iniciar, la terminal de la aplicación te solicitará dinámicamente tus credenciales locales de MySQL:
    * **Puerto de MySQL**: Introduce el puerto configurado en tu sistema (por defecto suele ser el 3306).
    * **Usuario de MySQL**: Tu usuario del sistema (ej. root).
    * **Contraseña de MySQL**: Tu contraseña de acceso a MySQL.
3. Si la conexión se realiza con éxito, se desplegará el **menú principal interactivo** en la consola para gestionar el CRM.

## Posibles errores comunes
* **Error de conexión** o puerto incorrecto.
    * **Solución**: Revisa que el servidor local de MySQL esté en ejecución e introduce correctamente el puerto (habitualmente 3306).
* **Acceso denegado** (Credenciales incorrectas)
    * **Solución**: Verifica que estás introduciendo correctamente el nombre de usuario y la contraseña de tu instalación local de MySQL.
* **Driver JDBC no encontrado** (ClassNotFoundException)
    * **Solución**: Asegúrate de haber importado el proyecto como Maven y tener conexión a internet para que el IDE descargue las dependencias especificadas en el pom.xml. Realiza un _Maven Reload_.
* **Base de datos no encontrada o tablas inexistentes**
    * **Solución**: Asegúrate de haber ejecutado previamente los scripts DDL y DML para crear y rellenar la base de datos _74_minutes_.
