package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.Cliente;
import com.m74.proyecto_crm.util.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryImpl implements  ClienteRepository {
    @Override
    public void save(Cliente cliente) throws SQLException {

        Connection conn = DataBaseConnection.getConnection();
        conn.setAutoCommit(false);

        String sqlCliente = "INSERT INTO clientes (nombre, apellido, direccion, id_codigo_postal, email, password_hash) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlTlf = "INSERT INTO telefonos_cliente (id_cliente, telefono) VALUES (?, ?)";

        try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psTlf = conn.prepareStatement(sqlTlf)) {

            psCliente.setString(1, cliente.getNombre());
            psCliente.setString(2, cliente.getApellido());
            psCliente.setString(3, cliente.getDireccion());
            psCliente.setInt(4, cliente.getIdCodigoPostal());
            psCliente.setString(5, cliente.getEmail());
            psCliente.setString(6, cliente.getPasswordHash());
            psCliente.executeUpdate();

            int idClienteGenerado;
            try (ResultSet rs = psCliente.getGeneratedKeys()) {
                if (rs.next()) {
                    idClienteGenerado = rs.getInt(1);
                } else {
                    throw new SQLException("Error al obtener el ID del cliente.");
                }
            }

            for (String telefono : cliente.getTelefonos()) {
                psTlf.setInt(1, idClienteGenerado);
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
    public Cliente findById(int id) throws SQLException {

        String sql = "SELECT c.*, cp.codigo_postal " +
                "FROM clientes c " +
                "INNER JOIN codigos_postales cp ON c.id_codigo_postal = cp.id_codigo_postal " +
                "WHERE c.id_cliente = ?";

        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String direccion = rs.getString("direccion");
                    int idCp = rs.getInt("id_codigo_postal");
                    String cpTexto = rs.getString("codigo_postal");
                    String email = rs.getString("email");
                    String passwordHash = rs.getString("password_hash");

                    Cliente cliente = new Cliente(id, nombre, apellido, direccion, idCp, cpTexto, email, passwordHash);
                    cargarTelefonos(cliente);
                    return cliente;
                }
            }
        }
        return null;
    }

    @Override
    public List<Cliente> findAll() throws SQLException {

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT c.*, cp.codigo_postal " +
                "FROM clientes c " +
                "INNER JOIN codigos_postales cp ON c.id_codigo_postal = cp.id_codigo_postal";

        Connection conn = DataBaseConnection.getConnection();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String direccion = rs.getString("direccion");
                int idCp = rs.getInt("id_codigo_postal");
                String cpTexto = rs.getString("codigo_postal");
                String email = rs.getString("email");
                String passwordHash = rs.getString("password_hash");

                Cliente cliente = new Cliente(id, nombre, apellido, direccion, idCp, cpTexto, email, passwordHash);
                cargarTelefonos(cliente);
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    @Override
    public void update(Cliente cliente) throws SQLException {

        Connection conn = DataBaseConnection.getConnection();
        conn.setAutoCommit(false);

        String sqlCliente = "UPDATE clientes SET nombre = ?, apellido = ?, direccion = ?, id_codigo_postal = ?, email = ?, password_hash = ? WHERE id_cliente = ?";
        String sqlDelTlfs = "DELETE FROM telefonos_cliente WHERE id_cliente = ?";
        String sqlInsTlf = "INSERT INTO telefonos_cliente (id_cliente, telefono) VALUES (?, ?)";

        try (PreparedStatement psCliente = conn.prepareStatement(sqlCliente);
             PreparedStatement psDelTlfs = conn.prepareStatement(sqlDelTlfs);
             PreparedStatement psInsTlf = conn.prepareStatement(sqlInsTlf)) {

            psCliente.setString(1, cliente.getNombre());
            psCliente.setString(2, cliente.getApellido());
            psCliente.setString(3, cliente.getDireccion());
            psCliente.setInt(4, cliente.getIdCodigoPostal());
            psCliente.setString(5, cliente.getEmail());
            psCliente.setString(6, cliente.getPasswordHash());
            psCliente.setInt(7, cliente.getIdCliente());
            psCliente.executeUpdate();

            psDelTlfs.setInt(1, cliente.getIdCliente());
            psDelTlfs.executeUpdate();

            for (String telefono : cliente.getTelefonos()) {
                psInsTlf.setInt(1, cliente.getIdCliente());
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
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private void cargarTelefonos(Cliente cliente) throws SQLException {
        Connection conn = DataBaseConnection.getConnection();

        String sqlTlf = "SELECT telefono FROM telefonos_cliente WHERE id_cliente = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlTlf)) {
            ps.setInt(1, cliente.getIdCliente());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cliente.addTelefono(rs.getString("telefono"));
                }
            }
        }
    }
}
