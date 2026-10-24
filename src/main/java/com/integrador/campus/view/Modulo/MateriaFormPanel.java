package com.integrador.campus.view.Modulo;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;
import com.integrador.campus.model.AnioLectivo;
import com.integrador.campus.model.Cursos;
import com.integrador.campus.model.Docente;
import com.integrador.campus.model.Jornada;
import com.integrador.campus.model.Materia;

import java.awt.*;
import java.util.List;
public class MateriaFormPanel extends JPanel{
    private final Color azulOscuro = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);

    public final JTextField txtBuscar;
    public final JTextField txtNombreMateria;
    //public final JTextField txtGrado;

    private final JTable tablaM;
    private final DefaultTableModel modeloTabla;

    public final JButton btnGuardarMateria;
    public final JButton btnActualizarMateria;
    public final JButton btnEliminarMateria;
    public final JButton btnLimpiarMateria;
    public final JButton btnBuscarMateria;
    public final JButton btnMostrarMaterias;

    private int idCursoSeleccionado = -1;

    public MateriaFormPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        //  FORMULARIO DE CURSOS (IZQUIERDA)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelFormulario.setPreferredSize(new Dimension(320, 600));

        JLabel lblTitulo = new JLabel("Gestión de Materias");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("Registra y administra las materias.");
        lblDescripcion.setForeground(new Color(110, 120, 135));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombreMateria = crearCampo("Ej: Matematicas");

    
        btnGuardarMateria = crearBoton("Guardar materia", true);
        btnActualizarMateria = crearBoton("Actualizar", false);
        btnEliminarMateria = crearBoton("Eliminar", false);
        //btnCambiarEstadoCurso = crearBoton("Cambiar estado", false);
        btnLimpiarMateria = crearBoton("Limpiar", false);

        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(Box.createVerticalStrut(22));

        agregarCampo(panelFormulario, "Nombre del Curso", txtNombreMateria);
        //agregarCampo(panelFormulario, "Grado", txtGrado);
      

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnGuardarMateria.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardarMateria.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        panelBotones.add(btnGuardarMateria);
        panelBotones.add(Box.createVerticalStrut(8));

        JPanel panelBotonesSecundarios = new JPanel(new GridLayout(0, 2, 8, 8));
        panelBotonesSecundarios.setOpaque(false);
        panelBotonesSecundarios.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelBotonesSecundarios.add(btnActualizarMateria);
        panelBotonesSecundarios.add(btnEliminarMateria);
        //panelBotonesSecundarios.add(btnCambiarEstadoCurso);
        panelBotonesSecundarios.add(btnLimpiarMateria);

        panelBotones.add(panelBotonesSecundarios);

        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(panelBotones);

        // TABLA Y BÚSQUEDA (DERECHA)
        JPanel panelTabla = new JPanel(new BorderLayout(0, 15));
        panelTabla.setBackground(fondo);

        JPanel encabezadoTabla = new JPanel(new BorderLayout());
        encabezadoTabla.setOpaque(false);

        JLabel lblLista = new JLabel("Materias registradas");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLista.setForeground(azulOscuro);
        encabezadoTabla.add(lblLista, BorderLayout.WEST);

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new EmptyBorder(12, 12, 12, 12));

        txtBuscar = new JTextField();
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar por nombre de materia");
        txtBuscar.putClientProperty(FlatClientProperties.STYLE, "showClearButton: true");

        btnBuscarMateria = new JButton("Buscar");
        btnMostrarMaterias = new JButton("Mostrar todos");

        JPanel accionesBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        accionesBusqueda.setOpaque(false);
        accionesBusqueda.add(btnBuscarMateria);
        accionesBusqueda.add(btnMostrarMaterias);

        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(accionesBusqueda, BorderLayout.EAST);

        String[] columnas = {"ID", "Materia"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaM = new JTable(modeloTabla);
        tablaM.setRowHeight(34);
        tablaM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaM.setAutoCreateRowSorter(true);
        tablaM.getTableHeader().setReorderingAllowed(false);
        tablaM.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tablaM);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)));

        panelTabla.add(encabezadoTabla, BorderLayout.NORTH);

        JPanel centroTabla = new JPanel(new BorderLayout(0, 12));
        centroTabla.setOpaque(false);
        centroTabla.add(panelBusqueda, BorderLayout.NORTH);
        centroTabla.add(scroll, BorderLayout.CENTER);

        panelTabla.add(centroTabla, BorderLayout.CENTER);

        add(panelFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);

        // Estado inicial de botones de acción
        btnActualizarMateria.setEnabled(false);
        btnEliminarMateria.setEnabled(false);
        //btnCambiarEstadoCurso.setEnabled(false);
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
        idCursoSeleccionado = -1;
        txtNombreMateria.setText("");
        //txtGrado.setText("");
        tablaM.clearSelection();

        btnGuardarMateria.setEnabled(true);
        btnActualizarMateria.setEnabled(false);
        btnEliminarMateria.setEnabled(false);
        //btnCambiarEstadoCurso.setEnabled(false);
        txtNombreMateria.requestFocusInWindow();
    }

    // Métodos de mensajes
    public void mostrarMensaje(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }
    public void mostrarError(String mensaje) { JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE); }
    public boolean confirmar(String mensaje) { 
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar operación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; 
    }

    public void llenarTablaCursos(List<Materia> materias) {
        modeloTabla.setRowCount(0);
        for (Materia m : materias
        ) {
            Object[] fila = {
                m.getIdMateria(),
                m.getNombreMateria(),
                
            };
            modeloTabla.addRow(fila);
        }
    }
    
    // Getters para el controlador
    public int getIdCursoSeleccionado() { return idCursoSeleccionado; }
    public void setIdCursoSeleccionado(int idCurso) { this.idCursoSeleccionado = idCurso; }
    public String getNombreCurso() { return txtNombreMateria.getText().trim(); }
    //public String getGrado() { return txtGrado.getText().trim(); }
    public String getTextoBusqueda() { return txtBuscar.getText().trim(); }

    public JTable getTabla() { return tablaM; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    public JButton getBtnGuardarCurso() { return btnGuardarMateria; }
    public JButton getBtnActualizarCurso() { return btnActualizarMateria; }
    public JButton getBtnEliminarCurso() { return btnEliminarMateria; }
    //public JButton getBtnCambiarEstadoCurso() { return btnCambiarEstadoCurso; }
    public JButton getBtnLimpiarCurso() { return btnLimpiarMateria; }
    public JButton getBtnBuscarCurso() { return btnBuscarMateria; }
    public JButton getBtnMostrarCursos() { return btnMostrarMaterias; }
    public JTextField getTxtBuscar() { return txtBuscar; }

}


