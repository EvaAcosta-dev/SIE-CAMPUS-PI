package com.integrador.campus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.AnioLectivo;
import com.integrador.campus.model.Cursos;
import com.integrador.campus.model.Nivel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AnioLectivoDao {
    
    // Registrar año escolar
    public boolean registrarAnioLectivo(AnioLectivo anioLectivo){
        String sql ="INSERT INTO anio_lectivo (id_anio,anio,fecha_inicio,fecha_fin,estado) VALUES (?,?,?,?,?)";
        try(Connection con = BDConexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){  
            ps.setInt(1,anioLectivo.getIdAnio());
            ps.setInt(2,anioLectivo.getAnio());
            ps.setDate(3, java.sql.Date.valueOf(anioLectivo.getFechaInicio()));
            ps.setDate(4,java.sql.Date.valueOf(anioLectivo.getFechaFin()));
            ps.setString(5, anioLectivo.getEstado());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar año lectivo: " + e);
            return false;
        }
    }
    
    // Método actualizar Año lectivo
    public boolean actualizaAnioLectivo(AnioLectivo anioLectivo){
        String sql= "UPDATE anio_lectivo SET anio=?,fecha_inicio=?,fecha_fin=?,estado=? WHERE id_anio=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setInt(1, anioLectivo.getAnio());
            ps.setDate(2, java.sql.Date.valueOf(anioLectivo.getFechaInicio()));
            ps.setDate(3, java.sql.Date.valueOf(anioLectivo.getFechaFin()));
            ps.setString(4, anioLectivo.getEstado());
            ps.setInt(5, anioLectivo.getIdAnio()); 
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar año lectivo: " + e.getMessage());
            return false;
        }
    }
    
    // Eliminar año
    public boolean eliminarAnioLectivo (int idAnio){
        String sql= "DELETE FROM anio_lectivo WHERE id_anio=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAnio);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar año lectivo: " + e);
            return false;
        }
    }
    
    // Lista buscar año escolar 
    public List<AnioLectivo> listaAnioLectivos(String value) throws SQLException {
        List<AnioLectivo> lista = new ArrayList<>(); 
        String sql = "SELECT id_anio, anio, fecha_inicio, fecha_fin, estado "
                   + "FROM anio_lectivo "
                   + "WHERE CAST(anio AS CHAR) LIKE ? "
                   + "OR CAST(fecha_inicio AS CHAR) LIKE ? "
                   + "OR CAST(fecha_fin AS CHAR) LIKE ? "
                   + "OR estado LIKE ? "
                   + "ORDER BY anio DESC"; 
                   
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String buscar = "%" + value + "%";
            ps.setString(1, buscar);
            ps.setString(2, buscar);
            ps.setString(3, buscar);
            ps.setString(4, buscar);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AnioLectivo al = new AnioLectivo();
                    al.setIdAnio(rs.getInt("id_anio"));
                    al.setAnio(rs.getInt("anio"));
                    al.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                    al.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                    al.setEstado(rs.getString("estado"));
                    
                    lista.add(al);
                }
            }
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, "Error al buscar año lectivo: " + e.getMessage());
            throw e; 
        }
        
        return lista;
    }
    public boolean cambiarEstadoAnio(
            int idAnio, String estado) {

        String sql =
            "UPDATE anio_lectivo SET estado = ? "
            + "WHERE id_estado = ?";

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idAnio);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                "Error al cambiar estado: " + e.getMessage()
            );
            return false;
        }
    }//comboBox
 public List<AnioLectivo> listarAnioLectivos() {
    List<AnioLectivo> anioLectivos = new ArrayList<>();

    String sql = "SELECT id_anio, anio "
               + "FROM anio_lectivo "
               + "ORDER BY anio ASC";

    try (Connection con = BDConexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            AnioLectivo anioLectivo = new AnioLectivo();

            anioLectivo.setIdAnio(rs.getInt("id_anio"));
            anioLectivo.setAnio(rs.getInt("anio"));

            anioLectivos.add(anioLectivo);
        }

    } catch (SQLException e) {
        System.err.println(
            "Error al listar años lectivos: " + e.getMessage()
        );
    }

    return anioLectivos;
}
}

