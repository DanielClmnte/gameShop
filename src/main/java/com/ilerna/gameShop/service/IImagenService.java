package com.ilerna.gameShop.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * Interfaz del servicio de imagenes.
 * Define las operaciones de subida y eliminacion de imagenes de productos.
 */
public interface IImagenService {
    String guardarImagen(MultipartFile archivo) throws IOException;
    void eliminarImagen(String urlONombre);
}

