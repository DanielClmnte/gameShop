package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.Videojuego;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repositorio para gestionar videojuegos
 * Simula consultas a la base de datos (sin JPA por ahora)
 */
public class VideojuegoRepository {
    
    // Simulamos una lista de videojuegos en memoria
    private static List<Videojuego> videojuegos = new ArrayList<>();
    
    static {
        // Datos iniciales de prueba
        
        // PlayStation 5
        Videojuego vj1 = new Videojuego(1, "Elden Ring", "Juego de rol de acción épico en un mundo abierto", 
                                       49.99, 15, 1, "elden-ring.jpg");
        vj1.setDesarrollador("FromSoftware");
        vj1.setFechaLanzamiento(LocalDate.of(2022, 2, 25));
        vj1.setCalificacion(4.8);
        videojuegos.add(vj1);
        
        Videojuego vj2 = new Videojuego(2, "Final Fantasy XVI", "Épica conclusión de la saga Final Fantasy", 
                                       59.99, 10, 1, "ff16.jpg");
        vj2.setDesarrollador("Square Enix");
        vj2.setFechaLanzamiento(LocalDate.of(2023, 6, 22));
        vj2.setCalificacion(4.6);
        videojuegos.add(vj2);
        
        Videojuego vj3 = new Videojuego(3, "Spider-Man 2", "Aventura de Spider-Man en Nueva York", 
                                       69.99, 20, 1, "spiderman2.jpg");
        vj3.setDesarrollador("Insomniac Games");
        vj3.setFechaLanzamiento(LocalDate.of(2023, 10, 20));
        vj3.setCalificacion(4.7);
        videojuegos.add(vj3);
        
        // Xbox
        Videojuego vj4 = new Videojuego(4, "Halo Infinite", "Nueva entrega de la icónica saga Halo", 
                                       59.99, 12, 2, "halo-infinite.jpg");
        vj4.setDesarrollador("Bungie");
        vj4.setFechaLanzamiento(LocalDate.of(2021, 12, 8));
        vj4.setCalificacion(4.4);
        videojuegos.add(vj4);
        
        Videojuego vj5 = new Videojuego(5, "Starfield", "RPG de ciencia ficción de Bethesda", 
                                       69.99, 8, 2, "starfield.jpg");
        vj5.setDesarrollador("Bethesda Game Studios");
        vj5.setFechaLanzamiento(LocalDate.of(2023, 9, 6));
        vj5.setCalificacion(4.5);
        videojuegos.add(vj5);
        
        // PC
        Videojuego vj6 = new Videojuego(6, "Baldur's Gate 3", "RPG de fantasía épico", 
                                       59.99, 25, 3, "baldurs-gate3.jpg");
        vj6.setDesarrollador("Larian Studios");
        vj6.setFechaLanzamiento(LocalDate.of(2023, 8, 3));
        vj6.setCalificacion(4.9);
        videojuegos.add(vj6);
        
        Videojuego vj7 = new Videojuego(7, "Cyberpunk 2077", "RPG de ciencia ficción en una megaciudad", 
                                       39.99, 18, 3, "cyberpunk2077.jpg");
        vj7.setDesarrollador("CD Projekt Red");
        vj7.setFechaLanzamiento(LocalDate.of(2020, 12, 10));
        vj7.setCalificacion(4.2);
        videojuegos.add(vj7);
        
        Videojuego vj8 = new Videojuego(8, "The Witcher 3", "Aventura de fantasía con Geralt de Rivia", 
                                       29.99, 30, 3, "witcher3.jpg");
        vj8.setDesarrollador("CD Projekt Red");
        vj8.setFechaLanzamiento(LocalDate.of(2015, 5, 19));
        vj8.setCalificacion(4.8);
        videojuegos.add(vj8);
    }
    
    /**
     * Obtener todos los videojuegos
     */
    public List<Videojuego> obtenerTodos() {
        return new ArrayList<>(videojuegos);
    }
    
    /**
     * Obtener videojuegos por plataforma
     */
    public List<Videojuego> obtenerPorPlataforma(int plataformaId) {
        return videojuegos.stream()
                .filter(v -> v.getPlataformaId() == plataformaId)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener un videojuego por ID
     */
    public Optional<Videojuego> obtenerPorId(int id) {
        return videojuegos.stream()
                .filter(v -> v.getId() == id)
                .findFirst();
    }
    
    /**
     * Buscar videojuegos por título (búsqueda parcial)
     */
    public List<Videojuego> buscarPorTitulo(String titulo) {
        return videojuegos.stream()
                .filter(v -> v.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener videojuegos disponibles (en stock)
     */
    public List<Videojuego> obtenerDisponibles() {
        return videojuegos.stream()
                .filter(Videojuego::isDisponible)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener videojuegos ordenados por calificación (de mayor a menor)
     */
    public List<Videojuego> obtenerPorCalificacion() {
        return videojuegos.stream()
                .sorted((v1, v2) -> Double.compare(v2.getCalificacion(), v1.getCalificacion()))
                .collect(Collectors.toList());
    }
    
    /**
     * Guardar un nuevo videojuego
     */
    public void guardar(Videojuego videojuego) {
        if (!videojuegos.contains(videojuego)) {
            videojuegos.add(videojuego);
        }
    }
    
    /**
     * Actualizar un videojuego existente
     */
    public void actualizar(Videojuego videojuego) {
        videojuegos.stream()
                .filter(v -> v.getId() == videojuego.getId())
                .findFirst()
                .ifPresent(v -> {
                    v.setTitulo(videojuego.getTitulo());
                    v.setDescripcion(videojuego.getDescripcion());
                    v.setPrecio(videojuego.getPrecio());
                    v.setStock(videojuego.getStock());
                    v.setImagen(videojuego.getImagen());
                });
    }
    
    /**
     * Eliminar un videojuego por ID
     */
    public void eliminarPorId(int id) {
        videojuegos.removeIf(v -> v.getId() == id);
    }
}

