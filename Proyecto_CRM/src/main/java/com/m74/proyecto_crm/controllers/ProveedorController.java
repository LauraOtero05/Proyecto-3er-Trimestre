package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.Proveedor;
import com.m74.proyecto_crm.services.ProveedorService;
import com.m74.proyecto_crm.services.UbicationService;
import com.m74.proyecto_crm.util.InputHelper;

import java.util.ArrayList;
import java.util.List;

public class ProveedorController {

    private final ProveedorService proveedorService;
    private final UbicationService ubicationService = new UbicationService();

    public ProveedorController() {
        this.proveedorService = new ProveedorService();
    }

    public void crearProveedor() {

        System.out.println("\n--- REGISTRAR NUEVO PROVEEDOR ---");

        String nombre = InputHelper.readString("Nombre de la empresa: ");
        String direccion = InputHelper.readString("Dirección física: ");
        String cpInput = InputHelper.readString("Código Postal (Texto/Números): ");

        int idCp = com.m74.proyecto_crm.util.UbicationHelper.solicitarORegistrarUbicacion(cpInput);
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

        proveedorService.crearProveedor(nuevoProveedor);
    }

    public void listarProveedores() {
        System.out.println("\n--- LISTADO GENERAL DE PROVEEDORES ---");
        List<Proveedor> proveedores = proveedorService.obtenerTodosLosProveedores();

        if (proveedores.isEmpty()) {
            System.out.println("No hay proveedores registrados en el sistema.");
            return;
        }

        for (Proveedor p : proveedores) {
            System.out.println(p);
        }
    }

    public void buscarProveedorPorId() {
        System.out.println("\n--- BUSCAR PROVEEDOR POR ID ---");
        int id = InputHelper.readInt("Introduce el ID del proveedor: ");

        Proveedor p = proveedorService.obtenerProveedorPorId(id);
        if (p != null) {
            System.out.println("\n[Proveedor Encontrado]");
            System.out.println(p);
        }
    }

    public void modificarProveedor() {
        System.out.println("\n--- ACTUALIZAR PROVEEDOR ---");
        int id = InputHelper.readInt("Introduce el ID del proveedor que deseas modificar: ");

        Proveedor proveedor = proveedorService.obtenerProveedorPorId(id);
        if (proveedor == null) return;

        int opcionModificar;
        do {
            System.out.println("\n--- DATOS ACTUALES DEL PROVEEDOR ---");
            System.out.println("1. Nombre:    " + proveedor.getNombre());
            System.out.println("2. Dirección: " + proveedor.getDireccion());
            System.out.println("3. CP Actual: " + proveedor.getCodigoPostal());
            System.out.println("4. Gestionar Teléfonos (" + proveedor.getTelefonos().size() + " registrados)");
            System.out.println("5. Gestionar Emails (" + proveedor.getEmails().size() + " registrados)");
            System.out.println("0. GUARDAR CAMBIOS Y SALIR");

            opcionModificar = InputHelper.readIntInRange("¿Qué campo deseas modificar? (0-5): ", 0, 5);

            switch (opcionModificar) {
                case 1 -> {
                    String nuevoNombre = InputHelper.readString("Introduce el nuevo Nombre: ");
                    proveedor.setNombre(nuevoNombre);
                }
                case 2 -> {
                    String nuevaDir = InputHelper.readString("Introduce la nueva Dirección: ");
                    proveedor.setDireccion(nuevaDir);
                }
                case 3 -> {
                    String nuevoCp = InputHelper.readString("Introduce el nuevo Código Postal: ");

                    // Reutilizamos el mismo asistente exacto en la modificación
                    int idCp = com.m74.proyecto_crm.util.UbicationHelper.solicitarORegistrarUbicacion(nuevoCp);

                    if (idCp != -1) {
                        proveedor.setIdCodigoPostal(idCp);
                        proveedor.setCodigoPostal(nuevoCp);
                    }
                }
                case 4 -> {
                    System.out.println("\nReemplazando lista de teléfonos. Escribe 'fin' para terminar:");
                    List<String> nuevosTlfs = new ArrayList<>();
                    while (true) {
                        String tlf = InputHelper.readString("Teléfono: ");
                        if (tlf.equalsIgnoreCase("fin")) break;
                        nuevosTlfs.add(tlf);
                    }
                    proveedor.setTelefonos(nuevosTlfs);
                }
                case 5 -> {
                    System.out.println("\nReemplazando lista de correos. Escribe 'fin' para terminar:");
                    List<String> nuevosEmails = new ArrayList<>();
                    while (true) {
                        String email = InputHelper.readString("Email: ");
                        if (email.equalsIgnoreCase("fin")) break;
                        nuevosEmails.add(email);
                    }
                    proveedor.setEmails(nuevosEmails);
                }
                case 0 -> {
                    System.out.println("Guardando actualizaciones en la base de datos...");
                    proveedorService.actualizarProveedor(proveedor);
                }
            }
        } while (opcionModificar != 0);
    }

    public void eliminarProveedor() {
        System.out.println("\n--- ELIMINAR PROVEEDOR ---");
        int id = InputHelper.readInt("Introduce el ID del proveedor a eliminar: ");
        proveedorService.eliminarProveedor(id);
    }


}
