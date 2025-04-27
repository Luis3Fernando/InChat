/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.server;

import java.io.*;
import java.net.*;

/**
 *
 * @author Luis Fernando
 */
public class Servidor {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            Thread fileServerDownload = new Thread(new ServerFileDownload());
            fileServerDownload.start();
            
            Thread fileServerThread = new Thread(new FileServer());
            fileServerThread.start();

            ServerSocket serverSocket = new ServerSocket(PORT);
            InetAddress serverAddress = InetAddress.getLocalHost();

            System.out.println("Servidor iniciado...");
            System.out.println("Direccion IP del servidor: " + serverAddress.getHostAddress());

            // Accept incoming connections 
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clientSocket);

                // Create a new client handler for the connected client 
                HandleThread clientHandler = new HandleThread(clientSocket);
                Context.clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
