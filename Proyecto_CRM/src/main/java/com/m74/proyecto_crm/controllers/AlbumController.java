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

        if (!albumService.verificarRelaciones(idProveedor, idGenero)) {
            System.out.println("No se pudo registrar el álbum. Revisa los IDs introducidos.");
            return;
        }

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

        Album album = albumService.obtenerAlbumPorId(id);

        if (album == null) {
            return;
        }

        int opcionModificar;

        do {
            System.out.println("\n--- DATOS ACTUALES DEL ÁLBUM ---");
            System.out.println("1. Título:       " + album.getTitulo());
            System.out.println("2. Artista:      " + album.getArtista());
            System.out.println("3. Formato:      " + album.getFormato());
            System.out.println("4. Precio:       " + album.getPrecio() + " €");
            System.out.println("5. Stock:        " + album.getStock());
            System.out.println("6. ID Proveedor: " + album.getIdProveedor());
            System.out.println("7. ID Género:    " + album.getIdGenero());
            System.out.println("0. GUARDAR CAMBIOS Y SALIR");

            opcionModificar = InputHelper.readIntInRange("¿Qué campo deseas modificar? (0-7): ", 0, 7);

            switch (opcionModificar) {

                case 1 -> {
                    String nuevoTitulo = InputHelper.readString("Introduce el nuevo Título: ");
                    album.setTitulo(nuevoTitulo);
                }
                case 2 -> {
                    String nuevoArtista = InputHelper.readString("Introduce el nuevo Artista: ");
                    album.setArtista(nuevoArtista);
                }
                case 3 -> {
                    boolean formatoValido = false;
                    while (!formatoValido) {

                        try {
                            String formatoTexto = InputHelper.readString("Introduce el nuevo Formato (CD, Vinilo, Cassette, Digital): ");
                            album.setFormato(FormatoDisco.fromString(formatoTexto));
                            formatoValido = true;

                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage() + " Inténtalo de nuevo.");

                        }
                    }
                }
                case 4 -> {
                    double nuevoPrecio = InputHelper.readDouble("Introduce el nuevo Precio (€): ");

                    if (nuevoPrecio < 0) {
                        System.out.println("Error: El precio no puede ser negativo. No se ha modificado.");

                    } else {
                        album.setPrecio(nuevoPrecio);
                    }
                }
                case 5 -> {
                    int nuevoStock = InputHelper.readInt("Introduce el nuevo Stock: ");

                    if (nuevoStock < 0) {
                        System.out.println("Error: El stock no puede ser negativo. No se ha modificado.");

                    } else {
                        album.setStock(nuevoStock);
                    }
                }
                case 6 -> {
                    int nuevoProv = InputHelper.readInt("Introduce el nuevo ID Proveedor: ");

                    if (albumService.verificarRelaciones(nuevoProv, album.getIdGenero())) {
                        album.setIdProveedor(nuevoProv);
                    }
                }
                case 7 -> {
                    int nuevoGen = InputHelper.readInt("Introduce el nuevo ID Género: ");

                    if (albumService.verificarRelaciones(album.getIdProveedor(), nuevoGen)) {
                        album.setIdGenero(nuevoGen);
                    }
                }
                case 0 -> {

                    if (albumService.verificarRelaciones(album.getIdProveedor(), album.getIdGenero())) {
                        System.out.println("Enviando actualizaciones a la base de datos...");
                        albumService.actualizarAlbum(album);

                    } else {
                        System.out.println("No se guardaron los cambios debido a IDs inválidos.");
                    }
                }
            }
        } while (opcionModificar != 0);
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
