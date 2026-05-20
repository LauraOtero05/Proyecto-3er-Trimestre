package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteRepository {
    void save(Cliente cliente) throws SQLException;

    Cliente findById(int id) throws SQLException;

    List<Cliente> findAll() throws SQLException;

    void update(Cliente cliente) throws SQLException;

    void delete(int id) throws SQLException;
}
