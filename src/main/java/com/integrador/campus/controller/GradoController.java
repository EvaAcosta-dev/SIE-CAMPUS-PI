package com.integrador.campus.controller;

import com.integrador.campus.dao.GradoDao;
import com.integrador.campus.dao.NivelDao;
import com.integrador.campus.model.Grado;
import com.integrador.campus.model.Nivel;
import com.integrador.campus.view.Modulo.GradoFormPanel;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class GradoController {

    private final GradoFormPanel vista;
    private final GradoDao gradoDao;
    private final NivelDao nivelDao;
    private List<Grado> gradosMostrados;

    public GradoController(GradoFormPanel vista, GradoDao gradoDao, NivelDao nivelDao) {
        this.vista = vista;
        this.gradoDao = gradoDao;
        this.nivelDao = nivelDao;
        configurarEventos();
        cargarNiveles();
        listarGrados("");
    }

    private void configurarEventos() {
        vista.getBtnGuardar().addActionListener(e -> guardarGrado());
        vista.getBtnActualizar().addActionListener(e -> actualizarGrado());
        vista.getBtnEliminar().addActionListener(e -> eliminarGrado());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarFormulario());
        vista.getBtnBuscar().addActionListener(e -> buscarGrados());
        vista.getBtnMostrarTodos().addActionListener(e -> mostrarTodos());
        vista.getTxtBuscar().addActionListener(e -> buscarGrados());

        vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarGrado();
            }
        });
    }

    private void cargarNiveles() {
        List<Nivel> niveles = nivelDao.listarNiveles();
        vista.cargarNiveles(niveles);
        if (niveles.isEmpty()) {
            vista.mostrarError("No hay niveles registrados. Registra un nivel antes de crear grados.");
        }
    }

    private void listarGrados(String criterio) {
        gradosMostrados = gradoDao.listaGrados(criterio);
        vista.limpiarFormulario();
        vista.llenarTablaGrados(gradosMostrados);
    }

    private void guardarGrado() {
        Grado grado = leerFormulario();
        if (grado == null) {
            return;
        }

        if (gradoDao.registrarGrado(grado)) {
            vista.mostrarMensaje("Grado registrado correctamente.");
            vista.limpiarFormulario();
            listarGrados(vista.getTextoBusqueda());
        } else {
            vista.mostrarError("No se pudo registrar el grado. Revisa los datos y la conexión con la base de datos.");
        }
    }

    private void actualizarGrado() {
        int idGrado = vista.getIdGradoSeleccionado();
        if (idGrado == -1) {
            vista.mostrarError("Selecciona un grado de la tabla para actualizar.");
            return;
        }

        Grado grado = leerFormulario();
        if (grado == null) {
            return;
        }
        grado.setIdGrado(idGrado);

        if (gradoDao.actualizarGrado(grado)) {
            vista.mostrarMensaje("Grado actualizado correctamente.");
            vista.limpiarFormulario();
            listarGrados(vista.getTextoBusqueda());
        } else {
            vista.mostrarError("No se pudo actualizar el grado.");
        }
    }

    private void eliminarGrado() {
        int idGrado = vista.getIdGradoSeleccionado();
        if (idGrado == -1) {
            vista.mostrarError("Selecciona un grado de la tabla para eliminar.");
            return;
        }
        if (!vista.confirmar("¿Estás seguro de que deseas eliminar este grado?")) {
            return;
        }

        if (gradoDao.eliminarGrado(idGrado)) {
            vista.mostrarMensaje("Grado eliminado correctamente.");
            vista.limpiarFormulario();
            listarGrados(vista.getTextoBusqueda());
        } else {
            vista.mostrarError("No se pudo eliminar el grado. Puede que esté asociado a otros registros.");
        }
    }

    private void buscarGrados() {
        listarGrados(vista.getTextoBusqueda());
        if (gradosMostrados.isEmpty()) {
            vista.mostrarMensaje("No se encontraron grados con ese criterio.");
        }
    }

    private void mostrarTodos() {
        vista.getTxtBuscar().setText("");
        listarGrados("");
    }

    private void seleccionarGrado() {
        int filaVista = vista.getTabla().getSelectedRow();
        if (filaVista < 0) {
            return;
        }
        int filaModelo = vista.getTabla().convertRowIndexToModel(filaVista);
        DefaultTableModel modelo = vista.getModeloTabla();
        int idGrado = ((Number) modelo.getValueAt(filaModelo, 0)).intValue();

        for (Grado grado : gradosMostrados) {
            if (grado.getIdGrado() == idGrado) {
                vista.cargarGradoEnFormulario(grado);
                return;
            }
        }
        vista.mostrarError("No fue posible cargar los datos del grado seleccionado.");
    }

    private Grado leerFormulario() {
        final short numeroGrado;
        try {
            numeroGrado = Short.parseShort(vista.getNumeroGrado());
            if (numeroGrado < -3) {
                vista.mostrarError("El número de grado no puede ser negativo.");
                vista.getTxtNumeroGrado().requestFocusInWindow();
                return null;
            }
        } catch (NumberFormatException e) {
            vista.mostrarError("Ingresa un número de grado válido.");
            vista.getTxtNumeroGrado().requestFocusInWindow();
            return null;
        }

        String nombreVisible = vista.getNombreVisible();
        if (nombreVisible.isEmpty()) {
            vista.mostrarError("El nombre visible del grado es obligatorio.");
            vista.getTxtNombreVisible().requestFocusInWindow();
            return null;
        }

        Nivel nivel = vista.getNivelSeleccionado();
        if (nivel == null) {
            vista.mostrarError("Selecciona el nivel académico al que pertenece el grado.");
            return null;
        }

        Grado grado = new Grado();
        grado.setNumeroGrado(numeroGrado);
        grado.setNombreVisible(nombreVisible);
        grado.setIdNivel(nivel.getIdNivel());
        grado.setNombreNivel(nivel.getNombreNivel());
        return grado;
    }
}
