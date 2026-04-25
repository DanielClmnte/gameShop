package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Videojuego;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VideojuegoRepository implements IVideojuegoRepository {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    // --- MÉTODOS BÁSICOS (CRUD) ---
    public List<Videojuego> obtenerTodos() {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) { videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public List<Videojuego> obtenerPorPlataforma(int plataformaId) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE plataforma_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, plataformaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { videojuegos.add(mapearVideojuego(rs)); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public Optional<Videojuego> obtenerPorId(int id) {
        String sql = "SELECT * FROM videojuegos WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapearVideojuego(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public List<Videojuego> buscarPorTitulo(String titulo) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE titulo LIKE ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { videojuegos.add(mapearVideojuego(rs)); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public List<Videojuego> obtenerDisponibles() {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE disponible = TRUE";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) { videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public List<Videojuego> obtenerPorCalificacion() {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos ORDER BY calificacion DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) { videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public void guardar(Videojuego videojuego) {
        String sql = "INSERT INTO videojuegos (titulo, descripcion, precio, stock, plataforma_id, imagen, fecha_lanzamiento, desarrollador, calificacion, disponible) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, videojuego.getTitulo());
            pstmt.setString(2, videojuego.getDescripcion());
            pstmt.setDouble(3, videojuego.getPrecio());
            pstmt.setInt(4, videojuego.getStock());
            pstmt.setInt(5, videojuego.getPlataformaId());
            pstmt.setString(6, videojuego.getImagen());
            pstmt.setDate(7, videojuego.getFechaLanzamiento() != null ? Date.valueOf(videojuego.getFechaLanzamiento()) : null);
            pstmt.setString(8, videojuego.getDesarrollador());
            pstmt.setDouble(9, videojuego.getCalificacion());
            pstmt.setBoolean(10, videojuego.getStock() > 0);
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) { if (keys.next()) videojuego.setId(keys.getInt(1)); }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void actualizar(Videojuego videojuego) {
        String sql = "UPDATE videojuegos SET titulo=?, descripcion=?, precio=?, stock=?, plataforma_id=?, imagen=?, fecha_lanzamiento=?, desarrollador=?, calificacion=?, disponible=? WHERE id=?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, videojuego.getTitulo());
            pstmt.setString(2, videojuego.getDescripcion());
            pstmt.setDouble(3, videojuego.getPrecio());
            pstmt.setInt(4, videojuego.getStock());
            pstmt.setInt(5, videojuego.getPlataformaId());
            pstmt.setString(6, videojuego.getImagen());
            pstmt.setDate(7, videojuego.getFechaLanzamiento() != null ? Date.valueOf(videojuego.getFechaLanzamiento()) : null);
            pstmt.setString(8, videojuego.getDesarrollador());
            pstmt.setDouble(9, videojuego.getCalificacion());
            pstmt.setBoolean(10, videojuego.getStock() > 0);
            pstmt.setInt(11, videojuego.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminarPorId(int id) {
        String sql = "DELETE FROM videojuegos WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- MÉTODOS DE PAGINACIÓN ---
    public List<Videojuego> obtenerTodosPaginado(int offset, int limit) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos ORDER BY id DESC LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit); pstmt.setInt(2, offset);
            try (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public int contarTodos() {
        String sql = "SELECT COUNT(*) FROM videojuegos";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) { if (rs.next()) return rs.getInt(1); }
        catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<Videojuego> obtenerPorPlataformaPaginado(int plataformaId, int offset, int limit) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE plataforma_id = ? ORDER BY id DESC LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, plataformaId); pstmt.setInt(2, limit); pstmt.setInt(3, offset);
            try (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public int contarPorPlataforma(int plataformaId) {
        String sql = "SELECT COUNT(*) FROM videojuegos WHERE plataforma_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, plataformaId);
            try (ResultSet rs = pstmt.executeQuery()) { if (rs.next()) return rs.getInt(1); }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<Videojuego> obtenerDisponiblesPaginado(int offset, int limit) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE disponible = TRUE ORDER BY id DESC LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit); pstmt.setInt(2, offset);
            try (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public int contarDisponibles() {
        String sql = "SELECT COUNT(*) FROM videojuegos WHERE disponible = TRUE";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) { if (rs.next()) return rs.getInt(1); }
        catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<Videojuego> buscarPorTituloPaginado(String titulo, int offset, int limit) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE titulo LIKE ? LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, "%" + titulo + "%"); pstmt.setInt(2, limit); pstmt.setInt(3, offset);
            try (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public int contarPorTitulo(String titulo) {
        String sql = "SELECT COUNT(*) FROM videojuegos WHERE titulo LIKE ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = pstmt.executeQuery()) { if (rs.next()) return rs.getInt(1); }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // --- NOVEDADES ---
    public List<Videojuego> obtenerNovedades(int limit) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE fecha_lanzamiento IS NOT NULL ORDER BY fecha_lanzamiento DESC LIMIT ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public List<Videojuego> obtenerNovedadesPaginado(int offset, int limit) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT * FROM videojuegos WHERE fecha_lanzamiento IS NOT NULL ORDER BY fecha_lanzamiento DESC LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit); pstmt.setInt(2, offset);
            try (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public int contarNovedades() {
        String sql = "SELECT COUNT(*) FROM videojuegos WHERE fecha_lanzamiento IS NOT NULL";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) { if (rs.next()) return rs.getInt(1); }
        catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // --- POPULARES ---
    public List<Videojuego> obtenerPopulares(int limit) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT v.*, " +
                "((COALESCE((SELECT SUM(cantidad) FROM detalle_pedidos WHERE videojuego_id = v.id), 0) * 0.6) + " +
                "(COALESCE((SELECT COUNT(*) FROM opiniones WHERE videojuego_id = v.id), 0) * 0.4)) AS score_popularidad " +
                "FROM videojuegos v ORDER BY score_popularidad DESC, v.calificacion DESC LIMIT ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public List<Videojuego> obtenerPopularesPaginado(int offset, int limit) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT v.*, " +
                "((COALESCE((SELECT SUM(cantidad) FROM detalle_pedidos WHERE videojuego_id = v.id), 0) * 0.6) + " +
                "(COALESCE((SELECT COUNT(*) FROM opiniones WHERE videojuego_id = v.id), 0) * 0.4)) AS score_popularidad " +
                "FROM videojuegos v ORDER BY score_popularidad DESC, v.calificacion DESC LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit); pstmt.setInt(2, offset);
            try (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public int contarPopulares() { return contarTodos(); }

    // --- MÁS VENDIDOS ---
    public List<Videojuego> obtenerMasVendidos(int limit) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT v.* FROM videojuegos v LEFT JOIN detalle_pedidos dp ON v.id = dp.videojuego_id " +
                "GROUP BY v.id ORDER BY COALESCE(SUM(dp.cantidad), 0) DESC LIMIT ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public List<Videojuego> obtenerMasVendidosPaginado(int offset, int limit) {
        List<Videojuego> videojuegos = new ArrayList<>();
        String sql = "SELECT v.* FROM videojuegos v LEFT JOIN detalle_pedidos dp ON v.id = dp.videojuego_id " +
                "GROUP BY v.id ORDER BY COALESCE(SUM(dp.cantidad), 0) DESC LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit); pstmt.setInt(2, offset);
            try (ResultSet rs = pstmt.executeQuery()) { while (rs.next()) videojuegos.add(mapearVideojuego(rs)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return videojuegos;
    }

    public int contarMasVendidos() { return contarTodos(); }

    private Videojuego mapearVideojuego(ResultSet rs) throws SQLException {
        Videojuego vj = new Videojuego();
        vj.setId(rs.getInt("id"));
        vj.setTitulo(rs.getString("titulo"));
        vj.setDescripcion(rs.getString("descripcion"));
        vj.setPrecio(rs.getDouble("precio"));
        vj.setStock(rs.getInt("stock"));
        vj.setPlataformaId(rs.getInt("plataforma_id"));
        vj.setImagen(rs.getString("imagen"));
        Date fecha = rs.getDate("fecha_lanzamiento");
        if (fecha != null) vj.setFechaLanzamiento(fecha.toLocalDate());
        vj.setDesarrollador(rs.getString("desarrollador"));
        vj.setCalificacion(rs.getDouble("calificacion"));
        vj.setDisponible(rs.getBoolean("disponible"));
        return vj;
    }
}
