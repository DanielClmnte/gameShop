package com.ilerna.gameShop.model;

import java.time.LocalDateTime;

/**
 * Método de pago (tarjeta) guardado por un usuario.
 * NUNCA se guarda el número completo — sólo los últimos 4 dígitos.
 */
public class MetodoPago {

    private int id;
    private int usuarioId;
    private String alias;
    private String tipo;            // VISA, MASTERCARD, AMEX…
    private String ultimos4;        // últimos 4 dígitos de la tarjeta
    private String titular;
    private String mesExpiracion;   // "01" - "12"
    private String anioExpiracion;  // "2026" - …
    private boolean esPrincipal;
    private LocalDateTime fechaCreacion;

    public MetodoPago() {}

    public MetodoPago(int id, int usuarioId, String alias, String tipo,
                      String ultimos4, String titular,
                      String mesExpiracion, String anioExpiracion, boolean esPrincipal) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.alias = alias;
        this.tipo = tipo;
        this.ultimos4 = ultimos4;
        this.titular = titular;
        this.mesExpiracion = mesExpiracion;
        this.anioExpiracion = anioExpiracion;
        this.esPrincipal = esPrincipal;
    }

    // Getters & Setters
    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }
    public int getUsuarioId()                       { return usuarioId; }
    public void setUsuarioId(int u)                 { this.usuarioId = u; }
    public String getAlias()                        { return alias; }
    public void setAlias(String alias)              { this.alias = alias; }
    public String getTipo()                         { return tipo; }
    public void setTipo(String tipo)                { this.tipo = tipo; }
    public String getUltimos4()                     { return ultimos4; }
    public void setUltimos4(String u)               { this.ultimos4 = u; }
    public String getTitular()                      { return titular; }
    public void setTitular(String titular)          { this.titular = titular; }
    public String getMesExpiracion()                { return mesExpiracion; }
    public void setMesExpiracion(String m)          { this.mesExpiracion = m; }
    public String getAnioExpiracion()               { return anioExpiracion; }
    public void setAnioExpiracion(String a)         { this.anioExpiracion = a; }
    public boolean isEsPrincipal()                  { return esPrincipal; }
    public void setEsPrincipal(boolean p)           { this.esPrincipal = p; }
    public LocalDateTime getFechaCreacion()         { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime f)   { this.fechaCreacion = f; }

    /** Texto para mostrar en selects del checkout */
    public String getResumen() {
        return alias + " — " + tipo + " •••• " + ultimos4 + "  " + mesExpiracion + "/" + anioExpiracion;
    }

    /** Icono según tipo de tarjeta */
    public String getIcono() {
        return switch (tipo == null ? "" : tipo.toUpperCase()) {
            case "MASTERCARD" -> "💳";
            case "AMEX"       -> "💳";
            default           -> "💳";
        };
    }
}

