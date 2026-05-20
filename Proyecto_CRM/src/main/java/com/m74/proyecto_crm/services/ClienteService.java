package com.m74.proyecto_crm.services;

import com.m74.proyecto_crm.entities.Cliente;
import com.m74.proyecto_crm.repositories.ClienteRepositoryImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {
    private final ClienteRepositoryImpl clienteRepository;

    public ClienteService() {
        this.clienteRepository = new ClienteRepositoryImpl();
    }

    public void crearCliente(Cliente cliente) {
        try {

            if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
            }
            if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
                throw new IllegalArgumentException("El apellido no puede estar vacío.");
            }
            if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("El email no puede estar vacío.");
            }
            if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty()) {
                throw new IllegalArgumentException("La dirección no puede estar vacía.");
            }

            clienteRepository.save(cliente);
            System.out.println("¡Cliente [" + cliente.getNombre() + " " + cliente.getApellido() + "] guardado con éxito!");

        } catch (SQLException e) {
            System.err.println("Error de base de datos al guardar el cliente: " + e.getMessage());
        }
    }

    public Cliente obtenerClientePorId(int id) {
        try {
            Cliente cliente = clienteRepository.findById(id);
            if (cliente == null) {
                System.out.println("No se encontró ningún cliente con el ID: " + id);
            }
            return cliente;
        } catch (SQLException e) {
            System.err.println("Error al buscar el cliente: " + e.getMessage());
            return null;
        }
    }

    public List<Cliente> obtenerTodosLosClientes() {
        try {
            return clienteRepository.findAll();
        } catch (SQLException e) {
            System.err.println("Error al recuperar el listado de clientes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void actualizarCliente(Cliente cliente) {
        try {
            if (clienteRepository.findById(cliente.getIdCliente()) == null) {
                System.out.println("Error: El cliente con ID " + cliente.getIdCliente() + " no existe.");
                return;
            }

            clienteRepository.update(cliente);
            System.out.println("¡Cliente actualizado con éxito en el sistema!");

        } catch (SQLException e) {
            System.err.println("Error al actualizar los datos del cliente: " + e.getMessage());
        }
    }

    public void eliminarCliente(int id) {
        try {
            if (clienteRepository.findById(id) == null) {
                System.out.println("Error: El cliente con ID " + id + " no existe.");
                return;
            }

            clienteRepository.delete(id);
            System.out.println("¡Cliente eliminado correctamente junto con todos sus teléfonos!");

        } catch (SQLException e) {
            System.err.println("No se puede eliminar el cliente. Tiene pedidos asociados en el sistema. " +
                    "Error de integridad: " + e.getMessage());
        }
    }
}
