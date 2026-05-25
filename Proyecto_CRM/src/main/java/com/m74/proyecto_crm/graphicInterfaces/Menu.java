package com.m74.proyecto_crm.graphicInterfaces;

import com.m74.proyecto_crm.controllers.*;
import com.m74.proyecto_crm.entities.*;
import com.m74.proyecto_crm.enums.EstadoPedido;
import com.m74.proyecto_crm.enums.FormatoDisco;
import com.m74.proyecto_crm.util.GeneroHelper;
import com.m74.proyecto_crm.util.InputHelper;

import java.time.LocalDate;
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

    private int selectClient() {
        List<Cliente> clientes = clienteController.obtenerTodosLosClientes();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
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
                System.out.println("ID no válido. Elige uno de la lista.");
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
        System.out.println("\n--- REGISTRAR NUEVO PROVEEDOR ---");

        String nombre = InputHelper.readString("Nombre de la empresa: ");
        String direccion = InputHelper.readString("Dirección física: ");
        String cpInput = InputHelper.readString("Código Postal (Texto/Números): ");

        int idCp = UbicationHelper.solicitarORegistrarUbicacion(cpInput);
        if (idCp == -1) {
            System.out.println("No se pudo procesar la dirección. Cancelando registro del proveedor.");
            return;
        }

        Proveedor nuevoProveedor = new Proveedor(0, nombre, direccion, idCp, cpInput);

        System.out.println("\n--- Introducción de Teléfonos (Escribe 'fin' para terminar) ---");
        while (true) {
            String tlf = InputHelper.readString("Teléfono: ");
            if (tlf.equalsIgnoreCase("fin")) break;
            nuevoProveedor.addTelefono(tlf);
        }

        System.out.println("\n--- Introducción de Correos Electrónicos (Escribe 'fin' para terminar) ---");
        while (true) {
            String email = InputHelper.readString("Email: ");
            if (email.equalsIgnoreCase("fin")) break;
            nuevoProveedor.addEmail(email);
        }

        proveedorController.crearProveedor(nuevoProveedor);
    }

    private void findAllSuppliers(){
        System.out.println("\n--- LISTADO GENERAL DE PROVEEDORES ---");
        List<Proveedor> proveedores = proveedorController.obtenerTodosLosProveedores();

        if (proveedores.isEmpty()) {
            System.out.println("No hay proveedores registrados en el sistema");
            return;
        }

        for (Proveedor p : proveedores) {
            System.out.println(p);
        }
    }

    private void findSupplierByID(){
        System.out.println("\n--- BUSCAR PROVEEDOR ---");

        List<Proveedor> listaCompleta = proveedorController.obtenerTodosLosProveedores();
        if (listaCompleta.isEmpty()) {
            System.out.println("No hay proveedores registrados en el sistema para buscar");
            return;
        }

        System.out.println("Proveedores disponibles en el sistema:");
        System.out.println("----------------------------------------");
        for (Proveedor p : listaCompleta) {
            System.out.println(" ID: " + p.getIdProveedor() + " | Empresa: " + p.getNombre());
        }
        System.out.println("----------------------------------------");

        String criterio = InputHelper.readString("Introduce el ID o el Nombre exacto de la empresa a buscar: ");
        Proveedor proveedorEncontrado = proveedorController.obtenerProveedorPorIdONombre(criterio);

        if (proveedorEncontrado != null) {
            System.out.println("\n==================================================");
            System.out.println(" ID Proveedor:  " + proveedorEncontrado.getIdProveedor());
            System.out.println(" Empresa:       " + proveedorEncontrado.getNombre());
            System.out.println(" Dirección:     " + proveedorEncontrado.getDireccion());
            System.out.println(" Cód. Postal:   " + proveedorEncontrado.getCodigoPostal());
            System.out.println(" Teléfonos:     " + proveedorEncontrado.getTelefonos());
            System.out.println(" Emails:        " + proveedorEncontrado.getEmails());
            System.out.println("==================================================\n");
        }
    }

    private void updateSupplier(){
        System.out.println("\n--- ACTUALIZAR PROVEEDOR ---");

        List<Proveedor> listaCompleta = proveedorController.obtenerTodosLosProveedores();
        if (listaCompleta.isEmpty()) {
            System.out.println("No hay proveedores registrados en el sistema para modificar");
            return;
        }

        System.out.println("Proveedores disponibles en el sistema:");
        System.out.println("----------------------------------------");
        for (Proveedor p : listaCompleta) {
            System.out.println(" ID: " + p.getIdProveedor() + " | Empresa: " + p.getNombre());
        }
        System.out.println("----------------------------------------");

        String criterio = InputHelper.readString("Introduce el ID o Nombre del proveedor (o escribe '0' para salir): ");
        if (criterio.trim().equals("0")) {
            System.out.println("Actualización cancelada de forma segura");
            return;
        }

        Proveedor proveedor = proveedorController.obtenerProveedorPorIdONombre(criterio);
        if (proveedor == null) return;

        int opcionModificar;
        do {
            System.out.println("\n--- DATOS ACTUALES DEL PROVEEDOR ---");
            System.out.println("1. Nombre:    " + proveedor.getNombre());
            System.out.println("2. Dirección: " + proveedor.getDireccion());
            System.out.println("3. CP Actual: " + proveedor.getCodigoPostal());
            System.out.println("4. Gestionar Teléfonos " + proveedor.getTelefonos());
            System.out.println("5. Gestionar Emails    " + proveedor.getEmails());
            System.out.println("0. GUARDAR CAMBIOS Y SALIR");

            opcionModificar = InputHelper.readIntInRange("¿Qué campo deseas modificar? (0-5): ", 0, 5);

            switch (opcionModificar) {
                case 1 -> {
                    System.out.println("[Modificando Nombre | Valor actual: " + proveedor.getNombre() + "]");
                    String nuevoNombre = InputHelper.readString("Introduce el nuevo Nombre (o escribe '0' para cancelar): ");
                    if (!nuevoNombre.trim().equals("0")) {
                        proveedor.setNombre(nuevoNombre);
                    } else {
                        System.out.println("Edición de campo cancelada");
                    }
                }
                case 2 -> {
                    System.out.println("[Modificando Dirección | Valor actual: " + proveedor.getDireccion() + "]");
                    String nuevaDir = InputHelper.readString("Introduce la nueva Dirección (o escribe '0' para cancelar): ");
                    if (!nuevaDir.trim().equals("0")) {
                        proveedor.setDireccion(nuevaDir);
                    } else {
                        System.out.println("Edición de campo cancelada.");
                    }
                }
                case 3 -> {
                    System.out.println("[Modificando CP | Valor actual: " + proveedor.getCodigoPostal() + "]");
                    String nuevoCp = InputHelper.readString("Introduce el nuevo Código Postal (o escribe '0' para cancelar): ");
                    if (!nuevoCp.trim().equals("0")) {
                        int idCp = proveedorController.buscarIdCP(nuevoCp);
                        if (idCp == -1) {
                            idCp = UbicationHelper.solicitarORegistrarUbicacion(nuevoCp);
                        }
                        if (idCp != -1) {
                            proveedor.setIdCodigoPostal(idCp);
                            proveedor.setCodigoPostal(nuevoCp);
                        }
                    } else {
                        System.out.println("Edición de campo cancelada");
                    }
                }
                case 4 -> gestionarListaContactos(proveedor.getTelefonos(), "Teléfono");
                case 5 -> gestionarListaContactos(proveedor.getEmails(), "Email");
                case 0 -> {
                    System.out.println("Guardando actualizaciones en la base de datos...");
                    proveedorController.actualizarProveedor(proveedor);
                }
            }
        } while (opcionModificar != 0);
    }

    private void deleteSupplier(){
        System.out.println("\n--- ELIMINAR PROVEEDOR ---");

        List<Proveedor> listaCompleta = proveedorController.obtenerTodosLosProveedores();
        if (listaCompleta.isEmpty()) {
            System.out.println("No hay proveedores en la tienda para eliminar.");
            return;
        }

        System.out.println("Proveedores en el sistema:");
        System.out.println("----------------------------------------");
        for (Proveedor p : listaCompleta) {
            System.out.println(" > ID: " + p.getIdProveedor() + " | Empresa: " + p.getNombre());
        }
        System.out.println("----------------------------------------");

        System.out.println("Introduce el ID del proveedor que deseas eliminar de forma PERMANENTE.");
        int id = InputHelper.readInt("ID del proveedor (o escribe '0' para cancelar la operación): ");

        if (id == 0) {
            System.out.println("Operación cancelada de forma segura. El proveedor no ha sufrido cambios.");
            return;
        }

        proveedorController.eliminarProveedor(id);
    }

    private void gestionarListaContactos(List<String> lista, String tipoContacto) {
        do {
            System.out.println("\n--- GESTIONAR " + tipoContacto.toUpperCase() + "S ---");
            if (lista.isEmpty()) {
                System.out.println("[No hay " + tipoContacto.toLowerCase() + "s registrados]");
            } else {
                for (int i = 0; i < lista.size(); i++) {
                    System.out.println((i + 1) + ". " + lista.get(i));
                }
            }
            System.out.println("A. Añadir nuevo " + tipoContacto.toLowerCase());
            System.out.println("M. Modificar uno existente");
            System.out.println("B. Borrar uno existente");
            System.out.println("0. Volver atrás");

            String entrada = InputHelper.readString("Elige una opción (A/M/B/0): ").toUpperCase().trim();

            switch (entrada) {
                case "A" -> {
                    String nuevo = InputHelper.readString("Introduce el nuevo " + tipoContacto.toLowerCase() + " (o escribe '0' para cancelar): ");
                    if (!nuevo.trim().equals("0")) lista.add(nuevo);
                }
                case "M" -> {
                    if (lista.isEmpty()) {
                        System.out.println("No hay nada que modificar");
                        break;
                    }
                    int indice = InputHelper.readIntInRange("Número del " + tipoContacto.toLowerCase() + " a modificar (1-" + lista.size() + "): ", 1, lista.size()) - 1;
                    String modificado = InputHelper.readString("Introduce el nuevo valor (Actual: " + lista.get(indice) + " | o escribe '0' para cancelar): ");
                    if (!modificado.trim().equals("0")) lista.set(indice, modificado);
                }
                case "B" -> {
                    if (lista.isEmpty()) {
                        System.out.println("No hay nada que borrar");
                        break;
                    }
                    int indice = InputHelper.readIntInRange("Número del " + tipoContacto.toLowerCase() + " a borrar (1-" + lista.size() + "): ", 1, lista.size()) - 1;
                    lista.remove(indice);
                    System.out.println(tipoContacto + " eliminado de la lista temporal.");
                }
                case "0" -> {}
                default -> System.out.println("Opción no válida.");
            }

            if (entrada.equals("0")) {
                break;
            }
        } while (true);
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

        System.out.println("\n----- REGISTRAR NUEVO ÁLBUM -----");
        String titulo = InputHelper.readString("Título del álbum: ");
        String artista = InputHelper.readString("Nombre del Artista o Banda: ");

        FormatoDisco formato = null;

        while (formato == null) {
            System.out.println("Formatos disponibles: VINILO, CD, CASSETTE, DIGITAL");
            String formatoStr = InputHelper.readString("Introduce el formato: ").toUpperCase().trim();
            try {
                formato = FormatoDisco.valueOf(formatoStr);
            } catch (IllegalArgumentException e) {
                System.out.println("¡Error! El formato '" + formatoStr + "' no es válido. Inténtalo de nuevo");
            }
        }

        double precio = -1;
        while (precio < 0) {
            precio = InputHelper.readDouble("Precio (€): ");
            if (precio < 0) System.out.println("El precio no puede ser negativo");
        }

        int stock = -1;
        while (stock < 0) {
            stock = InputHelper.readInt("Unidades en Stock: ");
            if (stock < 0) System.out.println("El stock no puede ser negativo");
        }

        int idProveedor = -1;
        while (idProveedor == -1) {
            System.out.println("\nConsultando empresas de distribución...");
            mostrarProveedoresResumen(albumController.obtenerTodosLosProveedores());

            String critProv = InputHelper.readString("Introduce el ID o Nombre del Proveedor seleccionado: ");
            Proveedor p = albumController.obtenerProveedorPorIdONombre(critProv);

            if (p != null) {
                idProveedor = p.getIdProveedor();
            } else {
                System.out.println("\nEl proveedor '" + critProv + "' no existe en el sistema");
                System.out.println("Por seguridad, debes registrar primero al proveedor desde su menú de Gestión de Proveedores.");
                System.out.println("-----------------------------------------------------------------------------------------");
                return;
            }
        }

        int idGenero = -1;
        while (idGenero == -1) {
            System.out.println("\nConsultando estilos musicales en la base de datos...");
            GeneroHelper.mostrarGenerosDisponibles();

            String critGen = InputHelper.readString("Introduce el ID o Nombre del Género Musical: ");
            idGenero = GeneroHelper.solicitarORegistrarGenero(critGen);
        }

        Album nuevo = new Album(0, titulo, artista, formato, precio, stock, idProveedor, "", idGenero, "");
        albumController.crearAlbum(nuevo);

    }

    private void findAllAlbums(){
        System.out.println("\n===============================================================================================================================================");
        System.out.println("                                                       CATÁLOGO GENERAL DE ÁLBUMES");
        System.out.println("===============================================================================================================================================");

        List<Album> lista = albumController.listarAlbumes();

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

    private void findAlbumByID(){
        System.out.println("\n----- BUSCAR ÁLBUM EN TIENDA -----");
        if (!mostrarResumen(albumController.listarAlbumesParaPedido())) return;

        int id = InputHelper.readInt("Introduce el ID del álbum que deseas consultar (o escribe '0' para salir): ");
        if (id == 0) {
            System.out.println("Búsqueda cancelada");
            return;
        }

        Album album = albumController.obtenerAlbumPorId(id);
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

    private void updateAlbum(){
        System.out.println("\n----- ACTUALIZAR ÁLBUM -----");
        if (!mostrarResumen(albumController.listarAlbumesParaPedido())) return;

        int id = InputHelper.readInt("Introduce el ID del álbum que deseas editar (o escribe '0' para salir): ");
        if (id == 0) return;

        Album album = albumController.obtenerAlbumPorId(id);
        if (album == null) {
            System.out.println("No se encontró ningún álbum con ese ID.");
            return;
        }

        int opcion;
        do {
            System.out.println("\n----- DATOS ACTUALES DEL ÁLBUM -----");
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
                    String s = InputHelper.readString("Nuevo Título (o escribe '0' para cancelar): ");
                    if (!s.equals("0")) album.setTitulo(s);
                }
                case 2 -> {
                    String s = InputHelper.readString("Nuevo Artista (o escribe '0' para cancelar): ");
                    if (!s.equals("0")) album.setArtista(s);
                }
                case 3 -> {
                    while (true) {
                        System.out.println("Formatos: VINILO, CD, CASSETTE, DIGITAL");
                        String s = InputHelper.readString("Nuevo Formato (o escribe '0' para cancelar): ").toUpperCase().trim();
                        if (s.equals("0")) break;
                        try {
                            album.setFormato(FormatoDisco.valueOf(s));
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Formato inválido. Inténtalo de nuevo");
                        }
                    }
                }
                case 4 -> {
                    while (true) {
                        double p = InputHelper.readDouble("Nuevo Precio (o escribe '0' para cancelar): ");
                        if (p == 0) break;
                        try {
                            album.setPrecio(p);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("¡Error! " + e.getMessage());
                        }
                    }
                }
                case 5 -> {
                    while (true) {
                        int st = InputHelper.readInt("Nuevo Stock (o escribe '-1' para cancelar): ");
                        if (st == -1) break;
                        try {
                            album.setStock(st);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("¡Error! " + e.getMessage());
                        }
                    }
                }
                case 6 -> {
                    System.out.println("\nProveedores disponibles en el sistema: ");
                    mostrarProveedoresResumen(albumController.obtenerTodosLosProveedores());

                    String critProv = InputHelper.readString("Introduce el ID o Nombre del nuevo Proveedor (o escribe '0' para cancelar): ");
                    if (!critProv.equals("0")) {
                        Proveedor p = albumController.obtenerProveedorPorIdONombre(critProv);
                        if (p != null) {
                            album.setIdProveedor(p.getIdProveedor());
                            album.setNombreProveedor(p.getNombre());
                        } else {
                            System.out.println("¡Error! El proveedor introducido no existe. No se han guardado cambios.");
                        }
                    }
                }
                case 7 -> {
                    System.out.println("\nGéneros musicales registrados actualmente: ");
                    GeneroHelper.mostrarGenerosDisponibles();

                    String critGen = InputHelper.readString("Introduce el ID o Nombre del nuevo Género (o escribe '0' para cancelar): ");
                    if (!critGen.equals("0")) {
                        int idGen = GeneroHelper.solicitarORegistrarGenero(critGen);
                        if (idGen != -1) album.setIdGenero(idGen);
                    }
                }
                case 0 -> {
                    System.out.println("Aplicando actualizaciones en la base de datos...");
                    albumController.actualizarAlbum(album);
                }
            }
        } while (opcion != 0);
    }

    private void deleteAlbum(){
        System.out.println("\n----- ELIMINAR ÁLBUM DEL SISTEMA -----");
        if (!mostrarResumen(albumController.listarAlbumesParaPedido())) return;

        int id = InputHelper.readInt("Introduce el ID del álbum que vas a borrar PERMANENTEMENTE (o escribe '0' para cancelar): ");
        if (id == 0) {
            System.out.println("Operación cancelada de forma segura. El disco sigue a salvo");
            return;
        }
        albumController.eliminarAlbum(id);
    }

    private void generateTxtAlbum(){
        System.out.println("\n----- EXPORTAR CATÁLOGO A TEXTO -----");
        String nombreArchivo = InputHelper.readString("Introduce el nombre o ruta del archivo (Ej: albumes.txt): ");

        if (nombreArchivo.trim().equals("0") || nombreArchivo.trim().isEmpty()) {
            System.out.println("Exportación cancelada");
            return;
        }

        if (!nombreArchivo.toLowerCase().endsWith(".txt")) {
            nombreArchivo += ".txt";
        }

        System.out.println("\nGenerando archivo... esto puede tardar un rato. No te preocupes, puedes seguir utilizando el CRM de forma normal.");

        String nombreFinal = nombreArchivo;

        new Thread(() -> albumController.exportarTxt(nombreFinal)).start();
    }

    private void imprimirFilaAlbum(Album album) {
        String t = album.getTitulo().length() > 24 ? album.getTitulo().substring(0, 21) + "..." : album.getTitulo();
        String art = album.getArtista().length() > 19 ? album.getArtista().substring(0, 16) + "..." : album.getArtista();
        String prov = album.getNombreProveedor() != null ? album.getNombreProveedor() : "";
        if (prov.length() > 21) prov = prov.substring(0, 18) + "...";
        String gen = album.getNombreGenero() != null ? album.getNombreGenero() : "";
        if (gen.length() > 14) gen = gen.substring(0, 11) + "...";

        System.out.format("| %-7d | %-25s | %-20s | %-10s | %6.2f € | %5d | %-22s | %-15s |\n",
                album.getIdProducto(), t, art, album.getFormato(), album.getPrecio(), album.getStock(), prov, gen
        );
    }

    private boolean mostrarResumen(List<Album> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay ningún álbum registrado en el sistema");
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

    private void mostrarProveedoresResumen(List<Proveedor> provs) {
        System.out.println("----------------------------------------");
        for (Proveedor p : provs) {
            System.out.println(" > ID: " + p.getIdProveedor() + " | Empresa: " + p.getNombre());
        }
        System.out.println("----------------------------------------");
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
