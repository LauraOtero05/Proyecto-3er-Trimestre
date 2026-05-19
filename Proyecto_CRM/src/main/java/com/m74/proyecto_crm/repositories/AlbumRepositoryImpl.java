package com.m74.proyecto_crm.repositories;

import com.m74.proyecto_crm.entities.Album;
import com.m74.proyecto_crm.enums.FormatoDisco;
import com.m74.proyecto_crm.util.DataBaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class AlbumRepositoryImpl implements AlbumRepository {

    @Override
    public void save(Album album) throws SQLException {

        String sql = "INSERT INTO albumes (titulo, artista, formato, precio, stock, id_proveedor, id_genero) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, album.getTitulo());
            ps.setString(2, album.getArtista());
            ps.setString(3, album.getFormato().getValue());
            ps.setDouble(4, album.getPrecio());
            ps.setInt(5, album.getStock());
            ps.setInt(6, album.getIdProveedor());
            ps.setInt(7, album.getIdGenero());

            ps.executeUpdate();
        }
    }

    @Override
    public Album findById(int id) throws SQLException {

        String sql = "SELECT * FROM albumes WHERE id_album = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAlbum(rs);
                }
            }
        }
        return null;

    }

    @Override
    public List<Album> findAll() throws SQLException {

        List<Album> albumes = new ArrayList<>();
        String sql = "SELECT * FROM albumes";

        Connection conn = DataBaseConnection.getConnection();

        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                albumes.add(mapearAlbum(rs));
            }
        }
        return albumes;
    }

    @Override
    public void update(Album album) throws SQLException {

        String sql = "UPDATE albumes SET titulo = ?, artista = ?, formato = ?, precio = ?, stock = ?, id_provedor = ?, id_genero = ? WHERE id_album = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, album.getTitulo());
            ps.setString(2, album.getArtista());
            ps.setString(3, album.getFormato().getValue());
            ps.setDouble(4, album.getPrecio());
            ps.setInt(5, album.getStock());
            ps.setInt(6, album.getIdProveedor());
            ps.setInt(7, album.getIdGenero());
            ps.setInt(8, album.getIdProducto());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM albumes WHERE id_album = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }


    private Album mapearAlbum(ResultSet rs) throws SQLException {

        int id = rs.getInt("id_album");
        String titulo = rs.getString("titulo");
        String artista = rs.getString("artista");
        FormatoDisco formato = FormatoDisco.fromString(rs.getString("formato"));
        double precio = rs.getDouble("precio");
        int stock = rs.getInt("stock");
        int idProveedor = rs.getInt("id_proveedor");
        int idGenero = rs.getInt("id_genero");

        return new Album(id, titulo, artista, formato, precio, stock, idProveedor, idGenero);
    }
}
