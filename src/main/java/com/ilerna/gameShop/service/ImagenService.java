package com.ilerna.gameShop.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Servicio para gestionar la subida y almacenamiento de imágenes.
 * Las imágenes se guardan en uploads/images/ (accesibles desde /uploads/images/).
 */
public class ImagenService {

    private static final String UPLOAD_DIR = "uploads/images";

    /**
     * Guarda un archivo de imagen en uploads/images/ y devuelve el nombre del archivo.
     * Si el archivo está vacío, devuelve null.
     *
     * @param archivo Fichero recibido del formulario
     * @return Nombre del archivo guardado, o null si no había fichero
     * @throws IOException si hay error al guardar
     */
    public String guardarImagen(MultipartFile archivo) throws IOException {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

        // Validar que sea una imagen
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        // Obtener extensión del archivo original
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        }

        // Generar nombre único para evitar colisiones
        String nombreFinal = UUID.randomUUID().toString().substring(0, 8) + "-" + limpiarNombre(nombreOriginal);

        // Crear directorio si no existe
        Path directorioPath = Paths.get(UPLOAD_DIR);
        Files.createDirectories(directorioPath);

        // Guardar archivo
        Path rutaDestino = directorioPath.resolve(nombreFinal);
        Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

        return nombreFinal;
    }

    /**
     * Elimina una imagen del directorio de uploads.
     *
     * @param nombreArchivo nombre del archivo a eliminar
     */
    public void eliminarImagen(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isBlank()) return;
        try {
            Path ruta = Paths.get(UPLOAD_DIR).resolve(nombreArchivo);
            Files.deleteIfExists(ruta);
        } catch (IOException e) {
            System.err.println("No se pudo eliminar la imagen: " + nombreArchivo);
        }
    }

    /** Elimina caracteres problemáticos del nombre del archivo */
    private String limpiarNombre(String nombre) {
        if (nombre == null) return "imagen.jpg";
        return nombre.replaceAll("[^a-zA-Z0-9._-]", "_").toLowerCase();
    }
}

