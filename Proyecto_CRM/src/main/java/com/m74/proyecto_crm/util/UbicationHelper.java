package com.m74.proyecto_crm.util;

import com.m74.proyecto_crm.services.UbicationService;

public class UbicationHelper {

    private static final UbicationService ubicationService = new UbicationService();

    public static int solicitarORegistrarUbicacion(String cpInput) {

        int idCp = ubicationService.buscarIdCP(cpInput);
        if (idCp != -1) {
            return idCp;
        }

        System.out.println("[!] El código postal no está registrado");

        String ciudad = InputHelper.readString("-> Ciudad: ");
        String provincia = InputHelper.readString("-> Provincia: ");
        String pais = "";

        while (true) {
            pais = InputHelper.readString("-> Código de País (Debe ser de 2 letras, ej: ES, US, GB): ").toUpperCase().trim();

            if (pais.length() == 2) {
                break;
            }

            System.out.println("[!] Error: El código de país debe tener EXACTAMENTE 2 caracteres");
        }

        if (!ubicationService.verificarExistePais(pais)) {
            System.out.println("\n[!] El código de país no está registrado");
            String nombrePais = InputHelper.readString("-> Introduce el nombre completo del País (Ej: España): ");

            boolean paisCreado = ubicationService.registrarPais(pais, nombrePais);
            if (!paisCreado) {
                System.out.println("[-] Error crítico al registrar el país. Proceso cancelado.\n");
                return -1;
            }
        }

        idCp = ubicationService.registrarCodigoPostal(cpInput, ciudad, provincia, pais);

        return idCp;
    }
}
