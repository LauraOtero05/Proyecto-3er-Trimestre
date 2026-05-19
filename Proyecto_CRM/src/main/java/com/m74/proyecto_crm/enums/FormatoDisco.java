package com.m74.proyecto_crm.enums;

public enum FormatoDisco {

    CD("CD"),
    VINILO("Vinilo"),
    CASSETTE("Cassette"),
    DIGITAL("Digital");    ;

    private final String value;

    FormatoDisco(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FormatoDisco fromString(String texto) {
        for (FormatoDisco formato : FormatoDisco.values()) {
            if (formato.value.equalsIgnoreCase(texto)) {
                return formato;
            }
        }
        throw new IllegalArgumentException("Formato desconocido: " + texto);
    }
}
