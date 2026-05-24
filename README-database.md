# Base de Datos

### Descripción
Este módulo contiene todo el trabajo realizado para la asignatura de Base de Datos dentro del proyecto intermodular CRM de la tienda de música en línea 74 Minutes.
Se ha diseñado e implementado una base de datos relacional completa partiendo del modelo conceptual hasta el modelo físico, incluyendo la lógica de negocio en PL/SQL y la migración del esquema a MySQL para su uso en el módulo de Programación.
El trabajo cubre:

Diseño del modelo Entidad–Relación (E-R)
Modelo lógico relacional y normalización hasta 3FN
Modelo físico implementado en Oracle Database
Scripts DDL y DML para Oracle y MySQL
120 consultas PL/SQL (24 por tabla): CRUD, cursores, procedimientos y funciones

## Diseño de la Base de Datos


### Modelo Entidad Relación

El modelo E-R define las siguientes entidades y relaciones:
Entidades principales:

Clientes (id, nombre, apellido, dirección, código postal, ciudad, provincia, país, teléfono, email, password_hash)
Trabajadores (DNI, nombre, apellido, rol, teléfono, email, password_hash)
Proveedores (id_proveedor, nombre, dirección, código postal, ciudad, provincia, país, teléfono, e-mail)
Álbumes (id, nombre, artista, id_proveedor, id_género musical, dirección, precio, stock)
Pedidos (id_pedido, id_cliente, id_comercial, fecha, estado de la venta, total)
Detalle_Venta (id_pedido, id_producto, cantidad)

Relaciones:

Realiza — Clientes (1,1) → Pedidos (1,N): un cliente realiza uno o varios pedidos y cada pedido pertenece a un único cliente
Asigna — Trabajadores (0,1) → Pedidos (1,N): un trabajador puede gestionar varios pedidos; un pedido puede no tener trabajador asignado
Contiene — Pedidos (0,N) ↔ Álbumes (1,N): relación N:M resuelta con la tabla intermedia Detalle_Venta, que almacena la cantidad
Provee — Proveedores (1,1) → Álbumes (1,N): un proveedor suministra varios álbumes; cada álbum tiene un único proveedor
Los atributos teléfono de Clientes y Trabajadores son multivalorados → tablas débiles independientes
Los atributos teléfono y e-mail de Proveedores son multivalorados → tablas débiles independientes
La información geográfica (dirección, código postal, ciudad, provincia, país) se normaliza en tablas Codigos_Postales y Paises compartidas por Clientes y Proveedores

### Modelo Lógico y Normalización

Modelo Lógico y Normalización
El modelo E-R se transformó a tablas relacionales aplicando:

1FN: separación de atributos multivalorados (teléfonos y emails) en tablas independientes con clave primaria compuesta
2FN: eliminación de dependencias parciales en la tabla Detalle_Venta
3FN: separación de la información geográfica en Codigos_Postales y Paises para eliminar dependencias transitivas compartidas entre Clientes y Proveedores

### Modelo Físico

El modelo físico cuenta con 13 tablas:
paises · codigos_postales · clientes · telefonos_cliente · trabajadores · telefonos_trabajador · proveedores · telefonos_proveedor · emails_proveedor · generos_musicales · albumes · pedidos · detalles_pedido

## Oracle SQL Developer

En Oracle se ha implementado el modelo físico completo con todas sus restricciones de integridad y la lógica de negocio en PL/SQL. Los scripts están divididos en:

SCHEMA Oracle.sql — DDL + DML: crea el usuario, las tablas, las restricciones y carga los datos de prueba
CONSULTAS/ — 120 bloques PL/SQL organizados por tabla

### Requisitos

Oracle Database XE 21c instalado y en ejecución
Oracle SQL Developer instalado
El servicio de Oracle debe estar activo antes de ejecutar cualquier script


Si usas Windows, comprueba que el servicio OracleServiceXEPDB1 está iniciado. Puedes verlo buscando Servicios en el menú inicio y buscando cualquier servicio que empiece por Oracle.

### Instrucciones de Configuración

Comprobar que Oracle está en ejecución
Abre una terminal (CMD o PowerShell) y ejecuta:
bashlsnrctl status
Debes ver que el listener está activo y escuchando en el puerto 1521 (puerto por defecto). Si el tuyo es distinto, anótalo porque lo necesitarás después.

Si el comando no se reconoce, busca la ruta de instalación de Oracle (normalmente C:\app\<tuusuario>\product\21c\dbhomeXE\bin) y ejecútalo desde ahí, o añade esa ruta al PATH del sistema.

2. Ejecutar el script DDL + DML en SQL Developer

Abre SQL Developer y conéctate con el usuario SYS o SYSTEM en modo SYSDBA (el usuario administrador de tu instalación de Oracle)
Ve a File > Open y abre el archivo SCHEMA Oracle.sql
Pulsa Run Script (F5) para ejecutarlo completo de una vez


Importante: dentro del script hay una línea de conexión que apunta a la URL de la base de datos:
sqlCONNECT "74min"/passwd74M@LOCALHOST:1521/XEPDB1
Si tu puerto es distinto al 1521 o tu nombre de servicio no es XEPDB1, edita esa línea antes de ejecutar. Para saber cuál es tu nombre de servicio, ejecuta esto con tu usuario admin:
sqlSELECT value FROM v$parameter WHERE name = 'service_names';

3. Crear la conexión en SQL Developer con el usuario del proyecto
Una vez ejecutado el script, crea una nueva conexión en SQL Developer con estos datos:
CampoValorConnection Name74min (o el nombre que quieras)Username74minPasswordpasswd74MHostnamelocalhostPort1521 (o el tuyo si es distinto)Service nameXEPDB1 (o el tuyo si es distinto)
Pulsa Test para verificar que la conexión funciona antes de guardarla.
4. Verificar que los datos se han cargado
Conéctate con el usuario 74min y ejecuta:
sqlSELECT COUNT(*) FROM clientes;
SELECT COUNT(*) FROM albumes;
SELECT COUNT(*) FROM pedidos;
Cada consulta debe devolver 10 registros. Si devuelve 0, el DML no se ejecutó correctamente; vuelve a abrir el script y ejecútalo de nuevo con F5.


### Consultas PL/SQL

### Descripción
Se han desarrollado 120 bloques PL/SQL en total, 24 por cada tabla. Cada archivo cubre las siguientes secciones:

CRUD (15 bloques por tabla): 5 inserciones, 5 modificaciones y 5 eliminaciones usando variables %TYPE, estructuras IF/CASE, bucles FOR/WHILE y manejo de excepciones
Cursores (5 bloques por tabla): consultas con cursores implícitos, explícitos y parametrizados que muestran los resultados por DBMS_OUTPUT
Procedimientos (2 por tabla): procedimientos almacenados con variables, estructuras de control y repetición, cursores, excepciones y funciones SQL
Funciones (2 por tabla): funciones que devuelven valores calculados o clasificaciones basadas en los datos

### Tablas cubiertas
ArchivoTablaEjemplos de lo que haceCONSULTAS PLSQL ALBUMES.sqlalbumesDescuentos por formato, reposición de stock, valor de inventario por géneroCONSULTAS PLSQL CLIENTES.sqlclientesInserción con validación de email, total de pedidos por cliente, nombre completoCONSULTAS PLSQL PEDIDOS.sqlpedidosCrear pedido validando stock, resumen mensual, estado de un pedidoCONSULTAS PLSQL PROVEEDORES.sqlproveedoresInserción con VARRAY, gasto total por proveedor, formato con más stockCONSULTAS PLSQL TRABAJADORES.sqltrabajadoresAlta con validación de DNI y rol, reasignación de pedidos, baja segura

### Cómo ejecutarlos en SQL Developer

Asegúrate de estar conectado con el usuario 74min (no con SYS ni SYSTEM)
Ejecuta primero esta línea para que los resultados aparezcan en consola:

sqlSET SERVEROUTPUT ON;
Esta línea ya está incluida al inicio de cada archivo, pero si no ves ninguna salida en la pestaña Script Output, ejecútala manualmente antes de nada.

Abre el archivo que quieras desde File > Open
Pulsa Run Script (F5) para ejecutar el archivo completo, o selecciona el bloque concreto que quieras probar y pulsa F5 sobre la selección


 Algunos bloques tienen el COMMIT; comentado (-- COMMIT;) para que puedas revisar los cambios antes de confirmarlos. Si quieres que los cambios sean permanentes, descoméntalo o ejecuta COMMIT; manualmente después.
```bash
    database/
    │
    ├── Base-de-Datos/
    │   └── CONSULTAS/
    │       ├── CONSULTAS PLSQL ALBUMES.sql
    │       ├── CONSULTAS PLSQL CLIENTES.sql
    │       ├── CONSULTAS PLSQL PEDIDOS.sql
    │       ├── CONSULTAS PLSQL PROVEEDORES.sql
    │       └── CONSULTAS PLSQL TRABAJADORES.sql
    │
    └── README.md
```


## MySQL

###Descripción
El esquema Oracle se ha migrado íntegramente a MySQL para su uso en el módulo de Programación con Java y JDBC. La base de datos se llama 74_minutes y contiene exactamente las mismas tablas y datos que el esquema Oracle, adaptados a la sintaxis de MySQL.
El script SCHEMA MYSQL.sql incluye tanto el DDL (creación de tablas) como el DML (inserción de datos).


### Requisitos

MySQL Server 8.x instalado y en ejecución
MySQL Workbench instalado

### Instrucciones de Configuración

MySQL usa por defecto el puerto 3306. Para verificar que el servicio está activo:

En Windows: abre el Administrador de servicios (busca "Servicios" en el menú inicio), localiza MySQL80 y comprueba que está en estado En ejecución. Si no lo está, haz clic derecho e inícialo.
En Mac/Linux: ejecuta en terminal sudo systemctl status mysql


Para saber qué puerto tiene configurado tu instalación, abre MySQL Workbench, haz doble clic en tu conexión y mira el campo Port. También puedes ejecutar SHOW VARIABLES LIKE 'port'; desde cualquier cliente conectado.

2. Ejecutar el script DDL + DML en MySQL Workbench

Abre MySQL Workbench y conéctate con tu usuario (por defecto root)
Ve a File > Open SQL Script y abre el archivo SCHEMA MYSQL.sql
Pulsa el botón Execute All (el rayo ⚡ con fondo amarillo) o usa el atajo Ctrl+Shift+Enter para ejecutar el script completo

3. Verificar que la base de datos se ha creado
Una vez ejecutado el script, comprueba que todo está bien:
sqlUSE 74_minutes;
SHOW TABLES;
Debes ver las 13 tablas. Después verifica los datos:
sqlSELECT COUNT(*) FROM clientes;
SELECT COUNT(*) FROM albumes;
SELECT COUNT(*) FROM pedidos;
Cada una debe devolver 10 registros.
