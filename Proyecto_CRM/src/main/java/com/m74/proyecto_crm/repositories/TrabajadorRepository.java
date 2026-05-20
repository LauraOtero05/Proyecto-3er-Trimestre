package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.Trabajador;

import java.sql.SQLException;
import java.util.List;

public interface TrabajadorRepository {
    void save(Trabajador trabajador) throws SQLException;

    Trabajador findByDni(String dni) throws SQLException;

    List<Trabajador> findAll() throws SQLException;

    void update(Trabajador trabajador) throws SQLException;

    void delete(String dni) throws SQLException;
}
