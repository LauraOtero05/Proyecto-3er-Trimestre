package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.util.DataBaseConnection;

import java.sql.*;

public class UbicationRepositoryImpl {

    public boolean existePais(String codigoPais) throws SQLException {
        String sql = "SELECT 1 FROM paises WHERE codigo_pais = ?";
        Connection conn = DataBaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigoPais.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int obtenerIdPorCodigoPostal(String cp) throws SQLException {
        String sql = "SELECT id_codigo_postal FROM codigos_postales WHERE codigo_postal = ?";
        Connection conn = DataBaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_codigo_postal");
            }
        }
        return -1;
    }

    public int crearCodigoPostal(String cp, String ciudad, String provincia, String codigoPais) throws SQLException {
        String sql = "INSERT INTO codigos_postales (codigo_postal, ciudad, provincia, codigo_pais) VALUES (?, ?, ?, ?)";
        Connection conn = DataBaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cp);
            ps.setString(2, ciudad);
            ps.setString(3, provincia);
            ps.setString(4, codigoPais.toUpperCase());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo crear el código postal.");
    }

    public void crearPais(String codigoPais, String nombrePais) throws SQLException {
        String sql = "INSERT INTO paises (codigo_pais, nombre) VALUES (?, ?)";
        Connection conn = DataBaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigoPais.toUpperCase());
            ps.setString(2, nombrePais);
            ps.executeUpdate();
        }
    }
}
