package com.integrador.campus.view.Modulo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.formdev.flatlaf.FlatClientProperties;
import com.integrador.campus.model.Nivel;


public class NivelFormPanel extends JPanel {
    
private final Color azulOscuro = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);

     public JTextField txtNombreNivel;
    public JTextField txtBuscarNivel;

    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    public final JButton btnGuardarNivel;
    public final JButton btnActualizarNivel;
    public final JButton btnEliminarNivel;
    public final JButton btnLimpiarNivel;
    public final JButton btnBuscarNivel;
    public final JButton btnMostrarNivel;

    private int idNivelSeleccionado = -1;

    public NivelFormPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        //  FORMULARIO DE CURSOS (IZQUIERDA)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelFormulario.setPreferredSize(new Dimension(320, 600));

        JLabel lblTitulo = new JLabel("Gestión de Niveles");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("Registra y administra los niveles academicos.");
        lblDescripcion.setForeground(new Color(110, 120, 135));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombreNivel = crearCampo("Ej: Preescolar,Basica Primaria...");

    
        btnGuardarNivel = crearBoton("Guardar Nivel", true);
        btnActualizarNivel = crearBoton("Actualizar", false);
        btnEliminarNivel = crearBoton("Eliminar", false);
        btnLimpiarNivel = crearBoton("Limpiar", false);

        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(Box.createVerticalStrut(22));

        agregarCampo(panelFormulario, "Nombre del Nivel", txtNombreNivel);
      

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnGuardarNivel.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardarNivel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        panelBotones.add(btnGuardarNivel);
        panelBotones.add(Box.createVerticalStrut(8));

        JPanel panelBotonesSecundarios = new JPanel(new GridLayout(0, 2, 8, 8));
        panelBotonesSecundarios.setOpaque(false);
        panelBotonesSecundarios.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBotonesSecundarios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));

        panelBotonesSecundarios.add(btnActualizarNivel);
        panelBotonesSecundarios.add(btnEliminarNivel);
        panelBotonesSecundarios.add(btnLimpiarNivel);

        panelBotones.add(panelBotonesSecundarios);

        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(panelBotones);

        // TABLA Y BÚSQUEDA (DERECHA)
        JPanel panelTabla = new JPanel(new BorderLayout(0, 15));
        panelTabla.setBackground(fondo);

        JPanel encabezadoTabla = new JPanel(new BorderLayout());
        encabezadoTabla.setOpaque(false);

        JLabel lblLista = new JLabel("Niveles registradas");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLista.setForeground(azulOscuro);
        encabezadoTabla.add(lblLista, BorderLayout.WEST);

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new EmptyBorder(12, 12, 12, 12));

        txtBuscarNivel = new JTextField();
        txtBuscarNivel.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar por nombre deL Nivel");
        txtBuscarNivel.putClientProperty(FlatClientProperties.STYLE, "showClearButton: true");

        btnBuscarNivel = new JButton("Buscar");
        btnMostrarNivel = new JButton("Mostrar todos");

        JPanel accionesBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        accionesBusqueda.setOpaque(false);
        accionesBusqueda.add(btnBuscarNivel);
        accionesBusqueda.add(btnMostrarNivel);

        panelBusqueda.add(txtBuscarNivel, BorderLayout.CENTER);
        panelBusqueda.add(accionesBusqueda, BorderLayout.EAST);

        String[] columnas = {"ID", "Nombre Nivel"};
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
        scrollFormulario.setPreferredSize(new Dimension(320, 0));
        
        add(scrollFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);

        // Estado inicial de botones de acción
        btnActualizarNivel.setEnabled(false);
        btnEliminarNivel.setEnabled(false);
    }

    // MÉTODOS DE APOYO Y ESTILO VISUAL
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

    // Limpiar formulario y restablecer estados de botones
    public void limpiarFormulario() {
        idNivelSeleccionado = -1;
        txtNombreNivel.setText("");
        tabla.clearSelection();

        btnGuardarNivel.setEnabled(true);
        btnActualizarNivel.setEnabled(false);
        btnEliminarNivel.setEnabled(false);
        txtNombreNivel.requestFocusInWindow();
    }

    // Métodos de mensajes
    public void mostrarMensaje(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }
    public void mostrarError(String mensaje) { JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE); }
    public boolean confirmar(String mensaje) { 
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar operación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; 
    }

    public void llenarTablaNiveles(List<Nivel> niveles) {
        modeloTabla.setRowCount(0);
        for (Nivel nivel : niveles) {
            Object[] fila = {
                nivel.getIdNivel(),
                nivel.getNombreNivel()
            };
            modeloTabla.addRow(fila);
        }
    }
    
    // Getters para el controlador
    public int getIdNivelSeleccionado() { return idNivelSeleccionado; }
    public void setIdNivelSeleccionado(int idNivel) { this.idNivelSeleccionado = idNivel; }
    public String getNombreNivel() { return txtNombreNivel.getText().trim(); }
    public String getTextoBusqueda() { return txtBuscarNivel.getText().trim(); }

    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    public JButton getBtnGuardarNivel() { return btnGuardarNivel; }
    public JButton getBtnActualizarNivel() { return btnActualizarNivel; }
    public JButton getBtnEliminarNivel() { return btnEliminarNivel; }
    public JButton getBtnLimpiarNivel() { return btnLimpiarNivel; }
    public JButton getBtnBuscarNivel() { return btnBuscarNivel; }
    public JButton getBtnMostrarNivel() { return btnMostrarNivel; }
    public JTextField getTxtBuscarNivel() { return txtBuscarNivel; }

}


