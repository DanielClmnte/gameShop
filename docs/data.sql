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
-- VIDEOJUEGOS (40 titulos con informacion real)
-- ==============================================

-- ── PlayStation 5 (plataforma_id = 1) ──────────────────────────

INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, desarrollador, fecha_lanzamiento, calificacion) VALUES
('Elden Ring',
 'RPG de accion epico en un vasto mundo abierto creado por Hidetaka Miyazaki y George R.R. Martin',
 49.99, 15, 1, '/uploads/images/elden-ring.jpg', 'FromSoftware', '2022-02-25', 4.80),

('God of War Ragnarok',
 'Kratos y Atreus se enfrentan al Ragnarok nordico en esta epica aventura de accion y narrativa',
 39.99, 20, 1, '/uploads/images/god-of-war-ragnarok.jpg', 'Santa Monica Studio', '2022-11-09', 4.70),

('Final Fantasy XVI',
 'RPG de accion con una historia epica de cristales y dominios en el mundo de Valisthea',
 49.99, 10, 1, '/uploads/images/ff16.jpg', 'Square Enix', '2023-06-22', 4.50),

("Marvel's Spider-Man 2",
 'Peter Parker y Miles Morales unen fuerzas contra Venom en una Nueva York de mundo abierto',
 69.99, 20, 1, '/uploads/images/spiderman2.jpg', 'Insomniac Games', '2023-10-20', 4.70),

("Dragon's Dogma 2",
 'RPG de accion en mundo abierto con un sistema de companeros dinamico y combate espectacular',
 59.99, 15, 1, '/uploads/images/dragons-dogma-2.jpg', 'Capcom', '2024-03-22', 4.20),

('Persona 5 Royal',
 'RPG japones donde un grupo de estudiantes lucha contra la corrupcion como los Phantom Thieves',
 29.99, 22, 1, '/uploads/images/persona-5-royal.jpg', 'Atlus', '2020-03-31', 4.80),

('Sonic Frontiers',
 'Sonic explora islas misteriosas en mundo abierto combinando plataformas y combate a alta velocidad',
 39.99, 15, 1, '/uploads/images/sonic-frontiers.jpg', 'Sonic Team', '2022-11-08', 3.80),

('Avatar: Frontiers of Pandora',
 'Aventura de accion en primera persona ambientada en Pandora con un nuevo clan Navi por descubrir',
 59.99, 14, 1, '/uploads/images/avatar-frontiers.jpg', 'Massive Entertainment', '2023-12-07', 3.60),

('Death Stranding 2: On The Beach',
 'Secuela del aclamado juego de Hideo Kojima que continua la historia de Sam Bridges conectando el mundo',
 69.99, 8, 1, '/uploads/images/death-stranding-2.jpg', 'Kojima Productions', '2025-06-26', 4.40),

('Expedition 33',
 'RPG de accion y exploracion en un mundo de fantasia inspirado en la pintura impresionista francesa',
 69.99, 10, 1, '/uploads/images/expedition-33.jpg', 'Sandfall Interactive', '2025-03-13', 4.20),

('Uncharted 4: A Thiefs End',
 'Nathan Drake vuelve para una ultima aventura en busca del tesoro del pirata Henry Avery',
 29.99, 25, 1, '/uploads/images/uncharted-4.jpg', 'Naughty Dog', '2016-05-10', 4.80),

('Detroit: Become Human',
 'Thriller interactivo donde tres androides despiertan conciencia en un Detroit futurista del 2038',
 19.99, 18, 1, '/uploads/images/detroit-become-human.jpg', 'Quantic Dream', '2018-05-25', 4.30),

('Monster Hunter Wilds',
 'La nueva entrega de la saga de caza de monstruos con ecosistemas vivos y combate cooperativo',
 69.99, 12, 1, '/uploads/images/monster-hunter-wilds.jpg', 'Capcom', '2025-02-28', 4.40),

('Resident Evil 4 Remake',
 'Remake del clasico de survival horror donde Leon S. Kennedy rescata a la hija del presidente en una aldea espanola',
 49.99, 16, 1, '/uploads/images/re4-remake.jpg', 'Capcom', '2023-03-24', 4.70),

('Pragmata',
 'Aventura de ciencia ficcion ambientada en un futuro distopico donde un astronauta y una nina buscan la verdad',
 69.99, 10, 1, '/uploads/images/pragmata.jpg', 'Capcom', '2026-07-15', 4.10),

('Resident Evil Requiem',
 'Nueva entrega de la saga Resident Evil que promete reinventar el survival horror con motor RE Engine',
 69.99, 14, 1, '/uploads/images/re-requiem.jpg', 'Capcom', '2026-09-20', 4.30);

-- ── Xbox Series X (plataforma_id = 2) ──────────────────────────

INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, desarrollador, fecha_lanzamiento, calificacion) VALUES
('Halo Infinite',
 'El Jefe Maestro se enfrenta a los Desterrados en una nueva aventura en el anillo Zeta Halo',
 39.99, 12, 2, '/uploads/images/halo-infinite.jpg', '343 Industries', '2021-12-08', 4.00),

('Starfield',
 'RPG de ciencia ficcion espacial donde exploras cientos de planetas en una galaxia inmensa',
 59.99, 8, 2, '/uploads/images/starfield.jpg', 'Bethesda Game Studios', '2023-09-06', 3.80),

('Forza Horizon 6',
 'Proxima entrega de la aclamada saga de carreras en mundo abierto con graficos de nueva generacion',
 69.99, 12, 2, '/uploads/images/forza-horizon-6.jpg', 'Playground Games', '2026-08-15', 4.50),

('Crimson Desert',
 'RPG de accion y aventura en mundo abierto con combate visceral ambientado en un continente en guerra',
 59.99, 14, 2, '/uploads/images/crimson-desert.jpg', 'Pearl Abyss', '2026-03-28', 4.20),

('Cyberpunk 2077',
 'RPG de accion en primera persona en Night City, una megaciudad futurista obsesionada con el poder y la tecnologia',
 29.99, 22, 2, '/uploads/images/cyberpunk-2077.jpg', 'CD Projekt Red', '2020-12-10', 4.50),

('NBA 2K26',
 'Simulador de baloncesto con modos Mi Carrera, Mi Equipo y jugabilidad mejorada con motor de nueva generacion',
 69.99, 20, 2, '/uploads/images/nba-2k26.jpg', 'Visual Concepts', '2026-09-05', 3.80),

('Borderlands 4',
 'Shooter looter cooperativo con millones de armas generadas proceduralmente en un nuevo planeta por explorar',
 69.99, 16, 2, '/uploads/images/borderlands-4.jpg', 'Gearbox Software', '2025-09-12', 4.10),

('Diablo IV',
 'RPG de accion oscuro donde los jugadores luchan contra las fuerzas del infierno en el mundo de Santuario',
 49.99, 18, 2, '/uploads/images/diablo-4.jpg', 'Blizzard Entertainment', '2023-06-06', 4.00),

('Overcooked! 2',
 'Juego cooperativo de cocina caos donde preparas platos en cocinas cada vez mas absurdas con tus amigos',
 24.99, 25, 2, '/uploads/images/overcooked-2.jpg', 'Ghost Town Games', '2018-08-07', 4.20);

-- ── PC (plataforma_id = 3) ──────────────────────────────────────

INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, desarrollador, fecha_lanzamiento, calificacion) VALUES
("Baldur's Gate 3",
 'RPG de fantasia epico basado en Dungeons and Dragons con libertad total de elecciones y consecuencias',
 59.99, 25, 3, '/uploads/images/baldurs-gate3.jpg', 'Larian Studios', '2023-08-03', 4.90),

('Satisfactory',
 'Juego de construccion de fabricas en primera persona ambientado en un planeta alienigena',
 29.99, 25, 3, '/uploads/images/satisfactory.jpg', 'Coffee Stain Studios', '2024-09-10', 4.50),

('Cult of the Lamb',
 'Gestiona tu propio culto mientras exploras mazmorras roguelike como un adorable cordero poseido',
 24.99, 20, 3, '/uploads/images/cult-of-the-lamb.jpg', 'Massive Monster', '2022-08-11', 4.30),

('Crusader Kings III',
 'Gran estrategia medieval donde gestionas una dinastia a traves de intrigas politicas y guerras',
 39.99, 18, 3, '/uploads/images/crusader-kings-3.jpg', 'Paradox Development Studio', '2020-09-01', 4.60),

('Euro Truck Simulator 2',
 'Simulador de conduccion de camiones por las carreteras de Europa con entregas de mercancias',
 19.99, 30, 3, '/uploads/images/euro-truck-simulator-2.jpg', 'SCS Software', '2012-10-18', 4.60),

('Daemon X Machina: Titanic Scion',
 'Secuela del juego de accion con mechas gigantes y combate aereo de alta velocidad',
 59.99, 10, 3, '/uploads/images/daemon-x-machina-ts.jpg', 'Marvelous', '2026-06-20', 4.10),

('ARC Raiders',
 'Shooter cooperativo en tercera persona donde la humanidad resiste una invasion de maquinas espaciales',
 39.99, 10, 3, '/uploads/images/arc-raiders.jpg', 'Embark Studios', '2026-03-10', 3.90),

('Cities: Skylines II',
 'Simulador de construccion de ciudades con sistemas realistas de economia, transporte y poblacion',
 49.99, 15, 3, '/uploads/images/cities-skylines-2.jpg', 'Colossal Order', '2023-10-24', 3.50),

('Blasphemous',
 'Metroidvania de accion penitente con arte pixel art gotico ambientado en la tierra de Cvstodia',
 24.99, 20, 3, '/uploads/images/blasphemous.jpg', 'The Game Kitchen', '2019-09-10', 4.40),

('Total War: Warhammer III',
 'Estrategia epica por turnos y batallas en tiempo real en el universo de fantasia de Warhammer',
 49.99, 14, 3, '/uploads/images/total-war-warhammer-3.jpg', 'Creative Assembly', '2022-02-17', 4.20),

('World of Warcraft: Midnight',
 'Nueva expansion del legendario MMORPG que lleva a los jugadores de vuelta a Quelthalas',
 49.99, 30, 3, '/uploads/images/wow-midnight.jpg', 'Blizzard Entertainment', '2026-11-10', 4.30),

('The Witcher 3: Wild Hunt',
 'RPG de mundo abierto donde Geralt de Rivia busca a su hija adoptiva perseguida por la Caceria Salvaje',
 14.99, 35, 3, '/uploads/images/witcher-3.jpg', 'CD Projekt Red', '2015-05-19', 4.90),

('Phasmophobia',
 'Juego cooperativo de caza de fantasmas donde investigas ubicaciones encantadas con equipamiento paranormal',
 13.99, 22, 3, '/uploads/images/phasmophobia.jpg', 'Kinetic Games', '2023-08-10', 4.40),

('RimWorld',
 'Simulador de colonias espaciales donde gestionas supervivientes en un planeta lejano con eventos aleatorios',
 34.99, 20, 3, '/uploads/images/rimworld.jpg', 'Ludeon Studios', '2018-10-17', 4.80),

('Golf It!',
 'Minigolf multijugador online con editor de mapas donde compites en circuitos creativos y desafiantes',
 8.99, 30, 3, '/uploads/images/golf-it.jpg', 'Perfuse Entertainment', '2017-02-27', 4.10);

-- ==============================================
-- FIN DE LOS DATOS
-- ==============================================
