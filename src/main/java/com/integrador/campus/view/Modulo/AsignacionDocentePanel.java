package com.integrador.campus.view.Modulo;

import com.integrador.campus.controller.AsignacionDocenteController;
import com.integrador.campus.model.AsignacionDocente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AsignacionDocentePanel extends JPanel {

    private final AsignacionDocenteController controller = new AsignacionDocenteController();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtDocente;
    private JTextField txtMateria;
    private JTextField txtCurso;
    private JTextField txtAnio;
    private JTextField txtEstado;

    public AsignacionDocentePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Asignación docente"));

        txtDocente = new JTextField();
        txtMateria = new JTextField();
        txtCurso = new JTextField();
        txtAnio = new JTextField("2026");
        txtEstado = new JTextField("ACTIVA");

        form.add(new JLabel("ID Docente:"));
        form.add(txtDocente);
        form.add(new JLabel("ID Materia:"));
        form.add(txtMateria);
        form.add(new JLabel("ID curso:"));
        form.add(txtCurso);
        form.add(new JLabel("Año lectivo:"));
        form.add(txtAnio);
        form.add(new JLabel("Estado:"));
        form.add(txtEstado);

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
                new String[]{"ID", "Docente", "Materia", "_curso", "Año", "Estado"}, 0
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
            AsignacionDocente a = new AsignacionDocente();
            a.setIdDocente(Integer.parseInt(txtDocente.getText().trim()));
            a.setIdMateria(Integer.parseInt(txtMateria.getText().trim()));
            a.setIdCurso(Integer.parseInt(txtCurso.getText().trim()));
            a.setAnioLectivo(Integer.parseInt(txtAnio.getText().trim()));
            a.setEstado(txtEstado.getText().trim());
            if (controller.registrarAsignacion(a)) {
                JOptionPane.showMessageDialog(this, "Asignación registrada.");
                listar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualiza() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una asignación.");
            return;
        }
        try {
            AsignacionDocente a = new AsignacionDocente();
            a.setIdAsignacion(Integer.parseInt(modeloTabla.getValueAt(row, 0).toString()));
            a.setIdDocente(Integer.parseInt(txtDocente.getText().trim()));
            a.setIdMateria(Integer.parseInt(txtMateria.getText().trim()));
            a.setIdCurso(Integer.parseInt(txtCurso.getText().trim()));
            a.setAnioLectivo(Integer.parseInt(txtAnio.getText().trim()));
            a.setEstado(txtEstado.getText().trim());
            if (controller.actualizarAsignacion(a)) {
                JOptionPane.showMessageDialog(this, "Asignación actualizada.");
                listar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una asignación.");
            return;
        }
        int id = Integer.parseInt(modeloTabla.getValueAt(row, 0).toString());
        if (controller.eliminarAsignacion(id)) {
            JOptionPane.showMessageDialog(this, "Asignación eliminada.");
            listar();
        }
    }

    private void listar() {
        List<AsignacionDocente> lista = controller.listarAsignaciones(0, 0);
        modeloTabla.setRowCount(0);
        for (AsignacionDocente a : lista) {
            modeloTabla.addRow(new Object[]{
                    a.getIdAsignacion(),
                    a.getIdDocente(),
                    a.getIdMateria(),
                    a.getIdCurso(),
                    a.getAnioLectivo(),
                    a.getEstado()
            });
        }
    }
}