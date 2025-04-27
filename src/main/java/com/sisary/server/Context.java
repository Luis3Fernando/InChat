/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Luis Fernando
 */
public class Context {

    public static CopyOnWriteArrayList<HandleThread> clients = new CopyOnWriteArrayList<>();
    public static List<Archivos> listaArchivos = new ArrayList<>();

    public static void broadcast(String message, HandleThread sender) {
        for (HandleThread client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public static String getConnectedUsers() {
        StringBuilder userList = new StringBuilder();
        for (HandleThread client : clients) {
            if (userList.length() > 0) {
                userList.append("; ");
            }
            userList.append(client.getUsername()).append(":").append(client.getAddressIP());
        }
        return userList.toString();
    }

    public static void NotifyList(String message) {
        for (HandleThread client : clients) {
            client.sendMessage(message);
        }
    }

    public static void sendMessageToUser(String fullMessage) {
        String[] parts = fullMessage.split(":", 3);
        if (parts.length == 3) {
            String senderUsername = parts[0];
            String message = parts[1];
            String receiverUsername = parts[2];

            for (HandleThread client : clients) {
                if (client.getUsername().equals(receiverUsername)) {
                    client.sendMessage(senderUsername + "/" + message);
                    return;
                }
            }
            // Opcional: Manejar el caso en que el receptor no esté conectado
            System.out.println("Usuario receptor no encontrado: " + receiverUsername);
        } else {
            // Manejar el caso en que el mensaje no tiene el formato correcto
            System.out.println("Formato de mensaje inválido: " + fullMessage);
        }
    }

    public static void sendNotifyFile(String receiverUsername, String Filename, String emiterUsername) {
        for (HandleThread client : clients) {
            if (client.getUsername().equals(receiverUsername)) {
                client.sendMessage(emiterUsername + "#" + Filename);
                return;
            }
        }
    }
}
