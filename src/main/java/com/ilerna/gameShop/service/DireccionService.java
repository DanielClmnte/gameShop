package com.ilerna.gameShop.service;

import com.ilerna.gameShop.model.DireccionUsuario;
import com.ilerna.gameShop.repository.DireccionRepository;
import java.util.List;
import java.util.Optional;

public class DireccionService implements IDireccionService {

    private final DireccionRepository repo;

    public DireccionService() { this.repo = new DireccionRepository(); }

    @Override
    public List<DireccionUsuario> obtenerPorUsuario(int usuarioId) {
        return repo.obtenerPorUsuario(usuarioId);
    }

    @Override
    public Optional<DireccionUsuario> obtenerPorId(int id) {
        return repo.obtenerPorId(id);
    }

    @Override
    public String guardar(int usuarioId, String alias, String nombre,
                          String direccion, String ciudad, String codigoPostal,
                          String telefono, boolean esPrincipal) {
        String err = validar(alias, nombre, direccion, ciudad, codigoPostal);
        if (err != null) return err;

        // Si es la primera dirección, hacerla principal automáticamente
        if (repo.obtenerPorUsuario(usuarioId).isEmpty()) esPrincipal = true;

        repo.guardar(new DireccionUsuario(0, usuarioId,
                alias.trim(), nombre.trim(), direccion.trim(), ciudad.trim(),
                codigoPostal.trim(), telefono, esPrincipal));
        return null;
    }

    @Override
    public String actualizar(int id, int usuarioId, String alias, String nombre,
                              String direccion, String ciudad, String codigoPostal,
                              String telefono, boolean esPrincipal) {
        String err = validar(alias, nombre, direccion, ciudad, codigoPostal);
        if (err != null) return err;

        Optional<DireccionUsuario> opt = repo.obtenerPorId(id);
        if (opt.isEmpty() || opt.get().getUsuarioId() != usuarioId) return "Dirección no encontrada.";

        repo.actualizar(new DireccionUsuario(id, usuarioId,
                alias.trim(), nombre.trim(), direccion.trim(), ciudad.trim(),
                codigoPostal.trim(), telefono, esPrincipal));
        return null;
    }

    @Override
    public void eliminar(int id, int usuarioId) {
        repo.eliminar(id, usuarioId);
    }

    @Override
    public List<DireccionUsuario> obtenerTodas() { return repo.obtenerTodas(); }

    @Override
    public void eliminarAdmin(int id) { repo.eliminarAdmin(id); }

    // ── Validación compartida ──
    private String validar(String alias, String nombre, String direccion,
                            String ciudad, String codigoPostal) {
        if (alias == null || alias.isBlank())         return "El alias es obligatorio.";
        if (nombre == null || nombre.isBlank())       return "El nombre es obligatorio.";
        if (direccion == null || direccion.isBlank()) return "La dirección es obligatoria.";
        if (ciudad == null || ciudad.isBlank())       return "La ciudad es obligatoria.";
        if (codigoPostal == null || !codigoPostal.matches("[0-9]{5}"))
            return "Código postal inválido (5 dígitos).";
        return null;
    }
}
