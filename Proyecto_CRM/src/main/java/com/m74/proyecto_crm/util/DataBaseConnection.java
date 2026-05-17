package com.m74.proyecto_crm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static Connection connection;

    public static boolean start(int port, String user, String password) {

        try {
            String url = "jdbc:mysql://localhost:" + port + "/74_minutes";
            connection = DriverManager.getConnection(url, user, password);
            return true;
        } catch (SQLException e) {
            System.out.println("Error de conexión. ");
            return false;
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
