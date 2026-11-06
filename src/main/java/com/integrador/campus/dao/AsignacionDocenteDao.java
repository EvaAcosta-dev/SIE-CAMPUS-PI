package com.integrador.campus.dao;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.AsignacionDocente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class AsignacionDocenteDao {

    public boolean registrarAsignacion(AsignacionDocente a) {
        String sql = "INSERT INTO asignacion_docente (id_docente, id_materia, id_curso, anio_lectivo, estado) VALUES (?, ?, ?, ?, ?)";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getIdDocente());
            ps.setInt(2, a.getIdMateria());
            ps.setInt(3, a.getIdCurso());
            ps.setInt(4, a.getAnioLectivo());
            ps.setString(5, a.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar asignación: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarAsignacion(AsignacionDocente a) {
        String sql = "UPDATE asignacion_docente SET id_docente=?, id_materia=?, id_curso=?, anio_lectivo=?, estado=? WHERE id_asignacion = ?";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getIdDocente());
            ps.setInt(2, a.getIdMateria());
            ps.setInt(3, a.getIdCurso());
            ps.setInt(4, a.getAnioLectivo());
            ps.setString(5, a.getEstado());
            ps.setInt(6, a.getIdAsignacion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar asignación: " + e.getMessage());
            return false;
        }
    }

    public List<AsignacionDocente> listarAsignaciones(int idDocente, int anioLectivo) {
        List<AsignacionDocente> lista = new ArrayList<>();
        String sql = "SELECT a.id_asignacion, a.id_docente, a.id_materia, a.id_curso, a.anio_lectivo, a.estado, a.fecha_asignacion, "
                   + "m.nombre_materia AS nombre_materia, "
                   + "co.nombre_curso AS nombre_curso "
                   + "FROM asignacion_docente a "
                   + "INNER JOIN materia m ON a.id_materia = m.id_materia "
                   + "INNER JOIN curso co ON a.id_curso = co.id_curso "
                   + (idDocente > 0 ? "WHERE a.id_docente = ? " : "")
                   + (anioLectivo > 0 ? (idDocente > 0 ? "AND " : "WHERE ") + "a.anio_lectivo = ? " : "")
                   + "ORDER BY co.nombre_curso, m.nombre_materia";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int idx = 1;
            if (idDocente > 0) ps.setInt(idx++, idDocente);
            if (anioLectivo > 0) ps.setInt(idx++, anioLectivo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AsignacionDocente a = new AsignacionDocente();
                    a.setIdAsignacion(rs.getInt("id_asignacion"));
                    a.setIdDocente(rs.getInt("id_docente"));
                    a.setIdMateria(rs.getInt("id_materia"));
                    a.setIdCurso(rs.getInt("id_curso"));
                    a.setAnioLectivo(rs.getInt("anio_lectivo"));
                    a.setEstado(rs.getString("estado"));
                    a.setFechaAsignacion(rs.getTimestamp("fecha_asignacion"));
                    lista.add(a);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar asignaciones: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarAsignacion(int idAsignacion) {
        String sql = "DELETE FROM asignacion_docente WHERE id_asignacion = ?";
        try(Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAsignacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar asignación: " + e.getMessage());
            return false;
        }
    }
}
