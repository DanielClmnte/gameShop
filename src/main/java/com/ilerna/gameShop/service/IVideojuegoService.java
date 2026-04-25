package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.PaginaResultado;
import com.ilerna.gameShop.model.Videojuego;
import java.util.List;
import java.util.Optional;

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

    // Paginados
    PaginaResultado<Videojuego> obtenerTodosPaginado(int pagina);
    PaginaResultado<Videojuego> obtenerPorPlataformaPaginado(int plataformaId, int pagina);
    PaginaResultado<Videojuego> obtenerDisponiblesPaginado(int pagina);
    PaginaResultado<Videojuego> buscarPorTituloPaginado(String titulo, int pagina);
    PaginaResultado<Videojuego> obtenerNovedadesPaginado(int pagina);
    PaginaResultado<Videojuego> obtenerPopularesPaginado(int pagina);
    PaginaResultado<Videojuego> obtenerMasVendidosPaginado(int pagina);

    // Métodos para Inicio (con limit)
    List<Videojuego> obtenerNovedades(int limit);
    List<Videojuego> obtenerPopulares(int limit);
    List<Videojuego> obtenerMasVendidos(int limit);
}