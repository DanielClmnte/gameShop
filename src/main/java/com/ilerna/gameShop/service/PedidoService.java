package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.model.Pedido;
import com.ilerna.gameShop.repository.PedidoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar pedidos.
 */
public class PedidoService {

    private PedidoRepository pedidoRepository;

    public PedidoService() {
        this.pedidoRepository = new PedidoRepository();
    }

    /**
     * Crea y guarda un nuevo pedido a partir de los datos del checkout.
     *
     * @return el Pedido guardado con su ID y número de pedido asignados
     */
    public Pedido crearPedido(int usuarioId,
                               String nombreCompleto, String email,
                               String direccion, String ciudad, String codigoPostal,
                               List<CarritoItem> items,
                               double subtotal) {

        int numeroPedido = generarNumeroPedido();
        double impuestos = subtotal * 0.21;
        double totalConImpuestos = subtotal + impuestos;

        Pedido pedido = new Pedido(
                0, numeroPedido, usuarioId,
                nombreCompleto, email,
                direccion, ciudad, codigoPostal,
                items,
                subtotal, impuestos, totalConImpuestos
        );

        return pedidoRepository.guardar(pedido);
    }

    /**
     * Devuelve todos los pedidos de un usuario, del más reciente al más antiguo.
     */
    public List<Pedido> obtenerPedidosDeUsuario(int usuarioId) {
        List<Pedido> lista = pedidoRepository.obtenerPorUsuario(usuarioId);
        // Invertir para mostrar el más reciente primero
        java.util.Collections.reverse(lista);
        return lista;
    }

    /**
     * Busca un pedido por número de pedido.
     */
    public Optional<Pedido> obtenerPorNumeroPedido(int numeroPedido) {
        return pedidoRepository.obtenerPorNumeroPedido(numeroPedido);
    }

    /** Genera un número de pedido único de 6 dígitos */
    private int generarNumeroPedido() {
        return (int) (System.currentTimeMillis() % 900_000) + 100_000;
    }
}

