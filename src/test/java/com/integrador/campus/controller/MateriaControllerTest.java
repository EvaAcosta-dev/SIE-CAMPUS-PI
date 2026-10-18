package com.integrador.campus.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SingleSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel; // Asegúrate de importar el tipo correcto de tu modelo

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.integrador.campus.dao.MateriaDao;
import com.integrador.campus.model.Materia;
import com.integrador.campus.view.Modulo.MateriaFormPanel;

@ExtendWith(MockitoExtension.class)
class MateriaControllerTest { // 🛠️ CORREGIDO: Se removió el genérico <selectionModel>

    @Mock
    private MateriaFormPanel vista;

    @Mock
    private MateriaDao materiaDao;

    @Mock
    private JButton btnGuardar;

    @Mock
    private JButton btnActualizar;

    @Mock
    private JButton btnEliminar;

    @Mock
    private JButton btnLimpiar;

    @Mock
    private JButton btnBuscar;

    @Mock
    private JButton btnMostrar;

    @Mock
    private JTable tabla;

    @Mock
    private javax.swing.ListSelectionModel selectionModel;
    
    @Mock
    private DefaultTableModel modeloTabla; // 🛠️ AGREGADO: Mock para el modelo de la tabla

    private MateriaController controller;

    @BeforeEach
    void setUp() throws SQLException {
        // Stub de botones para evitar NPE al registrar listeners en el constructor
        when(vista.getBtnGuardarCurso()).thenReturn(btnGuardar);
        when(vista.getBtnActualizarCurso()).thenReturn(btnActualizar);
        when(vista.getBtnEliminarCurso()).thenReturn(btnEliminar);
        when(vista.getBtnLimpiarCurso()).thenReturn(btnLimpiar);
        when(vista.getBtnBuscarCurso()).thenReturn(btnBuscar);
        when(vista.getBtnMostrarCursos()).thenReturn(btnMostrar);
        when(vista.getTabla()).thenReturn(tabla);
        
        // 🛠️ CORREGIDO: Se pasa el mock 'selectionModel' en minúscula, no la clase de Spring
        when(tabla.getSelectionModel()).thenReturn(selectionModel); 
        
        // listarTodasLasMaterias se ejecuta en el constructor
        when(materiaDao.listaMaterias("")).thenReturn(new ArrayList<>());

        controller = new MateriaController(vista, new Materia(), materiaDao);
    }

    @Test
    @DisplayName("Guardar materia valida campos y delega al DAO cuando todo es correcto")
    void guardarMateriaExitoso() throws SQLException {
        when(vista.getNombreCurso()).thenReturn("Matemáticas");
        when(materiaDao.registrarMateria(any(Materia.class))).thenReturn(true);

        ActionEvent evt = new ActionEvent(btnGuardar, ActionEvent.ACTION_PERFORMED, "guardar");
        controller.actionPerformed(evt);

        verify(materiaDao).registrarMateria(any(Materia.class));
        verify(vista).limpiarFormulario();
    }

    @Test
    @DisplayName("Guardar con nombre vacía muestra error y no llama al DAO")
    void guardarNombreVacioNoLlamaDao() throws SQLException {
        when(vista.getNombreCurso()).thenReturn("");

        ActionEvent evt = new ActionEvent(btnGuardar, ActionEvent.ACTION_PERFORMED, "guardar");
        controller.actionPerformed(evt);

        verify(materiaDao, never()).registrarMateria(any(Materia.class));
        verify(vista).mostrarError(anyString());
    }

    @Test
    @DisplayName("Buscar delega al DAO y llena la tabla con resultados")
    void buscarMateriaLlenaTabla() throws SQLException {
        when(vista.getTextoBusqueda()).thenReturn("Mat");
        List<Materia> resultado = new ArrayList<>();
        resultado.add(new Materia(1, "Matemáticas"));
        when(materiaDao.listaMaterias("Mat")).thenReturn(resultado);

        ActionEvent evt = new ActionEvent(btnBuscar, ActionEvent.ACTION_PERFORMED, "buscar");
        controller.actionPerformed(evt);

        verify(materiaDao).listaMaterias("Mat");
        verify(vista).llenarTablaCursos(resultado);
    }

    @Test
    @DisplayName("Eliminar obtiene id de la tabla y llama al DAO cuando se confirma")
    void eliminarMateriaLlamaDao() throws SQLException {
        // 🛠️ CORREGIDO: Estructuración correcta para simular llamadas en cascada de la tabla
        when(tabla.getSelectedRow()).thenReturn(0);
        when(vista.getModeloTabla()).thenReturn(modeloTabla);
        when(modeloTabla.getValueAt(0, 0)).thenReturn(1);
        
        when(vista.confirmar(anyString())).thenReturn(true);
        when(materiaDao.eliminarMateria(1)).thenReturn(true);

        ActionEvent evt = new ActionEvent(btnEliminar, ActionEvent.ACTION_PERFORMED, "eliminar");
        controller.actionPerformed(evt);

        verify(materiaDao).eliminarMateria(1);
    }
}