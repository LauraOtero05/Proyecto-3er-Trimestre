package com.m74.proyecto_crm.entities;

public class DetallePedido {

    private int idDetalle;
    private int idPedido;
    private int idAlbum;
    private int cantidad;

    public DetallePedido() {}

    public DetallePedido(int idPedido, int idAlbum, int cantidad) {
        this.idPedido = idPedido;
        this.idAlbum = idAlbum;
        this.cantidad = cantidad;
    }

    public DetallePedido(int idDetalle, int idPedido, int idAlbum, int cantidad) {
        this.idDetalle = idDetalle;
        this.idPedido = idPedido;
        this.idAlbum = idAlbum;
        this.cantidad = cantidad;
    }

    // region GETTER & SETTER

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    // endregion

    @Override
    public String toString() {
        return "Detalle Pedido:" +
                "ID: " + idDetalle +
                ", ID Pedido: " + idPedido +
                ", ID Album: " + idAlbum +
                ", Cantidad: " + cantidad;
    }
}
