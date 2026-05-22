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
        System.out.println("\n--- BUSCAR PROVEEDOR ---");

        List<Proveedor> listaCompleta = proveedorService.obtenerTodosLosProveedores();

        if (listaCompleta.isEmpty()) {
            System.out.println("No hay proveedores registrados en el sistema para buscar.");
            return;
        }

        System.out.println("Proveedores disponibles en el sistema:");
        System.out.println("----------------------------------------");
        for (Proveedor p : listaCompleta) {
            System.out.println(" > ID: " + p.getIdProveedor() + " | Empresa: " + p.getNombre());
        }
        System.out.println("----------------------------------------");

        String criterio = InputHelper.readString("Introduce el ID o el Nombre exacto de la empresa a buscar: ");

        Proveedor proveedorEncontrado = proveedorService.obtenerProveedorPorIdONombre(criterio);

        if (proveedorEncontrado != null) {
            System.out.println("\n==================================================");
            System.out.println("• ID Proveedor:  " + proveedorEncontrado.getIdProveedor());
            System.out.println("• Empresa:       " + proveedorEncontrado.getNombre());
            System.out.println("• Dirección:     " + proveedorEncontrado.getDireccion());
            System.out.println("• Cód. Postal:   " + proveedorEncontrado.getCodigoPostal());
            System.out.println("• Teléfonos:     " + proveedorEncontrado.getTelefonos());
            System.out.println("• Emails:        " + proveedorEncontrado.getEmails());
            System.out.println("==================================================\n");
        }
    }

    public void modificarProveedor() {
        System.out.println("\n--- ACTUALIZAR PROVEEDOR ---");

        List<Proveedor> listaCompleta = proveedorService.obtenerTodosLosProveedores();
        if (listaCompleta.isEmpty()) {
            System.out.println("No hay proveedores registrados en el sistema para modificar.");
            return;
        }

        System.out.println("Proveedores disponibles en el sistema:");
        System.out.println("----------------------------------------");
        for (Proveedor p : listaCompleta) {
            System.out.println(" > ID: " + p.getIdProveedor() + " | Empresa: " + p.getNombre());
        }
        System.out.println("----------------------------------------");

        String criterio = InputHelper.readString("Introduce el ID o Nombre del proveedor (o marca '0' para salir): ");
        if (criterio.trim().equals("0")) {
            System.out.println("Actualización cancelada de forma segura.");
            return;
        }

        Proveedor proveedor = proveedorService.obtenerProveedorPorIdONombre(criterio);
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
                    String nuevoNombre = InputHelper.readString("Introduce el nuevo Nombre (o marca '0' para cancelar): ");
                    if (!nuevoNombre.trim().equals("0")) {
                        proveedor.setNombre(nuevoNombre);
                    } else {
                        System.out.println("Edición de campo cancelada.");
                    }
                }
                case 2 -> {
                    System.out.println("[Modificando Dirección | Valor actual: " + proveedor.getDireccion() + "]");
                    String nuevaDir = InputHelper.readString("Introduce la nueva Dirección (o marca '0' para cancelar): ");
                    if (!nuevaDir.trim().equals("0")) {
                        proveedor.setDireccion(nuevaDir);
                    } else {
                        System.out.println("Edición de campo cancelada.");
                    }
                }
                case 3 -> {
                    System.out.println("[Modificando CP | Valor actual: " + proveedor.getCodigoPostal() + "]");
                    String nuevoCp = InputHelper.readString("Introduce el nuevo Código Postal (o marca '0' para cancelar): ");
                    if (!nuevoCp.trim().equals("0")) {
                        int idCp = ubicationService.buscarIdCP(nuevoCp);
                        if (idCp == -1) {
                            idCp = com.m74.proyecto_crm.util.UbicationHelper.solicitarORegistrarUbicacion(nuevoCp);
                        }
                        if (idCp != -1) {
                            proveedor.setIdCodigoPostal(idCp);
                            proveedor.setCodigoPostal(nuevoCp);
                        }
                    } else {
                        System.out.println("Edición de campo cancelada.");
                    }
                }
                case 4 -> gestionarListaContactos(proveedor.getTelefonos(), "Teléfono");
                case 5 -> gestionarListaContactos(proveedor.getEmails(), "Email");
                case 0 -> {
                    System.out.println("Guardando actualizaciones en la base de datos");
                    proveedorService.actualizarProveedor(proveedor);
                }
            }
        } while (opcionModificar != 0);
    }

    private void gestionarListaContactos(List<String> lista, String tipoContacto) {
        int opt; // Mantienes tu variable si la necesitas, aunque con el break directo se gestiona solo
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
                    String nuevo = InputHelper.readString("Introduce el nuevo " + tipoContacto.toLowerCase() + " (o marca '0' para cancelar): ");
                    if (!nuevo.trim().equals("0")) lista.add(nuevo);
                }
                case "M" -> {
                    if (lista.isEmpty()) {
                        System.out.println("No hay nada que modificar.");
                        break;
                    }
                    int indice = InputHelper.readIntInRange("Número del " + tipoContacto.toLowerCase() + " a modificar (1-" + lista.size() + "): ", 1, lista.size()) - 1;
                    String modificado = InputHelper.readString("Introduce el nuevo valor (Actual: " + lista.get(indice) + " | o marca '0' para cancelar): ");
                    if (!modificado.trim().equals("0")) lista.set(indice, modificado);
                }
                case "B" -> {
                    if (lista.isEmpty()) {
                        System.out.println("No hay nada que borrar.");
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

    public void eliminarProveedor() {
        System.out.println("\n--- ELIMINAR PROVEEDOR ---");

        List<Proveedor> listaCompleta = proveedorService.obtenerTodosLosProveedores();
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

        proveedorService.eliminarProveedor(id);
    }


}
