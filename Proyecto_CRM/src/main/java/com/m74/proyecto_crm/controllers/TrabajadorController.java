package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.Trabajador;
import com.m74.proyecto_crm.services.TrabajadorService;

import java.util.List;

public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    public TrabajadorController() {
        this.trabajadorService = new TrabajadorService();
    }

    public void crearTrabajador(Trabajador trabajador) {
        trabajadorService.crearTrabajador(trabajador);
    }

    public Trabajador obtenerTrabajadorPorDni(String dni) {
        return trabajadorService.obtenerTrabajadorPorDni(dni);
    }

    public List<Trabajador> obtenerTodosLosTrabajadores() {
        return trabajadorService.obtenerTodosLosTrabajadores();
    }

    public void actualizarTrabajador(Trabajador trabajador) {
        trabajadorService.actualizarTrabajador(trabajador);
    }

    public void eliminarTrabajador(String dni) {
        trabajadorService.eliminarTrabajador(dni);
    }
}
