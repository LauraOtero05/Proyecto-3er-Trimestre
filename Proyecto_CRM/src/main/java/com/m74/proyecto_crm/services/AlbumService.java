package com.m74.proyecto_crm.services;

import com.m74.proyecto_crm.entities.Album;
import com.m74.proyecto_crm.repositories.AlbumRepository;
import com.m74.proyecto_crm.repositories.AlbumRepositoryImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumService() {
        this.albumRepository = new AlbumRepositoryImpl();
    }

    public void crearAlbum(Album album) {

        try {
            if (album.getTitulo() == null || album.getTitulo().trim().isEmpty()) {
                throw new IllegalArgumentException("El título del álbum no puede estar vacío");

            }
            if (album.getArtista() == null || album.getArtista().trim().isEmpty()) {
                throw new IllegalArgumentException("El artista no puede estar vacío");

            }

            albumRepository.save(album);

            System.out.println("¡Álbum guardado con éxito en la base de datos!");

        } catch (SQLException e) {

            System.err.println("Error al guardar el álbum en la BD: " + e.getMessage());
        }
    }

    public Album obtenerAlbumPorId(int id) {

        try {

            Album album = albumRepository.findById(id);

            if (album == null) {
                System.out.println("No se encontró ningún álbum con el ID: " + id);

            }

            return album;

        } catch (SQLException e) {

            System.err.println("Error al buscar el álbum por ID: " + e.getMessage());

            return null;
        }
    }

    public List<Album> obtenerTodosLosAlbumes() {

        try {

            return albumRepository.findAll();

        } catch (SQLException e) {

            System.err.println("Error al recuperar los álbumes: " + e.getMessage());

            return new ArrayList<>();
        }
    }

    public void actualizarAlbum(Album album) {

        try {

            if (albumRepository.findById(album.getIdProducto()) == null) {

                System.out.println("No se puede actualizar. El álbum con ID " + album.getIdProducto() + " no existe");

                return;
            }

            albumRepository.update(album);

            System.out.println("¡Álbum actualizado con éxito!");

        } catch (SQLException e) {

            System.err.println("Error al actualizar el álbum: " + e.getMessage());
        }
    }

    public void eliminarAlbum(int id) {

        try {

            if (albumRepository.findById(id) == null) {

                System.out.println("No se puede eliminar. El álbum con ID " + id + " no existe");

                return;

            }

            albumRepository.delete(id);

            System.out.println("¡Álbum eliminado correctamente!");

        } catch (SQLException e) {

            System.err.println("No se pudo eliminar el álbum. Puede que esté asociado a un pedido. Error: " + e.getMessage());

        }
    }

    public void exportarAlbumesATxt(String rutaArchivo) {
        List<Album> listaAlbumes = obtenerTodosLosAlbumes();

        if (listaAlbumes.isEmpty()) {
            System.out.println("\nNo hay datos en la base de datos para exportar.");
            return;
        }
        java.io.File archivo = new java.io.File(rutaArchivo);

        try (java.io.FileWriter fw = new java.io.FileWriter(archivo); java.io.BufferedWriter bw = new java.io.BufferedWriter(fw)) {

            bw.write("=========================================================");
            bw.newLine();
            bw.write("          REPORTE GENERAL DE ÁLBUMES - 74_MINUTES        ");
            bw.newLine();
            bw.write("=========================================================");
            bw.newLine();
            bw.newLine();

            for (Album album : listaAlbumes) {
                bw.write(album.toString());
                bw.newLine();
            }

        } catch (java.io.IOException e) {
            System.err.println("\nError al escribir el archivo de texto: " + e.getMessage());
        }
    }

    public void updateStock(int idAlbum, int cantidad) throws SQLException {
        Album album = albumRepository.findById(idAlbum);

        if (album == null) {
            throw new SQLException("Álbum con ID " + idAlbum + " no encontrado.");
        }

        if (album.getStock() + cantidad < 0) {
            throw new SQLException("Stock insuficiente para el álbum '" + album.getTitulo() + "'. Stock actual: " + album.getStock());
        }

        albumRepository.updateStock(idAlbum, cantidad);
    }

    public boolean verificarRelaciones(int idProveedor, int idGenero) {

        try {

            AlbumRepositoryImpl repoImpl = (AlbumRepositoryImpl) this.albumRepository;

            if (!repoImpl.existeProveedor(idProveedor)) {
                System.out.println("Error: El ID de proveedor (" + idProveedor + ") no existe en el sistema.");
                return false;
            }

            if (!repoImpl.existeGenero(idGenero)) {
                System.out.println("Error: El ID de género musical (" + idGenero + ") no existe en el sistema.");
                return false;
            }

            return true;

        } catch (SQLException e) {

            System.err.println("Error al validar las relaciones en la base de datos: " + e.getMessage());
            return false;
        }
    }
}
