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


