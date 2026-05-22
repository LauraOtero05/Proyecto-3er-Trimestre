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
            ps.setString(3, album.getFormato().name());
            ps.setDouble(4, album.getPrecio());
            ps.setInt(5, album.getStock());
            ps.setInt(6, album.getIdProveedor());
            ps.setInt(7, album.getIdGenero());

            ps.executeUpdate();
        }
    }

    @Override
    public Album findById(int id) throws SQLException {
        String sql = "SELECT a.*, p.nombre AS proveedor_nombre, g.genero AS genero_nombre " +
                "FROM albumes a " +
                "INNER JOIN proveedores p ON a.id_proveedor = p.id_proveedor " +
                "INNER JOIN generos_musicales g ON a.id_genero = g.id_genero " +
                "WHERE a.id_album = ?";

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

        String sql = "SELECT a.*, p.nombre AS proveedor_nombre, g.genero AS genero_nombre " +
                "FROM albumes a " +
                "INNER JOIN proveedores p ON a.id_proveedor = p.id_proveedor " +
                "INNER JOIN generos_musicales g ON a.id_genero = g.id_genero";

        Connection conn = DataBaseConnection.getConnection();

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                albumes.add(mapearAlbum(rs));
            }
        }
        return albumes;
    }

    @Override
    public void update(Album album) throws SQLException {
        String sql = "UPDATE albumes SET titulo = ?, artista = ?, formato = ?, precio = ?, stock = ?, id_proveedor = ?, id_genero = ? WHERE id_album = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, album.getTitulo());
            ps.setString(2, album.getArtista());
            ps.setString(3, album.getFormato().name());
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

    @Override
    public void updateStock(int idAlbum, int cantidad) throws SQLException {
        String sql = "UPDATE albumes SET stock = stock + ? WHERE id_album = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, idAlbum);
            ps.executeUpdate();
        }
    }


    private Album mapearAlbum(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_album");
        String titulo = rs.getString("titulo");
        String artista = rs.getString("artista");

        FormatoDisco formato = FormatoDisco.valueOf(rs.getString("formato").toUpperCase());

        double precio = rs.getDouble("precio");
        int stock = rs.getInt("stock");
        int idProveedor = rs.getInt("id_proveedor");
        int idGenero = rs.getInt("id_genero");

        String nombreProv = rs.getString("proveedor_nombre");
        String nombreGen = rs.getString("genero_nombre");

        return new Album(id, titulo, artista, formato, precio, stock, idProveedor, nombreProv, idGenero, nombreGen);
    }

    public boolean existeProveedor(int idProveedor) throws SQLException {
        String sql = "SELECT 1 FROM proveedores WHERE id_proveedor = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProveedor);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existeGenero(int idGenero) throws SQLException {

        String sql = "SELECT 1 FROM generos_musicales WHERE id_genero = ?";
        Connection conn = DataBaseConnection.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGenero);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
