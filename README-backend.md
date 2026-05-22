# Programación

Descripción general del CRM en Java

## Tecnologías utilizadas 

Java 21
MySQL (conexión JDBC)
Conector (lo del pom.xml, creo)

## Arquitectura del Proyecto 

Explicación de la arquitectura en capas

```bash
    src/
    │
    ├── controllers/
    ├── services/
    ├── repositories/
    ├── entities/
    ├── enums/
    ├── graphicInterfaces/
    └── util/
```

## Requisitos previos

JDK 21 instalado
MySQL Server en ejecución
IDE recomendado: IntelliJ IDEA (compatible con cualquier IDE Java)
Driver JDBC: mysql-connector-j (instrucciones para comprobar q esta bien conectado al proyecto)

## Instrucciones para Configuración

Clonar el repositorio
Abrir el proyecto en IntelliJ
Añadir mysql-connector-j.jar a las librerías del proyecto
Asegurarse de que MySQL está en ejecución
Qué hacer si se usa un nombre de base de datos distinto al del script (dónde cambiarlo en el código)

## Instrucciones para Ejecutar

Ejecutar Main.java
El programa pedirá al inicio:

Puerto de MySQL (por defecto: 3306 — introducir el configurado en tu sistema)
Usuario de MySQL
Contraseña

Si la conexión es correcta se abrirá el menú principal

## Posibles errores comunes

Puerto incorrecto → revisar configuración de MySQL
Contraseña incorrecta → revisar credenciales
Driver no encontrado → revisar que el .jar está añadido al proyecto
Base de datos no encontrada → asegurarse de haber ejecutado los scripts DDL y DML

