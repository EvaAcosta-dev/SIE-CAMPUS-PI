package com.integrador.campus.controller;

import com.integrador.campus.dao.AnioLectivoDao;
import com.integrador.campus.dao.CursoDao;
import com.integrador.campus.dao.DocenteDao;
import com.integrador.campus.dao.JornadaDao;
import com.integrador.campus.model.AnioLectivo;
import com.integrador.campus.model.Cursos;
import com.integrador.campus.model.Docente;
import com.integrador.campus.model.Jornada;
import com.integrador.campus.view.Modulo.CursoFormPanel;

import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoController implements ActionListener {

    private final CursoFormPanel vista;
    private final JornadaDao jornadaDao;
    private final DocenteDao docenteDao;
    private final AnioLectivoDao anioDao;
    private final CursoDao cursoDao;
    private final Cursos cursos;

    private final List<Cursos> cursosActuales = new ArrayList<>();

   public CursoController(CursoDao cursoDao, Cursos cursos, CursoFormPanel vista) {
        this.vista = vista;
        this.cursoDao = cursoDao;

        this.jornadaDao = new JornadaDao();
        this.docenteDao = new DocenteDao();
        this.anioDao = new AnioLectivoDao();

        cargarCombos();
        listarTodosLosCursos();

        this.vista.btnGuardarCurso.addActionListener(this);
        this.vista.btnActualizarCurso.addActionListener(this);
        this.vista.btnEliminarCurso.addActionListener(this);
        this.vista.btnLimpiarCurso.addActionListener(this);
        this.vista.btnBuscarCurso.addActionListener(this);
        this.vista.btnMostrarCursos.addActionListener(this);

        this.vista.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarCursoSeleccionado();
            }
        });
        this.cursos = new Cursos();
    }

    private void cargarCombos() {
        try {
            vista.cargarJornadas(jornadaDao.listarJornadas());
            vista.cargarDocentes(docenteDao.listarDocentes());
            vista.cargarAnios(anioDao.listaAnioLectivos(""));
        } catch (Exception e) {
            vista.mostrarError("Error al cargar los combos: " + e.getMessage());
        }
    }

    private void listarTodosLosCursos() {
        try {
            List<Cursos> lista = cursoDao.listaCursos("");
            cursosActuales.clear();
            cursosActuales.addAll(lista);
            vista.llenarTablaCursos(lista);
        } catch (SQLException e) {
            vista.mostrarError("Error al listar cursos en la tabla: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnGuardarCurso) {
            if (validarCampos()) {
                Cursos nuevoCurso = capturarDatosDeVista();
                if (cursoDao.registrarCursosQuery(nuevoCurso)) {
                    JOptionPane.showMessageDialog(vista, "Curso registrado con éxito");
                    limpiarFormulario();
                    listarTodosLosCursos();
                }
            }
        }

        if (e.getSource() == vista.btnActualizarCurso) {
            if (validarCampos()) {
                Cursos cursoModificado = capturarDatosDeVista();
                if (cursoDao.actualizarCursosQuery(cursoModificado)) {
                    JOptionPane.showMessageDialog(vista, "Curso actualizado con éxito");
                    limpiarFormulario();
                    listarTodosLosCursos();
                }
            }
        }

        if (e.getSource() == vista.btnBuscarCurso) {
            try {
                String textoBusqueda = vista.txtBuscar.getText().trim();
                List<Cursos> filtrados = cursoDao.listaCursos(textoBusqueda);
                cursosActuales.clear();
                cursosActuales.addAll(filtrados);
                vista.llenarTablaCursos(filtrados);
            } catch (SQLException ex) {
                vista.mostrarError("Error en la búsqueda: " + ex.getMessage());
            }
        }

        if (e.getSource() == vista.btnEliminarCurso) {
            int fila = vista.getTabla().getSelectedRow();
            if (fila >= 0) {
                int idCurso = (int) vista.getModeloTabla().getValueAt(fila, 0);
                if (vista.confirmar("¿Está seguro de eliminar este curso?")) {
                    if (cursoDao.eliminarCurso(idCurso)) {
                        JOptionPane.showMessageDialog(vista, "Curso eliminado con éxito");
                        limpiarFormulario();
                        listarTodosLosCursos();
                    }
                }
            } else {
                vista.mostrarError("Seleccione un curso de la tabla para eliminar.");
            }
        }

        if (e.getSource() == vista.btnLimpiarCurso) {
            limpiarFormulario();
        }

        if (e.getSource() == vista.btnMostrarCursos) {
            listarTodosLosCursos();
        }
    }

    private Cursos capturarDatosDeVista() {
        Cursos c = new Cursos();
        c.setIdCurso(vista.getIdCursoSeleccionado());
        c.setNombreCurso(vista.txtNombreCurso.getText().trim());

        Jornada j = (Jornada) vista.comboBoxJornada.getSelectedItem();
        Docente d = (Docente) vista.comboBoxDocenteDirector.getSelectedItem();
        AnioLectivo a = (AnioLectivo) vista.comboBoxAnioLectivo.getSelectedItem();

        c.setIdJornada(j.getIdJornada());
        c.setIdAnio(a.getIdAnio());
        c.setIdDocenteDirector(d != null ? d.getIdDocente() : null);

        c.setIdGrado(Integer.parseInt(vista.txtGrado.getText()));

        return c;
    }

    private boolean validarCampos() {
        if (vista.txtNombreCurso.getText().trim().isEmpty()) {
            vista.mostrarError("El nombre del curso es obligatorio.");
            return false;
        }
        if (vista.txtGrado.getText().trim().isEmpty()) {
            vista.mostrarError("El grado es obligatorio.");
            return false;
        }
        if (vista.comboBoxJornada.getSelectedItem() == null || vista.comboBoxAnioLectivo.getSelectedItem() == null) {
            vista.mostrarError("Debe seleccionar una Jornada y un Año Lectivo.");
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        vista.limpiarFormulario();
    }

    private void cargarCursoSeleccionado() {
        int fila = vista.getTabla().getSelectedRow();
        if (fila >= 0) {
            int idCurso = (int) vista.getModeloTabla().getValueAt(fila, 0);

            Cursos cursoSeleccionado = null;
            for (Cursos c : cursosActuales) {
                if (c.getIdCurso() == idCurso) {
                    cursoSeleccionado = c;
                    break;
                }
            }

            if (cursoSeleccionado != null) {
                vista.setIdCursoSeleccionado(cursoSeleccionado.getIdCurso());
                vista.txtNombreCurso.setText(cursoSeleccionado.getNombreCurso());
                vista.txtGrado.setText(String.valueOf(cursoSeleccionado.getIdGrado()));

                String nombreJornada = cursoSeleccionado.getNombreJornada();
                for (int i = 0; i < vista.comboBoxJornada.getItemCount(); i++) {
                    Jornada j = vista.comboBoxJornada.getItemAt(i);
                    if (j != null && j.getNombreJornada().equals(nombreJornada)) {
                        vista.comboBoxJornada.setSelectedIndex(i);
                        break;
                    }
                }

                int idAnio = cursoSeleccionado.getIdAnio();
                for (int i = 0; i < vista.comboBoxAnioLectivo.getItemCount(); i++) {
                    AnioLectivo a = vista.comboBoxAnioLectivo.getItemAt(i);
                    if (a != null && a.getIdAnio() == idAnio) {
                        vista.comboBoxAnioLectivo.setSelectedIndex(i);
                        break;
                    }
                }

                String nombreDirector = cursoSeleccionado.getNombreDirector();
                if (nombreDirector != null) {
                    for (int i = 0; i < vista.comboBoxDocenteDirector.getItemCount(); i++) {
                        Docente d = vista.comboBoxDocenteDirector.getItemAt(i);
                        if (d != null && (d.getNombre() + " " + d.getApellido()).equals(nombreDirector)) {
                            vista.comboBoxDocenteDirector.setSelectedIndex(i);
                            break;
                        }
                    }
                }

                vista.btnActualizarCurso.setEnabled(true);
                vista.btnEliminarCurso.setEnabled(true);
                vista.btnGuardarCurso.setEnabled(false);
            }
        }
    }
}
