package com.m74.proyecto_crm.enums;

public enum RolTrabajador {
    ADMINISTRADOR("Administrador"),
    VENTAS("Ventas"),
    ALMACEN("Almacen"),
    GERENTE("Gerente");

    private final String value;

    RolTrabajador(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RolTrabajador fromString(String texto) {
        for (RolTrabajador rol : RolTrabajador.values()) {
            if (rol.value.equalsIgnoreCase(texto)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("Rol desconocido: " + texto);
    }
}
