/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.client;

import com.sisary.clases.ArchivoCliente;
import com.sisary.clases.Conversacion;
import com.sisary.clases.Global;
import com.sisary.clases.Mensaje;
import com.sisary.clases.Usuario;
import java.io.*;
import java.net.Socket;

public class Cliente {

    private Socket socket;
    private boolean error = false;

    public Cliente(String ipServidor, int puerto, Usuario usuario) {
        try {
            // Intentar conectarse al servidor
            socket = new Socket(ipServidor, puerto);
            Global.conexion = socket;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Enviar el usuario y la dirección IP al servidor en el formato: username:addressIP
            String infoUser = usuario.getNombreUsuario() + ":" + usuario.getDireccionIP();
            out.println(infoUser);

            // Hilo para recibir actualizaciones de la lista de usuarios
            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        handleServerMessage(serverResponse);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            error = true;
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }
    }
    
    public boolean ExistsError(){
        return error;
    }

    private void handleServerMessage(String message) {
        if (message.contains(";")) {
            // El mensaje contiene una lista de usuarios con formato usuario:ip
            String[] entries = message.split(";\\s*"); // Divide por `;` y elimina espacios opcionales

            // Iterar sobre cada entrada en la lista
            for (String entry : entries) {
                if (entry.contains(":")) {
                    String[] parts = entry.split(":");
                    if (parts.length == 2) {
                        // Crear un nuevo objeto Usuario
                        Usuario nuevoUsuario = new Usuario(parts[0], parts[1]);

                        // Verificar si el usuario ya está en la lista
                        boolean usuarioExiste = false;
                        for (Usuario usuario : Global.allUsers) {
                            if (usuario.getNombreUsuario().equals(nuevoUsuario.getNombreUsuario())) {
                                usuarioExiste = true;
                                break;
                            }
                        }

                        // Si el usuario no existe en la lista, agregarlo
                        if (!usuarioExiste) {
                            Global.allUsers.add(nuevoUsuario);
                        }
                    } else {
                        System.out.println("Formato de entrada inválido: " + entry);
                    }
                }
            }

            Global.updateUsersPanels();
        } else if (message.contains(":")) {
            // El mensaje contiene un solo usuario con formato usuario:ip
            String[] parts = message.split(":");
            if (parts.length == 2) {
                // Crear un nuevo objeto Usuario
                Usuario nuevoUsuario = new Usuario(parts[0], parts[1]);

                // Verificar si el usuario ya está en la lista
                boolean usuarioExiste = false;
                for (Usuario usuario : Global.allUsers) {
                    if (usuario.getNombreUsuario().equals(nuevoUsuario.getNombreUsuario())) {
                        usuarioExiste = true;
                        break;
                    }
                }

                // Si el usuario no existe en la lista, agregarlo
                if (!usuarioExiste) {
                    Global.allUsers.add(nuevoUsuario);
                }

                Global.updateUsersPanels();
            } else {
                System.out.println("Formato de mensaje inválido: " + message);
            }
        } else if (message.contains("/")) {
            // El mensaje contiene el formato emisor/mensaje
            String[] parts = message.split("/", 2);
            if (parts.length == 2) {
                String senderUsername = parts[0];
                String messageContent = parts[1];

                // Buscar la conversación con el receptor
                Conversacion conversacion = null;
                for (Conversacion c : Global.conversaciones) {
                    if (c.getContacto().getNombreUsuario().equals(senderUsername)) {
                        conversacion = c;
                        break;
                    }
                }

                // Si encontramos la conversación, agregar el mensaje
                if (conversacion != null) {
                    Mensaje nuevoMensaje = new Mensaje(messageContent, conversacion.getContacto(), Global.usuarioActual);
                    conversacion.agregarMensaje(nuevoMensaje);
                    if (Global.usuarioReceptor.getNombreUsuario().equals(senderUsername)) {
                        Global.updateChatPanel(conversacion);
                        Global.soundChat();
                    }
                    
                    else{
                        Global.markUserPanelWithMessage(senderUsername);
                        Global.soundMessage();
                    }
                } else {
                    System.out.println("No se encontró una conversación con el usuario: " + senderUsername);
                }
            } else {
                System.out.println("Formato de mensaje inválido: " + message);
            }
        } else if (message.contains("#")) {
            // El mensaje contiene una notificación de archivo en formato emisor#nombreArchivo
            String[] parts = message.split("#", 2);
            if (parts.length == 2) {
                String emisor = parts[0];
                String nombreArchivo = parts[1];

                // Crear un nuevo objeto ArchivoCliente
                ArchivoCliente archivoCliente = new ArchivoCliente(emisor, nombreArchivo);

                // Agregar el archivo a la lista de archivos en Global
                Global.archivosCliente.add(archivoCliente);
                Global.updateFilesPanels();
            } else {
                System.out.println("Formato de mensaje de archivo inválido: " + message);
            }
        } else {
            // Si no es del formato usuario:ip, es un mensaje normal
            System.out.println("Mensaje: " + message);
        }
    }
}
