package com.integrador.campus;

import com.formdev.flatlaf.FlatLightLaf;
import com.integrador.campus.controller.LoginController;
import com.integrador.campus.dao.UsuarioDao;
import com.integrador.campus.view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            UsuarioDao usuarioDao = new UsuarioDao();
            new LoginController(usuarioDao, loginFrame);
         
           loginFrame.setVisible(true);
        });
    }
}