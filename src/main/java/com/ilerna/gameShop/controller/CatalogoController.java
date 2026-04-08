package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.Plataforma;
import com.ilerna.gameShop.model.Videojuego;
import com.ilerna.gameShop.service.PlataformaService;
import com.ilerna.gameShop.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * Controlador para gestionar el catálogo de videojuegos por plataforma
 * Rutas principales:
 * - GET /catalogo : Mostrar todas las plataformas y videojuegos
 * - GET /catalogo/plataforma/{id} : Filtrar por plataforma
 */
@Controller
public class CatalogoController {
    
    private PlataformaService plataformaService;
    private VideojuegoService videojuegoService;
    
    // Constructor
    public CatalogoController() {
        this.plataformaService = new PlataformaService();
        this.videojuegoService = new VideojuegoService();
    }
    
    /**
     * Mostrar el catálogo principal con todas las plataformas
     */
    @GetMapping("/")
    public String mostrarCatalogoPrincipal(Model model) {
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        List<Videojuego> videojuegos = videojuegoService.obtenerTodos();
        
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("videojuegos", videojuegos);
        model.addAttribute("titulo", "Catálogo de Videojuegos - GameShop");
        
        return "catalogo/principal";
    }
    
    /**
     * Mostrar videojuegos de una plataforma específica
     */
    @GetMapping("/catalogo/plataforma/{id}")
    public String mostrarPorPlataforma(@PathVariable int id, Model model) {
        Optional<Plataforma> plataforma = plataformaService.obtenerPorId(id);
        List<Videojuego> videojuegos = videojuegoService.obtenerPorPlataforma(id);
        List<Plataforma> todasPlataformas = plataformaService.obtenerTodas();
        
        if (plataforma.isPresent()) {
            model.addAttribute("plataforma", plataforma.get());
            model.addAttribute("videojuegos", videojuegos);
            model.addAttribute("plataformas", todasPlataformas);
            model.addAttribute("titulo", "Juegos de " + plataforma.get().getNombre() + " - GameShop");
            return "catalogo/plataforma";
        }
        
        return "redirect:/";
    }
    
    /**
     * Buscar videojuegos por nombre
     */
    @GetMapping("/catalogo/buscar")
    public String buscarPorTitulo(@RequestParam String titulo, Model model) {
        List<Videojuego> resultados = videojuegoService.buscarPorTitulo(titulo);
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        model.addAttribute("videojuegos", resultados);
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("terminoBusqueda", titulo);
        model.addAttribute("titulo", "Resultados de búsqueda: " + titulo + " - GameShop");
        
        return "catalogo/resultados-busqueda";
    }
    
    /**
     * Mostrar videojuegos disponibles (en stock)
     */
    @GetMapping("/catalogo/disponibles")
    public String mostrarDisponibles(Model model) {
        List<Videojuego> disponibles = videojuegoService.obtenerDisponibles();
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        model.addAttribute("videojuegos", disponibles);
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("titulo", "Videojuegos Disponibles - GameShop");
        
        return "catalogo/disponibles";
    }
    
    /**
     * Mostrar videojuegos ordenados por calificación (más populares)
     */
    @GetMapping("/catalogo/populares")
    public String mostrarPopulares(Model model) {
        List<Videojuego> populares = videojuegoService.obtenerPorCalificacion();
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        model.addAttribute("videojuegos", populares);
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("titulo", "Juegos Más Populares - GameShop");
        
        return "catalogo/populares";
    }
}

