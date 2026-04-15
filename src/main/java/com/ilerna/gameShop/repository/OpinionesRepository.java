package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Opiniones;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar opiniones de usuarios.
 * Consultas SQL contra la tabla 'opiniones' de gameshop_db.
 */
public class OpinionesRepository {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    /**
     * Obtener todas las opiniones
     */
    public List<Opiniones> obtenerTodas() {
        List<Opiniones> opiniones = new ArrayList<>();
        String sql = "SELECT * FROM opiniones";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                opiniones.add(mapearOpinion(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar opiniones.");
            e.printStackTrace();
        }
        return opiniones;
    }

    /**
     * Obtener opiniones de un videojuego específico
     */
    public List<Opiniones> obtenerPorVideojuego(int videojuegoId) {
        List<Opiniones> opiniones = new ArrayList<>();
        String sql = "SELECT * FROM opiniones WHERE videojuego_id = ? ORDER BY fecha DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, videojuegoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    opiniones.add(mapearOpinion(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener opiniones por videojuego.");
            e.printStackTrace();
        }
        return opiniones;
    }

    /**
     * Obtener opiniones de un usuario específico
     */
    public List<Opiniones> obtenerPorUsuario(int usuarioId) {
        List<Opiniones> opiniones = new ArrayList<>();
        String sql = "SELECT * FROM opiniones WHERE usuario_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    opiniones.add(mapearOpinion(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener opiniones por usuario.");
            e.printStackTrace();
        }
        return opiniones;
    }

    /**
     * Obtener una opinión por ID
     */
    public Optional<Opiniones> obtenerPorId(int id) {
        String sql = "SELECT * FROM opiniones WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearOpinion(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener opinión por ID.");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Obtener calificación promedio de un videojuego
     */
    public double obtenerCalificacionPromedio(int videojuegoId) {
        String sql = "SELECT AVG(calificacion) AS promedio FROM opiniones WHERE videojuego_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, videojuegoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("promedio");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener calificación promedio.");
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Guardar una nueva opinión
     */
    public void guardar(Opiniones opinion) {
        String sql = "INSERT INTO opiniones (videojuego_id, usuario_id, calificacion, comentario, verificado) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, opinion.getVideojuegoId());
            pstmt.setInt(2, opinion.getUsuarioId());
            pstmt.setInt(3, opinion.getCalificacion());
            pstmt.setString(4, opinion.getComentario());
            pstmt.setBoolean(5, opinion.isVerificado());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    opinion.setId(keys.getInt(1));
                }
            }
            System.out.println("✅ Opinión guardada correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar opinión.");
            e.printStackTrace();
        }
    }

    /**
     * Actualizar una opinión existente
     */
    public void actualizar(Opiniones opinion) {
        String sql = "UPDATE opiniones SET calificacion = ?, comentario = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, opinion.getCalificacion());
            pstmt.setString(2, opinion.getComentario());
            pstmt.setInt(3, opinion.getId());
            pstmt.executeUpdate();
            System.out.println("✅ Opinión actualizada correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar opinión.");
            e.printStackTrace();
        }
    }

    /**
     * Eliminar una opinión por ID
     */
    public void eliminarPorId(int id) {
        String sql = "DELETE FROM opiniones WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Opinión eliminada correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar opinión.");
            e.printStackTrace();
        }
    }

    // ── Mapear ResultSet a Opiniones ──
    private Opiniones mapearOpinion(ResultSet rs) throws SQLException {
        Opiniones o = new Opiniones();
        o.setId(rs.getInt("id"));
        o.setVideojuegoId(rs.getInt("videojuego_id"));
        o.setUsuarioId(rs.getInt("usuario_id"));
        o.setCalificacion(rs.getInt("calificacion"));
        o.setComentario(rs.getString("comentario"));
        Timestamp fecha = rs.getTimestamp("fecha");
        o.setFecha(fecha != null ? fecha.toLocalDateTime().toLocalDate() : null);
        o.setVerificado(rs.getBoolean("verificado"));
        return o;
    }
}
