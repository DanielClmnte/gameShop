package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.model.Usuario;
import com.ilerna.gameShop.service.CarritoService;
import com.ilerna.gameShop.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

/**
 * Controlador para gestionar login y registro de usuarios.
 * Al hacer login/registro, migra el carrito de sesión (anónimo) a BD.
 */
@Controller
public class LoginController {

    private UsuarioService usuarioService;
    private CarritoService carritoService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
        this.carritoService = new CarritoService();
    }

    // ──────────────── LOGIN ────────────────

    @GetMapping("/login")
    public String mostrarLogin(
            @RequestParam(required = false) String redirect,
            Model model) {
        model.addAttribute("titulo", "Login - GameShop");
        model.addAttribute("redirect", redirect);
        return "auth/login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam(required = false) String redirect,
            HttpSession session,
            Model model) {

        Optional<Usuario> usuario = usuarioService.autenticar(email.trim(), contrasena);

        if (usuario.isPresent()) {
            // Guardar datos en sesión
            session.setAttribute("usuarioId", usuario.get().getId());
            session.setAttribute("usuarioNombre", usuario.get().getNombre());
            session.setAttribute("usuarioRol", usuario.get().getRol());

            // ★ Migrar carrito de sesión (anónimo) a BD
            migrarCarritoSesionABD(session, usuario.get().getId());

            // Si venía de una página protegida, volver allí
            if (redirect != null && !redirect.isBlank()) {
                return "redirect:" + redirect;
            }
            return "ADMIN".equals(usuario.get().getRol())
                    ? "redirect:/admin"
                    : "redirect:/";
        }

        model.addAttribute("error", "Email o contraseña incorrectos");
        model.addAttribute("redirect", redirect);
        model.addAttribute("titulo", "Login - GameShop");
        return "auth/login";
    }

    // ──────────────── LOGOUT ────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ──────────────── REGISTRO ────────────────

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("titulo", "Registro - GameShop");
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam String contrasenaRepetida,
            @RequestParam(required = false) String telefono,
            HttpSession session,
            Model model) {

        if (!contrasena.equals(contrasenaRepetida)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            model.addAttribute("nombre", nombre);
            model.addAttribute("email", email);
            model.addAttribute("titulo", "Registro - GameShop");
            return "auth/registro";
        }

        boolean ok = usuarioService.registrar(nombre, email, contrasena, telefono);

        if (ok) {
            // Login automático tras registro
            Optional<Usuario> nuevo = usuarioService.obtenerPorEmail(email);
            nuevo.ifPresent(u -> {
                session.setAttribute("usuarioId", u.getId());
                session.setAttribute("usuarioNombre", u.getNombre());
                session.setAttribute("usuarioRol", u.getRol());
                // ★ Migrar carrito de sesión a BD
                migrarCarritoSesionABD(session, u.getId());
            });
            return "redirect:/";
        }

        String error = usuarioService.obtenerPorEmail(email).isPresent()
                ? "El email ya está registrado"
                : "Error en los datos ingresados";
        model.addAttribute("error", error);
        model.addAttribute("nombre", nombre);
        model.addAttribute("email", email);
        model.addAttribute("titulo", "Registro - GameShop");
        return "auth/registro";
    }

    // ──────────────── PERFIL ────────────────

    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, Model model) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) return "redirect:/login";
        Optional<Usuario> usuario = usuarioService.obtenerPorId(usuarioId);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("titulo", "Mi Perfil - GameShop");
            return "auth/perfil";
        }
        return "redirect:/login";
    }

    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String codigoPostal,
            HttpSession session,
            Model model) {

        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) return "redirect:/login";

        // Validaciones backend
        java.util.List<String> errores = new java.util.ArrayList<>();
        if (nombre == null || nombre.trim().length() < 2 || nombre.trim().length() > 100)
            errores.add("El nombre debe tener entre 2 y 100 caracteres.");
        if (email == null || !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
            errores.add("El email no es válido.");
        if (telefono != null && !telefono.isBlank() && !telefono.matches("[0-9+\\-\\s]{6,20}"))
            errores.add("El teléfono no es válido.");
        if (codigoPostal != null && !codigoPostal.isBlank() && !codigoPostal.matches("[0-9]{5}"))
            errores.add("El código postal debe tener 5 dígitos numéricos.");

        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(usuarioId);
        if (!errores.isEmpty()) {
            usuarioOpt.ifPresent(u -> model.addAttribute("usuario", u));
            model.addAttribute("errores", errores);
            model.addAttribute("titulo", "Mi Perfil - GameShop");
            return "auth/perfil";
        }

        boolean ok = usuarioService.actualizarPerfil(usuarioId, nombre, email,
                telefono, direccion, ciudad, codigoPostal);
        if (!ok) {
            usuarioOpt.ifPresent(u -> model.addAttribute("usuario", u));
            model.addAttribute("errorEmail", "Ese email ya está registrado por otro usuario.");
            model.addAttribute("titulo", "Mi Perfil - GameShop");
            return "auth/perfil";
        }
        // Actualizar nombre en sesión
        session.setAttribute("usuarioNombre", nombre.trim());
        model.addAttribute("exito", "Perfil actualizado correctamente.");
        usuarioService.obtenerPorId(usuarioId).ifPresent(u -> model.addAttribute("usuario", u));
        model.addAttribute("titulo", "Mi Perfil - GameShop");
        return "auth/perfil";
    }

    @PostMapping("/perfil/cambiar-contrasena")
    public String cambiarContrasena(
            @RequestParam String contraseniaActual,
            @RequestParam String contrasenaNueva,
            @RequestParam String contrasenaRepetida,
            HttpSession session,
            Model model) {

        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) return "redirect:/login";

        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(usuarioId);
        usuarioOpt.ifPresent(u -> model.addAttribute("usuario", u));
        model.addAttribute("titulo", "Mi Perfil - GameShop");

        if (contrasenaNueva == null || contrasenaNueva.length() < 4) {
            model.addAttribute("errorPass", "La nueva contraseña debe tener al menos 4 caracteres.");
            return "auth/perfil";
        }
        if (!contrasenaNueva.equals(contrasenaRepetida)) {
            model.addAttribute("errorPass", "Las contraseñas no coinciden.");
            return "auth/perfil";
        }
        boolean ok = usuarioService.cambiarContrasena(usuarioId, contraseniaActual, contrasenaNueva);
        if (!ok) {
            model.addAttribute("errorPass", "La contraseña actual no es correcta.");
            return "auth/perfil";
        }
        model.addAttribute("exitoPass", "Contraseña cambiada correctamente.");
        return "auth/perfil";
    }

    // ──────────────── UTILIDAD: migrar carrito ────────────────

    /**
     * Migra los items del carrito de sesión (anónimo) a la BD del usuario logueado.
     * Luego limpia el carrito de sesión.
     */
    @SuppressWarnings("unchecked")
    private void migrarCarritoSesionABD(HttpSession session, int usuarioId) {
        List<CarritoItem> carritoSesion = (List<CarritoItem>) session.getAttribute("carritoSesion");
        if (carritoSesion != null && !carritoSesion.isEmpty()) {
            for (CarritoItem item : carritoSesion) {
                carritoService.agregarAlCarrito(usuarioId, item.getVideojuegoId(), item.getCantidad());
            }
            session.removeAttribute("carritoSesion");
        }
    }
}
