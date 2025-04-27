/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.clases;

import java.io.Serializable;

/**
 *
 * @author Luis Fernando
 */
public class Usuario implements Serializable {
    private String nombreUsuario;
    private String direccionIP;

    public Usuario(String nombreUsuario, String direccionIP) {
        this.nombreUsuario = nombreUsuario;
        this.direccionIP = direccionIP;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getDireccionIP() {
        return direccionIP;
    }

    @Override
    public String toString() {
        return nombreUsuario + " (" + direccionIP + ")";
    }
}