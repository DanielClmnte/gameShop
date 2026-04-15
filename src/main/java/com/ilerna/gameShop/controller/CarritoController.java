package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.model.Videojuego;
import com.ilerna.gameShop.service.CarritoService;
import com.ilerna.gameShop.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador del carrito de compras.
 * - Usuarios logueados: carrito en BD (via CarritoRepository)
 * - Usuarios anónimos: carrito en sesión HttpSession ("carritoSesion")
 * Al hacer login, el carrito de sesión se migra a BD.
 */
@Controller
public class CarritoController {

    private CarritoService carritoService;
    private VideojuegoService videojuegoService;

    public CarritoController() {
        this.carritoService = new CarritoService();
        this.videojuegoService = new VideojuegoService();
    }

    // ──────────────── VER CARRITO ────────────────

    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        List<CarritoItem> items;
        double total;
        int cantidad;

        if (usuarioId != null) {
            // Usuario logueado → BD
            items = carritoService.obtenerCarrito(usuarioId);
            total = carritoService.obtenerTotalCarrito(usuarioId);
            cantidad = carritoService.obtenerCantidadTotal(usuarioId);
        } else {
            // Anónimo → sesión
            items = obtenerCarritoSesion(session);
            total = items.stream().mapToDouble(CarritoItem::getSubtotal).sum();
            cantidad = items.stream().mapToInt(CarritoItem::getCantidad).sum();
        }

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("cantidad", cantidad);
        model.addAttribute("titulo", "Carrito de Compras - GameShop");
        return "carrito/ver-carrito";
    }

    // ──────────────── AÑADIR AL CARRITO ────────────────

    @PostMapping("/carrito/agregar/{videojuegoId}")
    public String agregarAlCarrito(
            @PathVariable int videojuegoId,
            @RequestParam(defaultValue = "1") int cantidad,
            HttpSession session) {

        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            // Usuario logueado → BD
            carritoService.agregarAlCarrito(usuarioId, videojuegoId, cantidad);
        } else {
            // Anónimo → sesión
            Optional<Videojuego> vj = videojuegoService.obtenerPorId(videojuegoId);
            if (vj.isPresent() && vj.get().isDisponible()) {
                List<CarritoItem> carrito = obtenerCarritoSesion(session);
                agregarItemSesion(carrito, vj.get(), cantidad);
                session.setAttribute("carritoSesion", carrito);
            }
        }
        return "redirect:/carrito";
    }

    // ──────────────── ACTUALIZAR CANTIDAD ────────────────

    @PostMapping("/carrito/actualizar/{videojuegoId}")
    public String actualizarCantidad(
            @PathVariable int videojuegoId,
            @RequestParam int nuevaCantidad,
            HttpSession session) {

        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            carritoService.actualizarCantidad(usuarioId, videojuegoId, nuevaCantidad);
        } else {
            List<CarritoItem> carrito = obtenerCarritoSesion(session);
            if (nuevaCantidad > 0) {
                for (CarritoItem item : carrito) {
                    if (item.getVideojuegoId() == videojuegoId) {
                        item.setCantidad(nuevaCantidad);
                        break;
                    }
                }
            } else {
                carrito.removeIf(item -> item.getVideojuegoId() == videojuegoId);
            }
            session.setAttribute("carritoSesion", carrito);
        }
        return "redirect:/carrito";
    }

    // ──────────────── ELIMINAR ITEM ────────────────

    @GetMapping("/carrito/eliminar/{videojuegoId}")
    public String eliminarDelCarrito(@PathVariable int videojuegoId, HttpSession session) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            carritoService.eliminarDelCarrito(usuarioId, videojuegoId);
        } else {
            List<CarritoItem> carrito = obtenerCarritoSesion(session);
            carrito.removeIf(item -> item.getVideojuegoId() == videojuegoId);
            session.setAttribute("carritoSesion", carrito);
        }
        return "redirect:/carrito";
    }

    // ──────────────── VACIAR CARRITO ────────────────

    @GetMapping("/carrito/vaciar")
    public String vaciarCarrito(HttpSession session) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            carritoService.vaciarCarrito(usuarioId);
        } else {
            session.removeAttribute("carritoSesion");
        }
        return "redirect:/carrito";
    }

    // ──────────────── UTILIDADES SESIÓN ────────────────

    @SuppressWarnings("unchecked")
    private List<CarritoItem> obtenerCarritoSesion(HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carritoSesion");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }

    private void agregarItemSesion(List<CarritoItem> carrito, Videojuego vj, int cantidad) {
        for (CarritoItem item : carrito) {
            if (item.getVideojuegoId() == vj.getId()) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        // No existe → crear nuevo
        CarritoItem nuevo = new CarritoItem(
                vj.getId(), cantidad, vj.getPrecio(),
                vj.getTitulo(), vj.getImagen()
        );
        carrito.add(nuevo);
    }
}
