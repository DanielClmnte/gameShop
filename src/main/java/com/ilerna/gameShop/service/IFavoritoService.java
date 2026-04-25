package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Favorito;
import java.util.List;

/**
 * Interfaz del servicio de favoritos/lista de deseados.
 * Define la logica de negocio para la gestion de favoritos.
 */
public interface IFavoritoService {
    List<Favorito> obtenerFavoritos(int usuarioId);
    void agregarFavorito(int usuarioId, int videojuegoId);
    void eliminarFavorito(int usuarioId, int videojuegoId);
    boolean esFavorito(int usuarioId, int videojuegoId);
    boolean toggleFavorito(int usuarioId, int videojuegoId);
    int contarFavoritos(int usuarioId);
}

