package com.integrador.campus.view.Modulo;

import com.formdev.flatlaf.FlatClientProperties;
import com.integrador.campus.model.Estudiante;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class EstudiantePanel extends JPanel {

    private final Color azulOscuro = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);

    private final JTextField txtBuscar;
    private final JTextField txtNombre;
    private final JTextField txtApellido;
    private final JTextField txtDocumento;
    private final JTextField txtTelefono;
    private final JTextField txtCorreo;
    private final JPasswordField txtPassword;
    private final JDateChooser dateNacimiento;

    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    private final JButton btnGuardar;
    private final JButton btnActualizar;
    private final JButton btnEliminar;
    private final JButton btnLimpiar;
    private final JButton btnBuscar;
    private final JButton btnMostrarTodos;

    private final JScrollPane scrollFormulario;

    private int idEstudianteSeleccionado = -1;

    public EstudiantePanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // FORMULARIO
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Gestión de estudiantes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("Registra al estudiante y su acceso.");
        lblDescripcion.setForeground(new Color(110, 120, 135));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombre = crearCampo("Nombres");
        txtApellido = crearCampo("Apellidos");
        txtDocumento = crearCampo("Número de documento");
        txtTelefono = crearCampo("Teléfono (opcional)");
        txtCorreo = crearCampo("correo@ejemplo.com");

        dateNacimiento = new JDateChooser();
        dateNacimiento.setDateFormatString("yyyy-MM-dd");
        dateNacimiento.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateNacimiento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        txtPassword = new JPasswordField();
        configurarCampo(txtPassword, "Contraseña (opcional al editar)");
        txtPassword.putClientProperty(
            FlatClientProperties.STYLE, "showRevealButton: true");

        btnGuardar = crearBoton("Guardar estudiante");
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
        agregarCampo(panelFormulario, "Fecha de nacimiento", dateNacimiento);
        agregarCampo(panelFormulario, "Teléfono", txtTelefono);
        agregarCampo(panelFormulario, "Correo electrónico (para iniciar sesión)", txtCorreo);
        agregarCampo(panelFormulario, "Contraseña", txtPassword);

        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panelFormulario.add(btnGuardar);
        panelFormulario.add(Box.createVerticalStrut(8));

        JPanel panelSecundarios = new JPanel(new GridLayout(0, 2, 8, 8));
        panelSecundarios.setOpaque(false);
        panelSecundarios.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSecundarios.add(btnActualizar);
        panelSecundarios.add(btnEliminar);
        panelSecundarios.add(btnLimpiar);
        panelFormulario.add(panelSecundarios);

        scrollFormulario = new JScrollPane(panelFormulario,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollFormulario.setBorder(null);
        scrollFormulario.getVerticalScrollBar().setUnitIncrement(16);
        scrollFormulario.setPreferredSize(new Dimension(340, 0));

        // TABLA Y BÚSQUEDA
        JPanel panelTabla = new JPanel(new BorderLayout(0, 15));
        panelTabla.setBackground(fondo);

        JLabel lblLista = new JLabel("Estudiantes registrados");
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

        String[] columnas = {
            "ID", "Nombres", "Apellidos", "Documento",
            "Nacimiento", "Teléfono", "Correo"
        };

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

    public void mostrarEstudiantes(List<Estudiante> lista) {
        modeloTabla.setRowCount(0);
        for (Estudiante e : lista) {
            modeloTabla.addRow(new Object[]{
                e.getIdEstudiante(),
                e.getNombre(),
                e.getApellido(),
                e.getNumDocumento(),
                e.getFechaNacimiento() != null ? e.getFechaNacimiento().toString() : "",
                e.getTelefono() != null ? e.getTelefono() : "",
                e.getCorreo() != null ? e.getCorreo() : "(sin usuario)"
            });
        }
    }

    public void cargarEnFormulario(Estudiante e) {
        idEstudianteSeleccionado = e.getIdEstudiante();

        txtNombre.setText(e.getNombre());
        txtApellido.setText(e.getApellido());
        txtDocumento.setText(e.getNumDocumento());
        dateNacimiento.setDate(e.getFechaNacimiento());
        txtTelefono.setText(e.getTelefono() != null ? e.getTelefono() : "");
        txtCorreo.setText(e.getCorreo() != null ? e.getCorreo() : "");
        txtPassword.setText(""); // nunca se muestra la contraseña guardada

        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    public void limpiarFormulario() {
        idEstudianteSeleccionado = -1;

        txtNombre.setText("");
        txtApellido.setText("");
        txtDocumento.setText("");
        dateNacimiento.setDate(null);
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        tabla.clearSelection();

        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        txtNombre.requestFocusInWindow();
    }

    // Para roles que solo consultan (ej. docente): oculta el formulario
    public void setSoloLectura() {
        scrollFormulario.setVisible(false);
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

    public int getIdEstudianteSeleccionado() { return idEstudianteSeleccionado; }
    public String getNombre() { return txtNombre.getText().trim(); }
    public String getApellido() { return txtApellido.getText().trim(); }
    public String getDocumento() { return txtDocumento.getText().trim(); }
    public String getTelefono() { return txtTelefono.getText().trim(); }
    public String getCorreo() { return txtCorreo.getText().trim(); }
    public String getContrasena() { return new String(txtPassword.getPassword()); }
    public Date getFechaNacimiento() { return dateNacimiento.getDate(); }
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