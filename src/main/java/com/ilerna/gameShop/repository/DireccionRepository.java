package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.DireccionUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DireccionRepository {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    private DireccionUsuario mapRow(ResultSet rs) throws SQLException {
        DireccionUsuario d = new DireccionUsuario();
        d.setId(rs.getInt("id"));
        d.setUsuarioId(rs.getInt("usuario_id"));
        d.setAlias(rs.getString("alias"));
        d.setNombre(rs.getString("nombre"));
        d.setDireccion(rs.getString("direccion"));
        d.setCiudad(rs.getString("ciudad"));
        d.setCodigoPostal(rs.getString("codigo_postal"));
        d.setTelefono(rs.getString("telefono"));
        d.setEsPrincipal(rs.getBoolean("es_principal"));
        Timestamp ts = rs.getTimestamp("fecha_creacion");
        if (ts != null) d.setFechaCreacion(ts.toLocalDateTime());
        return d;
    }

    public List<DireccionUsuario> obtenerPorUsuario(int usuarioId) {
        List<DireccionUsuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM direcciones_usuario WHERE usuario_id = ? ORDER BY es_principal DESC, id ASC";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Optional<DireccionUsuario> obtenerPorId(int id) {
        String sql = "SELECT * FROM direcciones_usuario WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public int guardar(DireccionUsuario d) {
        // Si es principal, quitar la marca de principal en las demás del mismo usuario
        if (d.isEsPrincipal()) quitarPrincipal(d.getUsuarioId(), 0);
        String sql = "INSERT INTO direcciones_usuario (usuario_id, alias, nombre, direccion, ciudad, codigo_postal, telefono, es_principal) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, d.getUsuarioId());
            ps.setString(2, d.getAlias());
            ps.setString(3, d.getNombre());
            ps.setString(4, d.getDireccion());
            ps.setString(5, d.getCiudad());
            ps.setString(6, d.getCodigoPostal());
            ps.setString(7, d.getTelefono());
            ps.setBoolean(8, d.isEsPrincipal());
            ps.executeUpdate();
            ResultSet gk = ps.getGeneratedKeys();
            if (gk.next()) return gk.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public void actualizar(DireccionUsuario d) {
        if (d.isEsPrincipal()) quitarPrincipal(d.getUsuarioId(), d.getId());
        String sql = "UPDATE direcciones_usuario SET alias=?, nombre=?, direccion=?, ciudad=?, codigo_postal=?, telefono=?, es_principal=? WHERE id=? AND usuario_id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, d.getAlias());
            ps.setString(2, d.getNombre());
            ps.setString(3, d.getDireccion());
            ps.setString(4, d.getCiudad());
            ps.setString(5, d.getCodigoPostal());
            ps.setString(6, d.getTelefono());
            ps.setBoolean(7, d.isEsPrincipal());
            ps.setInt(8, d.getId());
            ps.setInt(9, d.getUsuarioId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminar(int id, int usuarioId) {
        String sql = "DELETE FROM direcciones_usuario WHERE id = ? AND usuario_id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /** Admin: eliminar cualquiera */
    public void eliminarAdmin(int id) {
        try (PreparedStatement ps = getConnection().prepareStatement("DELETE FROM direcciones_usuario WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /** Admin: ver todas las direcciones de todos los usuarios */
    public List<DireccionUsuario> obtenerTodas() {
        List<DireccionUsuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM direcciones_usuario ORDER BY usuario_id, es_principal DESC";
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    private void quitarPrincipal(int usuarioId, int exceptoId) {
        String sql = "UPDATE direcciones_usuario SET es_principal = FALSE WHERE usuario_id = ? AND id != ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, exceptoId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}

