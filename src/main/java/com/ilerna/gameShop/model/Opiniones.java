package com.ilerna.gameShop.model;

import java.time.LocalDate;

/**
 * Clase que representa una opinión/reseña de un videojuego
 * Los usuarios pueden dejar comentarios y calificaciones
 */
public class Opiniones {
    private int id;
    private int videojuegoId; // ID del videojuego reseñado
    private int usuarioId; // ID del usuario que hace la reseña
    private int calificacion; // Puntuación de 1 a 5
    private String comentario; // Texto de la reseña
    private LocalDate fecha;
    private boolean verificado; // Si el usuario compró el producto

    // Constructor sin parámetros
    public Opiniones() {
    }

    // Constructor con parámetros principales
    public Opiniones(int id, int videojuegoId, int usuarioId, int calificacion, 
                     String comentario, boolean verificado) {
        this.id = id;
        this.videojuegoId = videojuegoId;
        this.usuarioId = usuarioId;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fecha = LocalDate.now();
        this.verificado = verificado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVideojuegoId() {
        return videojuegoId;
    }

    public void setVideojuegoId(int videojuegoId) {
        this.videojuegoId = videojuegoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        if (calificacion >= 1 && calificacion <= 5) {
            this.calificacion = calificacion;
        }
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    @Override
    public String toString() {
        return "Opiniones{" +
                "id=" + id +
                ", videojuegoId=" + videojuegoId +
                ", calificacion=" + calificacion +
                ", fecha=" + fecha +
                '}';
    }
}

