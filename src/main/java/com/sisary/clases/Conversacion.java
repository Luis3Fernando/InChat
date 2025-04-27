/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.clases;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Luis Fernando
 */
public class Conversacion {

    private Usuario contacto;
    private List<Mensaje> mensajes;

    public Conversacion(Usuario contacto) {
        this.contacto = contacto;
        this.mensajes = new ArrayList<>();
    }

    // Método para agregar un mensaje a la conversación
    public void agregarMensaje(Mensaje mensaje) {
        this.mensajes.add(mensaje);
    }

    // Getters y setters
    public Usuario getContacto() {
        return contacto;
    }

    public void setContacto(Usuario contacto) {
        this.contacto = contacto;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
}
