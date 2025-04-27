/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.clases;

import javax.swing.JPanel;

/**
 *
 * @author Luis Fernando
 */
public class PanelUsuario {

    private String nombreUsuario;
    private JPanel PanelPrincipal;

    public PanelUsuario(String nombreUsuario, JPanel panel) {
        this.PanelPrincipal = panel;
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public JPanel getPanelPrincipal() {
        return PanelPrincipal;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.PanelPrincipal = panelPrincipal;
    }
}
