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
public class FileServer implements Runnable {

    private static final int FILE_PORT = 12346;
    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public void run() {
        try {
            ServerSocket fileServerSocket = new ServerSocket(FILE_PORT);
            InetAddress serverAddress = InetAddress.getLocalHost();

            System.out.println("\nFile Server iniciado...");
            System.out.println("Direccion IP del servidor de archivos: " + serverAddress.getHostAddress());

            while (true) {
                Socket clientSocket = fileServerSocket.accept();
                System.out.println("Nuevo cliente conectado para transferencia de archivos: " + clientSocket);

                new Thread(new FileHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class FileHandler implements Runnable {

        private Socket clientSocket;
        private DataInputStream in;
        private DataOutputStream out;

        public FileHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String emiterName = in.readUTF();
                String receiverName = in.readUTF();
                String fileName = in.readUTF();
                long fileSize = in.readLong();

                // Crear la carpeta de uploads si no existe
                File dir = new File(UPLOAD_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Guardar el archivo en la carpeta específica en el servidor
                File file = new File(UPLOAD_DIR + fileName);
                FileOutputStream fos = new FileOutputStream(file);

                byte[] buffer = new byte[4096];
                int read = 0;
                long totalRead = 0;
                long remaining = fileSize;

                // Transferencia de archivo
                while ((read = in.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
                    totalRead += read;
                    remaining -= read;
                    fos.write(buffer, 0, read);
                }
                Context.sendNotifyFile(receiverName, fileName, emiterName);
                fos.close();
                System.out.println("Archivo " + fileName + " recibido exitosamente.");

                Archivos archivo = new Archivos(receiverName, emiterName, file.getAbsolutePath());
                Context.listaArchivos.add(archivo);
                // Aquí puedes implementar la lógica para notificar al cliente o hacer algo con el archivo recibido
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
