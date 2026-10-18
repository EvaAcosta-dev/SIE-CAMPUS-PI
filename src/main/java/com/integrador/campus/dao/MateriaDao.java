package com.integrador.campus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Materia;
import com.integrador.campus.model.Nivel;

public class MateriaDao {
    //registrar materia
    public boolean registrarMateria(Materia materia){
        String sql= "INSERT INTO materia (nombre_materia)VALUES (?)";
         try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, materia.getNombreMateria());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar materia: " + e.getMessage());
            return false; // Retornar false si ocurre un error
        }
        return true; // Retornar true si el registro fue exitoso, false en caso contrario
    }
    //metodos para listar los materias- comboBox
    public List<Materia> listarMaterias() {
        List<Materia> materias = new ArrayList<>();
        String sql = "SELECT id_materia, nombre_materia FROM materia ORDER BY nombre_materia ASC;";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Materia materia = new Materia();
                materia.setIdMateria(rs.getInt("id_materia"));
                materia.setNombreMateria(rs.getString("nombre_materia"));
                materias.add(materia);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar niveles: " + e.getMessage());
        }
        return materias;
    }
    //metodo para actualizar materia
    public boolean actualizarMateria(Materia materia) {   
        String sql = "UPDATE materia SET nombre_materia=? WHERE id_materia=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, materia.getNombreMateria());
            ps.setInt(2, materia.getIdMateria());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar materia: " + e.getMessage());
            return false;
        }
    } 
    //metodo para eliminar los materia
    public boolean eliminarMateria(int idMateria) {
        String sql = "DELETE FROM materia WHERE id_materia=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMateria);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar materia: " + e.getMessage());
            return false;
        }
    }
    //metodo para buscar un materia por nombre
    public List<Materia> listaMaterias(String value) throws SQLException {
    List<Materia> lista = new ArrayList<>();
        String sql = "SELECT id_materia, nombre_materia FROM materia WHERE nombre_materia LIKE ? "
        + "ORDER BY nombre_materia";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String buscar = "%" + value + "%";
            ps.setString(1, buscar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Materia materia = new Materia();
                    materia.setIdMateria(rs.getInt("id_materia"));
                    materia.setNombreMateria(rs.getString("nombre_materia"));
                    lista.add(materia);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar materia : " + e.getMessage());
        }
        return lista; // Retornar la lista de materia encontrados
    }
}

  
