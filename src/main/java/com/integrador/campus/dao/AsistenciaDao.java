package com.integrador.campus.dao;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Asistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class AsistenciaDao {

    public boolean registrarAsistencia(Asistencia a) {
        String sql = "INSERT INTO asistencia (id_estudiante, id_materia, id_curso, fecha_clase, estado, observacion) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getIdEstudiante());
            ps.setInt(2, a.getIdMateria());
            ps.setInt(3, a.getIdCurso());
            ps.setDate(4, a.getFechaClase());
            ps.setString(5, a.getEstado());
            ps.setString(6, a.getObservacion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar asistencia: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarAsistencia(Asistencia a) {
        String sql = "UPDATE asistencia SET estado=?, observacion=? WHERE id_asistencia = ?";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getEstado());
            ps.setString(2, a.getObservacion());
            ps.setInt(3, a.getIdAsistencia());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar asistencia: " + e.getMessage());
            return false;
        }
    }

    public List<Asistencia> listarAsistencias(int idEstudiante, int idMateria, int idCurso) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT a.id_asistencia, a.id_estudiante, a.id_materia, a.id_curso, a.fecha_clase, a.estado, a.observacion, a.fechaRegistro, "
                   + "m.nombre_materia AS nombre_materia "
                   + "FROM asistencia a "
                   + "INNER JOIN materia m ON a.id_materia = m.id_materia "
                   + "WHERE a.id_estudiante = ? AND a.id_materia = ? AND a.id_curso = ? "
                   + "ORDER BY a.fecha_clase DESC";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idMateria);
            ps.setInt(3, idCurso);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Asistencia a = new Asistencia();
                    a.setIdAsistencia(rs.getInt("id_asistencia"));
                    a.setIdEstudiante(rs.getInt("id_estudiante"));
                    a.setIdMateria(rs.getInt("id_materia"));
                    a.setIdCurso(rs.getInt("id_curso"));
                    a.setFechaClase(rs.getDate("fecha_clase"));
                    a.setEstado(rs.getString("estado"));
                    a.setObservacion(rs.getString("observacion"));
                    a.setFechaRegistro(rs.getTimestamp("fechaRegistro"));
                    lista.add(a);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar asistencias: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarAsistencia(int idAsistencia) {
        String sql = "DELETE FROM asistencia WHERE id_asistencia = ?";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAsistencia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar asistencia: " + e.getMessage());
            return false;
        }
    }
}
