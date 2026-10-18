package com.integrador.campus.dao;

import java.sql.Connection;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Grado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class GradoDao {
    //metodo para registrar un grado
    public boolean registrarGrado(Grado grado) {
        String sql = "INSERT INTO grado (numero_grado, nombre_visible,id_nivel) VALUES (?,?,?)";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            //ps.setInt(1, grado.getIdGrado());
            ps.setInt(1, grado.getNumeroGrado());
            ps.setString(2, grado.getNombreVisible());
            ps.setInt(4, grado.getIdNivel());
            ps.executeUpdate();
            return true; // Retornar true si el registro fue exitoso
        } catch (SQLException e) {
            System.err.println("Error al registrar grado: " + e.getMessage());
            return false; // Retornar false si ocurre un error
        }
    }
    //metodo para actualizar un grado
    public boolean actualizarGrado(Grado grado) {
        String sql = "UPDATE grado SET numero_grado=?, nombre_visible=?, id_nivel=? WHERE id_grado=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, grado.getNumeroGrado());
            ps.setString(2, grado.getNombreVisible());
            ps.setInt(3, grado.getIdNivel());
            ps.setInt(4, grado.getIdGrado());
            ps.executeUpdate();
            return true; // Retornar true si la actualización fue exitosa
        } catch (SQLException e) {
            System.err.println("Error al actualizar grado: " + e.getMessage());
            return false; // Retornar false si ocurre un error
        }
    }
    //metodo para eliminar un grado
    public boolean eliminarGrado(int idGrado) { 
        
        String sql = "DELETE FROM grado WHERE id_grado=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idGrado);
            ps.executeUpdate();
            return true; // Retornar true si la eliminación fue exitosa
        } catch (SQLException e) {
            System.err.println("Error al eliminar grado: " + e.getMessage());
            return false; // Retornar false si ocurre un error
        }
    }
    //metodo para buscar un grado 
    public List<Grado> listaGrados() {
    List<Grado> grados = new ArrayList<>();
    String sql=" SELECT g.id_grado,g.nombre_visible,g.numero_grado,n.nombre_nivel "
        + " FROM grado g "
        +" INNER JOIN nivel n ON g.id_nivel=n.id_nivel "
        +" WHERE g.numero_grado LIKE ?"
        +" OR g.nombre_visible LIKE ? "
        +" OR n.nombre_nivel LIKE ? ";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Grado grado = new Grado();
                grado.setIdGrado(rs.getInt("id_grado"));
                grado.setNombreVisible(rs.getString("nombre_visible"));
                grado.setNumeroGrado(rs.getShort("numero_grado"));
                grado.setIdNivel(rs.getInt("id_nivel"));
                grados.add(grado);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar grados: " + e.getMessage());
        }
        return grados;

    }
    //metodo para listar los grados
    public ArrayList<Grado> listarGrados() {
        ArrayList<Grado> grados = new ArrayList<>();
        String sql = "SELECT * FROM grado";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Grado grado = new Grado();
                grado.setIdGrado(rs.getInt("id_grado"));
                grado.setNumeroGrado(rs.getShort("numero_grado"));
                grado.setNombreVisible(rs.getString("nombre_visible"));
                grado.setIdNivel(rs.getInt("id_nivel"));
                grados.add(grado);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar grados: " + e.getMessage());
        }
        return grados;
    }

}
