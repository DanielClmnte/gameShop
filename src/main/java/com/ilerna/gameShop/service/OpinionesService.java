package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Opiniones;
import com.ilerna.gameShop.repository.OpinionesRepository;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar opiniones de usuarios
 * Contiene la lógica de negocio relacionada con reseñas
 */
public class OpinionesService {
    
    private OpinionesRepository opinionesRepository;
    
    // Constructor
    public OpinionesService() {
        this.opinionesRepository = new OpinionesRepository();
    }
    
    /**
     * Obtener todas las opiniones
     */
    public List<Opiniones> obtenerTodas() {
        return opinionesRepository.obtenerTodas();
    }
    
    /**
     * Obtener opiniones de un videojuego
     */
    public List<Opiniones> obtenerPorVideojuego(int videojuegoId) {
        return opinionesRepository.obtenerPorVideojuego(videojuegoId);
    }
    
    /**
     * Obtener opiniones de un usuario
     */
    public List<Opiniones> obtenerPorUsuario(int usuarioId) {
        return opinionesRepository.obtenerPorUsuario(usuarioId);
    }
    
    /**
     * Obtener una opinión por ID
     */
    public Optional<Opiniones> obtenerPorId(int id) {
        return opinionesRepository.obtenerPorId(id);
    }
    
    /**
     * Obtener calificación promedio de un videojuego
     */
    public double obtenerCalificacionPromedio(int videojuegoId) {
        return opinionesRepository.obtenerCalificacionPromedio(videojuegoId);
    }
    
    /**
     * Crear una nueva opinión
     */
    public void crearOpinion(Opiniones opinion) {
        if (opinion.getComentario() != null && !opinion.getComentario().isEmpty()) {
            opinionesRepository.guardar(opinion);
        }
    }
    
    /**
     * Actualizar una opinión existente
     */
    public void actualizarOpinion(Opiniones opinion) {
        opinionesRepository.actualizar(opinion);
    }
    
    /**
     * Eliminar una opinión por ID
     */
    public void eliminarOpinion(int id) {
        opinionesRepository.eliminarPorId(id);
    }
}

