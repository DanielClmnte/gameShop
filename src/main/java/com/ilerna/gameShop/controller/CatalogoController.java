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
     * Mostrar página de inicio con secciones (destacados, más vendidos, novedades)
     */
    @GetMapping("/")
    public String mostrarInicio(Model model) {
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        // Secciones con límite de items (2 filas de 6 = 12)
        List<Videojuego> destacados = videojuegoService.obtenerPorCalificacion().stream().limit(12).toList();
        List<Videojuego> masVendidos = videojuegoService.obtenerMasVendidos(12);
        List<Videojuego> novedades = videojuegoService.obtenerNovedades(12);
        
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("destacados", destacados);
        model.addAttribute("masVendidos", masVendidos);
        model.addAttribute("novedades", novedades);
        model.addAttribute("titulo", "GameShop - Tu tienda de videojuegos");
        
        return "catalogo/inicio";
    }
    
    /**
     * Ver todo el catálogo (paginado) con filtros de precio opcionales
     */
    @GetMapping("/catalogo/todos")
    public String mostrarTodos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            Model model) {
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        PaginaResultado<Videojuego> pagina = videojuegoService.obtenerTodosPaginado(page);
        
        // Filtrar por precio si se especifican parámetros
        if (precioMin != null || precioMax != null) {
            List<Videojuego> filtered = pagina.getItems().stream()
                .filter(v -> {
                    boolean cumpleMin = precioMin == null || v.getPrecio() >= precioMin;
                    boolean cumpleMax = precioMax == null || v.getPrecio() <= precioMax;
                    return cumpleMin && cumpleMax;
                })
                .toList();
            model.addAttribute("videojuegos", filtered);
            // Construir baseUrl con parámetros
            StringBuilder baseUrl = new StringBuilder("/catalogo/todos?");
            if (precioMin != null) baseUrl.append("precioMin=").append(precioMin).append("&");
            if (precioMax != null) baseUrl.append("precioMax=").append(precioMax).append("&");
            model.addAttribute("baseUrl", baseUrl.toString());
            model.addAttribute("filtroActivo", true);
        } else {
            model.addAttribute("videojuegos", pagina.getItems());
            model.addAttribute("baseUrl", "/catalogo/todos");
        }
        
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("pagina", pagina);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        model.addAttribute("titulo", "Todo el Catálogo - GameShop");
        
        return "catalogo/todos";
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
    
    /**
     * Mostrar novedades (ordenados por fecha lanzamiento DESC) (paginado)
     */
    @GetMapping("/catalogo/novedades")
    public String mostrarNovedades(
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        PaginaResultado<Videojuego> pagina = videojuegoService.obtenerNovedadesPaginado(page);
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        model.addAttribute("videojuegos", pagina.getItems());
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("pagina", pagina);
        model.addAttribute("baseUrl", "/catalogo/novedades");
        model.addAttribute("titulo", "Novedades - GameShop");
        
        return "catalogo/novedades";
    }
    
    /**
     * Mostrar más vendidos (por total vendido en pedidos) (paginado)
     */
    @GetMapping("/catalogo/mas-vendidos")
    public String mostrarMasVendidos(
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        PaginaResultado<Videojuego> pagina = videojuegoService.obtenerMasVendidosPaginado(page);
        List<Plataforma> plataformas = plataformaService.obtenerTodas();
        
        model.addAttribute("videojuegos", pagina.getItems());
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("pagina", pagina);
        model.addAttribute("baseUrl", "/catalogo/mas-vendidos");
        model.addAttribute("titulo", "Más Vendidos - GameShop");
        
        return "catalogo/mas-vendidos";
    }
}
