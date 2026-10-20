package com.integrador.campus.controller;

import com.integrador.campus.dao.EstudianteDao;
import com.integrador.campus.model.Estudiante;
import com.integrador.campus.view.Modulo.EstudiantePanel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;

public class EstudianteController {

    private static final String REGEX_CLAVE = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$";

    private final EstudianteDao dao;
    private final EstudiantePanel vista;
    private final boolean puedeEditar;

    // Estudiantes que hay actualmente en la tabla
    private List<Estudiante> actuales = new ArrayList<>();

    public EstudianteController(EstudianteDao dao, EstudiantePanel vista, String rol) {
        this.dao = dao;
        this.vista = vista;
        this.puedeEditar = "ADMIN".equals(rol) || "REGISTRO ACADEMICO".equals(rol);

        if (!puedeEditar) {
            vista.setSoloLectura();
        }

        configurarEventos();
        cargarEstudiantes();
    }

    private void configurarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarFormulario());
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getBtnMostrarTodos().addActionListener(e -> cargarEstudiantes());
        vista.getTxtBuscar().addActionListener(e -> buscar());

        vista.getTabla().getSelectionModel().addListSelectionListener(
            (ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    seleccionar();
                }
            });
    }

    private void cargarEstudiantes() {
        try {
            actuales = dao.listar(null);
            vista.mostrarEstudiantes(actuales);
        } catch (SQLException e) {
            vista.mostrarError("No fue posible cargar los estudiantes: " + e.getMessage());
        }
    }

    private void buscar() {
        String texto = vista.getTextoBusqueda();
        if (texto.isEmpty()) {
            cargarEstudiantes();
            return;
        }
        try {
            actuales = dao.listar(texto);
            vista.mostrarEstudiantes(actuales);
            if (actuales.isEmpty()) {
                vista.mostrarMensaje("No se encontraron estudiantes con ese criterio.");
            }
        } catch (SQLException e) {
            vista.mostrarError("Error al buscar: " + e.getMessage());
        }
    }

    private void guardar() {
        if (!tienePermiso() || !validar(true)) {
            return;
        }
        try {
            dao.registrar(leerFormulario());
            vista.mostrarMensaje("Estudiante registrado correctamente.");
            cargarEstudiantes();
            vista.limpiarFormulario();
        } catch (SQLException e) {
            vista.mostrarError(mensajeSql(e, "registrar"));
        }
    }

    private void actualizar() {
        if (!tienePermiso()) {
            return;
        }
        Estudiante seleccionado = buscarSeleccionado();
        if (seleccionado == null) {
            vista.mostrarError("Selecciona un estudiante de la tabla.");
            return;
        }
        if (!validar(false)) {
            return;
        }

        Estudiante est = leerFormulario();
        est.setIdEstudiante(seleccionado.getIdEstudiante());
        est.setIdUsuario(seleccionado.getIdUsuario());

        try {
            dao.actualizar(est);
            vista.mostrarMensaje("Estudiante actualizado correctamente.");
            cargarEstudiantes();
            vista.limpiarFormulario();
        } catch (SQLException e) {
            vista.mostrarError(mensajeSql(e, "actualizar"));
        }
    }

    private void eliminar() {
        if (!tienePermiso()) {
            return;
        }
        Estudiante seleccionado = buscarSeleccionado();
        if (seleccionado == null) {
            vista.mostrarError("Selecciona un estudiante de la tabla.");
            return;
        }
        if (!vista.confirmar("¿Seguro que deseas eliminar a este estudiante y su usuario?")) {
            return;
        }
        try {
            dao.eliminar(seleccionado);
            vista.mostrarMensaje("Estudiante eliminado correctamente.");
            cargarEstudiantes();
            vista.limpiarFormulario();
        } catch (SQLException e) {
            vista.mostrarError(
                "No se pudo eliminar. Es posible que tenga matrículas u otros "
                + "registros relacionados.\n" + e.getMessage());
        }
    }

    private void seleccionar() {
        Estudiante e = buscarSeleccionado();
        if (e != null) {
            vista.cargarEnFormulario(e);
        }
    }

    // Busca en la lista actual al estudiante de la fila seleccionada
    private Estudiante buscarSeleccionado() {
        int filaVista = vista.getTabla().getSelectedRow();
        if (filaVista == -1) {
            return null;
        }
        int filaModelo = vista.getTabla().convertRowIndexToModel(filaVista);
        int id = Integer.parseInt(
            vista.getModeloTabla().getValueAt(filaModelo, 0).toString());

        for (Estudiante e : actuales) {
            if (e.getIdEstudiante() == id) {
                return e;
            }
        }
        return null;
    }

    private Estudiante leerFormulario() {
        Estudiante e = new Estudiante();
        e.setNombre(vista.getNombre());
        e.setApellido(vista.getApellido());
        e.setNumDocumento(vista.getDocumento());
        e.setFechaNacimiento(new java.sql.Date(vista.getFechaNacimiento().getTime()));
        e.setTelefono(vista.getTelefono().isEmpty() ? null : vista.getTelefono());
        e.setCorreo(vista.getCorreo());
        e.setContrasena(vista.getContrasena());
        e.setActivo(true);
        return e;
    }

    private boolean validar(boolean esNuevo) {
        if (vista.getNombre().isEmpty() || vista.getApellido().isEmpty()
                || vista.getDocumento().isEmpty() || vista.getCorreo().isEmpty()) {
            vista.mostrarError("Nombres, apellidos, documento y correo son obligatorios.");
            return false;
        }
        if (!vista.getDocumento().matches("\\d{5,15}")) {
            vista.mostrarError("El documento debe tener entre 5 y 15 números.");
            return false;
        }
        if (vista.getFechaNacimiento() == null) {
            vista.mostrarError("Selecciona la fecha de nacimiento.");
            return false;
        }
        if (vista.getFechaNacimiento().after(new java.util.Date())) {
            vista.mostrarError("La fecha de nacimiento no puede ser futura.");
            return false;
        }
        if (!vista.getTelefono().isEmpty() && !vista.getTelefono().matches("\\d{7,15}")) {
            vista.mostrarError("El teléfono debe tener entre 7 y 15 números.");
            return false;
        }
        if (!vista.getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            vista.mostrarError("Ingresa un correo electrónico válido.");
            return false;
        }

        String clave = vista.getContrasena();
        boolean hayClave = !clave.trim().isEmpty();

        if (esNuevo && !hayClave) {
            vista.mostrarError("Ingresa una contraseña para el nuevo estudiante.");
            return false;
        }
        if (hayClave && !clave.matches(REGEX_CLAVE)) {
            vista.mostrarError("La contraseña es insegura.\n"
                + "Debe tener al menos 6 caracteres, una mayúscula, una minúscula y un número.");
            return false;
        }
        return true;
    }

    private boolean tienePermiso() {
        if (!puedeEditar) {
            vista.mostrarError("No tienes permiso para modificar estudiantes.");
            return false;
        }
        return true;
    }

    // 1062 = dato duplicado en MySQL
    private String mensajeSql(SQLException e, String accion) {
        if (e.getErrorCode() == 1062) {
            return "Ya existe un registro con ese documento o ese correo.";
        }
        return "Error al " + accion + " el estudiante: " + e.getMessage();
    }
}