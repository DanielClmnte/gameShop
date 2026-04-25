package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.MetodoPago;
import com.ilerna.gameShop.repository.MetodoPagoRepository;
import java.util.List;
import java.util.Optional;

public class MetodoPagoService implements IMetodoPagoService {

    private final MetodoPagoRepository repo;

    public MetodoPagoService() { this.repo = new MetodoPagoRepository(); }

    public List<MetodoPago> obtenerPorUsuario(int usuarioId) {
        return repo.obtenerPorUsuario(usuarioId);
    }

    public Optional<MetodoPago> obtenerPorId(int id) {
        return repo.obtenerPorId(id);
    }

    /**
     * Guardar nueva tarjeta a partir del número completo (sólo almacena los últimos 4).
     */
    public String guardar(int usuarioId, String alias, String tipo, String numeroCompleto,
                          String titular, String mes, String anio, boolean esPrincipal) {
        String err = validar(alias, tipo, numeroCompleto, titular, mes, anio);
        if (err != null) return err;

        // Si es el primero, hacerlo principal automáticamente
        if (repo.obtenerPorUsuario(usuarioId).isEmpty()) esPrincipal = true;

        String ultimos4 = numeroCompleto.replaceAll("[^0-9]", "");
        ultimos4 = ultimos4.substring(ultimos4.length() - 4);

        MetodoPago m = new MetodoPago(0, usuarioId, alias.trim(), tipo.toUpperCase(),
                ultimos4, titular.trim().toUpperCase(), mes, anio, esPrincipal);
        repo.guardar(m);
        return null;
    }

    public String actualizar(int id, int usuarioId, String alias, String tipo,
                              String titular, String mes, String anio, boolean esPrincipal) {
        if (alias == null || alias.isBlank()) return "El alias es obligatorio.";
        if (titular == null || titular.isBlank()) return "El titular es obligatorio.";
        if (mes == null || !mes.matches("0[1-9]|1[0-2]")) return "Mes de expiración inválido.";
        if (anio == null || !anio.matches("[0-9]{4}")) return "Año de expiración inválido.";

        Optional<MetodoPago> opt = repo.obtenerPorId(id);
        if (opt.isEmpty() || opt.get().getUsuarioId() != usuarioId) return "Tarjeta no encontrada.";

        MetodoPago m = opt.get();
        m.setAlias(alias.trim());
        m.setTipo(tipo.toUpperCase());
        m.setTitular(titular.trim().toUpperCase());
        m.setMesExpiracion(mes);
        m.setAnioExpiracion(anio);
        m.setEsPrincipal(esPrincipal);
        repo.actualizar(m);
        return null;
    }

    public void eliminar(int id, int usuarioId) {
        repo.eliminar(id, usuarioId);
    }

    // Admin
    public List<MetodoPago> obtenerTodos() { return repo.obtenerTodos(); }
    public void eliminarAdmin(int id)      { repo.eliminarAdmin(id); }

    private String validar(String alias, String tipo, String numero, String titular, String mes, String anio) {
        if (alias == null || alias.isBlank()) return "El alias es obligatorio.";
        if (titular == null || titular.isBlank()) return "El titular es obligatorio.";
        if (numero == null || numero.replaceAll("[^0-9]", "").length() < 16)
            return "El número de tarjeta debe tener 16 dígitos.";
        if (mes == null || !mes.matches("0[1-9]|1[0-2]")) return "Mes de expiración inválido.";
        if (anio == null || !anio.matches("[0-9]{4}")) return "Año de expiración inválido.";
        return null;
    }
}

