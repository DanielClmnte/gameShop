-- ==============================================
-- Datos de Prueba: GameShop
-- Proyecto: GameShop - 1 DAM
-- Autor: Daniel Clemente Gomez
-- Archivo: data.sql - Datos iniciales (INSERT)
-- Requisito: ejecutar INI_DB.sql primero
-- ==============================================

USE gameshop_db;

-- ==============================================
-- Plataformas (IDs 1, 2, 3)
-- ==============================================
INSERT INTO plataformas (nombre, descripcion) VALUES
('PlayStation 5', 'Consola de proxima generacion de Sony'),
('Xbox Series X', 'Consola de proxima generacion de Microsoft'),
('PC',            'Videojuegos para computadora');

-- ==============================================
-- Usuarios (contrasenas en texto plano - SOLO DESARROLLO)
-- Admin: login con nombre 'admin' y contrasena 'admin'
-- ==============================================
INSERT INTO usuarios (nombre, email, contrasena, telefono, rol) VALUES
('admin',        'admin@gameshop.com', 'admin',   '666111222', 'ADMIN'),
('Juan Perez',   'juan@example.com',   'pass123', '666222333', 'CLIENTE'),
('Maria Garcia', 'maria@example.com',  'pass456', '666333444', 'CLIENTE');

-- ==============================================
-- Videojuegos PlayStation 5 (plataforma_id = 1)
-- ==============================================
INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, desarrollador, fecha_lanzamiento, calificacion) VALUES
('Elden Ring',        'Juego de rol de accion epico en un mundo abierto', 49.99, 15, 1, '/uploads/images/elden-ring.jpg',  'FromSoftware',    '2022-02-25', 4.80),
('Final Fantasy XVI', 'Epica conclusion de la saga Final Fantasy',         59.99, 10, 1, '/uploads/images/ff16.jpg',         'Square Enix',     '2023-06-22', 4.60),
('Spider-Man 2',      'Aventura de Spider-Man en Nueva York',              69.99, 20, 1, '/uploads/images/spiderman2.jpg',   'Insomniac Games', '2023-10-20', 4.70);

-- ==============================================
-- Videojuegos Xbox Series X (plataforma_id = 2)
-- ==============================================
INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, desarrollador, fecha_lanzamiento, calificacion) VALUES
('Halo Infinite', 'Nueva entrega de la iconica saga Halo', 59.99, 12, 2, '/uploads/images/halo-infinite.jpg', 'Bungie',                '2021-12-08', 4.40),
('Starfield',     'RPG de ciencia ficcion de Bethesda',     69.99,  8, 2, '/uploads/images/starfield.jpg',     'Bethesda Game Studios', '2023-09-06', 4.50);

-- ==============================================
-- Videojuegos PC (plataforma_id = 3)
-- ==============================================
INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, desarrollador, fecha_lanzamiento, calificacion) VALUES
("Baldur's Gate 3", 'RPG de fantasia epico',                    59.99, 25, 3, '/uploads/images/baldurs-gate3.jpg', 'Larian Studios', '2023-08-03', 4.90),
('Cyberpunk 2077',  'RPG de ciencia ficcion en una megaciudad', 39.99, 18, 3, '/uploads/images/cyberpunk2077.jpg', 'CD Projekt Red', '2020-12-10', 4.20),
('The Witcher 3',   'Aventura de fantasia con Geralt de Rivia', 29.99, 30, 3, '/uploads/images/witcher3.jpg',      'CD Projekt Red', '2015-05-19', 4.80);

-- ==============================================
-- FIN DE LOS DATOS
-- ==============================================

