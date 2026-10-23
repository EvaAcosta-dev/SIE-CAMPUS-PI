package com.integrador.campus.view.Modulo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;
import com.integrador.campus.model.AnioLectivo;
import com.integrador.campus.model.Cursos;
import com.integrador.campus.model.Docente;
import com.integrador.campus.model.Jornada;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.util.List;

public class AnioFormPanel extends JPanel{
    private final Color azulOscuro = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);

    public JTextField txtAnio;
    public JDateChooser dateChooserInicio;
    public JDateChooser dateChooserFin;
    public JComboBox<String> cmbEstado;
    public final JTextField txtBuscarAnio;

    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    public final JButton btnGuardarAnio;
    public final JButton btnActualizarAnio;
    public final JButton btnEliminarAnio;
    public final JButton btnLimpiarAnio;
    public final JButton btnBuscarAnio;
    public final JButton btnMostrarAnio;
    public final JButton btnCambiarEstadoAnio;

    private int idAnioSeleccionado = -1;

    public AnioFormPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        //  FORMULARIO DE CURSOS (IZQUIERDA)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelFormulario.setPreferredSize(new Dimension(300, 600));

        JLabel lblTitulo = new JLabel("Gestión de Años Lectivos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("Registra y administra los años lectivos.");
        lblDescripcion.setForeground(new Color(110, 120, 135));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtAnio = crearCampo("Ej: 2026");

        dateChooserInicio = new JDateChooser();
        configurarCalendario(dateChooserInicio);
        dateChooserFin = new JDateChooser();
        configurarCalendario(dateChooserFin);

        cmbEstado = new JComboBox<>();
        cmbEstado.addItem("Activo");
        cmbEstado.addItem("Inactivo");
        configurarCombo(cmbEstado);

        btnGuardarAnio = crearBoton("Guardar Año", true);
        btnActualizarAnio = crearBoton("Actualizar", false);
        btnEliminarAnio = crearBoton("Eliminar", false);
        btnCambiarEstadoAnio = crearBoton("Cambiar estado", false);
        btnLimpiarAnio = crearBoton("Limpiar", false);

        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(Box.createVerticalStrut(22));

        agregarCampo(panelFormulario, "Año Lectivo", txtAnio);
        agregarCampo(panelFormulario, "Fecha de Inicio", dateChooserInicio);
        agregarCampo(panelFormulario, "Fecha de Fin", dateChooserFin);
        agregarCampo(panelFormulario, "Estado", cmbEstado);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnGuardarAnio.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardarAnio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        panelBotones.add(btnGuardarAnio);
        panelBotones.add(Box.createVerticalStrut(8));

        JPanel panelBotonesSecundarios = new JPanel(new GridLayout(0, 2, 8, 8));
        panelBotonesSecundarios.setOpaque(false);
        panelBotonesSecundarios.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBotonesSecundarios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));

        panelBotonesSecundarios.add(btnActualizarAnio);
        panelBotonesSecundarios.add(btnEliminarAnio);
        panelBotonesSecundarios.add(btnCambiarEstadoAnio);
        panelBotonesSecundarios.add(btnLimpiarAnio);

        panelBotones.add(panelBotonesSecundarios);

        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(panelBotones);

        // TABLA Y BÚSQUEDA (DERECHA)
        JPanel panelTabla = new JPanel(new BorderLayout(0, 15));
        panelTabla.setBackground(fondo);

        JPanel encabezadoTabla = new JPanel(new BorderLayout());
        encabezadoTabla.setOpaque(false);

        JLabel lblLista = new JLabel("Años lectivos registrados"); // Ajustado título
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLista.setForeground(azulOscuro);
        encabezadoTabla.add(lblLista, BorderLayout.WEST);

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new EmptyBorder(12, 12, 12, 12));

        txtBuscarAnio = new JTextField();
        txtBuscarAnio.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar por año lectivo...");
        txtBuscarAnio.putClientProperty(FlatClientProperties.STYLE, "showClearButton: true");

        btnBuscarAnio = new JButton("Buscar");
        btnMostrarAnio = new JButton("Mostrar todos");

        JPanel accionesBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        accionesBusqueda.setOpaque(false);
        accionesBusqueda.add(btnBuscarAnio);
        accionesBusqueda.add(btnMostrarAnio);

        panelBusqueda.add(txtBuscarAnio, BorderLayout.CENTER);
        panelBusqueda.add(accionesBusqueda, BorderLayout.EAST);

        String[] columnas = {"ID", "Año", "F.Inicio", "F.Fin","Estado"};
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
        scrollFormulario.setBorder(null);
        scrollFormulario.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollFormulario.getVerticalScrollBar().setUnitIncrement(16);
        scrollFormulario.setPreferredSize(new Dimension(300, 0));
        
        add(scrollFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);

        // Estado inicial de botones de acción
        btnActualizarAnio.setEnabled(false);
        btnEliminarAnio.setEnabled(false);
        btnCambiarEstadoAnio.setEnabled(false);
    }

    // MÉTODOS DE APOYO Y ESTILO VISUAL
    private JTextField crearCampo(String placeholder) {
        JTextField campo = new JTextField();
        configurarCampo(campo, placeholder);
        return campo;
    }

    public boolean confirmar(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                mensaje,
                "Confirmar acción",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        return respuesta == JOptionPane.YES_OPTION;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Años lectivos",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void configurarCampo(JTextField campo, String placeholder) {
        campo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    }

    private void configurarCalendario(JDateChooser calendario) {
        calendario.setDateFormatString("yyyy-MM-dd");
        calendario.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Garantiza que se expanda horizontalmente de forma responsiva bajo BoxLayout
        calendario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        calendario.setBackground(Color.WHITE);
    }

    private void configurarCombo(JComboBox<?> combo) {
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
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
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(12));
    }

    public JTable getTabla() {
        return tabla;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JButton getBtnGuardarAnio() {
        return btnGuardarAnio;
    }

    public JButton getBtnActualizarAnio() {
        return btnActualizarAnio;
    }

    public JButton getBtnEliminarAnio() {
        return btnEliminarAnio;
    }

    public JButton getBtnLimpiarAnio() {
        return btnLimpiarAnio;
    }

    public JButton getBtnBuscarAnio() {
        return btnBuscarAnio;
    }

    public JButton getBtnMostrarAnio() {
        return btnMostrarAnio;
    }

    public JButton getBtnCambiarEstadoAnio() {
        return btnCambiarEstadoAnio;
    }

    public JDateChooser getDateChooserInicio() {
        return dateChooserInicio;
    }

    public void setDateChooserInicio(JDateChooser dateChooserInicio) {
        this.dateChooserInicio = dateChooserInicio;
    }

    public JDateChooser getDateChooserFin() {
        return dateChooserFin;
    }

    public void setDateChooserFin(JDateChooser dateChooserFin) {
        this.dateChooserFin = dateChooserFin;
    }

    public JComboBox<String> getCmbEstado() {
        return cmbEstado;
    }

    public JTextField getTxtAnio() {
        return txtAnio;
    }

    public JTextField getTxtBuscarAnio() {
        return txtBuscarAnio;
    }




    
}