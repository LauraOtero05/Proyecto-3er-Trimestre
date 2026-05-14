USE 74_minutes;

INSERT INTO Cliente(nombre, apellido, calle, codigo_postal, email, password_hash) VALUES
('Carlos',    'Garcia',     'Calle Mayor, 12',           28001, 'carlos.garcia@gmail.com',     'hash001'),
('Laura',     'Martinez',   'Calle Gran Via, 34',        28013, 'laura.martinez@gmail.com',    'hash002'),
('Pedro',     'Fernandez',  'Calle del Carmen, 27',      30204, 'pedro.fernandez@hotmail.com', 'hash003'),
('Lorena',    'Lopez',      'Calle nueva, 1',            28462, 'lorena.lopez@hotmail.com',    'hash004'),
('Miguel',    'Sanchez',    'Avenida de la Paz, 5',      41001, 'miguel.sanchez@yahoo.com',    'hash005'),
('Sofia',     'Ruiz',       'Calle Serrano, 8',          28006, 'sofia.ruiz@gmail.com',        'hash006'),
('Andres',    'Torres',     'Calle Alcala, 45',          28009, 'andres.torres@gmail.com',     'hash007'),
('Maria',     'Diaz',       'Paseo de la Castellana, 3', 28046, 'maria.diaz@hotmail.com',      'hash008'),
('Pablo',     'Moreno',     'Calle Fuencarral, 22',      28004, 'pablo.moreno@yahoo.com',      'hash009'),
('Elena',     'Jimenez',    'Calle Goya, 17',            28001, 'elena.jimenez@gmail.com',     'hash010');

INSERT INTO TELEFONO_CLIENTE(id_cliente, Telefono) VALUES
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

INSERT INTO Trabajador(DNI, nombre, apellido, rol, email, password_hash) VALUES
('11111111A', 'Maria',   'Rodriguez', 'Almacen',  'maria.rodriguez@tienda.com',  'whash001'),
('22222222B', 'Cesar',   'Martin',    'Ventas',   'cesar.martin@tienda.com',     'whash002'),
('33333333C', 'Ana',     'Gomez',     'Almacen',  'ana.gomez@tienda.com',        'whash003'),
('44444444D', 'Luis',    'Perez',     'Ventas',   'luis.perez@tienda.com',       'whash004'),
('55555555E', 'Isabel',  'Navarro',   'Gerencia', 'isabel.navarro@tienda.com',   'whash005'),
('66666666F', 'Jorge',   'Castillo',  'Almacen',  'jorge.castillo@tienda.com',   'whash006'),
('77777777G', 'Marta',   'Gil',       'Ventas',   'marta.gil@tienda.com',        'whash007'),
('88888888H', 'Raul',    'Herrera',   'Almacen',  'raul.herrera@tienda.com',     'whash008'),
('99999999I', 'Carmen',  'Santos',    'Ventas',   'carmen.santos@tienda.com',    'whash009'),
('10101010J', 'Antonio', 'Flores',    'Gerencia', 'antonio.flores@tienda.com',   'whash010');

INSERT INTO TELEFONO_TRABAJADOR(DNI, Telefono) VALUES
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

INSERT INTO Proveedor(nombre, calle, codigo_postal) VALUES
('HYBE Labels',         'Calle del Prado, 5',           28904),
('Columbia Records',    'Calle Alcala, 17',             28740),
('Rimas Entertainment', 'Calle Bravo Murillo, 200',     28936),
('Universal Music',     'Calle Velazquez, 50',          28001),
('Sony Music',          'Avenida de Europa, 12',        28108),
('Warner Music',        'Calle Orense, 4',              28020),
('EMI Records',         'Paseo de Recoletos, 7',        28004),
('Atlantic Records',    'Calle Jorge Juan, 30',         28001),
('Interscope Records',  'Avenida del Mediterraneo, 3',  46010),
('Republic Records',    'Calle Colon, 15',              46004);

INSERT INTO TELEFONO_PROVEEDOR(id_proveedor, Telefono) VALUES
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

INSERT INTO EMAIL_PROVEEDOR(id_proveedor, email) VALUES
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

INSERT INTO genero_musical(genero) VALUES
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

INSERT INTO Album(titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES
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

INSERT INTO Pedidos(fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES
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

INSERT INTO DETALLE_PEDIDO(id_pedido, id_producto, cantidad) VALUES
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