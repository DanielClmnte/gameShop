package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.MetodoPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MetodoPagoRepository {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    private MetodoPago mapRow(ResultSet rs) throws SQLException {
        MetodoPago m = new MetodoPago();
        m.setId(rs.getInt("id"));
        m.setUsuarioId(rs.getInt("usuario_id"));
        m.setAlias(rs.getString("alias"));
        m.setTipo(rs.getString("tipo"));
        m.setUltimos4(rs.getString("ultimos4"));
        m.setTitular(rs.getString("titular"));
        m.setMesExpiracion(rs.getString("mes_expiracion"));
        m.setAnioExpiracion(rs.getString("anio_expiracion"));
        m.setEsPrincipal(rs.getBoolean("es_principal"));
        Timestamp ts = rs.getTimestamp("fecha_creacion");
        if (ts != null) m.setFechaCreacion(ts.toLocalDateTime());
        return m;
    }

    public List<MetodoPago> obtenerPorUsuario(int usuarioId) {
        List<MetodoPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM metodos_pago WHERE usuario_id = ? ORDER BY es_principal DESC, id ASC";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Optional<MetodoPago> obtenerPorId(int id) {
        String sql = "SELECT * FROM metodos_pago WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public int guardar(MetodoPago m) {
        if (m.isEsPrincipal()) quitarPrincipal(m.getUsuarioId(), 0);
        String sql = "INSERT INTO metodos_pago (usuario_id, alias, tipo, ultimos4, titular, mes_expiracion, anio_expiracion, es_principal) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getUsuarioId());
            ps.setString(2, m.getAlias());
            ps.setString(3, m.getTipo());
            ps.setString(4, m.getUltimos4());
            ps.setString(5, m.getTitular());
            ps.setString(6, m.getMesExpiracion());
            ps.setString(7, m.getAnioExpiracion());
            ps.setBoolean(8, m.isEsPrincipal());
            ps.executeUpdate();
            ResultSet gk = ps.getGeneratedKeys();
            if (gk.next()) return gk.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public void actualizar(MetodoPago m) {
        if (m.isEsPrincipal()) quitarPrincipal(m.getUsuarioId(), m.getId());
        String sql = "UPDATE metodos_pago SET alias=?, tipo=?, titular=?, mes_expiracion=?, anio_expiracion=?, es_principal=? WHERE id=? AND usuario_id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, m.getAlias());
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getTitular());
            ps.setString(4, m.getMesExpiracion());
            ps.setString(5, m.getAnioExpiracion());
            ps.setBoolean(6, m.isEsPrincipal());
            ps.setInt(7, m.getId());
            ps.setInt(8, m.getUsuarioId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminar(int id, int usuarioId) {
        String sql = "DELETE FROM metodos_pago WHERE id = ? AND usuario_id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminarAdmin(int id) {
        try (PreparedStatement ps = getConnection().prepareStatement("DELETE FROM metodos_pago WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<MetodoPago> obtenerTodos() {
        List<MetodoPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM metodos_pago ORDER BY usuario_id, es_principal DESC";
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private void quitarPrincipal(int usuarioId, int exceptoId) {
        String sql = "UPDATE metodos_pago SET es_principal = FALSE WHERE usuario_id = ? AND id != ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, exceptoId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}

