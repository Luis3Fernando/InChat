/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.server;

/**
 *
 * @author Luis Fernando
 */
public class Archivos {

    private String receptor;
    private String emisor;
    private String ubicacion;

    public Archivos(String receptor, String emisor, String ubicacion) {
        this.receptor = receptor;
        this.emisor = emisor;
        this.ubicacion = ubicacion;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString() {
        return "Archivo{" +
                "receptor='" + receptor + '\'' +
                ", emisor='" + emisor + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }
}

