package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.Videojuego;
import com.ilerna.gameShop.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * Controlador para gestionar el stock de productos
 * Rutas principales:
 * - GET /admin/stock : Listar productos por stock
 * - POST /admin/stock/actualizar : Actualizar stock
 */
@Controller
@RequestMapping("/admin/stock")
public class StockController {
    
    private VideojuegoService videojuegoService;
    
    // Constructor
    public StockController() {
        this.videojuegoService = new VideojuegoService();
    }
    
    /**
     * Mostrar lista de productos por stock
     */
    @GetMapping
    public String mostrarStock(
            @RequestParam(defaultValue = "todos") String filtro,
            Model model) {
        
        List<Videojuego> videojuegos;
        String titulo;
        
        switch(filtro) {
            case "agotados":
                videojuegos = videojuegoService.obtenerTodos();
                videojuegos.removeIf(Videojuego::isDisponible);
                titulo = "Productos Agotados";
                break;
            case "bajo-stock":
                videojuegos = videojuegoService.obtenerTodos();
                videojuegos.removeIf(v -> v.getStock() >= 10);
                titulo = "Stock Bajo (< 10 unidades)";
                break;
            default:
                videojuegos = videojuegoService.obtenerTodos();
                titulo = "Gestión de Stock";
        }
        
        model.addAttribute("videojuegos", videojuegos);
        model.addAttribute("filtro", filtro);
        model.addAttribute("titulo", titulo + " - GameShop");
        
        return "admin/stock";
    }
    
    /**
     * Actualizar el stock de un producto
     */
    @PostMapping("/actualizar")
    public String actualizarStock(
            @RequestParam int videojuegoId,
            @RequestParam int nuevoStock,
            @RequestParam(defaultValue = "todos") String filtro) {
        
        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(videojuegoId);
        
        if (videojuego.isPresent()) {
            Videojuego vj = videojuego.get();
            vj.setStock(nuevoStock);
            videojuegoService.actualizarVideojuego(vj);
        }
        
        return "redirect:/admin/stock?filtro=" + filtro;
    }
    
    /**
     * Aumentar stock
     */
    @PostMapping("/aumentar")
    public String aumentarStock(
            @RequestParam int videojuegoId,
            @RequestParam(defaultValue = "1") int cantidad,
            @RequestParam(defaultValue = "todos") String filtro) {
        
        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(videojuegoId);
        
        if (videojuego.isPresent()) {
            Videojuego vj = videojuego.get();
            vj.setStock(vj.getStock() + cantidad);
            videojuegoService.actualizarVideojuego(vj);
        }
        
        return "redirect:/admin/stock?filtro=" + filtro;
    }
    
    /**
     * Disminuir stock
     */
    @PostMapping("/disminuir")
    public String disminuirStock(
            @RequestParam int videojuegoId,
            @RequestParam(defaultValue = "1") int cantidad,
            @RequestParam(defaultValue = "todos") String filtro) {
        
        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(videojuegoId);
        
        if (videojuego.isPresent()) {
            Videojuego vj = videojuego.get();
            int nuevoStock = Math.max(0, vj.getStock() - cantidad);
            vj.setStock(nuevoStock);
            videojuegoService.actualizarVideojuego(vj);
        }
        
        return "redirect:/admin/stock?filtro=" + filtro;
    }
}

