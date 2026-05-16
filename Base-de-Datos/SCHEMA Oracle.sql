DROP USER "74min" CASCADE;
CREATE USER "74min" IDENTIFIED BY passwd74M;
GRANT CONNECT, RESOURCE, DBA TO "74min";
GRANT UNLIMITED TABLESPACE TO "74min";
ALTER PROFILE DEFAULT LIMIT PASSWORD_REUSE_TIME UNLIMITED;
ALTER PROFILE DEFAULT LIMIT PASSWORD_LIFE_TIME UNLIMITED;
CONNECT "74min"/passwd74M@LOCALHOST:1521/XEPDB1

CREATE TABLE paises (
    codigo_pais CHAR(2) NOT NULL,
    nombre VARCHAR2(100) NOT NULL,
    CONSTRAINT pk_paises PRIMARY KEY (codigo_pais)
);

COMMENT ON TABLE paises IS 'Países disponibles en el sistema';
COMMENT ON COLUMN paises.codigo_pais IS 'Código ISO';

CREATE TABLE codigos_postales (
    id_codigo_postal NUMBER(10) GENERATED ALWAYS AS IDENTITY,
    codigo_postal VARCHAR2(15) NOT NULL,
    ciudad VARCHAR2(100) NOT NULL,
    provincia VARCHAR2(100),
    codigo_pais CHAR(2) NOT NULL,
    CONSTRAINT pk_codigos_postales PRIMARY KEY (id_codigo_postal),
    CONSTRAINT uq_codigos_postales UNIQUE (codigo_postal, ciudad, codigo_pais),
    CONSTRAINT fk_cp_pais FOREIGN KEY (codigo_pais) REFERENCES paises(codigo_pais)
        ON DELETE SET NULL
);

COMMENT ON TABLE codigos_postales IS 'Códigos postales con ciudad y país asociado';

CREATE TABLE clientes (
    id_cliente NUMBER(10) GENERATED ALWAYS AS IDENTITY,
    nombre VARCHAR2(100) NOT NULL,
    apellido VARCHAR2(100) NOT NULL,
    direccion VARCHAR2(200) NOT NULL,
    id_codigo_postal NUMBER(10) NOT NULL,
    email VARCHAR2(150) NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    CONSTRAINT pk_clientes PRIMARY KEY (id_cliente),
    CONSTRAINT fk_cli_cp FOREIGN KEY (id_codigo_postal) REFERENCES codigos_postales(id_codigo_postal)
);

COMMENT ON TABLE clientes IS 'Clientes registrados en la tienda';
COMMENT ON COLUMN clientes.password_hash IS 'Hash de la contraseña del cliente';

CREATE TABLE telefonos_cliente (
    id_cliente NUMBER(10) NOT NULL,
    telefono VARCHAR2(20) NOT NULL,
    CONSTRAINT pk_tel_cliente PRIMARY KEY (id_cliente, telefono),
    CONSTRAINT fk_telcli_cli FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
        ON DELETE CASCADE
);

CREATE TABLE trabajadores (
    DNI VARCHAR2(20) NOT NULL,
    nombre VARCHAR2(100) NOT NULL,
    apellido VARCHAR2(100) NOT NULL,
    rol VARCHAR2(20)  NOT NULL,
    email VARCHAR2(150) NOT NULL,
    password_hash VARCHAR2(255) NOT NULL,
    CONSTRAINT pk_trabajadores PRIMARY KEY (DNI),
    CONSTRAINT chk_rol CHECK (rol IN ('Administrador','Ventas','Almacen','Gerente'))
);

COMMENT ON TABLE trabajadores IS 'Empleados de la tienda';
COMMENT ON COLUMN trabajadores.rol IS 'Administrador, Ventas, Almacen o Gerente';

CREATE TABLE telefonos_trabajador (
    DNI VARCHAR2(20) NOT NULL,
    telefono VARCHAR2(20) NOT NULL,
    CONSTRAINT pk_tel_trabajador PRIMARY KEY (DNI, telefono),
    CONSTRAINT fk_teltrab_trab FOREIGN KEY (DNI) REFERENCES trabajadores(DNI)
        ON DELETE CASCADE
);

CREATE TABLE proveedores (
    id_proveedor NUMBER(10) GENERATED ALWAYS AS IDENTITY,
    nombre VARCHAR2(150) NOT NULL,
    direccion VARCHAR2(200) NOT NULL,
    id_codigo_postal NUMBER(10) NOT NULL,
    CONSTRAINT pk_proveedores PRIMARY KEY (id_proveedor),
    CONSTRAINT fk_prov_cp FOREIGN KEY (id_codigo_postal) REFERENCES codigos_postales(id_codigo_postal)
);

COMMENT ON TABLE proveedores IS 'Proveedores de álbumes, en su mayoría discográficas';

CREATE TABLE telefonos_proveedor (
    id_proveedor NUMBER(10) NOT NULL,
    telefono VARCHAR2(20) NOT NULL,
    CONSTRAINT pk_tel_proveedor PRIMARY KEY (id_proveedor, telefono),
    CONSTRAINT fk_telprov_prov FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
        ON DELETE CASCADE
);

CREATE TABLE emails_proveedor (
    id_proveedor NUMBER(10) NOT NULL,
    email VARCHAR2(150) NOT NULL,
    CONSTRAINT pk_email_proveedor PRIMARY KEY (id_proveedor, email),
    CONSTRAINT fk_emailprov_prov FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
        ON DELETE CASCADE
);

CREATE TABLE generos_musicales (
    id_genero NUMBER(10) GENERATED ALWAYS AS IDENTITY,
    genero VARCHAR2(100) NOT NULL,
    CONSTRAINT pk_generos PRIMARY KEY (id_genero)
);

COMMENT ON TABLE generos_musicales IS 'Géneros musicales del catálogo';

CREATE TABLE albumes (
    id_album NUMBER(10) GENERATED ALWAYS AS IDENTITY,
    titulo VARCHAR2(200) NOT NULL,
    artista VARCHAR2(150) NOT NULL,
    formato VARCHAR2(10) NOT NULL,
    precio NUMBER(10,2) NOT NULL,
    stock NUMBER(10) DEFAULT 0 NOT NULL,
    id_proveedor NUMBER(10) NOT NULL,
    id_genero NUMBER(10) NOT NULL,
    CONSTRAINT pk_albumes PRIMARY KEY (id_album),
    CONSTRAINT chk_formato CHECK (formato IN ('Vinilo','CD','Cassette','Digital')),
    CONSTRAINT chk_precio CHECK (precio >= 0),
    CONSTRAINT chk_stock CHECK (stock  >= 0),
    CONSTRAINT fk_alb_prov FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor),
    CONSTRAINT fk_alb_genero FOREIGN KEY (id_genero)    REFERENCES generos_musicales(id_genero)
);

COMMENT ON TABLE  albumes IS 'Catálogo de álbumes disponibles para la venta';
COMMENT ON COLUMN albumes.formato IS 'Vinilo, CD, Cassette o Digital';
COMMENT ON COLUMN albumes.precio  IS 'Precio de venta en euros';
COMMENT ON COLUMN albumes.stock   IS 'Unidades disponibles en almacén';

CREATE TABLE pedidos (
    id_pedido NUMBER(10) GENERATED ALWAYS AS IDENTITY,
    fecha DATE NOT NULL,
    estado VARCHAR2(15) NOT NULL,
    importe_total NUMBER(10,2) NOT NULL,
    id_cliente NUMBER(10) NOT NULL,
    DNI_trabajador VARCHAR2(20),
    CONSTRAINT pk_pedidos PRIMARY KEY (id_pedido),
    CONSTRAINT chk_estado CHECK (estado IN ('Pendiente','Enviado','Entregado','Pagado','Cancelado')),
    CONSTRAINT chk_importe_total CHECK (importe_total >= 0),
    CONSTRAINT fk_ped_cli FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    CONSTRAINT fk_ped_trab FOREIGN KEY (DNI_trabajador) REFERENCES trabajadores(DNI)
        ON DELETE SET NULL
);

COMMENT ON TABLE  pedidos IS 'Pedidos realizados por los clientes';
COMMENT ON COLUMN pedidos.estado IS 'Pendiente, Enviado, Entregado, Pagado o Cancelado';
COMMENT ON COLUMN pedidos.DNI_trabajador IS 'Trabajador asignado, puede ser NULL si todavía no se ha asignado un trabajador';

CREATE TABLE detalles_pedido (
    id_detalle NUMBER(10) GENERATED ALWAYS AS IDENTITY,
    id_pedido NUMBER(10) NOT NULL,
    id_album NUMBER(10) NOT NULL,
    cantidad NUMBER(10) NOT NULL,
    CONSTRAINT pk_detalles_pedido PRIMARY KEY (id_detalle),
    CONSTRAINT chk_cantidad CHECK (cantidad >= 0),
    CONSTRAINT fk_det_pedido FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido)
        ON DELETE CASCADE,
    CONSTRAINT fk_det_album FOREIGN KEY (id_album) REFERENCES albumes(id_album)
);

COMMENT ON TABLE detalles_pedido IS 'Cantidad de productos solicitados en cada pedido';

INSERT INTO paises VALUES ('ES','España');
INSERT INTO paises VALUES ('US','Estados Unidos');
INSERT INTO paises VALUES ('FR','Francia');
INSERT INTO paises VALUES ('DE','Alemania');
INSERT INTO paises VALUES ('IT','Italia');
INSERT INTO paises VALUES ('PT','Portugal');
INSERT INTO paises VALUES ('GB','Reino Unido');
INSERT INTO paises VALUES ('JP','Japón');
INSERT INTO paises VALUES ('KR','Corea del Sur');
INSERT INTO paises VALUES ('MX','México');

INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('28001',    'Madrid',              'Madrid',         'ES');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('28006',    'Madrid',              'Madrid',         'ES');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('28009',    'Madrid',              'Madrid',         'ES');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('28013',    'Madrid',              'Madrid',         'ES');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('28004',    'Madrid',              'Madrid',         'ES');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('28046',    'Madrid',              'Madrid',         'ES');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('41001',    'Sevilla',             'Sevilla',        'ES');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('30204',    'Cartagena',           'Murcia',         'ES');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('28462',    'Villaviciosa de Odón','Madrid',         'ES');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('10001',    'New York',            'New York',       'US');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('90210',    'Beverly Hills',       'California',     'US');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('90028',    'Los Angeles',         'California',     'US');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('94105',    'San Francisco',       'California',     'US');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('04536',    'Seoul',               'Sudogwon',       'KR');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('150-0001', 'Tokyo',               'Kanto',          'JP');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('75008',    'Paris',               'Île-de-France',  'FR');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('10115',    'Berlin',              'Berlin',         'DE');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('00184',    'Rome',                'Lazio',          'IT');
INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES ('SW1A 1AA', 'London',              'Greater London', 'GB');

INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES ('Carlos', 'Garcia',    'Calle Mayor, 12',           1,  'carlos.garcia@gmail.com',    'hash001');
INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES ('Laura',  'Martinez',  'Calle Gran Via, 34',        2,  'laura.martinez@gmail.com',   'hash002');
INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES ('Pedro',  'Fernandez', 'Calle del Carmen, 27',      8,  'pedro.fernandez@hotmail.com','hash003');
INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES ('Lorena', 'Lopez',     'Calle nueva, 1',            9,  'lorena.lopez@hotmail.com',   'hash004');
INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES ('Miguel', 'Sanchez',   'Avenida de la Paz, 5',      7,  'miguel.sanchez@yahoo.com',   'hash005');
INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES ('Sofia',  'Ruiz',      'Calle Serrano, 8',          4,  'sofia.ruiz@gmail.com',       'hash006');
INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES ('Andres', 'Torres',    'Calle Alcala, 45',          6,  'andres.torres@gmail.com',    'hash007');
INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES ('Maria',  'Diaz',      'Paseo de la Castellana, 3', 8,  'maria.diaz@hotmail.com',     'hash008');
INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES ('Pablo',  'Moreno',    'Calle Fuencarral, 22',      5,  'pablo.moreno@yahoo.com',     'hash009');
INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES ('Elena',  'Jimenez',   'Calle Goya, 17',            1,  'elena.jimenez@gmail.com',    'hash010');

INSERT INTO telefonos_cliente VALUES (1,  '612345678');
INSERT INTO telefonos_cliente VALUES (2,  '623456789');
INSERT INTO telefonos_cliente VALUES (3,  '634567890');
INSERT INTO telefonos_cliente VALUES (4,  '645678901');
INSERT INTO telefonos_cliente VALUES (5,  '656789012');
INSERT INTO telefonos_cliente VALUES (6,  '667890123');
INSERT INTO telefonos_cliente VALUES (7,  '678901234');
INSERT INTO telefonos_cliente VALUES (8,  '689012345');
INSERT INTO telefonos_cliente VALUES (9,  '690123456');
INSERT INTO telefonos_cliente VALUES (10, '601234567');

INSERT INTO trabajadores VALUES ('11111111A','Maria',   'Rodriguez','Almacen', 'maria.rodriguez@tienda.com','whash001');
INSERT INTO trabajadores VALUES ('22222222B','Cesar',   'Martin',   'Ventas',  'cesar.martin@tienda.com',   'whash002');
INSERT INTO trabajadores VALUES ('33333333C','Ana',     'Gomez',    'Almacen', 'ana.gomez@tienda.com',      'whash003');
INSERT INTO trabajadores VALUES ('44444444D','Luis',    'Perez',    'Ventas',  'luis.perez@tienda.com',     'whash004');
INSERT INTO trabajadores VALUES ('55555555E','Isabel',  'Navarro',  'Gerente', 'isabel.navarro@tienda.com', 'whash005');
INSERT INTO trabajadores VALUES ('66666666F','Jorge',   'Castillo', 'Almacen', 'jorge.castillo@tienda.com', 'whash006');
INSERT INTO trabajadores VALUES ('77777777G','Marta',   'Gil',      'Ventas',  'marta.gil@tienda.com',      'whash007');
INSERT INTO trabajadores VALUES ('88888888H','Raul',    'Herrera',  'Almacen', 'raul.herrera@tienda.com',   'whash008');
INSERT INTO trabajadores VALUES ('99999999I','Carmen',  'Santos',   'Ventas',  'carmen.santos@tienda.com',  'whash009');
INSERT INTO trabajadores VALUES ('10101010J','Antonio', 'Flores',   'Gerente', 'antonio.flores@tienda.com', 'whash010');

INSERT INTO telefonos_trabajador VALUES ('11111111A', '611000001');
INSERT INTO telefonos_trabajador VALUES ('22222222B', '622000002');
INSERT INTO telefonos_trabajador VALUES ('33333333C', '633000003');
INSERT INTO telefonos_trabajador VALUES ('44444444D', '644000004');
INSERT INTO telefonos_trabajador VALUES ('55555555E', '655000005');
INSERT INTO telefonos_trabajador VALUES ('66666666F', '666000006');
INSERT INTO telefonos_trabajador VALUES ('77777777G', '677000007');
INSERT INTO telefonos_trabajador VALUES ('88888888H', '688000008');
INSERT INTO telefonos_trabajador VALUES ('99999999I', '699000009');
INSERT INTO telefonos_trabajador VALUES ('10101010J', '610000010');

INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES ('HYBE Labels',         '42, Hangang-daero, Yongsan-gu',       14);
INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES ('Columbia Records',    '25 Madison Avenue',                   10);
INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES ('Rimas Entertainment', '644 Ave. Fernández Juncos, Ste. 501', 11);
INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES ('Universal Music',     '2220 Colorado Avenue',                12);
INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES ('Sony Music Japan',    '9-6-35 Akasaka, Minato-ku',           15);
INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES ('Warner Music',        '1633 Broadway',                       10);
INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES ('EMI Records',         '4 Pancras Square, Kings Cross',       19);
INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES ('Atlantic Records',    '1633 Broadway',                       10);
INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES ('Interscope Records',  '2220 Colorado Avenue',                12);
INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES ('Republic Records',    '1755 Broadway',                       10);

INSERT INTO telefonos_proveedor VALUES (1,  '910000001');
INSERT INTO telefonos_proveedor VALUES (2,  '910000002');
INSERT INTO telefonos_proveedor VALUES (3,  '910000003');
INSERT INTO telefonos_proveedor VALUES (4,  '910000004');
INSERT INTO telefonos_proveedor VALUES (5,  '910000005');
INSERT INTO telefonos_proveedor VALUES (6,  '910000006');
INSERT INTO telefonos_proveedor VALUES (7,  '910000007');
INSERT INTO telefonos_proveedor VALUES (8,  '910000008');
INSERT INTO telefonos_proveedor VALUES (9,  '910000009');
INSERT INTO telefonos_proveedor VALUES (10, '910000010');

INSERT INTO emails_proveedor VALUES (1,  'contacto@hybelabels.com');
INSERT INTO emails_proveedor VALUES (2,  'info@columbiarecords.com');
INSERT INTO emails_proveedor VALUES (3,  'hola@rimasentertainment.com');
INSERT INTO emails_proveedor VALUES (4,  'contacto@universalmusic.com');
INSERT INTO emails_proveedor VALUES (5,  'info@sonymusic.com');
INSERT INTO emails_proveedor VALUES (6,  'contacto@warnermusic.com');
INSERT INTO emails_proveedor VALUES (7,  'info@emirecords.com');
INSERT INTO emails_proveedor VALUES (8,  'contacto@atlanticrecords.com');
INSERT INTO emails_proveedor VALUES (9,  'info@interscope.com');
INSERT INTO emails_proveedor VALUES (10, 'contacto@republicrecords.com');

INSERT INTO generos_musicales (genero) VALUES ('K-Pop');
INSERT INTO generos_musicales (genero) VALUES ('Rock');
INSERT INTO generos_musicales (genero) VALUES ('Classic');
INSERT INTO generos_musicales (genero) VALUES ('Reggaeton');
INSERT INTO generos_musicales (genero) VALUES ('Pop');
INSERT INTO generos_musicales (genero) VALUES ('Hip-Hop');
INSERT INTO generos_musicales (genero) VALUES ('Jazz');
INSERT INTO generos_musicales (genero) VALUES ('Electronica');
INSERT INTO generos_musicales (genero) VALUES ('R and B');
INSERT INTO generos_musicales (genero) VALUES ('Flamenco');

INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES ('Wake Up',           'BTS',             'CD',     32.99, 186, 1,  1);
INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES ('Buenas Noches',     'Quevedo',         'CD',     20.99, 128, 3,  4);
INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES ('Thriller',          'Michael Jackson', 'Vinilo', 24.99,  75, 4,  5);
INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES ('Back in Black',     'AC/DC',           'CD',     18.99,  60, 6,  2);
INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES ('The Dark Side',     'Pink Floyd',      'Vinilo', 29.99,  45, 7,  2);
INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES ('Random Access',     'Daft Punk',       'CD',     21.99,  90, 8,  8);
INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES ('Gold Digger',       'Kanye West',      'CD',     19.99, 110, 9,  6);
INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES ('Come Away With Me', 'Norah Jones',     'Vinilo', 22.99,  55, 10, 7);
INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES ('Besos en Guerra',   'Morat',           'CD',     17.99, 200, 2,  5);
INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES ('Motomami',          'Rosalia',         'CD',     23.99, 160, 3, 10);

INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (TO_DATE('2025-01-10','YYYY-MM-DD'), 'Entregado', 65.98,  1, '11111111A');
INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (TO_DATE('2025-01-15','YYYY-MM-DD'), 'Entregado', 20.99,  2, '22222222B');
INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (TO_DATE('2025-01-20','YYYY-MM-DD'), 'Enviado',   54.98,  3, '33333333C');
INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (TO_DATE('2025-02-01','YYYY-MM-DD'), 'Pendiente', 24.99,  4, '44444444D');
INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (TO_DATE('2025-02-05','YYYY-MM-DD'), 'Entregado', 37.98,  5, '55555555E');
INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (TO_DATE('2025-02-10','YYYY-MM-DD'), 'Enviado',   29.99,  6, '66666666F');
INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (TO_DATE('2025-02-14','YYYY-MM-DD'), 'Pendiente', 21.99,  7, '77777777G');
INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (TO_DATE('2025-02-20','YYYY-MM-DD'), 'Entregado', 42.98,  8, '88888888H');
INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (TO_DATE('2025-03-01','YYYY-MM-DD'), 'Enviado',   19.99,  9, '99999999I');
INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (TO_DATE('2025-03-05','YYYY-MM-DD'), 'Pendiente', 46.98, 10, '10101010J');

INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (1,  1,  2);
INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (2,  2,  1);
INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (3,  3,  2);
INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (4,  4,  1);
INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (5,  5,  1);
INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (6,  6,  1);
INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (7,  7,  1);
INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (8,  8,  1);
INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (9,  9,  1);
INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (10, 10, 2);

COMMIT;
