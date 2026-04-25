package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.CarritoItem;
import java.util.List;

/**
 * Interfaz del servicio del carrito de compras.
 * Define la logica de negocio para la gestion del carrito.
 */
public interface ICarritoService {
    List<CarritoItem> obtenerCarrito(int usuarioId);
    boolean agregarAlCarrito(int usuarioId, int videojuegoId, int cantidad);
    boolean actualizarCantidad(int usuarioId, int videojuegoId, int nuevaCantidad);
    void eliminarDelCarrito(int usuarioId, int videojuegoId);
    void vaciarCarrito(int usuarioId);
    int obtenerCantidadTotal(int usuarioId);
    double obtenerTotalCarrito(int usuarioId);
    boolean estaVacio(int usuarioId);
}

