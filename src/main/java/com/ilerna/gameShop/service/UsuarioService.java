package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Usuario;
import com.ilerna.gameShop.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar usuarios
 * Contiene la lógica de negocio relacionada con usuarios
 */
public class UsuarioService implements IUsuarioService {
    
    private UsuarioRepository usuarioRepository;
    
    // Constructor
    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }
    
    /**
     * Obtener todos los usuarios
     */
    public List<Usuario> obtenerTodos() { return usuarioRepository.obtenerTodos(); }

    /**
     * Obtener un usuario por ID
     */
    public Optional<Usuario> obtenerPorId(int id) { return usuarioRepository.obtenerPorId(id); }

    /**
     * Obtener un usuario por email
     */
    public Optional<Usuario> obtenerPorEmail(String email) { return usuarioRepository.obtenerPorEmail(email); }

    /**
     * Registrar un nuevo usuario
     */
    public boolean registrar(String nombre, String email, String contrasena, String telefono) {
        if (nombre == null || nombre.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || contrasena == null || contrasena.length() < 4) {
            return false;
        }
        if (usuarioRepository.existePorEmail(email)) return false;
        Usuario u = new Usuario(0, nombre.trim(), email.trim(), contrasena, "CLIENTE");
        u.setTelefono(telefono);
        usuarioRepository.guardar(u);
        return true;
    }

    /**
     * Autenticar usuario (login)
     */
    public Optional<Usuario> autenticar(String email, String contrasena) {
        return usuarioRepository.autenticar(email, contrasena);
    }

    /** Actualiza datos de perfil (sin contraseña). */
    public boolean actualizarPerfil(int usuarioId, String nombre, String email,
                                    String telefono, String direccion,
                                    String ciudad, String codigoPostal) {
        Optional<Usuario> opt = usuarioRepository.obtenerPorId(usuarioId);
        if (opt.isEmpty()) return false;
        // Verificar que el nuevo email no esté en uso por OTRO usuario
        Optional<Usuario> porEmail = usuarioRepository.obtenerPorEmail(email);
        if (porEmail.isPresent() && porEmail.get().getId() != usuarioId) return false;

        Usuario u = opt.get();
        u.setNombre(nombre == null ? u.getNombre() : nombre.trim());
        u.setEmail(email == null ? u.getEmail() : email.trim());
        u.setTelefono(telefono);
        u.setDireccion(direccion);
        u.setCiudad(ciudad);
        u.setCodigoPostal(codigoPostal);
        usuarioRepository.actualizar(u);
        return true;
    }

    /** Cambia la contraseña verificando la actual. */
    public boolean cambiarContrasena(int usuarioId, String contraseniaActual, String contrasenaNew) {
        Optional<Usuario> opt = usuarioRepository.obtenerPorId(usuarioId);
        if (opt.isEmpty()) return false;
        Usuario u = opt.get();
        if (!u.getContrasena().equals(contraseniaActual)) return false;
        u.setContrasena(contrasenaNew);
        usuarioRepository.actualizarConContrasena(u);
        return true;
    }

    // ── Admin: CRUD completo ──────────────────────────────────────────────────

    /** Crea un usuario desde el panel admin (puede ser CLIENTE o ADMIN). */
    public boolean crearUsuarioAdmin(String nombre, String email, String contrasena,
                                     String telefono, String rol) {
        if (nombre == null || nombre.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || contrasena == null || contrasena.length() < 4) return false;
        if (usuarioRepository.existePorEmail(email)) return false;
        Usuario u = new Usuario(0, nombre.trim(), email.trim(), contrasena,
                (rol != null && rol.equals("ADMIN")) ? "ADMIN" : "CLIENTE");
        u.setTelefono(telefono);
        usuarioRepository.guardar(u);
        return true;
    }

    /** Actualiza cualquier campo de un usuario (uso admin). */
    public boolean actualizarUsuarioAdmin(int id, String nombre, String email,
                                          String telefono, String direccion,
                                          String ciudad, String codigoPostal,
                                          String rol, boolean activo) {
        Optional<Usuario> opt = usuarioRepository.obtenerPorId(id);
        if (opt.isEmpty()) return false;
        Optional<Usuario> porEmail = usuarioRepository.obtenerPorEmail(email);
        if (porEmail.isPresent() && porEmail.get().getId() != id) return false;

        Usuario u = opt.get();
        u.setNombre(nombre == null ? u.getNombre() : nombre.trim());
        u.setEmail(email == null ? u.getEmail() : email.trim());
        u.setTelefono(telefono);
        u.setDireccion(direccion);
        u.setCiudad(ciudad);
        u.setCodigoPostal(codigoPostal);
        u.setRol((rol != null && rol.equals("ADMIN")) ? "ADMIN" : "CLIENTE");
        u.setActivo(activo);
        usuarioRepository.actualizarAdmin(u);
        return true;
    }

    /** Elimina un usuario por ID. */
    public void eliminarUsuario(int id) {
        usuarioRepository.eliminarPorId(id);
    }
}

