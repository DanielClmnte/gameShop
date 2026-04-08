-- ==============================================
-- Base de Datos: Tienda de Videojuegos
-- Proyecto: GameShop - 1º DAM
-- Autor: Daniel Clemente Gomez / Ángel Pozo
-- ==============================================

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS gameshop_db;
USE gameshop_db;

-- ==============================================
-- Tabla: plataformas
-- Descripción: Almacena las plataformas disponibles (PS5, Xbox, PC, etc.)
-- ==============================================
CREATE TABLE plataformas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- Tabla: usuarios
-- Descripción: Almacena la información de usuarios y administradores
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
);

-- ==============================================
-- Tabla: videojuegos
-- Descripción: Catálogo de videojuegos disponibles en la tienda
-- ==============================================
CREATE TABLE videojuegos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    plataforma_id INT NOT NULL,
    imagen VARCHAR(255),
    fecha_lanzamiento DATE,
    desarrollador VARCHAR(150),
    calificacion DECIMAL(3, 2) DEFAULT 0.00,
    disponible BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plataforma_id) REFERENCES plataformas(id)
);

-- ==============================================
-- Tabla: opiniones
-- Descripción: Reseñas y comentarios de usuarios sobre videojuegos
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
);

-- ==============================================
-- Tabla: carrito_items
-- Descripción: Items del carrito de compras (relación muchos-a-muchos)
-- ==============================================
CREATE TABLE carrito_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    videojuego_id INT NOT NULL,
    usuario_id INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (videojuego_id) REFERENCES videojuegos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- ==============================================
-- Tabla: pedidos
-- Descripción: Historial de compras de los usuarios
-- ==============================================
CREATE TABLE pedidos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    estado ENUM('PENDIENTE', 'CONFIRMADO', 'ENVIADO', 'ENTREGADO', 'CANCELADO') DEFAULT 'PENDIENTE',
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- ==============================================
-- Tabla: detalle_pedidos
-- Descripción: Detalles de los productos en cada pedido
-- ==============================================
CREATE TABLE detalle_pedidos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT NOT NULL,
    videojuego_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (videojuego_id) REFERENCES videojuegos(id)
);

-- ==============================================
-- DATOS DE PRUEBA
-- ==============================================

-- Insertar plataformas
INSERT INTO plataformas (nombre, descripcion) VALUES
('PlayStation 5', 'Consola de próxima generación de Sony'),
('Xbox Series X', 'Consola de próxima generación de Microsoft'),
('PC', 'Videojuegos para computadora');

-- Insertar usuarios de prueba
INSERT INTO usuarios (nombre, email, contrasena, telefono, rol) VALUES
('Admin GameShop', 'admin@gameshop.com', 'admin123', '666111222', 'ADMIN'),
('Juan Pérez', 'juan@example.com', 'pass123', '666222333', 'CLIENTE'),
('María García', 'maria@example.com', 'pass456', '666333444', 'CLIENTE');

-- Insertar videojuegos de ejemplo (PS5)
INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, desarrollador, fecha_lanzamiento) VALUES
('Elden Ring', 'Juego de rol de acción épico en un mundo abierto', 49.99, 15, 1, 'FromSoftware', '2022-02-25'),
('Final Fantasy XVI', 'Épica conclusión de la saga Final Fantasy', 59.99, 10, 1, 'Square Enix', '2023-06-22'),
('Spider-Man 2', 'Aventura de Spider-Man en Nueva York', 69.99, 20, 1, 'Insomniac Games', '2023-10-20');

-- Insertar videojuegos de ejemplo (Xbox)
INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, desarrollador, fecha_lanzamiento) VALUES
('Halo Infinite', 'Nueva entrega de la icónica saga Halo', 59.99, 12, 2, 'Bungie', '2021-12-08'),
('Starfield', 'RPG de ciencia ficción de Bethesda', 69.99, 8, 2, 'Bethesda Game Studios', '2023-09-06');

-- Insertar videojuegos de ejemplo (PC)
INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, desarrollador, fecha_lanzamiento) VALUES
('Baldur\'s Gate 3', 'RPG de fantasía épico', 59.99, 25, 3, 'Larian Studios', '2023-08-03'),
('Cyberpunk 2077', 'RPG de ciencia ficción en una megaciudad', 39.99, 18, 3, 'CD Projekt Red', '2020-12-10'),
('The Witcher 3', 'Aventura de fantasía con Geralt de Rivia', 29.99, 30, 3, 'CD Projekt Red', '2015-05-19');

-- Crear índices para optimizar búsquedas
CREATE INDEX idx_videojuegos_plataforma ON videojuegos(plataforma_id);
CREATE INDEX idx_videojuegos_titulo ON videojuegos(titulo);
CREATE INDEX idx_opiniones_videojuego ON opiniones(videojuego_id);
CREATE INDEX idx_opiniones_usuario ON opiniones(usuario_id);
CREATE INDEX idx_carrito_usuario ON carrito_items(usuario_id);
CREATE INDEX idx_pedidos_usuario ON pedidos(usuario_id);
CREATE INDEX idx_detalle_pedidos_pedido ON detalle_pedidos(pedido_id);

-- ==============================================
-- FIN DEL SCRIPT
-- ==============================================

