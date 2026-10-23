package com.integrador.campus.controller;

import com.integrador.campus.dao.AnioLectivoDao;
import com.integrador.campus.model.AnioLectivo;
import com.integrador.campus.view.Modulo.AnioFormPanel;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class AnioController {

    private final AnioFormPanel vista;
    private final AnioLectivoDao anioLectivoDao;
    private int idAnioSeleccionado = -1;

    public AnioController(
            AnioFormPanel vista,
            AnioLectivo anioLectivo,
            AnioLectivoDao anioLectivoDao) {
        this.vista = vista;
        this.anioLectivoDao = anioLectivoDao;
        configurarEventos();
        listarAnios("");
    }

    private void configurarEventos() {
        vista.getBtnGuardarAnio().addActionListener(e -> guardarAnio());
        vista.getBtnActualizarAnio().addActionListener(e -> actualizarAnio());
        vista.getBtnEliminarAnio().addActionListener(e -> eliminarAnio());
        vista.getBtnLimpiarAnio().addActionListener(e -> limpiarFormulario());
        vista.getBtnBuscarAnio().addActionListener(e -> buscarAnios());
        vista.getBtnMostrarAnio().addActionListener(e -> mostrarTodos());
        vista.getBtnCambiarEstadoAnio().addActionListener(e -> cambiarEstado());
        vista.getTxtBuscarAnio().addActionListener(e -> buscarAnios());

        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarAnio();
            }
        });
    }

    private void listarAnios(String criterio) {
        try {
            List<AnioLectivo> anios = anioLectivoDao.listaAnioLectivos(criterio);
            DefaultTableModel modelo = vista.getModeloTabla();
            modelo.setRowCount(0);

            for (AnioLectivo anio : anios) {
                modelo.addRow(new Object[]{
                    anio.getIdAnio(),
                    anio.getAnio(),
                    anio.getFechaInicio(),
                    anio.getFechaFin(),
                    anio.getEstado()
                });
            }

            limpiarSeleccion();
            if (anios.isEmpty() && !criterio.isEmpty()) {
                vista.mostrarMensaje("No se encontraron años lectivos.");
            }
        } catch (SQLException e) {
            vista.mostrarError("No fue posible cargar los años lectivos: " + e.getMessage());
        }
    }

    private void guardarAnio() {
        AnioLectivo anio = leerFormulario();
        if (anio == null) {
            return;
        }

        if (anioLectivoDao.registrarAnioLectivo(anio)) {
            vista.mostrarMensaje("Año lectivo registrado correctamente.");
            limpiarFormulario();
            listarAnios(vista.getTxtBuscarAnio().getText().trim());
        } else {
            vista.mostrarError("No se pudo registrar el año lectivo.");
        }
    }

    private void actualizarAnio() {
        if (!hayAnioSeleccionado()) {
            return;
        }

        AnioLectivo anio = leerFormulario();
        if (anio == null) {
            return;
        }
        anio.setIdAnio(idAnioSeleccionado);

        if (anioLectivoDao.actualizaAnioLectivo(anio)) {
            vista.mostrarMensaje("Año lectivo actualizado correctamente.");
            limpiarFormulario();
            listarAnios(vista.getTxtBuscarAnio().getText().trim());
        } else {
            vista.mostrarError("No se pudo actualizar el año lectivo.");
        }
    }

    private void eliminarAnio() {
        if (!hayAnioSeleccionado()) {
            return;
        }
        if (!vista.confirmar("¿Estás seguro de que deseas eliminar este año lectivo?")) {
            return;
        }

        if (anioLectivoDao.eliminarAnioLectivo(idAnioSeleccionado)) {
            vista.mostrarMensaje("Año lectivo eliminado correctamente.");
            limpiarFormulario();
            listarAnios(vista.getTxtBuscarAnio().getText().trim());
        } else {
            vista.mostrarError("No se pudo eliminar el año lectivo.");
        }
    }

    private void cambiarEstado() {
        if (!hayAnioSeleccionado()) {
            return;
        }

        int filaVista = vista.getTabla().getSelectedRow();
        int filaModelo = vista.getTabla().convertRowIndexToModel(filaVista);
        String estadoActual = String.valueOf(
                vista.getModeloTabla().getValueAt(filaModelo, 4));
        String estadoFormulario = String.valueOf(vista.getCmbEstado().getSelectedItem());
        String nuevoEstado = estadoFormulario.equalsIgnoreCase(estadoActual)
                ? ("Activo".equalsIgnoreCase(estadoActual) ? "Inactivo" : "Activo")
                : estadoFormulario;

        if (!vista.confirmar("¿Deseas cambiar el estado a " + nuevoEstado + "?")) {
            return;
        }

        if (anioLectivoDao.cambiarEstadoAnio(idAnioSeleccionado, nuevoEstado)) {
            vista.mostrarMensaje("El año lectivo ahora está " + nuevoEstado.toLowerCase() + ".");
            limpiarFormulario();
            listarAnios(vista.getTxtBuscarAnio().getText().trim());
        } else {
            vista.mostrarError("No se pudo cambiar el estado del año lectivo.");
        }
    }

    private void buscarAnios() {
        listarAnios(vista.getTxtBuscarAnio().getText().trim());
    }

    private void mostrarTodos() {
        vista.getTxtBuscarAnio().setText("");
        listarAnios("");
    }

    private void seleccionarAnio() {
        int filaVista = vista.getTabla().getSelectedRow();
        if (filaVista < 0) {
            limpiarSeleccion();
            return;
        }

        int filaModelo = vista.getTabla().convertRowIndexToModel(filaVista);
        DefaultTableModel modelo = vista.getModeloTabla();
        idAnioSeleccionado = ((Number) modelo.getValueAt(filaModelo, 0)).intValue();

        vista.getTxtAnio().setText(String.valueOf(modelo.getValueAt(filaModelo, 1)));
        vista.getDateChooserInicio().setDate(toDate((LocalDate) modelo.getValueAt(filaModelo, 2)));
        vista.getDateChooserFin().setDate(toDate((LocalDate) modelo.getValueAt(filaModelo, 3)));
        vista.getCmbEstado().setSelectedItem(modelo.getValueAt(filaModelo, 4));

        vista.getBtnGuardarAnio().setEnabled(false);
        vista.getBtnActualizarAnio().setEnabled(true);
        vista.getBtnEliminarAnio().setEnabled(true);
        vista.getBtnCambiarEstadoAnio().setEnabled(true);
    }

    private AnioLectivo leerFormulario() {
        String textoAnio = vista.getTxtAnio().getText().trim();
        Date fechaInicio = vista.getDateChooserInicio().getDate();
        Date fechaFin = vista.getDateChooserFin().getDate();

        if (textoAnio.isEmpty() || fechaInicio == null || fechaFin == null) {
            vista.mostrarError("Completa el año y ambas fechas.");
            return null;
        }

        final int anio;
        try {
            anio = Integer.parseInt(textoAnio);
        } catch (NumberFormatException e) {
            vista.mostrarError("El año debe ser un número entero.");
            return null;
        }

        if (anio < 1900 || anio > 9999) {
            vista.mostrarError("Ingresa un año válido entre 1900 y 9999.");
            return null;
        }

        LocalDate inicio = toLocalDate(fechaInicio);
        LocalDate fin = toLocalDate(fechaFin);
        if (fin.isBefore(inicio)) {
            vista.mostrarError("La fecha de fin no puede ser anterior a la fecha de inicio.");
            return null;
        }

        Object estadoSeleccionado = vista.getCmbEstado().getSelectedItem();
        if (estadoSeleccionado == null) {
            vista.mostrarError("Selecciona un estado para el año lectivo.");
            return null;
        }

        AnioLectivo anioLectivo = new AnioLectivo();
        anioLectivo.setAnio(anio);
        anioLectivo.setFechaInicio(inicio);
        anioLectivo.setFechaFin(fin);
        anioLectivo.setEstado(estadoSeleccionado.toString());
        return anioLectivo;
    }

    private boolean hayAnioSeleccionado() {
        if (idAnioSeleccionado != -1) {
            return true;
        }
        vista.mostrarError("Selecciona un año lectivo de la tabla.");
        return false;
    }

    private void limpiarFormulario() {
        vista.getTxtAnio().setText("");
        vista.getDateChooserInicio().setDate(null);
        vista.getDateChooserFin().setDate(null);
        vista.getCmbEstado().setSelectedIndex(0);
        vista.getTabla().clearSelection();
        limpiarSeleccion();
    }

    private void limpiarSeleccion() {
        idAnioSeleccionado = -1;
        vista.getBtnGuardarAnio().setEnabled(true);
        vista.getBtnActualizarAnio().setEnabled(false);
        vista.getBtnEliminarAnio().setEnabled(false);
        vista.getBtnCambiarEstadoAnio().setEnabled(false);
    }

    private LocalDate toLocalDate(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date toDate(LocalDate fecha) {
        return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
