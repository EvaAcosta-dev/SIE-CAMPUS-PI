package com.integrador.campus.dao;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Grado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GradoDao {
//registrar grado
    public boolean registrarGrado(Grado grado) {
        String sql = "INSERT INTO grado (numero_grado, nombre_visible, id_nivel) VALUES (?, ?, ?)";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setShort(1, grado.getNumeroGrado());
            ps.setString(2, grado.getNombreVisible());
            ps.setInt(3, grado.getIdNivel());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar grado: " + e.getMessage());
            return false;
        }
    }
//actualizar grado
    public boolean actualizarGrado(Grado grado) {
        String sql = "UPDATE grado SET numero_grado = ?, nombre_visible = ?, id_nivel = ? "
                + "WHERE id_grado = ?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setShort(1, grado.getNumeroGrado());
            ps.setString(2, grado.getNombreVisible());
            ps.setInt(3, grado.getIdNivel());
            ps.setInt(4, grado.getIdGrado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar grado: " + e.getMessage());
            return false;
        }
    }
//eliminar grado
    public boolean eliminarGrado(int idGrado) {
        String sql = "DELETE FROM grado WHERE id_grado = ?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idGrado);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar grado: " + e.getMessage());
            return false;
        }
    }

    public List<Grado> listaGrados() {
        return listaGrados("");
    }
//listar grados para busqueda
    public List<Grado> listaGrados(String criterio) {
        List<Grado> grados = new ArrayList<>();
        String sql = "SELECT g.id_grado, g.numero_grado, g.nombre_visible, "
                + "g.id_nivel, n.nombre_nivel "
                + "FROM grado g INNER JOIN nivel n ON g.id_nivel = n.id_nivel "
                + "WHERE CAST(g.numero_grado AS CHAR) LIKE ? "
                + "OR g.nombre_visible LIKE ? OR n.nombre_nivel LIKE ? "
                + "ORDER BY g.numero_grado, g.nombre_visible";

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String buscar = "%" + criterio.trim() + "%";
            ps.setString(1, buscar);
            ps.setString(2, buscar);
            ps.setString(3, buscar);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Grado grado = new Grado();
                    grado.setIdGrado(rs.getInt("id_grado"));
                    grado.setNumeroGrado(rs.getShort("numero_grado"));
                    grado.setNombreVisible(rs.getString("nombre_visible"));
                    grado.setIdNivel(rs.getInt("id_nivel"));
                    grado.setNombreNivel(rs.getString("nombre_nivel"));
                    grados.add(grado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar grados: " + e.getMessage());
        }
        return grados;
    }

    public ArrayList<Grado> listarGrados() {
        return new ArrayList<>(listaGrados());
    }
}
