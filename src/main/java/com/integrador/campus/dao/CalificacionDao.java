package com.integrador.campus.dao;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Calificacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class CalificacionDao {

    public boolean registrarCalificacion(Calificacion cal) {
        String sql = "INSERT INTO calificacion (id_estudiante, id_materia, id_curso, nota, periodo, anio_lectivo, observacion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cal.getIdEstudiante());
            ps.setInt(2, cal.getIdMateria());
            ps.setInt(3, cal.getIdCurso());
            ps.setDouble(4, cal.getNota());
            ps.setString(5, cal.getPeriodo());
            ps.setInt(6, cal.getAnioLectivo());
            ps.setString(7, cal.getObservacion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar calificación: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarCalificacion(Calificacion cal) {
        String sql = "UPDATE calificacion SET nota=?, periodo=?, observacion=? WHERE id_calificacion = ?";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, cal.getNota());
            ps.setString(2, cal.getPeriodo());
            ps.setString(3, cal.getObservacion());
            ps.setInt(4, cal.getIdCalificacion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar calificación: " + e.getMessage());
            return false;
        }
    }

    public List<Calificacion> listarCalificaciones(int idEstudiante, int anioLectivo) {
        List<Calificacion> lista = new ArrayList<>();
        String sql = "SELECT c.id_calificacion, c.id_estudiante, c.id_materia, c.id_curso, c.nota, "
                   + "c.periodo, c.anio_lectivo, c.observacion, c.fechaRegistro, "
                   + "m.nombre_materia AS nombre_materia, "
                   + "co.nombre_curso AS nombre_curso "
                   + "FROM calificacion c "
                   + "INNER JOIN materia m ON c.id_materia = m.id_materia "
                   + "INNER JOIN curso co ON c.id_curso = co.id_curso "
                   + "WHERE c.id_estudiante = ? AND c.anio_lectivo = ? "
                   + "ORDER BY m.nombre_materia, c.periodo";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            ps.setInt(2, anioLectivo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Calificacion c = new Calificacion();
                    c.setIdCalificacion(rs.getInt("id_calificacion"));
                    c.setIdEstudiante(rs.getInt("id_estudiante"));
                    c.setIdMateria(rs.getInt("id_materia"));
                    c.setIdCurso(rs.getInt("id_curso"));
                    c.setNota(rs.getDouble("nota"));
                    c.setPeriodo(rs.getString("periodo"));
                    c.setAnioLectivo(rs.getInt("anio_lectivo"));
                    c.setObservacion(rs.getString("observacion"));
                    c.setFechaRegistro(rs.getTimestamp("fechaRegistro"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar calificaciones: " + e.getMessage());
        }
        return lista;
    }

    public List<Calificacion> listarCalificacionesPorMateria(int idMateria, int idCurso, int anioLectivo) {
        List<Calificacion> lista = new ArrayList<>();
        String sql = "SELECT c.id_calificacion, c.id_estudiante, c.id_materia, c.id_curso, c.nota, "
                   + "c.periodo, c.anio_lectivo, c.observacion, c.fechaRegistro, "
                   + "CONCAT(e.nombre, ' ', e.apellido) AS nombre_estudiante "
                   + "FROM calificacion c "
                   + "INNER JOIN estudiante e ON c.id_estudiante = e.id_estudiante "
                   + "WHERE c.id_materia = ? AND c.id_curso = ? AND c.anio_lectivo = ? "
                   + "ORDER BY e.apellido, e.nombre";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMateria);
            ps.setInt(2, idCurso);
            ps.setInt(3, anioLectivo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Calificacion c = new Calificacion();
                    c.setIdCalificacion(rs.getInt("id_calificacion"));
                    c.setIdEstudiante(rs.getInt("id_estudiante"));
                    c.setIdMateria(rs.getInt("id_materia"));
                    c.setIdCurso(rs.getInt("id_curso"));
                    c.setNota(rs.getDouble("nota"));
                    c.setPeriodo(rs.getString("periodo"));
                    c.setAnioLectivo(rs.getInt("anio_lectivo"));
                    c.setObservacion(rs.getString("observacion"));
                    c.setFechaRegistro(rs.getTimestamp("fechaRegistro"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar calificaciones por materia: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarCalificacion(int idCalificacion) {
        String sql = "DELETE FROM calificacion WHERE id_calificacion = ?";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCalificacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar calificación: " + e.getMessage());
            return false;
        }
    }
}
