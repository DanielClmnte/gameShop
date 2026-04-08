package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.Plataforma;
import com.ilerna.gameShop.model.Videojuego;
import com.ilerna.gameShop.service.PlataformaService;
import com.ilerna.gameShop.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para el panel de administración
 * Rutas principales:
 * - GET /admin : Dashboard
 * - GET /admin/productos : Listar productos
 * - GET /admin/productos/nuevo : Formulario nuevo
 * - POST /admin/productos/guardar : Guardar producto
 * - GET /admin/productos/editar/{id} : Editar producto
 * - GET /admin/productos/eliminar/{id} : Eliminar producto
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private VideojuegoService videojuegoService;
    private PlataformaService plataformaService;
    
    // Constructor
    public AdminController() {
        this.videojuegoService = new VideojuegoService();
        this.plataformaService = new PlataformaService();
    }
    
    /**
     * Dashboard del administrador
     */
    @GetMapping
    public String mostrarDashboard(Model model) {
        List<Videojuego> videojuegos = videojuegoService.obtenerTodos();
        
        model.addAttribute("totalProductos", videojuegos.size());
        model.addAttribute("productosDisponibles", videojuegoService.obtenerDisponibles().size());
        model.addAttribute("productosAgotados", videojuegos.size() - videojuegoService.obtenerDisponibles().size());
        model.addAttribute("titulo", "Panel de Administración - GameShop");
        
        return "admin/dashboard";
    }
    
    /**
     * Listar todos los productos
     */
    @GetMapping("/productos")
    public String listarProductos(Model model) {
        List<Videojuego> videojuegos = videojuegoService.obtenerTodos();
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        model.addAttribute("videojuegos", videojuegos);
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("titulo", "Gestionar Productos - GameShop");
        
        return "admin/productos";
    }
    
    /**
     * Mostrar formulario para crear nuevo producto
     */
    @GetMapping("/productos/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("titulo", "Crear Producto - GameShop");
        
        return "admin/form-producto";
    }
    
    /**
     * Guardar un nuevo producto (POST)
     */
    @PostMapping("/productos/guardar")
    public String guardarProducto(
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam double precio,
            @RequestParam int stock,
            @RequestParam int plataformaId,
            @RequestParam String desarrollador,
            @RequestParam(required = false) String imagen) {
        
        Videojuego videojuego = new Videojuego(0, titulo, descripcion, precio, stock, plataformaId, imagen != null ? imagen : "sin-imagen.jpg");
        videojuego.setDesarrollador(desarrollador);
        videojuego.setFechaLanzamiento(LocalDate.now());
        videojuego.setCalificacion(0.0);
        
        videojuegoService.crearVideojuego(videojuego);
        
        return "redirect:/admin/productos";
    }
    
    /**
     * Mostrar formulario para editar producto
     */
    @GetMapping("/productos/editar/{id}")
    public String mostrarFormularioEditar(
            @PathVariable int id,
            Model model) {
        
        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(id);
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        if (videojuego.isPresent()) {
            model.addAttribute("videojuego", videojuego.get());
            model.addAttribute("plataformas", plataformas);
            model.addAttribute("titulo", "Editar Producto - GameShop");
            
            return "admin/form-producto";
        }
        
        return "redirect:/admin/productos";
    }
    
    /**
     * Actualizar un producto existente (POST)
     */
    @PostMapping("/productos/actualizar/{id}")
    public String actualizarProducto(
            @PathVariable int id,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam double precio,
            @RequestParam int stock,
            @RequestParam int plataformaId,
            @RequestParam String desarrollador,
            @RequestParam(required = false) String imagen) {
        
        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(id);
        
        if (videojuego.isPresent()) {
            Videojuego vj = videojuego.get();
            vj.setTitulo(titulo);
            vj.setDescripcion(descripcion);
            vj.setPrecio(precio);
            vj.setStock(stock);
            vj.setPlataformaId(plataformaId);
            vj.setDesarrollador(desarrollador);
            if (imagen != null && !imagen.isEmpty()) {
                vj.setImagen(imagen);
            }
            
            videojuegoService.actualizarVideojuego(vj);
        }
        
        return "redirect:/admin/productos";
    }
    
    /**
     * Eliminar un producto
     */
    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable int id) {
        videojuegoService.eliminarVideojuego(id);
        return "redirect:/admin/productos";
    }
}

