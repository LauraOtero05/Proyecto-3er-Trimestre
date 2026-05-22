package com.m74.proyecto_crm.util;

import com.m74.proyecto_crm.repositories.AlbumRepositoryImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneroHelper {

    private static final AlbumRepositoryImpl repo = new AlbumRepositoryImpl();

    public static int solicitarORegistrarGenero(String criterio) {
        try {
            int idGen = -1;
            String nombreGenero = "";

            if (criterio.matches("\\d+")) {
                idGen = Integer.parseInt(criterio);
                if (repo.existeGenero(idGen)) {
                    return idGen;
                }

                System.out.println("\nEl ID de género '" + idGen + "' no existe en el sistema.");
                String respuesta = InputHelper.readString("¿Deseas registrar un NUEVO género musical con otro nombre? (S/N): ").toUpperCase().trim();
                if (respuesta.equals("S")) {
                    nombreGenero = InputHelper.readString("Introduce el NOMBRE del nuevo género musical: ");
                } else {
                    return -1;
                }
            } else {
                nombreGenero = criterio.trim();
                idGen = buscarIdGeneroPorNombre(nombreGenero);
                if (idGen != -1) {
                    return idGen;
                }

                System.out.println("\nEl género '" + nombreGenero + "' no está registrado.");
                String respuesta = InputHelper.readString("¿Quieres registrar '" + nombreGenero + "' como un nuevo género musical? (S/N): ").toUpperCase().trim();
                if (!respuesta.equals("S")) {
                    return -1;
                }
            }

            System.out.println("Registrando el nuevo género '" + nombreGenero + "' en la base de datos...");
            idGen = insertarGeneroNuevo(nombreGenero);
            return idGen;

        } catch (SQLException e) {
            System.err.println("Error en el asistente de géneros: " + e.getMessage());
            return -1;
        }
    }

    private static int buscarIdGeneroPorNombre(String nombre) throws SQLException {
        String sql = "SELECT id_genero FROM generos_musicales WHERE UPPER(genero) = ?";
        Connection conn = DataBaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_genero");
            }
        }
        return -1;
    }

    private static int insertarGeneroNuevo(String nombre) throws SQLException {
        String sql = "INSERT INTO generos_musicales (genero) VALUES (?)";
        Connection conn = DataBaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public static void mostrarGenerosDisponibles() {
        String sql = "SELECT id_genero, genero FROM generos_musicales";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            System.out.println("Géneros musicales disponibles:");
            System.out.println("----------------------------------------");
            boolean tieneDatos = false;
            while (rs.next()) {
                tieneDatos = true;
                System.out.println("ID: " + rs.getInt("id_genero") + " | " + rs.getString("genero"));
            }
            if (!tieneDatos) {
                System.out.println("[No hay géneros musicales registrados aún]");
            }
            System.out.println("----------------------------------------");
        } catch (SQLException e) {
            System.err.println("Error al listar los géneros: " + e.getMessage());
        }
    }
}

