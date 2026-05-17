package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.DetallePedido;

import java.util.List;

public interface DetallePedidoRepository {

    DetallePedido findById(int id);

    List<DetallePedido> findByIdPedido(int idPedido);  // ← más útil que findAll

    int save(DetallePedido detalle);

    void update(DetallePedido detalle);

    void deleteById(int id);

}
