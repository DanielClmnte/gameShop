package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.model.DireccionUsuario;
import com.ilerna.gameShop.model.MetodoPago;
import com.ilerna.gameShop.model.Pedido;
import com.ilerna.gameShop.model.Usuario;
import com.ilerna.gameShop.service.CarritoService;
import com.ilerna.gameShop.service.DireccionService;
import com.ilerna.gameShop.service.MetodoPagoService;
import com.ilerna.gameShop.service.PedidoService;
import com.ilerna.gameShop.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador del proceso de checkout.
 * ⚠️ REQUIERE LOGIN: si no hay sesión activa, redirige a /login.
 */
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private CarritoService carritoService;
    private PedidoService pedidoService;
    private UsuarioService usuarioService;
    private DireccionService direccionService;
    private MetodoPagoService metodoPagoService;

    public CheckoutController() {
        this.carritoService = new CarritoService();
        this.pedidoService = new PedidoService();
        this.usuarioService = new UsuarioService();
        this.direccionService = new DireccionService();
        this.metodoPagoService = new MetodoPagoService();
    }

    @GetMapping
    public String mostrarCheckout(HttpSession session, Model model) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) return "redirect:/login?redirect=/checkout";

        List<CarritoItem> items = carritoService.obtenerCarrito(usuarioId);
        if (items.isEmpty()) return "redirect:/carrito";

        double total = carritoService.obtenerTotalCarrito(usuarioId);
        double impuestos = total * 0.21;

        // Obtener usuario y sus direcciones/tarjetas guardadas
        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(usuarioId);
        usuarioOpt.ifPresent(u -> model.addAttribute("usuario", u));

        List<DireccionUsuario> direcciones = direccionService.obtenerPorUsuario(usuarioId);
        List<MetodoPago> metodosPago = metodoPagoService.obtenerPorUsuario(usuarioId);

        model.addAttribute("direcciones", direcciones);
        model.addAttribute("metodosPago", metodosPago);
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("impuestos", impuestos);
        model.addAttribute("totalConImpuestos", total + impuestos);
        model.addAttribute("titulo", "Checkout - GameShop");
        return "checkout/checkout";
    }

    @PostMapping("/procesar")
    public String procesarCompra(
            @RequestParam(required = false) String usarDireccionGuardada,
            @RequestParam(required = false) Integer direccionId,
            @RequestParam(required = false) String nombreCompleto,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String codigoPostal,
            @RequestParam(required = false) String usarTarjetaGuardada,
            @RequestParam(required = false) Integer metodoPagoId,
            @RequestParam(required = false) String numeroTarjeta,
            @RequestParam(required = false) String mesExpiracion,
            @RequestParam(required = false) String anioExpiracion,
            @RequestParam(required = false) String cvv,
            HttpSession session,
            Model model) {

        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) return "redirect:/login?redirect=/checkout";

        List<CarritoItem> items = carritoService.obtenerCarrito(usuarioId);
        double subtotal = carritoService.obtenerTotalCarrito(usuarioId);
        if (items.isEmpty()) return "redirect:/carrito";

        List<String> errores = new ArrayList<>();

        // Inicializar variables de dirección
        String nombreFinal = "";
        String emailFinal = "";
        String direccionFinal = "";
        String ciudadFinal = "";
        String cpFinal = "";

        // Datos de envío (de dirección guardada o formulario)
        if ("true".equals(usarDireccionGuardada) && direccionId != null) {
            Optional<DireccionUsuario> dirOpt = direccionService.obtenerPorId(direccionId);
            if (dirOpt.isEmpty() || dirOpt.get().getUsuarioId() != usuarioId) {
                errores.add("Dirección seleccionada no válida.");
            } else {
                DireccionUsuario dir = dirOpt.get();
                nombreFinal = dir.getNombre();
                direccionFinal = dir.getDireccion();
                ciudadFinal = dir.getCiudad();
                cpFinal = dir.getCodigoPostal();
                // Email del usuario
                Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(usuarioId);
                emailFinal = usuarioOpt.map(Usuario::getEmail).orElse("");
            }
        } else {
            // Validar datos del formulario
            nombreFinal = nombreCompleto != null ? nombreCompleto.trim() : "";
            emailFinal = email != null ? email.trim() : "";
            direccionFinal = direccion != null ? direccion.trim() : "";
            ciudadFinal = ciudad != null ? ciudad.trim() : "";
            cpFinal = codigoPostal != null ? codigoPostal.trim() : "";

            if (nombreFinal.length() < 2 || nombreFinal.length() > 100)
                errores.add("El nombre completo debe tener entre 2 y 100 caracteres.");
            if (!emailFinal.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
                errores.add("El email introducido no es válido.");
            if (direccionFinal.length() < 5 || direccionFinal.length() > 255)
                errores.add("La dirección debe tener entre 5 y 255 caracteres.");
            if (ciudadFinal.length() < 2 || ciudadFinal.length() > 100)
                errores.add("La ciudad debe tener entre 2 y 100 caracteres.");
            if (!cpFinal.matches("[0-9]{5}"))
                errores.add("El código postal debe tener exactamente 5 dígitos numéricos.");
        }

        // Validar método de pago
        if ("true".equals(usarTarjetaGuardada) && metodoPagoId != null) {
            Optional<MetodoPago> metOpt = metodoPagoService.obtenerPorId(metodoPagoId);
            if (metOpt.isEmpty() || metOpt.get().getUsuarioId() != usuarioId) {
                errores.add("Tarjeta seleccionada no válida.");
            }
            // No necesitamos validar datos de tarjeta si usamos una guardada
        } else {
            // Validar datos de tarjeta nueva
            String tarjetaLimpia = numeroTarjeta != null ? numeroTarjeta.replaceAll("\\s+", "") : "";
            if (!tarjetaLimpia.matches("[0-9]{16}"))
                errores.add("El número de tarjeta debe tener 16 dígitos numéricos.");
            if (cvv == null || !cvv.trim().matches("[0-9]{3}"))
                errores.add("El CVV debe tener exactamente 3 dígitos numéricos.");
            if (mesExpiracion == null || !mesExpiracion.matches("(0[1-9]|1[0-2])"))
                errores.add("El mes de expiración no es válido.");
            if (anioExpiracion == null || !anioExpiracion.matches("[0-9]{4}"))
                errores.add("El año de expiración debe ser un número de 4 dígitos.");
            else if (Integer.parseInt(anioExpiracion) < 2026)
                errores.add("La tarjeta está caducada.");
        }

        if (!errores.isEmpty()) {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(usuarioId);
            usuarioOpt.ifPresent(u -> model.addAttribute("usuario", u));
            model.addAttribute("direcciones", direccionService.obtenerPorUsuario(usuarioId));
            model.addAttribute("metodosPago", metodoPagoService.obtenerPorUsuario(usuarioId));
            model.addAttribute("items", items);
            model.addAttribute("total", subtotal);
            model.addAttribute("impuestos", subtotal * 0.21);
            model.addAttribute("totalConImpuestos", subtotal * 1.21);
            model.addAttribute("errores", errores);
            model.addAttribute("titulo", "Checkout - GameShop");
            return "checkout/checkout";
        }

        // Guardar pedido
        Pedido pedido = pedidoService.crearPedido(
                usuarioId, nombreFinal, emailFinal,
                direccionFinal, ciudadFinal, cpFinal,
                items, subtotal
        );

        if (pedido == null || pedido.getId() == 0) {
            model.addAttribute("errores", List.of("Error al procesar el pedido. Por favor, inténtalo de nuevo."));
            Optional<Usuario> usuarioOpt2 = usuarioService.obtenerPorId(usuarioId);
            usuarioOpt2.ifPresent(u -> model.addAttribute("usuario", u));
            model.addAttribute("direcciones", direccionService.obtenerPorUsuario(usuarioId));
            model.addAttribute("metodosPago", metodoPagoService.obtenerPorUsuario(usuarioId));
            model.addAttribute("items", items);
            model.addAttribute("total", subtotal);
            model.addAttribute("impuestos", subtotal * 0.21);
            model.addAttribute("totalConImpuestos", subtotal * 1.21);
            model.addAttribute("titulo", "Checkout - GameShop");
            return "checkout/checkout";
        }

        carritoService.vaciarCarrito(usuarioId);

        model.addAttribute("pedido", pedido);
        model.addAttribute("titulo", "Pedido Confirmado - GameShop");
        return "checkout/confirmacion";
    }
}
