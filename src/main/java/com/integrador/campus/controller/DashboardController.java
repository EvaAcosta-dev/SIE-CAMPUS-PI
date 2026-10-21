package com.integrador.campus.controller;

import com.integrador.campus.dao.CursoDao;
import com.integrador.campus.dao.DocenteDao;
import com.integrador.campus.dao.EstudianteDao;
import com.integrador.campus.dao.PadreDao;
import com.integrador.campus.dao.UsuarioDao;
import com.integrador.campus.model.Cursos;
import com.integrador.campus.model.Usuario;
import com.integrador.campus.view.DashboardFrame;

public class DashboardController {

    private final DashboardFrame vista;

    public DashboardController(String nombreUsuario, String rol) {
        vista = new DashboardFrame(nombreUsuario, rol);

        Usuario usuario = new Usuario();
        UsuarioDao usuariosDao = new UsuarioDao();
        new UsuarioController(usuariosDao, usuario, vista.getUsuarioPanel());

        new EstudianteController(new com.integrador.campus.dao.EstudianteDao(), vista.getEstudiantePanel(), rol);

        new DocenteController(new com.integrador.campus.dao.DocenteDao(), vista.getDocentePanel(), rol);

        new AcudienteController(new PadreDao(), new EstudianteDao(), vista.getAcudientePanel(), rol); 
    }

    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
}
