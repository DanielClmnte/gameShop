package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.Usuario;
import com.ilerna.gameShop.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Controlador para gestionar login y registro de usuarios
 * Rutas principales:
 * - GET /login : Mostrar formulario de login
 * - POST /login : Procesar login
 * - GET /registro : Mostrar formulario de registro
 * - POST /registro : Procesar registro
 */
@Controller
public class LoginController {
    
    private UsuarioService usuarioService;
    
    // Constructor
    public LoginController() {
        this.usuarioService = new UsuarioService();
    }
    
    /**
     * Mostrar página de login
     */
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("titulo", "Login - GameShop");
        return "auth/login";
    }
    
    /**
     * Procesar login (POST)
     */
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String email,
            @RequestParam String contrasena,
            Model model) {
        
        Optional<Usuario> usuario = usuarioService.autenticar(email, contrasena);
        
        if (usuario.isPresent()) {
            // Login exitoso
            model.addAttribute("usuarioId", usuario.get().getId());
            model.addAttribute("usuarioNombre", usuario.get().getNombre());
            model.addAttribute("titulo", "Login exitoso - GameShop");
            return "auth/login-exitoso";
        } else {
            // Login fallido
            model.addAttribute("error", "Email o contraseña incorrectos");
            model.addAttribute("titulo", "Login - GameShop");
            return "auth/login";
        }
    }
    
    /**
     * Mostrar página de registro
     */
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("titulo", "Registro - GameShop");
        return "auth/registro";
    }
    
    /**
     * Procesar registro (POST)
     */
    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam String contrasenaRepetida,
            @RequestParam(required = false) String telefono,
            Model model) {
        
        // Validar que las contraseñas coincidan
        if (!contrasena.equals(contrasenaRepetida)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            model.addAttribute("nombre", nombre);
            model.addAttribute("email", email);
            model.addAttribute("titulo", "Registro - GameShop");
            return "auth/registro";
        }
        
        // Intentar registrar el usuario
        boolean registroExitoso = usuarioService.registrar(nombre, email, contrasena, telefono);
        
        if (registroExitoso) {
            // Registro exitoso
            model.addAttribute("mensaje", "Registro completado exitosamente");
            model.addAttribute("titulo", "Registro exitoso - GameShop");
            return "auth/registro-exitoso";
        } else {
            // Registro fallido
            String error = usuarioService.obtenerPorEmail(email).isPresent() 
                ? "El email ya está registrado" 
                : "Error en los datos ingresados";
            
            model.addAttribute("error", error);
            model.addAttribute("nombre", nombre);
            model.addAttribute("email", email);
            model.addAttribute("titulo", "Registro - GameShop");
            return "auth/registro";
        }
    }
    
    /**
     * Mostrar perfil del usuario
     */
    @GetMapping("/perfil")
    public String mostrarPerfil(
            @RequestParam int usuarioId,
            Model model) {
        
        Optional<Usuario> usuario = usuarioService.obtenerPorId(usuarioId);
        
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("titulo", "Mi Perfil - GameShop");
            return "auth/perfil";
        }
        
        return "redirect:/login";
    }
    
    /**
     * Logout
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}

