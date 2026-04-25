package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Plataforma;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de plataformas.
 * Define la logica de negocio para la gestion de plataformas.
 */
public interface IPlataformaService {
    List<Plataforma> obtenerTodas();
    Optional<Plataforma> obtenerPorId(int id);
    Optional<Plataforma> obtenerPorNombre(String nombre);
    void crearPlataforma(Plataforma plataforma);
    void actualizarPlataforma(Plataforma plataforma);
    void eliminarPlataforma(int id);
}

