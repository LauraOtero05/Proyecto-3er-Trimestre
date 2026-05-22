package com.m74.proyecto_crm.services;

import com.m74.proyecto_crm.entities.DetallePedido;
import com.m74.proyecto_crm.entities.Pedido;
import com.m74.proyecto_crm.repositories.PedidoRepository;
import com.m74.proyecto_crm.repositories.PedidoRepositoryImpl;
import com.m74.proyecto_crm.util.DataBaseConnection;

import java.sql.*;
import java.util.List;

public class PedidoService {

    private final PedidoRepository pedidoRepository = new PedidoRepositoryImpl();
    private final DetallePedidoService detallePedidoService = new DetallePedidoService();

    public Pedido findById(int id) {
        Pedido pedido = pedidoRepository.findById(id);

        if (pedido != null) {
            List<DetallePedido> detalles = detallePedidoService.findByIdPedido(id);
            pedido.setDetalles(detalles);
        }

        return pedido;
    }

    public List<Pedido> findAll() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        for (Pedido pedido : pedidos) {
            List<DetallePedido> detalles = detallePedidoService.findByIdPedido(pedido.getIdPedido());
            pedido.setDetalles(detalles);
        }

        return pedidos;
    }

    public void save(Pedido pedido) {
        Connection con = DataBaseConnection.getConnection();
        try {
            con.setAutoCommit(false);

            int idPedido = pedidoRepository.save(pedido);

            if (idPedido == -1) {
                con.rollback();
                System.out.println("Error al guardar el pedido.");
                return;
            }

            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setIdPedido(idPedido);
                int idDetalle = detallePedidoService.save(detalle);

                if (idDetalle == -1) {
                    con.rollback();
                    System.out.println("Error al guardar un detalle. Operación cancelada.");
                    return;
                }
            }

            con.commit();

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println("Error en la transacción: " + e.getMessage());
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException e) {
                System.out.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
    }

    public void update(Pedido pedido) {
        pedidoRepository.update(pedido);
    }

    public void deleteById(int id) {
        pedidoRepository.deleteById(id);
    }

}
