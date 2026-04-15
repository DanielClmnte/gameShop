package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.Plataforma;
import com.ilerna.gameShop.model.Videojuego;
import com.ilerna.gameShop.service.ImagenService;
import com.ilerna.gameShop.service.PlataformaService;
import com.ilerna.gameShop.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador del panel de administración.
 * Todas las rutas /admin/** están protegidas por AdminSecurityInterceptor.
 * Solo usuarios con rol ADMIN pueden acceder.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private VideojuegoService videojuegoService;
    private PlataformaService plataformaService;
    private ImagenService imagenService;

    public AdminController() {
        this.videojuegoService = new VideojuegoService();
        this.plataformaService = new PlataformaService();
        this.imagenService = new ImagenService();
    }

    // ──────────────── DASHBOARD ────────────────

    @GetMapping
    public String mostrarDashboard(Model model) {
        List<Videojuego> videojuegos = videojuegoService.obtenerTodos();
        model.addAttribute("totalProductos", videojuegos.size());
        model.addAttribute("productosDisponibles", videojuegoService.obtenerDisponibles().size());
        model.addAttribute("productosAgotados", videojuegos.size() - videojuegoService.obtenerDisponibles().size());
        model.addAttribute("titulo", "Panel de Administración - GameShop");
        return "admin/dashboard";
    }

    // ──────────────── LISTAR PRODUCTOS ────────────────

    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("videojuegos", videojuegoService.obtenerTodos());
        model.addAttribute("plataformas", plataformaService.obtenerTodas());
        model.addAttribute("titulo", "Gestionar Productos - GameShop");
        return "admin/productos";
    }

    // ──────────────── FORMULARIO NUEVO PRODUCTO ────────────────

    @GetMapping("/productos/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("plataformas", plataformaService.obtenerTodas());
        model.addAttribute("titulo", "Crear Producto - GameShop");
        return "admin/form-producto";
    }

    // ──────────────── GUARDAR NUEVO PRODUCTO (multipart) ────────────────

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

        // ID asignado automáticamente por AUTO_INCREMENT de la BD
        Videojuego videojuego = new Videojuego(0, titulo, descripcion, precio, stock, plataformaId, nombreImagen);
        videojuego.setDesarrollador(desarrollador);
        videojuego.setFechaLanzamiento(LocalDate.now());
        videojuego.setCalificacion(0.0);

        videojuegoService.crearVideojuego(videojuego);

        return "redirect:/admin/productos";
    }

    // ──────────────── FORMULARIO EDITAR PRODUCTO ────────────────

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

    // ──────────────── ACTUALIZAR PRODUCTO (multipart) ────────────────

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

            // Solo cambiar imagen si se subió una nueva
            String nuevaImagen = resolverNombreImagen(imagenFile, null, imagenActual);
            vj.setImagen(nuevaImagen);

            videojuegoService.actualizarVideojuego(vj);
        }
        return "redirect:/admin/productos";
    }

    // ──────────────── ELIMINAR PRODUCTO ────────────────

    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable int id) {
        // Eliminar imagen asociada
        videojuegoService.obtenerPorId(id).ifPresent(vj ->
                imagenService.eliminarImagen(vj.getImagen()));
        videojuegoService.eliminarVideojuego(id);
        return "redirect:/admin/productos";
    }

    // ──────────────── UTILIDADES ────────────────

    /**
     * Determina la URL de imagen a usar:
     * 1. Si se subió un archivo → guarda el fichero y devuelve /uploads/images/nombre
     * 2. Si se escribió un nombre en el texto → si no tiene la ruta, la añade
     * 3. Si nada → mantiene la URL anterior (fallback)
     */
    private String resolverNombreImagen(MultipartFile file, String nombreTexto, String fallback) {
        if (file != null && !file.isEmpty()) {
            try {
                return imagenService.guardarImagen(file); // ya devuelve "/uploads/images/..."
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Error al guardar imagen: " + e.getMessage());
            }
        }
        if (nombreTexto != null && !nombreTexto.isBlank()) {
            // Si el admin escribe solo "elden-ring.jpg", construir la URL completa
            return nombreTexto.startsWith("/uploads/")
                    ? nombreTexto
                    : "/uploads/images/" + nombreTexto;
        }
        return (fallback != null && !fallback.isBlank()) ? fallback : "/uploads/images/sin-imagen.jpg";
    }
}

