package cliente.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.Border;

import cliente.controller.LoginController;
import cliente.controller.StravaController;

public class VentanaLogin extends JFrame {
    private JTextField campoEmail;
    private JPasswordField campoPassword;
    private long token;
    private LoginController loginController;
    
    public VentanaLogin(LoginController loginController,StravaController stravaController) {
        // Configuración de la ventana
        setTitle("Inicio de Sesión");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // Creación de componentes
        JLabel labelEmail = new JLabel("Email:");
        JLabel labelPassword = new JLabel("Contraseña:");
        campoEmail = new JTextField(20);
        campoPassword = new JPasswordField(20);
        JButton botonLogin = new JButton("Iniciar Sesión");

        // Configuración del diseño
        setLayout(new BorderLayout());
        JPanel panelCabecera= new JPanel();
        JPanel panelCentral = new JPanel();
        panelCabecera.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel labelRegistro= new JLabel("No tienes cuenta?");
        JButton botonRegistro = new JButton("Registrarse");
        panelCabecera.setBackground(Color.DARK_GRAY);
        panelCentral.setBackground(Color.DARK_GRAY);
        panelCabecera.add(labelRegistro,BorderLayout.NORTH);
        panelCabecera.add(botonRegistro, BorderLayout.CENTER);
        panelCentral.setLayout(new FlowLayout(FlowLayout.CENTER));
        // Agregar componentes a la ventana 
        panelCentral.add(labelEmail);
        panelCentral.add(campoEmail);
        panelCentral.add(labelPassword);
        panelCentral.add(campoPassword);
        panelCentral.add(new JLabel()); // Espacio en blanco
        panelCentral.add(botonLogin);
        labelRegistro.setForeground(Color.white);
        labelEmail.setForeground(Color.white);
        labelPassword.setForeground(Color.white);
        add(panelCabecera,BorderLayout.NORTH);
        add(panelCentral,BorderLayout.CENTER);
      
        botonRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            	VentanaRegistro v= new VentanaRegistro(loginController);
            	}catch (Exception ex) {
            		
            	}
            }
        });
        // Configurar el ActionListener para el botón de inicio de sesión
        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	loginController.login(campoEmail.getText(), campoPassword.getText());
            	VentanaEntrenamientos v= new VentanaEntrenamientos(token,stravaController);
            }
        });

        // Configurar el KeyListener para el campo de contraseña
        campoPassword.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // No se utiliza en este ejemplo
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	loginController.login(campoEmail.getText(), campoPassword.getText());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // No se utiliza en este ejemplo
            }
        });
    }
}
