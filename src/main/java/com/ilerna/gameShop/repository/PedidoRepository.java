package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Pedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio para gestionar pedidos en memoria.
 * Usa una lista estática para persistir durante la sesión de la aplicación.
 */
public class PedidoRepository {

    private static List<Pedido> pedidos = new ArrayList<>();
    private static int contadorId = 1;

    /**
     * Guarda un nuevo pedido asignando un ID autoincremental.
     */
    public Pedido guardar(Pedido pedido) {
        pedido.setId(contadorId++);
        pedidos.add(pedido);
        return pedido;
    }

    /**
     * Obtiene todos los pedidos de un usuario.
     */
    public List<Pedido> obtenerPorUsuario(int usuarioId) {
        return pedidos.stream()
                .filter(p -> p.getUsuarioId() == usuarioId)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un pedido por su número de pedido.
     */
    public Optional<Pedido> obtenerPorNumeroPedido(int numeroPedido) {
        return pedidos.stream()
                .filter(p -> p.getNumeroPedido() == numeroPedido)
                .findFirst();
    }

    /**
     * Obtiene todos los pedidos (para el panel admin).
     */
    public List<Pedido> obtenerTodos() {
        return new ArrayList<>(pedidos);
    }
}

