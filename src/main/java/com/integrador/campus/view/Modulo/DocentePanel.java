package com.integrador.campus.view.Modulo;

import com.formdev.flatlaf.FlatClientProperties;
import com.integrador.campus.model.Docente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DocentePanel extends JPanel {

    private final Color azulOscuro = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);

    private final JTextField txtBuscar;
    private final JTextField txtNombre;
    private final JTextField txtApellido;
    private final JTextField txtDocumento;
    private final JTextField txtCorreo;
    private final JPasswordField txtPassword;

    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    private final JButton btnGuardar;
    private final JButton btnActualizar;
    private final JButton btnEliminar;
    private final JButton btnLimpiar;
    private final JButton btnBuscar;
    private final JButton btnMostrarTodos;

    public DocentePanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // FORMULARIO
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Gestión de docentes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("Registra al docente y su acceso.");
        lblDescripcion.setForeground(new Color(110, 120, 135));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombre = crearCampo("Nombres");
        txtApellido = crearCampo("Apellidos");
        txtDocumento = crearCampo("Número de documento");
        txtCorreo = crearCampo("correo@ejemplo.com");

        txtPassword = new JPasswordField();
        configurarCampo(txtPassword, "Contraseña (opcional al editar)");
        txtPassword.putClientProperty(
            FlatClientProperties.STYLE, "showRevealButton: true");

        btnGuardar = crearBoton("Guardar docente");
        btnActualizar = crearBoton("Actualizar");
        btnEliminar = crearBoton("Eliminar");
        btnLimpiar = crearBoton("Limpiar");

        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(Box.createVerticalStrut(18));

        agregarCampo(panelFormulario, "Nombres", txtNombre);
        agregarCampo(panelFormulario, "Apellidos", txtApellido);
        agregarCampo(panelFormulario, "Documento", txtDocumento);
        agregarCampo(panelFormulario, "Correo electrónico (para iniciar sesión)", txtCorreo);
        agregarCampo(panelFormulario, "Contraseña", txtPassword);

        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panelFormulario.add(btnGuardar);
        panelFormulario.add(Box.createVerticalStrut(8));

        JPanel panelSecundarios = new JPanel(new GridLayout(2, 2, 8, 8));
        panelSecundarios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));
        panelSecundarios.setOpaque(false);
        panelSecundarios.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSecundarios.add(btnActualizar);
        panelSecundarios.add(btnEliminar);
        panelSecundarios.add(btnLimpiar);
        panelFormulario.add(panelSecundarios);

        JScrollPane scrollFormulario = new JScrollPane(panelFormulario,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollFormulario.setBorder(null);
        scrollFormulario.getVerticalScrollBar().setUnitIncrement(16);
        scrollFormulario.setPreferredSize(new Dimension(340, 0));

        // TABLA Y BÚSQUEDA
        JPanel panelTabla = new JPanel(new BorderLayout(0, 15));
        panelTabla.setBackground(fondo);

        JLabel lblLista = new JLabel("Docentes registrados");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLista.setForeground(azulOscuro);

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new EmptyBorder(12, 12, 12, 12));

        txtBuscar = new JTextField();
        txtBuscar.putClientProperty(
            FlatClientProperties.PLACEHOLDER_TEXT,
            "Buscar por nombre, apellido, documento o correo...");
        txtBuscar.putClientProperty(
            FlatClientProperties.STYLE, "showClearButton: true");

        btnBuscar = new JButton("Buscar");
        btnMostrarTodos = new JButton("Mostrar todos");

        JPanel accionesBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        accionesBusqueda.setOpaque(false);
        accionesBusqueda.add(btnBuscar);
        accionesBusqueda.add(btnMostrarTodos);

        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(accionesBusqueda, BorderLayout.EAST);

        String[] columnas = {"ID", "Nombres", "Apellidos", "Documento", "Correo"};

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
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)));

        JPanel centroTabla = new JPanel(new BorderLayout(0, 12));
        centroTabla.setOpaque(false);
        centroTabla.add(panelBusqueda, BorderLayout.NORTH);
        centroTabla.add(scroll, BorderLayout.CENTER);

        panelTabla.add(lblLista, BorderLayout.NORTH);
        panelTabla.add(centroTabla, BorderLayout.CENTER);

        add(scrollFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    // ESTILO

    private JTextField crearCampo(String placeholder) {
        JTextField campo = new JTextField();
        configurarCampo(campo, placeholder);
        return campo;
    }

    private void configurarCampo(JTextField campo, String placeholder) {
        campo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBackground(azulOscuro);
        boton.setForeground(Color.WHITE);
        boton.putClientProperty(FlatClientProperties.STYLE, "buttonType: bold");
        return boton;
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo) {
        JLabel label = new JLabel(etiqueta);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(10));
    }

    // MOSTRAR INFORMACIÓN

    public void mostrarDocentes(List<Docente> lista) {
        modeloTabla.setRowCount(0);
        for (Docente d : lista) {
            modeloTabla.addRow(new Object[]{
                d.getIdDocente(),
                d.getNombre(),
                d.getApellido(),
                d.getDocumento(),
                d.getCorreo() != null ? d.getCorreo() : "(sin usuario)"
            });
        }
    }

    public void cargarEnFormulario(Docente d) {
        txtNombre.setText(d.getNombre());
        txtApellido.setText(d.getApellido());
        txtDocumento.setText(d.getDocumento());
        txtCorreo.setText(d.getCorreo() != null ? d.getCorreo() : "");
        txtPassword.setText(""); // nunca se muestra la contraseña guardada

        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    public void limpiarFormulario() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDocumento.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        tabla.clearSelection();

        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        txtNombre.requestFocusInWindow();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar operación",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    // GETTERS

    public String getNombre() { return txtNombre.getText().trim(); }
    public String getApellido() { return txtApellido.getText().trim(); }
    public String getDocumento() { return txtDocumento.getText().trim(); }
    public String getCorreo() { return txtCorreo.getText().trim(); }
    public String getContrasena() { return new String(txtPassword.getPassword()); }
    public String getTextoBusqueda() { return txtBuscar.getText().trim(); }

    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnMostrarTodos() { return btnMostrarTodos; }
    public JTextField getTxtBuscar() { return txtBuscar; }
}