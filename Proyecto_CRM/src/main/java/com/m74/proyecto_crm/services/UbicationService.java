package com.m74.proyecto_crm.services;

import com.m74.proyecto_crm.repositories.UbicationRepositoryImpl;

import java.sql.SQLException;

public class UbicationService {

    private final UbicationRepositoryImpl ubicationRepository = new UbicationRepositoryImpl();

    public int buscarIdCP(String cp) {
        try {
            return ubicationRepository.obtenerIdPorCodigoPostal(cp);
        } catch (SQLException e) {
            return -1;
        }
    }

    public boolean verificarExistePais(String codigoPais) {
        try {
            return ubicationRepository.existePais(codigoPais);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean registrarPais(String codigoPais, String nombrePais) {
        try {
            ubicationRepository.crearPais(codigoPais, nombrePais);
            System.out.println("¡País [" + nombrePais + "] registrado correctamente!");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar el país: " + e.getMessage());
            return false;
        }
    }

    public int registrarCodigoPostal(String cp, String ciudad, String provincia, String pais) {
        try {
            int nuevoId = ubicationRepository.crearCodigoPostal(cp, ciudad, provincia, pais);
            System.out.println("¡Código postal [" + cp + "] creado con éxito!");
            return nuevoId;
        } catch (SQLException e) {
            System.err.println("Error al crear el código postal: " + e.getMessage());
            return -1;
        }
    }
}
