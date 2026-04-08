# 🎮 GameShop - Tienda de Videojuegos E-Commerce

## Descripción del Proyecto
GameShop es una tienda de videojuegos online (e-commerce nicho gamer) desarrollada en **Spring Boot** con **Thymeleaf** como framework de vistas. El proyecto es ideal para aprender conceptos de desarrollo web como CRUD, relaciones entre entidades, autenticación y comercio electrónico.

**Proyecto de Fin de Año - 1º DAM**  
**Autor**: Daniel Clemente Gomez / Ángel Pozo Yarlequé

---

## 🚀 Características Principales

### Catálogo
- ✅ Catálogo de videojuegos filtrado por plataforma (PS5, Xbox Series X, PC)
- ✅ Búsqueda y filtrado de productos
- ✅ Fichas de producto detalladas con opiniones

### Carrito de Compras
- ✅ Agregar/eliminar productos
- ✅ Actualizar cantidades
- ✅ Cálculo automático de totales e impuestos

### Autenticación
- ✅ Sistema de login/registro
- ✅ Gestión de perfiles de usuario
- ✅ Protección de rutas admin

### Panel de Administración
- ✅ Dashboard con estadísticas
- ✅ CRUD completo de productos
- ✅ Gestión de stock (aumentar, disminuir, actualizar)
- ✅ Filtros por disponibilidad

### Sistema de Pedidos
- ✅ Checkout con formulario completo
- ✅ Validación de datos de pago (simulado)
- ✅ Generación de número de pedido
- ✅ Confirmación de compra

### Opiniones
- ✅ Reseñas con calificación (1-5 estrellas)
- ✅ Promedio de calificaciones por producto
- ✅ Filtrado de juegos populares

---

## 📁 Estructura del Proyecto

```
gameShop/
├── src/main/
│   ├── java/com/ilerna/gameShop/
│   │   ├── gameShopApplication.java       # Main
│   │   ├── config/
│   │   │   └── AdminSecurityInterceptor.java
│   │   ├── controller/
│   │   │   ├── CatalogoController.java
│   │   │   ├── FichaProductoController.java
│   │   │   ├── CarritoController.java
│   │   │   ├── LoginController.java
│   │   │   ├── AdminController.java
│   │   │   ├── StockController.java
│   │   │   └── CheckoutController.java
│   │   ├── model/
│   │   │   ├── Plataforma.java
│   │   │   ├── Videojuego.java
│   │   │   ├── Usuario.java
│   │   │   ├── Opiniones.java
│   │   │   └── CarritoItem.java
│   │   ├── repository/
│   │   │   ├── PlataformaRepository.java
│   │   │   ├── VideojuegoRepository.java
│   │   │   ├── UsuarioRepository.java
│   │   │   ├── OpinionesRepository.java
│   │   │   └── CarritoRepository.java
│   │   └── service/
│   │       ├── PlataformaService.java
│   │       ├── VideojuegoService.java
│   │       ├── UsuarioService.java
│   │       ├── OpinionesService.java
│   │       └── CarritoService.java
│   └── resources/
│       ├── application.properties
│       ├── static/css/
│       │   ├── common.css
│       │   ├── catalogo.css
│       │   ├── producto.css
│       │   ├── carrito.css
│       │   ├── auth.css
│       │   └── admin.css
│       └── templates/
│           ├── catalogo/
│           ├── producto/
│           ├── carrito/
│           ├── auth/
│           ├── admin/
│           └── checkout/
├── database/
│   └── gameshop_db.sql        # Script SQL
├── uploads/
│   └── images/                # Carpeta para imágenes
└── pom.xml                    # Dependencias Maven

```

---

## 🛠️ Tecnologías Utilizadas

- **Backend**: Java 21, Spring Boot 4.0.2
- **Frontend**: HTML5, CSS3, Thymeleaf
- **Base de Datos**: MySQL (phpMyAdmin)
- **Gestor de Dependencias**: Maven
- **Servidor**: Embedded Tomcat

---

## 📋 Requisitos Previos

- Java 21 instalado
- Maven instalado
- MySQL/MAMP/XAMPP instalado (para la BD)
- IDE: IntelliJ IDEA o VS Code

---

## 🚀 Instrucciones de Ejecución

### 1. Clonar el repositorio
```bash
git clone https://github.com/DanielClmnte/gameShop.git
cd gameShop
```

### 2. Importar la base de datos
```bash
# Opción 1: Desde phpMyAdmin
# 1. Abre phpMyAdmin (http://localhost/phpmyadmin)
# 2. Ve a Importar
# 3. Selecciona database/gameshop_db.sql
# 4. Ejecuta

# Opción 2: Desde terminal
mysql -u root -p < database/gameshop_db.sql
```

### 3. Configurar la conexión a BD (opcional para desarrollo)
Editar `/src/main/resources/application.properties`:
```properties
spring.application.name=gameShop
app.upload.dir=uploads/images
# Futura configuración BD
```

### 4. Ejecutar la aplicación
```bash
cd /Users/clemente/ProyectoGameShop/gameShop
./mvnw spring-boot:run
```

### 5. Acceder a la aplicación
```
http://localhost:8080
```

---

## 👤 Credenciales de Prueba

| Usuario | Email | Contraseña | Rol |
|---------|-------|-----------|-----|
| Admin | admin@gameshop.com | admin123 | ADMIN |
| Juan | juan@example.com | pass123 | CLIENTE |
| María | maria@example.com | pass456 | CLIENTE |

---

## 💳 Datos de Prueba para Checkout

| Dato | Valor |
|------|-------|
| Tarjeta | 1234567890123456 |
| Mes Expiración | 12 |
| Año Expiración | 2025 |
| CVV | 123 |

---

## 📚 Rutas Principales

### Público
- `/` - Catálogo principal
- `/catalogo/plataforma/{id}` - Juegos por plataforma
- `/catalogo/buscar` - Búsqueda
- `/catalogo/disponibles` - Juegos disponibles
- `/catalogo/populares` - Más populares
- `/producto/{id}` - Detalles del producto
- `/carrito` - Carrito de compras
- `/login` - Login
- `/registro` - Registro

### Admin (Protegidas)
- `/admin` - Dashboard
- `/admin/productos` - Gestionar productos
- `/admin/productos/nuevo` - Crear producto
- `/admin/productos/editar/{id}` - Editar producto
- `/admin/productos/eliminar/{id}` - Eliminar producto
- `/admin/stock` - Gestión de stock

### Compra
- `/checkout` - Formulario de compra
- `/checkout/procesar` - Procesar pago

---

## 🎨 Diseño

### Paleta de Colores
- **Fondo Oscuro**: #0a0a0a
- **Primario**: #00d4ff (Cyan)
- **Éxito**: #00ff00 (Verde)
- **Error**: #ff3333 (Rojo)
- **Advertencia**: #ffc107 (Amarillo)

### Características de UX
- ✅ Diseño responsive (mobile-first)
- ✅ Tema dark mode
- ✅ Animaciones suaves
- ✅ Iconos emoji intuitivos
- ✅ Navegación clara

---

## 📖 Ramas Git

```
✅ master                          → Main stable
✅ develop                         → Rama base de desarrollo
✅ feature/base-datos              → Modelos y BD
✅ feature/catalogo-plataformas    → Catálogo
✅ feature/fichas-producto         → Detalles de producto
✅ feature/carrito                 → Carrito de compras
✅ feature/login                   → Autenticación
✅ feature/admin-panel             → CRUD de productos
✅ feature/gestion-stock           → Control de inventario
✅ feature/checkout                → Proceso de compra
✅ feature/seguridad               → Protección de rutas
```

---

## 🔄 Flujo de Compra

```
1. Usuario entra al catálogo
   ↓
2. Busca/filtra productos
   ↓
3. Ve detalles y opiniones
   ↓
4. Agrega al carrito
   ↓
5. Revisa carrito
   ↓
6. Procede a checkout
   ↓
7. Ingresa datos personales
   ↓
8. Completa pago
   ↓
9. Confirmación y número de pedido
```

---

## 🐛 Limitaciones Conocidas

- Los datos se almacenan en memoria (no persisten tras reinicio)
- Las imágenes son placeholders
- La autenticación es basic (sin encriptación real)
- El pago es simulado (no integrado con pasarela real)

---

## 🔮 Mejoras Futuras

- [ ] Integración con pasarela de pago real (Stripe, PayPal)
- [ ] Encriptación de contraseñas (BCrypt)
- [ ] JPA/Hibernate para persistencia en BD
- [ ] Upload real de imágenes
- [ ] Sistema de notificaciones por email
- [ ] Historial de pedidos
- [ ] Cupones de descuento
- [ ] Recomendaciones personalizadas
- [ ] API REST
- [ ] Pruebas unitarias y de integración

---

## 📝 Notas Importantes

- Este proyecto es **EDUCACIONAL** y está optimizado para el aprendizaje
- El código prioriza **claridad sobre perfección**
- Ideal para entender conceptos de **e-commerce y web development**
- Se puede usar como base para un proyecto real con mejoras

---

## 📄 Licencia

Este proyecto es de código abierto y disponible bajo licencia MIT.

---

## 👨‍💼 Autor

**Daniel Clemente Gomez**  
Proyecto de Fin de Año - 1º DAM  
ILERNA Online

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor crea un fork y envía un pull request.

---

**Última actualización**: 8 de Abril, 2026  
**Estado**: ✅ Versión 1.0 Completa

