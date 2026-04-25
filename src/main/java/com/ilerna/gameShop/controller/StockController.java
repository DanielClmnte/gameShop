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

/**
 * Controlador para gestionar el stock de productos.
 * Rutas: GET /admin/stock, POST /admin/stock/actualizar, POST /admin/stock/aumentar, POST /admin/stock/disminuir
 */
@Controller
@RequestMapping("/admin/stock")
public class StockController {

    private final VideojuegoService videojuegoService;

    public StockController() {
        this.videojuegoService = new VideojuegoService();
    }

    /** Mostrar lista de productos filtrada por stock */
    @GetMapping
    public String mostrarStock(
            @RequestParam(defaultValue = "todos") String filtro,
            Model model) {

        List<Videojuego> videojuegos;
        String titulo;

        switch (filtro) {
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

    /** Establecer un stock concreto a un producto */
    @PostMapping("/actualizar")
    public String actualizarStock(
            @RequestParam int videojuegoId,
            @RequestParam int nuevoStock,
            @RequestParam(defaultValue = "todos") String filtro) {

        videojuegoService.obtenerPorId(videojuegoId).ifPresent(vj -> {
            vj.setStock(nuevoStock);
            videojuegoService.actualizarVideojuego(vj);
        });

        return "redirect:/admin/stock?filtro=" + filtro;
    }

    /** Aumentar stock de un producto */
    @PostMapping("/aumentar")
    public String aumentarStock(
            @RequestParam int videojuegoId,
            @RequestParam(defaultValue = "1") int cantidad,
            @RequestParam(defaultValue = "todos") String filtro) {

        videojuegoService.obtenerPorId(videojuegoId).ifPresent(vj -> {
            vj.setStock(vj.getStock() + cantidad);
            videojuegoService.actualizarVideojuego(vj);
        });

        return "redirect:/admin/stock?filtro=" + filtro;
    }

    /** Disminuir stock de un producto (mínimo 0) */
    @PostMapping("/disminuir")
    public String disminuirStock(
            @RequestParam int videojuegoId,
            @RequestParam(defaultValue = "1") int cantidad,
            @RequestParam(defaultValue = "todos") String filtro) {

        videojuegoService.obtenerPorId(videojuegoId).ifPresent(vj -> {
            vj.setStock(Math.max(0, vj.getStock() - cantidad));
            videojuegoService.actualizarVideojuego(vj);
        });

        return "redirect:/admin/stock?filtro=" + filtro;
    }
}
