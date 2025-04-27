/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.client;

import com.sisary.clases.Global;
import java.io.*;
import java.net.*;

public class FileClient {

    private String serverAddress;
    private int port;
    private File file;
    private byte[] bytes;
    private String receiverName;

    public FileClient(String serverAddress, int port, File file, String receiverName) {
        this.serverAddress = "192.168.169.199";
        this.port = 12346;
        this.file = file;
        this.bytes = new byte[16 * 1024];
        this.receiverName = receiverName;
    }

    public void sendFile() {
        try (
                Socket socket = new Socket(serverAddress, port); DataOutputStream out = new DataOutputStream(socket.getOutputStream()); FileInputStream fis = new FileInputStream(file)) {
            out.writeUTF(Global.usuarioActual.getNombreUsuario());
            // Enviar el nombre del receptor
            out.writeUTF(receiverName);

            // Enviar el nombre del archivo
            out.writeUTF(file.getName());

            // Enviar el tamaño del archivo
            out.writeLong(file.length());

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            System.out.println("Archivo " + file.getName() + " enviado exitosamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
