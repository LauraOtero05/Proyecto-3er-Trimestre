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

USE `74_minutes`;

INSERT INTO paises(codigo_pais, nombre) VALUES
('ES','España'),
('US','Estados Unidos'),
('FR','Francia'),
('DE','Alemania'),
('IT','Italia'),
('PT','Portugal'),
('GB','Reino Unido'),
('JP','Japón'),
('KR','Corea del Sur'),
('MX','México');

INSERT INTO codigos_postales(codigo_postal, ciudad, provincia, codigo_pais) VALUES
('28001', 'Madrid', 'Madrid', 'ES'),
('28006', 'Madrid', 'Madrid', 'ES'),
('28009', 'Madrid', 'Madrid', 'ES'),
('28013', 'Madrid', 'Madrid', 'ES'),
('28004', 'Madrid', 'Madrid', 'ES'),
('28046', 'Madrid', 'Madrid', 'ES'),
('41001', 'Sevilla', 'Sevilla', 'ES'),
('30204', 'Cartagena', 'Murcia', 'ES'),
('28462', 'Villaviciosa de Odón', 'Madrid', 'ES'),
('10001', 'New York', 'New York', 'US'),
('90210', 'Beverly Hills', 'California', 'US'),
('90028', 'Los Angeles', 'California', 'US'),
('94105', 'San Francisco', 'California', 'US'),
('04536', 'Seoul', 'Sudogwon', 'KR'),
('150-0001', 'Tokyo', 'Kanto', 'JP'),
('75008', 'Paris', 'Île-de-France', 'FR'),
('10115', 'Berlin', 'Berlin', 'DE'),
('00184', 'Rome', 'Lazio', 'IT'),
('SW1A 1AA', 'London', 'Greater London', 'GB');

INSERT INTO clientes(nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES
('Carlos', 'Garcia', 'Calle Mayor, 12', 1, 'carlos.garcia@gmail.com', 'hash001'),
('Laura', 'Martinez', 'Calle Gran Via, 34', 2, 'laura.martinez@gmail.com', 'hash002'),
('Pedro', 'Fernandez', 'Calle del Carmen, 27', 8, 'pedro.fernandez@hotmail.com', 'hash003'),
('Lorena', 'Lopez', 'Calle nueva, 1', 9, 'lorena.lopez@hotmail.com', 'hash004'),
('Miguel', 'Sanchez', 'Avenida de la Paz, 5', 7, 'miguel.sanchez@yahoo.com', 'hash005'),
('Sofia', 'Ruiz', 'Calle Serrano, 8', 4, 'sofia.ruiz@gmail.com', 'hash006'),
('Andres', 'Torres', 'Calle Alcala, 45', 6, 'andres.torres@gmail.com', 'hash007'),
('Maria', 'Diaz', 'Paseo de la Castellana, 3', 8, 'maria.diaz@hotmail.com', 'hash008'),
('Pablo', 'Moreno', 'Calle Fuencarral, 22', 5, 'pablo.moreno@yahoo.com', 'hash009'),
('Elena', 'Jimenez', 'Calle Goya, 17', 1, 'elena.jimenez@gmail.com', 'hash010');

INSERT INTO telefonos_cliente(id_cliente, Telefono) VALUES
(1,  '612345678'),
(2,  '623456789'),
(3,  '634567890'),
(4,  '645678901'),
(5,  '656789012'),
(6,  '667890123'),
(7,  '678901234'),
(8,  '689012345'),
(9,  '690123456'),
(10, '601234567');

INSERT INTO trabajadores(DNI, nombre, apellido, rol, email, password_hash) VALUES
('11111111A','Maria','Rodriguez','Almacen','maria.rodriguez@tienda.com','whash001'),
('22222222B','Cesar','Martin','Ventas','cesar.martin@tienda.com','whash002'),
('33333333C','Ana','Gomez','Almacen','ana.gomez@tienda.com','whash003'),
('44444444D','Luis','Perez','Ventas','luis.perez@tienda.com','whash004'),
('55555555E','Isabel','Navarro','Gerente','isabel.navarro@tienda.com','whash005'),
('66666666F','Jorge','Castillo','Almacen','jorge.castillo@tienda.com','whash006'),
('77777777G','Marta','Gil','Ventas','marta.gil@tienda.com','whash007'),
('88888888H','Raul','Herrera','Almacen','raul.herrera@tienda.com','whash008'),
('99999999I','Carmen','Santos','Ventas','carmen.santos@tienda.com','whash009'),
('10101010J','Antonio','Flores','Gerente','antonio.flores@tienda.com','whash010');

INSERT INTO telefonos_trabajador(DNI, Telefono) VALUES
('11111111A', '611000001'),
('22222222B', '622000002'),
('33333333C', '633000003'),
('44444444D', '644000004'),
('55555555E', '655000005'),
('66666666F', '666000006'),
('77777777G', '677000007'),
('88888888H', '688000008'),
('99999999I', '699000009'),
('10101010J', '610000010');

INSERT INTO proveedores(nombre, direccion, id_codigo_postal) VALUES
('HYBE Labels',         '42, Hangang-daero, Yongsan-gu', 14), 
('Columbia Records',    '25 Madison Avenue',             10), 
('Rimas Entertainment', '644 Ave. Fernández Juncos, Ste. 501', 11), 
('Universal Music',     '2220 Colorado Avenue',          12), 
('Sony Music Japan',    '9-6-35 Akasaka, Minato-ku',     15), 
('Warner Music',        '1633 Broadway',                 10), 
('EMI Records',         '4 Pancras Square, Kings Cross', 19), 
('Atlantic Records',    '1633 Broadway',                 10), 
('Interscope Records',  '2220 Colorado Avenue',          12), 
('Republic Records',    '1755 Broadway',                 10);

INSERT INTO telefonos_proveedor(id_proveedor, Telefono) VALUES
(1,  '910000001'),
(2,  '910000002'),
(3,  '910000003'),
(4,  '910000004'),
(5,  '910000005'),
(6,  '910000006'),
(7,  '910000007'),
(8,  '910000008'),
(9,  '910000009'),
(10, '910000010');

INSERT INTO emails_proveedor(id_proveedor, email) VALUES
(1,  'contacto@hybelabels.com'),
(2,  'info@columbiarecords.com'),
(3,  'hola@rimasentertainment.com'),
(4,  'contacto@universalmusic.com'),
(5,  'info@sonymusic.com'),
(6,  'contacto@warnermusic.com'),
(7,  'info@emirecords.com'),
(8,  'contacto@atlanticrecords.com'),
(9,  'info@interscope.com'),
(10, 'contacto@republicrecords.com');

INSERT INTO generos_musicales(genero) VALUES
('K-Pop'),
('Rock'),
('Classic'),
('Reggaeton'),
('Pop'),
('Hip-Hop'),
('Jazz'),
('Electronica'),
('R&B'),
('Flamenco');

INSERT INTO albumes(titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES
('Wake Up',           'BTS',             'CD',     32.99, 186, 1,  1),
('Buenas Noches',     'Quevedo',         'CD',     20.99, 128, 3,  4),
('Thriller',          'Michael Jackson', 'Vinilo', 24.99,  75, 4,  5),
('Back in Black',     'AC/DC',           'CD',     18.99,  60, 6,  2),
('The Dark Side',     'Pink Floyd',      'Vinilo', 29.99,  45, 7,  2),
('Random Access',     'Daft Punk',       'CD',     21.99,  90, 8,  8),
('Gold Digger',       'Kanye West',      'CD',     19.99, 110, 9,  6),
('Come Away With Me', 'Norah Jones',     'Vinilo', 22.99,  55, 10, 7),
('Besos en Guerra',   'Morat',           'CD',     17.99, 200, 2,  5),
('Motomami',          'Rosalia',         'CD',     23.99, 160, 3, 10);

INSERT INTO pedidos(fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES
('2025-01-10', 'Entregado', 65.98, 1,  '11111111A'),
('2025-01-15', 'Entregado', 20.99, 2,  '22222222B'),
('2025-01-20', 'Enviado',   54.98, 3,  '33333333C'),
('2025-02-01', 'Pendiente', 24.99, 4,  '44444444D'),
('2025-02-05', 'Entregado', 37.98, 5,  '55555555E'),
('2025-02-10', 'Enviado',   29.99, 6,  '66666666F'),
('2025-02-14', 'Pendiente', 21.99, 7,  '77777777G'),
('2025-02-20', 'Entregado', 42.98, 8,  '88888888H'),
('2025-03-01', 'Enviado',   19.99, 9,  '99999999I'),
('2025-03-05', 'Pendiente', 46.98, 10, '10101010J');

INSERT INTO detalles_pedido(id_pedido, id_album, cantidad) VALUES
(1,  1,  2),
(2,  2,  1),
(3,  3,  2),
(4,  4,  1),
(5,  5,  1),
(6,  6,  1),
(7,  7,  1),
(8,  8,  1),
(9,  9,  1),
(10, 10, 2);