package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Favorito;
import com.ilerna.gameShop.repository.FavoritoRepository;

import java.util.List;

/**
 * Servicio para gestionar la lista de deseados/favoritos.
 */
public class FavoritoService implements IFavoritoService {

    private FavoritoRepository favoritoRepository;

    public FavoritoService() {
        this.favoritoRepository = new FavoritoRepository();
    }

    public List<Favorito> obtenerFavoritos(int usuarioId) {
        return favoritoRepository.obtenerPorUsuario(usuarioId);
    }

    public void agregarFavorito(int usuarioId, int videojuegoId) {
        favoritoRepository.agregar(usuarioId, videojuegoId);
    }

    public void eliminarFavorito(int usuarioId, int videojuegoId) {
        favoritoRepository.eliminar(usuarioId, videojuegoId);
    }

    public boolean esFavorito(int usuarioId, int videojuegoId) {
        return favoritoRepository.existe(usuarioId, videojuegoId);
    }

    /**
     * Toggle: si ya es favorito lo quita, si no lo añade.
     * @return true si después del toggle el juego ES favorito
     */
    public boolean toggleFavorito(int usuarioId, int videojuegoId) {
        if (favoritoRepository.existe(usuarioId, videojuegoId)) {
            favoritoRepository.eliminar(usuarioId, videojuegoId);
            return false;
        } else {
            favoritoRepository.agregar(usuarioId, videojuegoId);
            return true;
        }
    }

    public int contarFavoritos(int usuarioId) {
        return favoritoRepository.contarPorUsuario(usuarioId);
    }
}

