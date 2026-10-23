package com.integrador.campus.controller;

import com.integrador.campus.dao.DocenteDao;
import com.integrador.campus.model.Docente;
import com.integrador.campus.view.Modulo.DocentePanel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;

public class DocenteController {

    private static final String REGEX_CLAVE = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$";

    private final DocenteDao dao;
    private final DocentePanel vista;
    private final boolean puedeEditar;

    // Docentes que hay actualmente en la tabla
    private List<Docente> actuales = new ArrayList<>();

    public DocenteController(DocenteDao dao, DocentePanel vista, String rol) {
        this.dao = dao;
        this.vista = vista;
        this.puedeEditar = "ADMIN".equals(rol) || "REGISTRO ACADEMICO".equals(rol);

        configurarEventos();
        cargarDocentes();
    }

    private void configurarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarFormulario());
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getBtnMostrarTodos().addActionListener(e -> cargarDocentes());
        vista.getTxtBuscar().addActionListener(e -> buscar());

        vista.getTabla().getSelectionModel().addListSelectionListener(
            (ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    seleccionar();
                }
            });
    }

    private void cargarDocentes() {
        try {
            actuales = dao.listar(null);
            vista.mostrarDocentes(actuales);
        } catch (SQLException e) {
            vista.mostrarError("No fue posible cargar los docentes: " + e.getMessage());
        }
    }

    private void buscar() {
        String texto = vista.getTextoBusqueda();
        if (texto.isEmpty()) {
            cargarDocentes();
            return;
        }
        try {
            actuales = dao.listar(texto);
            vista.mostrarDocentes(actuales);
            if (actuales.isEmpty()) {
                vista.mostrarMensaje("No se encontraron docentes con ese criterio.");
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
            vista.mostrarMensaje("Docente registrado correctamente.");
            cargarDocentes();
            vista.limpiarFormulario();
        } catch (SQLException e) {
            vista.mostrarError(mensajeSql(e, "registrar"));
        }
    }

    private void actualizar() {
        if (!tienePermiso()) {
            return;
        }
        Docente seleccionado = buscarSeleccionado();
        if (seleccionado == null) {
            vista.mostrarError("Selecciona un docente de la tabla.");
            return;
        }
        if (!validar(false)) {
            return;
        }

        Docente doc = leerFormulario();
        doc.setIdDocente(seleccionado.getIdDocente());
        doc.setIdUsuario(seleccionado.getIdUsuario());

        try {
            dao.actualizar(doc);
            vista.mostrarMensaje("Docente actualizado correctamente.");
            cargarDocentes();
            vista.limpiarFormulario();
        } catch (SQLException e) {
            vista.mostrarError(mensajeSql(e, "actualizar"));
        }
    }

    private void eliminar() {
        if (!tienePermiso()) {
            return;
        }
        Docente seleccionado = buscarSeleccionado();
        if (seleccionado == null) {
            vista.mostrarError("Selecciona un docente de la tabla.");
            return;
        }
        if (!vista.confirmar("¿Seguro que deseas eliminar a este docente y su usuario?")) {
            return;
        }
        try {
            dao.eliminar(seleccionado);
            vista.mostrarMensaje("Docente eliminado correctamente.");
            cargarDocentes();
            vista.limpiarFormulario();
        } catch (SQLException e) {
            vista.mostrarError(
                "No se pudo eliminar. Es posible que tenga materias o cursos "
                + "asignados.\n" + e.getMessage());
        }
    }

    private void seleccionar() {
        Docente d = buscarSeleccionado();
        if (d != null) {
            vista.cargarEnFormulario(d);
        }
    }

    // Busca en la lista actual al docente de la fila seleccionada
    private Docente buscarSeleccionado() {
        int filaVista = vista.getTabla().getSelectedRow();
        if (filaVista == -1) {
            return null;
        }
        int filaModelo = vista.getTabla().convertRowIndexToModel(filaVista);
        int id = Integer.parseInt(
            vista.getModeloTabla().getValueAt(filaModelo, 0).toString());

        for (Docente d : actuales) {
            if (d.getIdDocente() == id) {
                return d;
            }
        }
        return null;
    }

    private Docente leerFormulario() {
        Docente d = new Docente();
        d.setNombre(vista.getNombre());
        d.setApellido(vista.getApellido());
        d.setDocumento(vista.getDocumento());
        d.setCorreo(vista.getCorreo());
        d.setContrasena(vista.getContrasena());
        return d;
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
        if (!vista.getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            vista.mostrarError("Ingresa un correo electrónico válido.");
            return false;
        }

        String clave = vista.getContrasena();
        boolean hayClave = !clave.trim().isEmpty();

        if (esNuevo && !hayClave) {
            vista.mostrarError("Ingresa una contraseña para el nuevo docente.");
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
            vista.mostrarError("No tienes permiso para modificar docentes.");
            return false;
        }
        return true;
    }

    // 1062 = dato duplicado en MySQL
    private String mensajeSql(SQLException e, String accion) {
        if (e.getErrorCode() == 1062) {
            return "Ya existe un registro con ese documento o ese correo.";
        }
        return "Error al " + accion + " el docente: " + e.getMessage();
    }
}