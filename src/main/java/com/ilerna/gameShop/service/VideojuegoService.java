package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Videojuego;
import com.ilerna.gameShop.repository.VideojuegoRepository;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar videojuegos
 * Contiene la lógica de negocio relacionada con videojuegos
 */
public class VideojuegoService {
    
    private VideojuegoRepository videojuegoRepository;
    
    // Constructor
    public VideojuegoService() {
        this.videojuegoRepository = new VideojuegoRepository();
    }
    
    /**
     * Obtener todos los videojuegos
     */
    public List<Videojuego> obtenerTodos() {
        return videojuegoRepository.obtenerTodos();
    }
    
    /**
     * Obtener videojuegos filtrados por plataforma
     */
    public List<Videojuego> obtenerPorPlataforma(int plataformaId) {
        return videojuegoRepository.obtenerPorPlataforma(plataformaId);
    }
    
    /**
     * Obtener un videojuego por ID
     */
    public Optional<Videojuego> obtenerPorId(int id) {
        return videojuegoRepository.obtenerPorId(id);
    }
    
    /**
     * Buscar videojuegos por título
     */
    public List<Videojuego> buscarPorTitulo(String titulo) {
        return videojuegoRepository.buscarPorTitulo(titulo);
    }
    
    /**
     * Obtener videojuegos disponibles (en stock)
     */
    public List<Videojuego> obtenerDisponibles() {
        return videojuegoRepository.obtenerDisponibles();
    }
    
    /**
     * Obtener videojuegos ordenados por calificación
     */
    public List<Videojuego> obtenerPorCalificacion() {
        return videojuegoRepository.obtenerPorCalificacion();
    }
    
    /**
     * Crear un nuevo videojuego
     */
    public void crearVideojuego(Videojuego videojuego) {
        if (videojuego.getTitulo() != null && !videojuego.getTitulo().isEmpty()) {
            videojuegoRepository.guardar(videojuego);
        }
    }
    
    /**
     * Actualizar un videojuego existente
     */
    public void actualizarVideojuego(Videojuego videojuego) {
        videojuegoRepository.actualizar(videojuego);
    }
    
    /**
     * Eliminar un videojuego por ID
     */
    public void eliminarVideojuego(int id) {
        videojuegoRepository.eliminarPorId(id);
    }
    
    /**
     * Restar stock cuando se agrega al carrito
     */
    public void restarStock(int videojuegoId, int cantidad) {
        Optional<Videojuego> vj = videojuegoRepository.obtenerPorId(videojuegoId);
        if (vj.isPresent()) {
            Videojuego videojuego = vj.get();
            if (videojuego.getStock() >= cantidad) {
                videojuego.setStock(videojuego.getStock() - cantidad);
                videojuegoRepository.actualizar(videojuego);
            }
        }
    }
}

