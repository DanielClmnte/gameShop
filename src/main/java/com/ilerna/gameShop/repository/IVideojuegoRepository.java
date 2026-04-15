package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Videojuego;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio de videojuegos.
 * Define las operaciones CRUD contra la tabla 'videojuegos'.
 */
public interface IVideojuegoRepository {
    List<Videojuego> obtenerTodos();
    List<Videojuego> obtenerPorPlataforma(int plataformaId);
    Optional<Videojuego> obtenerPorId(int id);
    List<Videojuego> buscarPorTitulo(String titulo);
    List<Videojuego> obtenerDisponibles();
    List<Videojuego> obtenerPorCalificacion();
    void guardar(Videojuego videojuego);
    void actualizar(Videojuego videojuego);
    void eliminarPorId(int id);

    // ── Métodos paginados ──
    List<Videojuego> obtenerTodosPaginado(int offset, int limit);
    int contarTodos();
    List<Videojuego> obtenerPorPlataformaPaginado(int plataformaId, int offset, int limit);
    int contarPorPlataforma(int plataformaId);
    List<Videojuego> obtenerDisponiblesPaginado(int offset, int limit);
    int contarDisponibles();
    List<Videojuego> obtenerPorCalificacionPaginado(int offset, int limit);
    List<Videojuego> buscarPorTituloPaginado(String titulo, int offset, int limit);
    int contarPorTitulo(String titulo);
}
