package com.integrador.campus.view.Modulo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.integrador.campus.controller.CursoController;
import com.integrador.campus.dao.CursoDao;
import com.integrador.campus.model.Cursos;

import java.awt.*;

public class CursoMateriaPanel extends JPanel {
    private final Color fondo = new Color(245, 247, 251);

    // Instanciamos el panel de cursos 
    private final CursoFormPanel cursoFormPanel = new CursoFormPanel();
    Cursos cursos=new Cursos(); 
    CursoDao cursoDao= new CursoDao(); 
    CursoController cursoController= new CursoController(cursoDao,cursos, cursoFormPanel);

    public CursoMateriaPanel() {

        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Crear el componente de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Pestaña 1: Cursos 
        JPanel panelMaterias = crearPanelTemporal("Módulo de Gestión de Materias (Próximamente)");

        // Agregar las pestañas al contenedor principal
        tabbedPane.addTab("Cursos Académicos", cursoFormPanel);
        tabbedPane.addTab("Materias / Asignaturas", panelMaterias);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel crearPanelTemporal(String texto) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(label);
        return panel;
    }
}
    

