# AGENTS.md — GameShop

Guía de referencia rápida para agentes de IA que trabajen en este proyecto.

## Stack & Arranque

- **Java 21 · Spring Boot 4.0.2 · Thymeleaf · MySQL**
- Sin JPA/Hibernate: todos los repositorios usan **listas/maps estáticos en memoria** (los datos se pierden al reiniciar).
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
Controller → Service → Repository (static List/Map)
```

- **Controllers** (`controller/`): anotados con `@Controller`, instancian sus propios services en el constructor.
- **Services** (`service/`): lógica de negocio; también instancian sus propios repositorios con `new`.
- **Repositories** (`repository/`): colecciones `static` inicializadas en bloque `static {}`. Son la única fuente de datos en memoria.
- **Models** (`model/`): POJOs planos sin anotaciones JPA. `Videojuego.setStock()` actualiza automáticamente `disponible`.

## Datos en memoria — convenciones clave

- **`VideojuegoRepository`**: lista estática con 8 juegos predefinidos. IDs de plataforma: `1=PS5, 2=Xbox, 3=PC`.
- **`CarritoRepository`**: `Map<Integer, List<CarritoItem>>` donde la clave es el `usuarioId`.
- **`UsuarioRepository`**: lista estática con 3 usuarios precargados (ver tabla abajo).
- Al crear un nuevo `Videojuego` con ID `0`, el admin controller no asigna un ID automático — hay que añadir esa lógica manualmente.

## Autenticación & Sesión

- Login: `POST /login` — valida email/contraseña contra `UsuarioRepository`. Las contraseñas están en **texto plano**.
- `LoginController.procesarLogin` añade `usuarioId` al `Model`, **no a la `HttpSession`**. La sesión debe establecerse manualmente si se quiere que `AdminSecurityInterceptor` funcione.
- `AdminSecurityInterceptor` protege `/admin/**` comprobando `session.getAttribute("usuarioId")`.
- Roles: string `"CLIENTE"` o `"ADMIN"` en `Usuario.rol`.

| Usuario | Email | Contraseña | Rol |
|---------|-------|------------|-----|
| Admin GameShop | admin@gameshop.com | admin123 | ADMIN |
| Juan Pérez | juan@example.com | pass123 | CLIENTE |
| María García | maria@example.com | pass456 | CLIENTE |

## Rutas principales

| Área | Ruta |
|------|------|
| Catálogo | `GET /`, `/catalogo/plataforma/{id}`, `/catalogo/buscar`, `/catalogo/disponibles`, `/catalogo/populares` |
| Producto | `GET /producto/{id}`, `POST /producto/{id}/opinion` |
| Carrito | `GET /carrito?usuarioId=`, `POST /carrito/agregar/{id}`, `POST /carrito/actualizar/{id}` |
| Checkout | `GET /checkout?usuarioId=`, `POST /checkout/procesar` |
| Admin | `GET /admin`, `/admin/productos`, `/admin/stock` |

- `usuarioId` se pasa como **query param** en carrito y checkout (por defecto `1`).
- Checkout aplica **21% IVA** sobre el subtotal. Pago simulado: tarjeta de 16 dígitos + CVV de 3 dígitos.

## Plantillas Thymeleaf

Convención: `templates/{sección}/{vista}.html` → retorno del controller: `"{sección}/{vista}"`.

```java
return "admin/dashboard";   // → templates/admin/dashboard.html
return "carrito/ver-carrito";
```

CSS por sección en `static/css/`: `common.css`, `catalogo.css`, `producto.css`, `carrito.css`, `auth.css`, `admin.css`. Sin framework CSS externo — tema dark gamer (`#0a0a0a` fondo, `#00d4ff` primario).

## Base de datos (MySQL — pendiente de integrar)

El esquema SQL está en `database/gameshop_db.sql` (tablas: `plataformas`, `videojuegos`, `usuarios`, `opiniones`, `carrito_items`, `pedidos`, `detalle_pedidos`). Actualmente el código **no conecta con la BD** — los repositorios son en memoria. Para activar la BD, hay que añadir Spring Data JPA y configurar `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gameshop_db
spring.datasource.username=root
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=validate
```

## Imágenes de productos

Las imágenes se referencian por nombre de archivo (ej. `elden-ring.jpg`) almacenado en `Videojuego.imagen`. Se sirven desde `uploads/images/` (configurable con `app.upload.dir` en `application.properties`). Actualmente no hay upload real implementado.

## Archivos clave

| Archivo | Propósito |
|---------|-----------|
| `config/AdminSecurityInterceptor.java` | Protección de rutas `/admin/**` |
| `repository/VideojuegoRepository.java` | Datos de juegos + lógica de filtrado |
| `repository/CarritoRepository.java` | Estado del carrito por usuario |
| `service/CarritoService.java` | Orquesta carrito + stock |
| `controller/CheckoutController.java` | Flujo de compra y simulación de pago |
| `database/gameshop_db.sql` | Esquema MySQL completo con datos de prueba |

