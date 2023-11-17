package cliente.gui;

import java.awt.FlowLayout;
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
	private long token;

	public VentanaEntrenamientos(long token,StravaController stravaController) {
		// Configurar la ventana
		this.stravaController= stravaController;
		this.token = token;
		setTitle("EntrenamientoDTOes de Actividad");
		setSize(800, 500); // Tamaño ajustado
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Configurar componentes de la interfaz
		EntrenamientosDTOListModel = new DefaultListModel<>();
		EntrenamientosDTOList = new JList<>(EntrenamientosDTOListModel);
		EntrenamientosDTOList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		EntrenamientosDTOList.setCellRenderer(new EntrenamientoDTOListRenderer(this)); // Pasar la instancia de la
																						// ventana
		EntrenamientosDTOList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					mostrarDetallesEntrenamientoDTO(EntrenamientosDTOList.getSelectedIndex());
				}
			}
		});

		detallesTextArea = new JTextArea();
		detallesTextArea.setEditable(false);

		tituloField = new JTextField(15);
		distanciaField = new JTextField(5);
		duracionField = new JTextField(5);
		fechaInicioField = new JTextField(15);

		// Agrego JComboBox para elegir la actividad
		String[] actividades = { "CICLISMO", "RUNNING" };
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

		JButton agregarEntrenamientoDTOButton = new JButton("Agregar EntrenamientoDTO");
		agregarEntrenamientoDTOButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Obtener la actividad seleccionada del JComboBox
				agregarEntrenamientoDTO(tituloField.getText(), distanciaField.getText(), duracionField.getText(),
						fechaInicioField.getText(), (String) actividadComboBox.getSelectedItem());
			}
		});

		// Crear paneles para organizar la interfaz
		JPanel panelEntrenamientoDTOes = new JPanel(new BorderLayout());
		panelEntrenamientoDTOes.add(new JScrollPane(EntrenamientosDTOList), BorderLayout.WEST);

		JPanel panelDetalles = new JPanel(new BorderLayout());
		panelDetalles.add(new JScrollPane(detallesTextArea), BorderLayout.CENTER);

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
			Date fechaInicio = new Date();

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
			String fechaFinStr = dateFormat.format(fechaFin);
			String fecha_ini_str = dateFormat.format(fechaInicio);
			// Crear la sesión y agregarla al usuario
			EntrenamientoDTO nuevoEntrenamiento = new EntrenamientoDTO(duracion, titulo, distancia, fecha_ini_str,
					actividad);
			stravaController.crearEntrenamiento(token,duracion, titulo, actividad,distancia, fecha_ini_str);

			// Actualizar el modelo de lista
			EntrenamientosDTOListModel.addElement("EntrenamientoDTO " + nuevoEntrenamiento.getTitulo());

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
				&& EntrenamientoDTOIndex < stravaController.obtenerEntrenamientos(token).size()) {
			EntrenamientoDTO EntrenamientoDTO = stravaController.obtenerEntrenamientos(token)
					.get(EntrenamientoDTOIndex);
			detallesTextArea.setText(formatDetallesEntrenamientoDTO(EntrenamientoDTO));
		}
	}

	private String formatDetallesEntrenamientoDTO(EntrenamientoDTO EntrenamientoDTO) {
		String detalles = "Título: " + EntrenamientoDTO.getTitulo() + "\n" + "Distancia: "
				+ EntrenamientoDTO.getDistancia() + " km\n" + "Duración: " + EntrenamientoDTO.getDuracion() + " min\n"
				+ "Fecha de inicio: " + EntrenamientoDTO.getFecha_ini() + "\n" + "Actividad: "
				+ EntrenamientoDTO.getActividad() + "\n"; // Mostrar la actividad
		return detalles;
	}

	class EntrenamientoDTOListRenderer extends JLabel implements ListCellRenderer<String> {
		private static final long serialVersionUID= 1l;
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
				String[] parts = value.split(" ");
				if (parts.length == 2 && parts[0].equals("EntrenamientoDTO")) {
					String EntrenamientoDTOTitulo = parts[1];
					EntrenamientoDTO EntrenamientoDTO = obtenerEntrenamientoDTOPorTitulo(EntrenamientoDTOTitulo);

					// Obtener la sesión con mayor distancia para ambas actividades
					EntrenamientoDTO EntrenamientoDTOConMayorDistanciaRunning = obtenerEntrenamientoDTOConMayorDistancia(
							"running");
					EntrenamientoDTO EntrenamientoDTOConMayorDistanciaCiclismo = obtenerEntrenamientoDTOConMayorDistancia(
							"ciclismo");

					// Resaltar la sesión con mayor distancia en running
					if (EntrenamientoDTOConMayorDistanciaRunning != null
							&& EntrenamientoDTOConMayorDistanciaRunning.equals(EntrenamientoDTO)) {
						setForeground(Color.RED); // Puedes cambiar el color según tus preferencias
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
					Date fechaInicio = dateFormat.parse(EntrenamientoDTO.getFecha_ini());
					String fechaInicioStr = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fechaInicio);
					setText("Sesión " + EntrenamientoDTOTitulo + " - " + fechaInicioStr);

					// Resaltar EntrenamientoDTOes de running y ciclismo
					if (EntrenamientoDTO.getActividad() == "running") {
						setBackground(Color.YELLOW); // Amarillo para running
					} else if (EntrenamientoDTO.getActividad() == "ciclismo") {
						setBackground(Color.GREEN); // Verde para ciclismo
					}
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

			return EntrenamientoDTOConMayorDistancia;
		}

		// Método para obtener una sesión por su ID (simulado)
		private EntrenamientoDTO obtenerEntrenamientoDTOPorTitulo(String titulo) {
				return stravaController.obtenerEntrenamientoPorTitulo(token, titulo);
		}
	}
}