package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.service.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para el proceso de checkout y pago
 * Rutas principales:
 * - GET /checkout : Formulario de checkout
 * - POST /checkout/procesar : Procesar la compra
 */
@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    
    private CarritoService carritoService;
    
    // Constructor
    public CheckoutController() {
        this.carritoService = new CarritoService();
    }
    
    /**
     * Mostrar página de checkout
     */
    @GetMapping
    public String mostrarCheckout(
            @RequestParam(defaultValue = "1") int usuarioId,
            Model model) {
        
        List<CarritoItem> items = carritoService.obtenerCarrito(usuarioId);
        double total = carritoService.obtenerTotalCarrito(usuarioId);
        double impuestos = total * 0.21;
        double totalConImpuestos = total + impuestos;
        
        if (items.isEmpty()) {
            return "redirect:/carrito?usuarioId=" + usuarioId;
        }
        
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("impuestos", impuestos);
        model.addAttribute("totalConImpuestos", totalConImpuestos);
        model.addAttribute("usuarioId", usuarioId);
        model.addAttribute("titulo", "Checkout - GameShop");
        
        return "checkout/checkout";
    }
    
    /**
     * Procesar la compra (simulado)
     */
    @PostMapping("/procesar")
    public String procesarCompra(
            @RequestParam int usuarioId,
            @RequestParam String nombreCompleto,
            @RequestParam String email,
            @RequestParam String direccion,
            @RequestParam String ciudad,
            @RequestParam String codigoPostal,
            @RequestParam String numeroTarjeta,
            @RequestParam String mesExpiracion,
            @RequestParam String anioExpiracion,
            @RequestParam String cvv,
            Model model) {
        
        List<CarritoItem> items = carritoService.obtenerCarrito(usuarioId);
        double total = carritoService.obtenerTotalCarrito(usuarioId);
        
        if (items.isEmpty()) {
            return "redirect:/carrito?usuarioId=" + usuarioId;
        }
        
        // Simulación de procesamiento de pago
        // En producción, aquí se usaría una pasarela de pago real
        boolean pagoProcesado = validarDatosPago(numeroTarjeta, cvv);
        
        if (pagoProcesado) {
            // Pago exitoso - Generar pedido
            int numeroPedido = generarNumeroPedido();
            LocalDate fechaEntrega = LocalDate.now().plusDays(3);
            
            // Vaciar carrito
            carritoService.vaciarCarrito(usuarioId);
            
            // Mostrar confirmación
            model.addAttribute("numeroPedido", numeroPedido);
            model.addAttribute("total", total);
            model.addAttribute("totalConImpuestos", total * 1.21);
            model.addAttribute("fechaEntrega", fechaEntrega);
            model.addAttribute("nombreCompleto", nombreCompleto);
            model.addAttribute("email", email);
            model.addAttribute("items", items);
            model.addAttribute("titulo", "Pedido Confirmado - GameShop");
            
            return "checkout/confirmacion";
        } else {
            // Pago fallido
            model.addAttribute("error", "Error al procesar el pago. Verifica los datos de tu tarjeta.");
            model.addAttribute("usuarioId", usuarioId);
            model.addAttribute("titulo", "Error en el Pago - GameShop");
            
            return "checkout/error-pago";
        }
    }
    
    /**
     * Validar datos de pago (simulado)
     */
    private boolean validarDatosPago(String numeroTarjeta, String cvv) {
        // Validaciones básicas simuladas
        return numeroTarjeta.length() == 16 && cvv.length() == 3 && isNumeric(numeroTarjeta) && isNumeric(cvv);
    }
    
    /**
     * Verificar si una cadena es numérica
     */
    private boolean isNumeric(String str) {
        return str != null && str.matches("[0-9]+");
    }
    
    /**
     * Generar número de pedido (simulado)
     */
    private int generarNumeroPedido() {
        return (int) (System.currentTimeMillis() % 1000000) + 100000;
    }
}

