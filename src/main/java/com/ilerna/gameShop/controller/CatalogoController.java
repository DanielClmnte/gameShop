package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.PaginaResultado;
import com.ilerna.gameShop.model.Plataforma;
import com.ilerna.gameShop.model.Videojuego;
import com.ilerna.gameShop.service.PlataformaService;
import com.ilerna.gameShop.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para gestionar el catálogo de videojuegos por plataforma.
 * Todas las vistas usan paginación server-side (16 items por página).
 */
@Controller
public class CatalogoController {
    
    private PlataformaService plataformaService;
    private VideojuegoService videojuegoService;
    
    public CatalogoController() {
        this.plataformaService = new PlataformaService();
        this.videojuegoService = new VideojuegoService();
    }
    
    /**
     * Mostrar el catálogo principal con todas las plataformas (paginado)
     */
    @GetMapping("/")
    public String mostrarCatalogoPrincipal(
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        PaginaResultado<Videojuego> pagina = videojuegoService.obtenerTodosPaginado(page);
        
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("videojuegos", pagina.getItems());
        model.addAttribute("pagina", pagina);
        model.addAttribute("baseUrl", "/");
        model.addAttribute("titulo", "Catálogo de Videojuegos - GameShop");
        
        return "catalogo/principal";
    }
    
    /**
     * Mostrar videojuegos de una plataforma específica (paginado)
     */
    @GetMapping("/catalogo/plataforma/{id}")
    public String mostrarPorPlataforma(
            @PathVariable int id,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        Optional<Plataforma> plataforma = plataformaService.obtenerPorId(id);
        List<Plataforma> todasPlataformas = plataformaService.obtenerTodas();
        
        if (plataforma.isPresent()) {
            PaginaResultado<Videojuego> pagina = videojuegoService.obtenerPorPlataformaPaginado(id, page);
            model.addAttribute("plataforma", plataforma.get());
            model.addAttribute("videojuegos", pagina.getItems());
            model.addAttribute("plataformas", todasPlataformas);
            model.addAttribute("pagina", pagina);
            model.addAttribute("baseUrl", "/catalogo/plataforma/" + id);
            model.addAttribute("titulo", "Juegos de " + plataforma.get().getNombre() + " - GameShop");
            return "catalogo/plataforma";
        }
        
        return "redirect:/";
    }
    
    /**
     * Buscar videojuegos por nombre (paginado)
     */
    @GetMapping("/catalogo/buscar")
    public String buscarPorTitulo(
            @RequestParam String titulo,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        PaginaResultado<Videojuego> pagina = videojuegoService.buscarPorTituloPaginado(titulo, page);
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        model.addAttribute("videojuegos", pagina.getItems());
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("terminoBusqueda", titulo);
        model.addAttribute("pagina", pagina);
        model.addAttribute("baseUrl", "/catalogo/buscar?titulo=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8));
        model.addAttribute("titulo", "Resultados de búsqueda: " + titulo + " - GameShop");
        
        return "catalogo/resultados-busqueda";
    }
    
    /**
     * Mostrar videojuegos disponibles (en stock) (paginado)
     */
    @GetMapping("/catalogo/disponibles")
    public String mostrarDisponibles(
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        PaginaResultado<Videojuego> pagina = videojuegoService.obtenerDisponiblesPaginado(page);
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        model.addAttribute("videojuegos", pagina.getItems());
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("pagina", pagina);
        model.addAttribute("baseUrl", "/catalogo/disponibles");
        model.addAttribute("titulo", "Videojuegos Disponibles - GameShop");
        
        return "catalogo/disponibles";
    }
    
    /**
     * Mostrar videojuegos ordenados por calificación (más populares) (paginado)
     */
    @GetMapping("/catalogo/populares")
    public String mostrarPopulares(
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        PaginaResultado<Videojuego> pagina = videojuegoService.obtenerPorCalificacionPaginado(page);
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        model.addAttribute("videojuegos", pagina.getItems());
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("pagina", pagina);
        model.addAttribute("baseUrl", "/catalogo/populares");
        model.addAttribute("titulo", "Juegos Más Populares - GameShop");
        
        return "catalogo/populares";
    }
}
