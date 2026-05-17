package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.DetallePedido;
import com.m74.proyecto_crm.util.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoRepositoryImpl implements DetallePedidoRepository {

    @Override
    public DetallePedido findById(int id) {
        String sql = "SELECT * FROM detalles_pedido WHERE id_detalle = ?";

        try (PreparedStatement ps = DataBaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar detalle: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<DetallePedido> findByIdPedido(int idPedido) {
        String sql = "SELECT * FROM detalles_pedido WHERE id_pedido = ?";
        List<DetallePedido> detalles = new ArrayList<>();

        try (PreparedStatement ps = DataBaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                detalles.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar detalles: " + e.getMessage());
        }
        return detalles;
    }

    @Override
    public int save(DetallePedido detalle) {
        String sql = "INSERT INTO detalles_pedido (id_pedido, id_album, cantidad) VALUES (?, ?, ?)";

        try (PreparedStatement ps = DataBaseConnection.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdAlbum());
            ps.setInt(3, detalle.getCantidad());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("No se obtuvo id para el detalle.");
            }

        } catch (SQLException e) {
            System.out.println("Error al guardar detalle: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public void update(DetallePedido detalle) {
        String sql = "UPDATE detalles_pedido SET id_pedido = ?, id_album = ?, cantidad = ? WHERE id_detalle = ?";

        try (PreparedStatement ps = DataBaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdAlbum());
            ps.setInt(3, detalle.getCantidad());
            ps.setInt(4, detalle.getIdDetalle());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar detalle: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM detalles_pedido WHERE id_detalle = ?";

        try (PreparedStatement ps = DataBaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalle: " + e.getMessage());
        }
    }

    private DetallePedido mapRow(ResultSet rs) throws SQLException {
        return new DetallePedido(
                rs.getInt("id_detalle"),
                rs.getInt("id_pedido"),
                rs.getInt("id_album"),
                rs.getInt("cantidad")
        );
    }
}
