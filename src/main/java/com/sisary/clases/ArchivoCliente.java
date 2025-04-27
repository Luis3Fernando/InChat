/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.clases;

/**
 *
 * @author Luis Fernando
 */
public class ArchivoCliente {

    private String emisor;
    private String nombreArchivo;

    public ArchivoCliente(String emisor, String nombreArchivo) {
        this.emisor = emisor;
        this.nombreArchivo = nombreArchivo;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    @Override
    public String toString() {
        return "ArchivoCliente{"
                + "emisor='" + emisor + '\''
                + ", nombreArchivo='" + nombreArchivo + '\''
                + '}';
    }
}
