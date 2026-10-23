package com.integrador.campus.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.integrador.campus.config.BDConexion;
import com.integrador.campus.dao.AnioLectivoDao;
import com.integrador.campus.dao.PeriodoDao;
import com.integrador.campus.model.AnioLectivo;
import com.integrador.campus.model.Materia;
import com.integrador.campus.model.Nivel;
import com.integrador.campus.model.Periodo;
import com.integrador.campus.view.Modulo.PeriodoFormPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PeriodoController {

    private final PeriodoFormPanel vista;
    private final PeriodoDao periodoDao;
    private final AnioLectivoDao anioDao; 
    private int idPeriodoSeleccionado = -1;

    public PeriodoController(PeriodoFormPanel vista, PeriodoDao periodoDao, AnioLectivoDao anioDao) {
        this.vista = vista;
        this.periodoDao = periodoDao;
        this.anioDao = anioDao;

        configurarFiltroCombo();
        
        initListeners();
        
        cargarComboAnioLectivo();
        listarPeriodosEnTabla();
    }

    private void configurarFiltroCombo() {
        
    }

    private void initListeners() {
        vista.btnGuardarPeriodo.addActionListener(e -> guardarPeriodo());
        vista.btnActualizarPeriodo.addActionListener(e -> actualizarPeriodo());
        vista.btnEliminarPeriodo.addActionListener(e -> eliminarPeriodo());
        vista.btnLimpiarPeriodo.addActionListener(e -> limpiarFormulario());
        vista.btnBuscarPeriodo.addActionListener(e -> buscarPeriodos());
        vista.btnMostrarPeriodos.addActionListener(e -> listarPeriodosEnTabla());

        vista.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFilaTabla();
            }
        });
    }

    private void cargarComboAnioLectivo() {
        vista.comboBoxAnioLectivo.removeAllItems();
        
        List<AnioLectivo> listaAnios = anioDao.listarAnioLectivos();
        for (AnioLectivo anio : listaAnios) {
            vista.comboBoxAnioLectivo.addItem(anio);
        }
    }

    private void listarPeriodosEnTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tabla.getModel();
        modelo.setRowCount(0);

        List<Periodo> lista = periodoDao.listarPeriodos();
        for (Periodo periodo : lista) {
            modelo.addRow(new Object[]{
                periodo.getIdPeriodo(),
                periodo.getNombrePeriodo(),
                periodo.getAnio(),
                periodo.getOrden(),
                periodo.getFecha_inicio(),
                periodo.getFecha_fin(),
                periodo.isActivo() ? "Sí" : "No"
            });
        }
        limpiarFormulario();
    }

    private void guardarPeriodo() {
        Periodo periodo = construirPeriodoDesdeFormulario();
        if (periodo == null) return; 

        if (periodoDao.registrarPeriodo(periodo)) {
            JOptionPane.showMessageDialog(vista, "Periodo guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            listarPeriodosEnTabla();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar. Verifique restricciones de orden único por año.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarPeriodo() {
        if (idPeriodoSeleccionado == -1) return;

        Periodo periodo = construirPeriodoDesdeFormulario();
        if (periodo == null) return;
        periodo.setIdPeriodo(idPeriodoSeleccionado);

        if (periodoDao.actualizarPeriodo(periodo)) {
            JOptionPane.showMessageDialog(vista, "Periodo actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            listarPeriodosEnTabla();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al actualizar el periodo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPeriodo() {
        if (idPeriodoSeleccionado == -1) return;

        int confirmar = JOptionPane.showConfirmDialog(vista, "¿Eliminar este periodo?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            if (periodoDao.eliminarPeriodo(idPeriodoSeleccionado)) {
                JOptionPane.showMessageDialog(vista, "Periodo eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                listarPeriodosEnTabla();
            }
        }
    }

    private void buscarPeriodos() {
        String nombreFiltro = vista.txtBuscar.getText().trim();
        String anioTexto = vista.txtBuscarAnio.getText().trim();
        int anioFiltro = 0;
        if (!anioTexto.isEmpty()) {
            try {
                anioFiltro = Integer.parseInt(anioTexto);
                if (anioFiltro < 1) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "Ingrese un año válido.", "Año inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        int ordenFiltro = (int) vista.spinnerBuscarOrden.getValue();
        DefaultTableModel modelo = (DefaultTableModel) vista.tabla.getModel();
        modelo.setRowCount(0);

        List<Periodo> lista = periodoDao.buscarPeriodos(nombreFiltro, ordenFiltro, anioFiltro);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(
                vista,
                "No hay periodos que coincidan con los filtros indicados.",
                "Sin resultados",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        for (Periodo periodo : lista) {
            modelo.addRow(new Object[]{
                periodo.getIdPeriodo(),
                periodo.getNombrePeriodo(),
                periodo.getAnio(),
                periodo.getOrden(),
                periodo.getFecha_inicio(),
                periodo.getFecha_fin(),
                periodo.isActivo() ? "Sí" : "No"
            });
        }
    }

    private LocalDate aLocalDate(Date date) {
        return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    private Date aDate(LocalDate date) {
        return date != null ? Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }

    private void seleccionarFilaTabla() {
        int fila = vista.tabla.getSelectedRow();
        if (fila >= 0) {
            int filaModelo = vista.tabla.convertRowIndexToModel(fila);
            DefaultTableModel modelo = (DefaultTableModel) vista.tabla.getModel();
            
            idPeriodoSeleccionado = (int) modelo.getValueAt(filaModelo, 0);
            vista.txtNombrePeriodo.setText((String) modelo.getValueAt(filaModelo, 1));
            
            // Recorrer combo buscando coincidencia por el año mostrado
            int anioTabla = (int) modelo.getValueAt(filaModelo, 2);
            seleccionarAnioEnComboPorAnio(anioTabla);
            
            vista.spinnerOrden.setValue(modelo.getValueAt(filaModelo, 3));
            vista.dateChooserInicio.setDate(aDate((LocalDate) modelo.getValueAt(filaModelo, 4)));
            vista.dateChooserFin.setDate(aDate((LocalDate) modelo.getValueAt(filaModelo, 5)));
            
            String activoStr = (String) modelo.getValueAt(filaModelo, 6);
            vista.chkActivo.setSelected("Sí".equals(activoStr));

            vista.btnGuardarPeriodo.setEnabled(false);
            vista.btnActualizarPeriodo.setEnabled(true);
            vista.btnEliminarPeriodo.setEnabled(true);
        }
    }

    private Periodo construirPeriodoDesdeFormulario() {
        Object itemSeleccionado = vista.comboBoxAnioLectivo.getSelectedItem();
        
        if (vista.txtNombrePeriodo.getText().trim().isEmpty() || itemSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "El Nombre del periodo y el Año Lectivo son obligatorios.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        AnioLectivo anioLectivo = (AnioLectivo) itemSeleccionado;

        LocalDate inicio = aLocalDate(vista.dateChooserInicio.getDate());
        LocalDate fin = aLocalDate(vista.dateChooserFin.getDate());
        if (inicio == null || fin == null) {
            JOptionPane.showMessageDialog(vista, "Las fechas de inicio y fin son obligatorias.", "Fechas requeridas", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (fin.isBefore(inicio)) {
            JOptionPane.showMessageDialog(vista, "La fecha de finalización no puede ser anterior a la fecha de inicio.", "Error Cronológico", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Periodo periodo = new Periodo();
        periodo.setNombrePeriodo(vista.txtNombrePeriodo.getText().trim());
        periodo.setIdAnio(anioLectivo.getIdAnio());
        periodo.setOrden((int) vista.spinnerOrden.getValue());
        periodo.setFecha_inicio(inicio);
        periodo.setFecha_fin(fin);
        periodo.setActivo(vista.chkActivo.isSelected());

        return periodo;
    }

    private void seleccionarAnioEnComboPorAnio(int anio) {
        for (int i = 0; i < vista.comboBoxAnioLectivo.getItemCount(); i++) {
            Object item = vista.comboBoxAnioLectivo.getItemAt(i);
            if (item instanceof AnioLectivo && ((AnioLectivo) item).getAnio() == anio) {
                vista.comboBoxAnioLectivo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarFormulario() {
        idPeriodoSeleccionado = -1;
        vista.txtNombrePeriodo.setText("");
        if (vista.comboBoxAnioLectivo.getItemCount() > 0) vista.comboBoxAnioLectivo.setSelectedIndex(0);
        vista.spinnerOrden.setValue(1);
        vista.dateChooserInicio.setDate(null);
        vista.dateChooserFin.setDate(null);
        vista.chkActivo.setSelected(false);
        vista.txtBuscar.setText("");
        vista.txtBuscarAnio.setText("");
        vista.spinnerBuscarOrden.setValue(0);

        vista.btnGuardarPeriodo.setEnabled(true);
        vista.btnActualizarPeriodo.setEnabled(false);
        vista.btnEliminarPeriodo.setEnabled(false);
        vista.tabla.clearSelection();
    }
}
