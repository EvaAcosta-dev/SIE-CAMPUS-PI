package com.integrador.campus.view.Modulo;

import com.integrador.campus.controller.ConvivenciaController;
import com.integrador.campus.model.Convivencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ConvivenciaPanel extends JPanel {

    private final ConvivenciaController controller = new ConvivenciaController();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField txtEstudiante;
    private JTextField txtDocente;
    private JTextField txtFecha;
    private JTextField txtTipoFalta;
    private JTextField txtObservacion;
    private JTextField txtMedida;
    private JTextField txtEstado;

    public ConvivenciaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Convivencia"));

        txtEstudiante = new JTextField();
        txtDocente = new JTextField();
        txtFecha = new JTextField("2026-11-03");
        txtTipoFalta = new JTextField("LEVE");
        txtObservacion = new JTextField();
        txtMedida = new JTextField();
        txtEstado = new JTextField("PENDIENTE");

        form.add(new JLabel("ID Estudiante:"));
        form.add(txtEstudiante);
        form.add(new JLabel("ID Docente:"));
        form.add(txtDocente);
        form.add(new JLabel("Fecha (yyyy-mm-dd):"));
        form.add(txtFecha);
        form.add(new JLabel("Tipo falta:"));
        form.add(txtTipoFalta);
        form.add(new JLabel("Observación:"));
        form.add(txtObservacion);
        form.add(new JLabel("Medida disciplinaria:"));
        form.add(txtMedida);
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
                new String[]{"ID", "Estudiante", "Docente", "Fecha", "Tipo", "Observación", "Medida", "Estado"}, 0
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
            Convivencia c = new Convivencia();
            c.setIdEstudiante(Integer.parseInt(txtEstudiante.getText().trim()));
            c.setIdDocente(Integer.parseInt(txtDocente.getText().trim()));
            c.setFechaReporte(java.sql.Date.valueOf(txtFecha.getText().trim()));
            c.setTipoFalta(txtTipoFalta.getText().trim());
            c.setObservacion(txtObservacion.getText().trim());
            c.setMedidaDisciplinaria(txtMedida.getText().trim());
            c.setEstado(txtEstado.getText().trim());
            if (controller.registrarConvivencia(c)) {
                JOptionPane.showMessageDialog(this, "Convivencia registrada.");
                listar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void actualiza() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un reporte.");
            return;
        }
        try {
            Convivencia c = new Convivencia();
            c.setIdConvivencia(Integer.parseInt(modeloTabla.getValueAt(row, 0).toString()));
            c.setIdEstudiante(Integer.parseInt(txtEstudiante.getText().trim()));
            c.setIdDocente(Integer.parseInt(txtDocente.getText().trim()));
            c.setFechaReporte(java.sql.Date.valueOf(txtFecha.getText().trim()));
            c.setTipoFalta(txtTipoFalta.getText().trim());
            c.setObservacion(txtObservacion.getText().trim());
            c.setMedidaDisciplinaria(txtMedida.getText().trim());
            c.setEstado(txtEstado.getText().trim());
            if (controller.actualizarConvivencia(c)) {
                JOptionPane.showMessageDialog(this, "Convivencia actualizada.");
                listar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un reporte.");
            return;
        }
        int id = Integer.parseInt(modeloTabla.getValueAt(row, 0).toString());
        if (controller.eliminarConvivencia(id)) {
            JOptionPane.showMessageDialog(this, "Convivencia eliminada.");
            listar();
        }
    }

    private void listar() {
        try {
            int idEstudiante = Integer.parseInt(txtEstudiante.getText().trim());
            List<Convivencia> lista = controller.listarConvivencias(idEstudiante);
            modeloTabla.setRowCount(0);
            for (Convivencia c : lista) {
                modeloTabla.addRow(new Object[]{
                        c.getIdConvivencia(),
                        c.getIdEstudiante(),
                        c.getIdDocente(),
                        c.getFechaReporte(),
                        c.getTipoFalta(),
                        c.getObservacion(),
                        c.getMedidaDisciplinaria(),
                        c.getEstado()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}