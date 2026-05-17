package com.m74.proyecto_crm.graphicInterfaces;

import com.m74.proyecto_crm.util.DataBaseConnection;
import com.m74.proyecto_crm.util.InputHelper;

public class DataBaseView {

    public static void connection(){

        while (true) {
            int port = InputHelper.readInt("Introduce tu puerto de MySQL (Puerto por defecto: 3306): ");
            String user = InputHelper.readString("Introduce tu usuario de MySQL (Usuario administrador: root): ");
            String password = InputHelper.readString("Introduce la contraseña de " + user + ": ");

            boolean ok = DataBaseConnection.start(port, user, password);

            if (ok) {

                Menu menu = new Menu();
                menu.start();
                break;

            } else {

                System.out.println("\nNo se pudo conectar a la base de datos.");

                System.out.println("\n1. Volver a intentarlo");
                int opcion = InputHelper.readIntInRange("0. Salir\n", 0, 1);

                if (opcion == 0) {

                    System.out.println("Saliendo...");
                    break;

                }
            }
        }
    }
}
