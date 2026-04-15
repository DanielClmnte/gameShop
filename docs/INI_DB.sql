-- ==============================================
-- Base de Datos: Tienda de Videojuegos
-- Proyecto: GameShop - 1 DAM
-- Autor: Daniel Clemente Gomez
-- Archivo: INI_DB.sql - Esquema de la base de datos
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
DROP TABLE IF EXISTS favoritos;
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
-- imagen: URL completa (ej: /uploads/images/elden-ring.jpg)
-- disponible: TRUE cuando stock > 0
-- ==============================================
CREATE TABLE videojuegos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    plataforma_id INT NOT NULL,
    imagen VARCHAR(255) DEFAULT '/uploads/images/sin-imagen.jpg',
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
-- SIN FK en usuario_id para permitir carritos de usuarios anonimos
-- (los anonimos usan IDs negativos temporales por sesion)
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
    FOREIGN KEY (videojuego_id) REFERENCES videojuegos(id)
    -- Sin FK a usuarios: permite IDs negativos para usuarios anonimos
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================================
-- Tabla: favoritos (lista de deseados)
-- Solo para usuarios registrados
-- ==============================================
CREATE TABLE favoritos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    videojuego_id INT NOT NULL,
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_usuario_videojuego (usuario_id, videojuego_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (videojuego_id) REFERENCES videojuegos(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==============================================
-- Tabla: pedidos
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
CREATE INDEX idx_favoritos_usuario       ON favoritos(usuario_id);
CREATE INDEX idx_favoritos_videojuego    ON favoritos(videojuego_id);
CREATE INDEX idx_pedidos_usuario         ON pedidos(usuario_id);
CREATE INDEX idx_pedidos_numero          ON pedidos(numero_pedido);
CREATE INDEX idx_detalle_pedido          ON detalle_pedidos(pedido_id);

-- Reactivar comprobacion de claves foraneas
SET FOREIGN_KEY_CHECKS = 1;

-- ==============================================
-- FIN DEL ESQUEMA
-- Ejecutar data.sql a continuacion para insertar datos de prueba
-- ==============================================

