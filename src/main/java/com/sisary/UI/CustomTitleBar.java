/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisary.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomTitleBar extends JPanel {

    // Atributos para almacenar la posición inicial del ratón
    private int pX, pY;

    public CustomTitleBar(JPanel parentPanel, JFrame parentFrame, String title, Color backgroundColor) {
        // Configura el layout para que el panel ocupe todo el espacio del parentPanel
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setBounds(0, 0, parentPanel.getWidth(), parentPanel.getHeight());

        // Título a la izquierda
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 0)); // Margin left 20
        add(titleLabel, BorderLayout.WEST);

        // Panel para los botones a la derecha
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5)); // Espacio entre botones
        buttonPanel.setOpaque(false);

        // Botón de cerrar
        JButton closeButton = new JButton();
        closeButton.setPreferredSize(new Dimension(10, 10));
        closeButton.setBackground(Color.decode("#ff453a"));
        closeButton.setOpaque(true);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> parentFrame.dispose());

        // Botón de minimizar
        JButton minimizeButton = new JButton();
        minimizeButton.setPreferredSize(new Dimension(10, 10));
        minimizeButton.setBackground(Color.decode("#ff9f0a"));
        minimizeButton.setOpaque(true);
        minimizeButton.setBorderPainted(false);
        minimizeButton.addActionListener(e -> parentFrame.setState(Frame.ICONIFIED));

        // Añadir botones al panel
        buttonPanel.add(minimizeButton);
        buttonPanel.add(closeButton);

        // Añadir panel de botones a la derecha
        add(buttonPanel, BorderLayout.EAST);

        // Agregar la barra de título personalizada al panel padre
        parentPanel.setLayout(new BorderLayout());
        parentPanel.add(this, BorderLayout.NORTH);

        // Hacer que la barra se pueda arrastrar para mover la ventana
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Guardar la posición actual del ratón al presionar
                pX = e.getX();
                pY = e.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Calcular la nueva posición de la ventana
                parentFrame.setLocation(parentFrame.getLocation().x + e.getX() - pX, parentFrame.getLocation().y + e.getY() - pY);
            }
        });
    }
}
