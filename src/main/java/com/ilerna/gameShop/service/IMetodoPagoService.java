package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.MetodoPago;
import java.util.List;
import java.util.Optional;

/**
 * Contrato del servicio de métodos de pago.
 */
public interface IMetodoPagoService {
    List<MetodoPago> obtenerPorUsuario(int usuarioId);
    Optional<MetodoPago> obtenerPorId(int id);
    String guardar(int usuarioId, String alias, String tipo, String numeroCompleto,
                   String titular, String mes, String anio, boolean esPrincipal);
    String actualizar(int id, int usuarioId, String alias, String tipo,
                      String titular, String mes, String anio, boolean esPrincipal);
    void eliminar(int id, int usuarioId);
    List<MetodoPago> obtenerTodos();
    void eliminarAdmin(int id);
}

