package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.DireccionUsuario;
import java.util.List;
import java.util.Optional;

/**
 * Contrato del servicio de direcciones de usuario.
 */
public interface IDireccionService {
    List<DireccionUsuario> obtenerPorUsuario(int usuarioId);
    Optional<DireccionUsuario> obtenerPorId(int id);
    String guardar(int usuarioId, String alias, String nombre, String direccion,
                   String ciudad, String codigoPostal, String telefono, boolean esPrincipal);
    String actualizar(int id, int usuarioId, String alias, String nombre, String direccion,
                      String ciudad, String codigoPostal, String telefono, boolean esPrincipal);
    void eliminar(int id, int usuarioId);
    List<DireccionUsuario> obtenerTodas();
    void eliminarAdmin(int id);
}

