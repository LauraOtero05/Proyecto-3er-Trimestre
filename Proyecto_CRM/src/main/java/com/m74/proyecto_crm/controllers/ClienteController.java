package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.Cliente;
import com.m74.proyecto_crm.services.ClienteService;

import java.util.List;

public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController() {
        this.clienteService = new ClienteService();
    }

    public void crearCliente(Cliente cliente) {
        clienteService.crearCliente(cliente);
    }

    public Cliente obtenerClientePorId(int id) {
        return clienteService.obtenerClientePorId(id);
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteService.obtenerTodosLosClientes();
    }

    public void actualizarCliente(Cliente cliente) {
        clienteService.actualizarCliente(cliente);
    }

    public void eliminarCliente(int id) {
        clienteService.eliminarCliente(id);
    }

    public List<Cliente> obtenerTodosLosClientesParaCsv() {
        return clienteService.obtenerTodosLosClientes();
    }
}
