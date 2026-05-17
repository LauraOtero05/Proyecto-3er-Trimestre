package com.m74.proyecto_crm.services;

import com.m74.proyecto_crm.entities.DetallePedido;
import com.m74.proyecto_crm.repositories.DetallePedidoRepository;
import com.m74.proyecto_crm.repositories.DetallePedidoRepositoryImpl;

import java.util.List;

public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository = new DetallePedidoRepositoryImpl();

    public List<DetallePedido> findByIdPedido(int idPedido) {
        return detallePedidoRepository.findByIdPedido(idPedido);
    }

    public int save(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }

    public void update(DetallePedido detalle) {
        detallePedidoRepository.update(detalle);
    }

    public void deleteById(int id) {
        detallePedidoRepository.deleteById(id);
    }
}