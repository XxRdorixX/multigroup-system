// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Configuración de conexión a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/multigroup_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Obtiene conexion a la base de datos
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver no encontrado", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);         // Crear conexión usando credenciales
    }

    // metodo main para pruebas de conexion
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ ¡Conexión exitosa!");
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
        }
    }
}