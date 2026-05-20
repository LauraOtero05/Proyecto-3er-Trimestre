package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.Trabajador;
import com.m74.proyecto_crm.enums.RolTrabajador;
import com.m74.proyecto_crm.services.TrabajadorService;
import com.m74.proyecto_crm.util.InputHelper;

import java.util.ArrayList;
import java.util.List;

public class TrabajadorController {
    private final TrabajadorService trabajadorService;

    public TrabajadorController() {
        this.trabajadorService = new TrabajadorService();
    }

    public void crearTrabajador() {

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

        trabajadorService.crearTrabajador(nuevoTrabajador);
    }

    public void listarTrabajadores() {
        System.out.println("\n--- LISTADO GENERAL DE TRABAJADORES ---");
        List<Trabajador> trabajadores = trabajadorService.obtenerTodosLosTrabajadores();

        if (trabajadores.isEmpty()) {
            System.out.println("No hay trabajadores registrados en el sistema.");
            return;
        }

        for (Trabajador t : trabajadores) {
            System.out.println(t);
        }
    }

    public void buscarTrabajadorPorDni() {
        System.out.println("\n--- BUSCAR TRABAJADOR POR DNI ---");
        String dni = InputHelper.readString("Introduce el DNI del trabajador: ");

        Trabajador t = trabajadorService.obtenerTrabajadorPorDni(dni);
        if (t != null) {
            System.out.println("\n[Trabajador Encontrado]");
            System.out.println(t);
        }
    }

    public void modificarTrabajador() {
        System.out.println("\n--- ACTUALIZAR TRABAJADOR ---");
        String dni = InputHelper.readString("Introduce el DNI del trabajador que deseas modificar: ");

        Trabajador trabajador = trabajadorService.obtenerTrabajadorPorDni(dni);
        if (trabajador == null) return;

        int opcionModificar;
        do {
            System.out.println("\n--- DATOS ACTUALES DEL TRABAJADOR ---");
            System.out.println("1. Nombre:   " + trabajador.getNombre());
            System.out.println("2. Apellido: " + trabajador.getApellido());
            System.out.println("3. Rol:      " + trabajador.getRol().getValue());
            System.out.println("4. Email:    " + trabajador.getEmail());
            System.out.println("5. Contraseña: ********");
            System.out.println("6. Gestionar Teléfonos " + trabajador.getTelefonos());
            System.out.println("0. GUARDAR CAMBIOS Y SALIR");

            opcionModificar = InputHelper.readIntInRange("¿Qué campo deseas modificar? (0-6): ", 0, 6);

            switch (opcionModificar) {
                case 1 -> {
                    String nuevoNombre = InputHelper.readString("Introduce el nuevo Nombre: ");
                    trabajador.setNombre(nuevoNombre);
                }
                case 2 -> {
                    String nuevoApellido = InputHelper.readString("Introduce el nuevo Apellido: ");
                    trabajador.setApellido(nuevoApellido);
                }
                case 3 -> {
                    RolTrabajador nuevoRol = null;
                    while (nuevoRol == null) {
                        try {
                            String rolTexto = InputHelper.readString("Introduce el nuevo Rol (Administrador, Ventas, Almacen, Gerente): ");
                            nuevoRol = RolTrabajador.fromString(rolTexto);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage() + " Inténtalo de nuevo.");
                        }
                    }
                    trabajador.setRol(nuevoRol);
                }
                case 4 -> {
                    String nuevoEmail = InputHelper.readString("Introduce el nuevo Email: ");
                    trabajador.setEmail(nuevoEmail);
                }
                case 5 -> {
                    String nuevaPassword = InputHelper.readString("Introduce la nueva Contraseña: ");
                    trabajador.setPasswordHash(nuevaPassword);
                }
                case 6 -> {
                    System.out.println("\nReemplazando lista de teléfonos. Escribe 'fin' para terminar:");
                    List<String> nuevosTlfs = new ArrayList<>();
                    while (true) {
                        String tlf = InputHelper.readString("Teléfono: ");
                        if (tlf.equalsIgnoreCase("fin")) break;
                        nuevosTlfs.add(tlf);
                    }
                    trabajador.setTelefonos(nuevosTlfs);
                }
                case 0 -> {
                    System.out.println("Guardando actualizaciones en la base de datos...");
                    trabajadorService.actualizarTrabajador(trabajador);
                }
            }
        } while (opcionModificar != 0);
    }

    public void eliminarTrabajador() {
        System.out.println("\n--- ELIMINAR TRABAJADOR ---");
        String dni = InputHelper.readString("Introduce el DNI del trabajador a eliminar: ");
        trabajadorService.eliminarTrabajador(dni);
    }
}
