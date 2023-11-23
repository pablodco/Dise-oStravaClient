package cliente.gui;

import cliente.controller.LoginController;
import cliente.controller.StravaController;
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
import java.util.concurrent.Flow;

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
	private long token =-1;
	private DateFormat dt= new SimpleDateFormat("yyyy/mm/dd");

	public VentanaRegistro(LoginController loginController,StravaController stravaController) {
		this.loginController= loginController;
		// Configuración de la ventana
		setTitle("Registro de Usuario");
		setSize(500, 300);
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
		JPanel panelNombre =new JPanel();
		panelNombre.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel panelEmail =new JPanel();
		panelEmail.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel panelPassword =new JPanel();
		panelPassword.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel panelFechaNac =new JPanel();
		panelFechaNac.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel panelPesoKilo =new JPanel();
		panelPesoKilo.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel panelAltura =new JPanel();
		panelAltura.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel panelFrecCardRep =new JPanel();
		panelFrecCardRep.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel panelFrecCardMax=new JPanel();
		panelFrecCardMax.setLayout(new BoxLayout(panelFrecCardMax,BoxLayout.X_AXIS));
		campoNombre = new JTextField(20);
		campoPassword= new JPasswordField(20);
		campoEmail = new JTextField(20);
		campoFechaNac = new JTextField(20);
		campoPesoKilo = new JTextField(20);
		campoAltura = new JTextField(20);
		campoFrecCardMax = new JTextField(20);
		campoFrecCardRep = new JTextField(20);

		JButton botonRegistrar = new JButton("Registrar");

		// Configuración del diseño
		setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
	
		// Agregar componentes a la ventana
		panelNombre.add(labelNombre);
		panelNombre.add(campoNombre);
		panelEmail.add(labelEmail);
		panelEmail.add(campoEmail);
		panelPassword.add(labelPassword);
		panelPassword.add(campoPassword);
		panelFechaNac.add(labelFechaNac);
		panelFechaNac.add(campoFechaNac);
		panelPesoKilo.add(labelPesoKilo);
		panelPesoKilo.add(campoPesoKilo);
		panelAltura.add(labelAltura);
		panelAltura.add(campoAltura);
		panelFrecCardRep.add(labelFrecCardMax);
		panelFrecCardRep.add(campoFrecCardMax);
		panelFrecCardMax.add(labelFrecCardRep);
		panelFrecCardMax.add(campoFrecCardRep);
		add(new JLabel());// Espacio en blanco
		add(panelNombre);
		add(panelEmail);
		add(panelPassword);
		add(panelFechaNac);
		add(panelPesoKilo);
		add(panelAltura);
		add(panelFrecCardRep);
		add(panelFrecCardMax);
		add(botonRegistrar);
		// Configurar el ActionListener para el botón de registro
		botonRegistrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				realizarRegistro();
				VentanaEntrenamientos v= new VentanaEntrenamientos(token,stravaController);
				v.setVisible(true);
				v.setEnabled(true);
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
		token=loginController.registro(email,password,nombre,fechaNacStr,pesoKilo,altura,frecCardRep,frecCardMax);
	}

}
