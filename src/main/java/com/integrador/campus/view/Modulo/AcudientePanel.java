package com.integrador.campus.view.Modulo;

import com.formdev.flatlaf.FlatClientProperties;
import com.integrador.campus.model.Estudiante;
import com.integrador.campus.model.Padre;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AcudientePanel extends JPanel {

    private final Color azulOscuro = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);

    private final JTextField txtBuscar;
    private final JTextField txtNombre;
    private final JTextField txtApellido;
    private final JTextField txtTelefono;
    private final JTextField txtCorreo;
    private final JPasswordField txtPassword;

    // NUEVO: estudiante obligatorio al registrar un acudiente
    private final JComboBox<Estudiante> comboEstudianteNuevo;
    private final JComboBox<String> comboParentescoNuevo;

    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    private final JButton btnGuardar;
    private final JButton btnActualizar;
    private final JButton btnEliminar;
    private final JButton btnLimpiar;
    private final JButton btnBuscar;
    private final JButton btnMostrarTodos;

    // Sección de estudiantes vinculados
    private final JLabel lblAcudienteSeleccionado;
    private final JTable tablaVinculos;
    private final DefaultTableModel modeloVinculos;
    private final JComboBox<Estudiante> comboEstudiante;
    private final JComboBox<String> comboParentesco;
    private final JButton btnVincular;
    private final JButton btnQuitarVinculo;

    private final JScrollPane scrollFormulario;

    // NUEVO: se ejecuta cada vez que el panel se muestra
    private Runnable alMostrar;

    public AcudientePanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // FORMULARIO
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Gestión de acudientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("Registra al acudiente y su acceso.");
        lblDescripcion.setForeground(new Color(110, 120, 135));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombre = crearCampo("Nombres");
        txtApellido = crearCampo("Apellidos");
        txtTelefono = crearCampo("Teléfono (opcional)");
        txtCorreo = crearCampo("correo@ejemplo.com");

        txtPassword = new JPasswordField();
        configurarCampo(txtPassword, "Contraseña (opcional al editar)");
        txtPassword.putClientProperty(
            FlatClientProperties.STYLE, "showRevealButton: true");

        // NUEVO
        comboEstudianteNuevo = new JComboBox<>();
        comboEstudianteNuevo.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboEstudianteNuevo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        comboParentescoNuevo = new JComboBox<>(new String[]{"padre", "madre", "acudiente"});
        comboParentescoNuevo.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboParentescoNuevo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        btnGuardar = crearBoton("Guardar acudiente");
        btnActualizar = crearBoton("Actualizar");
        btnEliminar = crearBoton("Eliminar");
        btnLimpiar = crearBoton("Limpiar");

        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(Box.createVerticalStrut(18));

        agregarCampo(panelFormulario, "Nombres", txtNombre);
        agregarCampo(panelFormulario, "Apellidos", txtApellido);
        agregarCampo(panelFormulario, "Teléfono", txtTelefono);
        agregarCampo(panelFormulario, "Correo electrónico (para iniciar sesión)", txtCorreo);
        agregarCampo(panelFormulario, "Contraseña", txtPassword);
        agregarCampo(panelFormulario, "Estudiante a cargo (obligatorio)", comboEstudianteNuevo); // NUEVO
        agregarCampo(panelFormulario, "Parentesco", comboParentescoNuevo); // NUEVO

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

        scrollFormulario = new JScrollPane(panelFormulario,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollFormulario.setBorder(null);
        scrollFormulario.getVerticalScrollBar().setUnitIncrement(16);
        scrollFormulario.setPreferredSize(new Dimension(340, 0));

        // TABLA DE ACUDIENTES (arriba a la derecha)
        JPanel panelAcudientes = new JPanel(new BorderLayout(0, 10));
        panelAcudientes.setBackground(fondo);

        JLabel lblLista = new JLabel("Acudientes registrados");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLista.setForeground(azulOscuro);

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new EmptyBorder(10, 10, 10, 10));

        txtBuscar = new JTextField();
        txtBuscar.putClientProperty(
            FlatClientProperties.PLACEHOLDER_TEXT,
            "Buscar por nombre, apellido, teléfono o correo...");
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

        String[] columnas = {"ID", "Nombres", "Apellidos", "Teléfono", "Correo"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = crearTabla(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)));

        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setOpaque(false);
        centro.add(panelBusqueda, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        panelAcudientes.add(lblLista, BorderLayout.NORTH);
        panelAcudientes.add(centro, BorderLayout.CENTER);

        // ESTUDIANTES VINCULADOS (abajo a la derecha)
        JPanel panelVinculos = new JPanel(new BorderLayout(0, 10));
        panelVinculos.setBackground(Color.WHITE);
        panelVinculos.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel encabezadoVinculos = new JPanel();
        encabezadoVinculos.setOpaque(false);
        encabezadoVinculos.setLayout(new BoxLayout(encabezadoVinculos, BoxLayout.Y_AXIS));

        JLabel lblVinculos = new JLabel("Estudiantes a cargo");
        lblVinculos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblVinculos.setForeground(azulOscuro);
        lblVinculos.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblAcudienteSeleccionado = new JLabel("Selecciona un acudiente en la tabla.");
        lblAcudienteSeleccionado.setForeground(new Color(110, 120, 135));
        lblAcudienteSeleccionado.setAlignmentX(Component.LEFT_ALIGNMENT);

        encabezadoVinculos.add(lblVinculos);
        encabezadoVinculos.add(Box.createVerticalStrut(3));
        encabezadoVinculos.add(lblAcudienteSeleccionado);

        String[] columnasVinculos = {"ID", "Estudiante", "Documento", "Parentesco"};

        modeloVinculos = new DefaultTableModel(columnasVinculos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVinculos = crearTabla(modeloVinculos);

        JScrollPane scrollVinculos = new JScrollPane(tablaVinculos);
        scrollVinculos.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)));

        comboEstudiante = new JComboBox<>();
        comboEstudiante.setPreferredSize(new Dimension(280, 30));

        comboParentesco = new JComboBox<>(new String[]{"padre", "madre", "acudiente"});
        comboParentesco.setPreferredSize(new Dimension(110, 30));

        btnVincular = crearBoton("Vincular otro");
        btnQuitarVinculo = crearBoton("Quitar vínculo");

        JPanel accionesVinculo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        accionesVinculo.setOpaque(false);
        accionesVinculo.add(comboEstudiante);
        accionesVinculo.add(comboParentesco);
        accionesVinculo.add(btnVincular);
        accionesVinculo.add(btnQuitarVinculo);

        panelVinculos.add(encabezadoVinculos, BorderLayout.NORTH);
        panelVinculos.add(scrollVinculos, BorderLayout.CENTER);
        panelVinculos.add(accionesVinculo, BorderLayout.SOUTH);

        // Se organizan una encima de la otra
        JPanel panelDerecho = new JPanel(new GridLayout(2, 1, 0, 15));
        panelDerecho.setOpaque(false);
        panelDerecho.add(panelAcudientes);
        panelDerecho.add(panelVinculos);

        add(scrollFormulario, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        setVinculosHabilitados(false);

        // Avisa al controlador cada vez que se abre el módulo
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                if (alMostrar != null) {
                    alMostrar.run();
                }
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) { }

            @Override
            public void ancestorMoved(AncestorEvent event) { }
        });
    }

    // ESTILO

    private JTable crearTabla(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setRowHeight(30);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setAutoCreateRowSorter(true);
        t.getTableHeader().setReorderingAllowed(false);
        t.setFillsViewportHeight(true);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        return t;
    }

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

    public void mostrarAcudientes(List<Padre> lista) {
        modeloTabla.setRowCount(0);
        for (Padre p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getIdPadre(),
                p.getNombre(),
                p.getApellido(),
                p.getTelefono() != null ? p.getTelefono() : "",
                p.getCorreo() != null ? p.getCorreo() : "(sin usuario)"
            });
        }
    }

    // Llena los dos combos de estudiantes (el del formulario y el de abajo)
    public void mostrarEstudiantesCombo(List<Estudiante> lista) {
        comboEstudiante.removeAllItems();
        comboEstudianteNuevo.removeAllItems();
        for (Estudiante e : lista) {
            comboEstudiante.addItem(e);
            comboEstudianteNuevo.addItem(e);
        }
    }

    public void mostrarVinculos(List<Object[]> filas) {
        modeloVinculos.setRowCount(0);
        for (Object[] fila : filas) {
            modeloVinculos.addRow(fila);
        }
    }

    public void cargarEnFormulario(Padre p) {
        txtNombre.setText(p.getNombre());
        txtApellido.setText(p.getApellido());
        txtTelefono.setText(p.getTelefono() != null ? p.getTelefono() : "");
        txtCorreo.setText(p.getCorreo() != null ? p.getCorreo() : "");
        txtPassword.setText(""); // nunca se muestra la contraseña guardada

        lblAcudienteSeleccionado.setText("Acudiente: " + p.getNombre() + " " + p.getApellido());

        // Al editar, los estudiantes se manejan en la sección de abajo
        comboEstudianteNuevo.setEnabled(false);
        comboParentescoNuevo.setEnabled(false);

        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
        setVinculosHabilitados(true);
    }

    public void limpiarFormulario() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        tabla.clearSelection();

        modeloVinculos.setRowCount(0);
        lblAcudienteSeleccionado.setText("Selecciona un acudiente en la tabla.");

        // NUEVO: al registrar uno nuevo, el estudiante es obligatorio
        comboEstudianteNuevo.setEnabled(true);
        comboParentescoNuevo.setEnabled(true);

        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        setVinculosHabilitados(false);

        txtNombre.requestFocusInWindow();
    }

    private void setVinculosHabilitados(boolean habilitado) {
        comboEstudiante.setEnabled(habilitado);
        comboParentesco.setEnabled(habilitado);
        btnVincular.setEnabled(habilitado);
        btnQuitarVinculo.setEnabled(habilitado);
    }

    // Para roles que solo consultan: oculta el formulario
    public void setSoloLectura() {
        scrollFormulario.setVisible(false);
        comboEstudiante.setVisible(false);
        comboParentesco.setVisible(false);
        btnVincular.setVisible(false);
        btnQuitarVinculo.setVisible(false);
    }

    public void setAlMostrar(Runnable accion) {
        this.alMostrar = accion;
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
    public String getTelefono() { return txtTelefono.getText().trim(); }
    public String getCorreo() { return txtCorreo.getText().trim(); }
    public String getContrasena() { return new String(txtPassword.getPassword()); }
    public String getTextoBusqueda() { return txtBuscar.getText().trim(); }

    public Estudiante getEstudianteCombo() { return (Estudiante) comboEstudiante.getSelectedItem(); }
    public String getParentescoSeleccionado() { return (String) comboParentesco.getSelectedItem(); }

    public Estudiante getEstudianteNuevo() { return (Estudiante) comboEstudianteNuevo.getSelectedItem(); }
    public String getParentescoNuevo() { return (String) comboParentescoNuevo.getSelectedItem(); }
    public int getCantidadVinculos() { return modeloVinculos.getRowCount(); }

    // Devuelve el id de la relación seleccionada, o -1 si no hay fila elegida
    public int getIdRelacionSeleccionada() {
        int filaVista = tablaVinculos.getSelectedRow();
        if (filaVista == -1) {
            return -1;
        }
        int filaModelo = tablaVinculos.convertRowIndexToModel(filaVista);
        return Integer.parseInt(modeloVinculos.getValueAt(filaModelo, 0).toString());
    }

    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnMostrarTodos() { return btnMostrarTodos; }
    public JButton getBtnVincular() { return btnVincular; }
    public JButton getBtnQuitarVinculo() { return btnQuitarVinculo; }
    public JTextField getTxtBuscar() { return txtBuscar; }
}