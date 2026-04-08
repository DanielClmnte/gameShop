package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Plataforma;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar plataformas
 * Simula consultas a la base de datos (sin JPA por ahora)
 */
public class PlataformaRepository {
    
    // Simulamos una lista de plataformas en memoria
    private static List<Plataforma> plataformas = new ArrayList<>();
    
    static {
        // Datos iniciales de prueba
        plataformas.add(new Plataforma(1, "PlayStation 5", "Consola de próxima generación de Sony"));
        plataformas.add(new Plataforma(2, "Xbox Series X", "Consola de próxima generación de Microsoft"));
        plataformas.add(new Plataforma(3, "PC", "Videojuegos para computadora"));
    }
    
    /**
     * Obtener todas las plataformas
     */
    public List<Plataforma> obtenerTodas() {
        return new ArrayList<>(plataformas);
    }
    
    /**
     * Obtener una plataforma por ID
     */
    public Optional<Plataforma> obtenerPorId(int id) {
        return plataformas.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }
    
    /**
     * Obtener una plataforma por nombre
     */
    public Optional<Plataforma> obtenerPorNombre(String nombre) {
        return plataformas.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }
    
    /**
     * Guardar una nueva plataforma
     */
    public void guardar(Plataforma plataforma) {
        if (!plataformas.contains(plataforma)) {
            plataformas.add(plataforma);
        }
    }
    
    /**
     * Actualizar una plataforma existente
     */
    public void actualizar(Plataforma plataforma) {
        plataformas.stream()
                .filter(p -> p.getId() == plataforma.getId())
                .findFirst()
                .ifPresent(p -> {
                    p.setNombre(plataforma.getNombre());
                    p.setDescripcion(plataforma.getDescripcion());
                });
    }
    
    /**
     * Eliminar una plataforma por ID
     */
    public void eliminarPorId(int id) {
        plataformas.removeIf(p -> p.getId() == id);
    }
}

