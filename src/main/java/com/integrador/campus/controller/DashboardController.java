package com.integrador.campus.controller;

import com.integrador.campus.dao.CursoDao;
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

        
    }

    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
}
