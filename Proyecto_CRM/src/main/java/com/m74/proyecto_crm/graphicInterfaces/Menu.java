package com.m74.proyecto_crm.graphicInterfaces;

import com.m74.proyecto_crm.controllers.*;
import com.m74.proyecto_crm.entities.DetallePedido;
import com.m74.proyecto_crm.entities.Pedido;
import com.m74.proyecto_crm.util.InputHelper;

import java.util.ArrayList;
import java.util.List;
import com.m74.proyecto_crm.entities.Cliente;
import com.m74.proyecto_crm.entities.Trabajador;
import com.m74.proyecto_crm.enums.RolTrabajador;
import com.m74.proyecto_crm.util.UbicationHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

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

    private void addClient() {
        System.out.println("\n--- REGISTRAR NUEVO CLIENTE ---");
        String nombre = InputHelper.readString("Nombre: ");
        String apellido = InputHelper.readString("Apellido: ");
        String direccion = InputHelper.readString("Dirección: ");
        String cpInput = InputHelper.readString("Código Postal (Texto/Números): ");
        int idCp = UbicationHelper.solicitarORegistrarUbicacion(cpInput);
        if (idCp == -1) {
            System.out.println("No se pudo procesar la dirección. Cancelando registro del cliente.");
            return;
        }
        String email = InputHelper.readString("Email: ");
        String password = InputHelper.readString("Contraseña: ");
        Cliente nuevoCliente = new Cliente(0, nombre, apellido, direccion, idCp, cpInput, email, password);
        System.out.println("\n--- Introducción de Teléfonos (Escribe 'fin' para terminar) ---");
        while (true) {
            String tlf = InputHelper.readString("Teléfono: ");
            if (tlf.equalsIgnoreCase("fin")) break;
            nuevoCliente.addTelefono(tlf);
        }
        clienteController.crearCliente(nuevoCliente);
    }

    private void findAllClients() {
        System.out.println("\n--- LISTADO GENERAL DE CLIENTES ---");
        List<Cliente> clientes = clienteController.obtenerTodosLosClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados en el sistema.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    private void findClientByID() {
        System.out.println("\n--- BUSCAR CLIENTE POR ID ---");
        for (Cliente c : clienteController.obtenerTodosLosClientes()) {
            System.out.println("ID: " + c.getIdCliente() + " | " + c.getNombre() + " " + c.getApellido());
        }
        System.out.println("(Escribe 0 para cancelar)");
        int id = InputHelper.readInt("Introduce el ID del cliente: ");
        if (id == 0) { System.out.println("Operación cancelada."); return; }
        Cliente c = clienteController.obtenerClientePorId(id);
        if (c != null) {
            System.out.println("\n[Cliente Encontrado]");
            System.out.println(c);
        }
    }

    private void updateClient() {
        System.out.println("\n--- ACTUALIZAR CLIENTE ---");
        for (Cliente c : clienteController.obtenerTodosLosClientes()) {
            System.out.println("ID: " + c.getIdCliente() + " | " + c.getNombre() + " " + c.getApellido());
        }
        System.out.println("(Escribe 0 para cancelar)");
        int id = InputHelper.readInt("Introduce el ID del cliente que deseas modificar: ");
        if (id == 0) { System.out.println("Operación cancelada."); return; }
        Cliente cliente = clienteController.obtenerClientePorId(id);
        if (cliente == null) return;
        int opcionModificar;
        do {
            System.out.println("\n--- DATOS ACTUALES DEL CLIENTE ---");
            System.out.println("1. Nombre:    " + cliente.getNombre());
            System.out.println("2. Apellido:  " + cliente.getApellido());
            System.out.println("3. Dirección: " + cliente.getDireccion());
            System.out.println("4. CP Actual: " + cliente.getCodigoPostal());
            System.out.println("5. Email:     " + cliente.getEmail());
            System.out.println("6. Contraseña: ********");
            System.out.println("7. Teléfonos: " + cliente.getTelefonos());
            System.out.println("0. GUARDAR CAMBIOS Y SALIR");
            opcionModificar = InputHelper.readIntInRange("¿Qué campo deseas modificar? (0-7): ", 0, 7);
            switch (opcionModificar) {
                case 1 -> { String nuevoNombre = InputHelper.readString("Introduce el nuevo Nombre: "); cliente.setNombre(nuevoNombre); }
                case 2 -> { String nuevoApellido = InputHelper.readString("Introduce el nuevo Apellido: "); cliente.setApellido(nuevoApellido); }
                case 3 -> { String nuevaDir = InputHelper.readString("Introduce la nueva Dirección: "); cliente.setDireccion(nuevaDir); }
                case 4 -> { String nuevoCp = InputHelper.readString("Introduce el nuevo Código Postal: ");
                    int idCp = UbicationHelper.solicitarORegistrarUbicacion(nuevoCp);
                    if (idCp != -1) { cliente.setIdCodigoPostal(idCp); cliente.setCodigoPostal(nuevoCp); } }
                case 5 -> { String nuevoEmail = InputHelper.readString("Introduce el nuevo Email: "); cliente.setEmail(nuevoEmail); }
                case 6 -> { String nuevaPassword = InputHelper.readString("Introduce la nueva Contraseña: "); cliente.setPasswordHash(nuevaPassword); }
                case 7 -> { System.out.println("\nReemplazando lista de teléfonos. Escribe 'fin' para terminar:");
                    List<String> nuevosTlfs = new ArrayList<>();
                    while (true) { String tlf = InputHelper.readString("Teléfono: "); if (tlf.equalsIgnoreCase("fin")) break; nuevosTlfs.add(tlf); }
                    cliente.setTelefonos(nuevosTlfs); }
                case 0 -> { System.out.println("Guardando actualizaciones en la base de datos..."); clienteController.actualizarCliente(cliente); }
            }
        } while (opcionModificar != 0);
    }

    private void deleteClient() {
        System.out.println("\n--- ELIMINAR CLIENTE ---");
        for (Cliente c : clienteController.obtenerTodosLosClientes()) {
            System.out.println("ID: " + c.getIdCliente() + " | " + c.getNombre() + " " + c.getApellido());
        }
        System.out.println("(Escribe 0 para cancelar)");
        int id = InputHelper.readInt("Introduce el ID del cliente a eliminar: ");
        if (id == 0) { System.out.println("Operación cancelada."); return; }
        clienteController.eliminarCliente(id);
    }

    private void generateCsvClient() {
        System.out.println("\n--- EXPORTAR CLIENTES A CSV ---");
        List<Cliente> clientes = clienteController.obtenerTodosLosClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes para exportar.");
            return;
        }
        System.out.println("Generando archivo CSV, esto puede tardar un momento...");
        File archivo = new File("clientes_export.csv");
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            writer.println("ID,Nombre,Apellido,Dirección,Código Postal,Email,Teléfonos");
            for (Cliente c : clientes) {
                writer.println(c.getIdCliente() + "," + c.getNombre() + "," + c.getApellido() + "," +
                        c.getDireccion() + "," + c.getCodigoPostal() + "," + c.getEmail() + "," +
                        String.join("|", c.getTelefonos()));
            }
            System.out.println("¡Exportación completada! Archivo guardado en: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al exportar el archivo CSV: " + e.getMessage());
        }
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

    private void addWorker() {
        System.out.println("\n--- REGISTRAR NUEVO TRABAJADOR ---");
        String dni = InputHelper.readString("DNI: ");
        String nombre = InputHelper.readString("Nombre: ");
        String apellido = InputHelper.readString("Apellido: ");
        RolTrabajador rol = null;
        while (rol == null) {
            try {
                String rolTexto = InputHelper.readString("Rol (Administrador, Ventas, Almacen, Gerente): ");
                rol = RolTrabajador.fromString(rolTexto);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage() + " Inténtalo de nuevo.");
            }
        }
        String email = InputHelper.readString("Email: ");
        String password = InputHelper.readString("Contraseña: ");
        Trabajador nuevoTrabajador = new Trabajador(dni, nombre, apellido, rol, email, password);
        System.out.println("\n--- Introducción de Teléfonos (Escribe 'fin' para terminar) ---");
        while (true) {
            String tlf = InputHelper.readString("Teléfono: ");
            if (tlf.equalsIgnoreCase("fin")) break;
            nuevoTrabajador.addTelefono(tlf);
        }
        trabajadorController.crearTrabajador(nuevoTrabajador);
    }

    private void findAllWorkers() {
        System.out.println("\n--- LISTADO GENERAL DE TRABAJADORES ---");
        List<Trabajador> trabajadores = trabajadorController.obtenerTodosLosTrabajadores();
        if (trabajadores.isEmpty()) {
            System.out.println("No hay trabajadores registrados en el sistema.");
            return;
        }
        for (Trabajador t : trabajadores) {
            System.out.println(t);
        }
    }

    private void findWorkerByID() {
        System.out.println("\n--- BUSCAR TRABAJADOR POR DNI ---");
        for (Trabajador t : trabajadorController.obtenerTodosLosTrabajadores()) {
            System.out.println("DNI: " + t.getDni() + " | " + t.getNombre() + " " + t.getApellido());
        }
        System.out.println("(Escribe 0 para cancelar)");
        String dni = InputHelper.readString("Introduce el DNI del trabajador: ");
        if (dni.equals("0")) { System.out.println("Operación cancelada."); return; }
        Trabajador t = trabajadorController.obtenerTrabajadorPorDni(dni);
        if (t != null) {
            System.out.println("\n[Trabajador Encontrado]");
            System.out.println(t);
        }
    }

    private void updateWorker() {
        System.out.println("\n--- ACTUALIZAR TRABAJADOR ---");
        for (Trabajador t : trabajadorController.obtenerTodosLosTrabajadores()) {
            System.out.println("DNI: " + t.getDni() + " | " + t.getNombre() + " " + t.getApellido());
        }
        System.out.println("(Escribe 0 para cancelar)");
        String dni = InputHelper.readString("Introduce el DNI del trabajador que deseas modificar: ");
        if (dni.equals("0")) { System.out.println("Operación cancelada."); return; }
        Trabajador trabajador = trabajadorController.obtenerTrabajadorPorDni(dni);
        if (trabajador == null) return;
        int opcionModificar;
        do {
            System.out.println("\n--- DATOS ACTUALES DEL TRABAJADOR ---");
            System.out.println("1. Nombre:   " + trabajador.getNombre());
            System.out.println("2. Apellido: " + trabajador.getApellido());
            System.out.println("3. Rol:      " + trabajador.getRol().getValue());
            System.out.println("4. Email:    " + trabajador.getEmail());
            System.out.println("5. Contraseña: ********");
            System.out.println("6. Teléfonos: " + trabajador.getTelefonos());
            System.out.println("0. GUARDAR CAMBIOS Y SALIR");
            opcionModificar = InputHelper.readIntInRange("¿Qué campo deseas modificar? (0-6): ", 0, 6);
            switch (opcionModificar) {
                case 1 -> { String nuevoNombre = InputHelper.readString("Introduce el nuevo Nombre: "); trabajador.setNombre(nuevoNombre); }
                case 2 -> { String nuevoApellido = InputHelper.readString("Introduce el nuevo Apellido: "); trabajador.setApellido(nuevoApellido); }
                case 3 -> { RolTrabajador nuevoRol = null;
                    while (nuevoRol == null) { try { String rolTexto = InputHelper.readString("Introduce el nuevo Rol (Administrador, Ventas, Almacen, Gerente): "); nuevoRol = RolTrabajador.fromString(rolTexto); } catch (IllegalArgumentException e) { System.out.println("Error: " + e.getMessage() + " Inténtalo de nuevo."); } }
                    trabajador.setRol(nuevoRol); }
                case 4 -> { String nuevoEmail = InputHelper.readString("Introduce el nuevo Email: "); trabajador.setEmail(nuevoEmail); }
                case 5 -> { String nuevaPassword = InputHelper.readString("Introduce la nueva Contraseña: "); trabajador.setPasswordHash(nuevaPassword); }
                case 6 -> { System.out.println("\nReemplazando lista de teléfonos. Escribe 'fin' para terminar:");
                    List<String> nuevosTlfs = new ArrayList<>();
                    while (true) { String tlf = InputHelper.readString("Teléfono: "); if (tlf.equalsIgnoreCase("fin")) break; nuevosTlfs.add(tlf); }
                    trabajador.setTelefonos(nuevosTlfs); }
                case 0 -> { System.out.println("Guardando actualizaciones en la base de datos..."); trabajadorController.actualizarTrabajador(trabajador); }
            }
        } while (opcionModificar != 0);
    }

    private void deleteWorker() {
        System.out.println("\n--- ELIMINAR TRABAJADOR ---");
        for (Trabajador t : trabajadorController.obtenerTodosLosTrabajadores()) {
            System.out.println("DNI: " + t.getDni() + " | " + t.getNombre() + " " + t.getApellido());
        }
        System.out.println("(Escribe 0 para cancelar)");
        String dni = InputHelper.readString("Introduce el DNI del trabajador a eliminar: ");
        if (dni.equals("0")) { System.out.println("Operación cancelada."); return; }
        trabajadorController.eliminarTrabajador(dni);
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
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,5);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addOrder();
                case 2 -> findAllOrders();
                case 3 -> findOrderByID();
                case 4 -> updateOrder();
                case 5 -> deleteOrder();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);
    }

    private void addOrder(){

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
            System.out.println("Trabajador:   " + (pedido.getDniTrabajador() != null ? pedido.getDniTrabajador() : "Sin asignar"));

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
    }

    private void updateOrder(){
    }

    private void deleteOrder(){}

    // endregion

}
