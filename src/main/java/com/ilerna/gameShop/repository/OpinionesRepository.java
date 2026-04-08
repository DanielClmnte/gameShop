package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Opiniones;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio para gestionar opiniones de usuarios
 * Simula consultas a la base de datos (sin JPA por ahora)
 */
public class OpinionesRepository {
    
    // Simulamos una lista de opiniones en memoria
    private static List<Opiniones> opiniones = new ArrayList<>();
    
    static {
        // Datos iniciales de prueba
        opiniones.add(new Opiniones(1, 1, 1, 5, "Juego excelente, muy recomendado", true));
        opiniones.add(new Opiniones(2, 1, 2, 4, "Muy bueno, aunque un poco largo", false));
        opiniones.add(new Opiniones(3, 2, 1, 5, "Final Fantasy siempre es buena opción", true));
        opiniones.add(new Opiniones(4, 6, 1, 5, "Baldur's Gate 3 es épico, mejora que Elden Ring", true));
    }
    
    /**
     * Obtener todas las opiniones
     */
    public List<Opiniones> obtenerTodas() {
        return new ArrayList<>(opiniones);
    }
    
    /**
     * Obtener opiniones de un videojuego específico
     */
    public List<Opiniones> obtenerPorVideojuego(int videojuegoId) {
        return opiniones.stream()
                .filter(o -> o.getVideojuegoId() == videojuegoId)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener opiniones de un usuario específico
     */
    public List<Opiniones> obtenerPorUsuario(int usuarioId) {
        return opiniones.stream()
                .filter(o -> o.getUsuarioId() == usuarioId)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener una opinión por ID
     */
    public Optional<Opiniones> obtenerPorId(int id) {
        return opiniones.stream()
                .filter(o -> o.getId() == id)
                .findFirst();
    }
    
    /**
     * Obtener calificación promedio de un videojuego
     */
    public double obtenerCalificacionPromedio(int videojuegoId) {
        return opiniones.stream()
                .filter(o -> o.getVideojuegoId() == videojuegoId)
                .mapToInt(Opiniones::getCalificacion)
                .average()
                .orElse(0.0);
    }
    
    /**
     * Guardar una nueva opinión
     */
    public void guardar(Opiniones opinion) {
        if (!opiniones.contains(opinion)) {
            opiniones.add(opinion);
        }
    }
    
    /**
     * Actualizar una opinión existente
     */
    public void actualizar(Opiniones opinion) {
        opiniones.stream()
                .filter(o -> o.getId() == opinion.getId())
                .findFirst()
                .ifPresent(o -> {
                    o.setCalificacion(opinion.getCalificacion());
                    o.setComentario(opinion.getComentario());
                });
    }
    
    /**
     * Eliminar una opinión por ID
     */
    public void eliminarPorId(int id) {
        opiniones.removeIf(o -> o.getId() == id);
    }
}

