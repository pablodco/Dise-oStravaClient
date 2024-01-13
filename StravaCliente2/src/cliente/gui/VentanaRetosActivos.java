package cliente.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import cliente.controller.StravaController;
import servidor.dto.RetoDTO;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VentanaRetosActivos extends JFrame {
	private JButton boton;
	private JTable table;
	private JMenuBar menuBar;
	private RetoDTO RetoDTOSeleccionado; // Nueva variable para almacenar el RetoDTO seleccionado
	private StravaController stravaController;
	private JTextField tfNombre;
	private JTextField tfObjetivo;
	private JTextField tfDescripcion;
	private JTextField tfFechaInicio;
	private JTextField tfFechaFin;
	private long token;

	public VentanaRetosActivos(long token, StravaController stravaController) {
		this.stravaController = stravaController;
		this.token = token;
		setTitle("RetoDTOs Disponibles");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 400);

		// Crear paneles
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel tablePanel = new JPanel(new BorderLayout());
		JPanel panelDetalles = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel buttonPanel = new JPanel();

		JLabel label = new JLabel("LISTA DE Retos", SwingConstants.CENTER);
		mainPanel.add(label, BorderLayout.NORTH);
		// Crear los elementos para incluir datos
		JLabel labelDescripcion = new JLabel("Descripcion");
		tfDescripcion = new JTextField(300);
		JLabel labelNombre = new JLabel("Nombre");
		tfNombre = new JTextField();
		JLabel labelObjetivo = new JLabel("Objetivo");
		tfObjetivo = new JTextField();
		JLabel labelFechaInicio = new JLabel("Fecha de inicio (dd/MM/yyyy):");
		tfFechaInicio = new JTextField();
		JLabel labelFechaFin = new JLabel("Fecha de finalizacion:");
		tfFechaInicio = new JTextField();
		// Crear el modelo de la tabla
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Nombre");
		model.addColumn("Fecha Inicio");
		model.addColumn("Fecha Fin");
		model.addColumn("Objetivo");
		model.addColumn("Descripcion");
		model.addColumn("Actividades");
		model.addColumn("Tipo de Reto");
		model.addColumn("Progreso");

		menuBar = new JMenuBar();
		// Crear elementos del menú
		JMenuItem menuItemEntrenamientos = new JMenuItem("Entrenamientos");
		JMenuItem menuItemRetosDisponibles = new JMenuItem("Retos Disponibles");

		// Configurar ActionListener para cada elemento del menú
		menuItemEntrenamientos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaEntrenamientos v = new VentanaEntrenamientos(token, stravaController);
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

		// Agregar elementos al menú
		menuBar.add(menuItemEntrenamientos);
		menuBar.add(menuItemRetosDisponibles);

		// Configurar la barra de menú en la ventana
		setJMenuBar(menuBar);

		// Agregar datos de los RetoDTOs a la tabla
		List<RetoDTO> retosActivos = stravaController.obtenerRetosActivos(token);
		for (RetoDTO reto : retosActivos) {
			agregarRetoDTOATabla(model, reto);
		}
		// Crear la tabla con el modelo
		table = new JTable(model);

		// Personalizar el renderer para cambiar el color de la fila seleccionada
		CustomTableCellRenderer2 renderer = new CustomTableCellRenderer2();
		ProgressBarRenderer rendererbarra = new ProgressBarRenderer();
		// Aplicar el renderer a todas las columnas de la tabla
		for (int i = 0; i < table.getColumnCount() - 1; i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
		table.getColumnModel().getColumn(7).setCellRenderer(rendererbarra);
		table.getTableHeader().setBackground(Color.DARK_GRAY);
		table.getTableHeader().setForeground(Color.WHITE);
		table.setSelectionBackground(new Color(0, 200, 170));
		// Añadir la tabla a un JScrollPane
		JScrollPane scrollPane = new JScrollPane(table);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBackground(Color.DARK_GRAY);
		tablePanel.setBackground(Color.DARK_GRAY);
		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
					obtenerRetoDTOSeleccionado();
				} else {

				}
			}
		});
		// Agregar paneles al panel principal
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		mainPanel.add(panelDetalles, BorderLayout.NORTH);

		// Agregar el panel principal a la ventana
		getContentPane().add(mainPanel);
	}

	// Método para agregar un RetoDTO al modelo de la tabla
	private void agregarRetoDTOATabla(DefaultTableModel model, RetoDTO RetoDTO) {
		JProgressBar barraProgreso = new JProgressBar(0, 100);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		barraProgreso.setVisible(true);
		barraProgreso.setValue((int) stravaController.obtenerPorcentajeDeReto(RetoDTO, token));
		model.addRow(new Object[] { RetoDTO.getNombre(), dateFormat.format(RetoDTO.getFecha_ini()), dateFormat.format(RetoDTO.getFecha_fin()),
				RetoDTO.getObjetivo(), RetoDTO.getDescripcion(), RetoDTO.getActividades(), RetoDTO.getTipoObjectivo(),
				(int)stravaController.obtenerPorcentajeDeReto(RetoDTO, token)});
		stravaController.crearReto(token, RetoDTO.getObjetivo(), RetoDTO.getDescripcion(), RetoDTO.getNombre(),
				RetoDTO.getFecha_ini(), RetoDTO.getFecha_fin(), RetoDTO.getActividades(), RetoDTO.getTipoObjectivo());
	}

	private RetoDTO obtenerRetoDTOSeleccionado() {
		int filaSeleccionada = table.getSelectedRow();
		if (filaSeleccionada != -1) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			String nombre = (String) model.getValueAt(filaSeleccionada, 0);
			String fechaIniStr = (String) model.getValueAt(filaSeleccionada, 1);
			String fechaFinStr = (String) model.getValueAt(filaSeleccionada, 2);
			int objetivo = (int) model.getValueAt(filaSeleccionada, 3);
			String descripcion = (String) model.getValueAt(filaSeleccionada, 4);
			String actividades = ((String) model.getValueAt(filaSeleccionada, 5));
			String tipo = ((String) model.getValueAt(filaSeleccionada, 6));
			return new RetoDTO(objetivo, descripcion, nombre, actividades, this.parseFecha(fechaIniStr), this.parseFecha(fechaFinStr), tipo);
		} else {
			return null;
		}
	}

	// Método para convertir una cadena de fecha a un objeto Date
	private Date parseFecha(String fechaStr) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			return dateFormat.parse(fechaStr);
		} catch (Exception e) {
			e.printStackTrace(); // Manejo básico de errores, deberías mejorar esto según tus necesidades.
			return null;
		}
	}
}

class CustomTableCellRenderer2 extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1l;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// Configurar el aspecto de la celda
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setText(value.toString());

		// Cambiar el color de fondo si la celda está seleccionada
		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}

		return this;
	}
}

class ProgressBarRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1l;
	private JProgressBar progressBar = new JProgressBar(0, 100);

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// Convierte el valor de la celda a un porcentaje para el progreso de la barra.
		int progress = (int) value;
		progressBar.setValue(progress);
		JLabel valor= new JLabel(Integer.toString(progress));
		progressBar.add(valor);
		valor.setVisible(true);
		valor.setForeground(Color.BLACK);
		progressBar.setForeground(new Color(60,200,255));
		progressBar.setBackground(new Color(255,255,255));
		return progressBar;
	}
}