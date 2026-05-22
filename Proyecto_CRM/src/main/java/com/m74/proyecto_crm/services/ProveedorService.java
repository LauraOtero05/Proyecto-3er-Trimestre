package com.m74.proyecto_crm.services;

import com.m74.proyecto_crm.entities.Proveedor;
import com.m74.proyecto_crm.repositories.ProveedorRepositoryImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorService {

    private final ProveedorRepositoryImpl proveedorRepository;

    public ProveedorService() {
        this.proveedorRepository = new ProveedorRepositoryImpl();
    }

    public int verificarCodigoPostal(String cp) {

        try {
            return proveedorRepository.obtenerIdPorCodigoPostal(cp);

        } catch (SQLException e) {
            System.err.println("Error al verificar el código postal: " + e.getMessage());
            return -1;
        }

    }

    public int darDeAltaCodigoPostal(String cp, String ciudad, String provincia, String pais) {

        try {
            int nuevoId = proveedorRepository.crearCodigoPostal(cp, ciudad, provincia, pais);

            System.out.println("¡Código postal [" + cp + "] registrado correctamente en el sistema!");

            return nuevoId;

        } catch (SQLException e) {
            System.err.println("Error crítico al registrar el nuevo código postal: " + e.getMessage());
            return -1;
        }
    }

    public void crearProveedor(Proveedor proveedor) {
        try {

            if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del proveedor no puede estar vacío.");
            }
            if (proveedor.getDireccion() == null || proveedor.getDireccion().trim().isEmpty()) {
                throw new IllegalArgumentException("La dirección no puede estar vacía.");
            }

            proveedorRepository.save(proveedor);
            System.out.println("¡Proveedor [" + proveedor.getNombre() + "] guardado con éxito!");

        } catch (SQLException e) {
            System.err.println("Error de base de datos al guardar el proveedor: " + e.getMessage());
        }
    }

    public Proveedor obtenerProveedorPorId(int id) {
        try {
            Proveedor proveedor = proveedorRepository.findById(id);
            if (proveedor == null) {
                System.out.println("No se encontró ningún proveedor con el ID: " + id);
            }
            return proveedor;
        } catch (SQLException e) {
            System.err.println("Error al buscar el proveedor: " + e.getMessage());
            return null;
        }
    }

    public List<Proveedor> obtenerTodosLosProveedores() {
        try {
            return proveedorRepository.findAll();
        } catch (SQLException e) {
            System.err.println("Error al recuperar el listado de proveedores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void actualizarProveedor(Proveedor proveedor) {
        try {
            if (proveedorRepository.findById(proveedor.getIdProveedor()) == null) {
                System.out.println("Error: El proveedor con ID " + proveedor.getIdProveedor() + " no existe.");
                return;
            }

            proveedorRepository.update(proveedor);
            System.out.println("¡Proveedor actualizado con éxito en el sistema!");

        } catch (SQLException e) {
            System.err.println("Error al actualizar los datos del proveedor: " + e.getMessage());
        }
    }

    public void eliminarProveedor(int id) {
        try {
            if (proveedorRepository.findById(id) == null) {
                System.out.println("Error: El proveedor con ID " + id + " no existe.");
                return;
            }

            proveedorRepository.delete(id);
            System.out.println("¡Proveedor eliminado correctamente junto con todos sus contactos!");

        } catch (SQLException e) {
            // Captura si intentan borrar un proveedor que tiene álbumes asociados (Restricción ON DELETE RESTRICT)
            System.err.println("No se puede eliminar el proveedor. Hay álbumes en la tienda que dependen de él. " +
                    "Error de integridad: " + e.getMessage());
        }
    }

}
