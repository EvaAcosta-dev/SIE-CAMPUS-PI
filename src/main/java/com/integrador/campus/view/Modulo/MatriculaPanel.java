package com.integrador.campus.view.Modulo;

import com.integrador.campus.controller.MatriculaController;
import com.integrador.campus.model.Matricula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.List;

public class MatriculaPanel extends JPanel {

    private final MatriculaController controller = new MatriculaController();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtEstudiante;
    private JTextField txtCurso;
    private JTextField txtEstado;
    private JTextField txtObservacion;

    public MatriculaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Formulario
        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Matrícula"));

        txtEstudiante = new JTextField();
        txtCurso = new JTextField();
        txtEstado = new JTextField("ACTIVA");
        txtObservacion = new JTextField();

        form.add(new JLabel("ID Estudiante:"));
        form.add(txtEstudiante);
        form.add(new JLabel("ID curso:"));
        form.add(txtCurso);
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

        // Tabla
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Estudiante", "_curso", "Estado", "Observación", "Fecha"}, 0
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

        listar();
    }

    private void registrar() {
        try {
            Matricula m = new Matricula();
            m.setIdEstudiante(Integer.parseInt(txtEstudiante.getText().trim()));
            m.setIdCurso(Integer.parseInt(txtCurso.getText().trim()));
            m.setEstado(txtEstado.getText().trim());
            m.setObservacion(txtObservacion.getText().trim());
            if (controller.registrarMatricula(m)) {
                JOptionPane.showMessageDialog(this, "Matrícula registrada.");
                listar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualiza() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una matrícula.");
            return;
        }
        try {
            Matricula m = new Matricula();
            m.setIdMatricula(Integer.parseInt(modeloTabla.getValueAt(row, 0).toString()));
            m.setIdEstudiante(Integer.parseInt(txtEstudiante.getText().trim()));
            m.setIdCurso(Integer.parseInt(txtCurso.getText().trim()));
            m.setEstado(txtEstado.getText().trim());
            m.setObservacion(txtObservacion.getText().trim());
            if (controller.actualizarMatricula(m)) {
                JOptionPane.showMessageDialog(this, "Matrícula actualizada.");
                listar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una matrícula.");
            return;
        }
        int id = Integer.parseInt(modeloTabla.getValueAt(row, 0).toString());
        if (controller.eliminarMatricula(id)) {
            JOptionPane.showMessageDialog(this, "Matrícula eliminada.");
            listar();
        }
    }

    private void listar() {
        List<Matricula> lista = controller.listarMatriculas();
        modeloTabla.setRowCount(0);
        for (Matricula m : lista) {
            modeloTabla.addRow(new Object[]{
                    m.getIdMatricula(),
                    m.getIdEstudiante(),
                    m.getIdCurso(),
                    m.getEstado(),
                    m.getObservacion(),
                    m.getFechaMatricula()
            });
        }
    }
}