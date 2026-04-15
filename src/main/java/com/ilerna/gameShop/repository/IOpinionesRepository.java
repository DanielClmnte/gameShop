package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Opiniones;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio de opiniones.
 * Define las operaciones CRUD contra la tabla 'opiniones'.
 */
public interface IOpinionesRepository {
    List<Opiniones> obtenerTodas();
    List<Opiniones> obtenerPorVideojuego(int videojuegoId);
    List<Opiniones> obtenerPorUsuario(int usuarioId);
    Optional<Opiniones> obtenerPorId(int id);
    double obtenerCalificacionPromedio(int videojuegoId);
    void guardar(Opiniones opinion);
    void actualizar(Opiniones opinion);
    void eliminarPorId(int id);
}

