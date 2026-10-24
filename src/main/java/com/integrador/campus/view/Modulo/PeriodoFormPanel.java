package com.integrador.campus.view.Modulo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.integrador.campus.model.AnioLectivo;
import com.formdev.flatlaf.FlatClientProperties;
import com.toedter.calendar.JDateChooser;


public class PeriodoFormPanel extends JPanel {
    private final Color azulOscuro = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);

    public final JTextField txtBuscar;
    public final JTextField txtBuscarAnio;
    public final JSpinner spinnerBuscarOrden;
    public final JTextField txtNombrePeriodo;
    public final JSpinner spinnerOrden;
    public final JDateChooser dateChooserInicio;
    public final JDateChooser dateChooserFin;
    public final JCheckBox chkActivo;

    // Cambiar 'AnioLectivo' por la clase entidad correspondiente en tu proyecto
    public final JComboBox<AnioLectivo> comboBoxAnioLectivo;

    public final JTable tabla;
    private final DefaultTableModel modeloTabla;

    public final JButton btnGuardarPeriodo;
    public final JButton btnActualizarPeriodo;
    public final JButton btnEliminarPeriodo;
    public final JButton btnLimpiarPeriodo;
    public final JButton btnBuscarPeriodo;
    public final JButton btnMostrarPeriodos;

    private int idPeriodoSeleccionado = -1;

    public PeriodoFormPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // FORMULARIO DE PERIODOS (IZQUIERDA)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Gestión de Periodos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("Registra y administra los periodos escolares.");
        lblDescripcion.setForeground(new Color(110, 120, 135));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombrePeriodo = crearCampo("Ej: Primer Trimestre, Semestre I");
        
        // JSpinner limitado de 1 a 10 para mapear la columna 'orden'
        spinnerOrden = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        configurarComponenteGenerico(spinnerOrden);
        
        dateChooserInicio = crearSelectorFecha();
        dateChooserFin = crearSelectorFecha();
        
        chkActivo = new JCheckBox("Permitir carga de notas");
        chkActivo.setBackground(Color.WHITE);
        chkActivo.setAlignmentX(Component.LEFT_ALIGNMENT);

        comboBoxAnioLectivo = new JComboBox<>();
        configurarCombo(comboBoxAnioLectivo);

        btnGuardarPeriodo = crearBoton("Guardar periodo", true);
        btnActualizarPeriodo = crearBoton("Actualizar", false);
        btnEliminarPeriodo = crearBoton("Eliminar", false);
        btnLimpiarPeriodo = crearBoton("Limpiar", false);

        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(Box.createVerticalStrut(22));

        agregarCampo(panelFormulario, "Año Lectivo", comboBoxAnioLectivo);
        agregarCampo(panelFormulario, "Nombre del Periodo", txtNombrePeriodo);
        agregarCampo(panelFormulario, "Orden secuencia", spinnerOrden);
        agregarCampo(panelFormulario, "Fecha de Inicio", dateChooserInicio);
        agregarCampo(panelFormulario, "Fecha de Fin", dateChooserFin);
        
        // Espaciador y agregado del Checkbox mapeado a 'activo' tinyint(1)
        panelFormulario.add(Box.createVerticalStrut(10));
        panelFormulario.add(chkActivo);
        panelFormulario.add(Box.createVerticalStrut(20));

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnGuardarPeriodo.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardarPeriodo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        panelBotones.add(btnGuardarPeriodo);
        panelBotones.add(Box.createVerticalStrut(8));

        JPanel panelBotonesSecundarios = new JPanel(new GridLayout(0, 2, 8, 8));
        panelBotonesSecundarios.setOpaque(false);
        panelBotonesSecundarios.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelBotonesSecundarios.add(btnActualizarPeriodo);
        panelBotonesSecundarios.add(btnEliminarPeriodo);
        panelBotonesSecundarios.add(btnLimpiarPeriodo);

        panelBotones.add(panelBotonesSecundarios);

        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(panelBotones);

        // TABLA Y BÚSQUEDA (DERECHA)
        JPanel panelTabla = new JPanel(new BorderLayout(0, 15));
        panelTabla.setBackground(fondo);

        JPanel encabezadoTabla = new JPanel(new BorderLayout());
        encabezadoTabla.setOpaque(false);

        JLabel lblLista = new JLabel("Periodos registrados");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLista.setForeground(azulOscuro);
        encabezadoTabla.add(lblLista, BorderLayout.WEST);

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new EmptyBorder(12, 12, 12, 12));

        txtBuscar = new JTextField();
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Periodo (opcional)");
        txtBuscar.putClientProperty(FlatClientProperties.STYLE, "showClearButton: true");
        txtBuscar.setPreferredSize(new Dimension(180, 36));

        txtBuscarAnio = new JTextField();
        txtBuscarAnio.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Año");
        txtBuscarAnio.setPreferredSize(new Dimension(90, 36));

        spinnerBuscarOrden = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        spinnerBuscarOrden.setPreferredSize(new Dimension(90, 36));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelFiltros.setOpaque(false);
        panelFiltros.add(txtBuscar);
        panelFiltros.add(crearEtiquetaPequena("Año"));
        panelFiltros.add(txtBuscarAnio);
        panelFiltros.add(crearEtiquetaPequena("Orden (0 = todos)"));
        panelFiltros.add(spinnerBuscarOrden);

        btnBuscarPeriodo = new JButton("Buscar");
        btnMostrarPeriodos = new JButton("Mostrar todos");

        JPanel accionesBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        accionesBusqueda.setOpaque(false);
        accionesBusqueda.add(btnBuscarPeriodo);
        accionesBusqueda.add(btnMostrarPeriodos);

        panelBusqueda.add(panelFiltros, BorderLayout.CENTER);
        panelBusqueda.add(accionesBusqueda, BorderLayout.EAST);

        // Mapeo directo de la consulta de la tabla 'periodos'
        String[] columnas = {"ID", "Periodo", "Año Lectivo", "Orden", "F. Inicio", "F. Fin", "Notas Activas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(34);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)));

        panelTabla.add(encabezadoTabla, BorderLayout.NORTH);

        JPanel centroTabla = new JPanel(new BorderLayout(0, 12));
        centroTabla.setOpaque(false);
        centroTabla.add(panelBusqueda, BorderLayout.NORTH);
        centroTabla.add(scroll, BorderLayout.CENTER);

        panelTabla.add(centroTabla, BorderLayout.CENTER);

        JScrollPane scrollFormulario = new JScrollPane(panelFormulario);
        scrollFormulario.setPreferredSize(new Dimension(340, 600));
        scrollFormulario.setBorder(BorderFactory.createEmptyBorder());
        scrollFormulario.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollFormulario.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);

        // Estado inicial de botones de acción
        btnActualizarPeriodo.setEnabled(false);
        btnEliminarPeriodo.setEnabled(false);
    }

    // MÉTODOS DE APOYO Y ESTILO 
    private JTextField crearCampo(String placeholder) {
        JTextField campo = new JTextField();
        configurarCampo(campo, placeholder);
        return campo;
    }

    private JDateChooser crearSelectorFecha() {
        JDateChooser selector = new JDateChooser();
        selector.setDateFormatString("yyyy-MM-dd");
        selector.setPreferredSize(new Dimension(260, 36));
        selector.setAlignmentX(Component.LEFT_ALIGNMENT);
        selector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return selector;
    }

    private void configurarCampo(JTextField campo, String placeholder) {
        campo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    }

    private void configurarCombo(JComboBox<?> combo) {
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    }

    private void configurarComponenteGenerico(JComponent componente) {
        componente.setAlignmentX(Component.LEFT_ALIGNMENT);
        componente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    }

    private JButton crearBoton(String texto, boolean principal) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (principal) {
            boton.setBackground(azulOscuro);
            boton.setForeground(Color.WHITE);
            boton.putClientProperty(FlatClientProperties.STYLE, "buttonType: bold");
        }
        return boton;
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(azulOscuro);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(6));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(14));
    }

    private JLabel crearEtiquetaPequena(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(90, 100, 115));
        return lbl;
    }
}
