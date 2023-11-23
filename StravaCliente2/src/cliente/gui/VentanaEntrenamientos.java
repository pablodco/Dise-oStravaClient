package cliente.gui;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.BorderUIResource.TitledBorderUIResource;
import javax.swing.table.TableModel;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import cliente.controller.StravaController;
import servidor.dto.EntrenamientoDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaEntrenamientos extends JFrame {
	private DefaultListModel<String> EntrenamientosDTOListModel;
	private JList<String> EntrenamientosDTOList;
	private JTextArea detallesTextArea;
	private JTextField tituloField;
	private JTextField distanciaField;
	private JTextField duracionField;
	private JTextField fechaInicioField;
	private StravaController stravaController;
	private JMenuBar menuBar;
	private long token;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	EntrenamientoDTOListRenderer renderer;

	public VentanaEntrenamientos(long token, StravaController stravaController) {
		// Configurar la ventana
		this.stravaController = stravaController;
		this.token = token;
		setTitle("EntrenamientoDTOes de Actividad");
		setSize(800, 500); // Tamaño ajustado
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		renderer= new EntrenamientoDTOListRenderer(this);
		// Configurar componentes de la interfaz
		EntrenamientosDTOListModel = new DefaultListModel<>();
		EntrenamientosDTOList = new JList<>(EntrenamientosDTOListModel);
		EntrenamientosDTOList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		EntrenamientosDTOList.setCellRenderer(renderer); // Pasar la instancia de la
		menuBar = new JMenuBar();
		JMenuItem menuItemRetosActivos = new JMenuItem("Retos Activos");
		JMenuItem menuItemRetosDisponibles = new JMenuItem("Retos Disponibles");

		menuItemRetosActivos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaRetosActivos v = new VentanaRetosActivos(token, stravaController);
				v.setVisible(true);
			}
		});

		menuItemRetosDisponibles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaRetosDisponibles v = new VentanaRetosDisponibles(token, stravaController);
				v.setVisible(true);
			}
		});

		menuBar.add(menuItemRetosActivos);
		menuBar.add(menuItemRetosDisponibles);

		// Configurar la barra de menú en la ventana
		setJMenuBar(menuBar); // ventana
		EntrenamientosDTOList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					mostrarDetallesEntrenamientoDTO(EntrenamientosDTOList.getSelectedIndex());
				}
			}
		});
		this.add(new Menu());
		detallesTextArea = new JTextArea();
		detallesTextArea.setEditable(false);

		tituloField = new JTextField(15);
		distanciaField = new JTextField(5);
		duracionField = new JTextField(5);
		fechaInicioField = new JTextField(15);

		// Agrego JComboBox para elegir la actividad
		String[] actividades = { "ciclismo", "running" };
		JComboBox<String> actividadComboBox = new JComboBox<>(actividades);
		JPanel panelAgregarEntrenamientoDTO = new JPanel(new GridLayout(5, 2, 5, 5));
		panelAgregarEntrenamientoDTO
				.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
						"Agregar Sesión", TitledBorder.CENTER, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14)));

		panelAgregarEntrenamientoDTO.add(new JLabel("Título:"));
		panelAgregarEntrenamientoDTO.add(tituloField);
		panelAgregarEntrenamientoDTO.add(new JLabel("Distancia (km):"));
		panelAgregarEntrenamientoDTO.add(distanciaField);
		panelAgregarEntrenamientoDTO.add(new JLabel("Duración (min):"));
		panelAgregarEntrenamientoDTO.add(duracionField);
		panelAgregarEntrenamientoDTO.add(new JLabel("Fecha de inicio (HH:mm):")); // Nueva etiqueta
		panelAgregarEntrenamientoDTO.add(fechaInicioField);
		panelAgregarEntrenamientoDTO.add(new JLabel("Actividad:"));
		panelAgregarEntrenamientoDTO.add(actividadComboBox);

		JButton agregarEntrenamientoDTOButton = new JButton("Agregar Entrenamiento");
		agregarEntrenamientoDTOButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Obtener la actividad seleccionada del JComboBox
				agregarEntrenamientoDTO(tituloField.getText(), distanciaField.getText(), duracionField.getText(),
						fechaInicioField.getText(), (String) actividadComboBox.getSelectedItem());
				EntrenamientosDTOList.repaint();
			}
		});

		// Crear paneles para organizar la interfaz
		JPanel panelEntrenamientoDTOes = new JPanel(new BorderLayout());
		panelEntrenamientoDTOes.add(new JScrollPane(EntrenamientosDTOList), BorderLayout.WEST);

		JPanel panelDetalles = new JPanel(new BorderLayout());
		panelDetalles.add(new JScrollPane(detallesTextArea), BorderLayout.CENTER);
		for (EntrenamientoDTO entre : stravaController.obtenerEntrenamientos(token)) {
			// Calcular la fecha y hora de fin sumando la duración en minutos
			EntrenamientosDTOListModel.addElement("Sesion" + "-" + entre.getTitulo() + "-" + dateFormat.format(entre.getFecha_ini()));
			;
		}
		// Agregar bordes a los componentes en panelAgregarEntrenamientoDTO
		for (Component component : panelAgregarEntrenamientoDTO.getComponents()) {
			if (component instanceof JTextField) {
				((JComponent) component).setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
		}

		// Agregar el botón en el medio
		JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelBoton.add(agregarEntrenamientoDTOButton);

		// Configurar el diseño general de la ventana
		setLayout(new BorderLayout());
		add(panelEntrenamientoDTOes, BorderLayout.WEST);
		add(panelDetalles, BorderLayout.CENTER);
		add(panelAgregarEntrenamientoDTO, BorderLayout.NORTH);
		add(panelBoton, BorderLayout.SOUTH);

		// Mostrar la ventana
		setLocationRelativeTo(null); // Centrar la ventana en la pantalla
		setVisible(true);
	}

	private void agregarEntrenamientoDTO(String titulo, String distanciaStr, String duracionStr, String horaInicioStr,
			String actividad) {
		try {
			double distancia = Double.parseDouble(distanciaStr);
			long duracion = Integer.parseInt(duracionStr);

			// Asumir que el año y el mes son los actuales
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date fechaInicio = Calendar.getInstance().getTime();

			// Validar que las horas estén en el rango de 0 a 23
			int horas = Integer.parseInt(horaInicioStr.split(":")[0]);
			if (horas < 0 || horas >= 24) {
				JOptionPane.showMessageDialog(this, "Por favor, ingrese horas válidas (entre 0 y 23).");
				return; // Salir del método si las horas no están en el rango válido
			}
			fechaInicio.setHours(horas);

			// Validar que los minutos estén en el rango de 0 a 59
			int minutos = Integer.parseInt(horaInicioStr.split(":")[1]);
			if (minutos < 0 || minutos >= 60) {
				JOptionPane.showMessageDialog(this, "Por favor, ingrese minutos válidos (entre 0 y 59).");
				return; // Salir del método si los minutos no están en el rango válido
			}
			fechaInicio.setMinutes(minutos);

			// Calcular la fecha y hora de fin sumando la duración en minutos
			Date fechaFin = new Date(fechaInicio.getTime() + duracion * 60000);

			// Crear la sesión y agregarla al usuario
			EntrenamientoDTO nuevoEntrenamiento = new EntrenamientoDTO(duracion, titulo, distancia, fechaInicio,
					actividad);
			stravaController.crearEntrenamiento(token, duracion, titulo, actividad, distancia, fechaInicio);

			// Actualizar el modelo de lista
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date fechaInicioDate = nuevoEntrenamiento.getFecha_ini();
			String fechaInicioStr = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fechaInicioDate);
			EntrenamientosDTOListModel
					.addElement("Sesión" + "-" + nuevoEntrenamiento.getTitulo() + "-" + fechaInicioStr);
			// Limpiar los campos de entrada
			tituloField.setText("");
			distanciaField.setText("");
			duracionField.setText("");
			fechaInicioField.setText("");
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this,
					"Por favor, ingrese valores válidos para distancia, duración y hora de inicio.");
		}
	}

	private void mostrarDetallesEntrenamientoDTO(int EntrenamientoDTOIndex) {
		if (EntrenamientoDTOIndex >= 0
				&& EntrenamientoDTOIndex <= stravaController.obtenerEntrenamientos(token).size()) {
			EntrenamientoDTO EntrenamientoDTO = stravaController.obtenerEntrenamientos(token)
					.get(EntrenamientoDTOIndex);
			detallesTextArea.setText(formatDetallesEntrenamientoDTO(EntrenamientoDTO));
		}
	}

	private String formatDetallesEntrenamientoDTO(EntrenamientoDTO EntrenamientoDTO) {
		String detalles = "Título: " + EntrenamientoDTO.getTitulo() + "\n" + "Distancia: "
				+ EntrenamientoDTO.getDistancia() + " km\n" + "Duración: " + EntrenamientoDTO.getDuracion() + " min\n"
				+ "Fecha de inicio: " + dateFormat.format(EntrenamientoDTO.getFecha_ini()) + "\n" + "Actividad: "
				+ EntrenamientoDTO.getActividad() + "\n"; // Mostrar la actividad
		return detalles;
	}

	class EntrenamientoDTOListRenderer extends JLabel implements ListCellRenderer<String> {
		private static final long serialVersionUID = 1l;
		private VentanaEntrenamientos ventanaEntrenamientos;

		public EntrenamientoDTOListRenderer(VentanaEntrenamientos ventanaEntrenamientoes) {
			this.ventanaEntrenamientos = ventanaEntrenamientoes;
			setOpaque(true);
			setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) {
			setText(value);

			// Resaltar la sesión seleccionada
			if (isSelected) {
				setForeground(list.getSelectionForeground());
				setFont(getFont().deriveFont(Font.BOLD)); // Negrita para sesión seleccionada
				setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Borde negro cuando está seleccionada
			} else {
				setForeground(list.getForeground());
				setFont(getFont().deriveFont(Font.PLAIN)); // Restaurar el estilo normal
				setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Sin borde cuando no está seleccionada
			}

			// Resaltar la sesión con mayor distancia
			try {
				String[] parts = value.split("-");
				System.out.println(parts[0]);
				System.out.println(parts[1]);
				if (parts[0].equals("Sesion")) {
					String EntrenamientoDTOTitulo = parts[1];
					EntrenamientoDTO EntrenamientoDTO = obtenerEntrenamientoDTOPorTitulo(EntrenamientoDTOTitulo);
					System.out.println(EntrenamientoDTO);			// Resaltar EntrenamientoDTOes de running y ciclismo
					if (EntrenamientoDTO.getActividad().equals("running")) {
						setBackground(new Color(0, 255, 170)); // Amarillo para running
					} else if (EntrenamientoDTO.getActividad().equals("ciclismo")) {
						setBackground(new Color(250, 200, 50)); // Verde para ciclismo
					}
					// Obtener la sesión con mayor distancia para ambas actividades
					EntrenamientoDTO EntrenamientoDTOConMayorDistanciaRunning = obtenerEntrenamientoDTOConMayorDistancia(
							"running");
					EntrenamientoDTO EntrenamientoDTOConMayorDistanciaCiclismo = obtenerEntrenamientoDTOConMayorDistancia(
							"ciclismo");

					// Resaltar la sesión con mayor distancia en running
					if (EntrenamientoDTOConMayorDistanciaRunning != null
							&& EntrenamientoDTOConMayorDistanciaRunning.equals(EntrenamientoDTO)) {
						setForeground(new Color(250, 155, 0)); // Puedes cambiar el color según tus preferencias
						setFont(getFont().deriveFont(Font.BOLD | Font.ITALIC)); // Puedes ajustar el estilo según tus
																				// preferencias
					}

					// Resaltar la sesión con mayor distancia en ciclismo
					if (EntrenamientoDTOConMayorDistanciaCiclismo != null
							&& EntrenamientoDTOConMayorDistanciaCiclismo.equals(EntrenamientoDTO)) {
						setForeground(Color.BLUE); // Puedes cambiar el color según tus preferencias
						setFont(getFont().deriveFont(Font.BOLD | Font.ITALIC)); // Puedes ajustar el estilo según tus
																				// preferencias
					}

					// Formatear el texto
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					String fechaInicioStr = dateFormat.format(EntrenamientoDTO.getFecha_ini());
					setText("Sesión" + "-" + EntrenamientoDTOTitulo + "-" + fechaInicioStr);

		
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		// Método para obtener la sesión con mayor distancia para una actividad
		// específica
		private EntrenamientoDTO obtenerEntrenamientoDTOConMayorDistancia(String actividad) {
			double maxDistancia = -1;
			EntrenamientoDTO EntrenamientoDTOConMayorDistancia = null;

			for (EntrenamientoDTO EntrenamientoDTO : stravaController.obtenerEntrenamientos(token)) {
				if (EntrenamientoDTO.getActividad().equals(actividad)
						&& EntrenamientoDTO.getDistancia() > maxDistancia) {
					maxDistancia = EntrenamientoDTO.getDistancia();
					EntrenamientoDTOConMayorDistancia = EntrenamientoDTO;
				}
			}
			System.out.println(EntrenamientoDTOConMayorDistancia);
			return EntrenamientoDTOConMayorDistancia;
		}

		// Método para obtener una sesión por su ID (simulado)
		private EntrenamientoDTO obtenerEntrenamientoDTOPorTitulo(String titulo) {
			return stravaController.obtenerEntrenamientoPorTitulo(token, titulo);
		}
	}
}