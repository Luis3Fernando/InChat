/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.client;

import com.sisary.clases.ArchivoCliente;
import java.io.*;
import java.io.OutputStream;
import java.net.*;

/**
 *
 * @author Luis Fernando
 */
public class FileDownloadHandler {

    private String archivo_nombre;

    public FileDownloadHandler(String archivo_nombre) {
        this.archivo_nombre = archivo_nombre;
    }

    public void sendDownloadRequest(String destinoArchivo) {
        try {
            Socket socket = new Socket("192.168.169.199", 2222); // Puerto del servidor de archivos
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println("DOWNLOAD/" + this.archivo_nombre); // Enviar solicitud

            // Crear un nuevo hilo para recibir el archivo y guardarlo en la ubicación especificada
            new Thread(() -> receiveFile(destinoArchivo, socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(String destinoArchivo, Socket socket) {
        try (InputStream inputStream = socket.getInputStream(); FileOutputStream fileOutputStream = new FileOutputStream(destinoArchivo)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("Archivo descargado en: " + destinoArchivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
