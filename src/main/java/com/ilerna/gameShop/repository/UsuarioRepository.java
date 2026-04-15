package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar usuarios.
 * Consultas SQL contra la tabla 'usuarios' de gameshop_db.
 */
public class UsuarioRepository implements IUsuarioRepository {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    /**
     * Obtener todos los usuarios
     */
    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar usuarios.");
            e.printStackTrace();
        }
        return usuarios;
    }

    /**
     * Obtener un usuario por ID
     */
    public Optional<Usuario> obtenerPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario por ID.");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Obtener un usuario por email
     */
    public Optional<Usuario> obtenerPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuario por email.");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Verificar si un usuario existe por email
     */
    public boolean existePorEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar email.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Guardar un nuevo usuario (ID asignado por AUTO_INCREMENT)
     */
    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, contrasena, telefono, direccion, ciudad, codigo_postal, rol, activo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setString(4, usuario.getTelefono());
            pstmt.setString(5, usuario.getDireccion());
            pstmt.setString(6, usuario.getCiudad());
            pstmt.setString(7, usuario.getCodigoPostal());
            pstmt.setString(8, usuario.getRol() != null ? usuario.getRol() : "CLIENTE");
            pstmt.setBoolean(9, usuario.isActivo());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setId(keys.getInt(1));
                }
            }
            System.out.println("✅ Usuario guardado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar usuario.");
            e.printStackTrace();
        }
    }

    /**
     * Actualizar un usuario existente
     */
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, telefono = ?, direccion = ?, "
                   + "ciudad = ?, codigo_postal = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getTelefono());
            pstmt.setString(4, usuario.getDireccion());
            pstmt.setString(5, usuario.getCiudad());
            pstmt.setString(6, usuario.getCodigoPostal());
            pstmt.setInt(7, usuario.getId());
            pstmt.executeUpdate();
            System.out.println("✅ Usuario actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar usuario.");
            e.printStackTrace();
        }
    }

    /**
     * Eliminar un usuario por ID
     */
    public void eliminarPorId(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Usuario eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar usuario.");
            e.printStackTrace();
        }
    }

    /**
     * Autenticar usuario (login) — acepta email O nombre de usuario
     */
    public Optional<Usuario> autenticar(String identificador, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE (email = ? OR nombre = ?) AND contrasena = ? AND activo = TRUE";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, identificador);
            pstmt.setString(2, identificador);
            pstmt.setString(3, contrasena);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al autenticar usuario.");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // ── Mapear ResultSet a Usuario ──
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombre(rs.getString("nombre"));
        u.setEmail(rs.getString("email"));
        u.setContrasena(rs.getString("contrasena"));
        u.setTelefono(rs.getString("telefono"));
        u.setDireccion(rs.getString("direccion"));
        u.setCiudad(rs.getString("ciudad"));
        u.setCodigoPostal(rs.getString("codigo_postal"));
        u.setRol(rs.getString("rol"));
        u.setActivo(rs.getBoolean("activo"));
        Timestamp fechaReg = rs.getTimestamp("fecha_registro");
        u.setFechaRegistro(fechaReg != null ? fechaReg.toLocalDateTime().toLocalDate() : null);
        return u;
    }
}
