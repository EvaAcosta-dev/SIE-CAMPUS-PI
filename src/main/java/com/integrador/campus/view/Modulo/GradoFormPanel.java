package com.integrador.campus.view.Modulo;

import com.formdev.flatlaf.FlatClientProperties;
import com.integrador.campus.model.Grado;
import com.integrador.campus.model.Nivel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class GradoFormPanel extends JPanel {

    private final Color azulOscuro = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);
    private final JTextField txtNumeroGrado = new JTextField();
    private final JTextField txtNombreVisible = new JTextField();
    private final JTextField txtBuscar = new JTextField();
    private final JComboBox<Nivel> cmbNivel = new JComboBox<>();
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JButton btnGuardar;
    private final JButton btnActualizar;
    private final JButton btnEliminar;
    private final JButton btnLimpiar;
    private final JButton btnBuscar;
    private final JButton btnMostrarTodos;
    private int idGradoSeleccionado = -1;

    public GradoFormPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        formulario.setPreferredSize(new Dimension(320, 600));

        JLabel titulo = new JLabel("Gestión de Grados");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(azulOscuro);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel descripcion = new JLabel("Registra y administra los grados académicos.");
        descripcion.setForeground(new Color(110, 120, 135));
        descripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        configurarCampo(txtNumeroGrado, "Ej: 1");
        configurarCampo(txtNombreVisible, "Ej: Primero");
        configurarCombo(cmbNivel);

        formulario.add(titulo);
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(descripcion);
        formulario.add(Box.createVerticalStrut(22));
        agregarCampo(formulario, "Número de grado", txtNumeroGrado);
        agregarCampo(formulario, "Nombre visible", txtNombreVisible);
        agregarCampo(formulario, "Nivel académico", cmbNivel);

        btnGuardar = crearBoton("Guardar grado", true);
        btnActualizar = crearBoton("Actualizar", false);
        btnEliminar = crearBoton("Eliminar", false);
        btnLimpiar = crearBoton("Limpiar", false);

        JPanel botones = new JPanel();
        botones.setLayout(new BoxLayout(botones, BoxLayout.Y_AXIS));
        botones.setOpaque(false);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);
        botones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        botones.add(btnGuardar);
        botones.add(Box.createVerticalStrut(8));

        JPanel secundarios = new JPanel(new GridLayout(0, 2, 8, 8));
        secundarios.setOpaque(false);
        secundarios.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        secundarios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));

        secundarios.add(btnActualizar);
        secundarios.add(btnEliminar);
        secundarios.add(btnLimpiar);

        botones.add(secundarios);
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(botones);

        JPanel panelTabla = new JPanel(new BorderLayout(0, 12));
        panelTabla.setOpaque(false);
        JLabel tituloTabla = new JLabel("Grados registrados");
        tituloTabla.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloTabla.setForeground(azulOscuro);
        panelTabla.add(tituloTabla, BorderLayout.NORTH);

        JPanel busqueda = new JPanel(new BorderLayout(8, 0));
        busqueda.setBackground(Color.WHITE);
        busqueda.setBorder(new EmptyBorder(12, 12, 12, 12));
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar por número, nombre o nivel...");
        txtBuscar.putClientProperty(FlatClientProperties.STYLE, "showClearButton: true");
        btnBuscar = new JButton("Buscar");
        btnMostrarTodos = new JButton("Mostrar todos");
        JPanel accionesBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        accionesBusqueda.setOpaque(false);
        accionesBusqueda.add(btnBuscar);
        accionesBusqueda.add(btnMostrarTodos);
        busqueda.add(txtBuscar, BorderLayout.CENTER);
        busqueda.add(accionesBusqueda, BorderLayout.EAST);

        String[] columnas = {"ID", "Número", "Nombre visible", "Nivel"};
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

        JPanel contenidoTabla = new JPanel(new BorderLayout(0, 12));
        contenidoTabla.setOpaque(false);
        contenidoTabla.add(busqueda, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)));
        contenidoTabla.add(scroll, BorderLayout.CENTER);
        panelTabla.add(contenidoTabla, BorderLayout.CENTER);

        add(formulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
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

    public void llenarTablaGrados(List<Grado> grados) {
        modeloTabla.setRowCount(0);
        for (Grado grado : grados) {
            modeloTabla.addRow(new Object[]{
                grado.getIdGrado(),
                grado.getNumeroGrado(),
                grado.getNombreVisible(),
                grado.getNombreNivel()
            });
        }
    }

    public void cargarNiveles(List<Nivel> niveles) {
        cmbNivel.removeAllItems();
        for (Nivel nivel : niveles) {
            cmbNivel.addItem(nivel);
        }
    }

    public void cargarGradoEnFormulario(Grado grado) {
        idGradoSeleccionado = grado.getIdGrado();
        txtNumeroGrado.setText(Short.toString(grado.getNumeroGrado()));
        txtNombreVisible.setText(grado.getNombreVisible());
        for (int i = 0; i < cmbNivel.getItemCount(); i++) {
            if (cmbNivel.getItemAt(i).getIdNivel() == grado.getIdNivel()) {
                cmbNivel.setSelectedIndex(i);
                break;
            }
        }
        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    public void limpiarFormulario() {
        idGradoSeleccionado = -1;
        txtNumeroGrado.setText("");
        txtNombreVisible.setText("");
        if (cmbNivel.getItemCount() > 0) {
            cmbNivel.setSelectedIndex(0);
        }
        tabla.clearSelection();
        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        txtNumeroGrado.requestFocusInWindow();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Grados", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(
                this, mensaje, "Confirmar operación", JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }

    public int getIdGradoSeleccionado() {
        return idGradoSeleccionado;
    }

    public void setIdGradoSeleccionado(int idGrado) {
        idGradoSeleccionado = idGrado;
    }

    public String getNumeroGrado() {
        return txtNumeroGrado.getText().trim();
    }

    public String getNombreVisible() {
        return txtNombreVisible.getText().trim();
    }

    public String getTextoBusqueda() {
        return txtBuscar.getText().trim();
    }

    public Nivel getNivelSeleccionado() {
        return (Nivel) cmbNivel.getSelectedItem();
    }

    public JTable getTabla() {
        return tabla;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public JTextField getTxtNumeroGrado() {
        return txtNumeroGrado;
    }

    public JTextField getTxtNombreVisible() {
        return txtNombreVisible;
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnMostrarTodos() {
        return btnMostrarTodos;
    }
}
