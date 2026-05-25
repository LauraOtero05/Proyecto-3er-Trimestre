# 74 Minutes - CRM

74 Minutes es un sistema de gestión de relaciones con clientes (CRM) desarrollado como proyecto intermodular para la tienda de discos de vinilo del mismo nombre. El proyecto integra tres módulos académicos independientes que conforman una solución completa: desde el modelado y almacenamiento de datos en MySQL, pasando por la lógica de negocio en una aplicación de escritorio Java, hasta la interfaz visual e interactiva del panel de administración en el navegador.

Cada módulo se desarrolla en su propia rama del repositorio y cuenta con documentación específica en su respectivo apartado del README.md.

## Tecnologías Utilizadas 

Resumen general: Java, MySQL, HTML/CSS/JS, Git

| Módulo | Tecnologías |
|-------|------------|
| **Base de Datos** | Oracle Database XE 21c, Oracle SQL Developer, MySQL Server 8.0, MySQL Workbench  |
| **Programación** |Java 21, JDBC, Maven, MySQL Server |
| **Lenguaje de Marcas** | HTML5, CSS3, JavaScript (ES6+), Firebase Hosting |
| **Entornos de Desarrollo** | Git, GitHub |

## Estructura del Repositorio y Ramas

El repositorio se organiza mediante un sistema de ramas por módulo y por integrante, lo que permite el desarrollo paralelo y autónomo de cada funcionalidad sin conflictos en el código:

```bash
    main
    │
    ├── backend
    │   ├── backend-alvaro
    │   ├── backend-laura
    │   └── backend-noemi
    │
    ├── database
    │   ├── database-alvaro
    │   ├── database-laura
    │   └── database-noemi
    │
    ├── frontend
    │   ├── frontend-alvaro
    │   ├── frontend-laura
    │   └── frontend-noemi
    │
    └── readme
```
### Flujo de trabajo con ramas:

- Cada integrante del equipo trabaja en su rama personal (ej. frontend-laura), desarrollando sus funcionalidades de forma aislada.
- Una vez validado el trabajo, se integra en la rama base del módulo correspondiente (ej. frontend) mediante merge.
- Finalmente, las ramas base de los tres módulos se consolidan en la rama main, que representa la versión estable y entregable del proyecto.

## Versiones y tags

El proyecto utiliza tags de Git para marcar los hitos principales del desarrollo y facilitar la trazabilidad de las entregas:

| Tag | Descripción |
|-------|------------|
| `db-v1.0` | Primera versión estable del proyecto de Base de Datos  |
| `db-v2.0` |Segunda versión estable del proyecto completo de Base de Datos |
| `db-v2.1` |Segunda versión corregida y estable del proyecto completo de Base de Datos |
| `backend-v1.0` | Primera versión estable del proyecto completo de JAVA |
| `backend-v2.0` | Segunda versión estable del proyecto completo de JAVA |
| `frontend-v1.0` | Primera versión estable del proyecto completo de Lenguaje de Marcas |

Para consultar todos los tags disponibles:

```bash
git tag -l
```

Para acceder al estado del proyecto en una versión concreta:

```bash
git checkout db-v1.0
```

---
# Base de Datos

### Descripción

Este módulo contiene todo el trabajo realizado para la asignatura de Base de Datos dentro del proyecto intermodular CRM de la tienda de música _74 Minutes_.

Se ha diseñado e implementado una base de datos relacional completa partiendo del modelo conceptual hasta el modelo físico, incluyendo la lógica de negocio en PL/SQL y la migración del esquema a MySQL para su uso en el módulo de Programación.

El trabajo cubre:

- Diseño del modelo Entidad–Relación (E-R)
- Modelo lógico relacional y normalización hasta 3FN
- Modelo físico implementado en Oracle Database y MySQL
- Scripts DDL y DML para Oracle y MySQL
- 120 consultas PL/SQL (24 por tabla): CRUD, cursores, procedimientos y funciones

## Diseño de la Base de Datos

### Modelo Entidad Relación

El modelo E-R define las siguientes entidades y relaciones.

**Entidades principales:**

- **Clientes** (id, nombre, apellido, dirección, código postal, ciudad, provincia, país, teléfono, email, password_hash)
- **Trabajadores** (DNI, nombre, apellido, rol, teléfono, email, password_hash)
- **Proveedores** (id_proveedor, nombre, dirección, código postal, ciudad, provincia, país, teléfono, e-mail)
- **Álbumes** (id, nombre, artista, id_proveedor, id_género musical, dirección, precio, stock)
- **Pedidos** (id_pedido, id_cliente, id_comercial, fecha, estado de la venta, total)
- **Detalle_Venta** (id_pedido, id_producto, cantidad)

**Relaciones:**

- **Realiza** — Clientes (1,1) → Pedidos (1,N): un cliente realiza uno o varios pedidos y cada pedido pertenece a un único cliente
- **Asigna** — Trabajadores (0,1) → Pedidos (1,N): un trabajador puede gestionar varios pedidos; un pedido puede no tener trabajador asignado
- **Contiene** — Pedidos (0,N) ↔ Álbumes (1,N): relación N:M resuelta con la tabla intermedia Detalle_Venta, que almacena la cantidad
- **Provee** — Proveedores (1,1) → Álbumes (1,N): un proveedor suministra varios álbumes; cada álbum tiene un único proveedor

**Atributos especiales:**

- Los atributos teléfono de Clientes y Trabajadores son multivaluados → tablas débiles independientes
- Los atributos teléfono y e-mail de Proveedores son multivaluados → tablas débiles independientes
- La información geográfica (dirección, código postal, ciudad, provincia, país) se normaliza en tablas `Codigos_Postales` y `Paises` compartidas por Clientes y Proveedores

### Modelo Lógico y Normalización

El modelo E-R se transformó a tablas relacionales aplicando:

- **1FN:** separación de atributos multivaluados (teléfonos y emails) en tablas independientes con clave primaria compuesta
- **2FN:** eliminación de dependencias parciales en la tabla Detalle_Venta
- **3FN:** separación de la información geográfica en `Codigos_Postales` y `Paises` para eliminar dependencias transitivas compartidas entre Clientes y Proveedores

### Modelo Físico

El modelo físico cuenta con **13 tablas**:

`paises` · `codigos_postales` · `clientes` · `telefonos_cliente` · `trabajadores` · `telefonos_trabajador` · `proveedores` · `telefonos_proveedor` · `emails_proveedor` · `generos_musicales` · `albumes` · `pedidos` · `detalles_pedido`

## Oracle SQL Developer

En Oracle se ha implementado el modelo físico completo con todas sus restricciones de integridad y la lógica de negocio en PL/SQL. Los scripts están divididos en:

- **`SCHEMA Oracle.sql`** — DDL + DML: crea el usuario, las tablas, las restricciones y carga los datos de prueba
- **`CONSULTAS/`** — 120 bloques PL/SQL organizados por tabla

### Requisitos

- Oracle Database XE 21c instalado y en ejecución
- Oracle SQL Developer instalado
- El servicio de Oracle debe estar activo antes de ejecutar cualquier script

> Si usas Windows, comprueba que el servicio `OracleServiceXEPDB1` está iniciado. Puedes verlo buscando **Servicios** en el menú inicio y buscando cualquier servicio que empiece por `Oracle`.


### Instrucciones de Configuración

#### 1. Comprobar que Oracle está en ejecución

Abre una terminal (CMD o PowerShell) y ejecuta:

```bash
lsnrctl status
```

Debes ver que el listener está activo y escuchando en el puerto **1521** (puerto por defecto). Si el tuyo es distinto, más adelante deberás modificarlo en el script de ejecución de la base de datos.


#### 2. Ejecutar el script `SCHEMA ORACLE.sql` en SQL Developer

1. Abre **SQL Developer**.
2. Ve a **Archivo > Abrir** y abre el archivo `SCHEMA Oracle.sql`.

> **Importante:** dentro del script hay una línea de conexión que apunta a la URL de la base de datos:
> ```sql
> CONNECT "74min"/passwd74M@LOCALHOST:1521/XEPDB1
> ```
> Si tu puerto es distinto al `1521` o tu nombre de servicio no es `XEPDB1`, edita esa línea antes de ejecutar. Para saber cuál es tu nombre de servicio, ejecuta esto con tu usuario admin:
> ```sql
> SELECT value FROM v$parameter WHERE name = 'service_names';
> ```

3. Pulsa **Ejecutar Script (F5)** para ejecutarlo completo de una vez.
4. Aparecerá una ventana para seleccionar la conexión; elige **SYSTEM**.

#### 3. Crear la conexión en SQL Developer con el usuario del proyecto

Una vez ejecutado el script, crea una nueva conexión en SQL Developer:

1. En el panel izquierdo, haz clic en el **icono verde `+`** (o ve a **Conexiones > Nueva Conexión...**)
2. Se abrirá la ventana *Nueva / Seleccionar Conexión a Base de Datos*
3. Rellena los campos con los siguientes datos:

| Campo | Valor |
|---|---|
| **Connection Name** | `74min` (o el nombre que quieras) |
| **Username** | **`"74min"`** *(con las comillas incluidas)* |
| **Password** | `passwd74M` |
| **Hostname** | `localhost` |
| **Port** | `1521` (o el tuyo si es distinto) |
| **Service name** | `XEPDB1` (o el tuyo si es distinto) |

4. Pulsa **Probar** para verificar que la conexión funciona antes de guardarla.
5. Si el estado de la conexión te aparece **Correcto**, pulsa en **Conectar**. En caso contrario comprueba que has introducido correctamente los datos anteriores.


#### 4. Verificar que los datos se han cargado

Conéctate con el usuario `"74min"` y ejecuta:

```sql
SELECT COUNT(*) FROM clientes;
SELECT COUNT(*) FROM albumes;
SELECT COUNT(*) FROM pedidos;
```

Cada consulta debe devolver **10 registros**. Si devuelve 0, el DML no se ejecutó correctamente; vuelve a abrir el script y ejecútalo de nuevo con F5.


### Consultas PL/SQL

#### Descripción

Se han desarrollado **120 bloques PL/SQL** en total, **24 por cada tabla**. Cada archivo cubre las siguientes secciones:

- **CRUD** (15 bloques por tabla): 5 inserciones, 5 modificaciones y 5 eliminaciones usando variables `%TYPE`, estructuras `IF`/`CASE`, bucles `FOR`/`WHILE` y manejo de excepciones
- **Cursores** (5 bloques por tabla): consultas con cursores implícitos, explícitos y parametrizados que muestran los resultados por `DBMS_OUTPUT`
- **Procedimientos** (2 por tabla): procedimientos almacenados con variables, estructuras de control y repetición, cursores, excepciones y funciones SQL
- **Funciones** (2 por tabla): funciones que devuelven valores calculados o clasificaciones basadas en los datos

#### Tablas cubiertas

| Archivo | Tabla | Ejemplos de lo que hace |
|---|---|---|
| `CONSULTAS PLSQL ALBUMES.sql` | albumes | Descuentos por formato, reposición de stock, valor de inventario por género |
| `CONSULTAS PLSQL CLIENTES.sql` | clientes | Inserción con validación de email, total de pedidos por cliente, nombre completo |
| `CONSULTAS PLSQL PEDIDOS.sql` | pedidos | Crear pedido validando stock, resumen mensual, estado de un pedido |
| `CONSULTAS PLSQL PROVEEDORES.sql` | proveedores | Inserción con VARRAY, gasto total por proveedor, formato con más stock |
| `CONSULTAS PLSQL TRABAJADORES.sql` | trabajadores | Alta con validación de DNI y rol, reasignación de pedidos, baja segura |

#### Cómo ejecutarlos en SQL Developer

1. Asegúrate de estar conectado con el usuario **`"74min"`** (no con `SYS` ni `SYSTEM`)
2. Ejecuta primero esta línea para que los resultados aparezcan en consola:

```sql
SET SERVEROUTPUT ON;
```

> Esta línea ya está incluida al inicio de cada archivo, pero si no ves ninguna salida en la pestaña *Script Output*, ejecútala manualmente antes de nada.

3. Abre el archivo que quieras desde **Archivo > Abrir**
4. Pulsa **Ejecutar Script (F5)** para ejecutar el archivo completo, o selecciona el bloque concreto que quieras probar y pulsa F5 sobre la selección

> Algunos bloques tienen el `COMMIT;` comentado (`-- COMMIT;`) para que puedas revisar los cambios antes de confirmarlos. Si quieres que los cambios sean permanentes, descoméntalo o ejecuta `COMMIT;` manualmente después.


### Estructura de archivos

```
database/
│
├── Base-de-Datos/
│   ├── CONSULTAS/
│   │    ├── CONSULTAS PLSQL ALBUMES.sql
│   │    ├── CONSULTAS PLSQL CLIENTES.sql
│   │    ├── CONSULTAS PLSQL PEDIDOS.sql
│   │    ├── CONSULTAS PLSQL PROVEEDORES.sql
│   │    └── CONSULTAS PLSQL TRABAJADORES.sql
│   ├── MODELO CREACION BDD.drawio
│   ├── MODELO FISICO.png
│   ├── SCHEMA MYSQL.sql
│   └── SCHEMA ORACLE.sql
│
└── README.md
```

## MySQL

### Descripción

El esquema Oracle se ha migrado íntegramente a MySQL para su uso en el módulo de Programación con Java y JDBC. La base de datos se llama `74_minutes` y contiene exactamente las mismas tablas y datos que el esquema Oracle, adaptados a la sintaxis de MySQL.

El script `SCHEMA MYSQL.sql` incluye tanto el DDL (creación de tablas) como el DML (inserción de datos).

### Requisitos

- MySQL Server 8.x instalado y en ejecución
- MySQL Workbench instalado


### Instrucciones de Configuración

#### 1. Verificar que MySQL está en ejecución

MySQL usa por defecto el puerto **3306**. Para verificar que el servicio está activo:

- **Windows:** abre el Administrador de servicios (busca "Servicios" en el menú inicio), localiza `MySQL80` o superior y comprueba que está en estado **En ejecución**. Si no lo está, haz clic derecho e inícialo.
- **Mac/Linux:** ejecuta en terminal `sudo systemctl status mysql`

> Para saber qué puerto tiene configurado tu instalación, abre MySQL Workbench, haz doble clic en tu conexión y mira el campo **Port**. También puedes ejecutar `SHOW VARIABLES LIKE 'port';` desde cualquier cliente conectado.


#### 2. Ejecutar el script DDL + DML en MySQL Workbench

1. Abre **MySQL Workbench** y conéctate con tu usuario (por defecto `root`)
2. Ve a **File > Open SQL Script** y abre el archivo `SCHEMA MYSQL.sql`
3. Pulsa el botón **Execute All** o usa el atajo `Ctrl+Shift+Enter` para ejecutar el script completo

#### 3. Verificar que la base de datos se ha creado

Una vez ejecutado el script, comprueba que todo está bien:

```sql
USE 74_minutes;
SHOW TABLES;
```

Debes ver las **13 tablas**. Después verifica los datos:

```sql
SELECT COUNT(*) FROM clientes;
SELECT COUNT(*) FROM albumes;
SELECT COUNT(*) FROM pedidos;
```

Cada una debe devolver **10 registros**.

---
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
---
# Lenguaje de Marcas

Este módulo del proyecto contiene la aplicación web del CRM de la tienda de vinilos 74 Minutes. La interfaz ha sido diseñada como un panel de administración interactivo orientado al empleado, permitiendo gestionar de forma dinámica y visual las operaciones comerciales del negocio directamente desde el navegador.

La aplicación funciona en su totalidad en el lado del cliente (client-side), sin conexión a un servidor backend. Toda la lógica de negocio, la manipulación de la interfaz y la simulación de persistencia de datos se realizan mediante JavaScript y SessionStorage.


## Tecnologías utilizadas 

- **HTML5:** Estructuración semántica de las vistas, formularios interactivos y tablas del panel de gestión.
- **CSS3:** Maquetación visual responsive, diseño unificado bajo la metodología BEM, transiciones y animaciones.
- **JavaScript (ES6+):** Motor lógico del CRM. Gestiona la persistencia temporal en SessionStorage, la manipulación dinámica del DOM, la arquitectura de clases orientada a objetos y el control reactivo del viewport mediante el objeto global Window.

## Requisitos previos

Este proyecto no requiere instalación de dependencias, servidores locales ni conexión a bases de datos. Para ejecutarlo solo necesitas:

- Navegador web moderno: Google Chrome, Mozilla Firefox, Microsoft Edge o Safari.
- Editor de código (opcional): Visual Studio Code o cualquier editor de texto plano para inspeccionar el código fuente.
- Acceso a Git para clonar el repositorio (o bien descargar el archivo .ZIP directamente desde GitHub).
- Sistema operativo compatible: Windows, macOS o Linux.

## Instrucciones para Ejecutar

## Opción 1: Ejecución Local

1. Clonar el repositorio:

```bash
git clone https://github.com/LauraOtero05/Proyecto-3er-Trimestre.git
```

2. Acceder a la carpeta del proyecto:

```bash
cd Proyecto-3er-Trimestre
```

3. Abrir la aplicación:

- Localiza el archivo index.html en la raíz del proyecto y ábrelo directamente con tu navegador (doble clic o arrastrar a la ventana del navegador).
- Opcional: Si utilizas Visual Studio Code con la extensión Live Server, haz clic derecho sobre index.html > "Open with Live Server".

4. Navegar por el CRM:

- En la pantalla de Login, introduce cualquier correo y contraseña para acceder al Dashboard.
- Utiliza el menú lateral izquierdo para navegar entre las secciones: Dashboard, Clientes, Trabajadores, Productos, Proveedores y Perfil.

### Opción 2: Acceso Directo en la Nube (Firebase Hosting) 

Si se desea evaluar la aplicación de manera inmediata sin descargar archivos ni realizar configuraciones:

- URL del CRM desplegado: https://minutes-74.web.app
- Simplemente accede al enlace desde cualquier dispositivo con navegador. La aplicación se ejecutará de forma instantánea.

> **Nota:** La versión desplegada en Firebase Hosting es la versión de producción del proyecto. El código fuente completo y editable se encuentra en este repositorio de GitHub.

## Notas importantes

- Persistencia temporal (SessionStorage): La aplicación no persiste datos en disco ni en base de datos externa. Toda la información se almacena en el SessionStorage del navegador. Esto implica que los datos modificados, creados o eliminados por el usuario se reiniciarán a su estado inicial al cerrar la pestaña o el navegador.
- Datos de ejemplo precargados: La aplicación carga automáticamente un conjunto de registros de prueba la primera vez que se accede. Si ya existen datos modificados en la sesión activa, el sistema los respeta y no los sobrescribe.
- Sin dependencias externas: El proyecto no utiliza frameworks de JavaScript, ni preprocesadores CSS, ni gestores de paquetes. 

## Posibles errores comunes

**La página aparece sin estilos o con el layout roto:**

 Asegúrate de que estás abriendo el archivo index.html de la raíz del proyecto, es decir, la carpeta `Proyecto-3er-Trimestre`. Las rutas de los recursos CSS y JS son relativas a la raíz.

 **El menú lateral no navega correctamente entre secciones:**

Si abres el archivo directamente con el protocolo file://, algunos navegadores pueden bloquear la navegación entre páginas por políticas de seguridad. En ese caso, utiliza la extensión Live Server de Visual Studio Code o accede a la versión desplegada en Firebase.

 **Los datos no se mantienen al cambiar de página:**

 Verifica que el navegador no está en modo de navegación privada/incógnito, ya que algunos navegadores restringen el acceso a SessionStorage en ese modo.



## Estructura de Archivos del Proyecto

```bash
    frontend/
    │
    ├── Lenguaje-de-Marcas/
    │   ├── pages/               # Vistas HTML
    │   ├── resources/           # Recursos estáticos
    │   ├── script/              # Lógica de programación JavaScript
    │   ├── styles/              # Hojas de estilo CSS
    │   └── index.html           # Archivo raiz del proyecto
    │   
    └── README.md
```

## Autores

- Laura Otero Martín
- Noemí Cano Conesa
- Álvaro Baz Rodríguez
