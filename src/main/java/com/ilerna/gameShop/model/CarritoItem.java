package com.ilerna.gameShop.model;

/**
 * Clase que representa un item en el carrito de compras
 * Vincula un videojuego con una cantidad
 */
public class CarritoItem {
    private int id;
    private int videojuegoId;
    private int cantidad;
    private double precioUnitario; // Precio al momento de agregar al carrito
    private String tituloVideojuego; // Para facilitar la visualización
    private String imagenVideojuego;

    // Constructor sin parámetros
    public CarritoItem() {
    }

    // Constructor con parámetros principales
    public CarritoItem(int videojuegoId, int cantidad, double precioUnitario, 
                      String tituloVideojuego, String imagenVideojuego) {
        this.videojuegoId = videojuegoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tituloVideojuego = tituloVideojuego;
        this.imagenVideojuego = imagenVideojuego;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getTituloVideojuego() {
        return tituloVideojuego;
    }

    public void setTituloVideojuego(String tituloVideojuego) {
        this.tituloVideojuego = tituloVideojuego;
    }

    public String getImagenVideojuego() {
        return imagenVideojuego;
    }

    public void setImagenVideojuego(String imagenVideojuego) {
        this.imagenVideojuego = imagenVideojuego;
    }

    // Método para calcular el subtotal del item
    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

    @Override
    public String toString() {
        return "CarritoItem{" +
                "id=" + id +
                ", videojuegoId=" + videojuegoId +
                ", cantidad=" + cantidad +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}

