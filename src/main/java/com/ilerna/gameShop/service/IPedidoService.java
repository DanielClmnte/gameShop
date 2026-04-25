package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.model.Pedido;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de pedidos.
 * Define la logica de negocio para la gestion de pedidos.
 */
public interface IPedidoService {
    Pedido crearPedido(int usuarioId, String nombreCompleto, String email,
                       String direccion, String ciudad, String codigoPostal,
                       List<CarritoItem> items, double subtotal);
    List<Pedido> obtenerPedidosDeUsuario(int usuarioId);
    Optional<Pedido> obtenerPorNumeroPedido(int numeroPedido);
}

