package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.CarritoItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio para gestionar carritos de compras
 * Simula carritos en memoria usando HashMap (clave: usuarioId, valor: lista de items)
 */
public class CarritoRepository {
    
    // Simulamos carritos en memoria: Map<usuarioId, List<CarritoItem>>
    private static Map<Integer, List<CarritoItem>> carritos = new HashMap<>();
    
    /**
     * Obtener el carrito de un usuario
     */
    public List<CarritoItem> obtenerCarrito(int usuarioId) {
        return carritos.getOrDefault(usuarioId, new ArrayList<>());
    }
    
    /**
     * Agregar un item al carrito
     */
    public void agregarItem(int usuarioId, CarritoItem item) {
        List<CarritoItem> carrito = carritos.getOrDefault(usuarioId, new ArrayList<>());
        
        // Verificar si el videojuego ya existe en el carrito
        Optional<CarritoItem> existente = carrito.stream()
                .filter(ci -> ci.getVideojuegoId() == item.getVideojuegoId())
                .findFirst();
        
        if (existente.isPresent()) {
            // Si existe, aumentar la cantidad
            existente.get().setCantidad(existente.get().getCantidad() + item.getCantidad());
        } else {
            // Si no existe, agregar el item
            carrito.add(item);
        }
        
        carritos.put(usuarioId, carrito);
    }
    
    /**
     * Actualizar la cantidad de un item
     */
    public void actualizarCantidad(int usuarioId, int videojuegoId, int nuevaCantidad) {
        List<CarritoItem> carrito = carritos.getOrDefault(usuarioId, new ArrayList<>());
        
        carrito.stream()
                .filter(ci -> ci.getVideojuegoId() == videojuegoId)
                .findFirst()
                .ifPresent(item -> item.setCantidad(nuevaCantidad));
    }
    
    /**
     * Eliminar un item del carrito
     */
    public void eliminarItem(int usuarioId, int videojuegoId) {
        List<CarritoItem> carrito = carritos.getOrDefault(usuarioId, new ArrayList<>());
        carrito.removeIf(ci -> ci.getVideojuegoId() == videojuegoId);
    }
    
    /**
     * Vaciar el carrito
     */
    public void vaciarCarrito(int usuarioId) {
        carritos.remove(usuarioId);
    }
    
    /**
     * Obtener la cantidad total de items en el carrito
     */
    public int obtenerCantidadTotal(int usuarioId) {
        return carritos.getOrDefault(usuarioId, new ArrayList<>())
                .stream()
                .mapToInt(CarritoItem::getCantidad)
                .sum();
    }
    
    /**
     * Obtener el total del carrito (suma de subtotales)
     */
    public double obtenerTotalCarrito(int usuarioId) {
        return carritos.getOrDefault(usuarioId, new ArrayList<>())
                .stream()
                .mapToDouble(CarritoItem::getSubtotal)
                .sum();
    }
    
    /**
     * Verificar si existe un item en el carrito
     */
    public boolean existeItem(int usuarioId, int videojuegoId) {
        return carritos.getOrDefault(usuarioId, new ArrayList<>())
                .stream()
                .anyMatch(ci -> ci.getVideojuegoId() == videojuegoId);
    }
}

