package cliente.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import cliente.controller.StravaController;
import servidor.dto.RetoDTO;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VentanaRetosDisponibles extends JFrame {
	private static final long serialVersionUID = 1l;
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
	private JComboBox<String> actividades;
	private JComboBox<String> tipo;
	private long token;

	public VentanaRetosDisponibles(long token, StravaController stravaController) {
		this.stravaController = stravaController;
		setTitle("RetoDTOs Disponibles");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);

		// Crear paneles
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel tablePanel = new JPanel(new BorderLayout());
		JPanel panelDetalles = new JPanel(new GridLayout(8, 2));
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
		tfFechaInicio = new JTextField("dd/MM/yyyy");
		JLabel labelFechaFin = new JLabel("Fecha de finalizacion:");
		tfFechaFin = new JTextField("dd/MM/yyyy");
		JButton botonReto = new JButton("Crear Reto");
		botonReto.setEnabled(false);
		tfObjetivo.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
					tfObjetivo.setText(tfObjetivo.getText() + e.getKeyChar());
					e.consume();
				} else {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		// Crear el modelo de la tabla
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Nombre");
		model.addColumn("Fecha Inicio");
		model.addColumn("Fecha Fin");
		model.addColumn("Objetivo");
		model.addColumn("Descripcion");
		model.addColumn("Actividades");
		model.addColumn("Tipo de Reto");

		menuBar = new JMenuBar();
		// Crear elementos del men�
		JMenuItem menuItemEntrenamientos = new JMenuItem("Entrenamientos");
		JMenuItem menuItemRetosActivos = new JMenuItem("Retos Activos");

		// Configurar ActionListener para cada elemento del men�
		menuItemEntrenamientos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaEntrenamientos v = new VentanaEntrenamientos(token, stravaController);
				v.setVisible(true);
			}
		});

		menuItemRetosActivos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaRetosActivos v = new VentanaRetosActivos(token, stravaController);
				v.setVisible(true);
			}
		});

		// Agregar elementos al men�
		menuBar.add(menuItemEntrenamientos);
		menuBar.add(menuItemRetosActivos);

		// Configurar la barra de men� en la ventana
		setJMenuBar(menuBar);

		// Agregar datos de los RetoDTOs a la tabla
		List<RetoDTO> retosDisponibles = stravaController.obtenerRetosDisponibles(token);
		for (RetoDTO reto : retosDisponibles) {
			agregarRetoDTOATabla(model, reto);
		}
		// Crear la tabla con el modelo
		table = new JTable(model);

		// Personalizar el renderer para cambiar el color de la fila seleccionada
		CustomTableCellRenderer renderer = new CustomTableCellRenderer();

		// Aplicar el renderer a todas las columnas de la tabla
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
		table.getTableHeader().setBackground(Color.DARK_GRAY);
		table.getTableHeader().setForeground(Color.WHITE);
		table.setSelectionBackground(new Color(200, 200, 25));
		// A�adir la tabla a un JScrollPane
		JScrollPane scrollPane = new JScrollPane(table);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBackground(Color.DARK_GRAY);
		tablePanel.setBackground(Color.DARK_GRAY);
		// Crear el bot�n
		boton = new JButton("Aceptar RetoDTO");
		boton.setEnabled(false); // Inicialmente deshabilitado

		// Agregar un ActionListener al bot�n
		boton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Almacena el RetoDTO seleccionado en la variable RetoDTOSeleccionado

				RetoDTOSeleccionado = obtenerRetoDTOSeleccionado();
				System.out.println(RetoDTOSeleccionado);
				if (stravaController.aceptarReto(token, RetoDTOSeleccionado.getNombre())) {
					JOptionPane.showMessageDialog(VentanaRetosDisponibles.this,
							" Reto aceptado: " + RetoDTOSeleccionado.getNombre());
				} else {
					System.err.println(" EL reto no ha sido aceptado");
					JOptionPane.showMessageDialog(VentanaRetosDisponibles.this,
							" Reto no aceptado: " + RetoDTOSeleccionado.getNombre());
				}

			}
		});
		botonReto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
					agregarRetoDTOATabla(model, new RetoDTO (Integer.parseInt(tfObjetivo.getText()),tfDescripcion.getText(),
							 tfNombre.getText(),(String)actividades.getSelectedItem(),dt.parse(tfFechaInicio.getText()),dt.parse(tfFechaFin.getText()),(String)tipo.getSelectedItem()));
//					 stravaController.crearReto(token,Integer.parseInt(tfObjetivo.getText()),tfDescripcion.getText(),
//					 tfNombre.getText(), dt.parse(tfFechaInicio.getText()),
//					 dt.parse(tfFechaFin.getText()),(String)actividades.getSelectedItem(),(String)tipo.getSelectedItem());
				} catch (ParseException ex) {
					ex.printStackTrace();
				}
			}
		});

		buttonPanel.add(boton);
		ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
					boton.setEnabled(true);
				} else {
					boton.setEnabled(false);
				}
			}
		});
		String[] actividad = { "running", "ciclismo", "running,ciclismo" };
		String[] tipos = { "DISTANCIA","TIEMPO" };
		actividades = new JComboBox<String>(actividad);
		tipo = new JComboBox<String>(tipos);
		panelDetalles.add(labelNombre);
		panelDetalles.add(tfNombre);
		panelDetalles.add(labelObjetivo);
		panelDetalles.add(tfObjetivo);
		panelDetalles.add(labelDescripcion);
		panelDetalles.add(tfDescripcion);
		panelDetalles.add(labelFechaInicio);
		panelDetalles.add(tfFechaInicio);
		panelDetalles.add(labelFechaFin);
		panelDetalles.add(tfFechaFin);
		panelDetalles.add(actividades);
		panelDetalles.add(tipo);
		panelDetalles.add(botonReto);
		if (tfNombre.getText() != "" && tfDescripcion.getText() != "" && tfObjetivo.getText() != ""
				&& tfFechaInicio.getText() != "" && tfFechaFin.getText() != "") {
			botonReto.setEnabled(true);
		} else {
			botonReto.setEnabled(false);
		}
		// Agregar paneles al panel principal
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		panelPrincipal.add(panelDetalles, BorderLayout.NORTH);
		panelPrincipal.add(tablePanel, BorderLayout.CENTER);
		mainPanel.add(panelPrincipal, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Agregar el panel principal a la ventana
		getContentPane().add(mainPanel);
	}

	// M�todo para agregar un RetoDTO al modelo de la tabla
	private void agregarRetoDTOATabla(DefaultTableModel model, RetoDTO RetoDTO) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		model.addRow(new Object[] { RetoDTO.getNombre(), dateFormat.format(RetoDTO.getFecha_ini()),dateFormat.format(RetoDTO.getFecha_fin()),
				RetoDTO.getObjetivo(), RetoDTO.getDescripcion(), RetoDTO.getActividades(),
				RetoDTO.getTipoObjectivo() });
		stravaController.crearReto(token, RetoDTO.getObjetivo(), RetoDTO.getDescripcion(), RetoDTO.getNombre(),
				RetoDTO.getFecha_ini(), RetoDTO.getFecha_fin(), RetoDTO.getActividades(),
				RetoDTO.getTipoObjectivo().toString());
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
			String tipoObjetivo = ((String) model.getValueAt(filaSeleccionada, 6));
			return new RetoDTO(objetivo, descripcion, nombre, actividades, this.parseFecha(fechaIniStr), this.parseFecha(fechaFinStr), tipoObjetivo);
		} else {
			return null;
		}
	}

	// M�todo para convertir una cadena de fecha a un objeto Date
	private Date parseFecha(String fechaStr) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			return dateFormat.parse(fechaStr);
		} catch (Exception e) {
			e.printStackTrace(); // Manejo b�sico de errores, deber�as mejorar esto seg�n tus necesidades.
			return null;
		}
	}
}

class CustomTableCellRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 1l;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
// Configurar el aspecto de la celda
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setText(value.toString());

// Cambiar el color de fondo si la celda est� seleccionada
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