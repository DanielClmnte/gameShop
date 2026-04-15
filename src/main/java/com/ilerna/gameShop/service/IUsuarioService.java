package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de usuarios.
 * Define la logica de negocio para la gestion de usuarios y autenticacion.
 */
public interface IUsuarioService {
    List<Usuario> obtenerTodos();
    Optional<Usuario> obtenerPorId(int id);
    Optional<Usuario> obtenerPorEmail(String email);
    boolean registrar(String nombre, String email, String contrasena, String telefono);
    Optional<Usuario> autenticar(String email, String contrasena);
    void actualizarPerfil(int usuarioId, String nombre, String telefono,
                          String direccion, String ciudad, String codigoPostal);
    boolean cambiarContrasena(int usuarioId, String contraseniaActual, String contrasenaNew);
    void desactivarUsuario(int usuarioId);
}

