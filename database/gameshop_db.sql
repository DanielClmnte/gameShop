-- ==============================================
-- Base de Datos: Tienda de Videojuegos
-- Proyecto: GameShop - 1 DAM
-- Autor: Daniel Clemente Gomez / Angel Pozo
-- Version: 2.0 - Actualizada para coincidir con modelos Java
-- ==============================================

-- Codificacion UTF-8 para caracteres especiales
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS gameshop_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE gameshop_db;

-- Desactivar comprobacion de claves foraneas para importar sin problemas
SET FOREIGN_KEY_CHECKS = 0;

-- ==============================================
-- Eliminar tablas si existen (para reimportar limpio)
-- ==============================================
DROP TABLE IF EXISTS detalle_pedidos;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS carrito_items;
DROP TABLE IF EXISTS opiniones;
DROP TABLE IF EXISTS videojuegos;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS plataformas;

-- ==============================================
-- Tabla: plataformas
-- IDs fijos: 1=PlayStation 5, 2=Xbox Series X, 3=PC
-- ==============================================
CREATE TABLE plataformas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================================
-- Tabla: usuarios
-- rol: 'CLIENTE' o 'ADMIN' (igual que Usuario.rol en Java)
-- contrasenas en texto plano - SOLO PARA DESARROLLO
-- ==============================================
CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    ciudad VARCHAR(100),
    codigo_postal VARCHAR(10),
    rol ENUM('CLIENTE', 'ADMIN') DEFAULT 'CLIENTE',
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================================
-- Tabla: videojuegos
-- imagen: nombre de archivo (ej: elden-ring.jpg)
--         se sirve desde /uploads/images/{imagen}
-- disponible: TRUE cuando stock > 0
-- ==============================================
CREATE TABLE videojuegos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    plataforma_id INT NOT NULL,
    imagen VARCHAR(255) DEFAULT 'sin-imagen.jpg',
    fecha_lanzamiento DATE,
    desarrollador VARCHAR(150),
    calificacion DECIMAL(3, 2) DEFAULT 0.00,
    disponible BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plataforma_id) REFERENCES plataformas(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================================
-- Tabla: opiniones
-- calificacion: 1 a 5 estrellas
-- ==============================================
CREATE TABLE opiniones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    videojuego_id INT NOT NULL,
    usuario_id INT NOT NULL,
    calificacion INT NOT NULL CHECK (calificacion >= 1 AND calificacion <= 5),
    comentario TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verificado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (videojuego_id) REFERENCES videojuegos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================================
-- Tabla: carrito_items
-- titulo_videojuego e imagen_videojuego: snapshot al agregar
-- Coincide con CarritoItem.java
-- ==============================================
CREATE TABLE carrito_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    videojuego_id INT NOT NULL,
    usuario_id INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    titulo_videojuego VARCHAR(150),
    imagen_videojuego VARCHAR(255),
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (videojuego_id) REFERENCES videojuegos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================================
-- Tabla: pedidos
-- Alineada con Pedido.java:
--   numero_pedido       = Pedido.numeroPedido      (6 digitos visibles)
--   nombre_completo     = Pedido.nombreCompleto
--   subtotal            = Pedido.subtotal
--   impuestos           = Pedido.impuestos         (21% IVA)
--   total_con_impuestos = Pedido.totalConImpuestos
--   estado              = Pedido.estado            PROCESANDO|ENVIADO|ENTREGADO
--   fecha_entrega_estimada = Pedido.fechaEntregaEstimada
-- ==============================================
CREATE TABLE pedidos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero_pedido INT NOT NULL UNIQUE,
    usuario_id INT NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    email VARCHAR(100) NOT NULL,
    direccion VARCHAR(255),
    ciudad VARCHAR(100),
    codigo_postal VARCHAR(10),
    subtotal DECIMAL(10, 2) NOT NULL,
    impuestos DECIMAL(10, 2) NOT NULL,
    total_con_impuestos DECIMAL(10, 2) NOT NULL,
    estado ENUM('PROCESANDO', 'ENVIADO', 'ENTREGADO') DEFAULT 'PROCESANDO',
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega_estimada DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================================
-- Tabla: detalle_pedidos
-- titulo_videojuego e imagen_videojuego: snapshot al comprar
-- subtotal: columna calculada automaticamente
-- ==============================================
CREATE TABLE detalle_pedidos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT NOT NULL,
    videojuego_id INT NOT NULL,
    titulo_videojuego VARCHAR(150) NOT NULL,
    imagen_videojuego VARCHAR(255),
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (videojuego_id) REFERENCES videojuegos(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================================
-- INDICES para optimizar busquedas frecuentes
-- ==============================================
CREATE INDEX idx_videojuegos_plataforma  ON videojuegos(plataforma_id);
CREATE INDEX idx_videojuegos_titulo      ON videojuegos(titulo);
CREATE INDEX idx_videojuegos_disponible  ON videojuegos(disponible);
CREATE INDEX idx_opiniones_videojuego    ON opiniones(videojuego_id);
CREATE INDEX idx_opiniones_usuario       ON opiniones(usuario_id);
CREATE INDEX idx_carrito_usuario         ON carrito_items(usuario_id);
CREATE INDEX idx_pedidos_usuario         ON pedidos(usuario_id);
CREATE INDEX idx_pedidos_numero          ON pedidos(numero_pedido);
CREATE INDEX idx_detalle_pedido          ON detalle_pedidos(pedido_id);

-- ==============================================
-- DATOS DE PRUEBA
-- ==============================================

-- Plataformas (IDs 1, 2, 3 deben coincidir con PlataformaRepository.java)
INSERT INTO plataformas (nombre, descripcion) VALUES
('PlayStation 5', 'Consola de proxima generacion de Sony'),
('Xbox Series X', 'Consola de proxima generacion de Microsoft'),
('PC',            'Videojuegos para computadora');

-- Usuarios (contrasenas en texto plano - SOLO DESARROLLO)
INSERT INTO usuarios (nombre, email, contrasena, telefono, rol) VALUES
('Admin GameShop', 'admin@gameshop.com', 'admin123', '666111222', 'ADMIN'),
('Juan Perez',     'juan@example.com',   'pass123',  '666222333', 'CLIENTE'),
('Maria Garcia',   'maria@example.com',  'pass456',  '666333444', 'CLIENTE');

-- Videojuegos PlayStation 5 (plataforma_id = 1)
INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, desarrollador, fecha_lanzamiento, calificacion) VALUES
('Elden Ring',        'Juego de rol de accion epico en un mundo abierto', 49.99, 15, 1, 'elden-ring.jpg',  'FromSoftware',    '2022-02-25', 4.80),
('Final Fantasy XVI', 'Epica conclusion de la saga Final Fantasy',         59.99, 10, 1, 'ff16.jpg',         'Square Enix',     '2023-06-22', 4.60),
('Spider-Man 2',      'Aventura de Spider-Man en Nueva York',              69.99, 20, 1, 'spiderman2.jpg',   'Insomniac Games', '2023-10-20', 4.70);

-- Videojuegos Xbox Series X (plataforma_id = 2)
INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, desarrollador, fecha_lanzamiento, calificacion) VALUES
('Halo Infinite', 'Nueva entrega de la iconica saga Halo', 59.99, 12, 2, 'halo-infinite.jpg', 'Bungie',                '2021-12-08', 4.40),
('Starfield',     'RPG de ciencia ficcion de Bethesda',     69.99,  8, 2, 'starfield.jpg',     'Bethesda Game Studios', '2023-09-06', 4.50);

-- Videojuegos PC (plataforma_id = 3)
INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, desarrollador, fecha_lanzamiento, calificacion) VALUES
("Baldur's Gate 3", 'RPG de fantasia epico',                    59.99, 25, 3, 'baldurs-gate3.jpg', 'Larian Studios', '2023-08-03', 4.90),
('Cyberpunk 2077',  'RPG de ciencia ficcion en una megaciudad', 39.99, 18, 3, 'cyberpunk2077.jpg', 'CD Projekt Red', '2020-12-10', 4.20),
('The Witcher 3',   'Aventura de fantasia con Geralt de Rivia', 29.99, 30, 3, 'witcher3.jpg',      'CD Projekt Red', '2015-05-19', 4.80);

-- Reactivar comprobacion de claves foraneas
SET FOREIGN_KEY_CHECKS = 1;

-- ==============================================
-- FIN DEL SCRIPT
-- ==============================================
