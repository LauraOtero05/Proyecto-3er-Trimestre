package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.Album;

import java.sql.SQLException;
import java.util.List;

public interface AlbumRepository {

    void save(Album album) throws SQLException;

    Album findById(int id) throws SQLException;

    List<Album> findAll() throws SQLException;

    void update(Album album) throws SQLException;

    void delete(int id) throws SQLException;

    void updateStock(int idAlbum, int cantidad) throws SQLException;
}
