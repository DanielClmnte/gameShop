package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.Usuario;
import com.ilerna.gameShop.model.Videojuego;
import com.ilerna.gameShop.service.ImagenService;
import com.ilerna.gameShop.service.PlataformaService;
import com.ilerna.gameShop.service.UsuarioService;
import com.ilerna.gameShop.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private VideojuegoService videojuegoService;
    private PlataformaService plataformaService;
    private ImagenService imagenService;
    private UsuarioService usuarioService;

    public AdminController() {
        this.videojuegoService = new VideojuegoService();
        this.plataformaService = new PlataformaService();
        this.imagenService = new ImagenService();
        this.usuarioService = new UsuarioService();
    }

    @GetMapping
    public String mostrarDashboard(Model model) {
        List<Videojuego> videojuegos = videojuegoService.obtenerTodos();
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        model.addAttribute("totalProductos", videojuegos.size());
        model.addAttribute("productosDisponibles", videojuegoService.obtenerDisponibles().size());
        model.addAttribute("productosAgotados", videojuegos.size() - videojuegoService.obtenerDisponibles().size());
        model.addAttribute("totalUsuarios", usuarios.size());
        model.addAttribute("titulo", "Panel de Administración - GameShop");
        return "admin/dashboard";
    }

    // ── PRODUCTOS ──

    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("videojuegos", videojuegoService.obtenerTodos());
        model.addAttribute("plataformas", plataformaService.obtenerTodas());
        model.addAttribute("titulo", "Gestionar Productos - GameShop");
        return "admin/productos";
    }

    @GetMapping("/productos/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("plataformas", plataformaService.obtenerTodas());
        model.addAttribute("titulo", "Crear Producto - GameShop");
        return "admin/form-producto";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam double precio,
            @RequestParam int stock,
            @RequestParam int plataformaId,
            @RequestParam String desarrollador,
            @RequestParam(required = false) MultipartFile imagenFile,
            @RequestParam(required = false) String imagenNombre) {

        String nombreImagen = resolverNombreImagen(imagenFile, imagenNombre, "/uploads/images/sin-imagen.jpg");
        Videojuego videojuego = new Videojuego(0, titulo, descripcion, precio, stock, plataformaId, nombreImagen);
        videojuego.setDesarrollador(desarrollador);
        videojuego.setFechaLanzamiento(LocalDate.now());
        videojuego.setCalificacion(0.0);
        videojuegoService.crearVideojuego(videojuego);
        return "redirect:/admin/productos";
    }

    @GetMapping("/productos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(id);
        if (videojuego.isPresent()) {
            model.addAttribute("videojuego", videojuego.get());
            model.addAttribute("plataformas", plataformaService.obtenerTodas());
            model.addAttribute("titulo", "Editar Producto - GameShop");
            return "admin/form-producto";
        }
        return "redirect:/admin/productos";
    }

    @PostMapping("/productos/actualizar/{id}")
    public String actualizarProducto(
            @PathVariable int id,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam double precio,
            @RequestParam int stock,
            @RequestParam int plataformaId,
            @RequestParam String desarrollador,
            @RequestParam(required = false) MultipartFile imagenFile,
            @RequestParam(required = false) String imagenActual) {

        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(id);
        if (videojuego.isPresent()) {
            Videojuego vj = videojuego.get();
            vj.setTitulo(titulo);
            vj.setDescripcion(descripcion);
            vj.setPrecio(precio);
            vj.setStock(stock);
            vj.setPlataformaId(plataformaId);
            vj.setDesarrollador(desarrollador);
            vj.setImagen(resolverNombreImagen(imagenFile, null, imagenActual));
            videojuegoService.actualizarVideojuego(vj);
        }
        return "redirect:/admin/productos";
    }

    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable int id) {
        videojuegoService.obtenerPorId(id).ifPresent(vj -> imagenService.eliminarImagen(vj.getImagen()));
        videojuegoService.eliminarVideojuego(id);
        return "redirect:/admin/productos";
    }

    // ── USUARIOS CRUD ──

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("titulo", "Gestionar Usuarios - GameShop");
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/nuevo")
    public String formNuevoUsuario(Model model) {
        model.addAttribute("titulo", "Crear Usuario - GameShop");
        return "admin/form-usuario";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam(required = false) String telefono,
            @RequestParam String rol,
            Model model) {

        if (nombre == null || nombre.trim().length() < 2
                || email == null || !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")
                || contrasena == null || contrasena.length() < 4) {
            model.addAttribute("error", "Datos inválidos. Nombre ≥ 2 caracteres, email válido, contraseña ≥ 4 caracteres.");
            model.addAttribute("titulo", "Crear Usuario - GameShop");
            return "admin/form-usuario";
        }
        boolean ok = usuarioService.crearUsuarioAdmin(nombre, email, contrasena, telefono, rol);
        if (!ok) {
            model.addAttribute("error", "El email ya está registrado.");
            model.addAttribute("titulo", "Crear Usuario - GameShop");
            return "admin/form-usuario";
        }
        return "redirect:/admin/usuarios?exito=creado";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String formEditarUsuario(@PathVariable int id, Model model) {
        Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("titulo", "Editar Usuario - GameShop");
            return "admin/form-usuario";
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/actualizar/{id}")
    public String actualizarUsuario(
            @PathVariable int id,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String codigoPostal,
            @RequestParam String rol,
            @RequestParam(defaultValue = "false") boolean activo,
            Model model) {

        if (nombre == null || nombre.trim().length() < 2
                || email == null || !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")
                || (codigoPostal != null && !codigoPostal.isBlank() && !codigoPostal.matches("[0-9]{5}"))) {
            Optional<Usuario> u = usuarioService.obtenerPorId(id);
            u.ifPresent(usr -> model.addAttribute("usuario", usr));
            model.addAttribute("error", "Datos inválidos. Revisa nombre, email y código postal.");
            model.addAttribute("titulo", "Editar Usuario - GameShop");
            return "admin/form-usuario";
        }
        boolean ok = usuarioService.actualizarUsuarioAdmin(id, nombre, email, telefono,
                direccion, ciudad, codigoPostal, rol, activo);
        if (!ok) {
            Optional<Usuario> u = usuarioService.obtenerPorId(id);
            u.ifPresent(usr -> model.addAttribute("usuario", usr));
            model.addAttribute("error", "El email ya está en uso por otro usuario.");
            model.addAttribute("titulo", "Editar Usuario - GameShop");
            return "admin/form-usuario";
        }
        return "redirect:/admin/usuarios?exito=actualizado";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable int id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/usuarios?exito=eliminado";
    }

    // ── UTILIDADES ──

    private String resolverNombreImagen(MultipartFile file, String nombreTexto, String fallback) {
        if (file != null && !file.isEmpty()) {
            try {
                return imagenService.guardarImagen(file);
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Error al guardar imagen: " + e.getMessage());
            }
        }
        if (nombreTexto != null && !nombreTexto.isBlank()) {
            return nombreTexto.startsWith("/uploads/") ? nombreTexto : "/uploads/images/" + nombreTexto;
        }
        return (fallback != null && !fallback.isBlank()) ? fallback : "/uploads/images/sin-imagen.jpg";
    }
}
