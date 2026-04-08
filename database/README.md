# 📊 Base de Datos - GameShop

## Descripción
Este archivo SQL contiene la estructura completa de la base de datos para el proyecto **GameShop** - Tienda de Videojuegos.

## 📋 Tablas Principales

### 1. **plataformas**
- Almacena las plataformas de juego (PS5, Xbox, PC)
- Campos: id, nombre, descripcion

### 2. **usuarios**
- Información de clientes y administradores
- Campos: id, nombre, email, contrasena, telefono, direccion, ciudad, codigo_postal, rol, activo, fecha_registro

### 3. **videojuegos**
- Catálogo de productos
- Campos: id, titulo, descripcion, precio, stock, plataforma_id, imagen, fecha_lanzamiento, desarrollador, calificacion, disponible

### 4. **opiniones**
- Reseñas y comentarios de usuarios
- Campos: id, videojuego_id, usuario_id, calificacion (1-5), comentario, fecha, verificado

### 5. **carrito_items**
- Items en el carrito de compras
- Campos: id, videojuego_id, usuario_id, cantidad, precio_unitario

### 6. **pedidos**
- Historial de compras
- Campos: id, usuario_id, total, estado, fecha_pedido, fecha_entrega

### 7. **detalle_pedidos**
- Detalles de productos en cada pedido
- Campos: id, pedido_id, videojuego_id, cantidad, precio_unitario

## 🚀 Cómo Importar a phpMyAdmin

### Opción 1: Desde interfaz web phpMyAdmin
1. Abre phpMyAdmin en tu navegador (http://localhost/phpmyadmin)
2. Ve a **Importar**
3. Selecciona el archivo **gameshop_db.sql**
4. Haz clic en **Ejecutar**

### Opción 2: Desde terminal (MySQL)
```bash
mysql -u root -p < gameshop_db.sql
```

### Opción 3: Desde terminal (con especificar contraseña)
```bash
mysql -u root -p'tu_contraseña' < gameshop_db.sql
```

## 📝 Datos de Prueba Incluidos

El script incluye datos de ejemplo:
- **3 plataformas**: PS5, Xbox Series X, PC
- **3 usuarios**: 1 admin y 2 clientes
- **8 videojuegos**: Distribuidos entre las plataformas

## 🔑 Credenciales de Prueba

| Usuario | Email | Contraseña | Rol |
|---------|-------|-----------|-----|
| Admin GameShop | admin@gameshop.com | admin123 | ADMIN |
| Juan Pérez | juan@example.com | pass123 | CLIENTE |
| María García | maria@example.com | pass456 | CLIENTE |

## ⚠️ Importante

- Estos datos son **SOLO PARA DESARROLLO LOCAL**
- Las contraseñas deben ser encriptadas en producción
- Este archivo es parte de la rama `feature/base-datos`

---

**Proyecto**: GameShop - Tienda de Videojuegos  
**Nivel**: 1º DAM  
**Autor**: Daniel Clemente Gomez

