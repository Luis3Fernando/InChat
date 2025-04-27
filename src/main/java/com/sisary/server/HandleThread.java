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
public class HandleThread implements Runnable {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String Username;
    private String AddressIP;

    public HandleThread(Socket socket) {
        this.clientSocket = socket;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String initialMessage = in.readLine();
            String[] parts = initialMessage.split(":");

            if (parts.length == 2) {
                Username = parts[0];
                AddressIP = parts[1];
                System.out.println("User: " + Username + " con IP: " + AddressIP + " connected." + "\n");
            } else {
                System.out.println("Invalid format received from client.");
                return;
            }
            
            Context.NotifyList(Context.getConnectedUsers());

            out.println("Conexion Exitosa");
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                Context.sendMessageToUser(inputLine);
            }
            // Remove the client handler from the list 
            //Context.clients.remove(this);

            // Close the input and output streams and the client socket 
            //in.close();
            //out.close();
            //clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUsername() {
        return Username;
    }

    public String getAddressIP() {
        return AddressIP;
    }
}
