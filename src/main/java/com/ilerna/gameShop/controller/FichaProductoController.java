package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.Opiniones;
import com.ilerna.gameShop.model.Videojuego;
import com.ilerna.gameShop.service.FavoritoService;
import com.ilerna.gameShop.service.OpinionesService;
import com.ilerna.gameShop.service.VideojuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

/**
 * Controlador para gestionar las fichas de productos individuales
 * Rutas principales:
 * - GET /producto/{id} : Mostrar ficha de producto
 * - POST /producto/{id}/opinion : Agregar opinión
 */
@Controller
public class FichaProductoController {
    
    private VideojuegoService videojuegoService;
    private OpinionesService opinionesService;
    private FavoritoService favoritoService;
    
    // Constructor
    public FichaProductoController() {
        this.videojuegoService = new VideojuegoService();
        this.opinionesService = new OpinionesService();
        this.favoritoService = new FavoritoService();
    }
    
    /**
     * Mostrar la ficha completa de un videojuego
     */
    @GetMapping("/producto/{id}")
    public String mostrarFichaProducto(@PathVariable int id, HttpSession session, Model model) {
        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(id);
        
        if (videojuego.isPresent()) {
            Videojuego vj = videojuego.get();
            List<Opiniones> opiniones = opinionesService.obtenerPorVideojuego(id);
            double calificacionPromedio = opinionesService.obtenerCalificacionPromedio(id);
            
            // Juegos relacionados (misma plataforma, excluyendo el actual)
            List<Videojuego> relacionados = videojuegoService.obtenerPorPlataforma(vj.getPlataformaId());
            relacionados.removeIf(v -> v.getId() == id);
            
            // Comprobar si es favorito del usuario logueado
            boolean esFavorito = false;
            Integer usuarioId = (Integer) session.getAttribute("usuarioId");
            if (usuarioId != null) {
                esFavorito = favoritoService.esFavorito(usuarioId, id);
            }
            
            model.addAttribute("videojuego", vj);
            model.addAttribute("opiniones", opiniones);
            model.addAttribute("calificacionPromedio", calificacionPromedio);
            model.addAttribute("totalOpiniones", opiniones.size());
            model.addAttribute("relacionados", relacionados);
            model.addAttribute("esFavorito", esFavorito);
            model.addAttribute("titulo", vj.getTitulo() + " - GameShop");
            
            return "producto/ficha";
        }
        
        return "redirect:/";
    }
    
    /**
     * Mostrar la página de agregar opinión (requiere login)
     */
    @GetMapping("/producto/{id}/nueva-opinion")
    public String mostrarFormularioOpinion(@PathVariable int id, HttpSession session, Model model) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login?redirect=/producto/" + id + "/nueva-opinion";
        }

        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(id);
        
        if (videojuego.isPresent()) {
            model.addAttribute("videojuegoId", id);
            model.addAttribute("videojuegoTitulo", videojuego.get().getTitulo());
            model.addAttribute("titulo", "Agregar opinión - GameShop");
            return "producto/agregar-opinion";
        }
        
        return "redirect:/";
    }
    
    /**
     * Guardar una nueva opinión (POST) — usa el usuario de la sesión
     */
    @PostMapping("/producto/{id}/guardar-opinion")
    public String guardarOpinion(
            @PathVariable int id,
            @RequestParam int calificacion,
            @RequestParam String comentario,
            HttpSession session) {

        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login?redirect=/producto/" + id + "/nueva-opinion";
        }

        Opiniones nuevaOpinion = new Opiniones(0, id, usuarioId, calificacion, comentario, false);
        opinionesService.crearOpinion(nuevaOpinion);
        
        return "redirect:/producto/" + id;
    }
    
    /**
     * Mostrar juegos relacionados (misma plataforma)
     */
    @GetMapping("/producto/{id}/relacionados")
    public String mostrarRelacionados(@PathVariable int id, Model model) {
        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(id);
        
        if (videojuego.isPresent()) {
            int plataformaId = videojuego.get().getPlataformaId();
            List<Videojuego> relacionados = videojuegoService.obtenerPorPlataforma(plataformaId);
            
            // Filtrar el juego actual de la lista
            relacionados.removeIf(v -> v.getId() == id);
            
            model.addAttribute("videojuegos", relacionados);
            model.addAttribute("titulo", "Productos relacionados - GameShop");
            
            return "producto/relacionados";
        }
        
        return "redirect:/";
    }
}
