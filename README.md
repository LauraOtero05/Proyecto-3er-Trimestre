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
    │   └── frontend
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
| `backend-v1.0` | Primera versión estable del proyecto completo de JAVA |
| `frontend-v1.0` | Primera versión estable del proyecto completo de Lenguaje de Marcas |

Para consultar todos los tags disponibles:

```bash
git tag -l
```

Para acceder al estado del proyecto en una versión concreta:

```bash
git checkout db-v1.0
```

# Base de Datos

# Lenguaje de Marcas

# Programación


## Autores

- Laura Otero Martín
- Noemí Cano Conesa
- Álvaro Baz Rodríguez
