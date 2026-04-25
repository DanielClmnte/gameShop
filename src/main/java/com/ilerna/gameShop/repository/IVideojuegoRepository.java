package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Videojuego;
import java.util.List;
import java.util.Optional;

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

    // Métodos de Paginación y Especiales
    List<Videojuego> obtenerTodosPaginado(int offset, int limit);
    int contarTodos();
    List<Videojuego> obtenerPorPlataformaPaginado(int plataformaId, int offset, int limit);
    int contarPorPlataforma(int plataformaId);
    List<Videojuego> obtenerDisponiblesPaginado(int offset, int limit);
    int contarDisponibles();
    List<Videojuego> buscarPorTituloPaginado(String titulo, int offset, int limit);
    int contarPorTitulo(String titulo);

    // Novedades, Populares y Más Vendidos
    List<Videojuego> obtenerNovedades(int limit);
    List<Videojuego> obtenerNovedadesPaginado(int offset, int limit);
    int contarNovedades();
    List<Videojuego> obtenerPopulares(int limit);
    List<Videojuego> obtenerPopularesPaginado(int offset, int limit);
    int contarPopulares();
    List<Videojuego> obtenerMasVendidos(int limit);
    List<Videojuego> obtenerMasVendidosPaginado(int offset, int limit);
    int contarMasVendidos();
}