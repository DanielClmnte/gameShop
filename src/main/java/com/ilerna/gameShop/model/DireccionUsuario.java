package com.ilerna.gameShop.model;

import java.time.LocalDateTime;

/**
 * Dirección de envío guardada por un usuario.
 */
public class DireccionUsuario {

    private int id;
    private int usuarioId;
    private String alias;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    private String telefono;
    private boolean esPrincipal;
    private LocalDateTime fechaCreacion;

    public DireccionUsuario() {}

    public DireccionUsuario(int id, int usuarioId, String alias, String nombre,
                             String direccion, String ciudad, String codigoPostal,
                             String telefono, boolean esPrincipal) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.alias = alias;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.telefono = telefono;
        this.esPrincipal = esPrincipal;
    }

    // Getters & Setters
    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public int getUsuarioId()                   { return usuarioId; }
    public void setUsuarioId(int usuarioId)     { this.usuarioId = usuarioId; }
    public String getAlias()                    { return alias; }
    public void setAlias(String alias)          { this.alias = alias; }
    public String getNombre()                   { return nombre; }
    public void setNombre(String nombre)        { this.nombre = nombre; }
    public String getDireccion()                { return direccion; }
    public void setDireccion(String direccion)  { this.direccion = direccion; }
    public String getCiudad()                   { return ciudad; }
    public void setCiudad(String ciudad)        { this.ciudad = ciudad; }
    public String getCodigoPostal()             { return codigoPostal; }
    public void setCodigoPostal(String cp)      { this.codigoPostal = cp; }
    public String getTelefono()                 { return telefono; }
    public void setTelefono(String telefono)    { this.telefono = telefono; }
    public boolean isEsPrincipal()              { return esPrincipal; }
    public void setEsPrincipal(boolean p)       { this.esPrincipal = p; }
    public LocalDateTime getFechaCreacion()     { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime f){ this.fechaCreacion = f; }

    /** Resumen legible para mostrar en selects */
    public String getResumen() {
        return alias + " — " + direccion + ", " + ciudad + " " + codigoPostal;
    }
}

