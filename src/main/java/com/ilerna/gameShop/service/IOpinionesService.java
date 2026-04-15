package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.Opiniones;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del servicio de opiniones.
 * Define la logica de negocio para la gestion de opiniones/resenas.
 */
public interface IOpinionesService {
    List<Opiniones> obtenerTodas();
    List<Opiniones> obtenerPorVideojuego(int videojuegoId);
    List<Opiniones> obtenerPorUsuario(int usuarioId);
    Optional<Opiniones> obtenerPorId(int id);
    double obtenerCalificacionPromedio(int videojuegoId);
    void crearOpinion(Opiniones opinion);
    void actualizarOpinion(Opiniones opinion);
    void eliminarOpinion(int id);
}

