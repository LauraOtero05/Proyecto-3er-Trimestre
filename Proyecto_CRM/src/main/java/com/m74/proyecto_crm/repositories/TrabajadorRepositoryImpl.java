package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.Trabajador;
import com.m74.proyecto_crm.enums.RolTrabajador;
import com.m74.proyecto_crm.util.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorRepositoryImpl implements  TrabajadorRepository {
    @Override
    public void save(Trabajador trabajador) throws SQLException {

        Connection conn = DataBaseConnection.getConnection();
        conn.setAutoCommit(false);

        String sqlTrabajador = "INSERT INTO trabajadores (DNI, nombre, apellido, rol, email, password_hash) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlTlf = "INSERT INTO telefonos_trabajador (DNI, telefono) VALUES (?, ?)";

        try (PreparedStatement psTrabajador = conn.prepareStatement(sqlTrabajador); PreparedStatement psTlf = conn.prepareStatement(sqlTlf)) {

            psTrabajador.setString(1, trabajador.getDni());
            psTrabajador.setString(2, trabajador.getNombre());
            psTrabajador.setString(3, trabajador.getApellido());
            psTrabajador.setString(4, trabajador.getRol().getValue());
            psTrabajador.setString(5, trabajador.getEmail());
            psTrabajador.setString(6, trabajador.getPasswordHash());
            psTrabajador.executeUpdate();

            for (String telefono : trabajador.getTelefonos()) {
                psTlf.setString(1, trabajador.getDni());
                psTlf.setString(2, telefono);
                psTlf.addBatch();
            }
            psTlf.executeBatch();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public Trabajador findByDni(String dni) throws SQLException {

        String sql = "SELECT * FROM trabajadores WHERE DNI = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    RolTrabajador rol = RolTrabajador.fromString(rs.getString("rol"));
                    String email = rs.getString("email");
                    String passwordHash = rs.getString("password_hash");

                    Trabajador trabajador = new Trabajador(dni, nombre, apellido, rol, email, passwordHash);
                    cargarTelefonos(trabajador);
                    return trabajador;
                }
            }
        }
        return null;
    }

    @Override
    public List<Trabajador> findAll() throws SQLException {

        List<Trabajador> trabajadores = new ArrayList<>();
        String sql = "SELECT * FROM trabajadores";
        Connection conn = DataBaseConnection.getConnection();

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String dni = rs.getString("DNI");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                RolTrabajador rol = RolTrabajador.fromString(rs.getString("rol"));
                String email = rs.getString("email");
                String passwordHash = rs.getString("password_hash");

                Trabajador trabajador = new Trabajador(dni, nombre, apellido, rol, email, passwordHash);
                cargarTelefonos(trabajador);
                trabajadores.add(trabajador);
            }
        }
        return trabajadores;
    }

    @Override
    public void update(Trabajador trabajador) throws SQLException {

        Connection conn = DataBaseConnection.getConnection();
        conn.setAutoCommit(false);

        String sqlTrabajador = "UPDATE trabajadores SET nombre = ?, apellido = ?, rol = ?, email = ?, password_hash = ? WHERE DNI = ?";
        String sqlDelTlfs = "DELETE FROM telefonos_trabajador WHERE DNI = ?";
        String sqlInsTlf = "INSERT INTO telefonos_trabajador (DNI, telefono) VALUES (?, ?)";

        try (PreparedStatement psTrabajador = conn.prepareStatement(sqlTrabajador); PreparedStatement psDelTlfs = conn.prepareStatement(sqlDelTlfs); PreparedStatement psInsTlf = conn.prepareStatement(sqlInsTlf)) {

            psTrabajador.setString(1, trabajador.getNombre());
            psTrabajador.setString(2, trabajador.getApellido());
            psTrabajador.setString(3, trabajador.getRol().getValue());
            psTrabajador.setString(4, trabajador.getEmail());
            psTrabajador.setString(5, trabajador.getPasswordHash());
            psTrabajador.setString(6, trabajador.getDni());
            psTrabajador.executeUpdate();

            psDelTlfs.setString(1, trabajador.getDni());
            psDelTlfs.executeUpdate();

            for (String telefono : trabajador.getTelefonos()) {
                psInsTlf.setString(1, trabajador.getDni());
                psInsTlf.setString(2, telefono);
                psInsTlf.addBatch();
            }
            psInsTlf.executeBatch();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public void delete(String dni) throws SQLException {

        String sql = "DELETE FROM trabajadores WHERE DNI = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            ps.executeUpdate();
        }
    }

    private void cargarTelefonos(Trabajador trabajador) throws SQLException {
        Connection conn = DataBaseConnection.getConnection();

        String sqlTlf = "SELECT telefono FROM telefonos_trabajador WHERE DNI = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlTlf)) {
            ps.setString(1, trabajador.getDni());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trabajador.addTelefono(rs.getString("telefono"));
                }
            }
        }
    }
}
