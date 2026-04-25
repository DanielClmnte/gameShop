package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Favorito;
import java.util.List;

/**
 * Interfaz del repositorio de favoritos.
 * Define las operaciones contra la tabla 'favoritos'.
 */
public interface IFavoritoRepository {
    List<Favorito> obtenerPorUsuario(int usuarioId);
    void agregar(int usuarioId, int videojuegoId);
    void eliminar(int usuarioId, int videojuegoId);
    boolean existe(int usuarioId, int videojuegoId);
    int contarPorUsuario(int usuarioId);
}

