package com.integrador.campus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Nivel;

public class NivelDao {
public boolean registrarNivel(Nivel nivel) {
    
        // Implementar la lógica para registrar un nivel en la base de datos
        String sql = "INSERT INTO nivel (nombre_nivel) VALUES (?)";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            //ps.setInt(1, nivel.getIdNivel());
            ps.setString(1, nivel.getNombreNivel());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar nivel: " + e.getMessage());
            return false; // Retornar false si ocurre un error
        }
        return true; // Retornar true si el registro fue exitoso, false en caso contrario
    }
    //metodos para listar los niveles- comboBox
    public List<Nivel> listarNiveles() {
        List<Nivel> niveles = new ArrayList<>();
        String sql = "SELECT id_nivel, nombre_nivel FROM nivel ORDER BY nombre_nivel ASC;";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Nivel nivel = new Nivel();
                nivel.setIdNivel(rs.getInt("id_nivel"));
                nivel.setNombreNivel(rs.getString("nombre_nivel"));
                niveles.add(nivel);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar niveles: " + e.getMessage());
        }
        return niveles;
    }
    //metodo para actualizar los niveles
    public boolean actualizarNivel(Nivel nivel) {   
        String sql = "UPDATE nivel SET nombre_nivel=? WHERE id_nivel=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nivel.getNombreNivel());
            ps.setInt(2, nivel.getIdNivel());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar nivel: " + e.getMessage());
            return false;
        }
    } 
    //metodo para eliminar los niveles
    public boolean eliminarNivel(int idNivel) {
        String sql = "DELETE FROM nivel WHERE id_nivel=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idNivel);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar nivel: " + e.getMessage());
            return false;
        }
    }
    //metodo para buscar un nivel por nombre
    public List<Nivel> listaNiveles(String value) throws SQLException {
    List<Nivel> lista = new ArrayList<>();
        String sql = "SELECT id_nivel, nombre_nivel FROM nivel WHERE nombre_nivel LIKE ? "
        + "ORDER BY nombre_nivel";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String buscar = "%" + value + "%";
            ps.setString(1, buscar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Nivel nivel = new Nivel();
                    nivel.setIdNivel(rs.getInt("id_nivel"));
                    nivel.setNombreNivel(rs.getString("nombre_nivel"));
                    lista.add(nivel);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar nivel : " + e.getMessage());
        }
        return lista; // Retornar la lista de niveles encontrados
    }
}
