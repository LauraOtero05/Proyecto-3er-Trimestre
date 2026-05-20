package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.Cliente;
import com.m74.proyecto_crm.services.ClienteService;
import com.m74.proyecto_crm.util.InputHelper;
import com.m74.proyecto_crm.util.UbicationHelper;

import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController() {
        this.clienteService = new ClienteService();
    }

    public void crearCliente() {

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

        clienteService.crearCliente(nuevoCliente);
    }

    public void listarClientes() {
        System.out.println("\n--- LISTADO GENERAL DE CLIENTES ---");
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados en el sistema.");
            return;
        }

        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    public void buscarClientePorId() {
        System.out.println("\n--- BUSCAR CLIENTE POR ID ---");
        int id = InputHelper.readInt("Introduce el ID del cliente: ");

        Cliente c = clienteService.obtenerClientePorId(id);
        if (c != null) {
            System.out.println("\n[Cliente Encontrado]");
            System.out.println(c);
        }
    }

    public void modificarCliente() {
        System.out.println("\n--- ACTUALIZAR CLIENTE ---");
        int id = InputHelper.readInt("Introduce el ID del cliente que deseas modificar: ");

        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) return;

        int opcionModificar;
        do {
            System.out.println("\n--- DATOS ACTUALES DEL CLIENTE ---");
            System.out.println("1. Nombre:    " + cliente.getNombre());
            System.out.println("2. Apellido:  " + cliente.getApellido());
            System.out.println("3. Dirección: " + cliente.getDireccion());
            System.out.println("4. CP Actual: " + cliente.getCodigoPostal());
            System.out.println("5. Email:     " + cliente.getEmail());
            System.out.println("6. Contraseña ********");
            System.out.println("7. Teléfonos: " + cliente.getTelefonos());
            System.out.println("0. GUARDAR CAMBIOS Y SALIR");

            opcionModificar = InputHelper.readIntInRange("¿Qué campo deseas modificar? (0-7): ", 0, 7);

            switch (opcionModificar) {
                case 1 -> {
                    String nuevoNombre = InputHelper.readString("Introduce el nuevo Nombre: ");
                    cliente.setNombre(nuevoNombre);
                }
                case 2 -> {
                    String nuevoApellido = InputHelper.readString("Introduce el nuevo Apellido: ");
                    cliente.setApellido(nuevoApellido);
                }
                case 3 -> {
                    String nuevaDir = InputHelper.readString("Introduce la nueva Dirección: ");
                    cliente.setDireccion(nuevaDir);
                }
                case 4 -> {
                    String nuevoCp = InputHelper.readString("Introduce el nuevo Código Postal: ");

                    int idCp = UbicationHelper.solicitarORegistrarUbicacion(nuevoCp);

                    if (idCp != -1) {
                        cliente.setIdCodigoPostal(idCp);
                        cliente.setCodigoPostal(nuevoCp);
                    }
                }
                case 5 -> {
                    String nuevoEmail = InputHelper.readString("Introduce el nuevo Email: ");
                    cliente.setEmail(nuevoEmail);
                }
                case 6 -> {
                    String nuevaPassword = InputHelper.readString("Introduce la nueva Contraseña: ");
                    cliente.setPasswordHash(nuevaPassword);
                }
                case 7 -> {
                    System.out.println("\nReemplazando lista de teléfonos. Escribe 'fin' para terminar:");
                    List<String> nuevosTlfs = new ArrayList<>();
                    while (true) {
                        String tlf = InputHelper.readString("Teléfono: ");
                        if (tlf.equalsIgnoreCase("fin")) break;
                        nuevosTlfs.add(tlf);
                    }
                    cliente.setTelefonos(nuevosTlfs);
                }
                case 0 -> {
                    System.out.println("Guardando actualizaciones en la base de datos...");
                    clienteService.actualizarCliente(cliente);
                }
            }
        } while (opcionModificar != 0);
    }

    public void eliminarCliente() {
        System.out.println("\n--- ELIMINAR CLIENTE ---");
        int id = InputHelper.readInt("Introduce el ID del cliente a eliminar: ");
        clienteService.eliminarCliente(id);
    }

    public void exportarCsv() {
        System.out.println("\n--- EXPORTAR CLIENTES A CSV ---");
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes para exportar.");
            return;
        }

        String nombreArchivo = "clientes_export.csv";
        System.out.println("Generando archivo CSV, esto puede tardar un momento...");
        java.io.File archivo = new java.io.File(nombreArchivo);

        try (java.io.PrintWriter writer = new java.io.PrintWriter(
                new java.io.OutputStreamWriter(
                        new java.io.FileOutputStream(archivo), java.nio.charset.StandardCharsets.UTF_8))) {

            writer.println("ID,Nombre,Apellido,Dirección,Código Postal,Email,Teléfonos");

            for (Cliente c : clientes) {
                writer.println(c.getIdCliente() + "," + c.getNombre() + "," + c.getApellido() + "," +
                        c.getDireccion() + "," + c.getCodigoPostal() + "," + c.getEmail() + "," +
                        String.join("|", c.getTelefonos()));
            }

            System.out.println("¡Exportación completada! Archivo guardado en: " + archivo.getAbsolutePath());

        } catch (java.io.IOException e) {
            System.err.println("Error al exportar el archivo CSV: " + e.getMessage());
        }
    }
}
