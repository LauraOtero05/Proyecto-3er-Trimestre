package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.Pedido;

import java.util.List;

public interface PedidoRepository {

    Pedido findById(int id);

    List<Pedido> findAll();

    int save(Pedido pedido);

    void update(Pedido pedido);

    void deleteById(int id);

}
