package com.integrador.campus.controller;

import com.integrador.campus.dao.MateriaDao;
import com.integrador.campus.model.Materia;
import com.integrador.campus.view.Modulo.MateriaFormPanel;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MateriaController implements ActionListener {
    private final MateriaFormPanel vista;
    private final MateriaDao materiaDao;
    private final Materia materia;

    private final List<Materia> materiasActuales = new ArrayList<>();

    public MateriaController(MateriaFormPanel vista, Materia materia, MateriaDao materiaDao) {
        this.vista = vista;
        this.materia = materia;
        this.materiaDao = materiaDao;

        listarTodasLasMaterias();

        // Asociar eventos a los botones de la vista de Materias
        this.vista.getBtnGuardarCurso().addActionListener(this);       // Botón Guardar
        this.vista.getBtnActualizarCurso().addActionListener(this);   // Botón Actualizar
        this.vista.getBtnEliminarCurso().addActionListener(this);     // Botón Eliminar
        this.vista.getBtnLimpiarCurso().addActionListener(this);      // Botón Limpiar
        this.vista.getBtnBuscarCurso().addActionListener(this);       // Botón Buscar
        this.vista.getBtnMostrarCursos().addActionListener(this);     // Botón Mostrar Todos

        // Listener para seleccionar una fila de la tabla y cargarla en el formulario
        this.vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarMateriaSeleccionada();
            }
        });
    }

    private void listarTodasLasMaterias() {
        try {
            // Usamos "" para que devuelva todas las materias ordenadas
            List<Materia> lista = materiaDao.listaMaterias("");
            materiasActuales.clear();
            materiasActuales.addAll(lista);
            vista.llenarTablaCursos(lista);
        } catch (SQLException e) {
            vista.mostrarError("Error al listar materias en la tabla: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Botón Guardar
        if (e.getSource() == vista.getBtnGuardarCurso()) {
            if (validarCampos()) {
                Materia nuevaMateria = capturarDatosDeVista();
                if (materiaDao.registrarMateria(nuevaMateria)) {
                    JOptionPane.showMessageDialog(vista, "Materia registrada con éxito");
                    limpiarFormulario();
                    listarTodasLasMaterias();
                } else {
                    vista.mostrarError("No se pudo registrar la materia.");
                }
            }
        }

        // Botón Actualizar
        if (e.getSource() == vista.getBtnActualizarCurso()) {
            if (validarCampos()) {
                Materia materiaModificada = capturarDatosDeVista();
                if (materiaDao.actualizarMateria(materiaModificada)) {
                    JOptionPane.showMessageDialog(vista, "Materia actualizada con éxito");
                    limpiarFormulario();
                    listarTodasLasMaterias();
                } else {
                    vista.mostrarError("No se pudo actualizar la materia.");
                }
            }
        }

        // Botón Buscar
        if (e.getSource() == vista.getBtnBuscarCurso()) {
            try {
                String textoBusqueda = vista.getTextoBusqueda();
                List<Materia> filtrados = materiaDao.listaMaterias(textoBusqueda);
                if (filtrados.isEmpty()) {
                    // Mensaje  cuando no hay coincidencias
                    vista.mostrarError("El criterio de búsqueda no existe. Intente nuevamente.");
                   //volver a mostrar todos para limpiar la vista
                    listarTodasLasMaterias();
                } else {
                    materiasActuales.clear();
                    materiasActuales.addAll(filtrados);
                    vista.llenarTablaCursos(filtrados);
                }
            } catch (SQLException ex) {
                vista.mostrarError("Error en la búsqueda: " + ex.getMessage());
            }
        }
        

        // Botón Eliminar
        if (e.getSource() == vista.getBtnEliminarCurso()) {
            int fila = vista.getTabla().getSelectedRow();
            if (fila >= 0) {
                int idMateria = (int) vista.getModeloTabla().getValueAt(fila, 0);
                if (vista.confirmar("¿Está seguro de eliminar esta materia?")) {
                    if (materiaDao.eliminarMateria(idMateria)) {
                        JOptionPane.showMessageDialog(vista, "Materia eliminada con éxito");
                        limpiarFormulario();
                        listarTodasLasMaterias();
                    } else {
                        vista.mostrarError("No se pudo eliminar la materia.");
                    }
                }
            } else {
                vista.mostrarError("Seleccione una materia de la tabla para eliminar.");
            }
        }

        // Botón Limpiar
        if (e.getSource() == vista.getBtnLimpiarCurso()) {
            limpiarFormulario();
        }

        // Botón Mostrar Todos
        if (e.getSource() == vista.getBtnMostrarCursos()) {
            vista.getTxtBuscar().setText("");
            listarTodasLasMaterias();
        }
    }

    private Materia capturarDatosDeVista() {
        Materia m = new Materia();
        m.setIdMateria(vista.getIdCursoSeleccionado()); // Usando el método auxiliar de la vista
        m.setNombreMateria(vista.getNombreCurso().trim());
        return m;
    }

    private boolean validarCampos() {
        if (vista.getNombreCurso().isEmpty()) {
            vista.mostrarError("El nombre de la materia es obligatorio.");
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        vista.limpiarFormulario();
    }

    private void cargarMateriaSeleccionada() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila >= 0) {
            int idMateria = (int) vista.getModeloTabla().getValueAt(fila, 0);

            Materia materiaSeleccionada = null;
            for (Materia m : materiasActuales) {
                if (m.getIdMateria() == idMateria) {
                    materiaSeleccionada = m;
                    break;
                }
            }

            if (materiaSeleccionada != null) {
                vista.setIdCursoSeleccionado(materiaSeleccionada.getIdMateria());
                vista.txtNombreMateria.setText(materiaSeleccionada.getNombreMateria());

                // Control de estados de los botones
                vista.getBtnActualizarCurso().setEnabled(true);
                vista.getBtnEliminarCurso().setEnabled(true);
                vista.getBtnGuardarCurso().setEnabled(false);
            }
        }
    }
    
}
