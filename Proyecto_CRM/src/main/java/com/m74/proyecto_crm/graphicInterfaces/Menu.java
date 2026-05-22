package com.m74.proyecto_crm.graphicInterfaces;

import com.m74.proyecto_crm.controllers.*;
import com.m74.proyecto_crm.entities.*;
import com.m74.proyecto_crm.enums.EstadoPedido;
import com.m74.proyecto_crm.util.InputHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    public void start(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A 74 MINUTES");
            System.out.println("¿Qué quiere gestionar?");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Gestionar Trabajadores");
            System.out.println("3. Gestionar Proveedores");
            System.out.println("4. Gestionar Álbumes");
            System.out.println("5. Gestionar Pedidos");
            System.out.println("0. SALIR");
            option = InputHelper.readIntInRange("",0,5);

            switch (option){

                case 0 -> System.out.println("Saliendo");
                case 1 -> manageClient();
                case 2 -> manageWorker();
                case 3 -> manageSupplier();
                case 4 -> manageAlbum();
                case 5 -> manageOrder();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);

    }

    // region CLIENT

    private final ClienteController clienteController = new ClienteController();

    private void manageClient(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A GESTIÓN DE CLIENTES");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Añadir Cliente");
            System.out.println("2. Ver todos los Clientes");
            System.out.println("3. Ver un Cliente");
            System.out.println("4. Actualizar un Cliente");
            System.out.println("5. Eliminar un Cliente");
            System.out.println("6. Exportar Clientes a un documento .csv");
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,6);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addClient();
                case 2 -> findAllClients();
                case 3 -> findClientByID();
                case 4 -> updateClient();
                case 5 -> deleteClient();
                case 6 -> generateCsvClient();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);
    }

    private void addClient(){
        clienteController.crearCliente();
    }

    private void findAllClients(){
        clienteController.listarClientes();
    }

    private void findClientByID(){
        clienteController.buscarClientePorId();
    }

    private void updateClient(){
        clienteController.modificarCliente();
    }

    private void deleteClient(){
        clienteController.eliminarCliente();
    }

    private void generateCsvClient(){
        clienteController.exportarCsv();
    }

    private int selectClient() {
        List<Cliente> clientes = clienteController.obtenerTodosLosClientes();

        if (clientes.isEmpty()) {
            System.out.println("[!] No hay clientes registrados.");
            return -1;
        }

        System.out.println("\n--- CLIENTES ---");
        System.out.println("ID   | Nombre");
        System.out.println("-----|--------------------");
        for (Cliente c : clientes) {
            System.out.printf("%-4d | %s %s%n",
                    c.getIdCliente(), c.getNombre(), c.getApellido());
        }

        List<Integer> idsValidos = clientes.stream()
                .map(Cliente::getIdCliente)
                .toList();

        int id;
        do {
            id = InputHelper.readInt("Introduce el ID del cliente: ");
            if (!idsValidos.contains(id)) {
                System.out.println("[!] ID no válido. Elige uno de la lista.");
            }
        } while (!idsValidos.contains(id));

        return id;
    }

    // endregion

    // region WORKER

    private final TrabajadorController trabajadorController = new TrabajadorController();


    private void manageWorker(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A GESTIÓN DE TRABAJADORES");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Añadir Trabajador");
            System.out.println("2. Ver todos los Trabajadores");
            System.out.println("3. Ver un Trabajador");
            System.out.println("4. Actualizar un Trabajador");
            System.out.println("5. Eliminar un Trabajador");
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,5);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addWorker();
                case 2 -> findAllWorkers();
                case 3 -> findWorkerByID();
                case 4 -> updateWorker();
                case 5 -> deleteWorker();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);
    }

    private void addWorker(){
        trabajadorController.crearTrabajador();

    }

    private void findAllWorkers(){
        trabajadorController.listarTrabajadores();

    }

    private void findWorkerByID(){
        trabajadorController.buscarTrabajadorPorDni();

    }

    private void updateWorker(){
        trabajadorController.modificarTrabajador();
    }

    private void deleteWorker(){
        trabajadorController.eliminarTrabajador();
    }

    private String selectWorker() {
        List<Trabajador> trabajadores = trabajadorController.obtenerTodosLosTrabajadores();

        if (trabajadores.isEmpty()) {
            System.out.println("No hay trabajadores registrados. Se dejará sin asignar.");
            return null;
        }

        System.out.println("\n--- TRABAJADORES ---");
        System.out.println("Nº  | DNI                 | Nombre");
        System.out.println("----|---------------------|--------------------");
        for (int i = 0; i < trabajadores.size(); i++) {
            Trabajador t = trabajadores.get(i);
            System.out.printf("%-3d | %-20s | %s %s%n",
                    i + 1, t.getDni(), t.getNombre(), t.getApellido());
        }
        System.out.println("0   | Sin asignar");

        System.out.println("Selecciona un trabajador");
        System.out.println("Si no desea asignar un trabajador pulse 0");
        int opcion = InputHelper.readIntInRange("", 0, trabajadores.size());
        if (opcion == 0) return null;
        return trabajadores.get(opcion - 1).getDni();
    }

    // endregion

    // region SUPPLIER

    private final ProveedorController proveedorController = new ProveedorController();

    private void manageSupplier(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A GESTIÓN DE PROVEEDORES");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Añadir Proveedor");
            System.out.println("2. Ver todos los Proveedores");
            System.out.println("3. Ver un Proveedor");
            System.out.println("4. Actualizar un Proveedor");
            System.out.println("5. Eliminar un Proveedor por su ID");
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,5);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addSupplier();
                case 2 -> findAllSuppliers();
                case 3 -> findSupplierByID();
                case 4 -> updateSupplier();
                case 5 -> deleteSupplier();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);
    }

    private void addSupplier(){
        proveedorController.crearProveedor();
    }

    private void findAllSuppliers(){
        proveedorController.listarProveedores();
    }

    private void findSupplierByID(){
        proveedorController.buscarProveedorPorId();
    }

    private void updateSupplier(){
        proveedorController.modificarProveedor();
    }

    private void deleteSupplier(){
        proveedorController.eliminarProveedor();
    }

    // endregion

    // region ALBUM

    private final AlbumController albumController = new AlbumController();

    private void manageAlbum(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A GESTIÓN DE ÁLBUMES");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Añadir Album");
            System.out.println("2. Ver todos los Álbumes");
            System.out.println("3. Ver un Album");
            System.out.println("4. Actualizar un Album");
            System.out.println("5. Eliminar un Album");
            System.out.println("6. Exportar Álbumes a un documento .txt");
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,6);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addAlbum();
                case 2 -> findAllAlbums();
                case 3 -> findAlbumByID();
                case 4 -> updateAlbum();
                case 5 -> deleteAlbum();
                case 6 -> generateTxtAlbum();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }

        }while (option!= 0);

    }

    private void addAlbum(){
        albumController.crearAlbum();
    }

    private void findAllAlbums(){
        albumController.listarAlbumes();
    }

    private void findAlbumByID(){
        albumController.buscarAlbumPorId();
    }

    private void updateAlbum(){
        albumController.modificarAlbum();
    }

    private void deleteAlbum(){
        albumController.eliminarAlbum();
    }

    private void generateTxtAlbum(){
        albumController.exportarTxt();
    }

    // endregion

    // region ORDER

    private final PedidoController pedidoController = new PedidoController();
    private final DetallePedidoController detallePedidoController = new DetallePedidoController();

    private void manageOrder(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A GESTIÓN DE PEDIDOS");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Añadir Pedido");
            System.out.println("2. Ver todos los Pedidos");
            System.out.println("3. Ver un Pedido");
            System.out.println("4. Actualizar un Pedido");
            System.out.println("5. Eliminar un Pedido");
            System.out.println("6. Exportar Pedidos a un documento .txt");
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,6);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addOrder();
                case 2 -> findAllOrders();
                case 3 -> findOrderByID();
                case 4 -> updateOrder();
                case 5 -> deleteOrder();
                case 6 -> generateTxtOrder();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);
    }

    private void addOrder(){

        System.out.println("\n--- NUEVO PEDIDO ---");

        int idCliente = selectClient();
        if (idCliente == -1) return;

        String dniTrabajador = selectWorker();

        List<DetallePedido> detalles = selectDetail();
        if (detalles.isEmpty()) return;

        double importeTotal = 0;

        for (DetallePedido detalle : detalles) {
            Album album = albumController.obtenerAlbumPorId(detalle.getIdAlbum());
            if (album != null) {
                importeTotal += album.getPrecio() * detalle.getCantidad();
            }
        }

        Pedido pedido = new Pedido(
                LocalDate.now(),
                EstadoPedido.PENDIENTE,
                importeTotal,
                idCliente,
                dniTrabajador
        );

        pedido.setDetalles(detalles);

        System.out.println("\n--- RESUMEN DEL PEDIDO ---");
        System.out.println("Cliente:  " + idCliente);
        System.out.println("Trabajador: " + (dniTrabajador != null ? dniTrabajador : "Sin asignar"));
        System.out.println("Importe total: " + String.format("%.2f", importeTotal) + " €");
        System.out.println("Detalles:");
        for (DetallePedido d : detalles) {
            Album a = albumController.obtenerAlbumPorId(d.getIdAlbum());
            System.out.println("  - " + (a != null ? a.getTitulo() : "ID " + d.getIdAlbum()) +
                    " x " + d.getCantidad() +
                    " = " + String.format("%.2f", (a != null ? a.getPrecio() * d.getCantidad() : 0)) + " €");
        }

        System.out.println("\n1. Confirmar pedido");
        System.out.println("0. Cancelar");
        int confirmar = InputHelper.readIntInRange("", 0, 1);

        if (confirmar == 0) {
            System.out.println("Pedido cancelado.");
            return;
        }

        pedidoController.save(pedido);
        System.out.println("Pedido creado correctamente.");

    }

    private void findAllOrders(){

        List<Pedido> pedidos = pedidoController.findAll();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }

        System.out.println("\n========== PEDIDOS ==========");
        for (Pedido pedido : pedidos) {

            System.out.println("--------------------------------");
            System.out.println("ID:           " + pedido.getIdPedido());
            System.out.println("Fecha:        " + pedido.getFecha());
            System.out.println("Estado:       " + pedido.getEstado().getValue());
            System.out.println("Importe:      " + pedido.getImporteTotal() + " €");
            System.out.println("ID Cliente:   " + pedido.getIdCliente());

            if(pedido.getDniTrabajador() == null){

                System.out.println("Trabajador:   Sin asignar ");
            }else{

                System.out.println("Trabajador:   " + pedido.getDniTrabajador());
            }

            List<DetallePedido> detalles = detallePedidoController.findByIdPedido(pedido.getIdPedido());

            if (detalles.isEmpty()) {
                System.out.println("Detalles:     Sin detalles");
            } else {
                System.out.println("Detalles:");
                for (DetallePedido detalle : detalles) {
                    System.out.println("  - ID Álbum: " + detalle.getIdAlbum() +
                            " | Cantidad: " + detalle.getCantidad());
                }
            }
        }
        System.out.println("================================\n");

    }

    private void findOrderByID(){

        int id = selectOrder();
        if (id == -1) return;

        Pedido pedido = pedidoController.findById(id);
        if (pedido == null) {
            System.out.println("Pedido no encontrado.");
            return;
        }

        System.out.println("\n========================================");
        System.out.println("         DETALLE DEL PEDIDO #" + pedido.getIdPedido());
        System.out.println("========================================");
        System.out.println("• Fecha:      " + pedido.getFecha());
        System.out.println("• Estado:     " + pedido.getEstado().getValue());
        System.out.println("• Importe:    " + String.format("%.2f", pedido.getImporteTotal()) + " €");
        System.out.println("• Cliente:    ID " + pedido.getIdCliente());
        System.out.println("• Trabajador: " + (pedido.getDniTrabajador() != null ? pedido.getDniTrabajador() : "Sin asignar"));

        System.out.println("\n--- LÍNEAS DEL PEDIDO ---");

        List<DetallePedido> detalles = pedido.getDetalles();

        if (detalles == null || detalles.isEmpty()) {
            System.out.println("  Sin detalles registrados.");
        } else {
            System.out.println("Nº  | ID Álbum | Título                    | Cantidad | Subtotal");
            System.out.println("----|----------|---------------------------|----------|----------");

            double total = 0;
            for (int i = 0; i < detalles.size(); i++) {
                DetallePedido d = detalles.get(i);
                Album album = albumController.obtenerAlbumPorId(d.getIdAlbum());

                String titulo = album != null
                        ? (album.getTitulo().length() > 24 ? album.getTitulo().substring(0, 21) + "..." : album.getTitulo())
                        : "ID " + d.getIdAlbum();

                double subtotal = album != null ? album.getPrecio() * d.getCantidad() : 0;
                total += subtotal;

                System.out.printf("%-3d | %-8d | %-25s | %-8d | %.2f €%n",
                        i + 1,
                        d.getIdAlbum(),
                        titulo,
                        d.getCantidad(),
                        subtotal);
            }

            System.out.println("----------------------------------------");
            System.out.printf("TOTAL:                                         %.2f €%n", total);
        }

        System.out.println("========================================\n");


    }

    private void updateOrder(){

        int id = selectOrder();
        if (id == -1) return;

        Pedido pedido = pedidoController.findById(id);
        if (pedido == null) {
            System.out.println("Pedido no encontrado.");
            return;
        }

        int opcion;
        do {
            System.out.println("\n========================================");
            System.out.println("     EDITANDO PEDIDO: " + pedido.getIdPedido());
            System.out.println("========================================");
            System.out.println("1. Cambiar cliente       (actual: ID " + pedido.getIdCliente() + ")");
            System.out.println("2. Cambiar trabajador    (actual: " + (pedido.getDniTrabajador() != null ? pedido.getDniTrabajador() : "Sin asignar") + ")");
            System.out.println("3. Cambiar estado        (actual: " + pedido.getEstado().getValue() + ")");
            System.out.println("4. Cambiar fecha         (actual: " + pedido.getFecha() + ")");
            System.out.println("5. Gestionar detalles");
            System.out.println("0. Guardar y volver");

            opcion = InputHelper.readIntInRange("", 0, 5);

            switch (opcion) {
                case 1 -> {
                    int idCliente = selectClient();
                    if (idCliente != -1) pedido.setIdCliente(idCliente);
                }
                case 2 -> {
                        String dni = selectWorker();
                        pedido.setDniTrabajador(dni);
                }
                case 3 -> {
                    System.out.println("Selecciona un nuevo estado: ");
                    EstadoPedido[] estados = EstadoPedido.values();
                    for (int i = 0; i < estados.length; i++) {
                        System.out.println((i + 1) + ". " + estados[i].getValue());
                    }
                    int optEstado = InputHelper.readIntInRange("", 1, estados.length);
                    pedido.setEstado(estados[optEstado - 1]);
                }
                case 4 -> {
                    System.out.println("Introduce la nueva fecha:");
                    int anio = InputHelper.readInt("Año (ej: 2025): ");
                    int mes  = InputHelper.readIntInRange("Mes (1-12): ", 1, 12);
                    int dia  = InputHelper.readIntInRange("Día (1-31): ", 1, 31);
                    try {
                        pedido.setFecha(LocalDate.of(anio, mes, dia));
                    } catch (Exception e) {
                        System.out.println("Fecha no válida. No se ha podido modificar.");
                    }
                }
                case 5 -> manageDetail(pedido);
                case 0 -> {

                    double total = 0;
                    for (DetallePedido d : pedido.getDetalles()) {
                        Album a = albumController.obtenerAlbumPorId(d.getIdAlbum());
                        if (a != null) total += a.getPrecio() * d.getCantidad();
                    }
                    pedido.setImporteTotal(total);
                    pedidoController.update(pedido);
                    System.out.println("Pedido actualizado correctamente.");
                }
            }
        } while (opcion != 0);

    }

    private void deleteOrder(){

        int id = selectOrder();
        if (id == -1) return;

        Pedido pedido = pedidoController.findById(id);
        if (pedido == null) {
            System.out.println("Pedido no encontrado.");
            return;
        }

        System.out.println("\n¿Estás seguro de que quieres eliminar el pedido " + id + "?");
        System.out.println("1. Sí, eliminar");
        System.out.println("0. Cancelar");
        int confirmacion = InputHelper.readIntInRange("", 0, 1);

        if (confirmacion == 0) {
            System.out.println("Operación cancelada.");
            return;
        }

        pedidoController.deleteById(id);
        System.out.println("Pedido " + id + " eliminado correctamente.");
    }

    private void generateTxtOrder(){

        System.out.println("\n--- EXPORTAR PEDIDOS A TXT ---");

        String nombreArchivo = InputHelper.readString("Introduce el nombre del archivo (Ej: pedidos.txt): ");

        if (nombreArchivo.trim().equals("0") || nombreArchivo.trim().isEmpty()) {
            System.out.println("Exportación cancelada.");
            return;
        }

        if (!nombreArchivo.toLowerCase().endsWith(".txt")) {
            nombreArchivo += ".txt";
        }

        System.out.println("\nGenerando informe, esto puede tardar un rato.");
        pedidoController.generateTxtOrder(nombreArchivo);

    }

    private int selectOrder() {

        List<Pedido> pedidos = pedidoController.findAll();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return -1;
        }

        System.out.println("\n========== PEDIDOS ==========");
        System.out.println("ID   | Estado      | ID Cliente");
        System.out.println("-----|-------------|----------");
        for (Pedido pedido : pedidos) {
            System.out.printf("%-4d | %-11s | %d%n",
                    pedido.getIdPedido(),
                    pedido.getEstado().getValue(),
                    pedido.getIdCliente());
        }
        System.out.println("==============================\n");

        List<Integer> idsValidos = pedidos.stream()
                .map(Pedido::getIdPedido)
                .toList();

        int id;
        do {
            id = InputHelper.readInt("Introduce el ID del pedido: ");
            if (!idsValidos.contains(id)) {
                System.out.println("ID no válido. Elige uno de la lista.");
            }
        } while (!idsValidos.contains(id));

        return id;
    }

    private List<DetallePedido> selectDetail() {
        List<DetallePedido> detalles = new ArrayList<>();
        List<Album> albumes = albumController.listarAlbumesParaPedido();

        if (albumes.isEmpty()) {
            System.out.println("No hay álbumes disponibles.");
            return detalles;
        }

        System.out.println("\n--- ÁLBUMES DISPONIBLES ---");
        System.out.println("ID   | Título                    | Precio  | Stock");
        System.out.println("-----|---------------------------|---------|------");
        for (Album a : albumes) {
            String titulo = a.getTitulo().length() > 24 ? a.getTitulo().substring(0, 21) + "..." : a.getTitulo();
            System.out.printf("%-4d | %-25s | %6.2f€ | %d%n",
                    a.getIdProducto(), titulo, a.getPrecio(), a.getStock());
        }

        do {
            System.out.println("\nIntroduce el ID de un álbum para añadirlo al pedido (0 para terminar): ");

            int idSeleccionado = InputHelper.readInt("");

            if (idSeleccionado == 0) {
                if (detalles.isEmpty()) {
                    System.out.println("Debes añadir al menos un álbum al pedido.");
                } else {
                    break;
                }
                continue;
            }


            Album albumSeleccionado = null;
            for (Album a : albumes) {
                if (a.getIdProducto() == idSeleccionado) {
                    albumSeleccionado = a;
                    break;
                }
            }

            if (albumSeleccionado == null) {
                System.out.println("El ID introducido no corresponde a ningún álbum disponible.");
                continue;
            }

            if (albumSeleccionado.getStock() == 0) {
                System.out.println("El álbum '" + albumSeleccionado.getTitulo() + "' no tiene stock disponible.");
                continue;
            }

            int cantidad = InputHelper.readIntInRange(
                    "Cantidad (stock disponible: " + albumSeleccionado.getStock() + "): ",
                    1, albumSeleccionado.getStock()
            );

            detalles.add(new DetallePedido(0, albumSeleccionado.getIdProducto(), cantidad));
            System.out.println("Álbum añadido. Puedes añadir otro o pulsa 0 para continuar.");

        } while (true);

        return detalles;
    }

    private void manageDetail(Pedido pedido) {
        int opcion;
        do {
            List<DetallePedido> detalles = pedido.getDetalles();

            System.out.println("\n--- DETALLES DEL PEDIDO: " + pedido.getIdPedido() + " ---");

            if (detalles == null || detalles.isEmpty()) {
                System.out.println("  Sin detalles registrados.");
            } else {
                System.out.println("ID Det. | ID Álbum | Título                    | Cantidad");
                System.out.println("--------|----------|---------------------------|----------");
                for (DetallePedido d : detalles) {
                    Album a = albumController.obtenerAlbumPorId(d.getIdAlbum());
                    String titulo = a != null
                            ? (a.getTitulo().length() > 24 ? a.getTitulo().substring(0, 21) + "..." : a.getTitulo())
                            : "ID " + d.getIdAlbum();
                    System.out.printf("%-7d | %-8d | %-25s | %d%n",
                            d.getIdDetalle(), d.getIdAlbum(), titulo, d.getCantidad());
                }
            }

            System.out.println("\n1. Añadir detalle");
            System.out.println("2. Editar cantidad de un detalle");
            System.out.println("3. Eliminar un detalle");
            System.out.println("0. Volver");

            opcion = InputHelper.readIntInRange("", 0, 3);

            switch (opcion) {
                case 1 -> {
                    List<Album> albumes = albumController.listarAlbumesParaPedido();
                    if (albumes.isEmpty()) {
                        System.out.println("No hay álbumes disponibles.");
                        break;
                    }

                    System.out.println("\n--- ÁLBUMES DISPONIBLES ---");
                    System.out.println("ID   | Título                    | Precio  | Stock");
                    System.out.println("-----|---------------------------|---------|------");
                    for (Album a : albumes) {
                        String t = a.getTitulo().length() > 24 ? a.getTitulo().substring(0, 21) + "..." : a.getTitulo();
                        System.out.printf("%-4d | %-25s | %6.2f€ | %d%n",
                                a.getIdProducto(), t, a.getPrecio(), a.getStock());
                    }

                    System.out.println("Introduce el ID del álbum (0 para cancelar): ");
                    int idSeleccionado = InputHelper.readInt("");
                    if (idSeleccionado == 0) break;

                    Album albumSeleccionado = null;
                    for (Album a : albumes) {
                        if (a.getIdProducto() == idSeleccionado) {
                            albumSeleccionado = a;
                            break;
                        }
                    }

                    if (albumSeleccionado == null) {
                        System.out.println("El ID introducido no es válido.");
                        break;
                    }

                    if (albumSeleccionado.getStock() == 0) {
                        System.out.println("El álbum '" + albumSeleccionado.getTitulo() + "' no tiene stock disponible.");
                        break;
                    }

                    int cantidad = InputHelper.readIntInRange(
                            "Cantidad (máx. " + albumSeleccionado.getStock() + "): ",
                            1, albumSeleccionado.getStock()
                    );

                    DetallePedido newDetail = new DetallePedido(pedido.getIdPedido(), albumSeleccionado.getIdProducto(), cantidad);
                    int idDetalle = detallePedidoController.save(newDetail);

                    if (idDetalle != -1) {
                        newDetail.setIdDetalle(idDetalle);
                        pedido.addDetalle(newDetail);
                        System.out.println("Detalle añadido correctamente.");
                    } else {
                        System.out.println("Error al añadir el detalle.");
                    }
                }
                case 2 -> {
                    if (detalles == null || detalles.isEmpty()) {
                        System.out.println("No hay detalles para editar.");
                        break;
                    }

                    System.out.println("Introduce el ID del Detalle a editar (0 para cancelar): ");
                    int idDetSeleccionado = InputHelper.readInt("");
                    if (idDetSeleccionado == 0) break;

                    DetallePedido detalle = null;
                    for (DetallePedido d : detalles) {
                        if (d.getIdDetalle() == idDetSeleccionado) {
                            detalle = d;
                            break;
                        }
                    }

                    if (detalle == null) {
                        System.out.println("No se encontró ningún detalle con ese ID en este pedido.");
                        break;
                    }

                    Album album = albumController.obtenerAlbumPorId(detalle.getIdAlbum());
                    if (album == null) {
                        System.out.println("Álbum no encontrado.");
                        break;
                    }

                    int stockDisponible = album.getStock() + detalle.getCantidad();
                    System.out.println("Cantidad actual: " + detalle.getCantidad());

                    int nuevaCantidad = InputHelper.readIntInRange(
                            "Nueva cantidad (máx. " + stockDisponible + "): ",
                            1, stockDisponible
                    );

                    DetallePedido detalleActualizado = new DetallePedido(
                            detalle.getIdDetalle(),
                            detalle.getIdPedido(),
                            detalle.getIdAlbum(),
                            nuevaCantidad
                    );

                    detallePedidoController.update(detalleActualizado);
                    detalle.setCantidad(nuevaCantidad);
                    System.out.println("Cantidad actualizada correctamente.");
                }
                case 3 -> {
                    if (detalles == null || detalles.isEmpty()) {
                        System.out.println("No hay detalles para eliminar.");
                        break;
                    }

                    if (detalles.size() == 1) {
                        System.out.println("Un pedido debe tener al menos un detalle. No puedes eliminar el último.");
                        break;
                    }

                    System.out.println("Introduce el ID del Detalle a eliminar (0 para cancelar): ");
                    int idDetSeleccionado = InputHelper.readInt("");
                    if (idDetSeleccionado == 0) break;

                    DetallePedido detalle = null;
                    int indiceABorrar = -1;
                    for (int i = 0; i < detalles.size(); i++) {
                        if (detalles.get(i).getIdDetalle() == idDetSeleccionado) {
                            detalle = detalles.get(i);
                            indiceABorrar = i;
                            break;
                        }
                    }

                    if (detalle == null) {
                        System.out.println("No se encontró ningún detalle con ese ID.");
                        break;
                    }

                    detallePedidoController.deleteById(detalle.getIdDetalle());
                    pedido.getDetalles().remove(indiceABorrar);
                    System.out.println("Detalle eliminado correctamente.");
                }
            }
        } while (opcion != 0);
    }
    // endregion

}
