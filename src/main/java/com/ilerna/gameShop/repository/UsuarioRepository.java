package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar usuarios
 * Simula consultas a la base de datos (sin JPA por ahora)
 */
public class UsuarioRepository {
    
    // Simulamos una lista de usuarios en memoria
    private static List<Usuario> usuarios = new ArrayList<>();
    
    static {
        // Datos iniciales de prueba
        Usuario admin = new Usuario(1, "Admin GameShop", "admin@gameshop.com", "admin123", "ADMIN");
        admin.setTelefono("666111222");
        usuarios.add(admin);
        
        Usuario usuario1 = new Usuario(2, "Juan Pérez", "juan@example.com", "pass123", "CLIENTE");
        usuario1.setTelefono("666222333");
        usuarios.add(usuario1);
        
        Usuario usuario2 = new Usuario(3, "María García", "maria@example.com", "pass456", "CLIENTE");
        usuario2.setTelefono("666333444");
        usuarios.add(usuario2);
    }
    
    /**
     * Obtener todos los usuarios
     */
    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }
    
    /**
     * Obtener un usuario por ID
     */
    public Optional<Usuario> obtenerPorId(int id) {
        return usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }
    
    /**
     * Obtener un usuario por email
     */
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
    
    /**
     * Verificar si un usuario existe por email
     */
    public boolean existePorEmail(String email) {
        return usuarios.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
    
    /**
     * Guardar un nuevo usuario
     */
    public void guardar(Usuario usuario) {
        if (!existePorEmail(usuario.getEmail())) {
            // Asignar ID automático
            int nuevoId = usuarios.stream()
                    .mapToInt(Usuario::getId)
                    .max()
                    .orElse(0) + 1;
            usuario.setId(nuevoId);
            usuarios.add(usuario);
        }
    }
    
    /**
     * Actualizar un usuario existente
     */
    public void actualizar(Usuario usuario) {
        usuarios.stream()
                .filter(u -> u.getId() == usuario.getId())
                .findFirst()
                .ifPresent(u -> {
                    u.setNombre(usuario.getNombre());
                    u.setEmail(usuario.getEmail());
                    u.setTelefono(usuario.getTelefono());
                    u.setDireccion(usuario.getDireccion());
                    u.setCiudad(usuario.getCiudad());
                    u.setCodigoPostal(usuario.getCodigoPostal());
                });
    }
    
    /**
     * Eliminar un usuario por ID
     */
    public void eliminarPorId(int id) {
        usuarios.removeIf(u -> u.getId() == id);
    }
    
    /**
     * Autenticar usuario (login)
     */
    public Optional<Usuario> autenticar(String email, String contrasena) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) 
                         && u.getContrasena().equals(contrasena)
                         && u.isActivo())
                .findFirst();
    }
}

