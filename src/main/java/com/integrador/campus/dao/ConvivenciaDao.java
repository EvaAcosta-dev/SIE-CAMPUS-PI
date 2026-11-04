package com.integrador.campus.dao;

import com.integrador.campus.config.BDConnection;
import com.integrador.campus.model.Convivencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ConvivenciaDao {

    public boolean registrarConvivencia(Convivencia c) {
        String sql = "INSERT INTO convivencia (id_estudiante, id_docente, fecha_reporte, tipo_falta, observacion, medida_disciplinaria, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getIdEstudiante());
            ps.setInt(2, c.getIdDocente());
            ps.setDate(3, c.getFechaReporte());
            ps.setString(4, c.getTipoFalta());
            ps.setString(5, c.getObservacion());
            ps.setString(6, c.getMedidaDisciplinaria());
            ps.setString(7, c.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar convivencia: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarConvivencia(Convivencia c) {
        String sql = "UPDATE convivencia SET tipo_falta=?, observacion=?, medida_disciplinaria=?, estado=? WHERE id_convivencia";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getTipoFalta());
            ps.setString(2, c.getObservacion());
            ps.setString(3, c.getMedidaDisciplinaria());
            ps.setString(4, c.getEstado());
            ps.setInt(5, c.getIdConvivencia());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar convivencia: " + e.getMessage());
            return false;
        }
    }

    public List<Convivencia> listarConvivencias(int idEstudiante) {
        List<Convivencia> lista = new ArrayList<>();
        String sql = "SELECT c.id_convivencia, c.id_estudiante, c.id_docente, c.fecha_reporte, c.tipo_falta, "
                   + "c.observacion, c.medida_disciplinaria, c.estado, c.fechaRegistro, "
                   + "CONCAT(d.nombre, ' ', d.apellido) AS nombre_docente "
                   + "FROM convivencia c "
                   + "INNER JOIN docente d ON c.id_docente = d.id_docente "
                   + "WHERE c.id_estudiante = ? "
                   + "ORDER BY c.fecha_reporte DESC";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Convivencia c = new Convivencia();
                    c.setIdConvivencia(rs.getInt("id_convivencia"));
                    c.setIdEstudiante(rs.getInt("id_estudiante"));
                    c.setIdDocente(rs.getInt("id_docente"));
                    c.setFechaReporte(rs.getDate("fecha_reporte"));
                    c.setTipoFalta(rs.getString("tipo_falta"));
                    c.setObservacion(rs.getString("observacion"));
                    c.setMedidaDisciplinaria(rs.getString("medida_disciplinaria"));
                    c.setEstado(rs.getString("estado"));
                    c.setFechaRegistro(rs.getTimestamp("fechaRegistro"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar convivencias: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarConvivencia(int idConvivencia) {
        String sql = "DELETE FROM convivencia WHERE id_convivencia";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idConvivencia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar convivencia: " + e.getMessage());
            return false;
        }
    }
}