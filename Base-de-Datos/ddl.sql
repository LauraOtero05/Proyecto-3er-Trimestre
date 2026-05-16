CREATE DATABASE IF NOT EXISTS `74_minutes` 
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

USE  `74_minutes`;

DROP TABLE IF EXISTS detalles_pedido;
DROP TABLE IF EXISTS pedidos;

DROP TABLE IF EXISTS telefonos_cliente;
DROP TABLE IF EXISTS telefonos_trabajador;
DROP TABLE IF EXISTS telefonos_proveedor;
DROP TABLE IF EXISTS emails_proveedor;

DROP TABLE IF EXISTS generos_musicales;
DROP TABLE IF EXISTS albumes;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS trabajadores;
DROP TABLE IF EXISTS proveedores;

DROP TABLE IF EXISTS codigos_postales;
DROP TABLE IF EXISTS paises;

CREATE TABLE paises (
    codigo_pais CHAR(2) NOT NULL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE codigos_postales (
    id_codigo_postal INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    codigo_postal VARCHAR(15) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    provincia VARCHAR(100) NULL,
    codigo_pais CHAR(2) NOT NULL,
    UNIQUE (codigo_postal, ciudad, codigo_pais),
    FOREIGN KEY (codigo_pais) REFERENCES paises(codigo_pais)
    ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE clientes(
    id_cliente INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    id_codigo_postal INT NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_codigo_postal) REFERENCES codigos_postales(id_codigo_postal)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE telefonos_cliente(
    id_cliente INT NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    PRIMARY KEY(id_cliente, telefono),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE trabajadores(
    DNI VARCHAR(20) NOT NULL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    rol ENUM('Administrador', 'Ventas', 'Almacen', 'Gerente') NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE telefonos_trabajador(
    DNI VARCHAR(20) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    PRIMARY KEY(DNI, telefono),
    FOREIGN KEY (DNI) REFERENCES trabajadores(DNI)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE proveedores(
    id_proveedor INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    id_codigo_postal INT NOT NULL,
    FOREIGN KEY (id_codigo_postal) REFERENCES codigos_postales(id_codigo_postal)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE telefonos_proveedor(
    id_proveedor INT NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    PRIMARY KEY(id_proveedor, telefono),
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE emails_proveedor(
    id_proveedor INT NOT NULL,
    email VARCHAR(150) NOT NULL,
    PRIMARY KEY(id_proveedor, email),
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE generos_musicales(
    id_genero INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    genero VARCHAR(100) NOT NULL
);

CREATE TABLE albumes(
    id_album INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    artista VARCHAR(150) NOT NULL,
    formato ENUM('Vinilo', 'CD', 'Cassette', 'Digital') NOT NULL,
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    id_proveedor INT NOT NULL,
    id_genero INT NOT NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_genero) REFERENCES generos_musicales(id_genero)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE pedidos(
    id_pedido INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    estado ENUM('Pendiente', 'Enviado', 'Entregado', 'Pagado', 'Cancelado') NOT NULL,
    importe_total DECIMAL(10,2) NOT NULL CHECK (importe_total >= 0),
    id_cliente INT NOT NULL,
    DNI_trabajador VARCHAR(20) NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
    ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (DNI_trabajador) REFERENCES trabajadores(DNI)
    ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE detalles_pedido(
    id_detalle INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_album INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad >= 0),
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido)
    ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_album) REFERENCES albumes(id_album)
    ON DELETE RESTRICT ON UPDATE CASCADE
);