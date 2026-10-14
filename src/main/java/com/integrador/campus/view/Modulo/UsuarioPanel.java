package com.integrador.campus.view.Modulo;

import com.integrador.campus.model.Usuario;
import com.integrador.campus.model.Rol;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioPanel extends JPanel {

    private final Color azulOscuro = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);

    private final JTextField txtBuscar;
    private final JTextField txtNombre;
    private final JTextField txtApellido;
    private final JTextField txtEmail;
    private final JPasswordField txtPassword;

    private final JComboBox<Rol> comboBoxRol;
    private final JComboBox<String> comboEstado;

    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    private final JButton btnGuardar;
    private final JButton btnActualizar;
    private final JButton btnEliminar;
    private final JButton btnCambiarEstado;
    private final JButton btnLimpiar;
    private final JButton btnBuscar;
    private final JButton btnMostrarTodos;

    private int idUsuarioSeleccionado = -1;

    public UsuarioPanel() {

        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // FORMULARIO DE USUARIOS

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(
            new BoxLayout(panelFormulario, BoxLayout.Y_AXIS)
        );

        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(
            new EmptyBorder(20, 20, 20, 20)
        );
        panelFormulario.setPreferredSize(
            new Dimension(300, 600)
        );

        JLabel lblTitulo = new JLabel("Gestión de usuarios");
        lblTitulo.setFont(
            new Font("Segoe UI", Font.BOLD, 20)
        );
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel(
            "Registra y administra las cuentas."
        );
        lblDescripcion.setForeground(new Color(110, 120, 135));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombre = crearCampo("Nombres");
        txtApellido = crearCampo("Apellidos");
        txtEmail = crearCampo("correo@ejemplo.com");

        txtPassword = new JPasswordField();
        configurarCampo(txtPassword, "Contraseña (opcional al editar)");
        txtPassword.putClientProperty(
            FlatClientProperties.STYLE,
            "showRevealButton: true"
        );

        comboBoxRol = new JComboBox<>();
        comboBoxRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboBoxRol.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 36)
        );

        comboEstado = new JComboBox<>(
            new String[]{"Activo", "Inactivo"}
        );
        comboEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboEstado.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 36)
        );

        btnGuardar = crearBoton("Guardar usuario", true);
        btnActualizar = crearBoton("Actualizar", true);
        btnEliminar = crearBoton("Eliminar", true);
        btnCambiarEstado = crearBoton("Cambiar estado", true);
        btnLimpiar = crearBoton("Limpiar", true);

        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(Box.createVerticalStrut(22));

        agregarCampo(panelFormulario, "Nombres", txtNombre);
        agregarCampo(panelFormulario, "Apellidos", txtApellido);
        agregarCampo(panelFormulario, "Correo electrónico", txtEmail);
        agregarCampo(panelFormulario, "Contraseña", txtPassword);
        agregarCampo(panelFormulario, "Rol", comboBoxRol);
        agregarCampo(panelFormulario, "Estado de la cuenta", comboEstado);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Guardar: ocupa todo el ancho
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnGuardar.setPreferredSize(new Dimension(200, 38));

        panelBotones.add(btnGuardar);
        panelBotones.add(Box.createVerticalStrut(8));

        //  botones: dos columnas
        JPanel panelBotonesSecundarios = new JPanel(
            new java.awt.GridLayout(0, 2, 8, 8)
        );
        panelBotonesSecundarios.setOpaque(false);
        panelBotonesSecundarios.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelBotonesSecundarios.add(btnActualizar);
        panelBotonesSecundarios.add(btnEliminar);
        panelBotonesSecundarios.add(btnCambiarEstado);
        panelBotonesSecundarios.add(btnLimpiar);

        panelBotones.add(panelBotonesSecundarios);

        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(panelBotones);

        // TABLA Y BÚSQUEDA

        JPanel panelTabla = new JPanel(new BorderLayout(0, 15));
        panelTabla.setBackground(fondo);

        JPanel encabezadoTabla = new JPanel(new BorderLayout());
        encabezadoTabla.setOpaque(false);

        JLabel lblLista = new JLabel("Usuarios registrados");
        lblLista.setFont(
            new Font("Segoe UI", Font.BOLD, 20)
        );
        lblLista.setForeground(azulOscuro);

        encabezadoTabla.add(lblLista, BorderLayout.WEST);

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(
            new EmptyBorder(12, 12, 12, 12)
        );

        txtBuscar = new JTextField();
        txtBuscar.putClientProperty(
            FlatClientProperties.PLACEHOLDER_TEXT,
            "Buscar por nombre, apellido, correo o rol..."
        );
        txtBuscar.putClientProperty(
            FlatClientProperties.STYLE,
            "showClearButton: true"
        );

        btnBuscar = new JButton("Buscar");
        btnMostrarTodos = new JButton("Mostrar todos");

        JPanel accionesBusqueda = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        accionesBusqueda.setOpaque(false);
        accionesBusqueda.add(btnBuscar);
        accionesBusqueda.add(btnMostrarTodos);

        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(accionesBusqueda, BorderLayout.EAST);

        String[] columnas = {
            "ID", "Nombres", "Apellidos",
            "Correo electrónico", "Rol", "Estado"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(34);
        tabla.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        tabla.setAutoCreateRowSorter(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(
            BorderFactory.createLineBorder(
                new Color(225, 230, 238)
            )
        );

        panelTabla.add(encabezadoTabla, BorderLayout.NORTH);

        JPanel centroTabla = new JPanel(new BorderLayout(0, 12));
        centroTabla.setOpaque(false);
        centroTabla.add(panelBusqueda, BorderLayout.NORTH);
        centroTabla.add(scroll, BorderLayout.CENTER);

        panelTabla.add(centroTabla, BorderLayout.CENTER);

        add(panelFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);

        // Estado inicial de los botones
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCambiarEstado.setEnabled(false);
    }

    // CREACIÓN Y ESTILO DE COMPONENTES

    private JTextField crearCampo(String placeholder) {
        JTextField campo = new JTextField();
        configurarCampo(campo, placeholder);
        return campo;
    }

    private void configurarCampo(
            JTextField campo, String placeholder) {

        campo.putClientProperty(
            FlatClientProperties.PLACEHOLDER_TEXT,
            placeholder
        );

        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 36)
        );
    }

    private JButton crearBoton(
            String texto, boolean principal) {

        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        if (principal) {
            boton.setBackground(azulOscuro);
            boton.setForeground(Color.WHITE);
            boton.putClientProperty(
                FlatClientProperties.STYLE,
                "buttonType: bold"
            );
        }

        return boton;
    }

    private void agregarCampo(
            JPanel panel, String etiqueta, JComponent campo) {

        JLabel label = new JLabel(etiqueta);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(12));
    }

    // MÉTODOS PARA MOSTRAR INFORMACIÓN

    public void mostrarUsuarios(List<Usuario> usuarios) {
        modeloTabla.setRowCount(0);

        for (Usuario u : usuarios) {
            String nombreRol = u.getRol() != null
                ? u.getRol().getNombreRol()
                : "Sin rol";

            String estado = u.isActivo()
                ? "Activo"
                : "Inactivo";

            modeloTabla.addRow(new Object[]{
                u.getIdUsuario(),
                u.getNombre(),
                u.getApellido(),
                u.getCorreo(),
                nombreRol,
                estado
            });
        }
    }

    public void mostrarRoles(List<Rol> roles) {
        comboBoxRol.removeAllItems();

        for (Rol rol : roles) {
            comboBoxRol.addItem(rol);
        }
    }

    public void cargarUsuarioEnFormulario(Usuario u) {
        idUsuarioSeleccionado = u.getIdUsuario();

        txtNombre.setText(u.getNombre());
        txtApellido.setText(u.getApellido());
        txtEmail.setText(u.getCorreo());

        // Nunca se recupera la contraseña guardada.
        txtPassword.setText("");

        seleccionarRolPorId(u.getIdRol());

        comboEstado.setSelectedItem(
            u.isActivo() ? "Activo" : "Inactivo"
        );

        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnCambiarEstado.setEnabled(true);
    }

    private void seleccionarRolPorId(int idRol) {
        for (int i = 0; i < comboBoxRol.getItemCount(); i++) {
            Rol rol = comboBoxRol.getItemAt(i);

            if (rol.getIdRol() == idRol) {
                comboBoxRol.setSelectedIndex(i);
                return;
            }
        }
    }

    public void limpiarFormulario() {
        idUsuarioSeleccionado = -1;

        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtPassword.setText("");

        if (comboBoxRol.getItemCount() > 0) {
            comboBoxRol.setSelectedIndex(0);
        }

        comboEstado.setSelectedItem("Activo");
        tabla.clearSelection();

        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCambiarEstado.setEnabled(false);

        txtNombre.requestFocusInWindow();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(
            this,
            mensaje,
            "Confirmar operación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        ) == JOptionPane.YES_OPTION;
    }

    // GETTERS PARA EL CONTROLADOR

    public int getIdUsuarioSeleccionado() {
        return idUsuarioSeleccionado;
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getApellido() {
        return txtApellido.getText().trim();
    }

    public String getCorreo() {
        return txtEmail.getText().trim();
    }

    public String getContrasena() {
        return new String(txtPassword.getPassword());
    }

    public Rol getRolSeleccionado() {
        return (Rol) comboBoxRol.getSelectedItem();
    }

    public boolean isCuentaActiva() {
        return "Activo".equals(comboEstado.getSelectedItem());
    }

    public String getTextoBusqueda() {
        return txtBuscar.getText().trim();
    }

    public JTable getTabla() {
        return tabla;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
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

    public JButton getBtnCambiarEstado() {
        return btnCambiarEstado;
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

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }
}