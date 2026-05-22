package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.DetallePedido;
import com.m74.proyecto_crm.entities.Pedido;
import com.m74.proyecto_crm.services.PedidoService;

import java.util.List;

public class PedidoController {

    private final PedidoService pedidoService = new PedidoService();

    public List<Pedido> findAll() {
        return pedidoService.findAll();
    }

    public Pedido findById(int id) {
        return pedidoService.findById(id);
    }

    public void save(Pedido pedido) {
        pedidoService.save(pedido);
    }

    public void update(Pedido pedido) {
        pedidoService.update(pedido);
    }

    public void deleteById(int id) {
        pedidoService.deleteById(id);
    }
}
