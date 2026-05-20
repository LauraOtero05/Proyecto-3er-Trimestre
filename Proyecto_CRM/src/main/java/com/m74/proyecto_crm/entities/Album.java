package com.m74.proyecto_crm.entities;

import com.m74.proyecto_crm.enums.FormatoDisco;

import java.util.Objects;

public class Album {

    private int idProducto;
    private String titulo;
    private String artista;
    private FormatoDisco formato;
    private double precio;
    private int stock;
    private int idProveedor;
    private String nombreProveedor;
    private int idGenero;
    private String nombreGenero;

    public Album() {}

    public Album(int idProducto, String titulo, String artista, FormatoDisco formato, double precio, int stock, int idProveedor, String nombreProveedor, int idGenero, String nombreGenero) {
        this.idProducto = idProducto;
        this.titulo = titulo;
        this.artista = artista;
        this.formato = formato;
        this.precio = precio;
        this.stock = stock;
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
        this.idGenero = idGenero;
        this.nombreGenero = nombreGenero;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public FormatoDisco getFormato() {
        return formato;
    }

    public void setFormato(FormatoDisco formato) {
        this.formato = formato;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getNombreGenero() {
        return nombreGenero;
    }

    public void setNombreGenero(String nombreGenero) {
        this.nombreGenero = nombreGenero;
    }

    @Override
    public String toString() {
        return  "Id Album: " + idProducto +
                ", Titulo: " + titulo +
                ", Artista: " + artista +
                ", Formato: " + formato +
                ", Precio: " + precio + " €" +
                ", Stock: " + stock +
                ", Proveedor: " + nombreProveedor +
                ", Género: " + nombreGenero;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return idProducto == album.idProducto && Objects.equals(titulo, album.titulo) && Objects.equals(artista, album.artista) && formato == album.formato;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, titulo, artista, formato);
    }
}
