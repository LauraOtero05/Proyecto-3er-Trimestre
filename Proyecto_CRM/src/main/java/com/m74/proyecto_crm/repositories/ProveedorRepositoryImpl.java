package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.Proveedor;
import com.m74.proyecto_crm.util.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorRepositoryImpl implements  ProveedorRepository {

    @Override
    public void save(Proveedor proveedor) throws SQLException {

        Connection conn = DataBaseConnection.getConnection();
        conn.setAutoCommit(false);

        String sqlProv = "INSERT INTO proveedores (nombre, direccion, id_codigo_postal) VALUES (?, ?, ?)";
        String sqlTlf = "INSERT INTO telefonos_proveedor (id_proveedor, telefono) VALUES (?, ?)";
        String sqlEmail = "INSERT INTO emails_proveedor (id_proveedor, email) VALUES (?, ?)";

        try (PreparedStatement psProv = conn.prepareStatement(sqlProv, Statement.RETURN_GENERATED_KEYS); PreparedStatement psTlf = conn.prepareStatement(sqlTlf); PreparedStatement psEmail = conn.prepareStatement(sqlEmail)) {

            psProv.setString(1, proveedor.getNombre());
            psProv.setString(2, proveedor.getDireccion());
            psProv.setInt(3, proveedor.getIdCodigoPostal());
            psProv.executeUpdate();

            int idProveedorGenerado;
            try (ResultSet rs = psProv.getGeneratedKeys()) {
                if (rs.next()) {
                    idProveedorGenerado = rs.getInt(1);
                } else {
                    throw new SQLException("Error al obtener el ID del proveedor.");
                }
            }

            for (String telefono : proveedor.getTelefonos()) {
                psTlf.setInt(1, idProveedorGenerado);
                psTlf.setString(2, telefono);
                psTlf.addBatch();
            }
            psTlf.executeBatch();

            for (String email : proveedor.getEmails()) {
                psEmail.setInt(1, idProveedorGenerado);
                psEmail.setString(2, email);
                psEmail.addBatch();
            }
            psEmail.executeBatch();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }

    }

    @Override
    public Proveedor findById(int id) throws SQLException {

        String sql = "SELECT p.*, cp.codigo_postal " +
                "FROM proveedores p " +
                "INNER JOIN codigos_postales cp ON p.id_codigo_postal = cp.id_codigo_postal " +
                "WHERE p.id_proveedor = ?";

        String sqlCorregido = "SELECT p.*, cp.codigo_postal " +
                "FROM proveedores p " +
                "INNER JOIN codigos_postales cp ON p.id_codigo_postal = cp.id_codigo_postal " +
                "WHERE p.id_proveedor = ?";

        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sqlCorregido)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String direccion = rs.getString("direccion");
                    int idCp = rs.getInt("id_codigo_postal");
                    String cpTexto = rs.getString("codigo_postal");

                    Proveedor prov = new Proveedor(id, nombre, direccion, idCp, cpTexto);
                    cargarContactos(prov);
                    return prov;
                }
            }
        }
        return null;
    }

    @Override
    public List<Proveedor> findAll() throws SQLException {
        List<Proveedor> proveedores = new ArrayList<>();

        String sql = "SELECT p.*, cp.codigo_postal " +
                "FROM proveedores p " +
                "INNER JOIN codigos_postales cp ON p.id_codigo_postal = cp.id_codigo_postal";

        Connection conn = DataBaseConnection.getConnection();

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_proveedor");
                String nombre = rs.getString("nombre");
                String direccion = rs.getString("direccion");
                int idCp = rs.getInt("id_codigo_postal");

                String cpTexto = rs.getString("codigo_postal");

                Proveedor prov = new Proveedor(id, nombre, direccion, idCp, cpTexto);

                cargarContactos(prov);
                proveedores.add(prov);
            }
        }
        return proveedores;
    }

    @Override
    public void update(Proveedor proveedor) throws SQLException {

        Connection conn = DataBaseConnection.getConnection();

        conn.setAutoCommit(false);

        String sqlProv = "UPDATE proveedores SET nombre = ?, direccion = ?, id_codigo_postal = ? WHERE id_proveedor = ?";
        String sqlDelTlfs = "DELETE FROM telefonos_proveedor WHERE id_proveedor = ?";
        String sqlDelEmails = "DELETE FROM emails_proveedor WHERE id_proveedor = ?";
        String sqlInsTlf = "INSERT INTO telefonos_proveedor (id_proveedor, telefono) VALUES (?, ?)";
        String sqlInsEmail = "INSERT INTO emails_proveedor (id_proveedor, email) VALUES (?, ?)";

        try (PreparedStatement psProv = conn.prepareStatement(sqlProv); PreparedStatement psDelTlfs = conn.prepareStatement(sqlDelTlfs); PreparedStatement psDelEmails = conn.prepareStatement(sqlDelEmails); PreparedStatement psInsTlf = conn.prepareStatement(sqlInsTlf); PreparedStatement psInsEmail = conn.prepareStatement(sqlInsEmail)) {

            psProv.setString(1, proveedor.getNombre());
            psProv.setString(2, proveedor.getDireccion());
            psProv.setInt(3, proveedor.getIdCodigoPostal());
            psProv.setInt(4, proveedor.getIdProveedor());
            psProv.executeUpdate();

            psDelTlfs.setInt(1, proveedor.getIdProveedor());
            psDelTlfs.executeUpdate();

            for (String telefono : proveedor.getTelefonos()) {
                psInsTlf.setInt(1, proveedor.getIdProveedor());
                psInsTlf.setString(2, telefono);
                psInsTlf.addBatch();
            }
            psInsTlf.executeBatch();

            psDelEmails.setInt(1, proveedor.getIdProveedor());
            psDelEmails.executeUpdate();

            for (String email : proveedor.getEmails()) {
                psInsEmail.setInt(1, proveedor.getIdProveedor());
                psInsEmail.setString(2, email);
                psInsEmail.addBatch();
            }
            psInsEmail.executeBatch();

            conn.commit();

        } catch (SQLException e) {

            conn.rollback();
            throw e;

        } finally {

            conn.setAutoCommit(true);
        }

    }

    @Override
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM proveedores WHERE id_proveedor = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

    }

    private void cargarContactos(Proveedor proveedor) throws SQLException {
        Connection conn = DataBaseConnection.getConnection();

        String sqlTlf = "SELECT telefono FROM telefonos_proveedor WHERE id_proveedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlTlf)) {
            ps.setInt(1, proveedor.getIdProveedor());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    proveedor.addTelefono(rs.getString("telefono"));
                }
            }
        }
        String sqlEmail = "SELECT email FROM emails_proveedor WHERE id_proveedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlEmail)) {
            ps.setInt(1, proveedor.getIdProveedor());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    proveedor.addEmail(rs.getString("email"));
                }
            }
        }
    }

}
