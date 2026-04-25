package com.ilerna.gameShop.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Representa un pedido realizado por un usuario.
 * Se persiste en las tablas 'pedidos' y 'detalle_pedidos' de gameshop_db.
 */
public class Pedido {

    private int id;
    private int numeroPedido;
    private int usuarioId;

    // Datos de envío capturados en el momento del pago
    private String nombreCompleto;
    private String email;
    private String direccion;
    private String ciudad;
    private String codigoPostal;

    // Snapshot del carrito en el momento de la compra
    private List<CarritoItem> items;

    private double subtotal;
    private double impuestos;
    private double totalConImpuestos;

    private LocalDate fechaPedido;
    private LocalDate fechaEntregaEstimada;
    private String estado; // PROCESANDO | ENVIADO | ENTREGADO

    public Pedido() {}

    public Pedido(int id, int numeroPedido, int usuarioId,
                  String nombreCompleto, String email,
                  String direccion, String ciudad, String codigoPostal,
                  List<CarritoItem> items,
                  double subtotal, double impuestos, double totalConImpuestos) {
        this.id = id;
        this.numeroPedido = numeroPedido;
        this.usuarioId = usuarioId;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.items = items;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.totalConImpuestos = totalConImpuestos;
        this.fechaPedido = LocalDate.now();
        this.fechaEntregaEstimada = LocalDate.now().plusDays(3);
        this.estado = "PROCESANDO";
    }

    // ── Getters y Setters ──

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(int numeroPedido) { this.numeroPedido = numeroPedido; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public List<CarritoItem> getItems() { return items; }
    public void setItems(List<CarritoItem> items) { this.items = items; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getImpuestos() { return impuestos; }
    public void setImpuestos(double impuestos) { this.impuestos = impuestos; }

    public double getTotalConImpuestos() { return totalConImpuestos; }
    public void setTotalConImpuestos(double totalConImpuestos) { this.totalConImpuestos = totalConImpuestos; }

    public LocalDate getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDate fechaPedido) { this.fechaPedido = fechaPedido; }

    public LocalDate getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

