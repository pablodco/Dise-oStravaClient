package cliente.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import cliente.controller.StravaController;
import servidor.dto.RetoDTO;

public class VentanaRetosActivos extends JFrame{
	private JButton boton;
	private JTable table;
	private RetoDTO RetoDTOSeleccionado; // Nueva variable para almacenar el RetoDTO seleccionado
	private StravaController stravaController;
	private long token;
	public VentanaRetosActivos(long token,StravaController stravaController) {
		this.stravaController= stravaController;
		setTitle("RetoDTOs Disponibles");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 400);

		// Crear paneles
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel tablePanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel();

		JLabel label = new JLabel("LISTA DE RetoDTOS", SwingConstants.CENTER);
		mainPanel.add(label, BorderLayout.NORTH);

		// Crear el modelo de la tabla
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Nombre");
		model.addColumn("Fecha Inicio");
		model.addColumn("Fecha Fin");
		model.addColumn("Objetivo");
		model.addColumn("Descripcion");
		model.addColumn("Actividades");

		// Agregar datos de los RetoDTOs a la tabla
		List<RetoDTO> retosDisponibles =stravaController.obtenerRetosDisponibles(token);
		for(RetoDTO reto: retosDisponibles) {
			agregarRetoDTOATabla(model, reto);
		}
		// Crear la tabla con el modelo
		table = new JTable(model);

		// Personalizar el renderer para cambiar el color de la fila seleccionada
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (isSelected) {
					setBackground(Color.GRAY);
				} else {
					setBackground(table.getBackground());
				}
				return this;
			}
		};

		// Aplicar el renderer a todas las columnas de la tabla
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

		// Añadir la tabla a un JScrollPane
		JScrollPane scrollPane = new JScrollPane(table);
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		// Crear el botón
		boton = new JButton("Aceptar RetoDTO");
		boton.setEnabled(false); // Inicialmente deshabilitado

		// Agregar un ActionListener al botón
		boton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Almacena el RetoDTO seleccionado en la variable RetoDTOSeleccionado
				VentanaMenu v= new VentanaMenu(token,stravaController);
				
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

		// Agregar paneles al panel principal
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Agregar el panel principal a la ventana
		getContentPane().add(mainPanel);
	}

	// Método para agregar un RetoDTO al modelo de la tabla
	private void agregarRetoDTOATabla(DefaultTableModel model, RetoDTO RetoDTO) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		model.addRow(new Object[] { RetoDTO.getNombre(),RetoDTO.getFecha_ini(),
				RetoDTO.getFecha_ini(), RetoDTO.getObjetivo(), RetoDTO.getDescripcion(), });
		stravaController.crearReto(token,RetoDTO.getObjetivo(),RetoDTO.getDescripcion(),RetoDTO.getNombre(),RetoDTO.getFecha_ini(),RetoDTO.getFecha_fin(),RetoDTO.getActividades());
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

			// Convertir las cadenas de fecha a objetos Date
			Date fechaIni = parseFecha(fechaIniStr);
			Date fechaFin = parseFecha(fechaFinStr);

			return new RetoDTO(objetivo,nombre, fechaIniStr, fechaFinStr,descripcion ,actividades);
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
