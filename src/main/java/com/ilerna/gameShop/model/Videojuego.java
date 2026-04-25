package com.ilerna.gameShop.model;

import java.time.LocalDate;

/**
 * Clase que representa un videojuego en la tienda
 * Contiene información del producto: título, precio, stock, etc.
 */
public class Videojuego {
    private int id;
    private String titulo;
    private String descripcion;
    private double precio;
    private int stock;
    private int plataformaId; // Referencia a la plataforma
    private String imagen; // Nombre del archivo de imagen
    private LocalDate fechaLanzamiento;
    private String desarrollador;
    private double calificacion; // Promedio de opiniones
    private boolean disponible;

    // Constructor sin parámetros
    public Videojuego() {
    }

    // Constructor con parámetros principales
    public Videojuego(int id, String titulo, String descripcion, double precio, 
                      int stock, int plataformaId, String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.plataformaId = plataformaId;
        this.imagen = imagen;
        this.disponible = stock > 0;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
        this.disponible = stock > 0;
    }

    public int getPlataformaId() {
        return plataformaId;
    }

    public void setPlataformaId(int plataformaId) {
        this.plataformaId = plataformaId;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Videojuego{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", disponible=" + disponible +
                '}';
    }
}

