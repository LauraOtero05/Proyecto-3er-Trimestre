# Base de Datos - Xtart Manager

## Descripción
Este repositorio contiene la base de datos del proyecto académico **Xtart Manager**, un gestor de **ligas de fútbol**. La base de datos está compuesta por 15 tablas:

- `ligas`
- `jornadas`
- `estadios`
- `equipos`
- `entrenadores`
- `arbitros`
- `partidos`
- `jugadores`
- `tienda_fichajes`
- `jugadas_partidos`
- `entrenadores_equipos`
- `partidos_arbitros`
- `posiciones`
- `codigopostal_ciudad`
- `telefono_jugador`

La **base de datos** ha sido diseñada siguiendo un proceso completo de modelado:

1. Modelo Entidad-Relación
2. Modelo Lógico
3. Normalización
4. Modelo Físico
5. Implementación en MySQL

## Estructura del repositorio
El proyecto está organizado en las siguientes carpetas:

```bash
    xtart_manager/
    │
    ├── MODELOS DRAW IO/
    │   └── MODELOS Xtart Manager.drawio
    │
    ├── MODELO FISICO/
    │   ├── modelo fisico.dbs
    │   └── modelo fisico.dbs.bak
    │
    ├── SCRIPTS/
    │   ├── creacion_DDL_XtartManager.sql
    │   ├── datos_creacion_DML_XtartManager.sql
    │   └── errores_DDL.sql
    │
    └── README.md
```

- **MODELOS DRAW IO** → Contiene los diagramas realizados con Draw.io (modelo ER, lógico y normalización).

- **MODELO FISICO** → Contiene el diagrama del modelo físico realizado con DBSchema.

- **SCRIPTS** → Scripts SQL:

    - **DDL** (creación de base de datos y tablas)

    - **DML** (inserción de datos)

    - **Script** que demuestra la correcta aplicación de las restricciones definidas y la forma en la que el sistema impide operaciones que romperían la integridad del esquema. 


## Tecnologías utilizadas

- **MySQL**: Sistema de gestión de bases de datos relacional utilizado para la implementación del modelo físico.  
- **SQL (DDL y DML)**: 
    - **DDL (Data Definition Language)** para la creación de la base de datos, tablas, claves primarias, claves foráneas y restricciones.
    - **DML (Data Manipulation Language)** para la inserción de datos iniciales.
- **Draw.io**: Herramienta utilizada para el diseño del modelo entidad-relación, modelo lógico y proceso de normalización. 
- **DBSchema**: Utilizado para generar y visualizar el modelo físico de la base de datos. normalización. 
- **Git y GitHub**: Control de versiones y gestión del repositorio.

## Requisitos del proyecto

- **MySQL Server** instalado.  
- **Cliente de MySQL** o herramienta como: 
    - MySQL Workbench.
    - DBeaver
- **Acceso a Git** para clonar el repositorio.  
- **Sistema operativo compatible**: Windows, macOS o Linux.  

## Despliegue de la base de datos
Para implementar la base de datos **Xtart Manager** en MySQL, seguir los siguientes pasos:
1. **Clonar el repositorio:**

```bash
    git clone https://github.com/LauraOtero05/Proyecto_2_Trimestre--BASE-DE-DATOS.git
```
2. **Acceder a la carpeta del proyecto:**
```bash
    cd Proyecto_2_Trimestre--BASE-DE-DATOS
```
3. **Abrir el cliente de MySQL:**

Conectarse al servidor MySQL desde:
- MySQL Workbench
- DBeaver
- Consola MySQL

## Ejecución en MySQL / DBeaver

1. **Asegurar que MySQL esté en ejecución**
Verificar que el servidor MySQL esté activo antes de comenzar.

2. **Crear conexión en DBeaver (opcional)**
    1. Abrir DBeaver
    2. Crear nueva conexión → MySQL
    3. Configurar: 
        - Host:  `localhost`
        - Puerto: `3306`
        - Usuario: `root` (o el usuario que corresponda)
        - Contraseña correspondiente

3. **Ejecutar scripts en el orden correcto**
El orden de ejecución es fundamental:
1. `Creacion_DDL_XtartManager.sql`
2. `Creacion_DML_XtartManager.sql`

4. **Verificación**

```bash
USE xtart_manager;
SHOW TABLES;
SELECT COUNT(*) FROM equipos;
```
Si los datos aparecen correctamente, la base de datos está lista.

# Modelo Entidad-Relación y Modelos previos al desarrollo de la Base de Datos en lenguaje SQL
El sistema **Xtart Manager** ha sido diseñado siguiendo un modelo relacional completo, partiendo de un **Modelo Entidad-Relación (ER)** que posteriormente fue transformado en **Modelo Lógico**, a continuación fue **Normalizado** y finalmente fue transformado en **Modelo Físico** implementado en MySQL.

El diseño garantiza:
- Coherencia estructural
- Integridad referencial
- Eliminación de redundancias
- Correcta normalización de los datos

## Entidades principales

Las entidades principales del sistema son:

- `ligas`
- `jornadas`
- `equipos`
- `jugadores`
- `entrenadores`
- `partidos`
- `arbitros`
- `estadios`
- `fichajes`
- `posiciones`
- `jugadas_partidos`

Estas entidades representan los elementos fundamentales necesarios para simular el funcionamiento de una aplicación de gestión de ligas de fútbol.

## Relaciones clave del sistema

### Liga – Jornada (1:N)

Una liga puede tener múltiples jornadas, pero cada jornada pertenece únicamente a una liga.
- Clave foránea: `jornadas.id_liga`
- Reglas aplicadas:
  - `ON UPDATE CASCADE`
  - `ON DELETE RESTRICT`

Esto impide eliminar una liga si existen jornadas asociadas.

### Equipo – Jugador (1:N)

Un equipo puede tener varios jugadores, pero cada jugador pertenece a un único equipo.
- Clave foránea: `jugadores.id_equipo`
- Regla: `ON DELETE RESTRICT`

### Entrenadores – Equipos (N:N)

Un entrenador puede dirigir distintos equipos en diferentes temporadas, y un equipo puede tener distintos entrenadores a lo largo del tiempo.
Se resuelve mediante la tabla intermedia:
- `entrenadores_equipos`
Clave primaria compuesta:
```sql
PRIMARY KEY (id_equipo, id_entrenador)
```
Incluye además información adicional:
- `temporada`
- `fecha_inicio`
- `fecha_fin`

Esto permite almacenar el histórico de entrenadores por temporada. 

### Partidos – Árbitros (N:N)

Un partido puede tener varios árbitros y un árbitro puede participar en varios partidos.

Se resuelve mediante la tabla intermedia:
- `partidos_arbitros`

Esta relación enlaza con la clave primaria compuesta de `partidos`:
```sql
PRIMARY KEY (id_equipo, id_entrenador)
```

### Relación Reflexiva en Árbitros 

La tabla `arbitros` incluye el campo: 
```sql
FOREIGN KEY (supervisa) REFERENCES arbitros(id_arbitro) 
```
Generando una relación reflexiva, donde un árbitro puede supervisar a otro.

### Relación Partido – Equipo (1:N) 

En la tabla `partidos` existen tres claves foráneas hacia `equipos`:
- `id_equipo_local`
- `id_equipo_visitante`
- `id_equipo_ganador`

Esto permite modelar correctamente los roles dentro de un partido.

### Clave Primaria Compuesta en Partidos

La tabla `partidos` utiliza:
```sql
PRIMARY KEY (numero_partido, id_jornada)
```
Esto representa un **Relación de Identificación** donde un partido depende de la jornada a la que pertenece para ser idientificado.

### Control de integridad y restricciones

El modelo incluye distintos mecanismos de control:
- Claves foráneas con reglas:
  - `CASCADE`
  - `RESTRICT`
  - `SET NULL`
- Restricción `CHECK`:
```sql
CHECK (dorsal > 0)
```
- Uso de `ENUM`:
    - Estado de jornada (`pendiente`, `iniciada`, `finalizada`)
    - Tipo de césped (`natural`, `artificial`)
- Campos `NOT NULL` en atributos obligatorios
Estos mecanismos garantizan la coherencia y validez de los datos almacenados.

## Normalización
El modelo ha sido normalizado hasta **Tercera Forma Normal (3FN)**:

- Separación de tablas intermedias para relaciones N:N
- Tabla independiente `codigopostal_ciudad` para evitar redundancia de ciudades
- Tabla `telefono_jugador` separada para separar los valores del atributo multivaluado `teléfono`
- Tabla independiente para `posiciones` y tabla intermedia entre `posiciones` y `jugadores` permitiendo reducir redundancias y que un jugador tenga la posibilidad de jugar en varias posiciones en distintos partidos
- Eliminación de dependencias parciales y transitivas

Esto mejora la integridad del sistema, evita redundancias y optimiza el mantenimiento de los datos.

## Explicación de las tablas

| Tabla | Descripción |
|-------|------------|
| `ligas` | Contiene la información general de cada liga. |
| `jornadas` | Registra las jornadas de cada liga. |
| `estadios` | Almacena los datos de los estadios. |
| `equipos` | Contiene la información de los equipos participantes. |
| `entrenadores` | Guarda los datos personales y experiencia de los entrenadores. |
| `arbitros` | Registra la información de los árbitros. |
| `partidos` | Almacena los partidos disputados en cada jornada. |
| `jugadores` | Contiene la información de los jugadores de cada equipo. |
| `fichajes` | Registra los jugadores sin equipo disponibles en la tienda de fichajes. |
| `jugadas_partidos` | Guarda estadísticas individuales de jugadores en partidos. |
| `entrenadores_equipos` | Tabla intermedia que relaciona entrenadores con equipos por temporada. |
| `partidos_arbitros` | Tabla intermedia que relaciona los árbitros asignados a cada partido. |
| `telefono_jugador` | Permite almacenar uno o varios teléfonos asociados a cada jugador. |
| `posiciones` | Define las posiciones en las que puede jugar un jugador (portero, defensa, etc.). |
| `codigopostal_ciudad` | Almacena la relación entre códigos postales y ciudades, evitando redundancia en otras tablas. |

## Autores

- Laura Otero Martín
- Noemí Cano Conesa
- Sergio Rodríguez López


Proyecto_2Trimestre__Lenguaje-de-Marcas/
│
├── css/
│   ├── styles.css
│   └── stylesQuerys.css
│
├── imagenes/
│   └── imagenes del proyecto
│
├── js/
│   └── script.js
│
├── paginas/
│   ├── CopaDelRey.html
│   ├── equipos.html
│   ├── index.html
│   ├── Liga.html
│   ├── Partidos.html
│   └── Supercopa.html
│
├── videos/
│   └── videos del proyecto
│
└── README.md
