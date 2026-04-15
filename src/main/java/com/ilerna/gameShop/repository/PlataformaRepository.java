package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Plataforma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar plataformas.
 * Consultas SQL contra la tabla 'plataformas' de gameshop_db.
 */
public class PlataformaRepository {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    /**
     * Obtener todas las plataformas
     */
    public List<Plataforma> obtenerTodas() {
        List<Plataforma> plataformas = new ArrayList<>();
        String sql = "SELECT * FROM plataformas";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                plataformas.add(mapearPlataforma(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar plataformas.");
            e.printStackTrace();
        }
        return plataformas;
    }

    /**
     * Obtener una plataforma por ID
     */
    public Optional<Plataforma> obtenerPorId(int id) {
        String sql = "SELECT * FROM plataformas WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearPlataforma(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener plataforma por ID.");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Obtener una plataforma por nombre
     */
    public Optional<Plataforma> obtenerPorNombre(String nombre) {
        String sql = "SELECT * FROM plataformas WHERE nombre = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearPlataforma(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener plataforma por nombre.");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Guardar una nueva plataforma
     */
    public void guardar(Plataforma plataforma) {
        String sql = "INSERT INTO plataformas (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, plataforma.getNombre());
            pstmt.setString(2, plataforma.getDescripcion());
            pstmt.executeUpdate();
            System.out.println("✅ Plataforma guardada correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar plataforma.");
            e.printStackTrace();
        }
    }

    /**
     * Actualizar una plataforma existente
     */
    public void actualizar(Plataforma plataforma) {
        String sql = "UPDATE plataformas SET nombre = ?, descripcion = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, plataforma.getNombre());
            pstmt.setString(2, plataforma.getDescripcion());
            pstmt.setInt(3, plataforma.getId());
            pstmt.executeUpdate();
            System.out.println("✅ Plataforma actualizada correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar plataforma.");
            e.printStackTrace();
        }
    }

    /**
     * Eliminar una plataforma por ID
     */
    public void eliminarPorId(int id) {
        String sql = "DELETE FROM plataformas WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Plataforma eliminada correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar plataforma.");
            e.printStackTrace();
        }
    }

    // ── Mapear ResultSet a Plataforma ──
    private Plataforma mapearPlataforma(ResultSet rs) throws SQLException {
        Plataforma p = new Plataforma();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        return p;
    }
}
