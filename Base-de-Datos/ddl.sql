CREATE DATABASE IF NOT EXISTS 74_minutes;
USE  74_minutes;

CREATE TABLE Cliente(
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    calle VARCHAR(200),
    codigo_postal INT,
    email VARCHAR(150) UNIQUE,
    password_hash VARCHAR(255)
);

CREATE TABLE TELEFONO_CLIENTE(
    id_cliente INT,
    Telefono VARCHAR(20),
    PRIMARY KEY(id_cliente, Telefono),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

CREATE TABLE Trabajador(
    DNI VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    rol VARCHAR(50),
    email VARCHAR(150) UNIQUE,
    password_hash VARCHAR(255)
);

CREATE TABLE TELEFONO_TRABAJADOR(
    DNI VARCHAR(20),
    Telefono VARCHAR(20),
    PRIMARY KEY(DNI, Telefono),
    FOREIGN KEY (DNI) REFERENCES Trabajador(DNI)
);

CREATE TABLE Proveedor(
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150),
    calle VARCHAR(200),
    codigo_postal INT
);

CREATE TABLE TELEFONO_PROVEEDOR(
    id_proveedor INT,
    Telefono VARCHAR(20),
    PRIMARY KEY(id_proveedor, Telefono),
    FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor)
);

CREATE TABLE EMAIL_PROVEEDOR(
    id_proveedor INT,
    email VARCHAR(150),
    PRIMARY KEY(id_proveedor, email),
    FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor)
);

CREATE TABLE genero_musical(
    id_genero INT AUTO_INCREMENT PRIMARY KEY,
    genero VARCHAR(100)
);

CREATE TABLE Album(
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200),
    artista VARCHAR(150),
    formato VARCHAR(50),
    precio DECIMAL(10,2),
    stock INT,
    id_proveedor INT,
    id_genero INT,
    FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor),
    FOREIGN KEY (id_genero) REFERENCES genero_musical(id_genero)
);

CREATE TABLE Pedidos(
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    estado VARCHAR(50),
    importe_total DECIMAL(10,2),
    id_cliente INT,
    DNI_trabajador VARCHAR(20),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
    FOREIGN KEY (DNI_trabajador) REFERENCES Trabajador(DNI)
);

CREATE TABLE DETALLE_PEDIDO(
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_producto INT,
    cantidad INT,
    FOREIGN KEY (id_pedido) REFERENCES Pedidos(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES Album(id_producto)
);

USE 74_minutes;

SELECT * FROM Cliente;
SELECT * FROM TELEFONO_CLIENTE;
SELECT * FROM Trabajador;
SELECT * FROM TELEFONO_TRABAJADOR;
SELECT * FROM Proveedor;
SELECT * FROM TELEFONO_PROVEEDOR;
SELECT * FROM EMAIL_PROVEEDOR;
SELECT * FROM genero_musical;
SELECT * FROM Album;
SELECT * FROM Pedidos;
SELECT * FROM DETALLE_PEDIDO;