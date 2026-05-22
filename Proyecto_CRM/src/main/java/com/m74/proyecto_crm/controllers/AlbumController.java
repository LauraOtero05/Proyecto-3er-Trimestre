package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.Album;
import com.m74.proyecto_crm.entities.Proveedor;
import com.m74.proyecto_crm.enums.FormatoDisco;
import com.m74.proyecto_crm.services.AlbumService;
import com.m74.proyecto_crm.services.ProveedorService;
import com.m74.proyecto_crm.util.GeneroHelper;
import com.m74.proyecto_crm.util.InputHelper;

import java.util.List;

public class AlbumController {

    private final AlbumService albumService;
    private final ProveedorService proveedorService;

    public AlbumController() {
        this.albumService = new AlbumService();
        this.proveedorService = new ProveedorService();
    }

    public void crearAlbum() {
        System.out.println("\n--- REGISTRAR NUEVO ÁLBUM ---");

        String titulo = InputHelper.readString("Título del álbum: ");
        String artista = InputHelper.readString("Artista / Banda: ");

        FormatoDisco formato = null;
        while (formato == null) {
            System.out.println("Formatos disponibles: VINILO, CD, CASSETTE, DIGITAL");
            String formatoStr = InputHelper.readString("-> Introduce el formato: ").toUpperCase().trim();
            try {
                formato = FormatoDisco.valueOf(formatoStr);
            } catch (IllegalArgumentException e) {
                System.out.println("[!] Error: El formato '" + formatoStr + "' no es válido. Inténtalo de nuevo.");
            }
        }

        double precio = -1;
        while (precio < 0) {
            precio = InputHelper.readDouble("Precio (€): ");
            if (precio < 0) System.out.println("[!] El precio no puede ser negativo.");
        }

        int stock = -1;
        while (stock < 0) {
            stock = InputHelper.readInt("Unidades en Stock: ");
            if (stock < 0) System.out.println("[!] El stock no puede ser negativo.");
        }

        int idProveedor = -1;
        while (idProveedor == -1) {
            System.out.println("\n[i] Consultando empresas de distribución...");
            mostrarProveedoresResumen();

            String critProv = InputHelper.readString("Introduce el ID o Nombre del Proveedor seleccionado: ");
            Proveedor p = proveedorService.obtenerProveedorPorIdONombre(critProv);

            if (p != null) {
                idProveedor = p.getIdProveedor();
            } else {
                System.out.println("\n[!] El proveedor '" + critProv + "' no existe en el sistema.");
                System.out.println("[i] Por seguridad, debes registrar primero al proveedor desde su menú de Gestión de Proveedores.");
                System.out.println("-----------------------------------------------------------------------------------------");
                return;
            }
        }

        int idGenero = -1;
        while (idGenero == -1) {
            System.out.println("\n[i] Consultando estilos musicales en base de datos...");
            GeneroHelper.mostrarGenerosDisponibles(); // Pintamos los géneros existentes

            String critGen = InputHelper.readString("Introduce el ID o Nombre del Género Musical: ");
            idGenero = GeneroHelper.solicitarORegistrarGenero(critGen);
        }

        Album nuevo = new Album(0, titulo, artista, formato, precio, stock, idProveedor, "", idGenero, "");
        albumService.crearAlbum(nuevo);
    }

    public void listarAlbumes() {
        System.out.println("\n===============================================================================================================================================");
        System.out.println("                                                       CATÁLOGO GENERAL DE ÁLBUMES");
        System.out.println("===============================================================================================================================================");

        List<Album> lista = albumService.obtenerTodosLosAlbumes();

        if (lista.isEmpty()) {
            System.out.println("   El catálogo está vacío actualmente.");
            System.out.println("===============================================================================================================================================");
            return;
        }

        System.out.format("| %-7s | %-25s | %-20s | %-10s | %-9s | %-7s | %-22s | %-15s |\n",
                "ID PROD", "TÍTULO DEL ÁLBUM", "ARTISTA / BANDA", "FORMATO", "PRECIO", "STOCK", "DISTRIBUIDOR", "GÉNERO");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");

        for (Album a : lista) {
            imprimirFilaAlbum(a);
        }

        System.out.println("===============================================================================================================================================\n");
    }

    private void imprimirFilaAlbum(Album album) {
        String t = album.getTitulo().length() > 24 ? album.getTitulo().substring(0, 21) + "..." : album.getTitulo();
        String art = album.getArtista().length() > 19 ? album.getArtista().substring(0, 16) + "..." : album.getArtista();

        String prov = album.getNombreProveedor() != null ? album.getNombreProveedor() : "";
        if (prov.length() > 21) prov = prov.substring(0, 18) + "...";

        String gen = album.getNombreGenero() != null ? album.getNombreGenero() : "";
        if (gen.length() > 14) gen = gen.substring(0, 11) + "...";

        System.out.format(
                "| %-7d | %-25s | %-20s | %-10s | %6.2f € | %5d | %-22s | %-15s |\n",
                album.getIdProducto(), t, art, album.getFormato(), album.getPrecio(), album.getStock(), prov, gen
        );
    }

    public void buscarAlbumPorId() {
        System.out.println("\n--- BUSCAR ÁLBUM EN TIENDA ---");

        if (!mostrarResumen()) return;

        int id = InputHelper.readInt("Introduce el ID del álbum que deseas consultar (o '0' para salir): ");
        if (id == 0) {
            System.out.println("Búsqueda cancelada.");
            return;
        }

        Album album = albumService.obtenerAlbumPorId(id);
        if (album != null) {
            System.out.println("\n==================================================");
            System.out.println("              ¡ÁLBUM LOCALIZADO!");
            System.out.println("==================================================");
            System.out.println("• ID Producto: " + album.getIdProducto());
            System.out.println("• Título:      " + album.getTitulo());
            System.out.println("• Artista:     " + album.getArtista());
            System.out.println("• Formato:     " + album.getFormato());
            System.out.println("• Precio:      " + album.getPrecio() + " €");
            System.out.println("• Stock:       " + album.getStock() + " uds.");
            System.out.println("• Distribuye:  " + album.getNombreProveedor());
            System.out.println("• Género:      " + album.getNombreGenero());
            System.out.println("==================================================");
        }
    }

    public void modificarAlbum() {
        System.out.println("\n--- ACTUALIZAR ÁLBUM ---");
        if (!mostrarResumen()) return;

        int id = InputHelper.readInt("Introduce el ID del álbum que deseas editar (o '0' para salir): ");
        if (id == 0) return;

        Album album = albumService.obtenerAlbumPorId(id);
        if (album == null) return;

        int opcion;
        do {
            System.out.println("\n--- DATOS ACTUALES DEL ÁLBUM ---");
            System.out.println("1. Título:    " + album.getTitulo());
            System.out.println("2. Artista:   " + album.getArtista());
            System.out.println("3. Formato:   " + album.getFormato());
            System.out.println("4. Precio:    " + album.getPrecio() + " €");
            System.out.println("5. Stock:     " + album.getStock() + " uds.");
            System.out.println("6. Distrib.:  " + album.getNombreProveedor() + " (ID: " + album.getIdProveedor() + ")");
            System.out.println("7. Género:    " + album.getNombreGenero() + " (ID: " + album.getIdGenero() + ")");
            System.out.println("0. GUARDAR CAMBIOS Y REGRESAR");

            opcion = InputHelper.readIntInRange("¿Qué campo quieres editar? (0-7): ", 0, 7);

            switch (opcion) {
                case 1 -> {
                    String s = InputHelper.readString("Nuevo Título (o marca '0' para cancelar): ");
                    if (!s.equals("0")) album.setTitulo(s);
                }
                case 2 -> {
                    String s = InputHelper.readString("Nuevo Artista (o marca '0' para cancelar): ");
                    if (!s.equals("0")) album.setArtista(s);
                }
                case 3 -> {
                    while (true) {
                        System.out.println("Formatos: VINILO, CD, CASSETTE, DIGITAL");
                        String s = InputHelper.readString("Nuevo Formato (o marca '0' para cancelar): ").toUpperCase().trim();
                        if (s.equals("0")) break;
                        try {
                            album.setFormato(FormatoDisco.valueOf(s));
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("[!] Formato inválido. Inténtalo de nuevo.");
                        }
                    }
                }
                case 4 -> {
                    while (true) {
                        double p = InputHelper.readDouble("Nuevo Precio (o marca '0' para cancelar): ");
                        if (p == 0) break;
                        try {
                            album.setPrecio(p);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("[!] Error: " + e.getMessage());
                        }
                    }
                }
                case 5 -> {
                    while (true) {
                        int st = InputHelper.readInt("Nuevo Stock (o marca '-1' para cancelar): ");
                        if (st == -1) break;
                        try {
                            album.setStock(st);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("[!] Error: " + e.getMessage());
                        }
                    }
                }
                case 6 -> {
                    // Muestra los proveedores en caliente antes de editar
                    System.out.println("\nProveedores disponibles en el sistema:");
                    mostrarProveedoresResumen();

                    String critProv = InputHelper.readString("Introduce el ID o Nombre del nuevo Proveedor (o '0' para cancelar): ");
                    if (!critProv.equals("0")) {
                        Proveedor p = proveedorService.obtenerProveedorPorIdONombre(critProv);
                        if (p != null) {
                            album.setIdProveedor(p.getIdProveedor());
                            album.setNombreProveedor(p.getNombre());
                        } else {
                            System.out.println("[-] El proveedor introducido no existe. No se han guardado cambios.");
                        }
                    }
                }
                case 7 -> {
                    // Muestra los géneros en caliente antes de editar
                    System.out.println("\nGéneros musicales registrados actualmente:");
                    GeneroHelper.mostrarGenerosDisponibles();

                    String critGen = InputHelper.readString("Introduce el ID o Nombre del nuevo Género (o '0' para cancelar): ");
                    if (!critGen.equals("0")) {
                        int idGen = GeneroHelper.solicitarORegistrarGenero(critGen);
                        if (idGen != -1) album.setIdGenero(idGen);
                    }
                }
                case 0 -> {
                    System.out.println("Impactando actualizaciones en la base de datos...");
                    albumService.actualizarAlbum(album);
                }
            }
        } while (opcion != 0);
    }

    public void eliminarAlbum() {
        System.out.println("\n--- ELIMINAR ÁLBUM DEL SISTEMA ---");

        if (!mostrarResumen()) return;

        int id = InputHelper.readInt("Introduce el ID del álbum que vas a borrar PERMANENTEMENTE (o '0' para cancelar): ");
        if (id == 0) {
            System.out.println("[-] Operación abortada de forma segura. El disco sigue a salvo.");
            return;
        }

        albumService.eliminarAlbum(id);
    }

    public void exportarTxt() {
        System.out.println("\n--- EXPORTAR CATÁLOGO A TEXTO ---");
        String nombreArchivo = InputHelper.readString("Introduce el nombre o ruta del archivo (Ej: albumes.txt): ");

        if (nombreArchivo.trim().equals("0") || nombreArchivo.trim().isEmpty()) {
            System.out.println("Exportación cancelada.");
            return;
        }

        if (!nombreArchivo.toLowerCase().endsWith(".txt")) {
            nombreArchivo += ".txt";
        }

        System.out.println("\n[i] Generando reporte... esto puede tardar un rato. Puedes seguir utilizando el CRM de forma normal.");

        albumService.exportarAlbumesATxt(nombreArchivo);
    }

    private boolean mostrarResumen() {
        List<Album> lista = albumService.obtenerTodosLosAlbumes();
        if (lista.isEmpty()) {
            System.out.println("No hay ningún álbum registrado en el sistema.");
            return false;
        }

        System.out.println("Álbumes disponibles en tienda:");
        System.out.println("----------------------------------------");
        for (Album a : lista) {
            System.out.println(" > ID: " + a.getIdProducto() + " | '" + a.getTitulo() + "' de " + a.getArtista());
        }
        System.out.println("----------------------------------------");
        return true;
    }

    private void mostrarProveedoresResumen() {
        List<Proveedor> provs = proveedorService.obtenerTodosLosProveedores();
        System.out.println("----------------------------------------");
        for (Proveedor p : provs) {
            System.out.println(" > ID: " + p.getIdProveedor() + " | Empresa: " + p.getNombre());
        }
        System.out.println("----------------------------------------");
    }

    public List<Album> listarAlbumesParaPedido() {
        return albumService.obtenerTodosLosAlbumes();
    }

    public Album obtenerAlbumPorId(int id) {
        return albumService.obtenerAlbumPorId(id);
    }


}
