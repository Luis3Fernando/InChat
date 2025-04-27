/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.clases;

import com.sisary.client.Cliente;
import com.sisary.client.FileDownloadHandler;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Luis Fernando
 */
public class Global {

    public static Usuario usuarioActual;
    public static Cliente clienteActual;
    public static Usuario usuarioReceptor;
    public static Socket conexion;

    public static List<Usuario> allUsers = new ArrayList<>();
    public static List<Conversacion> conversaciones = new ArrayList<>();
    public static List<ArchivoCliente> archivosCliente = new ArrayList<>();
    public static List<PanelUsuario> panelesUsuarios = new ArrayList<>();

    public static JScrollPane scrollUsers = new JScrollPane();
    public static JScrollPane scrollChats = new JScrollPane();
    public static JScrollPane scrollArchivos = new JScrollPane();

    public static JLabel direccionIpExterno = new JLabel();
    public static JLabel nombreusuarioExterno1 = new JLabel();
    public static JLabel nombreusuarioExterno2 = new JLabel();

    public static void updateUsersPanels() {
        // Crear el panel contenedor
        JPanel userPanelContainer = new JPanel();
        userPanelContainer.setLayout(new BoxLayout(userPanelContainer, BoxLayout.Y_AXIS));
        userPanelContainer.setBackground(new Color(29, 29, 29)); // Fondo azul para el contenedor
        userPanelContainer.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir borde para el contenedor
        panelesUsuarios.clear();
        // Iterar sobre la lista de usuarios y agregar paneles
        for (Usuario usuario : allUsers) {
            if (Global.usuarioActual.getNombreUsuario().equals(usuario.getNombreUsuario())) {
                continue;
            } else {
                JPanel userPanel = new JPanel();
                userPanel.setLayout(new BorderLayout()); // Usar BorderLayout para centrar el JLabel
                userPanel.setBackground(new Color(29, 29, 29)); // Fondo amarillo para los paneles de usuario
                userPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY)); // Borde inferior negro
                userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Altura fija para los paneles

                // Crear y agregar el JLabel con el nombre de usuario
                JLabel userLabel = new JLabel(usuario.getNombreUsuario());
                userLabel.setForeground(Color.white);
                userLabel.setFont(new Font("Lato", Font.PLAIN, 14));
                userLabel.setHorizontalAlignment(SwingConstants.LEFT); // Centrar el texto
                userPanel.add(userLabel, BorderLayout.CENTER);

                // Agregar un MouseListener al panel para manejar el clic
                userPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        showChatPanel(usuario);
                        updateFilesPanels();
                        unmarkUserPanelWithMessage(usuario.getNombreUsuario());
                        nombreusuarioExterno1.setText(usuario.getNombreUsuario());
                        nombreusuarioExterno2.setText(usuario.getNombreUsuario());
                        direccionIpExterno.setText("Direccion IP: " + usuario.getDireccionIP());
                    }
                });

                userPanelContainer.add(userPanel);
                PanelUsuario panelusuario = new PanelUsuario(usuario.getNombreUsuario(), userPanel);
                panelesUsuarios.add(panelusuario);
            }
        }

        // Ajustar el tamaño del contenedor
        userPanelContainer.setPreferredSize(new Dimension(userPanelContainer.getPreferredSize().width, 50 * allUsers.size()));

        // Actualizar el contenido del ScrollPane
        scrollUsers.setViewportView(userPanelContainer);
        scrollUsers.revalidate(); // Forzar a que el componente se redibuje
        scrollUsers.repaint();    // Forzar el redibujado del componente
    }

    public static void unmarkUserPanelWithMessage(String nombreUsuario) {
        for (PanelUsuario panelUsuario : panelesUsuarios) {
            if (panelUsuario.getNombreUsuario().equals(nombreUsuario)) {
                JPanel userPanel = panelUsuario.getPanelPrincipal();

                // Buscar el panel que contiene el indicador verde
                for (Component comp : userPanel.getComponents()) {
                    if (comp instanceof JPanel) {
                        JPanel rightPanel = (JPanel) comp;
                        if (rightPanel.getComponentCount() > 0 && rightPanel.getComponent(0).getBackground().equals(new Color(0x00FF00))) {
                            // Eliminar el panel del indicador de mensaje
                            userPanel.remove(rightPanel);
                            break; // Salir del bucle una vez que se encuentra y elimina el panel
                        }
                    }
                }

                // Actualizar el panel para que se muestre el cambio
                userPanel.revalidate();
                userPanel.repaint();
                break; // Salir del bucle una vez que se encuentra el usuario
            }
        }
    }

    public static void markUserPanelWithMessage(String nombreUsuario) {
        for (PanelUsuario panelUsuario : panelesUsuarios) {
            if (panelUsuario.getNombreUsuario().equals(nombreUsuario)) {
                JPanel userPanel = panelUsuario.getPanelPrincipal();

                // Crear el panel verde de 20x20
                JPanel messageIndicator = new JPanel();
                messageIndicator.setBackground(new Color(0x00FF00)); // Color verde
                messageIndicator.setPreferredSize(new Dimension(20, 20));

                // Crear un panel contenedor con FlowLayout centrado
                JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                rightPanel.setPreferredSize(new Dimension(30, userPanel.getHeight())); // Asegurar que se ajuste al tamaño del panel padre
                rightPanel.setOpaque(false); // Hacer transparente para que no cubra el color de fondo del panel principal

                // Añadir el indicador de mensaje al contenedor
                rightPanel.add(messageIndicator);

                // Añadir el panel contenedor al lado derecho del panel del usuario
                userPanel.add(rightPanel, BorderLayout.EAST);

                // Actualizar el panel para que se muestre el cambio
                userPanel.revalidate();
                userPanel.repaint();
                break; // Salir del bucle una vez que se encuentra el usuario
            }
        }
    }

    public static void updateFilesPanels() {
        // Crear el panel contenedor
        JPanel userPanelContainer = new JPanel();
        userPanelContainer.setLayout(new BoxLayout(userPanelContainer, BoxLayout.Y_AXIS));
        userPanelContainer.setBackground(new Color(29, 29, 29)); // Fondo azul para el contenedor
        userPanelContainer.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir borde para el contenedor

        // Iterar sobre la lista de usuarios y agregar paneles
        for (ArchivoCliente archivo : archivosCliente) {
            if (Global.usuarioReceptor.getNombreUsuario().equals(archivo.getEmisor())) {
                JPanel userPanel = new JPanel();
                userPanel.setLayout(new BorderLayout()); // Usar BorderLayout para centrar el JLabel
                userPanel.setBackground(new Color(0x333333)); // Fondo amarillo para los paneles de usuario
                userPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); // Borde inferior negro
                userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Altura fija para los paneles

                // Crear y agregar el JLabel con el nombre de usuario
                JLabel userLabel = new JLabel(archivo.getNombreArchivo());
                userLabel.setForeground(Color.white);
                userLabel.setFont(new Font("Lato", Font.PLAIN, 14));
                userLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrar el texto
                userPanel.add(userLabel, BorderLayout.CENTER);

                // Agregar un MouseListener al panel para manejar el clic
                userPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        JFileChooser folderChooser = new JFileChooser();
                        // Configura el JFileChooser para seleccionar carpetas
                        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        // Establecer la carpeta de descargas como directorio inicial
                        String userHome = System.getProperty("user.home");
                        folderChooser.setCurrentDirectory(new File(userHome + File.separator + "Downloads"));
                        folderChooser.setDialogTitle("Selecciona la carpeta para guardar el archivo");

                        // Mostrar el diálogo
                        int result = folderChooser.showSaveDialog(null);

                        if (result == JFileChooser.APPROVE_OPTION) {
                            File selectedFolder = folderChooser.getSelectedFile();
                            System.out.println("Carpeta seleccionada para guardar: " + selectedFolder.getAbsolutePath());

                            // Definir el nombre del archivo
                            String nombreArchivo = archivo.getNombreArchivo();
                            File destinoArchivo = new File(selectedFolder, nombreArchivo);

                            FileDownloadHandler descargarArchivo = new FileDownloadHandler(nombreArchivo);
                            // Llama al método para enviar la solicitud de descarga del archivo
                            descargarArchivo.sendDownloadRequest(destinoArchivo.getAbsolutePath());
                        }
                    }

                });

                userPanelContainer.add(userPanel);
            }
        }

        // Ajustar el tamaño del contenedor
        userPanelContainer.setPreferredSize(new Dimension(userPanelContainer.getPreferredSize().width, 50 * allUsers.size()));

        // Actualizar el contenido del ScrollPane
        scrollArchivos.setViewportView(userPanelContainer);
        scrollArchivos.revalidate(); // Forzar a que el componente se redibuje
        scrollArchivos.repaint();    // Forzar el redibujado del componente
    }

    public static void showChatPanel(Usuario usuario) {
        Conversacion conversacion = getConversacion(usuario);
        updateChatPanel(conversacion);
    }

    public static void updateChatPanel(Conversacion conversacion) {
        Global.usuarioReceptor = conversacion.getContacto();

        // Crear el panel contenedor para los chats
        JPanel chatPanelContainer = new JPanel();
        chatPanelContainer.setLayout(new BoxLayout(chatPanelContainer, BoxLayout.Y_AXIS));
        chatPanelContainer.setBackground(new Color(0x383838)); // Fondo blanco para el contenedor de chat
        chatPanelContainer.setBorder(new EmptyBorder(10, 10, 10, 10)); // Añadir borde para el contenedor

        // Iterar sobre los mensajes de la conversación
        for (Mensaje mensaje : conversacion.getMensajes()) {
            // Crear panel de mensaje
            JPanel mensajePanel = new JPanel();
            mensajePanel.setLayout(new BorderLayout());
            mensajePanel.setBackground(new Color(0x383838));
            // Crear y configurar el JLabel para el mensaje
            JLabel mensajeLabel = new JLabel("<html><body style='color: "
                    + (mensaje.getEmisor().equals(Global.usuarioActual) ? "white" : "black")
                    + ";'>" + mensaje.getContenido() + "</body></html>");
            mensajeLabel.setOpaque(true); // Permite que el JLabel tenga un color de fondo
            mensajeLabel.setBackground(mensaje.getEmisor().equals(Global.usuarioActual) ? new Color(0x27b599) : Color.LIGHT_GRAY);
            mensajeLabel.setHorizontalAlignment(mensaje.getEmisor().equals(Global.usuarioActual) ? SwingConstants.RIGHT : SwingConstants.LEFT);
            mensajeLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // Añadir el JLabel al panel y alinearlo
            mensajePanel.add(mensajeLabel, mensaje.getEmisor().equals(Global.usuarioActual) ? BorderLayout.EAST : BorderLayout.WEST);

            // Ajustar el tamaño del panel para que ocupe todo el ancho
            mensajePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            // Agregar el mensaje al contenedor de chat
            chatPanelContainer.add(mensajePanel);
            chatPanelContainer.add(Box.createVerticalStrut(10)); // Espacio entre mensajes
        }

        // Actualizar el contenido del ScrollPane
        scrollChats.setViewportView(chatPanelContainer);
        scrollChats.revalidate(); // Forzar a que el componente se redibuje
        scrollChats.repaint();    // Forzar el redibujado del componente
        
        scrollChats.getVerticalScrollBar().setValue(scrollChats.getVerticalScrollBar().getMaximum());

    }

    public static Conversacion getConversacion(Usuario receptor) {
        for (Conversacion conv : Global.conversaciones) {
            if (conv.getContacto().equals(receptor)) {
                return conv;
            }
        }
        // Si no existe la conversación, crearla y agregarla a la lista global
        Conversacion nuevaConversacion = new Conversacion(receptor);
        Global.conversaciones.add(nuevaConversacion);
        return nuevaConversacion;
    }

    public static void soundChat() {
        String resourcePath = "/sound/chat.wav";
        try (InputStream audioSrc = Global.class.getResourceAsStream(resourcePath); BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc)) {

            if (audioSrc == null) {
                System.err.println("Could not find resource: " + resourcePath);
                return;
            }

            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                if (false) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void soundMessage() {
        String resourcePath = "/sound/mensaje.wav";
        try (InputStream audioSrc = Global.class.getResourceAsStream(resourcePath); BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc)) {

            if (audioSrc == null) {
                System.err.println("Could not find resource: " + resourcePath);
                return;
            }

            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                if (false) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
