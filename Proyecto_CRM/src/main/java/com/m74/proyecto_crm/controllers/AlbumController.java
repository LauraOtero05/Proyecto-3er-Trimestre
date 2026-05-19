package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.Album;
import com.m74.proyecto_crm.enums.FormatoDisco;
import com.m74.proyecto_crm.services.AlbumService;
import com.m74.proyecto_crm.util.InputHelper;

import java.util.List;

public class AlbumController {

    private final AlbumService albumService;

    public AlbumController() {
        this.albumService = new AlbumService();
    }

    public void crearAlbum() {

        System.out.println("\n--- REGISTRAR NUEVO ÁLBUM ---");

        String titulo = InputHelper.readString("Título: ");
        String artista = InputHelper.readString("Artista: ");

        FormatoDisco formato = null;

        while (formato == null) {

            try {
                String formatoTexto = InputHelper.readString("Formato (CD, Vinilo, Cassette, Digital): ");

                formato = FormatoDisco.fromString(formatoTexto);

            } catch (IllegalArgumentException e) {

                System.out.println("Error: " + e.getMessage() + " Inténtalo de nuevo.");
            }
        }

        double precio = InputHelper.readDouble("Precio (€): ");
        int stock = InputHelper.readInt("Stock inicial: ");
        int idProveedor = InputHelper.readInt("ID Proveedor: ");
        int idGenero = InputHelper.readInt("ID Género: ");

        Album nuevoAlbum = new Album(0, titulo, artista, formato, precio, stock, idProveedor, idGenero);

        albumService.crearAlbum(nuevoAlbum);
    }

    public void listarAlbumes() {

        System.out.println("\n--- LISTADO GENERAL DE ÁLBUMES ---");

        List<Album> albumes = albumService.obtenerTodosLosAlbumes();

        if (albumes.isEmpty()) {

            System.out.println("No hay álbumes registrados en el sistema.");

            return;
        }

        for (Album album : albumes) {
            System.out.println(album);
        }
    }

    public void buscarAlbumPorId() {

        System.out.println("\n--- BUSCAR ÁLBUM POR ID ---");

        int id = InputHelper.readInt("Introduce el ID del álbum: ");

        Album album = albumService.obtenerAlbumPorId(id);

        if (album != null) {
            System.out.println("\nÁlbum Encontrado: " + album);
        }
    }

    public void modificarAlbum() {

        System.out.println("\n--- ACTUALIZAR ÁLBUM ---");

        int id = InputHelper.readInt("Introduce el ID del álbum que deseas modificar: ");

        Album existente = albumService.obtenerAlbumPorId(id);

        if (existente == null) {
            return;
        }

        System.out.println("\nDeje los campos vacíos o use los nuevos valores:");

        String titulo = InputHelper.readString("Nuevo Título (Actual: " + existente.getTitulo() + "): ");
        String artista = InputHelper.readString("Nuevo Artista (Actual: " + existente.getArtista() + "): ");

        FormatoDisco formato = null;

        while (formato == null) {

            try {

                String formatoTexto = InputHelper.readString("Nuevo Formato (Actual: " + existente.getFormato() + "): ");

                formato = FormatoDisco.fromString(formatoTexto);

            } catch (IllegalArgumentException e) {

                System.out.println("Error: " + e.getMessage() + " Inténtalo de nuevo.");
            }
        }

        double precio = InputHelper.readDouble("Nuevo Precio (€) (Actual: " + existente.getPrecio() + "): ");
        int stock = InputHelper.readInt("Nuevo Stock (Actual: " + existente.getStock() + "): ");
        int idProveedor = InputHelper.readInt("Nuevo ID Proveedor (Actual: " + existente.getIdProveedor() + "): ");
        int idGenero = InputHelper.readInt("Nuevo ID Género (Actual: " + existente.getIdGenero() + "): ");

        Album albumActualizado = new Album(id, titulo, artista, formato, precio, stock, idProveedor, idGenero);

        albumService.actualizarAlbum(albumActualizado);
    }

    public void eliminarAlbum() {

        System.out.println("\n--- ELIMINAR ÁLBUM ---");

        int id = InputHelper.readInt("Introduce el ID del álbum a eliminar: ");

        albumService.eliminarAlbum(id);
    }


    public void exportarTxt() {

        System.out.println("\n--- EXPORTAR LISTADO DE ÁLBUMES ---");

        String nombreArchivo = InputHelper.readString("Introduce el nombre del archivo (Ej: listado_albumes.txt): ");

        albumService.exportarAlbumesATxt(nombreArchivo);
    }
}
