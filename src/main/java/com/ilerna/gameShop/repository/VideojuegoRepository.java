package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Videojuego;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar videojuegos.
 * Consultas SQL contra la tabla 'videojuegos' de gameshop_db.
 */
public class VideojuegoRepository implements IVideojuegoRepository {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    /**
     * Obtener todos los videojuegos
     */
    public List<Videojuego> obtenerTodos() {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                videojuegos.add(mapearVideojuego(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar videojuegos.");
            e.printStackTrace();
        }
        return videojuegos;
    }

    /**
     * Obtener videojuegos por plataforma
     */
    public List<Videojuego> obtenerPorPlataforma(int plataformaId) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE plataforma_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, plataformaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    videojuegos.add(mapearVideojuego(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener videojuegos por plataforma.");
            e.printStackTrace();
        }
        return videojuegos;
    }

    /**
     * Obtener un videojuego por ID
     */
    public Optional<Videojuego> obtenerPorId(int id) {
        String sql = "SELECT * FROM videojuegos WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearVideojuego(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener videojuego por ID.");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Buscar videojuegos por título (búsqueda parcial)
     */
    public List<Videojuego> buscarPorTitulo(String titulo) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE titulo LIKE ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    videojuegos.add(mapearVideojuego(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar videojuegos por título.");
            e.printStackTrace();
        }
        return videojuegos;
    }

    /**
     * Obtener videojuegos disponibles (en stock)
     */
    public List<Videojuego> obtenerDisponibles() {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE disponible = TRUE";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                videojuegos.add(mapearVideojuego(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener videojuegos disponibles.");
            e.printStackTrace();
        }
        return videojuegos;
    }

    /**
     * Obtener videojuegos ordenados por calificación (de mayor a menor)
     */
    public List<Videojuego> obtenerPorCalificacion() {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos ORDER BY calificacion DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                videojuegos.add(mapearVideojuego(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener videojuegos por calificación.");
            e.printStackTrace();
        }
        return videojuegos;
    }

    /**
     * Guardar un nuevo videojuego (ID asignado por AUTO_INCREMENT)
     */
    public void guardar(Videojuego videojuego) {
        String sql = "INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, fecha_lanzamiento, desarrollador, calificacion, disponible) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, videojuego.getTitulo());
            pstmt.setString(2, videojuego.getDescripcion());
            pstmt.setDouble(3, videojuego.getPrecio());
            pstmt.setInt(4, videojuego.getStock());
            pstmt.setInt(5, videojuego.getPlataformaId());
            pstmt.setString(6, videojuego.getImagen());
            pstmt.setDate(7, videojuego.getFechaLanzamiento() != null
                    ? Date.valueOf(videojuego.getFechaLanzamiento()) : null);
            pstmt.setString(8, videojuego.getDesarrollador());
            pstmt.setDouble(9, videojuego.getCalificacion());
            pstmt.setBoolean(10, videojuego.getStock() > 0);
            pstmt.executeUpdate();

            // Obtener el ID generado
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    videojuego.setId(keys.getInt(1));
                }
            }
            System.out.println("✅ Videojuego guardado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar videojuego.");
            e.printStackTrace();
        }
    }

    /**
     * Actualizar un videojuego existente
     */
    public void actualizar(Videojuego videojuego) {
        String sql = "UPDATE videojuegos SET titulo = ?, descripcion = ?, precio = ?, stock = ?, "
                   + "plataforma_id = ?, imagen = ?, fecha_lanzamiento = ?, desarrollador = ?, "
                   + "calificacion = ?, disponible = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, videojuego.getTitulo());
            pstmt.setString(2, videojuego.getDescripcion());
            pstmt.setDouble(3, videojuego.getPrecio());
            pstmt.setInt(4, videojuego.getStock());
            pstmt.setInt(5, videojuego.getPlataformaId());
            pstmt.setString(6, videojuego.getImagen());
            pstmt.setDate(7, videojuego.getFechaLanzamiento() != null
                    ? Date.valueOf(videojuego.getFechaLanzamiento()) : null);
            pstmt.setString(8, videojuego.getDesarrollador());
            pstmt.setDouble(9, videojuego.getCalificacion());
            pstmt.setBoolean(10, videojuego.getStock() > 0);
            pstmt.setInt(11, videojuego.getId());
            pstmt.executeUpdate();
            System.out.println("✅ Videojuego actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar videojuego.");
            e.printStackTrace();
        }
    }

    /**
     * Eliminar un videojuego por ID
     */
    public void eliminarPorId(int id) {
        String sql = "DELETE FROM videojuegos WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Videojuego eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar videojuego.");
            e.printStackTrace();
        }
    }

    // ── Mapear ResultSet a Videojuego ──
    private Videojuego mapearVideojuego(ResultSet rs) throws SQLException {
        Videojuego vj = new Videojuego();
        vj.setId(rs.getInt("id"));
        vj.setTitulo(rs.getString("titulo"));
        vj.setDescripcion(rs.getString("descripcion"));
        vj.setPrecio(rs.getDouble("precio"));
        vj.setStock(rs.getInt("stock"));
        vj.setPlataformaId(rs.getInt("plataforma_id"));
        vj.setImagen(rs.getString("imagen"));
        Date fechaLanz = rs.getDate("fecha_lanzamiento");
        vj.setFechaLanzamiento(fechaLanz != null ? fechaLanz.toLocalDate() : null);
        vj.setDesarrollador(rs.getString("desarrollador"));
        vj.setCalificacion(rs.getDouble("calificacion"));
        vj.setDisponible(rs.getBoolean("disponible"));
        return vj;
    }
}
