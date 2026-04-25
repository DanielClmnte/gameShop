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

@Controller
public class CarritoController {

    private final CarritoService carritoService;
    private final VideojuegoService videojuegoService;

    public CarritoController() {
        this.carritoService = new CarritoService();
        this.videojuegoService = new VideojuegoService();
    }

    // ── VER CARRITO ──

    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        List<CarritoItem> items;
        double total;
        int cantidad;

        if (usuarioId != null) {
            items    = carritoService.obtenerCarrito(usuarioId);
            total    = carritoService.obtenerTotalCarrito(usuarioId);
            cantidad = carritoService.obtenerCantidadTotal(usuarioId);
        } else {
            items    = obtenerCarritoSesion(session);
            total    = items.stream().mapToDouble(CarritoItem::getSubtotal).sum();
            cantidad = items.stream().mapToInt(CarritoItem::getCantidad).sum();
        }

        // Flash de stock: leer y limpiar de sesión
        String errorStock = (String) session.getAttribute("errorStock");
        if (errorStock != null) {
            model.addAttribute("errorStock", errorStock);
            session.removeAttribute("errorStock");
        }

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("cantidad", cantidad);
        model.addAttribute("titulo", "Carrito de Compras - GameShop");
        return "carrito/ver-carrito";
    }

    // ── AÑADIR AL CARRITO ──

    @PostMapping("/carrito/agregar/{videojuegoId}")
    public String agregarAlCarrito(
            @PathVariable int videojuegoId,
            @RequestParam(defaultValue = "1") int cantidad,
            HttpSession session) {

        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            boolean agregado = carritoService.agregarAlCarrito(usuarioId, videojuegoId, cantidad);
            if (!agregado) {
                session.setAttribute("errorStock", "No hay suficiente stock disponible para este producto.");
            }
        } else {
            Optional<Videojuego> vj = videojuegoService.obtenerPorId(videojuegoId);
            if (vj.isPresent() && vj.get().isDisponible()) {
                int stock = vj.get().getStock();
                if (stock < cantidad) {
                    session.setAttribute("errorStock", "Solo hay " + stock + " unidades disponibles.");
                } else {
                    List<CarritoItem> carrito = obtenerCarritoSesion(session);
                    agregarItemSesion(carrito, vj.get(), cantidad);
                    session.setAttribute("carritoSesion", carrito);
                }
            }
        }
        return "redirect:/carrito";
    }

    // ── ACTUALIZAR CANTIDAD ──

    @PostMapping("/carrito/actualizar/{videojuegoId}")
    public String actualizarCantidad(
            @PathVariable int videojuegoId,
            @RequestParam int nuevaCantidad,
            HttpSession session) {

        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            boolean ok = carritoService.actualizarCantidad(usuarioId, videojuegoId, nuevaCantidad);
            if (!ok) {
                session.setAttribute("errorStock", "No hay suficiente stock para la cantidad solicitada.");
            }
        } else {
            Optional<Videojuego> vj = videojuegoService.obtenerPorId(videojuegoId);
            if (vj.isPresent() && nuevaCantidad > 0) {
                int stock = vj.get().getStock();
                if (stock < nuevaCantidad) {
                    session.setAttribute("errorStock", "Solo hay " + stock + " unidades disponibles.");
                } else {
                    List<CarritoItem> carrito = obtenerCarritoSesion(session);
                    for (CarritoItem item : carrito) {
                        if (item.getVideojuegoId() == videojuegoId) {
                            item.setCantidad(nuevaCantidad);
                            break;
                        }
                    }
                    session.setAttribute("carritoSesion", carrito);
                }
            } else if (nuevaCantidad <= 0) {
                List<CarritoItem> carrito = obtenerCarritoSesion(session);
                carrito.removeIf(i -> i.getVideojuegoId() == videojuegoId);
                session.setAttribute("carritoSesion", carrito);
            }
        }
        return "redirect:/carrito";
    }

    // ── ELIMINAR ITEM ──

    @GetMapping("/carrito/eliminar/{videojuegoId}")
    public String eliminarDelCarrito(@PathVariable int videojuegoId, HttpSession session) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId != null) {
            carritoService.eliminarDelCarrito(usuarioId, videojuegoId);
        } else {
            List<CarritoItem> carrito = obtenerCarritoSesion(session);
            carrito.removeIf(i -> i.getVideojuegoId() == videojuegoId);
            session.setAttribute("carritoSesion", carrito);
        }
        return "redirect:/carrito";
    }

    // ── VACIAR CARRITO ──

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

    // ── UTILIDADES ──

    @SuppressWarnings("unchecked")
    private List<CarritoItem> obtenerCarritoSesion(HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carritoSesion");
        return carrito != null ? carrito : new ArrayList<>();
    }

    private void agregarItemSesion(List<CarritoItem> carrito, Videojuego vj, int cantidad) {
        for (CarritoItem item : carrito) {
            if (item.getVideojuegoId() == vj.getId()) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        carrito.add(new CarritoItem(vj.getId(), cantidad, vj.getPrecio(), vj.getTitulo(), vj.getImagen()));
    }
}
