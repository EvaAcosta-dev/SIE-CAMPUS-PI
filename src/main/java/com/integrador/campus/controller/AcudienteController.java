package com.integrador.campus.controller;

import com.integrador.campus.dao.EstudianteDao;
import com.integrador.campus.dao.PadreDao;
import com.integrador.campus.model.Estudiante;
import com.integrador.campus.model.Padre;
import com.integrador.campus.view.Modulo.AcudientePanel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;

public class AcudienteController {

    private static final String REGEX_CLAVE = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$";

    private final PadreDao dao;
    private final EstudianteDao estudianteDao;
    private final AcudientePanel vista;
    private final boolean puedeEditar;

    // Acudientes que hay actualmente en la tabla
    private List<Padre> actuales = new ArrayList<>();
    // Acudiente elegido en la tabla (null si no hay ninguno)
    private Padre seleccionado = null;

    public AcudienteController(PadreDao dao, EstudianteDao estudianteDao,
                               AcudientePanel vista, String rol) {
        this.dao = dao;
        this.estudianteDao = estudianteDao;
        this.vista = vista;
        this.puedeEditar = "ADMIN".equals(rol) || "REGISTRO ACADEMICO".equals(rol);

        if (!puedeEditar) {
            vista.setSoloLectura();
        }

        configurarEventos();

        // Cada vez que se abre el módulo se recarga todo,
        // así aparecen los estudiantes registrados después
        vista.setAlMostrar(() -> {
            limpiar();
            cargarAcudientes();
            cargarEstudiantesCombo();
        });

        cargarAcudientes();
        cargarEstudiantesCombo();
    }

    private void configurarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardar());
        vista.getBtnActualizar().addActionListener(e -> actualizar());
        vista.getBtnEliminar().addActionListener(e -> eliminar());
        vista.getBtnLimpiar().addActionListener(e -> limpiar());
        vista.getBtnBuscar().addActionListener(e -> buscar());
        vista.getBtnMostrarTodos().addActionListener(e -> cargarAcudientes());
        vista.getTxtBuscar().addActionListener(e -> buscar());
        vista.getBtnVincular().addActionListener(e -> vincular());
        vista.getBtnQuitarVinculo().addActionListener(e -> quitarVinculo());

        vista.getTabla().getSelectionModel().addListSelectionListener(
            (ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    seleccionar();
                }
            });
    }

    private void cargarAcudientes() {
        try {
            actuales = dao.listar(null);
            vista.mostrarAcudientes(actuales);
        } catch (SQLException e) {
            vista.mostrarError("No fue posible cargar los acudientes: " + e.getMessage());
        }
    }

    private void buscar() {
        String texto = vista.getTextoBusqueda();
        if (texto.isEmpty()) {
            cargarAcudientes();
            return;
        }
        try {
            actuales = dao.listar(texto);
            vista.mostrarAcudientes(actuales);
            if (actuales.isEmpty()) {
                vista.mostrarMensaje("No se encontraron acudientes con ese criterio.");
            }
        } catch (SQLException e) {
            vista.mostrarError("Error al buscar: " + e.getMessage());
        }
    }

    private void guardar() {
        if (!tienePermiso() || !validar(true)) {
            return;
        }

        // El acudiente se registra junto con su primer estudiante
        Estudiante est = vista.getEstudianteNuevo();

        try {
            dao.registrar(leerFormulario(), est.getIdEstudiante(), vista.getParentescoNuevo());
            vista.mostrarMensaje("Acudiente registrado y vinculado a "
                    + est.getNombre() + " " + est.getApellido() + ".");
            cargarAcudientes();
            limpiar();
        } catch (SQLException e) {
            vista.mostrarError(mensajeSql(e, "registrar"));
        }
    }

    private void actualizar() {
        if (!tienePermiso()) {
            return;
        }
        if (seleccionado == null) {
            vista.mostrarError("Selecciona un acudiente de la tabla.");
            return;
        }
        if (!validar(false)) {
            return;
        }

        Padre p = leerFormulario();
        p.setIdPadre(seleccionado.getIdPadre());
        p.setIdUsuario(seleccionado.getIdUsuario());

        try {
            dao.actualizar(p);
            vista.mostrarMensaje("Acudiente actualizado correctamente.");
            cargarAcudientes();
            limpiar();
        } catch (SQLException e) {
            vista.mostrarError(mensajeSql(e, "actualizar"));
        }
    }

    private void eliminar() {
        if (!tienePermiso()) {
            return;
        }
        if (seleccionado == null) {
            vista.mostrarError("Selecciona un acudiente de la tabla.");
            return;
        }
        if (!vista.confirmar("¿Seguro que deseas eliminar a este acudiente, "
                + "su usuario y sus vínculos con estudiantes?")) {
            return;
        }
        try {
            dao.eliminar(seleccionado);
            vista.mostrarMensaje("Acudiente eliminado correctamente.");
            cargarAcudientes();
            limpiar();
        } catch (SQLException e) {
            vista.mostrarError("No se pudo eliminar: " + e.getMessage());
        }
    }

    // ----- Vínculos con estudiantes -----

    private void vincular() {
        if (!tienePermiso()) {
            return;
        }
        if (seleccionado == null) {
            vista.mostrarError("Selecciona un acudiente de la tabla.");
            return;
        }
        Estudiante est = vista.getEstudianteCombo();
        if (est == null) {
            vista.mostrarError("No hay estudiantes para vincular. Registra uno primero.");
            return;
        }
        try {
            boolean creado = dao.vincular(
                seleccionado.getIdPadre(),
                est.getIdEstudiante(),
                vista.getParentescoSeleccionado());

            if (creado) {
                cargarVinculos();
            } else {
                vista.mostrarError("Ese estudiante ya está vinculado a este acudiente.");
            }
        } catch (SQLException e) {
            vista.mostrarError("No se pudo vincular: " + e.getMessage());
        }
    }

    private void quitarVinculo() {
        if (!tienePermiso()) {
            return;
        }
        int idRelacion = vista.getIdRelacionSeleccionada();
        if (idRelacion == -1) {
            vista.mostrarError("Selecciona un estudiante de la lista de abajo.");
            return;
        }

        // Un acudiente siempre debe tener al menos un estudiante a cargo
        if (vista.getCantidadVinculos() <= 1) {
            vista.mostrarError("Un acudiente debe tener al menos un estudiante a cargo.\n"
                + "Vincula otro estudiante antes de quitar este, "
                + "o elimina al acudiente.");
            return;
        }

        if (!vista.confirmar("¿Quitar el vínculo con este estudiante?")) {
            return;
        }
        try {
            dao.desvincular(idRelacion);
            cargarVinculos();
        } catch (SQLException e) {
            vista.mostrarError("No se pudo quitar el vínculo: " + e.getMessage());
        }
    }

    private void cargarVinculos() {
        if (seleccionado == null) {
            return;
        }
        try {
            vista.mostrarVinculos(dao.listarVinculos(seleccionado.getIdPadre()));
        } catch (SQLException e) {
            vista.mostrarError("No fue posible cargar los estudiantes a cargo: " + e.getMessage());
        }
    }

    private void cargarEstudiantesCombo() {
        try {
            vista.mostrarEstudiantesCombo(estudianteDao.listar(null));
        } catch (SQLException e) {
            vista.mostrarError("No fue posible cargar los estudiantes: " + e.getMessage());
        }
    }

    // ----- Utilidades -----

    private void limpiar() {
        seleccionado = null;
        vista.limpiarFormulario();
    }

    private void seleccionar() {
        Padre p = buscarSeleccionado();
        if (p == null) {
            return;
        }
        seleccionado = p;
        vista.cargarEnFormulario(p);
        cargarVinculos();
    }

    // Busca en la lista actual al acudiente de la fila seleccionada
    private Padre buscarSeleccionado() {
        int filaVista = vista.getTabla().getSelectedRow();
        if (filaVista == -1) {
            return null;
        }
        int filaModelo = vista.getTabla().convertRowIndexToModel(filaVista);
        int id = Integer.parseInt(
            vista.getModeloTabla().getValueAt(filaModelo, 0).toString());

        for (Padre p : actuales) {
            if (p.getIdPadre() == id) {
                return p;
            }
        }
        return null;
    }

    private Padre leerFormulario() {
        Padre p = new Padre();
        p.setNombre(vista.getNombre());
        p.setApellido(vista.getApellido());
        p.setTelefono(vista.getTelefono().isEmpty() ? null : vista.getTelefono());
        p.setCorreo(vista.getCorreo());
        p.setContrasena(vista.getContrasena());
        return p;
    }

    private boolean validar(boolean esNuevo) {
        if (vista.getNombre().isEmpty() || vista.getApellido().isEmpty()
                || vista.getCorreo().isEmpty()) {
            vista.mostrarError("Nombres, apellidos y correo son obligatorios.");
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
        if (vista.getCorreo().length() > 55) {
            vista.mostrarError("El correo del acudiente no puede pasar de 55 caracteres.");
            return false;
        }

        // No se registra un acudiente sin estudiante
        if (esNuevo && vista.getEstudianteNuevo() == null) {
            vista.mostrarError("Para registrar un acudiente debes elegir un estudiante a cargo.\n"
                + "Si no aparece ninguno, primero registra al estudiante en el módulo Estudiantes.");
            return false;
        }

        String clave = vista.getContrasena();
        boolean hayClave = !clave.trim().isEmpty();

        if (esNuevo && !hayClave) {
            vista.mostrarError("Ingresa una contraseña para el nuevo acudiente.");
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
            vista.mostrarError("No tienes permiso para modificar acudientes.");
            return false;
        }
        return true;
    }

    // 1062 = dato duplicado en MySQL
    private String mensajeSql(SQLException e, String accion) {
        if (e.getErrorCode() == 1062) {
            return "Ya existe un usuario con ese correo.";
        }
        return "Error al " + accion + " el acudiente: " + e.getMessage();
    }
}