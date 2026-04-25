package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.model.Pedido;
import com.ilerna.gameShop.repository.PedidoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar pedidos.
 */
public class PedidoService implements IPedidoService {

    private PedidoRepository pedidoRepository;
    private VideojuegoService videojuegoService;

    public PedidoService() {
        this.pedidoRepository = new PedidoRepository();
        this.videojuegoService = new VideojuegoService();
    }

    /**
     * Crea y guarda un nuevo pedido a partir de los datos del checkout.
     * Actualiza el stock de los productos comprados.
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

        // Estado inicial del pedido (ENUM en BD: PROCESANDO | ENVIADO | ENTREGADO)
        pedido.setEstado("PROCESANDO");

        // Guardar el pedido
        Pedido pedidoGuardado = pedidoRepository.guardar(pedido);

        // Actualizar stock de los productos comprados
        if (pedidoGuardado != null && items != null) {
            for (CarritoItem item : items) {
                videojuegoService.restarStock(item.getVideojuegoId(), item.getCantidad());
            }
        }

        return pedidoGuardado;
    }

    /**
     * Devuelve todos los pedidos de un usuario, del más reciente al más antiguo.
     */
    public List<Pedido> obtenerPedidosDeUsuario(int usuarioId) {
        // El repositorio ya devuelve ordenado por fecha DESC (más reciente primero)
        return pedidoRepository.obtenerPorUsuario(usuarioId);
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

