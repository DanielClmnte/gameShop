package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Videojuego;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de videojuegos.
 * Define la logica de negocio para la gestion de videojuegos.
 */
public interface IVideojuegoService {
    List<Videojuego> obtenerTodos();
    List<Videojuego> obtenerPorPlataforma(int plataformaId);
    Optional<Videojuego> obtenerPorId(int id);
    List<Videojuego> buscarPorTitulo(String titulo);
    List<Videojuego> obtenerDisponibles();
    List<Videojuego> obtenerPorCalificacion();
    void crearVideojuego(Videojuego videojuego);
    void actualizarVideojuego(Videojuego videojuego);
    void eliminarVideojuego(int id);
    void restarStock(int videojuegoId, int cantidad);
}

