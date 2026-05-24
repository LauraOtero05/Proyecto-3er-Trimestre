package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.Proveedor;
import com.m74.proyecto_crm.services.ProveedorService;
import com.m74.proyecto_crm.services.UbicationService;
import com.m74.proyecto_crm.util.InputHelper;
import com.m74.proyecto_crm.util.UbicationHelper;

import java.util.ArrayList;
import java.util.List;

public class ProveedorController {

    private final ProveedorService proveedorService;
    private final UbicationService ubicationService;

    public ProveedorController() {
        this.proveedorService = new ProveedorService();
        this.ubicationService = new UbicationService();
    }

    public void crearProveedor(Proveedor nuevoProveedor) {
        proveedorService.crearProveedor(nuevoProveedor);
    }

    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorService.obtenerTodosLosProveedores();
    }

    public Proveedor obtenerProveedorPorIdONombre(String criterio) {
        return proveedorService.obtenerProveedorPorIdONombre(criterio);
    }

    public void actualizarProveedor(Proveedor proveedor) {
        proveedorService.actualizarProveedor(proveedor);
    }

    public void eliminarProveedor(int id) {
        proveedorService.eliminarProveedor(id);
    }

    public int buscarIdCP(String cp) {
        return ubicationService.buscarIdCP(cp);
    }
}
