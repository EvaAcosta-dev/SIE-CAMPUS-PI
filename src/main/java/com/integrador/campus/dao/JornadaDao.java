package com.integrador.campus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Jornada;

public class JornadaDao {
    //metodo para registrar una jornada
    public boolean registrarJornada(Jornada jornada) {
        String sql = "INSERT INTO jornada (nombre_jornada) VALUES (?)";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, jornada.getNombreJornada());
            ps.executeUpdate();
            return true; // Retornar true si el registro fue exitoso
        } catch (SQLException e) {
            System.err.println("Error al registrar jornada: " + e.getMessage());
            return false; // Retornar false si ocurre un error
        }
    }
    //metodo para actualizar una jornada
    public boolean actualizarJornada(Jornada jornada) {
        String sql = "UPDATE jornada SET nombre_jornada=? WHERE id_jornada=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, jornada.getNombreJornada());
            ps.setInt(2, jornada.getIdJornada());
            ps.executeUpdate();
            return true; // Retornar true si la actualización fue exitosa
        } catch (SQLException e) {
            System.err.println("Error al actualizar jornada: " + e.getMessage());
            return false; // Retornar false si ocurre un error
        }
    }
    //metodo para eliminar una jornada
    public boolean eliminarJornada(int idJornada) {
        String sql = "DELETE FROM jornada WHERE id_jornada=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idJornada);
            ps.executeUpdate();
            return true; // Retornar true si la eliminación fue exitosa
        } catch (SQLException e) {
            System.err.println("Error al eliminar jornada: " + e.getMessage());
            return false; // Retornar false si ocurre un error
        }
    }
    //metodo pra buscar una jornada por nombre
    public List<Jornada> buscarJornadaPorNombre(String value) {
        List<Jornada> jornadas = new ArrayList<>();
        String sql = "SELECT id_jornada, nombre_jornada FROM jornada WHERE nombre_jornada LIKE ?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + value + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Jornada jornada = new Jornada();
                    jornada.setIdJornada(rs.getInt("id_jornada"));
                    jornada.setNombreJornada(rs.getString("nombre_jornada"));
                    jornadas.add(jornada);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar jornada por nombre: " + e.getMessage());
        }
        return jornadas;
    }

    //metodo para listar las jornadas-utilizado para el comboBox
    public List<Jornada> listarJornadas() {
    List<Jornada> lista = new ArrayList<>();

    String sql = "SELECT id_jornada, nombre_jornada "
               + "FROM jornada ORDER BY nombre_jornada ASC";

    try (Connection con = BDConexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Jornada j = new Jornada();
            j.setIdJornada(rs.getInt("id_jornada"));
            j.setNombreJornada(rs.getString("nombre_jornada"));
            lista.add(j);
        }
    } catch (SQLException e) {
        System.err.println("Error al listar jornadas: " + e.getMessage());
    }
    return lista;
}
}
