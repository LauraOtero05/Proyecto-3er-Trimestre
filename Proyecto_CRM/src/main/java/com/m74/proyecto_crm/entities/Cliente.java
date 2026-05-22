package com.m74.proyecto_crm.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cliente {
    private int idCliente;
    private String nombre;
    private String apellido;
    private String direccion;
    private int idCodigoPostal;
    private String codigoPostal;
    private String email;
    private String passwordHash;
    private List<String> telefonos;

    public Cliente() {
        this.telefonos = new ArrayList<>();
    }

    public Cliente(int idCliente, String nombre, String apellido, String direccion,
                   int idCodigoPostal, String codigoPostal, String email, String passwordHash) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.idCodigoPostal = idCodigoPostal;
        this.codigoPostal = codigoPostal;
        this.email = email;
        this.passwordHash = passwordHash;
        this.telefonos = new ArrayList<>();
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdCodigoPostal() {
        return idCodigoPostal;
    }

    public void setIdCodigoPostal(int idCodigoPostal) {
        this.idCodigoPostal = idCodigoPostal;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }

    public void addTelefono(String telefono) {
        if (telefono != null && !telefono.trim().isEmpty()) this.telefonos.add(telefono);
    }

    @Override
    public String toString() {
        return "Cliente:" +
                " idCliente: " + idCliente +
                ", nombre: " + nombre +
                ", apellido: " + apellido +
                ", direccion: " + direccion +
                ", codigoPostal: " + codigoPostal +
                ", email: " + email +
                ", telefonos: " + telefonos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return idCliente == cliente.idCliente &&
                idCodigoPostal == cliente.idCodigoPostal &&
                Objects.equals(nombre, cliente.nombre) &&
                Objects.equals(apellido, cliente.apellido) &&
                Objects.equals(email, cliente.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente, nombre, apellido, email, idCodigoPostal);
    }
}
