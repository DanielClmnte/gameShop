package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Plataforma;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio de plataformas.
 * Define las operaciones CRUD contra la tabla 'plataformas'.
 */
public interface IPlataformaRepository {
    List<Plataforma> obtenerTodas();
    Optional<Plataforma> obtenerPorId(int id);
    Optional<Plataforma> obtenerPorNombre(String nombre);
    void guardar(Plataforma plataforma);
    void actualizar(Plataforma plataforma);
    void eliminarPorId(int id);
}

