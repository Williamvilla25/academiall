package com.uniajc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor de conexión a la base de datos PostgreSQL (Neon Tech)
 * Implementa el patrón de diseño Singleton.
 */
public class ConexionBD {
    private static ConexionBD instancia;
    private Connection conexion;

    private String url;
    private String usuario;
    private String contrasena;

    private ConexionBD() {
        cargarConfiguracion();
    }

    private void cargarConfiguracion() {
        Properties props = new Properties();
        try {
            File configFile = new File("config.properties");
            if (configFile.exists()) {
                try (InputStream input = new FileInputStream(configFile)) {
                    props.load(input);
                }
            } else {
                try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                    if (input != null) {
                        props.load(input);
                    }
                }
            }

            this.url = props.getProperty("URL");
            this.usuario = props.getProperty("USER");
            this.contrasena = props.getProperty("PASSWORD");

            // Cargar el driver JDBC de PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Error al cargar la configuración de base de datos: " + e.getMessage());
        }
    }

    public static synchronized ConexionBD obtenerInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public synchronized Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            if (url == null) {
                cargarConfiguracion();
            }
            if (usuario != null && contrasena != null) {
                conexion = DriverManager.getConnection(url, usuario, contrasena);
            } else {
                conexion = DriverManager.getConnection(url);
            }
        }
        return conexion;
    }

    public synchronized void desconectar() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            } finally {
                conexion = null;
            }
        }
    }
}

