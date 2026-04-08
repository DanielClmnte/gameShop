package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.service.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controlador para gestionar el carrito de compras
 * Rutas principales:
 * - GET /carrito : Mostrar carrito
 * - POST /carrito/agregar/{id} : Agregar al carrito
 * - POST /carrito/actualizar/{id} : Actualizar cantidad
 * - GET /carrito/eliminar/{id} : Eliminar del carrito
 */
@Controller
public class CarritoController {
    
    private CarritoService carritoService;
    
    // Constructor
    public CarritoController() {
        this.carritoService = new CarritoService();
    }
    
    /**
     * Mostrar el carrito del usuario
     */
    @GetMapping("/carrito")
    public String mostrarCarrito(
            @RequestParam(defaultValue = "1") int usuarioId,
            Model model) {
        
        List<CarritoItem> items = carritoService.obtenerCarrito(usuarioId);
        double total = carritoService.obtenerTotalCarrito(usuarioId);
        int cantidad = carritoService.obtenerCantidadTotal(usuarioId);
        
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("cantidad", cantidad);
        model.addAttribute("usuarioId", usuarioId);
        model.addAttribute("titulo", "Carrito de Compras - GameShop");
        
        return "carrito/ver-carrito";
    }
    
    /**
     * Agregar un videojuego al carrito (POST)
     */
    @PostMapping("/carrito/agregar/{videojuegoId}")
    public String agregarAlCarrito(
            @PathVariable int videojuegoId,
            @RequestParam(defaultValue = "1") int cantidad,
            @RequestParam(defaultValue = "1") int usuarioId) {
        
        carritoService.agregarAlCarrito(usuarioId, videojuegoId, cantidad);
        
        return "redirect:/carrito?usuarioId=" + usuarioId;
    }
    
    /**
     * Actualizar cantidad de un item (POST)
     */
    @PostMapping("/carrito/actualizar/{videojuegoId}")
    public String actualizarCantidad(
            @PathVariable int videojuegoId,
            @RequestParam int nuevaCantidad,
            @RequestParam(defaultValue = "1") int usuarioId) {
        
        carritoService.actualizarCantidad(usuarioId, videojuegoId, nuevaCantidad);
        
        return "redirect:/carrito?usuarioId=" + usuarioId;
    }
    
    /**
     * Eliminar un item del carrito
     */
    @GetMapping("/carrito/eliminar/{videojuegoId}")
    public String eliminarDelCarrito(
            @PathVariable int videojuegoId,
            @RequestParam(defaultValue = "1") int usuarioId) {
        
        carritoService.eliminarDelCarrito(usuarioId, videojuegoId);
        
        return "redirect:/carrito?usuarioId=" + usuarioId;
    }
    
    /**
     * Vaciar todo el carrito
     */
    @GetMapping("/carrito/vaciar")
    public String vaciarCarrito(
            @RequestParam(defaultValue = "1") int usuarioId) {
        
        carritoService.vaciarCarrito(usuarioId);
        
        return "redirect:/carrito?usuarioId=" + usuarioId;
    }
}

