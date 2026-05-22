package com.m74.proyecto_crm.services;

import com.m74.proyecto_crm.entities.*;
import com.m74.proyecto_crm.repositories.PedidoRepository;
import com.m74.proyecto_crm.repositories.PedidoRepositoryImpl;
import com.m74.proyecto_crm.util.DataBaseConnection;

import java.sql.*;
import java.util.List;

public class PedidoService {

    private final PedidoRepository pedidoRepository = new PedidoRepositoryImpl();
    private final DetallePedidoService detallePedidoService = new DetallePedidoService();
    private final AlbumService albumService = new AlbumService();
    private final ClienteService clienteService = new ClienteService();
    private final TrabajadorService trabajadorService = new TrabajadorService();

    public Pedido findById(int id) {
        Pedido pedido = pedidoRepository.findById(id);

        if (pedido != null) {
            List<DetallePedido> detalles = detallePedidoService.findByIdPedido(id);
            pedido.setDetalles(detalles);
        }

        return pedido;
    }

    public List<Pedido> findAll() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        for (Pedido pedido : pedidos) {
            List<DetallePedido> detalles = detallePedidoService.findByIdPedido(pedido.getIdPedido());
            pedido.setDetalles(detalles);
        }

        return pedidos;
    }

    public List<Cliente> findAllClientes() {
        return clienteService.obtenerTodosLosClientes();
    }

    public List<Trabajador> findAllTrabajadores() {
        return trabajadorService.obtenerTodosLosTrabajadores();
    }

    public void save(Pedido pedido) {
        int idPedido = pedidoRepository.save(pedido);
        if (idPedido == -1) System.out.println("Error al crear el pedido.");
    }

    public void update(Pedido pedido) {
        pedidoRepository.update(pedido);
    }

    public void deleteById(int id) {
        pedidoRepository.deleteById(id);
    }

    public void generateTxtOrder(String rutaArchivo){

        List<Pedido> pedidos = pedidoRepository.findAll();

        for (Pedido pedido : pedidos) {
            pedido.setDetalles(detallePedidoService.findByIdPedido(pedido.getIdPedido()));
        }

        if (pedidos.isEmpty()) {
            System.out.println("\nNo hay datos en la base de datos para exportar.");
            return;
        }

        java.io.File archivo = new java.io.File(rutaArchivo);

        try (java.io.FileWriter fw = new java.io.FileWriter(archivo);
             java.io.BufferedWriter bw = new java.io.BufferedWriter(fw)) {

            bw.write("============================================================== ");
            bw.write("          SITUACIÓN ACTUAL DE LOS PEDIDOS - 74_MINUTES         ");
            bw.write(" ============================================================= ");
            bw.newLine();

            for (Pedido pedido : pedidos) {
                bw.write("----------------------------------------");
                bw.newLine();
                bw.write("Pedido #" + pedido.getIdPedido());
                bw.newLine();
                bw.write("Fecha:      " + pedido.getFecha());
                bw.newLine();
                bw.write("Estado:     " + pedido.getEstado().getValue());
                bw.newLine();
                bw.write("Importe:    " + String.format("%.2f", pedido.getImporteTotal()) + " €");
                bw.newLine();
                bw.write("Cliente:    ID " + pedido.getIdCliente());
                bw.newLine();
                bw.write("Trabajador: " + (pedido.getDniTrabajador() != null ? pedido.getDniTrabajador() : "Sin asignar"));
                bw.newLine();

                List<DetallePedido> detalles = pedido.getDetalles();
                if (detalles == null || detalles.isEmpty()) {
                    bw.write("Detalles:   Sin detalles");
                    bw.newLine();
                } else {
                    bw.write("Detalles:");
                    bw.newLine();
                    for (DetallePedido d : detalles) {
                        try {
                            Album a = albumService.obtenerAlbumPorId(d.getIdAlbum());
                            String titulo = a != null ? a.getTitulo() : "ID " + d.getIdAlbum();
                            double subtotal = a != null ? a.getPrecio() * d.getCantidad() : 0;
                            bw.write("  - " + titulo +
                                    " | Cantidad: " + d.getCantidad() +
                                    " | Subtotal: " + String.format("%.2f", subtotal) + " €");
                            bw.newLine();
                        } catch (Exception e) {
                            bw.write("  - ID Álbum: " + d.getIdAlbum() + " | Cantidad: " + d.getCantidad());
                            bw.newLine();
                        }
                    }
                }
                bw.newLine();
            }

            bw.write("========================= ");
            bw.write("  Total de pedidos: " + pedidos.size());
            bw.write("======================== ");

            System.out.println("Exportación en proceso. Archivo guardado en: " + archivo.getAbsolutePath());

        } catch (java.io.IOException e) {
            System.err.println("\nError al escribir el archivo: " + e.getMessage());
        }

    }
}
