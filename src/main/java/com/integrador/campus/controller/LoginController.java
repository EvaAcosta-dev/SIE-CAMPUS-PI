package com.integrador.campus.controller;

import com.integrador.campus.dao.UsuarioDao;
import com.integrador.campus.model.Usuario;
//import com.integrador.campus.controller.DashboardController;
import com.integrador.campus.view.LoginFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginController implements ActionListener {

    private final UsuarioDao usuariosDao;
    private final LoginFrame loginView;

    public LoginController(UsuarioDao usuariosDao, LoginFrame loginView) {
        this.usuariosDao = usuariosDao;
        this.loginView = loginView;
        this.loginView.btnIngresar.addActionListener(this);// Conectar el botón con el controlador

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginView.btnIngresar) {
            realizarLogin();
        }
    }

    private void realizarLogin() {
        String correo = loginView.txtCorreo.getText().trim();
        char[] passArray = loginView.txtContrasena.getPassword();
        String contrasena = new String(passArray);

       

        if (correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Por favor complete todos los campos.", 
             "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioLogeado = usuariosDao.login(correo, contrasena);

        if (usuarioLogeado == null) {
            JOptionPane.showMessageDialog(
                loginView,
                "Correo o contraseña incorrectos.",
                "Error de autenticación",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Nombre completo para mostrar en el dashboard
        String nombreCompleto = usuarioLogeado.getNombre() + " " + usuarioLogeado.getApellido();
        DashboardController dashController = new DashboardController(
                nombreCompleto, usuarioLogeado.getRol().getNombreRol());
        dashController.iniciar();
        loginView.dispose();
    }
}    