package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.model.Pedido;
import com.ilerna.gameShop.service.CarritoService;
import com.ilerna.gameShop.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * Controlador del proceso de checkout.
 * ⚠️ REQUIERE LOGIN: si no hay sesión activa, redirige a /login.
 * - GET  /checkout          : Formulario de pago (muestra carrito)
 * - POST /checkout/procesar : Procesa pago, guarda pedido y vacía carrito
 */
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private CarritoService carritoService;
    private PedidoService pedidoService;

    public CheckoutController() {
        this.carritoService = new CarritoService();
        this.pedidoService = new PedidoService();
    }

    // ──────────────── MOSTRAR CHECKOUT ────────────────

    @GetMapping
    public String mostrarCheckout(HttpSession session, Model model) {
        // Requiere login
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login?redirect=/checkout";
        }

        List<CarritoItem> items = carritoService.obtenerCarrito(usuarioId);
        if (items.isEmpty()) {
            return "redirect:/carrito";
        }

        double total = carritoService.obtenerTotalCarrito(usuarioId);
        double impuestos = total * 0.21;

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("impuestos", impuestos);
        model.addAttribute("totalConImpuestos", total + impuestos);
        model.addAttribute("titulo", "Checkout - GameShop");
        return "checkout/checkout";
    }

    // ──────────────── PROCESAR PAGO ────────────────

    @PostMapping("/procesar")
    public String procesarCompra(
            @RequestParam String nombreCompleto,
            @RequestParam String email,
            @RequestParam String direccion,
            @RequestParam String ciudad,
            @RequestParam String codigoPostal,
            @RequestParam String numeroTarjeta,
            @RequestParam String mesExpiracion,
            @RequestParam String anioExpiracion,
            @RequestParam String cvv,
            HttpSession session,
            Model model) {

        // Requiere login
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login?redirect=/checkout";
        }

        List<CarritoItem> items = carritoService.obtenerCarrito(usuarioId);
        double subtotal = carritoService.obtenerTotalCarrito(usuarioId);

        if (items.isEmpty()) {
            return "redirect:/carrito";
        }

        // Validación simulada de tarjeta
        if (!validarDatosPago(numeroTarjeta, cvv)) {
            model.addAttribute("error", "Error al procesar el pago. Verifica los datos de tu tarjeta.");
            model.addAttribute("titulo", "Error en el Pago - GameShop");
            return "checkout/error-pago";
        }

        // ✅ Guardar pedido
        Pedido pedido = pedidoService.crearPedido(
                usuarioId, nombreCompleto, email,
                direccion, ciudad, codigoPostal,
                items, subtotal
        );

        // Vaciar carrito
        carritoService.vaciarCarrito(usuarioId);

        // Pasar datos a la vista de confirmación
        model.addAttribute("pedido", pedido);
        model.addAttribute("titulo", "Pedido Confirmado - GameShop");
        return "checkout/confirmacion";
    }

    // ──────────────── UTILIDADES ────────────────

    private boolean validarDatosPago(String numeroTarjeta, String cvv) {
        return numeroTarjeta != null && numeroTarjeta.matches("[0-9]{16}")
                && cvv != null && cvv.matches("[0-9]{3}");
    }
}
