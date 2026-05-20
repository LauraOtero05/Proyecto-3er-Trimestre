package com.m74.proyecto_crm.entities;

import com.m74.proyecto_crm.enums.RolTrabajador;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Trabajador {
    private String dni;
    private String nombre;
    private String apellido;
    private RolTrabajador rol;
    private String email;
    private String passwordHash;
    private List<String> telefonos;

    public Trabajador() {
        this.telefonos = new ArrayList<>();
    }

    public Trabajador(String dni, String nombre, String apellido, RolTrabajador rol,
                      String email, String passwordHash) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.email = email;
        this.passwordHash = passwordHash;
        this.telefonos = new ArrayList<>();
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public RolTrabajador getRol() {
        return rol;
    }

    public void setRol(RolTrabajador rol) {
        this.rol = rol;
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
        return "Trabajador:" +
                " DNI: " + dni +
                ", nombre: " + nombre +
                ", apellido: " + apellido +
                ", rol: " + rol.getValue() +
                ", email: " + email +
                ", telefonos: " + telefonos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Trabajador trabajador = (Trabajador) o;
        return Objects.equals(dni, trabajador.dni) &&
                Objects.equals(nombre, trabajador.nombre) &&
                Objects.equals(apellido, trabajador.apellido) &&
                Objects.equals(email, trabajador.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni, nombre, apellido, email);
    }
}
