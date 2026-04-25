package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Plataforma;
import com.ilerna.gameShop.repository.PlataformaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar plataformas
 * Contiene la lógica de negocio relacionada con plataformas
 */
public class PlataformaService implements IPlataformaService {
    
    private PlataformaRepository plataformaRepository;
    
    // Constructor
    public PlataformaService() {
        this.plataformaRepository = new PlataformaRepository();
    }
    
    /**
     * Obtener todas las plataformas
     */
    public List<Plataforma> obtenerTodas() {
        return plataformaRepository.obtenerTodas();
    }
    
    /**
     * Obtener una plataforma por ID
     */
    public Optional<Plataforma> obtenerPorId(int id) {
        return plataformaRepository.obtenerPorId(id);
    }
    
    /**
     * Obtener una plataforma por nombre
     */
    public Optional<Plataforma> obtenerPorNombre(String nombre) {
        return plataformaRepository.obtenerPorNombre(nombre);
    }
    
    /**
     * Crear una nueva plataforma
     */
    public void crearPlataforma(Plataforma plataforma) {
        if (plataforma.getNombre() != null && !plataforma.getNombre().isEmpty()) {
            plataformaRepository.guardar(plataforma);
        }
    }
    
    /**
     * Actualizar una plataforma existente
     */
    public void actualizarPlataforma(Plataforma plataforma) {
        plataformaRepository.actualizar(plataforma);
    }
    
    /**
     * Eliminar una plataforma por ID
     */
    public void eliminarPlataforma(int id) {
        plataformaRepository.eliminarPorId(id);
    }
}

