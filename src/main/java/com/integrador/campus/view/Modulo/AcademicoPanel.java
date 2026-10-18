package com.integrador.campus.view.Modulo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;


public class AcademicoPanel extends JPanel {

    private final Color fondo = new Color(245, 247, 251);
    // Instanciamos el panel de cursos y paterias
    private final AnioFormPanel anioFormPanel = new AnioFormPanel();
    private final NivelFormPanel nivelPanel=new NivelFormPanel();
    private final GradoFormPanel gradoFormPanel = new GradoFormPanel();
    private final PeriodoFormPanel periodoPanel=new PeriodoFormPanel();


    public  AcademicoPanel (){
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Crear el componente de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
    // Agregar los paneles correspondientes a cada pestaña
        tabbedPane.addTab("Año Escolar", anioFormPanel);
        tabbedPane.addTab("Niveles Escolares", nivelPanel);
        tabbedPane.addTab("Grados", gradoFormPanel);
        tabbedPane.addTab("Periodos Académicos", periodoPanel);

        // Agregar el contenedor al panel principal
        add(tabbedPane, BorderLayout.CENTER);
    }
}
