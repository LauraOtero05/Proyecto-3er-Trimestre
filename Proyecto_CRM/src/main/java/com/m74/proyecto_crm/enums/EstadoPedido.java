package com.m74.proyecto_crm.enums;

public enum EstadoPedido {

    PENDIENTE("Pendiente"),
    ENVIADO("Enviado"),
    ENTREGADO("Entregado"),
    PAGADO("Pagado"),
    CANCELADO("Cancelado");

    private final String value;

    private EstadoPedido(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EstadoPedido fromString(String texto) {
        for (EstadoPedido estado : EstadoPedido.values()) {
            if (estado.value.equalsIgnoreCase(texto)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado desconocido: " + texto);
    }
}
