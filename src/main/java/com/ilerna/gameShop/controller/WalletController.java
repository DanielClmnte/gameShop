package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.DireccionUsuario;
import com.ilerna.gameShop.model.MetodoPago;
import com.ilerna.gameShop.service.DireccionService;
import com.ilerna.gameShop.service.MetodoPagoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

/**
 * Gestión de direcciones y formas de pago para el usuario y el admin.
 */
@Controller
public class WalletController {

    private final DireccionService direccionService;
    private final MetodoPagoService metodoPagoService;

    public WalletController() {
        this.direccionService = new DireccionService();
        this.metodoPagoService = new MetodoPagoService();
    }

    // ══════════════════════════════════════
    //  DIRECCIONES — USUARIO
    // ══════════════════════════════════════

    @GetMapping("/perfil/direcciones")
    public String listaDirecciones(HttpSession session, Model model) {
        Integer uid = (Integer) session.getAttribute("usuarioId");
        if (uid == null) return "redirect:/login?redirect=/perfil/direcciones";
        model.addAttribute("direcciones", direccionService.obtenerPorUsuario(uid));
        model.addAttribute("titulo", "Mis Direcciones - GameShop");
        return "perfil/direcciones";
    }

    @PostMapping("/perfil/direcciones/guardar")
    public String guardarDireccion(@RequestParam String alias,
                                   @RequestParam String nombre,
                                   @RequestParam String direccion,
                                   @RequestParam String ciudad,
                                   @RequestParam String codigoPostal,
                                   @RequestParam(required = false) String telefono,
                                   @RequestParam(defaultValue = "false") boolean esPrincipal,
                                   HttpSession session, Model model) {
        Integer uid = (Integer) session.getAttribute("usuarioId");
        if (uid == null) return "redirect:/login";
        String err = direccionService.guardar(uid, alias, nombre, direccion, ciudad, codigoPostal, telefono, esPrincipal);
        if (err != null) {
            model.addAttribute("error", err);
            model.addAttribute("direcciones", direccionService.obtenerPorUsuario(uid));
            model.addAttribute("titulo", "Mis Direcciones - GameShop");
            return "perfil/direcciones";
        }
        return "redirect:/perfil/direcciones?ok=1";
    }

    @PostMapping("/perfil/direcciones/actualizar/{id}")
    public String actualizarDireccion(@PathVariable int id,
                                      @RequestParam String alias,
                                      @RequestParam String nombre,
                                      @RequestParam String direccion,
                                      @RequestParam String ciudad,
                                      @RequestParam String codigoPostal,
                                      @RequestParam(required = false) String telefono,
                                      @RequestParam(defaultValue = "false") boolean esPrincipal,
                                      HttpSession session, Model model) {
        Integer uid = (Integer) session.getAttribute("usuarioId");
        if (uid == null) return "redirect:/login";
        direccionService.actualizar(id, uid, alias, nombre, direccion, ciudad, codigoPostal, telefono, esPrincipal);
        return "redirect:/perfil/direcciones?ok=1";
    }

    @GetMapping("/perfil/direcciones/eliminar/{id}")
    public String eliminarDireccion(@PathVariable int id, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("usuarioId");
        if (uid != null) direccionService.eliminar(id, uid);
        return "redirect:/perfil/direcciones";
    }

    // ══════════════════════════════════════
    //  MÉTODOS DE PAGO — USUARIO
    // ══════════════════════════════════════

    @GetMapping("/perfil/pagos")
    public String listaPagos(HttpSession session, Model model) {
        Integer uid = (Integer) session.getAttribute("usuarioId");
        if (uid == null) return "redirect:/login?redirect=/perfil/pagos";
        model.addAttribute("metodos", metodoPagoService.obtenerPorUsuario(uid));
        model.addAttribute("titulo", "Mis Formas de Pago - GameShop");
        return "perfil/pagos";
    }

    @PostMapping("/perfil/pagos/guardar")
    public String guardarPago(@RequestParam String alias,
                              @RequestParam String tipo,
                              @RequestParam String numero,
                              @RequestParam String titular,
                              @RequestParam String mes,
                              @RequestParam String anio,
                              @RequestParam(defaultValue = "false") boolean esPrincipal,
                              HttpSession session, Model model) {
        Integer uid = (Integer) session.getAttribute("usuarioId");
        if (uid == null) return "redirect:/login";
        String err = metodoPagoService.guardar(uid, alias, tipo, numero, titular, mes, anio, esPrincipal);
        if (err != null) {
            model.addAttribute("error", err);
            model.addAttribute("metodos", metodoPagoService.obtenerPorUsuario(uid));
            model.addAttribute("titulo", "Mis Formas de Pago - GameShop");
            return "perfil/pagos";
        }
        return "redirect:/perfil/pagos?ok=1";
    }

    @PostMapping("/perfil/pagos/actualizar/{id}")
    public String actualizarPago(@PathVariable int id,
                                 @RequestParam String alias,
                                 @RequestParam String tipo,
                                 @RequestParam String titular,
                                 @RequestParam String mes,
                                 @RequestParam String anio,
                                 @RequestParam(defaultValue = "false") boolean esPrincipal,
                                 HttpSession session) {
        Integer uid = (Integer) session.getAttribute("usuarioId");
        if (uid != null) metodoPagoService.actualizar(id, uid, alias, tipo, titular, mes, anio, esPrincipal);
        return "redirect:/perfil/pagos?ok=1";
    }

    @GetMapping("/perfil/pagos/eliminar/{id}")
    public String eliminarPago(@PathVariable int id, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("usuarioId");
        if (uid != null) metodoPagoService.eliminar(id, uid);
        return "redirect:/perfil/pagos";
    }
}

