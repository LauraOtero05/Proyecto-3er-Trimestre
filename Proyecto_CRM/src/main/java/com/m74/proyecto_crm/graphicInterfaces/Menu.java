package com.m74.proyecto_crm.graphicInterfaces;

import com.m74.proyecto_crm.util.InputHelper;

public class Menu {

    public void start(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A 74 MINUTES");
            System.out.println("¿Qué quiere gestionar?");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Gestionar Trabajadores");
            System.out.println("3. Gestionar Proveedores");
            System.out.println("4. Gestionar Álbumes");
            System.out.println("5. Gestionar Pedidos");
            System.out.println("0. SALIR");
            option = InputHelper.readIntInRange("",0,5);

            switch (option){

                case 0 -> System.out.println("Saliendo");
                case 1 -> manageClient();
                case 2 -> manageWorker();
                case 3 -> manageAlbum();
                case 4 -> manageSupplier();
                case 5 -> manageOrder();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);

    }

    // region CLIENT

    private void manageClient(){

    }

    // endregion

    // region WORKER

    private void manageWorker(){

    }

    // endregion

    // region SUPPLIER

    private void manageSupplier(){

    }

    // endregion

    // region ALBUM

    private void manageAlbum(){

    }

    // endregion

    // region ORDER

    private void manageOrder(){

    }

    // endregion

}
