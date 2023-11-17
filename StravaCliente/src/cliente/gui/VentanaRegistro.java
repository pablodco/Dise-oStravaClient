package cliente.gui;

import javax.swing.JFrame;

import cliente.controller.LoginController;
import cliente.remote.ServiceLocator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaRegistro extends JFrame {
	private JTextField campoNombre;
	private LoginController loginController;
	private JTextField campoEmail;
	private JPasswordField campoPassword;
	private JTextField campoFechaNac;
	private JTextField campoPesoKilo;
	private JTextField campoAltura;
	private JTextField campoFrecCardMax;
	private JTextField campoFrecCardRep;
	private DateFormat dt= new SimpleDateFormat("yyyy/mm/dd");

	public VentanaRegistro(LoginController loginController) {
		this.loginController= loginController;
		// Configuración de la ventana
		setTitle("Registro de Usuario");
		setSize(400, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Creación de componentes
		JLabel labelNombre = new JLabel("Nombre:");
		JLabel labelEmail = new JLabel("Email:");
		JLabel labelPassword= new JLabel("Password:");
		JLabel labelFechaNac = new JLabel("Fecha de Nacimiento (YYYY/MM/DD):");
		JLabel labelPesoKilo = new JLabel("Peso (kg):");
		JLabel labelAltura = new JLabel("Altura (cm):");
		JLabel labelFrecCardMax = new JLabel("Frecuencia Cardíaca Máxima:");
		JLabel labelFrecCardRep = new JLabel("Frecuencia Cardíaca en Reposo:");

		campoNombre = new JTextField(20);
		campoPassword= new JPasswordField();
		campoEmail = new JTextField(20);
		campoFechaNac = new JTextField(20);
		campoPesoKilo = new JTextField(20);
		campoAltura = new JTextField(20);
		campoFrecCardMax = new JTextField(20);
		campoFrecCardRep = new JTextField(20);

		JButton botonRegistrar = new JButton("Registrar");

		// Configuración del diseño
		setLayout(new GridLayout(8, 2));

		// Agregar componentes a la ventana
		add(labelNombre);
		add(campoNombre);
		add(labelEmail);
		add(campoEmail);
		add(labelPassword);
		add(campoPassword);
		add(labelFechaNac);
		add(campoFechaNac);
		add(labelPesoKilo);
		add(campoPesoKilo);
		add(labelAltura);
		add(campoAltura);
		add(labelFrecCardMax);
		add(campoFrecCardMax);
		add(labelFrecCardRep);
		add(campoFrecCardRep);
		add(new JLabel()); // Espacio en blanco
		add(botonRegistrar);

		// Configurar el ActionListener para el botón de registro
		botonRegistrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				realizarRegistro();
			}
		});

		// Configurar el KeyListener para el campo de la fecha de nacimiento
		campoFechaNac.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// No se utiliza en este ejemplo
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					realizarRegistro();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// No se utiliza en este ejemplo
			}
		});
	}

	private void realizarRegistro() {
		// Obtener el contenido de los campos de texto
		String nombre = campoNombre.getText();
		String email = campoEmail.getText();
		String password = campoPassword.getText();
		String fechaNacStr = campoFechaNac.getText();
		int pesoKilo = Integer.parseInt(campoPesoKilo.getText());
		int altura = Integer.parseInt(campoAltura.getText());
		int frecCardMax = Integer.parseInt(campoFrecCardMax.getText());
		int frecCardRep = Integer.parseInt(campoFrecCardRep.getText());
		loginController.registro(email,password,nombre,fechaNacStr,pesoKilo,altura,frecCardRep,frecCardMax);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		LoginController lC= new LoginController(new ServiceLocator());
			public void run() {
				new VentanaRegistro(lC);
			}
		});
	}
}
