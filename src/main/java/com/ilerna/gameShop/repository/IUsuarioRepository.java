package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio de usuarios.
 * Define las operaciones CRUD contra la tabla 'usuarios'.
 */
public interface IUsuarioRepository {
    List<Usuario> obtenerTodos();
    Optional<Usuario> obtenerPorId(int id);
    Optional<Usuario> obtenerPorEmail(String email);
    boolean existePorEmail(String email);
    void guardar(Usuario usuario);
    void actualizar(Usuario usuario);
    void eliminarPorId(int id);
    Optional<Usuario> autenticar(String identificador, String contrasena);
}

