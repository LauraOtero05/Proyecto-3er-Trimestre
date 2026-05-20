package com.m74.proyecto_crm.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proveedor {

    private int idProveedor;
    private String nombre;
    private String direccion;
    private int idCodigoPostal;
    private String codigoPostal;
    private List<String> telefonos;
    private List<String> emails;

    public Proveedor() {
        this.telefonos = new ArrayList<>();
        this.emails = new ArrayList<>();
    }

    public Proveedor(int idProveedor, String nombre, String direccion, int idCodigoPostal, String codigoPostal) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.direccion = direccion;
        this.idCodigoPostal = idCodigoPostal;
        this.codigoPostal = codigoPostal;
        this.telefonos = new ArrayList<>();
        this.emails = new ArrayList<>();
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public List<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public void addTelefono(String telefono) {
        if (telefono != null && !telefono.trim().isEmpty()) this.telefonos.add(telefono);
    }

    public void addEmail(String email) {
        if (email != null && !email.trim().isEmpty()) this.emails.add(email);
    }

    @Override
    public String toString() {
        return  "\nId Proveedor:    " + idProveedor + "\n" +
                "Nombre:          " + nombre +  "\n" +
                "Direccion:       " + direccion +
                ", Código Postal: " + codigoPostal +  "\n" +
                "Teléfonos:       " + telefonos +  "\n" +
                "E-mails:         " + emails + "\n" +
                "---------------------";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Proveedor proveedor = (Proveedor) o;
        return idProveedor == proveedor.idProveedor && idCodigoPostal == proveedor.idCodigoPostal && Objects.equals(nombre, proveedor.nombre) && Objects.equals(direccion, proveedor.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProveedor, nombre, direccion, idCodigoPostal);
    }


}
