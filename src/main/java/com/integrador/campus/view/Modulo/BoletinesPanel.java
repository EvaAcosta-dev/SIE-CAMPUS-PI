package com.integrador.campus.view.Modulo;

import com.integrador.campus.controller.BoletinController;
import com.integrador.campus.model.Boletin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BoletinesPanel extends JPanel {

    private final BoletinController controller = new BoletinController();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtEstudiante;
    private JTextField txtCurso;
    private JTextField txtAnio;
    private JTextField txtPeriodo;
    private JTextField txtPromedio;
    private JTextField txtAprobadas;
    private JTextField txtReprobadas;
    private JTextField txtEstado;

    public BoletinesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Boletín"));

        txtEstudiante = new JTextField();
        txtCurso = new JTextField();
        txtAnio = new JTextField("2026");
        txtPeriodo = new JTextField("PRIMER_TRIMESTRE");
        txtPromedio = new JTextField();
        txtAprobadas = new JTextField("0");
        txtReprobadas = new JTextField("0");
        txtEstado = new JTextField("GENERADO");

        form.add(new JLabel("ID Estudiante:"));
        form.add(txtEstudiante);
        form.add(new JLabel("ID curso:"));
        form.add(txtCurso);
        form.add(new JLabel("Año lectivo:"));
        form.add(txtAnio);
        form.add(new JLabel("Periodo:"));
        form.add(txtPeriodo);
        form.add(new JLabel("Promedio:"));
        form.add(txtPromedio);
        form.add(new JLabel("Aprobadas:"));
        form.add(txtAprobadas);
        form.add(new JLabel("Reprobadas:"));
        form.add(txtReprobadas);
        form.add(new JLabel("Estado:"));
        form.add(txtEstado);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnListar = new JButton("Listar");
        JButton btnEstado = new JButton("Cambiar estado");
        JButton btnEliminar = new JButton("Eliminar");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnRegistrar);
        botones.add(btnListar);
        botones.add(btnEstado);
        botones.add(btnEliminar);

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Estudiante", "_curso", "Año", "Periodo", "Promedio", "Aprob.", "Reprob.", "Estado"}, 0
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
        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listar();
            }
        });
        btnEstado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarEstado();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });
    }

    private void registrar() {
        try {
            Boletin b = new Boletin();
            b.setIdEstudiante(Integer.parseInt(txtEstudiante.getText().trim()));
            b.setIdCurso(Integer.parseInt(txtCurso.getText().trim()));
            b.setAnioLectivo(Integer.parseInt(txtAnio.getText().trim()));
            b.setPeriodo(txtPeriodo.getText().trim());
            b.setPromedioGeneral(Double.parseDouble(txtPromedio.getText().trim()));
            b.setAprobadas(Integer.parseInt(txtAprobadas.getText().trim()));
            b.setReprobadas(Integer.parseInt(txtReprobadas.getText().trim()));
            b.setEstadoBoletin(txtEstado.getText().trim());
            if (controller.generarBoletin(b)) {
                JOptionPane.showMessageDialog(this, "Boletín registrado.");
                listar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void listar() {
        try {
            int idEstudiante = Integer.parseInt(txtEstudiante.getText().trim());
            List<Boletin> lista = controller.listarBoletines(idEstudiante);
            modeloTabla.setRowCount(0);
            for (Boletin b : lista) {
                modeloTabla.addRow(new Object[]{
                        b.getIdBoletin(),
                        b.getIdEstudiante(),
                        b.getIdCurso(),
                        b.getAnioLectivo(),
                        b.getPeriodo(),
                        b.getPromedioGeneral(),
                        b.getAprobadas(),
                        b.getReprobadas(),
                        b.getEstadoBoletin()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void cambiarEstado() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un boletín.");
            return;
        }
        int id = Integer.parseInt(modeloTabla.getValueAt(row, 0).toString());
        String nuevoEstado = (String) JOptionPane.showInputDialog(this, "Nuevo estado:", "Cambiar estado", JOptionPane.PLAIN_MESSAGE, null, null, txtEstado.getText());
        if (nuevoEstado != null && controller.actualizarEstadoBoletin(id, nuevoEstado)) {
            JOptionPane.showMessageDialog(this, "Estado actualizado.");
            listar();
        }
    }

    private void eliminar() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un boletín.");
            return;
        }
        int id = Integer.parseInt(modeloTabla.getValueAt(row, 0).toString());
        if (controller.eliminarBoletin(id)) {
            JOptionPane.showMessageDialog(this, "Boletín eliminado.");
            listar();
        }
    }
}