package com.integrador.campus.view.Modulo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.integrador.campus.controller.CursoController;
import com.integrador.campus.controller.MateriaController;
import com.integrador.campus.dao.CursoDao;
import com.integrador.campus.dao.MateriaDao;
import com.integrador.campus.model.Cursos;
import com.integrador.campus.model.Materia;

import java.awt.*;

public class CursoMateriaPanel extends JPanel {
    private final Color fondo = new Color(245, 247, 251);

    // Instanciamos el panel de cursos y paterias
    private final CursoFormPanel cursoFormPanel = new CursoFormPanel();
    private final MateriaFormPanel materiaPanel=new MateriaFormPanel();

    Cursos cursos=new Cursos(); 
    CursoDao cursoDao= new CursoDao(); 
    CursoController cursoController= new CursoController(cursoDao,cursos, cursoFormPanel);

    Materia materia=new Materia();
    MateriaDao materiaDao=new MateriaDao();
    MateriaController materiaController=new MateriaController(materiaPanel, materia, materiaDao);
    public CursoMateriaPanel() {

        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Crear el componente de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Agregar las pestañas al contenedor principal
        tabbedPane.addTab("Cursos Académicos", cursoFormPanel);
        tabbedPane.addTab("Materias / Asignaturas", materiaPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    
}