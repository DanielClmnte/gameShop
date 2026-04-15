package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.repository.CarritoRepository;
import java.util.List;

/**
 * Servicio para gestionar el carrito de compras
 * Contiene la lógica de negocio del carrito
 */
public class CarritoService implements ICarritoService {
    
    private CarritoRepository carritoRepository;
    private VideojuegoService videojuegoService;
    
    // Constructor
    public CarritoService() {
        this.carritoRepository = new CarritoRepository();
        this.videojuegoService = new VideojuegoService();
    }
    
    /**
     * Obtener el carrito de un usuario
     */
    public List<CarritoItem> obtenerCarrito(int usuarioId) {
        return carritoRepository.obtenerCarrito(usuarioId);
    }
    
    /**
     * Agregar un videojuego al carrito
     */
    public void agregarAlCarrito(int usuarioId, int videojuegoId, int cantidad) {
        var videojuego = videojuegoService.obtenerPorId(videojuegoId);
        
        if (videojuego.isPresent() && videojuego.get().isDisponible()) {
            CarritoItem item = new CarritoItem(
                    videojuegoId,
                    cantidad,
                    videojuego.get().getPrecio(),
                    videojuego.get().getTitulo(),
                    videojuego.get().getImagen()
            );
            carritoRepository.agregarItem(usuarioId, item);
        }
    }
    
    /**
     * Actualizar la cantidad de un item en el carrito
     */
    public void actualizarCantidad(int usuarioId, int videojuegoId, int nuevaCantidad) {
        if (nuevaCantidad > 0) {
            carritoRepository.actualizarCantidad(usuarioId, videojuegoId, nuevaCantidad);
        } else {
            eliminarDelCarrito(usuarioId, videojuegoId);
        }
    }
    
    /**
     * Eliminar un item del carrito
     */
    public void eliminarDelCarrito(int usuarioId, int videojuegoId) {
        carritoRepository.eliminarItem(usuarioId, videojuegoId);
    }
    
    /**
     * Vaciar todo el carrito
     */
    public void vaciarCarrito(int usuarioId) {
        carritoRepository.vaciarCarrito(usuarioId);
    }
    
    /**
     * Obtener la cantidad total de items
     */
    public int obtenerCantidadTotal(int usuarioId) {
        return carritoRepository.obtenerCantidadTotal(usuarioId);
    }
    
    /**
     * Obtener el total a pagar
     */
    public double obtenerTotalCarrito(int usuarioId) {
        return carritoRepository.obtenerTotalCarrito(usuarioId);
    }
    
    /**
     * Verificar si el carrito está vacío
     */
    public boolean estaVacio(int usuarioId) {
        return obtenerCantidadTotal(usuarioId) == 0;
    }
}

