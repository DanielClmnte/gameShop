package com.ilerna.gameShop.model;

/**
 * Clase que representa una plataforma de videojuegos
 * Ejemplo: PS5, Xbox, PC
 */
public class Plataforma {
    private int id;
    private String nombre;
    private String descripcion;

    // Constructor sin parámetros
    public Plataforma() {
    }

    // Constructor con parámetros
    public Plataforma(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Plataforma{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}

