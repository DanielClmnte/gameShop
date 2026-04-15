package com.ilerna.gameShop.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase Singleton para gestionar la conexión a la base de datos MySQL.
 * Usa MAMP (puerto 3306) con la base de datos gameshop_db.
 */
public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/gameshop_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Conexion instancia;
    private Connection connection;

    // Constructor privado para evitar la creación externa de instancias
    private Conexion() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión a la base de datos establecida.");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos.");
            e.printStackTrace();
        }
    }

    // Método para obtener la instancia del Singleton
    public static synchronized Conexion getInstancia() {
        if (instancia == null || instancia.connection == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    // Método para obtener la conexión activa
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Método para cerrar la conexión manualmente
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión.");
            e.printStackTrace();
        }
    }
}


