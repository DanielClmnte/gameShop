package com.ilerna.gameShop.model;

import java.time.LocalDate;

/**
 * Clase que representa un juego en la lista de deseados/favoritos.
 * Incluye campos transient para mostrar info del videojuego en la vista.
 */
public class Favorito {
    private int id;
    private int usuarioId;
    private int videojuegoId;
    private LocalDate fechaAgregado;

    // Campos auxiliares para la vista (traídos via JOIN)
    private String tituloVideojuego;
    private String imagenVideojuego;
    private double precioVideojuego;
    private boolean disponibleVideojuego;

    public Favorito() {}

    public Favorito(int usuarioId, int videojuegoId) {
        this.usuarioId = usuarioId;
        this.videojuegoId = videojuegoId;
        this.fechaAgregado = LocalDate.now();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getVideojuegoId() { return videojuegoId; }
    public void setVideojuegoId(int videojuegoId) { this.videojuegoId = videojuegoId; }

    public LocalDate getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(LocalDate fechaAgregado) { this.fechaAgregado = fechaAgregado; }

    public String getTituloVideojuego() { return tituloVideojuego; }
    public void setTituloVideojuego(String tituloVideojuego) { this.tituloVideojuego = tituloVideojuego; }

    public String getImagenVideojuego() { return imagenVideojuego; }
    public void setImagenVideojuego(String imagenVideojuego) { this.imagenVideojuego = imagenVideojuego; }

    public double getPrecioVideojuego() { return precioVideojuego; }
    public void setPrecioVideojuego(double precioVideojuego) { this.precioVideojuego = precioVideojuego; }

    public boolean isDisponibleVideojuego() { return disponibleVideojuego; }
    public void setDisponibleVideojuego(boolean disponibleVideojuego) { this.disponibleVideojuego = disponibleVideojuego; }
}

