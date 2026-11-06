package com.integrador.campus.view.Modulo;

import com.integrador.campus.controller.AsistenciaController;
import com.integrador.campus.model.Asistencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AsistenciaPanel extends JPanel {

    private final AsistenciaController controller = new AsistenciaController();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtEstudiante;
    private JTextField txtMateria;
    private JTextField txtCurso;
    private JTextField txtFecha;
    private JTextField txtEstado;
    private JTextField txtObservacion;

    public AsistenciaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Asistencia"));

        txtEstudiante = new JTextField();
        txtMateria = new JTextField();
        txtCurso = new JTextField();
        txtFecha = new JTextField("2026-11-03");
        txtEstado = new JTextField("PRESENTE");
        txtObservacion = new JTextField();

        form.add(new JLabel("ID Estudiante:"));
        form.add(txtEstudiante);
        form.add(new JLabel("ID Materia:"));
        form.add(txtMateria);
        form.add(new JLabel("ID curso:"));
        form.add(txtCurso);
        form.add(new JLabel("Fecha (yyyy-mm-dd):"));
        form.add(txtFecha);
        form.add(new JLabel("Estado:"));
        form.add(txtEstado);
        form.add(new JLabel("Observación:"));
        form.add(txtObservacion);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnActualiza = new JButton("Actualiza");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListar = new JButton("Listar");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnRegistrar);
        botones.add(btnActualiza);
        botones.add(btnEliminar);
        botones.add(btnListar);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Estudiante", "Materia", "_curso", "Fecha", "Estado", "Obs."}, 0
        ) {
            public boolean isEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabla);

        add(form, BorderLayout.NORTH);
        add(botones, BorderLayout.SOUTH);
        add(scroll, BorderLayout.CENTER);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrar();
            }
        });
        btnActualiza.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualiza();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });
        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listar();
            }
        });
    }

    private void registrar() {
        try {
            Asistencia a = new Asistencia();
            a.setIdEstudiante(Integer.parseInt(txtEstudiante.getText().trim()));
            a.setIdMateria(Integer.parseInt(txtMateria.getText().trim()));
            a.setIdCurso(Integer.parseInt(txtCurso.getText().trim()));
            a.setFechaClase(java.sql.Date.valueOf(txtFecha.getText().trim()));
            a.setEstado(txtEstado.getText().trim());
            a.setObservacion(txtObservacion.getText().trim());
            if (controller.registrarAsistencia(a)) {
                JOptionPane.showMessageDialog(this, "Asistencia registrada.");
                listar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualiza() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una asistencia.");
            return;
        }
        try {
            Asistencia a = new Asistencia();
            a.setIdAsistencia(Integer.parseInt(modeloTabla.getValueAt(row, 0).toString()));
            a.setIdEstudiante(Integer.parseInt(txtEstudiante.getText().trim()));
            a.setIdMateria(Integer.parseInt(txtMateria.getText().trim()));
            a.setIdCurso(Integer.parseInt(txtCurso.getText().trim()));
            a.setFechaClase(java.sql.Date.valueOf(txtFecha.getText().trim()));
            a.setEstado(txtEstado.getText().trim());
            a.setObservacion(txtObservacion.getText().trim());
            if (controller.actualizarAsistencia(a)) {
                JOptionPane.showMessageDialog(this, "Asistencia actualizada.");
                listar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una asistencia.");
            return;
        }
        int id = Integer.parseInt(modeloTabla.getValueAt(row, 0).toString());
        if (controller.eliminarAsistencia(id)) {
            JOptionPane.showMessageDialog(this, "Asistencia eliminada.");
            listar();
        }
    }

    private void listar() {
        try {
            int idEstudiante = Integer.parseInt(txtEstudiante.getText().trim());
            int idMateria = Integer.parseInt(txtMateria.getText().trim());
            int idCurso = Integer.parseInt(txtCurso.getText().trim());
            List<Asistencia> lista = controller.listarAsistencias(idEstudiante, idMateria, idCurso);
            modeloTabla.setRowCount(0);
            for (Asistencia a : lista) {
                modeloTabla.addRow(new Object[]{
                        a.getIdAsistencia(),
                        a.getIdEstudiante(),
                        a.getIdMateria(),
                        a.getIdCurso(),
                        a.getFechaClase(),
                        a.getEstado(),
                        a.getObservacion()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}