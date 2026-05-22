package com.m74.proyecto_crm.graphicInterfaces;

import com.m74.proyecto_crm.controllers.DetallePedidoController;
import com.m74.proyecto_crm.controllers.PedidoController;
import com.m74.proyecto_crm.entities.DetallePedido;
import com.m74.proyecto_crm.entities.Pedido;
import com.m74.proyecto_crm.util.InputHelper;

import java.util.ArrayList;
import java.util.List;

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

        int option;

        do{

            System.out.println("\nBIENVENIDO A GESTIÓN DE CLIENTES");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Añadir Cliente");
            System.out.println("2. Ver todos los Clientes");
            System.out.println("3. Ver un Cliente");
            System.out.println("4. Actualizar un Cliente");
            System.out.println("5. Eliminar un Cliente");
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,5);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addClient();
                case 2 -> findAllClients();
                case 3 -> findClientByID();
                case 4 -> updateClient();
                case 5 -> deleteClient();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);
    }

    private void addClient(){

    }

    private void findAllClients(){
    }

    private void findClientByID(){
    }

    private void updateClient(){
    }

    private void deleteClient(){}

    // endregion

    // region WORKER

    private void manageWorker(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A GESTIÓN DE TRABAJADORES");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Añadir Trabajador");
            System.out.println("2. Ver todos los Trabajadores");
            System.out.println("3. Ver un Trabajador");
            System.out.println("4. Actualizar un Trabajador");
            System.out.println("5. Eliminar un Trabajador");
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,5);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addWorker();
                case 2 -> findAllWorkers();
                case 3 -> findWorkerByID();
                case 4 -> updateWorker();
                case 5 -> deleteWorker();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);
    }

    private void addWorker(){

    }

    private void findAllWorkers(){
    }

    private void findWorkerByID(){
    }

    private void updateWorker(){
    }

    private void deleteWorker(){}

    // endregion

    // region SUPPLIER

    private void manageSupplier(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A GESTIÓN DE PROVEEDORES");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Añadir Proveedor");
            System.out.println("2. Ver todos los Proveedor");
            System.out.println("3. Ver un Proveedor");
            System.out.println("4. Actualizar un Proveedor");
            System.out.println("5. Eliminar un Proveedor");
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,5);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addSupplier();
                case 2 -> findAllSuppliers();
                case 3 -> findSupplierByID();
                case 4 -> updateSupplier();
                case 5 -> deleteSupplier();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);
    }

    private void addSupplier(){

    }

    private void findAllSuppliers(){
    }

    private void findSupplierByID(){
    }

    private void updateSupplier(){
    }

    private void deleteSupplier(){}

    // endregion

    // region ALBUM

    private void manageAlbum(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A GESTIÓN DE ÁLBUMES");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Añadir Album");
            System.out.println("2. Ver todos los Álbumes");
            System.out.println("3. Ver un Album");
            System.out.println("4. Actualizar un Album");
            System.out.println("5. Eliminar un Album");
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,5);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addAlbum();
                case 2 -> findAllAlbums();
                case 3 -> findAlbumByID();
                case 4 -> updateAlbum();
                case 5 -> deleteAlbum();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);
    }

    private void addAlbum(){

    }

    private void findAllAlbums(){
    }

    private void findAlbumByID(){
    }

    private void updateAlbum(){
    }

    private void deleteAlbum(){}

    // endregion

    // region ORDER

    private final PedidoController pedidoController = new PedidoController();
    private final DetallePedidoController detallePedidoController = new DetallePedidoController();

    private void manageOrder(){

        int option;

        do{

            System.out.println("\nBIENVENIDO A GESTIÓN DE PEDIDOS");
            System.out.println("¿Qué quieres hacer?");
            System.out.println("1. Añadir Pedido");
            System.out.println("2. Ver todos los Pedidos");
            System.out.println("3. Ver un Pedido");
            System.out.println("4. Actualizar un Pedido");
            System.out.println("5. Eliminar un Pedido");
            System.out.println("0. Volver al menú anterior");
            option = InputHelper.readIntInRange("",0,5);

            switch (option){

                case 0 -> System.out.println("Volviendo");
                case 1 -> addOrder();
                case 2 -> findAllOrders();
                case 3 -> findOrderByID();
                case 4 -> updateOrder();
                case 5 -> deleteOrder();
                default -> System.out.println("La opción seleccionada no es válida, inténtelo de nuevo. \n ");
            }
        }while (option!= 0);
    }

    private void addOrder(){

    }

    private void findAllOrders(){

        List<Pedido> pedidos = pedidoController.findAll();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }

        System.out.println("\n========== PEDIDOS ==========");
        for (Pedido pedido : pedidos) {

            System.out.println("--------------------------------");
            System.out.println("ID:           " + pedido.getIdPedido());
            System.out.println("Fecha:        " + pedido.getFecha());
            System.out.println("Estado:       " + pedido.getEstado().getValue());
            System.out.println("Importe:      " + pedido.getImporteTotal() + " €");
            System.out.println("ID Cliente:   " + pedido.getIdCliente());
            System.out.println("Trabajador:   " + (pedido.getDniTrabajador() != null ? pedido.getDniTrabajador() : "Sin asignar"));

            List<DetallePedido> detalles = detallePedidoController.findByIdPedido(pedido.getIdPedido());

            if (detalles.isEmpty()) {
                System.out.println("Detalles:     Sin detalles");
            } else {
                System.out.println("Detalles:");
                for (DetallePedido detalle : detalles) {
                    System.out.println("  - ID Álbum: " + detalle.getIdAlbum() +
                            " | Cantidad: " + detalle.getCantidad());
                }
            }
        }
        System.out.println("================================\n");

    }

    private void findOrderByID(){
    }

    private void updateOrder(){
    }

    private void deleteOrder(){}

    // endregion

}
