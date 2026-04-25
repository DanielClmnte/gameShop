package com.ilerna.gameShop.repository;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton que gestiona la conexión a MySQL.
 * Las credenciales se leen desde application.properties (db.url, db.user, db.password).
 */
public class Conexion {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try (InputStream in = Conexion.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            if (in != null) {
                props.load(in);
                URL      = props.getProperty("db.url");
                USER     = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");
            }
        } catch (Exception e) {
            System.err.println("❌ No se pudo leer application.properties.");
            e.printStackTrace();
        }
    }

    private static Conexion instancia;
    private Connection connection;

    private Conexion() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión a la base de datos establecida.");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos.");
            e.printStackTrace();
        }
    }

    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Reconexión a la base de datos establecida.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

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


