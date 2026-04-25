package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Pedido;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio de pedidos.
 * Define las operaciones contra las tablas 'pedidos' y 'detalle_pedidos'.
 */
public interface IPedidoRepository {
    Pedido guardar(Pedido pedido);
    List<Pedido> obtenerPorUsuario(int usuarioId);
    Optional<Pedido> obtenerPorNumeroPedido(int numeroPedido);
    List<Pedido> obtenerTodos();
}

