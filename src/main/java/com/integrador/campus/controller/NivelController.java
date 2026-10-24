package com.integrador.campus.controller;

import com.integrador.campus.dao.NivelDao;
import com.integrador.campus.model.Nivel;
import com.integrador.campus.view.Modulo.NivelFormPanel;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class NivelController {

    private final NivelFormPanel vista;
    private final NivelDao nivelDao;

    public NivelController(NivelFormPanel vista, NivelDao nivelDao) {
        this.vista = vista;
        this.nivelDao = nivelDao;
        configurarEventos();
        listarTodosLosNiveles();
    }

    private void configurarEventos() {
        vista.getBtnGuardarNivel().addActionListener(e -> guardarNivel());
        vista.getBtnActualizarNivel().addActionListener(e -> actualizarNivel());
        vista.getBtnEliminarNivel().addActionListener(e -> eliminarNivel());
        vista.getBtnLimpiarNivel().addActionListener(e -> vista.limpiarFormulario());
        vista.getBtnBuscarNivel().addActionListener(e -> buscarNiveles());
        vista.getBtnMostrarNivel().addActionListener(e -> mostrarTodos());
        vista.getTxtBuscarNivel().addActionListener(e -> buscarNiveles());

        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarNivel();
            }
        });
    }

    private void listarTodosLosNiveles() {
        vista.limpiarFormulario();
        vista.llenarTablaNiveles(nivelDao.listarNiveles());
    }

    private void guardarNivel() {
        if (!validarCampos()) {
            return;
        }

        Nivel nuevoNivel = capturarDatosDeVista();
        if (nivelDao.registrarNivel(nuevoNivel)) {
            vista.mostrarMensaje("Nivel registrado correctamente.");
            vista.limpiarFormulario();
            listarTodosLosNiveles();
        } else {
            vista.mostrarError("No se pudo registrar el nivel.");
        }
    }

    private void actualizarNivel() {
        int idNivel = vista.getIdNivelSeleccionado();
        if (idNivel == -1) {
            vista.mostrarError("Selecciona un nivel de la tabla para actualizar.");
            return;
        }
        if (!validarCampos()) {
            return;
        }

        Nivel nivelActualizado = capturarDatosDeVista();
        nivelActualizado.setIdNivel(idNivel);
        if (nivelDao.actualizarNivel(nivelActualizado)) {
            vista.mostrarMensaje("Nivel actualizado correctamente.");
            vista.limpiarFormulario();
            listarTodosLosNiveles();
        } else {
            vista.mostrarError("No se pudo actualizar el nivel.");
        }
    }

    private void eliminarNivel() {
        int idNivel = vista.getIdNivelSeleccionado();
        if (idNivel == -1) {
            vista.mostrarError("Selecciona un nivel de la tabla para eliminar.");
            return;
        }
        if (!vista.confirmar("¿Está seguro de eliminar este nivel?")) {
            return;
        }

        if (nivelDao.eliminarNivel(idNivel)) {
            vista.mostrarMensaje("Nivel eliminado correctamente.");
            vista.limpiarFormulario();
            listarTodosLosNiveles();
        } else {
            vista.mostrarError("No se pudo eliminar el nivel.");
        }
    }

    private void buscarNiveles() {
        String criterio = vista.getTextoBusqueda();
        if (criterio.isEmpty()) {
            listarTodosLosNiveles();
            return;
        }

        try {
            List<Nivel> niveles = nivelDao.listaNiveles(criterio);
            vista.limpiarFormulario();
            vista.llenarTablaNiveles(niveles);
            if (niveles.isEmpty()) {
                vista.mostrarMensaje("No se encontraron niveles con ese nombre.");
            }
        } catch (SQLException e) {
            vista.mostrarError("Error al buscar niveles: " + e.getMessage());
        }
    }

    private void mostrarTodos() {
        vista.getTxtBuscarNivel().setText("");
        listarTodosLosNiveles();
    }

    private void seleccionarNivel() {
        int filaVista = vista.getTabla().getSelectedRow();
        if (filaVista < 0) {
            return;
        }

        int filaModelo = vista.getTabla().convertRowIndexToModel(filaVista);
        Object valorId = vista.getModeloTabla().getValueAt(filaModelo, 0);
        Object valorNombre = vista.getModeloTabla().getValueAt(filaModelo, 1);
        int idNivel = ((Number) valorId).intValue();

        vista.setIdNivelSeleccionado(idNivel);
        vista.txtNombreNivel.setText(String.valueOf(valorNombre));
        vista.getBtnGuardarNivel().setEnabled(false);
        vista.getBtnActualizarNivel().setEnabled(true);
        vista.getBtnEliminarNivel().setEnabled(true);
    }

    private Nivel capturarDatosDeVista() {
        Nivel nivel = new Nivel();
        nivel.setIdNivel(vista.getIdNivelSeleccionado());
        nivel.setNombreNivel(vista.getNombreNivel());
        return nivel;
    }

    private boolean validarCampos() {
        if (vista.getNombreNivel().isEmpty()) {
            vista.mostrarError("El nombre del nivel es obligatorio.");
            vista.txtNombreNivel.requestFocusInWindow();
            return false;
        }
        return true;
    }
}
