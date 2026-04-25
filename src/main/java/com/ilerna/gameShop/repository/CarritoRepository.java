package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.CarritoItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar carritos de compras.
 * Consultas SQL contra la tabla 'carrito_items' de gameshop_db.
 */
public class CarritoRepository implements ICarritoRepository {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    /**
     * Obtener el carrito de un usuario
     */
    public List<CarritoItem> obtenerCarrito(int usuarioId) {
        List<CarritoItem> items = new ArrayList<>();
        String sql = "SELECT * FROM carrito_items WHERE usuario_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapearCarritoItem(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener carrito.");
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Agregar un item al carrito (o aumentar cantidad si ya existe)
     */
    public void agregarItem(int usuarioId, CarritoItem item) {
        // Verificar si ya existe el videojuego en el carrito del usuario
        String sqlCheck = "SELECT id, cantidad FROM carrito_items WHERE usuario_id = ? AND videojuego_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sqlCheck)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, item.getVideojuegoId());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Ya existe: aumentar cantidad
                    int nuevaCantidad = rs.getInt("cantidad") + item.getCantidad();
                    int itemId = rs.getInt("id");
                    String sqlUpdate = "UPDATE carrito_items SET cantidad = ? WHERE id = ?";
                    try (PreparedStatement pstmtU = getConnection().prepareStatement(sqlUpdate)) {
                        pstmtU.setInt(1, nuevaCantidad);
                        pstmtU.setInt(2, itemId);
                        pstmtU.executeUpdate();
                    }
                } else {
                    // No existe: insertar nuevo
                    String sqlInsert = "INSERT INTO carrito_items (videojuego_id, usuario_id, cantidad, precio_unitario, titulo_videojuego, imagen_videojuego) "
                                     + "VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmtI = getConnection().prepareStatement(sqlInsert)) {
                        pstmtI.setInt(1, item.getVideojuegoId());
                        pstmtI.setInt(2, usuarioId);
                        pstmtI.setInt(3, item.getCantidad());
                        pstmtI.setDouble(4, item.getPrecioUnitario());
                        pstmtI.setString(5, item.getTituloVideojuego());
                        pstmtI.setString(6, item.getImagenVideojuego());
                        pstmtI.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al agregar item al carrito.");
            e.printStackTrace();
        }
    }

    /**
     * Actualizar la cantidad de un item
     */
    public void actualizarCantidad(int usuarioId, int videojuegoId, int nuevaCantidad) {
        String sql = "UPDATE carrito_items SET cantidad = ? WHERE usuario_id = ? AND videojuego_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, nuevaCantidad);
            pstmt.setInt(2, usuarioId);
            pstmt.setInt(3, videojuegoId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar cantidad del carrito.");
            e.printStackTrace();
        }
    }

    /**
     * Eliminar un item del carrito
     */
    public void eliminarItem(int usuarioId, int videojuegoId) {
        String sql = "DELETE FROM carrito_items WHERE usuario_id = ? AND videojuego_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, videojuegoId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar item del carrito.");
            e.printStackTrace();
        }
    }

    /**
     * Vaciar el carrito de un usuario
     */
    public void vaciarCarrito(int usuarioId) {
        String sql = "DELETE FROM carrito_items WHERE usuario_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al vaciar carrito.");
            e.printStackTrace();
        }
    }

    /**
     * Obtener la cantidad total de items en el carrito
     */
    public int obtenerCantidadTotal(int usuarioId) {
        String sql = "SELECT COALESCE(SUM(cantidad), 0) AS total FROM carrito_items WHERE usuario_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener cantidad total del carrito.");
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Obtener el total del carrito (suma de subtotales)
     */
    public double obtenerTotalCarrito(int usuarioId) {
        String sql = "SELECT COALESCE(SUM(cantidad * precio_unitario), 0) AS total FROM carrito_items WHERE usuario_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener total del carrito.");
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Obtener la cantidad de un item concreto en el carrito (0 si no existe)
     */
    public int obtenerCantidadItem(int usuarioId, int videojuegoId) {
        String sql = "SELECT COALESCE(cantidad, 0) FROM carrito_items WHERE usuario_id = ? AND videojuego_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, videojuegoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener cantidad del item en carrito.");
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Verificar si existe un item en el carrito
     */
    public boolean existeItem(int usuarioId, int videojuegoId) {
        String sql = "SELECT COUNT(*) FROM carrito_items WHERE usuario_id = ? AND videojuego_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, videojuegoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar item en carrito.");
            e.printStackTrace();
        }
        return false;
    }

    // ── Mapear ResultSet a CarritoItem ──
    private CarritoItem mapearCarritoItem(ResultSet rs) throws SQLException {
        CarritoItem item = new CarritoItem();
        item.setId(rs.getInt("id"));
        item.setVideojuegoId(rs.getInt("videojuego_id"));
        item.setCantidad(rs.getInt("cantidad"));
        item.setPrecioUnitario(rs.getDouble("precio_unitario"));
        item.setTituloVideojuego(rs.getString("titulo_videojuego"));
        item.setImagenVideojuego(rs.getString("imagen_videojuego"));
        return item;
    }
}
