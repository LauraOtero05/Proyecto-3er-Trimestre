package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.Proveedor;

import java.sql.SQLException;
import java.util.List;

public interface ProveedorRepository {

    void save(Proveedor proveedor) throws SQLException;

    Proveedor findById(int id) throws SQLException;

    List<Proveedor> findAll() throws SQLException;

    void update(Proveedor proveedor) throws SQLException;

    void delete(int id) throws SQLException;
}
