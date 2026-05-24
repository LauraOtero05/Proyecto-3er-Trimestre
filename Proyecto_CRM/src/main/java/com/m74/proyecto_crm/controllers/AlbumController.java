package com.m74.proyecto_crm.controllers;

import com.m74.proyecto_crm.entities.Album;
import com.m74.proyecto_crm.entities.Proveedor;
import com.m74.proyecto_crm.services.AlbumService;
import com.m74.proyecto_crm.services.ProveedorService;

import java.util.List;

public class AlbumController {

    private final AlbumService albumService;
    private final ProveedorService proveedorService;

    public AlbumController() {
        this.albumService = new AlbumService();
        this.proveedorService = new ProveedorService();
    }

    public void crearAlbum(Album nuevo) {
        albumService.crearAlbum(nuevo);
    }

    public List<Album> listarAlbumes() {
        return albumService.obtenerTodosLosAlbumes();
    }

    public void eliminarAlbum(int id) {
        albumService.eliminarAlbum(id);
    }

    public void exportarTxt(String nombreArchivo) {
        albumService.exportarAlbumesATxt(nombreArchivo);
    }

    public List<Album> listarAlbumesParaPedido() {
        return albumService.obtenerTodosLosAlbumes();
    }

    public Album obtenerAlbumPorId(int id) {
        return albumService.obtenerAlbumPorId(id);
    }

    public void actualizarAlbum(Album album) {
        albumService.actualizarAlbum(album);
    }

    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorService.obtenerTodosLosProveedores();
    }

    public Proveedor obtenerProveedorPorIdONombre(String criterio) {
        return proveedorService.obtenerProveedorPorIdONombre(criterio);
    }
}
