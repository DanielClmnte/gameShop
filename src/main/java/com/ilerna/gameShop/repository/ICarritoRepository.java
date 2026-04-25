package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.CarritoItem;
import java.util.List;

/**
 * Interfaz del repositorio del carrito de compras.
 * Define las operaciones CRUD contra la tabla 'carrito_items'.
 */
public interface ICarritoRepository {
    List<CarritoItem> obtenerCarrito(int usuarioId);
    void agregarItem(int usuarioId, CarritoItem item);
    void actualizarCantidad(int usuarioId, int videojuegoId, int nuevaCantidad);
    void eliminarItem(int usuarioId, int videojuegoId);
    void vaciarCarrito(int usuarioId);
    int obtenerCantidadTotal(int usuarioId);
    double obtenerTotalCarrito(int usuarioId);
    boolean existeItem(int usuarioId, int videojuegoId);
    int obtenerCantidadItem(int usuarioId, int videojuegoId);
}

