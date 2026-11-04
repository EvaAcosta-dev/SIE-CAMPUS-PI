package com.integrador.campus.dao;

import com.integrador.campus.config.BDConnection;
import com.integrador.campus.model.Matricula;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class MatriculaDao {

    public boolean registrarMatricula(Matricula matricula) {
        String sql = "INSERT INTO matricula (id_estudiante, id_curso, estado, observacion) VALUES (?, ?, ?, ?)";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, matricula.getIdEstudiante());
            ps.setInt(2, matricula.getIdCurso());
            ps.setString(3, matricula.getEstado());
            ps.setString(4, matricula.getObservacion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar matrícula: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarMatricula(Matricula matricula) {
        String sql = "UPDATE matricula SET id_estudiante=?, id_curso=?, estado=?, observacion=? WHERE id_matricula?";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, matricula.getIdEstudiante());
            ps.setInt(2, matricula.getIdCurso());
            ps.setString(3, matricula.getEstado());
            ps.setString(4, matricula.getObservacion());
            ps.setInt(5, matricula.getIdMatricula());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar matrícula: " + e.getMessage());
            return false;
        }
    }

    public List<Matricula> listarMatriculas() {
        List<Matricula> lista = new ArrayList<>();
        String sql = "SELECT m.id_matricula, m.id_estudiante, m.id_curso, m.estado, m.observacion, m.fecha_matricula, "
                   + "CONCAT(e.nombre, ' ', e.apellido) AS nombre_estudiante, "
                   + "c.nombre_curso AS nombre_curso "
                   + "FROM matricula m "
                   + "INNER JOIN estudiante e ON m.id_estudiante = e.id_estudiante "
                   + "INNER JOIN curso c ON m.id_curso = c.id_curso "
                   + "ORDER BY m.id_matricula DESC";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Matricula m = new Matricula();
                m.setIdMatricula(rs.getInt("id_matricula"));
                m.setIdEstudiante(rs.getInt("id_estudiante"));
                m.setIdCurso(rs.getInt("id_curso"));
                m.setEstado(rs.getString("estado"));
                m.setObservacion(rs.getString("observacion"));
                m.setFechaMatricula(rs.getTimestamp("fecha_matricula"));
                lista.add(m);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar matrículas: " + e.getMessage());
        }
        return lista;
    }

    public List<Matricula> buscarMatricula(String valor) {
        List<Matricula> lista = new ArrayList<>();
        String sql = "SELECT m.id_matricula, m.id_estudiante, m.id_curso, m.estado, m.observacion, m.fecha_matricula, "
                   + "CONCAT(e.nombre, ' ', e.apellido) AS nombre_estudiante, "
                   + "c.nombre_curso AS nombre_curso "
                   + "FROM matricula m "
                   + "INNER JOIN estudiante e ON m.id_estudiante = e.id_estudiante "
                   + "INNER JOIN curso c ON m.id_curso = c.id_curso "
                   + "WHERE CONCAT(e.nombre, ' ', e.apellido) LIKE ? OR c.nombre_curso LIKE ? OR m.estado LIKE ? "
                   + "ORDER BY m.id_matricula DESC";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String bus = "%" + valor + "%";
            ps.setString(1, bus);
            ps.setString(2, bus);
            ps.setString(3, bus);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Matricula m = new Matricula();
                    m.setIdMatricula(rs.getInt("id_matricula"));
                    m.setIdEstudiante(rs.getInt("id_estudiante"));
                    m.setIdCurso(rs.getInt("id_curso"));
                    m.setEstado(rs.getString("estado"));
                    m.setObservacion(rs.getString("observacion"));
                    m.setFechaMatricula(rs.getTimestamp("fecha_matricula"));
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar matrícula: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarMatricula(int idMatricula) {
        String sql = "DELETE FROM matricula WHERE id_matricula";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMatricula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar matrícula: " + e.getMessage());
            return false;
        }
    }
}