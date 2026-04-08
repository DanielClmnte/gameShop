package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.service.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * Controlador del carrito de compras.
 * No requiere login para añadir/ver productos.
 * El usuarioId se obtiene de la sesión (login) o de un ID anónimo temporal.
 * Rutas:
 * - GET  /carrito                      : Ver carrito
 * - POST /carrito/agregar/{id}         : Añadir producto
 * - POST /carrito/actualizar/{id}      : Cambiar cantidad
 * - GET  /carrito/eliminar/{id}        : Quitar producto
 * - GET  /carrito/vaciar               : Vaciar todo
 */
@Controller
public class CarritoController {

    private CarritoService carritoService;

    public CarritoController() {
        this.carritoService = new CarritoService();
    }

    // ──────────────── VER CARRITO ────────────────

    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model) {
        int usuarioId = obtenerUsuarioId(session);

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

    // ──────────────── AÑADIR AL CARRITO ────────────────

    @PostMapping("/carrito/agregar/{videojuegoId}")
    public String agregarAlCarrito(
            @PathVariable int videojuegoId,
            @RequestParam(defaultValue = "1") int cantidad,
            HttpSession session) {

        int usuarioId = obtenerUsuarioId(session);
        carritoService.agregarAlCarrito(usuarioId, videojuegoId, cantidad);
        return "redirect:/carrito";
    }

    // ──────────────── ACTUALIZAR CANTIDAD ────────────────

    @PostMapping("/carrito/actualizar/{videojuegoId}")
    public String actualizarCantidad(
            @PathVariable int videojuegoId,
            @RequestParam int nuevaCantidad,
            HttpSession session) {

        int usuarioId = obtenerUsuarioId(session);
        carritoService.actualizarCantidad(usuarioId, videojuegoId, nuevaCantidad);
        return "redirect:/carrito";
    }

    // ──────────────── ELIMINAR ITEM ────────────────

    @GetMapping("/carrito/eliminar/{videojuegoId}")
    public String eliminarDelCarrito(@PathVariable int videojuegoId, HttpSession session) {
        int usuarioId = obtenerUsuarioId(session);
        carritoService.eliminarDelCarrito(usuarioId, videojuegoId);
        return "redirect:/carrito";
    }

    // ──────────────── VACIAR CARRITO ────────────────

    @GetMapping("/carrito/vaciar")
    public String vaciarCarrito(HttpSession session) {
        int usuarioId = obtenerUsuarioId(session);
        carritoService.vaciarCarrito(usuarioId);
        return "redirect:/carrito";
    }

    // ──────────────── UTILIDAD: ID de usuario ────────────────

    /**
     * Obtiene el ID del usuario:
     * - Si está logueado → usa su ID real de sesión
     * - Si es anónimo   → genera un ID negativo único por sesión
     */
    private int obtenerUsuarioId(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("usuarioId");
        if (uid != null) return uid;

        // Usuario anónimo: crear ID temporal basado en el ID de sesión
        Integer anonId = (Integer) session.getAttribute("anonCarritoId");
        if (anonId == null) {
            anonId = -(Math.abs(session.getId().hashCode() % 100_000) + 1);
            session.setAttribute("anonCarritoId", anonId);
        }
        return anonId;
    }
}
