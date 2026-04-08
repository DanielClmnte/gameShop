package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Usuario;
import com.ilerna.gameShop.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar usuarios
 * Contiene la lógica de negocio relacionada con usuarios
 */
public class UsuarioService {
    
    private UsuarioRepository usuarioRepository;
    
    // Constructor
    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }
    
    /**
     * Obtener todos los usuarios
     */
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.obtenerTodos();
    }
    
    /**
     * Obtener un usuario por ID
     */
    public Optional<Usuario> obtenerPorId(int id) {
        return usuarioRepository.obtenerPorId(id);
    }
    
    /**
     * Obtener un usuario por email
     */
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.obtenerPorEmail(email);
    }
    
    /**
     * Registrar un nuevo usuario
     */
    public boolean registrar(String nombre, String email, String contrasena, String telefono) {
        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            contrasena == null || contrasena.length() < 4) {
            return false;
        }
        
        // Verificar si el email ya existe
        if (usuarioRepository.existePorEmail(email)) {
            return false;
        }
        
        Usuario nuevoUsuario = new Usuario(0, nombre, email, contrasena, "CLIENTE");
        nuevoUsuario.setTelefono(telefono);
        usuarioRepository.guardar(nuevoUsuario);
        return true;
    }
    
    /**
     * Autenticar usuario (login)
     */
    public Optional<Usuario> autenticar(String email, String contrasena) {
        return usuarioRepository.autenticar(email, contrasena);
    }
    
    /**
     * Actualizar perfil de usuario
     */
    public void actualizarPerfil(int usuarioId, String nombre, String telefono, 
                                 String direccion, String ciudad, String codigoPostal) {
        Optional<Usuario> usuario = usuarioRepository.obtenerPorId(usuarioId);
        
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            u.setNombre(nombre);
            u.setTelefono(telefono);
            u.setDireccion(direccion);
            u.setCiudad(ciudad);
            u.setCodigoPostal(codigoPostal);
            usuarioRepository.actualizar(u);
        }
    }
    
    /**
     * Cambiar contraseña
     */
    public boolean cambiarContrasena(int usuarioId, String contraseniaActual, String contrasenaNew) {
        Optional<Usuario> usuario = usuarioRepository.obtenerPorId(usuarioId);
        
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            // Verificar que la contraseña actual sea correcta
            if (u.getContrasena().equals(contraseniaActual)) {
                u.setContrasena(contrasenaNew);
                usuarioRepository.actualizar(u);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Desactivar usuario
     */
    public void desactivarUsuario(int usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.obtenerPorId(usuarioId);
        usuario.ifPresent(u -> u.setActivo(false));
    }
}

