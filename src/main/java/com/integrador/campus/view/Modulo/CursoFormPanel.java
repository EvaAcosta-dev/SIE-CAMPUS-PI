package com.integrador.campus.view.Modulo;

import com.formdev.flatlaf.FlatClientProperties;
import com.integrador.campus.dao.DocenteDao;
import com.integrador.campus.dao.JornadaDao;
import com.integrador.campus.model.AnioLectivo;
import com.integrador.campus.model.Cursos;
import com.integrador.campus.model.Docente;
import com.integrador.campus.model.Jornada;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CursoFormPanel extends JPanel {
    private final Color azulOscuro = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);

    public final JTextField txtBuscar;
    public final JTextField txtNombreCurso;
    public final JTextField txtGrado;

    public final JComboBox<Jornada> comboBoxJornada;
    public final JComboBox<AnioLectivo> comboBoxAnioLectivo;
    public final JComboBox<Docente> comboBoxDocenteDirector;

    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    public final JButton btnGuardarCurso;
    public final JButton btnActualizarCurso;
    public final JButton btnEliminarCurso;
    public final JButton btnLimpiarCurso;
    public final JButton btnBuscarCurso;
    public final JButton btnMostrarCursos;

    private int idCursoSeleccionado = -1;

    public CursoFormPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        //  FORMULARIO DE CURSOS (IZQUIERDA)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelFormulario.setPreferredSize(new Dimension(320, 600));

        JLabel lblTitulo = new JLabel("Gestión de Cursos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(azulOscuro);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("Registra y administra los cursos.");
        lblDescripcion.setForeground(new Color(110, 120, 135));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombreCurso = crearCampo("Ej: 10-A, 601");
        txtGrado = crearCampo("Ej: 6, 10, 11");

        comboBoxJornada = new JComboBox<>();
        configurarCombo(comboBoxJornada);

        comboBoxAnioLectivo = new JComboBox<>();
        configurarCombo(comboBoxAnioLectivo);

        comboBoxDocenteDirector = new JComboBox<>();
        configurarCombo(comboBoxDocenteDirector);

        btnGuardarCurso = crearBoton("Guardar curso", true);
        btnActualizarCurso = crearBoton("Actualizar", false);
        btnEliminarCurso = crearBoton("Eliminar", false);
        //btnCambiarEstadoCurso = crearBoton("Cambiar estado", false);
        btnLimpiarCurso = crearBoton("Limpiar", false);

        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(Box.createVerticalStrut(22));

        agregarCampo(panelFormulario, "Nombre del Curso", txtNombreCurso);
        agregarCampo(panelFormulario, "Grado", txtGrado);
        agregarCampo(panelFormulario, "Jornada", comboBoxJornada);
        agregarCampo(panelFormulario, "Año Lectivo", comboBoxAnioLectivo);
        agregarCampo(panelFormulario, "Docente Director", comboBoxDocenteDirector);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);
        panelBotones.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnGuardarCurso.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGuardarCurso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        panelBotones.add(btnGuardarCurso);
        panelBotones.add(Box.createVerticalStrut(8));

        JPanel panelBotonesSecundarios = new JPanel(new GridLayout(0, 2, 8, 8));
        panelBotonesSecundarios.setOpaque(false);
        panelBotonesSecundarios.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelBotonesSecundarios.add(btnActualizarCurso);
        panelBotonesSecundarios.add(btnEliminarCurso);
        //panelBotonesSecundarios.add(btnCambiarEstadoCurso);
        panelBotonesSecundarios.add(btnLimpiarCurso);

        panelBotones.add(panelBotonesSecundarios);

        panelFormulario.add(Box.createVerticalStrut(5));
        panelFormulario.add(panelBotones);

        // TABLA Y BÚSQUEDA (DERECHA)
        JPanel panelTabla = new JPanel(new BorderLayout(0, 15));
        panelTabla.setBackground(fondo);

        JPanel encabezadoTabla = new JPanel(new BorderLayout());
        encabezadoTabla.setOpaque(false);

        JLabel lblLista = new JLabel("Cursos registrados");
        lblLista.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLista.setForeground(azulOscuro);
        encabezadoTabla.add(lblLista, BorderLayout.WEST);

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new EmptyBorder(12, 12, 12, 12));

        txtBuscar = new JTextField();
        txtBuscar.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buscar por nombre, grado o jornada...");
        txtBuscar.putClientProperty(FlatClientProperties.STYLE, "showClearButton: true");

        btnBuscarCurso = new JButton("Buscar");
        btnMostrarCursos = new JButton("Mostrar todos");

        JPanel accionesBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        accionesBusqueda.setOpaque(false);
        accionesBusqueda.add(btnBuscarCurso);
        accionesBusqueda.add(btnMostrarCursos);

        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(accionesBusqueda, BorderLayout.EAST);

        String[] columnas = {"ID", "Curso", "Grado", "Jornada", "Año Lectivo", "Director de Grupo"};
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

        add(panelFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);

        // Estado inicial de botones de acción
        btnActualizarCurso.setEnabled(false);
        btnEliminarCurso.setEnabled(false);
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
        txtNombreCurso.setText("");
        txtGrado.setText("");
        if (comboBoxJornada.getItemCount() > 0) comboBoxJornada.setSelectedIndex(0);
        if (comboBoxAnioLectivo.getItemCount() > 0) comboBoxAnioLectivo.setSelectedIndex(0);
        if (comboBoxDocenteDirector.getItemCount() > 0) comboBoxDocenteDirector.setSelectedIndex(0);
        tabla.clearSelection();

        btnGuardarCurso.setEnabled(true);
        btnActualizarCurso.setEnabled(false);
        btnEliminarCurso.setEnabled(false);
        //btnCambiarEstadoCurso.setEnabled(false);
        txtNombreCurso.requestFocusInWindow();
    }

    // Métodos de mensajes
    public void mostrarMensaje(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }
    public void mostrarError(String mensaje) { JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE); }
    public boolean confirmar(String mensaje) { 
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar operación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; 
    }

    public void llenarTablaCursos(List<Cursos> cursos) {
        modeloTabla.setRowCount(0);
        for (Cursos c : cursos) {
            Object[] fila = {
                c.getIdCurso(),
                c.getNombreCurso(),
                c.getNombreGrado(),
                c.getNombreJornada(),
                c.getAnio(),
                c.getNombreDirector() != null ? c.getNombreDirector() : "Sin director"
            };
            modeloTabla.addRow(fila);
        }
    }
    //para el comboBoxJornada
   public void cargarJornadas(List<Jornada> jornadas) {
    comboBoxJornada.removeAllItems();

    for (Jornada jornada : jornadas) {
        comboBoxJornada.addItem(jornada);
    }
}

public void cargarDocentes(List<Docente> docentes) {
    comboBoxDocenteDirector.removeAllItems();

    for (Docente docente : docentes) {
        comboBoxDocenteDirector.addItem(docente);
    }
}

public void cargarAnios(List<AnioLectivo> anios) {
    comboBoxAnioLectivo.removeAllItems();

    for (AnioLectivo anio : anios) {
        comboBoxAnioLectivo.addItem(anio);
    }
}

    
    // Getters para el controlador
    public int getIdCursoSeleccionado() { return idCursoSeleccionado; }
    public void setIdCursoSeleccionado(int idCurso) { this.idCursoSeleccionado = idCurso; }
    public String getNombreCurso() { return txtNombreCurso.getText().trim(); }
    public String getGrado() { return txtGrado.getText().trim(); }
    public Jornada getJornadaSeleccionada() { return (Jornada) comboBoxJornada.getSelectedItem(); }
    public AnioLectivo getAnioSeleccionado() { return (AnioLectivo) comboBoxAnioLectivo.getSelectedItem(); }
    public Docente getDirectorSeleccionado() { return (Docente) comboBoxDocenteDirector.getSelectedItem(); }
    public String getTextoBusqueda() { return txtBuscar.getText().trim(); }

    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }

    public JButton getBtnGuardarCurso() { return btnGuardarCurso; }
    public JButton getBtnActualizarCurso() { return btnActualizarCurso; }
    public JButton getBtnEliminarCurso() { return btnEliminarCurso; }
    //public JButton getBtnCambiarEstadoCurso() { return btnCambiarEstadoCurso; }
    public JButton getBtnLimpiarCurso() { return btnLimpiarCurso; }
    public JButton getBtnBuscarCurso() { return btnBuscarCurso; }
    public JButton getBtnMostrarCursos() { return btnMostrarCursos; }
    public JTextField getTxtBuscar() { return txtBuscar; }

}