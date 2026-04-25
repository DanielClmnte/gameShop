package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.Favorito;
import com.ilerna.gameShop.service.FavoritoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * Controlador para la lista de deseados / favoritos.
 * Requiere login para todas las operaciones.
 */
@Controller
public class FavoritoController {

    private FavoritoService favoritoService;

    public FavoritoController() {
        this.favoritoService = new FavoritoService();
    }

    /**
     * Ver lista de favoritos del usuario logueado
     */
    @GetMapping("/favoritos")
    public String verFavoritos(HttpSession session, Model model) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login?redirect=/favoritos";
        }

        List<Favorito> favoritos = favoritoService.obtenerFavoritos(usuarioId);
        model.addAttribute("favoritos", favoritos);
        model.addAttribute("titulo", "Mis Favoritos - GameShop");
        return "favoritos/lista";
    }

    /**
     * Toggle favorito: añade si no existe, quita si existe.
     * Redirige a la página de origen.
     */
    @PostMapping("/favoritos/toggle/{videojuegoId}")
    public String toggleFavorito(
            @PathVariable int videojuegoId,
            @RequestParam(required = false, defaultValue = "/") String redirect,
            HttpSession session) {

        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login?redirect=" + redirect;
        }

        favoritoService.toggleFavorito(usuarioId, videojuegoId);
        return "redirect:" + redirect;
    }

    /**
     * Eliminar de favoritos (desde la página de lista)
     */
    @PostMapping("/favoritos/eliminar/{videojuegoId}")
    public String eliminarFavorito(@PathVariable int videojuegoId, HttpSession session) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login?redirect=/favoritos";
        }

        favoritoService.eliminarFavorito(usuarioId, videojuegoId);
        return "redirect:/favoritos";
    }
}

