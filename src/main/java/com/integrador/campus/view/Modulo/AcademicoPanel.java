package com.integrador.campus.view.Modulo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;
import com.integrador.campus.controller.AnioController;
import com.integrador.campus.controller.GradoController;
import com.integrador.campus.controller.NivelController;
import com.integrador.campus.controller.PeriodoController;
import com.integrador.campus.dao.AnioLectivoDao;
import com.integrador.campus.dao.GradoDao;
import com.integrador.campus.dao.NivelDao;
import com.integrador.campus.dao.PeriodoDao;
import com.integrador.campus.model.AnioLectivo;
import com.integrador.campus.model.Periodo;


public class AcademicoPanel extends JPanel {

    private final Color fondo = new Color(245, 247, 251);
    // Instanciamos el panel de cursos y paterias
    private final AnioFormPanel anioFormPanel = new AnioFormPanel();
    private final NivelFormPanel nivelPanel=new NivelFormPanel();
    private final GradoFormPanel gradoFormPanel = new GradoFormPanel();
    private final PeriodoFormPanel periodoPanel=new PeriodoFormPanel();

    Periodo periodo=new Periodo();
    PeriodoDao periodoDao=new PeriodoDao();
    AnioLectivoDao anioDao=new AnioLectivoDao();
    PeriodoController periodoController=new PeriodoController(periodoPanel, periodoDao, anioDao);

    AnioLectivo anioLectivo=new AnioLectivo();
    AnioLectivoDao anioLectivoDao=new AnioLectivoDao();
    AnioController anioController=new AnioController(anioFormPanel,anioLectivo,anioLectivoDao);

    NivelDao nivelDao=new NivelDao();
    NivelController nivelController=new NivelController(nivelPanel, nivelDao);
    GradoDao gradoDao =new GradoDao();
    GradoController gradoController=new GradoController(gradoFormPanel, gradoDao, nivelDao);

    public  AcademicoPanel (){
        setLayout(new BorderLayout(15, 15));
        setBackground(fondo);
        setBorder(new EmptyBorder(2, 15, 15, 15));

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
