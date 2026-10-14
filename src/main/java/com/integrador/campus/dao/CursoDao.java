package com.integrador.campus.dao;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.AnioLectivo;
import com.integrador.campus.model.Cursos;
import com.integrador.campus.model.Docente;
import com.integrador.campus.model.Jornada;
import com.integrador.campus.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


public class CursoDao {
    //metodo para el registro de los cursos
     public boolean registrarCursosQuery(Cursos c) {
        String sql = "INSERT INTO curso(nombre_Curso, id_grado, id_anio, id_docente_director, id_jornada) VALUES (?,?,?,?,?)";
     try(Connection con = BDConexion.getConexion();
        PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, c.getNombreCurso());
            ps.setInt(2, c.getIdGrado());
            ps.setInt(3, c.getIdAnio());
            if (c.getIdDocenteDirector() != null) {
                ps.setInt(4, c.getIdDocenteDirector());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setInt(5, c.getIdJornada());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error registrar curso: " + e);
            return false;
        }
    }
//metodo para actualizar los cursos
    public boolean actualizarCursosQuery(Cursos c) {
        String sql = "UPDATE curso SET nombre_Curso=?, id_grado=?, id_anio=?, id_docente_director=?, id_jornada=? WHERE id_curso=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombreCurso());
            ps.setInt(2, c.getIdGrado());
            ps.setInt(3, c.getIdAnio());
            if (c.getIdDocenteDirector() != null) {
                ps.setInt(4, c.getIdDocenteDirector());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setInt(5, c.getIdJornada());
            ps.setInt(6, c.getIdCurso());
            ps.executeUpdate(); // Ejecutar UPDATE
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar curso: " + e);
            return false;
        }
    }
    //metodo para listar los cursos en tabla
    public List<Cursos> listarCursos() {
        List<Cursos> cursos = new ArrayList<>();
        String sql = "SELECT c.id_curso, c.nombre_Curso, c.id_grado, c.id_anio, "
                   + "c.id_docente_director, c.id_jornada, "
                   + "g.nombre_visible AS nombre_grado, j.nombre_jornada, al.anio AS anio_lectivo "
                   + "FROM curso c "
                   + "LEFT JOIN grado g ON c.id_grado = g.id_grado "
                   + "LEFT JOIN jornada j ON c.id_jornada = j.id_jornada "
                   + "LEFT JOIN anio_lectivo al ON c.id_anio = al.id_anio";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cursos c = new Cursos();
                c.setIdCurso(rs.getInt("id_curso"));
                c.setNombreCurso(rs.getString("nombre_Curso"));
                c.setIdGrado(rs.getInt("id_grado"));
                c.setIdAnio(rs.getInt("id_anio"));
                if (rs.getObject("id_docente_director") != null) {
                    c.setIdDocenteDirector(rs.getInt("id_docente_director"));
                }
                c.setIdJornada(rs.getInt("id_jornada"));
                c.setNombreGrado(rs.getString("nombre_grado"));
                c.setNombreJornada(rs.getString("nombre_jornada"));
                if (rs.getObject("anio_lectivo") != null) {
                    c.setAnio(rs.getInt("anio_lectivo"));
                }
                cursos.add(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar cursos: " + e);
        }
        return cursos;
    }
    //metodo para buscar cursos
   public List<Cursos> listaCursos(String value) throws SQLException {
    List<Cursos> lista = new ArrayList<>();
        String sql = "SELECT c.id_curso, c.nombre_curso, c.id_grado, g.nombre_visible AS nombre_grado, "
        + "j.nombre_jornada, c.id_anio, al.anio AS anio_lectivo, "
        + "CONCAT(d.nombre, ' ', d.apellido) AS nombre_docente "
        + "FROM curso c "
        + "INNER JOIN grado g ON c.id_grado = g.id_grado "
        + "INNER JOIN jornada j ON c.id_jornada = j.id_jornada "
        + "LEFT JOIN docente d ON c.id_docente_director = d.id_docente "
        + "INNER JOIN nivel n ON g.id_nivel = n.id_nivel "
        + "INNER JOIN anio_lectivo al ON c.id_anio = al.id_anio "
        + "WHERE n.nombre_nivel LIKE ? "
        + "OR c.nombre_curso LIKE ? "
        + "OR g.nombre_visible LIKE ? "
        + "OR CONCAT(d.nombre, ' ', d.apellido) LIKE ? "
        + "OR j.nombre_jornada LIKE ? "
        + "ORDER BY c.id_curso DESC";

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             String buscar = "%" + value + "%";

            ps.setString(1, buscar);
            ps.setString(2, buscar);
            ps.setString(3, buscar);
            ps.setString(4, buscar);
            ps.setString(5, buscar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cursos c = new Cursos();
                    c.setIdCurso(rs.getInt("id_curso"));
                    c.setNombreCurso(rs.getString("nombre_curso"));
                    c.setIdGrado(rs.getInt("id_grado"));
                    c.setNombreGrado(rs.getString("nombre_grado"));
                    c.setNombreJornada(rs.getString("nombre_jornada"));
                    c.setIdAnio(rs.getInt("id_anio"));
                    c.setAnio(rs.getInt("anio_lectivo"));
                    c.setNombreDirector(rs.getString("nombre_docente"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar curso: " + e);
        }
        return lista; // Retorna la lista de cursos encontrados
    }
    //metodo para eliminar cursos
    public boolean eliminarCurso(int idCurso) {
        String sql = "DELETE FROM curso WHERE id_curso=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCurso);
            int filasAfectadas = ps.executeUpdate(); // Ejecutar DELETE
            return filasAfectadas > 0; // Retorna true si se eliminó al menos una fila
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar curso: " + e);
            return false;
        }
    }
    
    
}
