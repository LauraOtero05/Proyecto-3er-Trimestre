package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.DetallePedido;
import com.m74.proyecto_crm.services.DetallePedidoService;

import java.util.List;

public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService = new DetallePedidoService();

    public List<DetallePedido> findByIdPedido(int idPedido) {
        return detallePedidoService.findByIdPedido(idPedido);
    }

    public int save(DetallePedido detalle) {
        return detallePedidoService.save(detalle);
    }

    public void update(DetallePedido detalle) {
        detallePedidoService.update(detalle);
    }

    public void deleteById(int id) {
        detallePedidoService.deleteById(id);
    }

}
