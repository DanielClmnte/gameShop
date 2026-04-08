package com.ilerna.gameShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class gameShopApplication {
    /**
     * Proyecto: Tienda de videojuegos
     * @author Daniel Clemente Gomez
     */
    /*

      Enlace: http://localhost:8080/productos
      Probar el controlador: http://localhost:8080/prueba
      Probar el manejo de errores: http://localhost:8080/pruebaError
     */
    public static void main(String[] args) {
        SpringApplication.run(gameShopApplication.class, args);
    }
}
