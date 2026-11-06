package com.integrador.campus.dao;

import com.integrador.campus.config.BDConnection;
import com.integrador.campus.model.Boletin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class BoletinDao {

    public boolean generarBoletin(Boletin b) {
        String sql = "INSERT INTO boletin (id_estudiante, id_curso, anio_lectivo, periodo, promedio_general, aprobadas, reprobadas, estado_boletin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, b.getIdEstudiante());
            ps.setInt(2, b.getIdCurso());
            ps.setInt(3, b.getAnioLectivo());
            ps.setString(4, b.getPeriodo());
            ps.setDouble(5, b.getPromedioGeneral());
            ps.setInt(6, b.getAprobadas());
            ps.setInt(7, b.getReprobadas());
            ps.setString(8, b.getEstadoBoletin());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al generar boletín: " + e.getMessage());
            return false;
        }
    }

    public List<Boletin> listarBoletines(int idEstudiante) {
        List<Boletin> lista = new ArrayList<>();
        String sql = "SELECT b.id_boletin, b.id_estudiante, b.id_curso, b.anio_lectivo, b.periodo, "
                   + "b.promedio_general, b.aprobadas, b.reprobadas, b.estado_boletin, b.fecha_generacion, "
                   + "co.nombre_curso AS nombre_curso "
                   + "FROM boletin b "
                   + "INNER JOIN curso co ON b.id_curso = co.id_curso "
                   + "WHERE b.id_estudiante = ? "
                   + "ORDER BY b.anio_lectivo DESC, b.periodo";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Boletin b = new Boletin();
                    b.setIdBoletin(rs.getInt("id_boletin"));
                    b.setIdEstudiante(rs.getInt("id_estudiante"));
                    b.setIdCurso(rs.getInt("id_curso"));
                    b.setAnioLectivo(rs.getInt("anio_lectivo"));
                    b.setPeriodo(rs.getString("periodo"));
                    b.setPromedioGeneral(rs.getDouble("promedio_general"));
                    b.setAprobadas(rs.getInt("aprobadas"));
                    b.setReprobadas(rs.getInt("reprobadas"));
                    b.setEstadoBoletin(rs.getString("estado_boletin"));
                    b.setFechaGeneracion(rs.getTimestamp("fecha_generacion"));
                    lista.add(b);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar boletines: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarEstadoBoletin(int idBoletin, String estado) {
        String sql = "UPDATE boletin SET estado_boletin = ? WHERE id_boletin = ?";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idBoletin);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar estado boletín: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarBoletin(int idBoletin) {
        String sql = "DELETE FROM boletin WHERE id_boletin = ?";
        try (Connection con = BDConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idBoletin);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar boletín: " + e.getMessage());
            return false;
        }
    }
}