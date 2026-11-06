package com.integrador.campus.view.Modulo;

import com.integrador.campus.controller.CalificacionController;
import com.integrador.campus.model.Calificacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class NotasPanel extends JPanel {

    private final CalificacionController controller = new CalificacionController();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtEstudiante;
    private JTextField txtMateria;
    private JTextField txtCurso;
    private JTextField txtNota;
    private JTextField txtPeriodo;
    private JTextField txtAnio;

    public NotasPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Calificación"));

        txtEstudiante = new JTextField();
        txtMateria = new JTextField();
        txtCurso = new JTextField();
        txtNota = new JTextField();
        txtPeriodo = new JTextField("PRIMER_TRIMESTRE");
        txtAnio = new JTextField("2026");

        form.add(new JLabel("ID Estudiante:"));
        form.add(txtEstudiante);
        form.add(new JLabel("ID Materia:"));
        form.add(txtMateria);
        form.add(new JLabel("ID curso:"));
        form.add(txtCurso);
        form.add(new JLabel("Nota (0.0-5.0):"));
        form.add(txtNota);
        form.add(new JLabel("Periodo:"));
        form.add(txtPeriodo);
        form.add(new JLabel("Año lectivo:"));
        form.add(txtAnio);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnActualiza = new JButton("Actualiza");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListar = new JButton("Listar por estudiante");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnRegistrar);
        botones.add(btnActualiza);
        botones.add(btnEliminar);
        botones.add(btnListar);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Estudiante", "Materia", "_curso", "Nota", "Periodo", "Año"}, 0
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
                listarPorEstudiante();
            }
        });
    }

    private void registrar() {
        try {
            Calificacion c = new Calificacion();
            c.setIdEstudiante(Integer.parseInt(txtEstudiante.getText().trim()));
            c.setIdMateria(Integer.parseInt(txtMateria.getText().trim()));
            c.setIdCurso(Integer.parseInt(txtCurso.getText().trim()));
            c.setNota(Double.parseDouble(txtNota.getText().trim()));
            c.setPeriodo(txtPeriodo.getText().trim());
            c.setAnioLectivo(Integer.parseInt(txtAnio.getText().trim()));
            if (controller.registrarCalificacion(c)) {
                JOptionPane.showMessageDialog(this, "Calificación registrada.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualiza() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una calificación.");
            return;
        }
        try {
            Calificacion c = new Calificacion();
            c.setIdCalificacion(Integer.parseInt(modeloTabla.getValueAt(row, 0).toString()));
            c.setIdEstudiante(Integer.parseInt(txtEstudiante.getText().trim()));
            c.setIdMateria(Integer.parseInt(txtMateria.getText().trim()));
            c.setIdCurso(Integer.parseInt(txtCurso.getText().trim()));
            c.setNota(Double.parseDouble(txtNota.getText().trim()));
            c.setPeriodo(txtPeriodo.getText().trim());
            c.setAnioLectivo(Integer.parseInt(txtAnio.getText().trim()));
            if (controller.actualizarCalificacion(c)) {
                JOptionPane.showMessageDialog(this, "Calificación actualizada.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una calificación.");
            return;
        }
        int id = Integer.parseInt(modeloTabla.getValueAt(row, 0).toString());
        if (controller.eliminarCalificacion(id)) {
            JOptionPane.showMessageDialog(this, "Calificación eliminada.");
        }
    }

    private void listarPorEstudiante() {
        try {
            int idEstudiante = Integer.parseInt(txtEstudiante.getText().trim());
            int anio = Integer.parseInt(txtAnio.getText().trim());
            List<Calificacion> lista = controller.listarCalificaciones(idEstudiante, anio);
            modeloTabla.setRowCount(0);
            for (Calificacion c : lista) {
                modeloTabla.addRow(new Object[]{
                        c.getIdCalificacion(),
                        c.getIdEstudiante(),
                        c.getIdMateria(),
                        c.getIdCurso(),
                        c.getNota(),
                        c.getPeriodo(),
                        c.getAnioLectivo()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}