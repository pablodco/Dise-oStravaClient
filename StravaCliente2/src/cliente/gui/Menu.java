package cliente.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar {
	private static final long serialVersionUID=1l;
	public Menu() {
		// Crear elementos del menú
		JMenuItem menuItemEntrenamientos = new JMenuItem("Entrenamientos");
		JMenuItem menuItemRetosActivos = new JMenuItem("Retos Activos");
		JMenuItem menuItemRetosDisponibles = new JMenuItem("Retos Disponibles");

		// Configurar ActionListener para cada elemento del menú
		menuItemEntrenamientos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirVentanaEntrenamientos();
			}
		});

		menuItemRetosActivos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirVentanaRetosActivos();
			}
		});

		menuItemRetosDisponibles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirVentanaRetosDisponibles();
			}
		});

		// Agregar elementos al menú
		add(menuItemEntrenamientos);
		add(menuItemRetosActivos);
		add(menuItemRetosDisponibles);

	}

	private void abrirVentanaEntrenamientos() {
		// Aquí debes abrir o cambiar a la ventana de entrenamientos
		// Puedes crear una instancia de VentanaEntrenamientos y hacerla visible
		// Por ejemplo:
		// new VentanaEntrenamientos().setVisible(true);
		System.out.println("Abriendo Ventana de Entrenamientos");
	}

	private void abrirVentanaRetosActivos() {
		// Aquí debes abrir o cambiar a la ventana de retos activos
		// Puedes crear una instancia de VentanaRetosActivos y hacerla visible
		// Por ejemplo:
		// new VentanaRetosActivos().setVisible(true);
		System.out.println("Abriendo Ventana de Retos Activos");
	}

	private void abrirVentanaRetosDisponibles() {
		// Aquí debes abrir o cambiar a la ventana de retos disponibles
		// Puedes crear una instancia de VentanaRetosDisponibles y hacerla visible
		// Por ejemplo:
		// new VentanaRetosDisponibles().setVisible(true);
		System.out.println("Abriendo Ventana de Retos Disponibles");
	}
}
