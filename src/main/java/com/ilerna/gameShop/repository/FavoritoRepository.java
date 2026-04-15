package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Favorito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar la tabla 'favoritos'.
 * JDBC puro contra gameshop_db.
 */
public class FavoritoRepository {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    /**
     * Obtener todos los favoritos de un usuario (con datos del videojuego)
     */
    public List<Favorito> obtenerPorUsuario(int usuarioId) {
        List<Favorito> favoritos = new ArrayList<>();
        String sql = "SELECT f.*, v.titulo, v.imagen, v.precio, v.disponible "
                   + "FROM favoritos f "
                   + "JOIN videojuegos v ON f.videojuego_id = v.id "
                   + "WHERE f.usuario_id = ? "
                   + "ORDER BY f.fecha_agregado DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    favoritos.add(mapearFavorito(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener favoritos.");
            e.printStackTrace();
        }
        return favoritos;
    }

    /**
     * Agregar un videojuego a favoritos (ignora si ya existe)
     */
    public void agregar(int usuarioId, int videojuegoId) {
        String sql = "INSERT IGNORE INTO favoritos (usuario_id, videojuego_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, videojuegoId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al agregar favorito.");
            e.printStackTrace();
        }
    }

    /**
     * Eliminar un videojuego de favoritos
     */
    public void eliminar(int usuarioId, int videojuegoId) {
        String sql = "DELETE FROM favoritos WHERE usuario_id = ? AND videojuego_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, videojuegoId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar favorito.");
            e.printStackTrace();
        }
    }

    /**
     * Comprobar si un videojuego está en los favoritos de un usuario
     */
    public boolean existe(int usuarioId, int videojuegoId) {
        String sql = "SELECT COUNT(*) FROM favoritos WHERE usuario_id = ? AND videojuego_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            pstmt.setInt(2, videojuegoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar favorito.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Contar favoritos de un usuario
     */
    public int contarPorUsuario(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM favoritos WHERE usuario_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al contar favoritos.");
            e.printStackTrace();
        }
        return 0;
    }

    // ── Mapear ResultSet a Favorito (con datos del videojuego via JOIN) ──
    private Favorito mapearFavorito(ResultSet rs) throws SQLException {
        Favorito f = new Favorito();
        f.setId(rs.getInt("id"));
        f.setUsuarioId(rs.getInt("usuario_id"));
        f.setVideojuegoId(rs.getInt("videojuego_id"));
        Timestamp fecha = rs.getTimestamp("fecha_agregado");
        f.setFechaAgregado(fecha != null ? fecha.toLocalDateTime().toLocalDate() : null);
        // Datos del videojuego (del JOIN)
        f.setTituloVideojuego(rs.getString("titulo"));
        f.setImagenVideojuego(rs.getString("imagen"));
        f.setPrecioVideojuego(rs.getDouble("precio"));
        f.setDisponibleVideojuego(rs.getBoolean("disponible"));
        return f;
    }
}

