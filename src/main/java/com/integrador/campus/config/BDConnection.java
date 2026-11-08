package com.integrador.campus.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class BDConnection {

    // Carga una sola vez las propiedades externas desde resources/config.properties.
    private static final Properties CONFIG = cargarConfiguracion();

    // Valores usados por JDBC para abrir la conexion a MySQL.
    private static final String URL = obtenerPropiedad("db.url");
    private static final String USER = obtenerPropiedad("db.user");
    private static final String PASSWORD = obtenerPropiedad("db.password");

    static {
        try {
            // Registra el controlador MySQL Connector antes de crear conexiones.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
        }
    }

    // Devuelve una conexion nueva para cada operacion DAO que la solicite.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static Properties cargarConfiguracion() {
        Properties properties = new Properties();

        try (InputStream input = BDConnection.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException(
                        "No se encontro src/main/resources/config.properties"
                );
            }

            // Lee las claves URL, usuario y contraseña desde el archivo externo.
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No fue posible leer config.properties", e
            );
        }
    }

    private static String obtenerPropiedad(String clave) {
        String valor = CONFIG.getProperty(clave);

        // Detiene la carga si falta una propiedad necesaria para conectarse.
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Falta la propiedad '" + clave + "' en config.properties"
            );
        }

        return valor.trim();
    }
}