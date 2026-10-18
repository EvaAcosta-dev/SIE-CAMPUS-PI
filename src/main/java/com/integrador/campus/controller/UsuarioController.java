
package com.integrador.campus.controller;

import com.integrador.campus.dao.UsuarioDao;
import com.integrador.campus.model.Rol;
import com.integrador.campus.model.Usuario;
import com.integrador.campus.view.Modulo.UsuarioPanel;

import java.sql.SQLException;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JOptionPane;

public class UsuarioController {

   private final UsuarioPanel vista;
    private final UsuarioDao dao;
    private final Usuario usuario;

    public UsuarioController(UsuarioDao dao, Usuario usuario, UsuarioPanel vista) {
        this.dao = dao;
        this.usuario = usuario;
        this.vista = vista;

        configurarEventos();
        cargarRoles();
        cargarUsuarios();
    }

    // Configura los eventos de los botones y de la tabla.
    private void configurarEventos() {

        vista.getBtnGuardar().addActionListener(e -> guardarUsuario());

        vista.getBtnActualizar().addActionListener(e -> actualizarUsuario());

        vista.getBtnEliminar().addActionListener(e -> eliminarUsuario());

        vista.getBtnCambiarEstado().addActionListener(e -> cambiarEstado());

        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarFormulario());

        vista.getBtnBuscar().addActionListener(e -> buscarUsuarios());

        vista.getBtnMostrarTodos().addActionListener(e -> cargarUsuarios());

        vista.getTxtBuscar().addActionListener(e -> buscarUsuarios());

        vista.getTabla().getSelectionModel().addListSelectionListener(
                (ListSelectionEvent e) -> {
                    if (!e.getValueIsAdjusting()) {
                        seleccionarUsuario();
                    }
                }
        );
    }

    // Carga los roles disponibles en el formulario.
    private void cargarRoles() {
        List<Rol> roles = dao.listaRoles();
        vista.mostrarRoles(roles);
    }

    // Consulta y muestra todos los usuarios.
    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = dao.listaUsuario();
            
            if (usuarios.isEmpty()) {
                vista.mostrarUsuarios(usuarios); // Limpia la tabla visualmente
                vista.mostrarMensaje("No hay usuarios registrados en el sistema actualmente.");
            } else {
                vista.mostrarUsuarios(usuarios);
            }
        } catch (Exception e) {
            vista.mostrarError(
                    "No fue posible cargar los usuarios: " + e.getMessage()
            );
        }
    }

    // Registra un usuario nuevo.
    private void guardarUsuario() {

        if (!validarFormulario()) {
            return;
        }

        Rol rol = vista.getRolSeleccionado();

        Usuario usuario = new Usuario();
        usuario.setNombre(vista.getNombre().trim());
        usuario.setApellido(vista.getApellido().trim());
        usuario.setCorreo(vista.getCorreo().trim());
        usuario.setContrasena(vista.getContrasena());
        usuario.setIdRol(rol.getIdRol());
        usuario.setRol(rol);
        usuario.setActivo(vista.isCuentaActiva());

        try {
            boolean guardado = dao.registrarUsuario(usuario);

            if (guardado) {
                vista.mostrarMensaje("Usuario registrado correctamente.");
                cargarUsuarios();
                vista.limpiarFormulario();
            } else {
                vista.mostrarError(
                        "No se pudo registrar el usuario. "
                        + "Verifica los datos y la conexión con la base de datos."
                );
            }

        } catch (Exception e) {
            vista.mostrarError(
                    "Error al registrar el usuario: " + e.getMessage()
            );
        }
    }

    // Actualiza los datos del usuario seleccionado.
    private void actualizarUsuario() {

        int id = vista.getIdUsuarioSeleccionado();

        if (id == -1) {
            vista.mostrarError("Selecciona un usuario de la tabla.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Rol rol = vista.getRolSeleccionado();

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setNombre(vista.getNombre().trim());
        usuario.setApellido(vista.getApellido().trim());
        usuario.setCorreo(vista.getCorreo().trim());

        // Si la contraseña queda vacía, el DAO conserva la actual.
        usuario.setContrasena(vista.getContrasena());

        usuario.setIdRol(rol.getIdRol());
        usuario.setRol(rol);
        usuario.setActivo(vista.isCuentaActiva());

        try {
            boolean actualizado = dao.actualizarUsuario(usuario);

            if (actualizado) {
                vista.mostrarMensaje("Usuario actualizado correctamente.");
                cargarUsuarios();
                vista.limpiarFormulario();
            } else {
                vista.mostrarError("No se pudo actualizar el usuario.");
            }

        } catch (Exception e) {
            vista.mostrarError(
                    "Error al actualizar el usuario: " + e.getMessage()
            );
        }
    }

    // Elimina el usuario seleccionado.
    private void eliminarUsuario() {

        int id = vista.getIdUsuarioSeleccionado();

        if (id == -1) {
            vista.mostrarError("Selecciona un usuario de la tabla.");
            return;
        }

        if (!vista.confirmar(
                "¿Estás segura de que deseas eliminar este usuario?"
        )) {
            return;
        }

        try {
            boolean eliminado = dao.eliminarUsuario(id);

            if (eliminado) {
                vista.mostrarMensaje("Usuario eliminado correctamente.");
                cargarUsuarios();
                vista.limpiarFormulario();
            } else {
                vista.mostrarError(
                        "No se pudo eliminar el usuario."
                );
            }

        } catch (Exception e) {
            vista.mostrarError(
                    "Error al eliminar el usuario. "
                    + "Es posible que tenga registros relacionados: "
                    + e.getMessage()
            );
        }
    }

    // Aplica el estado elegido en el formulario.
    private void cambiarEstado() {

        int id = vista.getIdUsuarioSeleccionado();

        if (id == -1) {
            vista.mostrarError("Selecciona un usuario de la tabla.");
            return;
        }

        boolean activo = vista.isCuentaActiva();

        try {
            boolean actualizado = dao.cambiarEstadoUsuario(id, activo);

            if (actualizado) {
                vista.mostrarMensaje(
                        activo
                                ? "La cuenta está activa."
                                : "La cuenta está inactiva."
                );

                cargarUsuarios();
                vista.limpiarFormulario();
            } else {
                vista.mostrarError("No se pudo cambiar el estado.");
            }

        } catch (Exception e) {
            vista.mostrarError(
                    "Error al cambiar el estado: " + e.getMessage()
            );
        }
    }

    // Busca usuarios por nombre, apellido, correo o rol.
    private void buscarUsuarios() {

        String texto = vista.getTextoBusqueda().trim();

        if (texto.isEmpty()) {
            cargarUsuarios();
            return;
        }

        try {
            List<Usuario> usuarios = dao.listaUsuario(texto);
            vista.mostrarUsuarios(usuarios);

            if (usuarios.isEmpty()) {
                vista.mostrarMensaje("No se encontraron usuarios con ese criterio.");
                cargarUsuarios();
            }

        } catch (SQLException e) {
            vista.mostrarError(
                    "Error al buscar usuarios: " + e.getMessage()
            );
        }
    }

    // Carga en el formulario los datos de la fila seleccionada.
    private void seleccionarUsuario() {

        int filaVista = vista.getTabla().getSelectedRow();

        if (filaVista == -1) {
            return;
        }

        int filaModelo = vista.getTabla()
                .convertRowIndexToModel(filaVista);

        Object valorId = vista.getModeloTabla()
                .getValueAt(filaModelo, 0);

        int id;

        try {
            id = Integer.parseInt(valorId.toString());
        } catch (NumberFormatException e) {
            vista.mostrarError("No fue posible identificar el usuario.");
            return;
        }

        List<Usuario> usuarios = dao.listaUsuario();

        for (Usuario usuario : usuarios) {
            if (usuario.getIdUsuario() == id) {
                vista.cargarUsuarioEnFormulario(usuario);
                return;
            }
        }

        vista.mostrarError(
                "No se encontró el usuario seleccionado."
        );
    }

    // Valida los datos obligatorios.
    private boolean validarFormulario() {

        if (vista.getNombre().trim().isEmpty() || vista.getApellido().trim().isEmpty() || vista.getCorreo().trim().isEmpty()||vista.getContrasena().trim().isEmpty()) {
            vista.mostrarError("Todos los campos del usuario son obligatorios.");
            return false;
        }

        String correo = vista.getCorreo().trim();

        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            vista.mostrarError("Ingresa un correo electrónico válido.");
            return false;
        }
        String contrasena = vista.getContrasena();

        if (!contrasena.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$")) {
            if (contrasena == null || contrasena.trim().isEmpty()) {
                vista.mostrarError("La contraseña es obligatoria para nuevos usuarios.");
                return false;
            }
            if (!contrasena.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$")) {
                vista.mostrarError("La contraseña es insegura.\nDebe tener al menos 6 caracteres, una mayúscula, una minúscula y un número.");
                return false;
            }
        } else {
            if (contrasena != null && !contrasena.trim().isEmpty()) {
                if (!contrasena.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$")) {
                    vista.mostrarError("La nueva contraseña es insegura.\nDebe tener al menos 6 caracteres, una mayúscula, una minúscula y un número.");
                    return false;
                }
            }
        }
        if (vista.getIdUsuarioSeleccionado() == -1
                && vista.getContrasena().trim().isEmpty()) {
            vista.mostrarError(
                    "Ingresa una contraseña para el nuevo usuario."
            );
            return false;
        }

        if (vista.getRolSeleccionado() == null) {
            vista.mostrarError("Selecciona un rol.");
            return false;
        }

        return true;
    }
}
