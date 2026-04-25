package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.PaginaResultado;
import com.ilerna.gameShop.model.Videojuego;
import com.ilerna.gameShop.repository.VideojuegoRepository;
import com.ilerna.gameShop.repository.IVideojuegoRepository;
import java.util.List;
import java.util.Optional;

public class VideojuegoService implements IVideojuegoService {

    private IVideojuegoRepository videojuegoRepository;

    public VideojuegoService() {
        this.videojuegoRepository = new VideojuegoRepository();
    }

    // --- MÉTODOS SIMPLES ---
    public List<Videojuego> obtenerTodos() { return videojuegoRepository.obtenerTodos(); }
    public List<Videojuego> obtenerPorPlataforma(int plataformaId) { return videojuegoRepository.obtenerPorPlataforma(plataformaId); }
    public Optional<Videojuego> obtenerPorId(int id) { return videojuegoRepository.obtenerPorId(id); }
    public List<Videojuego> buscarPorTitulo(String titulo) { return videojuegoRepository.buscarPorTitulo(titulo); }
    public List<Videojuego> obtenerDisponibles() { return videojuegoRepository.obtenerDisponibles(); }
    public List<Videojuego> obtenerPorCalificacion() { return videojuegoRepository.obtenerPorCalificacion(); }
    public void crearVideojuego(Videojuego v) { videojuegoRepository.guardar(v); }
    public void actualizarVideojuego(Videojuego v) { videojuegoRepository.actualizar(v); }
    public void eliminarVideojuego(int id) { videojuegoRepository.eliminarPorId(id); }

    public void restarStock(int id, int cant) {
        videojuegoRepository.obtenerPorId(id).ifPresent(v -> {
            if(v.getStock() >= cant) { v.setStock(v.getStock() - cant); videojuegoRepository.actualizar(v); }
        });
    }

    // --- MÉTODOS PAGINADOS ---
    private int getOffset(int pagina) { return PaginaResultado.calcularOffset(pagina < 1 ? 1 : pagina); }

    public PaginaResultado<Videojuego> obtenerTodosPaginado(int p) {
        return new PaginaResultado<>(videojuegoRepository.obtenerTodosPaginado(getOffset(p), PaginaResultado.ITEMS_POR_PAGINA), p, videojuegoRepository.contarTodos());
    }

    public PaginaResultado<Videojuego> obtenerPorPlataformaPaginado(int id, int p) {
        return new PaginaResultado<>(videojuegoRepository.obtenerPorPlataformaPaginado(id, getOffset(p), PaginaResultado.ITEMS_POR_PAGINA), p, videojuegoRepository.contarPorPlataforma(id));
    }

    public PaginaResultado<Videojuego> obtenerDisponiblesPaginado(int p) {
        return new PaginaResultado<>(videojuegoRepository.obtenerDisponiblesPaginado(getOffset(p), PaginaResultado.ITEMS_POR_PAGINA), p, videojuegoRepository.contarDisponibles());
    }

    public PaginaResultado<Videojuego> buscarPorTituloPaginado(String t, int p) {
        return new PaginaResultado<>(videojuegoRepository.buscarPorTituloPaginado(t, getOffset(p), PaginaResultado.ITEMS_POR_PAGINA), p, videojuegoRepository.contarPorTitulo(t));
    }

    public PaginaResultado<Videojuego> obtenerNovedadesPaginado(int p) {
        return new PaginaResultado<>(videojuegoRepository.obtenerNovedadesPaginado(getOffset(p), PaginaResultado.ITEMS_POR_PAGINA), p, videojuegoRepository.contarNovedades());
    }

    public PaginaResultado<Videojuego> obtenerPopularesPaginado(int p) {
        return new PaginaResultado<>(videojuegoRepository.obtenerPopularesPaginado(getOffset(p), PaginaResultado.ITEMS_POR_PAGINA), p, videojuegoRepository.contarPopulares());
    }

    public PaginaResultado<Videojuego> obtenerMasVendidosPaginado(int p) {
        return new PaginaResultado<>(videojuegoRepository.obtenerMasVendidosPaginado(getOffset(p), PaginaResultado.ITEMS_POR_PAGINA), p, videojuegoRepository.contarMasVendidos());
    }

    // --- MÉTODOS LIMIT (PARA INICIO) ---
    public List<Videojuego> obtenerNovedades(int limit) { return videojuegoRepository.obtenerNovedades(limit); }
    public List<Videojuego> obtenerPopulares(int limit) { return videojuegoRepository.obtenerPopulares(limit); }
    public List<Videojuego> obtenerMasVendidos(int limit) { return videojuegoRepository.obtenerMasVendidos(limit); }
}