package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.DetallePedido;
import com.m74.proyecto_crm.entities.Pedido;
import com.m74.proyecto_crm.enums.EstadoPedido;
import com.m74.proyecto_crm.util.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepositoryImpl implements  PedidoRepository {

    @Override
    public Pedido findById(int id) {

        String sql = "SELECT * FROM pedidos WHERE id_pedido = ?";

        try (PreparedStatement ps = DataBaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar pedido: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Pedido> findAll() {
        String sql = "SELECT * FROM pedidos";
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement ps = DataBaseConnection.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pedidos.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pedidos: " + e.getMessage());
        }
        return pedidos;
    }

    @Override
    public int save(Pedido pedido) {
        Connection con = DataBaseConnection.getConnection();
        try {
            con.setAutoCommit(false);

            String sql = "INSERT INTO pedidos (fecha, estado, importe_total, id_cliente, DNI_trabajador) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, Date.valueOf(pedido.getFecha()));
                ps.setString(2, pedido.getEstado().getValue());
                ps.setDouble(3, pedido.getImporteTotal());
                ps.setInt(4, pedido.getIdCliente());

                if (pedido.getDniTrabajador() != null) {
                    ps.setString(5, pedido.getDniTrabajador());
                } else {
                    ps.setNull(5, Types.VARCHAR);
                }

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (!rs.next()) throw new SQLException("No se obtuvo id para el pedido.");

                int idPedido = rs.getInt(1);

                DetallePedidoRepositoryImpl detallePedidoRepository = new DetallePedidoRepositoryImpl();
                AlbumRepositoryImpl albumRepository = new AlbumRepositoryImpl();

                for (DetallePedido detalle : pedido.getDetalles()) {
                    detalle.setIdPedido(idPedido);
                    int idDetalle = detallePedidoRepository.save(detalle);
                    if (idDetalle == -1) throw new SQLException("Error al guardar detalle.");
                    albumRepository.updateStock(detalle.getIdAlbum(), -detalle.getCantidad());
                }

                con.commit();
                return idPedido;
            }

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println("Error al guardar pedido: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) {
                System.out.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
        return -1;
    }

    @Override
    public void update(Pedido pedido) {
        String sql = "UPDATE pedidos SET fecha = ?, estado = ?, importe_total = ?, id_cliente = ?, DNI_trabajador = ? WHERE id_pedido = ?";

        try (PreparedStatement ps = DataBaseConnection.getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(pedido.getFecha()));
            ps.setString(2, pedido.getEstado().getValue());
            ps.setDouble(3, pedido.getImporteTotal());
            ps.setInt(4, pedido.getIdCliente());

            if (pedido.getDniTrabajador() != null) {
                ps.setString(5, pedido.getDniTrabajador());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            ps.setInt(6, pedido.getIdPedido());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar pedido: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        Connection con = DataBaseConnection.getConnection();
        try {
            con.setAutoCommit(false);

            DetallePedidoRepositoryImpl detallePedidoRepository = new DetallePedidoRepositoryImpl();
            AlbumRepositoryImpl albumRepository = new AlbumRepositoryImpl();

            List<DetallePedido> detalles = detallePedidoRepository.findByIdPedido(id);
            for (DetallePedido detalle : detalles) {
                albumRepository.updateStock(detalle.getIdAlbum(), detalle.getCantidad());
            }

            String sql = "DELETE FROM pedidos WHERE id_pedido = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            con.commit();

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println("Error al eliminar pedido: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) {
                System.out.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
    }

    private Pedido mapRow(ResultSet rs) throws SQLException {

        String dniTrabajador = rs.getString("DNI_trabajador");
        if (rs.wasNull()) dniTrabajador = null;

        return new Pedido(
                rs.getInt("id_pedido"),
                rs.getDate("fecha").toLocalDate(),
                EstadoPedido.fromString(rs.getString("estado")),
                rs.getDouble("importe_total"),
                rs.getInt("id_cliente"),
                dniTrabajador
        );
    }
}
