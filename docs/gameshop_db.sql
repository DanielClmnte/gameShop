-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:8889
-- Tiempo de generación: 25-04-2026 a las 11:31:40
-- Versión del servidor: 8.0.44
-- Versión de PHP: 8.3.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gameshop_db`
--

CREATE DATABASE IF NOT EXISTS `gameshop_db`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE `gameshop_db`;

SET FOREIGN_KEY_CHECKS = 0;

-- Eliminar tablas si existen (para reimportacion limpia)
DROP TABLE IF EXISTS `detalle_pedidos`;
DROP TABLE IF EXISTS `pedidos`;
DROP TABLE IF EXISTS `favoritos`;
DROP TABLE IF EXISTS `carrito_items`;
DROP TABLE IF EXISTS `opiniones`;
DROP TABLE IF EXISTS `direcciones_usuario`;
DROP TABLE IF EXISTS `metodos_pago`;
DROP TABLE IF EXISTS `videojuegos`;
DROP TABLE IF EXISTS `usuarios`;
DROP TABLE IF EXISTS `plataformas`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `carrito_items`
--

CREATE TABLE `carrito_items` (
  `id` int NOT NULL,
  `videojuego_id` int NOT NULL,
  `usuario_id` int NOT NULL,
  `cantidad` int NOT NULL DEFAULT '1',
  `precio_unitario` decimal(10,2) NOT NULL,
  `titulo_videojuego` varchar(150) DEFAULT NULL,
  `imagen_videojuego` varchar(255) DEFAULT NULL,
  `fecha_agregado` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `carrito_items`
--

INSERT INTO `carrito_items` (`id`, `videojuego_id`, `usuario_id`, `cantidad`, `precio_unitario`, `titulo_videojuego`, `imagen_videojuego`, `fecha_agregado`) VALUES
(19, 3, 5, 1, 49.99, 'Final Fantasy XVI', '/uploads/images/33e1b76d-final_fantasy_16.png', '2026-04-23 18:49:42');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_pedidos`
--

CREATE TABLE `detalle_pedidos` (
  `id` int NOT NULL,
  `pedido_id` int NOT NULL,
  `videojuego_id` int NOT NULL,
  `titulo_videojuego` varchar(150) NOT NULL,
  `imagen_videojuego` varchar(255) DEFAULT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) GENERATED ALWAYS AS ((`cantidad` * `precio_unitario`)) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `detalle_pedidos`
--

INSERT INTO `detalle_pedidos` (`id`, `pedido_id`, `videojuego_id`, `titulo_videojuego`, `imagen_videojuego`, `cantidad`, `precio_unitario`) VALUES
(1, 1, 67, 'Slime Rancher 2', '/uploads/images/cc8d8d8d-slime_rancher_2.webp', 1, 29.99),
(2, 2, 5, 'Dragon\'s Dogma 2', '/uploads/images/2357bab8-dragon_dog_ma_2.jpg.webp', 2, 59.99),
(3, 2, 69, 'Terraria', '/uploads/images/dcf4f7d1-terraria.jpg', 1, 9.99),
(4, 3, 69, 'Terraria', '/uploads/images/dcf4f7d1-terraria.jpg', 1, 9.99),
(5, 4, 69, 'Terraria', '/uploads/images/dcf4f7d1-terraria.jpg', 1, 9.99),
(6, 4, 70, 'Valheim', '/uploads/images/964643af-valheim.jpg', 1, 19.99),
(7, 5, 3, 'Final Fantasy XVI', '/uploads/images/33e1b76d-final_fantasy_16.png', 99, 49.99),
(8, 6, 3, 'Final Fantasy XVI', '/uploads/images/33e1b76d-final_fantasy_16.png', 99, 49.99),
(9, 7, 36, 'World of Warcraft: Midnight', '/uploads/images/210dff8c-warcraft_midnight.jpg.avif', 1, 49.99),
(10, 8, 70, 'Valheim', '/uploads/images/964643af-valheim.jpg', 1, 19.99),
(11, 8, 5, 'Dragon\'s Dogma 2', '/uploads/images/2357bab8-dragon_dog_ma_2.jpg.webp', 1, 59.99),
(12, 9, 67, 'Slime Rancher 2', '/uploads/images/cc8d8d8d-slime_rancher_2.webp', 1, 29.99),
(13, 10, 5, 'Dragon\'s Dogma 2', '/uploads/images/2357bab8-dragon_dog_ma_2.jpg.webp', 1, 59.99),
(14, 11, 5, 'Dragon\'s Dogma 2', '/uploads/images/2357bab8-dragon_dog_ma_2.jpg.webp', 1, 59.99),
(15, 12, 70, 'Valheim', '/uploads/images/964643af-valheim.jpg', 21, 19.99),
(16, 13, 69, 'Terraria', '/uploads/images/dcf4f7d1-terraria.jpg', 1, 9.99),
(17, 14, 5, 'Dragon\'s Dogma 2', '/uploads/images/2357bab8-dragon_dog_ma_2.jpg.webp', 1, 59.99);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `direcciones_usuario`
--

CREATE TABLE `direcciones_usuario` (
  `id` int NOT NULL,
  `usuario_id` int NOT NULL,
  `alias` varchar(50) NOT NULL DEFAULT 'Mi dirección',
  `nombre` varchar(100) NOT NULL,
  `direccion` varchar(255) NOT NULL,
  `ciudad` varchar(100) NOT NULL,
  `codigo_postal` varchar(5) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `es_principal` tinyint(1) DEFAULT '0',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `direcciones_usuario`
--

INSERT INTO `direcciones_usuario` (`id`, `usuario_id`, `alias`, `nombre`, `direccion`, `ciudad`, `codigo_postal`, `telefono`, `es_principal`, `fecha_creacion`) VALUES
(1, 2, 'Casa', 'Juan Perez', 'Calle Gran Via 10 3A', 'Madrid', '28013', '666222333', 1, '2026-04-22 21:54:09'),
(2, 5, 'Casa', 'Daniel clemente', 'Calle mayor', 'Madrid', '28012', '63243521', 1, '2026-04-23 18:49:07'),
(3, 2, 'Trabajo', 'Juan Perez', 'Calle san marcelo', 'Madrid', '28001', '612345678', 0, '2026-04-24 13:31:23'),
(4, 1, 'casa del admin', 'daniel clemente', 'Calle mayor', 'Madrid', '28001', '612345678', 1, '2026-04-24 14:16:54');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `favoritos`
--

CREATE TABLE `favoritos` (
  `id` int NOT NULL,
  `usuario_id` int NOT NULL,
  `videojuego_id` int NOT NULL,
  `fecha_agregado` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `favoritos`
--

INSERT INTO `favoritos` (`id`, `usuario_id`, `videojuego_id`, `fecha_agregado`) VALUES
(2, 2, 69, '2026-04-17 13:26:09'),
(3, 1, 69, '2026-04-22 14:36:15'),
(4, 3, 69, '2026-04-22 15:22:53'),
(5, 1, 5, '2026-04-24 14:15:16'),
(6, 1, 70, '2026-04-24 14:19:13');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `metodos_pago`
--

CREATE TABLE `metodos_pago` (
  `id` int NOT NULL,
  `usuario_id` int NOT NULL,
  `alias` varchar(50) NOT NULL DEFAULT 'Mi tarjeta',
  `tipo` varchar(20) NOT NULL DEFAULT 'VISA',
  `ultimos4` char(4) NOT NULL,
  `titular` varchar(100) NOT NULL,
  `mes_expiracion` char(2) NOT NULL,
  `anio_expiracion` char(4) NOT NULL,
  `es_principal` tinyint(1) DEFAULT '0',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `metodos_pago`
--

INSERT INTO `metodos_pago` (`id`, `usuario_id`, `alias`, `tipo`, `ultimos4`, `titular`, `mes_expiracion`, `anio_expiracion`, `es_principal`, `fecha_creacion`) VALUES
(1, 2, 'Visa Personal', 'VISA', '3456', 'JUAN PEREZ', '12', '2028', 1, '2026-04-22 21:54:09'),
(2, 5, 'vista visa', 'VISA', '0000', 'DANIEL CLEMENTE', '12', '2029', 1, '2026-04-23 18:49:27');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `opiniones`
--

CREATE TABLE `opiniones` (
  `id` int NOT NULL,
  `videojuego_id` int NOT NULL,
  `usuario_id` int NOT NULL,
  `calificacion` int NOT NULL,
  `comentario` text,
  `fecha` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `verificado` tinyint(1) DEFAULT '0'
) ;

--
-- Volcado de datos para la tabla `opiniones`
--

INSERT INTO `opiniones` (`id`, `videojuego_id`, `usuario_id`, `calificacion`, `comentario`, `fecha`, `verificado`) VALUES
(1, 57, 2, 3, 'CUBITOS', '2026-04-17 13:31:29', 0),
(2, 57, 2, 5, 'nunca llego', '2026-04-17 13:32:12', 0),
(3, 57, 2, 5, 'nunca llego\r\n', '2026-04-17 13:32:33', 0),
(4, 57, 2, 1, 'nuna llego :V', '2026-04-17 13:33:04', 0),
(5, 69, 2, 3, 'nn', '2026-04-17 15:25:33', 0),
(6, 69, 2, 4, 'esta bien xd', '2026-04-23 23:55:52', 0),
(7, 70, 1, 1, 'Esta bugiado el juego no me gusta ', '2026-04-24 14:19:33', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `id` int NOT NULL,
  `numero_pedido` int NOT NULL,
  `usuario_id` int NOT NULL,
  `nombre_completo` varchar(150) NOT NULL,
  `email` varchar(100) NOT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `ciudad` varchar(100) DEFAULT NULL,
  `codigo_postal` varchar(10) DEFAULT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `impuestos` decimal(10,2) NOT NULL,
  `total_con_impuestos` decimal(10,2) NOT NULL,
  `estado` enum('PROCESANDO','ENVIADO','ENTREGADO') DEFAULT 'PROCESANDO',
  `fecha_pedido` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_entrega_estimada` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `pedidos`
--

INSERT INTO `pedidos` (`id`, `numero_pedido`, `usuario_id`, `nombre_completo`, `email`, `direccion`, `ciudad`, `codigo_postal`, `subtotal`, `impuestos`, `total_con_impuestos`, `estado`, `fecha_pedido`, `fecha_entrega_estimada`) VALUES
(1, 297050, 1, 'dani', 'clemente.daniel1212@gmail.com', 'san marcelo 18', 'madrid', '28017', 29.99, 6.30, 36.29, 'PROCESANDO', '2026-04-15 17:03:17', '2026-04-18'),
(2, 922428, 2, 'dsdsd', 'sdsdsd@sffsf.com', 'sdasdas', 'dwwd', 'dwd', 129.97, 27.29, 157.26, 'PROCESANDO', '2026-04-15 17:58:42', '2026-04-18'),
(3, 820888, 2, 'dadaad', 'ccacac@fsfsf.com', 'saADA', 'ASFASFA', 'ASF', 9.99, 2.10, 12.09, 'PROCESANDO', '2026-04-17 13:27:00', '2026-04-20'),
(4, 864352, 2, 'dadad', 'adadada@fff.com', 'cdas', 'adada', 'adada', 29.98, 6.30, 36.28, 'PROCESANDO', '2026-04-21 15:27:44', '2026-04-24'),
(5, 673823, 1, 'admin', 'admin@gameshop.com', 'fasfaf', 'afaf', 'afafaf', 4949.01, 1039.29, 5988.30, 'PROCESANDO', '2026-04-22 14:39:33', '2026-04-25'),
(6, 792240, 2, 'Juan Perez', 'juan@example.com', 'fafsfa', 'faaf', 'afaf', 4949.01, 1039.29, 5988.30, 'PROCESANDO', '2026-04-22 14:41:32', '2026-04-25'),
(7, 962205, 2, 'Juan Perez', 'juan@example.com', 'Calle Gran Via 10 3A', 'Madrid', '28013', 49.99, 10.50, 60.49, 'PROCESANDO', '2026-04-24 11:44:22', '2026-04-27'),
(8, 996203, 2, 'Juan Perez', 'juan@example.com', 'Calle Gran Via 10 3A', 'Madrid', '28013', 79.98, 16.80, 96.78, 'PROCESANDO', '2026-04-24 13:29:56', '2026-04-27'),
(9, 530057, 2, 'Juan Perez', 'juan@example.com', 'Calle san marcelo', 'Madrid', '28001', 29.99, 6.30, 36.29, 'PROCESANDO', '2026-04-24 13:52:10', '2026-04-27'),
(10, 339950, 1, 'admin', 'admin@gameshop.com', 'calle mayor', 'Madrid', '28001', 59.99, 12.60, 72.59, 'PROCESANDO', '2026-04-24 14:03:59', '2026-04-27'),
(11, 152219, 1, 'admin', 'admin@gameshop.com', 'ccalle mayor', 'madrid', '28012', 59.99, 12.60, 72.59, 'PROCESANDO', '2026-04-24 14:15:52', '2026-04-27'),
(12, 335271, 1, 'daniel clemente', 'admin@gameshop.com', 'Calle mayor', 'Madrid', '28001', 419.79, 88.16, 507.95, 'PROCESANDO', '2026-04-24 14:18:55', '2026-04-27'),
(13, 741935, 3, 'Maria Garcia', 'maria@example.com', 'Calle mayor', 'Madrid', '29001', 9.99, 2.10, 12.09, 'PROCESANDO', '2026-04-24 14:25:41', '2026-04-27'),
(14, 690189, 2, 'Juan Perez', 'juan@example.com', 'Calle Gran Via 10 3A', 'Madrid', '28013', 59.99, 12.60, 72.59, 'PROCESANDO', '2026-04-25 10:39:50', '2026-04-28');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `plataformas`
--

CREATE TABLE `plataformas` (
  `id` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `plataformas`
--

INSERT INTO `plataformas` (`id`, `nombre`, `descripcion`, `fecha_creacion`) VALUES
(1, 'PlayStation 5', 'Consola de proxima generacion de Sony', '2026-04-15 16:05:13'),
(2, 'Xbox Series X', 'Consola de proxima generacion de Microsoft', '2026-04-15 16:05:13'),
(3, 'PC', 'Videojuegos para computadora', '2026-04-15 16:05:13');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `ciudad` varchar(100) DEFAULT NULL,
  `codigo_postal` varchar(10) DEFAULT NULL,
  `rol` enum('CLIENTE','ADMIN') DEFAULT 'CLIENTE',
  `activo` tinyint(1) DEFAULT '1',
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre`, `email`, `contrasena`, `telefono`, `direccion`, `ciudad`, `codigo_postal`, `rol`, `activo`, `fecha_registro`) VALUES
(1, 'admin', 'admin@gameshop.com', 'admin', '666111222', 'calle mayor', 'madrid', '', 'ADMIN', 1, '2026-04-15 16:05:13'),
(2, 'Juan Perez', 'juan@example.com', 'pass123', '666222333', 'kjjkkk', '', '', 'CLIENTE', 1, '2026-04-15 16:05:13'),
(3, 'Maria Garcia', 'maria@example.com', 'pass456', '666333444', NULL, NULL, NULL, 'CLIENTE', 1, '2026-04-15 16:05:13'),
(4, 'daniel', 'clemente@gmail.com', '1234', '612345678', '', '', '', 'CLIENTE', 0, '2026-04-23 18:47:52'),
(5, 'daniel', 'dani@gmail.com', '1234', '6543321232', NULL, NULL, NULL, 'CLIENTE', 1, '2026-04-23 18:48:39'),
(6, 'fffff', 'wfsfwf@gmail.com', '1234', '612345678', NULL, NULL, NULL, 'CLIENTE', 1, '2026-04-24 14:03:21');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `videojuegos`
--

CREATE TABLE `videojuegos` (
  `id` int NOT NULL,
  `titulo` varchar(150) NOT NULL,
  `descripcion` text,
  `precio` decimal(10,2) NOT NULL,
  `stock` int NOT NULL DEFAULT '0',
  `plataforma_id` int NOT NULL,
  `imagen` varchar(255) DEFAULT '/uploads/images/sin-imagen.jpg',
  `fecha_lanzamiento` date DEFAULT NULL,
  `desarrollador` varchar(150) DEFAULT NULL,
  `calificacion` decimal(3,2) DEFAULT '0.00',
  `disponible` tinyint(1) DEFAULT '1',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `videojuegos`
--

INSERT INTO `videojuegos` (`id`, `titulo`, `descripcion`, `precio`, `stock`, `plataforma_id`, `imagen`, `fecha_lanzamiento`, `desarrollador`, `calificacion`, `disponible`, `fecha_creacion`) VALUES
(1, 'Elden Ring', 'RPG de accion epico en un vasto mundo abierto creado por Hidetaka Miyazaki y George R.R. Martin', 49.99, 418, 1, '/uploads/images/19e80d40-7b54f5e4-0857-4ce3-8a18-2b8c431e8a9e.jpg', '2022-02-25', 'FromSoftware', 4.80, 1, '2026-04-15 16:05:14'),
(2, 'God of War Ragnarok', 'Kratos y Atreus se enfrentan al Ragnarok nordico en esta epica aventura de accion y narrativa', 39.99, 3, 1, '/uploads/images/5162083c-god_of_war_ragnarok.jpg.webp', '2022-11-09', 'Santa Monica Studio', 4.70, 1, '2026-04-15 16:05:14'),
(3, 'Final Fantasy XVI', 'RPG de accion con una historia epica de cristales y dominios en el mundo de Valisthea', 49.99, 8, 1, '/uploads/images/33e1b76d-final_fantasy_16.png', '2023-06-22', 'Square Enix', 4.50, 1, '2026-04-15 16:05:14'),
(4, 'Marvel\'s Spider-Man 2', 'Peter Parker y Miles Morales unen fuerzas contra Venom en una Nueva York de mundo abierto', 69.99, 20, 1, '/uploads/images/5faaeaea-spiderman_2.jpg', '2023-10-20', 'Insomniac Games', 4.70, 1, '2026-04-15 16:05:14'),
(5, 'Dragon\'s Dogma 2', 'RPG de accion en mundo abierto con un sistema de companeros dinamico y combate espectacular', 59.99, 10, 1, '/uploads/images/2357bab8-dragon_dog_ma_2.jpg.webp', '2024-03-22', 'Capcom', 4.20, 1, '2026-04-15 16:05:14'),
(6, 'Persona 5 Royal', 'RPG japones donde un grupo de estudiantes lucha contra la corrupcion como los Phantom Thieves', 29.99, 22, 1, '/uploads/images/e772ff3c-persona_5_royal.jpeg', '2020-03-31', 'Atlus', 4.80, 1, '2026-04-15 16:05:14'),
(7, 'Sonic Frontiers', 'Sonic explora islas misteriosas en mundo abierto combinando plataformas y combate a alta velocidad', 39.99, 15, 1, '/uploads/images/41b85843-sonic_frontiers.png.avif', '2022-11-08', 'Sonic Team', 3.80, 1, '2026-04-15 16:05:14'),
(8, 'Avatar: Frontiers of Pandora', 'Aventura de accion en primera persona ambientada en Pandora con un nuevo clan Navi por descubrir', 59.99, 14, 1, '/uploads/images/e651f9b7-avatar.jpg', '2023-12-07', 'Massive Entertainment', 3.60, 1, '2026-04-15 16:05:14'),
(9, 'Death Stranding 2: On The Beach', 'Secuela del aclamado juego de Hideo Kojima que continua la historia de Sam Bridges conectando el mundo', 69.99, 8, 1, '/uploads/images/39a7ee98-death_stranding_2.webp', '2025-06-26', 'Kojima Productions', 4.40, 1, '2026-04-15 16:05:14'),
(10, 'Expedition 33', 'RPG de accion y exploracion en un mundo de fantasia inspirado en la pintura impresionista francesa', 69.99, 10, 1, '/uploads/images/6d1ba9b7-expedition_33.webp', '2025-03-13', 'Sandfall Interactive', 4.20, 1, '2026-04-15 16:05:14'),
(11, 'Uncharted 4: A Thiefs End', 'Nathan Drake vuelve para una ultima aventura en busca del tesoro del pirata Henry Avery', 29.99, 25, 1, '/uploads/images/8eb9a21d-uncharted_4.webp', '2016-05-10', 'Naughty Dog', 4.80, 1, '2026-04-15 16:05:14'),
(12, 'Detroit: Become Human', 'Thriller interactivo donde tres androides despiertan conciencia en un Detroit futurista del 2038', 19.99, 18, 1, '/uploads/images/fe3004a8-detroit_become_human.jpg', '2018-05-25', 'Quantic Dream', 4.30, 1, '2026-04-15 16:05:14'),
(13, 'Monster Hunter Wilds', 'La nueva entrega de la saga de caza de monstruos con ecosistemas vivos y combate cooperativo', 69.99, 12, 1, '/uploads/images/e1efda95-monster_hunter_wilds.jpg', '2025-02-28', 'Capcom', 4.40, 1, '2026-04-15 16:05:14'),
(14, 'Resident Evil 4 Remake', 'Remake del clasico de survival horror donde Leon S. Kennedy rescata a la hija del presidente en una aldea espanola', 49.99, 16, 1, '/uploads/images/627a48ba-resident_evil_4.webp', '2023-03-24', 'Capcom', 4.70, 1, '2026-04-15 16:05:14'),
(15, 'Pragmata', 'Aventura de ciencia ficcion ambientada en un futuro distopico donde un astronauta y una nina buscan la verdad', 69.99, 10, 1, '/uploads/images/47a20ff2-pragmata.webp', '2026-07-15', 'Capcom', 4.10, 1, '2026-04-15 16:05:14'),
(16, 'Resident Evil Requiem', 'Nueva entrega de la saga Resident Evil que promete reinventar el survival horror con motor RE Engine', 69.99, 14, 1, '/uploads/images/cefa1197-resident_evil_requiem.jpg', '2026-09-20', 'Capcom', 4.30, 1, '2026-04-15 16:05:14'),
(17, 'Halo Infinite', 'El Jefe Maestro se enfrenta a los Desterrados en una nueva aventura en el anillo Zeta Halo', 39.99, 12, 2, '/uploads/images/a3749542-halo_infinite.jpeg.webp', '2021-12-08', '343 Industries', 4.00, 1, '2026-04-15 16:05:14'),
(18, 'Starfield', 'RPG de ciencia ficcion espacial donde exploras cientos de planetas en una galaxia inmensa', 59.99, 8, 2, '/uploads/images/89bccebd-starfield.jpg', '2023-09-06', 'Bethesda Game Studios', 3.80, 1, '2026-04-15 16:05:14'),
(19, 'Forza Horizon 6', 'Proxima entrega de la aclamada saga de carreras en mundo abierto con graficos de nueva generacion', 69.99, 12, 2, '/uploads/images/b26beaeb-forza_horizon_6.webp', '2026-08-15', 'Playground Games', 4.50, 1, '2026-04-15 16:05:14'),
(20, 'Crimson Desert', 'RPG de accion y aventura en mundo abierto con combate visceral ambientado en un continente en guerra', 59.99, 14, 2, '/uploads/images/5aed885f-crimson_desert.jpg.webp', '2026-03-28', 'Pearl Abyss', 4.20, 1, '2026-04-15 16:05:14'),
(21, 'Cyberpunk 2077', 'RPG de accion en primera persona en Night City, una megaciudad futurista obsesionada con el poder y la tecnologia', 29.99, 22, 2, '/uploads/images/63923b14-cyberpunk_2077.jpg', '2020-12-10', 'CD Projekt Red', 4.50, 1, '2026-04-15 16:05:14'),
(22, 'NBA 2K26', 'Simulador de baloncesto con modos Mi Carrera, Mi Equipo y jugabilidad mejorada con motor de nueva generacion', 69.99, 19, 2, '/uploads/images/4d12a167-2k26.jpeg', '2026-09-05', 'Visual Concepts', 3.80, 1, '2026-04-15 16:05:14'),
(23, 'Borderlands 4', 'Shooter looter cooperativo con millones de armas generadas proceduralmente en un nuevo planeta por explorar', 69.99, 16, 2, '/uploads/images/8093efee-borderlands_4.png.avif', '2025-09-12', 'Gearbox Software', 4.10, 1, '2026-04-15 16:05:14'),
(24, 'Diablo IV', 'RPG de accion oscuro donde los jugadores luchan contra las fuerzas del infierno en el mundo de Santuario', 49.99, 18, 2, '/uploads/images/690c5d20-diabloiv.webp', '2023-06-06', 'Blizzard Entertainment', 4.00, 1, '2026-04-15 16:05:14'),
(25, 'Overcooked! 2', 'Juego cooperativo de cocina caos donde preparas platos en cocinas cada vez mas absurdas con tus amigos', 24.99, 25, 2, '/uploads/images/cc7c4f16-overcooked_2.png.avif', '2018-08-07', 'Ghost Town Games', 4.20, 1, '2026-04-15 16:05:14'),
(26, 'Baldur\'s Gate 3', 'RPG de fantasia epico basado en Dungeons and Dragons con libertad total de elecciones y consecuencias', 59.99, 25, 3, '/uploads/images/223ea56a-baldurs_gate_3.jpg', '2023-08-03', 'Larian Studios', 4.90, 1, '2026-04-15 16:05:14'),
(27, 'Satisfactory', 'Juego de construccion de fabricas en primera persona ambientado en un planeta alienigena', 29.99, 25, 3, '/uploads/images/3671b45f-satisfactory.jpg.webp', '2024-09-10', 'Coffee Stain Studios', 4.50, 1, '2026-04-15 16:05:14'),
(28, 'Cult of the Lamb', 'Gestiona tu propio culto mientras exploras mazmorras roguelike como un adorable cordero poseido', 24.99, 20, 3, '/uploads/images/c0ba99f5-cult_of_the_lamb.jpg', '2022-08-11', 'Massive Monster', 4.30, 1, '2026-04-15 16:05:14'),
(29, 'Crusader Kings III', 'Gran estrategia medieval donde gestionas una dinastia a traves de intrigas politicas y guerras', 39.99, 18, 3, '/uploads/images/4aacf280-crusader_king.jpg.avif', '2020-09-01', 'Paradox Development Studio', 4.60, 1, '2026-04-15 16:05:14'),
(30, 'Euro Truck Simulator 2', 'Simulador de conduccion de camiones por las carreteras de Europa con entregas de mercancias', 19.99, 30, 3, '/uploads/images/1947f90a-eurotruck_simulator_2.jpg.avif', '2012-10-18', 'SCS Software', 4.60, 1, '2026-04-15 16:05:14'),
(31, 'Daemon X Machina: Titanic Scion', 'Secuela del juego de accion con mechas gigantes y combate aereo de alta velocidad', 59.99, 10, 3, '/uploads/images/51c51b76-daemon_x_machina.webp', '2026-06-20', 'Marvelous', 4.10, 1, '2026-04-15 16:05:14'),
(32, 'ARC Raiders', 'Shooter cooperativo en tercera persona donde la humanidad resiste una invasion de maquinas espaciales', 39.99, 10, 3, '/uploads/images/239ace45-arc_raiders.webp', '2026-03-10', 'Embark Studios', 3.90, 1, '2026-04-15 16:05:14'),
(33, 'Cities: Skylines II', 'Simulador de construccion de ciudades con sistemas realistas de economia, transporte y poblacion', 49.99, 15, 3, '/uploads/images/91fd332f-cities_skylines_2.webp', '2023-10-24', 'Colossal Order', 3.50, 1, '2026-04-15 16:05:14'),
(34, 'Blasphemous', 'Metroidvania de accion penitente con arte pixel art gotico ambientado en la tierra de Cvstodia', 24.99, 20, 3, '/uploads/images/87207e4e-blasphemous.png.webp', '2019-09-10', 'The Game Kitchen', 4.40, 1, '2026-04-15 16:05:14'),
(35, 'Total War: Warhammer III', 'Estrategia epica por turnos y batallas en tiempo real en el universo de fantasia de Warhammer', 49.99, 14, 3, '/uploads/images/9e323317-deep_rock_galactic.jpg.webp', '2022-02-17', 'Creative Assembly', 4.20, 1, '2026-04-15 16:05:14'),
(36, 'World of Warcraft: Midnight', 'Nueva expansion del legendario MMORPG que lleva a los jugadores de vuelta a Quelthalas', 49.99, 29, 3, '/uploads/images/210dff8c-warcraft_midnight.jpg.avif', '2026-11-10', 'Blizzard Entertainment', 4.30, 1, '2026-04-15 16:05:14'),
(37, 'The Witcher 3: Wild Hunt', 'RPG de mundo abierto donde Geralt de Rivia busca a su hija adoptiva perseguida por la Caceria Salvaje', 14.99, 35, 3, '/uploads/images/1fe2f96e-the_witcher_3.jpg', '2015-05-19', 'CD Projekt Red', 4.90, 1, '2026-04-15 16:05:14'),
(38, 'Phasmophobia', 'Juego cooperativo de caza de fantasmas donde investigas ubicaciones encantadas con equipamiento paranormal', 13.99, 22, 3, '/uploads/images/6a91751a-phasmophobia.webp', '2023-08-10', 'Kinetic Games', 4.40, 1, '2026-04-15 16:05:14'),
(39, 'RimWorld', 'Simulador de colonias espaciales donde gestionas supervivientes en un planeta lejano con eventos aleatorios', 34.99, 20, 3, '/uploads/images/00af9ee7-rimworld.webp', '2018-10-17', 'Ludeon Studios', 4.80, 1, '2026-04-15 16:05:14'),
(40, 'Golf It!', 'Minigolf multijugador online con editor de mapas donde compites en circuitos creativos y desafiantes', 8.99, 30, 3, '/uploads/images/ee6b4953-golf_it.webp', '2017-02-27', 'Perfuse Entertainment', 4.10, 1, '2026-04-15 16:05:14'),
(41, 'Dragon Quest XI: Ecos de un Pasado Perdido', 'RPG japones clasico donde un joven heroe marcado por el destino recorre el mundo para derrotar la oscuridad', 39.99, 20, 1, '/uploads/images/27833cbd-dragon_quest_xi.png.avif', '2018-09-04', 'Square Enix', 4.70, 1, '2026-04-15 16:05:14'),
(42, 'Helldivers 2', 'Shooter cooperativo en tercera persona donde soldados de la Super Tierra luchan por la democracia galactica', 39.99, 18, 1, '/uploads/images/86ee2d37-helldrivers_2.jpg', '2024-02-08', 'Arrowhead Game Studios', 4.30, 1, '2026-04-15 16:05:14'),
(43, 'Little Nightmares 3', 'Aventura de terror en la que dos ninos atrapados en la Espiral intentan escapar de sus pesadillas', 29.99, 14, 1, '/uploads/images/696a27d3-little_nightmare_3.jpg', '2025-11-18', 'Supermassive Games', 4.20, 1, '2026-04-15 16:05:14'),
(44, 'Dark Souls Remastered', 'Edicion remasterizada del legendario RPG de accion que definio el genero soulslike con su dificultad implacable', 39.99, 15, 1, '/uploads/images/23296bb8-dark_souls_remastered.jpg', '2018-05-25', 'FromSoftware', 4.70, 1, '2026-04-15 16:05:14'),
(45, 'Digimon: Time Strangers', 'RPG de accion donde los jugadores viajan entre eras para salvar el mundo digital junto a sus Digimon', 59.99, 10, 1, '/uploads/images/a5ad0844-digimon_time_straner.jpg', '2026-08-20', 'Bandai Namco', 4.00, 1, '2026-04-15 16:05:14'),
(46, 'No Man\'s Sky', 'Aventura de exploracion espacial procedural con galaxias infinitas, construccion de bases y modo cooperativo', 29.99, 20, 1, '/uploads/images/06745fcd-no_mans_sky.jpg', '2016-08-09', 'Hello Games', 4.30, 1, '2026-04-15 16:05:14'),
(47, 'Blasphemous 2', 'Secuela del metroidvania penitente con nuevas armas, habilidades y un mundo gotico aun mas extenso', 29.99, 18, 1, '/uploads/images/7a4a1c06-blasphemous_2.jpg', '2023-08-24', 'The Game Kitchen', 4.50, 1, '2026-04-15 16:05:14'),
(48, 'Cuphead', 'Juego run and gun con estetica de dibujos animados de los anos 30 y jefes finales desafiantes', 19.99, 22, 2, '/uploads/images/ba6396a6-cuphead.jpg', '2017-09-29', 'Studio MDHR', 4.70, 1, '2026-04-15 16:05:14'),
(49, 'Planet of Lana', 'Aventura cinematografica de puzles donde una nina y su companero felino salvan su mundo de una invasion', 19.99, 16, 2, '/uploads/images/93599751-planet_of_lana.webp', '2023-05-23', 'Wishfully Studios', 4.20, 1, '2026-04-15 16:05:14'),
(50, 'Sea of Stars', 'RPG por turnos inspirado en los clasicos con combate basado en tiempo, exploracion y una historia epica', 34.99, 18, 2, '/uploads/images/6f5e35c2-sea_of_stars.jpg', '2023-08-29', 'Sabotage Studio', 4.50, 1, '2026-04-15 16:05:14'),
(51, 'Dead by Daylight', 'Juego de terror asimetrico multijugador donde cuatro supervivientes escapan de un asesino implacable', 19.99, 25, 2, '/uploads/images/31f8c346-dead_by_deadlight.jpeg', '2016-06-14', 'Behaviour Interactive', 4.00, 1, '2026-04-15 16:05:14'),
(52, 'Slay the Spire 2', 'Roguelike de construccion de mazos con nuevos personajes, cartas y mecanicas estrategicas adictivas', 29.99, 15, 2, '/uploads/images/a2e53f0e-slay_the_spire_2.webp', '2025-04-15', 'Mega Crit', 4.60, 1, '2026-04-15 16:05:14'),
(53, 'Temtem', 'MMO de captura de criaturas inspirado en Pokemon con combate por turnos cooperativo y mundo persistente', 34.99, 14, 2, '/uploads/images/336887f4-temtem.jpg', '2022-09-06', 'Crema Games', 3.80, 1, '2026-04-15 16:05:14'),
(54, 'Bread & Fred', 'Plataformas cooperativo donde dos pinguinos atados por una cuerda escalan una montana helada', 14.99, 20, 2, '/uploads/images/8aae325b-pan_y_federico.jpg', '2024-05-23', 'SandCastles Studio', 4.20, 1, '2026-04-15 16:05:14'),
(55, 'Stardew Valley', 'Simulador de granja donde heredas una parcela y construyes tu vida cultivando, pescando y haciendo amigos', 14.99, 35, 3, '/uploads/images/69d2c0dc-stardew_valley.jpg', '2016-02-26', 'ConcernedApe', 4.90, 1, '2026-04-15 16:05:14'),
(56, 'Undertale', 'RPG indie donde cada monstruo puede ser derrotado o perdonado en un mundo subterraneo lleno de humor y emocion', 9.99, 30, 3, '/uploads/images/c328468d-undertale.jpg', '2015-09-15', 'Toby Fox', 4.80, 1, '2026-04-15 16:05:14'),
(57, 'Minecraft', 'Sandbox de supervivencia y creatividad donde construyes mundos infinitos bloque a bloque', 23.99, 0, 3, '/uploads/images/09569cc2-minecraft.jpg.webp', '2011-11-18', 'Mojang Studios', 4.90, 0, '2026-04-15 16:05:14'),
(58, 'Kingdom Come: Deliverance', 'RPG historico medieval ambientado en Bohemia del siglo XV con combate realista y narrativa inmersiva', 29.99, 18, 3, '/uploads/images/45e911bc-kingdom_come_deliverance.png', '2018-02-13', 'Warhorse Studios', 4.40, 1, '2026-04-15 16:05:14'),
(59, 'PEAK', 'Juego de escalada y parkour en primera persona donde superas montanas imposibles con fisicas realistas', 19.99, 15, 3, '/uploads/images/11db564a-peak.jpg', '2026-02-14', 'Ysbryd Games', 4.10, 1, '2026-04-15 16:05:14'),
(60, 'The Elder Scrolls V: Skyrim', 'RPG de mundo abierto epico donde el Dovahkiin lucha contra dragones en la provincia nordica de Skyrim', 19.99, 30, 3, '/uploads/images/3117d2a5-skyrim.webp', '2011-11-11', 'Bethesda Game Studios', 4.80, 1, '2026-04-15 16:05:14'),
(61, 'Tekken 8', 'Juego de lucha con graficos de nueva generacion, nuevos mecanicas de calor y la saga Mishima en su punto mas intenso', 69.99, 16, 1, '/uploads/images/72fb78d2-tekken_8.jpg', '2024-01-26', 'Bandai Namco', 4.40, 1, '2026-04-15 16:05:14'),
(62, 'The Last of Us Part I', 'Remake del aclamado juego de supervivencia donde Joel y Ellie cruzan unos Estados Unidos devastados por una pandemia', 69.99, 14, 1, '/uploads/images/1608db7a-the_last_of_us_part1.webp', '2022-09-02', 'Naughty Dog', 4.70, 1, '2026-04-15 16:05:14'),
(63, 'The Last of Us Part II', 'Secuela donde Ellie emprende un viaje de venganza en un mundo postapocaliptico lleno de violencia y emociones', 49.99, 16, 1, '/uploads/images/9836a1cb-the_last_of_us_part2.jpg', '2020-06-19', 'Naughty Dog', 4.60, 1, '2026-04-15 16:05:14'),
(64, 'Final Fantasy X/X-2 HD Remaster', 'Remasterizacion en HD de los clasicos RPG de Square Enix con Tidus y Yuna en el mundo de Spira', 29.99, 20, 1, '/uploads/images/9bdac507-final__fantasy_x_x2.jpg', '2019-04-16', 'Square Enix', 4.60, 1, '2026-04-15 16:05:14'),
(65, 'BioShock: The Collection', 'Coleccion completa con BioShock, BioShock 2 y BioShock Infinite remasterizados en una sola edicion', 29.99, 18, 2, '/uploads/images/63fa87da-bioshock_collection.png.avif', '2016-09-13', '2K Games', 4.70, 1, '2026-04-15 16:05:14'),
(66, 'Deep Rock Galactic', 'Shooter cooperativo donde enanos espaciales minan recursos y luchan contra criaturas alienigenas en cuevas procedurales', 29.99, 22, 2, '/uploads/images/c2e466b8-deep_rock_galactic.jpg.webp', '2020-05-13', 'Ghost Ship Games', 4.60, 1, '2026-04-15 16:05:14'),
(67, 'Slime Rancher 2', 'Secuela del simulador de granja de slimes donde Beatrix explora nuevas islas y descubre nuevas especies', 29.99, 16, 3, '/uploads/images/cc8d8d8d-slime_rancher_2.webp', '2024-11-12', 'Monomi Park', 4.30, 1, '2026-04-15 16:05:14'),
(68, 'Slime Rancher', 'Simulador de granja donde capturas y crias adorables slimes en un planeta lejano lleno de color', 19.99, 25, 3, '/uploads/images/4b2c5f91-slime_rancher.webp', '2017-08-01', 'Monomi Park', 4.50, 1, '2026-04-15 16:05:14'),
(69, 'Terraria', 'Aventura sandbox 2D con exploracion, construccion, crafting y combate contra jefes en mundos generados proceduralmente', 9.99, 28, 3, '/uploads/images/dcf4f7d1-terraria.jpg', '2011-05-16', 'Re-Logic', 4.90, 1, '2026-04-15 16:05:14'),
(70, 'Valheim', 'Juego de supervivencia cooperativo vikingo donde exploras un purgatorio mitico y construyes tu legado', 19.99, 0, 3, '/uploads/images/964643af-valheim.jpg', '2021-02-02', 'Iron Gate AB', 4.50, 0, '2026-04-15 16:05:14');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `carrito_items`
--
ALTER TABLE `carrito_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `videojuego_id` (`videojuego_id`),
  ADD KEY `idx_carrito_usuario` (`usuario_id`);

--
-- Indices de la tabla `detalle_pedidos`
--
ALTER TABLE `detalle_pedidos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `videojuego_id` (`videojuego_id`),
  ADD KEY `idx_detalle_pedido` (`pedido_id`);

--
-- Indices de la tabla `direcciones_usuario`
--
ALTER TABLE `direcciones_usuario`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_dir_usuario` (`usuario_id`);

--
-- Indices de la tabla `favoritos`
--
ALTER TABLE `favoritos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_usuario_videojuego` (`usuario_id`,`videojuego_id`),
  ADD KEY `idx_favoritos_usuario` (`usuario_id`),
  ADD KEY `idx_favoritos_videojuego` (`videojuego_id`);

--
-- Indices de la tabla `metodos_pago`
--
ALTER TABLE `metodos_pago`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_pago_usuario` (`usuario_id`);

--
-- Indices de la tabla `opiniones`
--
ALTER TABLE `opiniones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_opiniones_videojuego` (`videojuego_id`),
  ADD KEY `idx_opiniones_usuario` (`usuario_id`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero_pedido` (`numero_pedido`),
  ADD KEY `idx_pedidos_usuario` (`usuario_id`),
  ADD KEY `idx_pedidos_numero` (`numero_pedido`);

--
-- Indices de la tabla `plataformas`
--
ALTER TABLE `plataformas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indices de la tabla `videojuegos`
--
ALTER TABLE `videojuegos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_videojuegos_plataforma` (`plataforma_id`),
  ADD KEY `idx_videojuegos_titulo` (`titulo`),
  ADD KEY `idx_videojuegos_disponible` (`disponible`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `carrito_items`
--
ALTER TABLE `carrito_items`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT de la tabla `detalle_pedidos`
--
ALTER TABLE `detalle_pedidos`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `direcciones_usuario`
--
ALTER TABLE `direcciones_usuario`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `favoritos`
--
ALTER TABLE `favoritos`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `metodos_pago`
--
ALTER TABLE `metodos_pago`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `opiniones`
--
ALTER TABLE `opiniones`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `plataformas`
--
ALTER TABLE `plataformas`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `videojuegos`
--
ALTER TABLE `videojuegos`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=72;

SET FOREIGN_KEY_CHECKS = 1;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `carrito_items`
--
ALTER TABLE `carrito_items`
  ADD CONSTRAINT `carrito_items_ibfk_1` FOREIGN KEY (`videojuego_id`) REFERENCES `videojuegos` (`id`);

--
-- Filtros para la tabla `detalle_pedidos`
--
ALTER TABLE `detalle_pedidos`
  ADD CONSTRAINT `detalle_pedidos_ibfk_1` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`),
  ADD CONSTRAINT `detalle_pedidos_ibfk_2` FOREIGN KEY (`videojuego_id`) REFERENCES `videojuegos` (`id`);

--
-- Filtros para la tabla `direcciones_usuario`
--
ALTER TABLE `direcciones_usuario`
  ADD CONSTRAINT `direcciones_usuario_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `favoritos`
--
ALTER TABLE `favoritos`
  ADD CONSTRAINT `favoritos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `favoritos_ibfk_2` FOREIGN KEY (`videojuego_id`) REFERENCES `videojuegos` (`id`);

--
-- Filtros para la tabla `metodos_pago`
--
ALTER TABLE `metodos_pago`
  ADD CONSTRAINT `metodos_pago_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `opiniones`
--
ALTER TABLE `opiniones`
  ADD CONSTRAINT `opiniones_ibfk_1` FOREIGN KEY (`videojuego_id`) REFERENCES `videojuegos` (`id`),
  ADD CONSTRAINT `opiniones_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `pedidos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `videojuegos`
--
ALTER TABLE `videojuegos`
  ADD CONSTRAINT `videojuegos_ibfk_1` FOREIGN KEY (`plataforma_id`) REFERENCES `plataformas` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
