package com.m74.proyecto_crm.entities;

import com.m74.proyecto_crm.enums.EstadoPedido;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private int idPedido;
    private LocalDate fecha;
    private EstadoPedido estado;
    private double importeTotal;
    private int idCliente;
    private String dniTrabajador;
    private List<DetallePedido> detalles;

    public Pedido(){}

    public Pedido(int idPedido, LocalDate fecha, EstadoPedido estado, double importeTotal, int idCliente, String dniTrabajador) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.estado = estado;
        this.importeTotal = importeTotal;
        this.idCliente = idCliente;
        this.dniTrabajador = dniTrabajador;
    }

    public Pedido(LocalDate fecha, EstadoPedido estado, double importeTotal, int idCliente, String dniTrabajador) {
        this.fecha = fecha;
        this.estado = estado;
        this.importeTotal = importeTotal;
        this.idCliente = idCliente;
        this.dniTrabajador = dniTrabajador;
        this.detalles = new ArrayList<>();
    }

    // region GETTER & SETTER

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getDniTrabajador() {
        return dniTrabajador;
    }

    public void setDniTrabajador(String dniTrabajador) {
        this.dniTrabajador = dniTrabajador;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    // endregion

    @Override
    public String toString() {
        return "Pedido: " +
                "ID: " + idPedido +
                ", Fecha: " + fecha +
                ", Estado: " + estado.getValue() +
                ", Importe Total: " + importeTotal +
                ", ID Cliente: " + idCliente +
                ", DNI Trabajador: " + dniTrabajador;
    }
}
