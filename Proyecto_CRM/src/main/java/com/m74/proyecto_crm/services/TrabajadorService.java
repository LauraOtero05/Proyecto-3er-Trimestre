package com.m74.proyecto_crm.services;

import com.m74.proyecto_crm.entities.Trabajador;
import com.m74.proyecto_crm.repositories.TrabajadorRepositoryImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorService {
    private final TrabajadorRepositoryImpl trabajadorRepository;

    public TrabajadorService() {
        this.trabajadorRepository = new TrabajadorRepositoryImpl();
    }

    public void crearTrabajador(Trabajador trabajador) {
        try {

            if (trabajador.getDni() == null || trabajador.getDni().trim().isEmpty()) {
                throw new IllegalArgumentException("El DNI del trabajador no puede estar vacío.");
            }
            if (trabajador.getNombre() == null || trabajador.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del trabajador no puede estar vacío.");
            }
            if (trabajador.getApellido() == null || trabajador.getApellido().trim().isEmpty()) {
                throw new IllegalArgumentException("El apellido no puede estar vacío.");
            }
            if (trabajador.getEmail() == null || trabajador.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("El email no puede estar vacío.");
            }

            trabajadorRepository.save(trabajador);
            System.out.println("¡Trabajador [" + trabajador.getNombre() + " " + trabajador.getApellido() + "] guardado con éxito!");

        } catch (SQLException e) {
            System.err.println("Error de base de datos al guardar el trabajador: " + e.getMessage());
        }
    }

    public Trabajador obtenerTrabajadorPorDni(String dni) {
        try {
            Trabajador trabajador = trabajadorRepository.findByDni(dni);
            if (trabajador == null) {
                System.out.println("No se encontró ningún trabajador con el DNI: " + dni);
            }
            return trabajador;
        } catch (SQLException e) {
            System.err.println("Error al buscar el trabajador: " + e.getMessage());
            return null;
        }
    }

    public List<Trabajador> obtenerTodosLosTrabajadores() {
        try {
            return trabajadorRepository.findAll();
        } catch (SQLException e) {
            System.err.println("Error al recuperar el listado de trabajadores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void actualizarTrabajador(Trabajador trabajador) {
        try {
            if (trabajadorRepository.findByDni(trabajador.getDni()) == null) {
                System.out.println("Error: El trabajador con DNI " + trabajador.getDni() + " no existe.");
                return;
            }

            trabajadorRepository.update(trabajador);
            System.out.println("¡Trabajador actualizado con éxito en el sistema!");

        } catch (SQLException e) {
            System.err.println("Error al actualizar los datos del trabajador: " + e.getMessage());
        }
    }

    public void eliminarTrabajador(String dni) {
        try {
            if (trabajadorRepository.findByDni(dni) == null) {
                System.out.println("Error: El trabajador con DNI " + dni + " no existe.");
                return;
            }

            trabajadorRepository.delete(dni);
            System.out.println("¡Trabajador eliminado correctamente junto con todos sus teléfonos!");

        } catch (SQLException e) {
            System.err.println("No se puede eliminar el trabajador. Tiene pedidos asociados en el sistema. " +
                    "Error de integridad: " + e.getMessage());
        }
    }
}
