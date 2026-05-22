package com.m74.proyecto_crm.services;

import com.m74.proyecto_crm.entities.DetallePedido;
import com.m74.proyecto_crm.repositories.DetallePedidoRepository;
import com.m74.proyecto_crm.repositories.DetallePedidoRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository = new DetallePedidoRepositoryImpl();
    private final AlbumService albumService = new AlbumService();

    public DetallePedido findById(int id) {
        return detallePedidoRepository.findById(id);
    }

    public List<DetallePedido> findByIdPedido(int idPedido) {
        return detallePedidoRepository.findByIdPedido(idPedido);
    }

    public int save(DetallePedido detalle) {
        try {
            albumService.updateStock(detalle.getIdAlbum(), -detalle.getCantidad());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        return detallePedidoRepository.save(detalle);
    }

    public void update(DetallePedido detalleNuevo) {
        DetallePedido detalleAntiguo = detallePedidoRepository.findById(detalleNuevo.getIdDetalle());

        if (detalleAntiguo == null) {
            System.out.println("Detalle no encontrado.");
            return;
        }

        int diferencia = detalleAntiguo.getCantidad() - detalleNuevo.getCantidad();

        try {
            albumService.updateStock(detalleNuevo.getIdAlbum(), diferencia);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        detallePedidoRepository.update(detalleNuevo);
    }

    public void deleteById(int id) {
        DetallePedido detalle = detallePedidoRepository.findById(id);

        if (detalle == null) {
            System.out.println("Detalle no encontrado.");
            return;
        }

        try {
            albumService.updateStock(detalle.getIdAlbum(), detalle.getCantidad());
        } catch (SQLException e) {
            System.out.println("Error al devolver stock: " + e.getMessage());
            return;
        }

        detallePedidoRepository.deleteById(id);
    }
}