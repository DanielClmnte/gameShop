# AGENTS.md — GameShop

Guía de referencia rápida para agentes de IA que trabajen en este proyecto.

## Stack & Arranque

- **Java 17 · Spring Boot 4.0.2 · Thymeleaf · MySQL 8**
- Sin JPA/Hibernate: todos los repositorios usan **JDBC puro** (`PreparedStatement`) contra MySQL mediante un singleton `Conexion.java`.
- **No hay Spring Security**: la protección de rutas admin es un `HandlerInterceptor` manual (`config/AdminSecurityInterceptor.java`).
- Sin `@Autowired` ni contenedor DI para servicios/repos: cada clase instancia sus dependencias con `new` en el constructor.

```bash
# Ejecutar la aplicación
./mvnw spring-boot:run

# Compilar y empaquetar
./mvnw package

# Ejecutar tests
./mvnw test
```

App disponible en `http://localhost:8080`.

## Arquitectura de capas

```
Controller → Service → Repository (JDBC → MySQL)
```

- **Controllers** (`controller/`): anotados con `@Controller`, instancian sus propios services en el constructor.
- **Services** (`service/`): lógica de negocio; también instancian sus propios repositorios con `new`.
- **Repositories** (`repository/`): consultas SQL con `PreparedStatement` contra MySQL. Usan `Conexion.getInstancia().getConnection()`.
- **Models** (`model/`): POJOs planos sin anotaciones JPA. `Videojuego.setStock()` actualiza automáticamente `disponible`.
- **Conexion** (`repository/Conexion.java`): singleton que gestiona la conexión JDBC (`jdbc:mysql://localhost:3306/gameshop_db`, user=`root`, password=`root`).

## Base de datos MySQL

El esquema SQL está en `database/gameshop_db.sql` (tablas: `plataformas`, `videojuegos`, `usuarios`, `opiniones`, `carrito_items`, `pedidos`, `detalle_pedidos`).

**Requisito**: MySQL corriendo en `localhost:3306` con la BD `gameshop_db` creada ejecutando el script SQL. Credenciales por defecto: `root`/`root` (MAMP).

- IDs de plataforma: `1=PlayStation 5, 2=Xbox Series X, 3=PC`.
- IDs asignados por `AUTO_INCREMENT` — el código recupera las claves generadas con `getGeneratedKeys()`.
- `PedidoRepository.guardar()` usa **transacciones** (inserta en `pedidos` + `detalle_pedidos` en una sola tx).

## Autenticación & Sesión

- Login: `POST /login` — valida email (o nombre) / contraseña contra `UsuarioRepository`. Las contraseñas están en **texto plano**.
- `LoginController.procesarLogin` guarda `usuarioId`, `usuarioNombre` y `usuarioRol` en la `HttpSession`.
- `AdminSecurityInterceptor` protege `/admin/**` comprobando `session.getAttribute("usuarioId")` y `session.getAttribute("usuarioRol")`.
- `CartCountInterceptor` inyecta `carritoCount` en todas las vistas para el badge del header.
- Roles: string `"CLIENTE"` o `"ADMIN"` en `Usuario.rol`.

| Usuario | Email | Contraseña | Rol |
|---------|-------|------------|-----|
| admin | admin@gameshop.com | admin | ADMIN |
| Juan Perez | juan@example.com | pass123 | CLIENTE |
| Maria Garcia | maria@example.com | pass456 | CLIENTE |

## Rutas principales

| Área | Ruta |
|------|------|
| Catálogo | `GET /`, `/catalogo/plataforma/{id}`, `/catalogo/buscar`, `/catalogo/disponibles`, `/catalogo/populares` |
| Producto | `GET /producto/{id}`, `GET /producto/{id}/nueva-opinion`, `POST /producto/{id}/guardar-opinion` |
| Carrito | `GET /carrito`, `POST /carrito/agregar/{id}`, `POST /carrito/actualizar/{id}`, `GET /carrito/eliminar/{id}`, `GET /carrito/vaciar` |
| Checkout | `GET /checkout`, `POST /checkout/procesar` |
| Pedidos | `GET /pedidos` |
| Auth | `GET /login`, `POST /login`, `GET /logout`, `GET /registro`, `POST /registro`, `GET /perfil` |
| Admin | `GET /admin`, `/admin/productos`, `/admin/productos/nuevo`, `/admin/productos/guardar`, `/admin/productos/editar/{id}`, `/admin/productos/actualizar/{id}`, `/admin/productos/eliminar/{id}`, `/admin/stock` |

- `usuarioId` se obtiene de la **HttpSession** (login) o de un ID anónimo negativo generado por sesión.
- Checkout y opiniones **requieren login** — redirigen a `/login?redirect=...` si no hay sesión.
- Checkout aplica **21% IVA** sobre el subtotal. Pago simulado: tarjeta de 16 dígitos + CVV de 3 dígitos.

## Plantillas Thymeleaf

Convención: `templates/{sección}/{vista}.html` → retorno del controller: `"{sección}/{vista}"`.

```java
return "admin/dashboard";   // → templates/admin/dashboard.html
return "carrito/ver-carrito";
```

- **Fragments**: `fragments/layout.html` (header público + footer), `fragments/admin-layout.html` (header admin).
- CSS por sección en `static/css/`: `common.css`, `catalogo.css`, `producto.css`, `carrito.css`, `checkout.css`, `auth.css`, `admin.css`, `pedidos.css`. Sin framework CSS externo — tema dark gamer (`#0a0a0a` fondo, `#00d4ff` primario).

## Imágenes de productos

Las imágenes se almacenan como URL completa en `Videojuego.imagen` (ej. `/uploads/images/elden-ring.jpg`). Se sirven desde `uploads/images/` configurado como recurso estático en `WebConfig.java`. `ImagenService` gestiona la subida y eliminación de archivos.

## Archivos clave

| Archivo | Propósito |
|---------|-----------|
| `config/AdminSecurityInterceptor.java` | Protección de rutas `/admin/**` |
| `config/CartCountInterceptor.java` | Inyecta `carritoCount` en todas las vistas |
| `config/WebConfig.java` | Registra interceptores y sirve `/uploads/**` |
| `repository/Conexion.java` | Singleton de conexión JDBC a MySQL |
| `repository/VideojuegoRepository.java` | CRUD de videojuegos (SQL) |
| `repository/CarritoRepository.java` | CRUD del carrito por usuario (SQL) |
| `repository/PedidoRepository.java` | Pedidos con transacciones (SQL) |
| `service/CarritoService.java` | Orquesta carrito + stock |
| `service/ImagenService.java` | Subida/eliminación de imágenes |
| `controller/CheckoutController.java` | Flujo de compra y simulación de pago |
| `database/gameshop_db.sql` | Esquema MySQL completo con datos de prueba |

