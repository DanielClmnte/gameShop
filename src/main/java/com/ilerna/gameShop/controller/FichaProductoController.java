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
 * Controlador para las fichas de productos individuales.
 * Rutas: GET /producto/{id}, GET /producto/{id}/nueva-opinion, POST /producto/{id}/guardar-opinion
 */
@Controller
public class FichaProductoController {

    private final VideojuegoService videojuegoService;
    private final OpinionesService opinionesService;
    private final FavoritoService favoritoService;

    public FichaProductoController() {
        this.videojuegoService = new VideojuegoService();
        this.opinionesService  = new OpinionesService();
        this.favoritoService   = new FavoritoService();
    }

    /** Mostrar la ficha completa de un videojuego */
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

    /** Mostrar formulario de opinión (requiere login) */
    @GetMapping("/producto/{id}/nueva-opinion")
    public String mostrarFormularioOpinion(@PathVariable int id, HttpSession session, Model model) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) return "redirect:/login?redirect=/producto/" + id + "/nueva-opinion";

        Optional<Videojuego> videojuego = videojuegoService.obtenerPorId(id);
        if (videojuego.isPresent()) {
            model.addAttribute("videojuegoId", id);
            model.addAttribute("videojuegoTitulo", videojuego.get().getTitulo());
            model.addAttribute("titulo", "Agregar opinión - GameShop");
            return "producto/agregar-opinion";
        }
        return "redirect:/";
    }

    /** Guardar una nueva opinión */
    @PostMapping("/producto/{id}/guardar-opinion")
    public String guardarOpinion(
            @PathVariable int id,
            @RequestParam int calificacion,
            @RequestParam String comentario,
            HttpSession session) {

        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) return "redirect:/login?redirect=/producto/" + id + "/nueva-opinion";

        if (calificacion < 1 || calificacion > 5) return "redirect:/producto/" + id;
        if (comentario == null || comentario.isBlank() || comentario.length() > 1000)
            return "redirect:/producto/" + id;

        opinionesService.crearOpinion(new Opiniones(0, id, usuarioId, calificacion, comentario.trim(), false));
        return "redirect:/producto/" + id;
    }
}
