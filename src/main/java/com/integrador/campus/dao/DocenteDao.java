package com.integrador.campus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Docente;

public class DocenteDao {
    //comboBox 
    public List<Docente> listarDocentes() {
    List<Docente> lista = new ArrayList<>();

    String sql = "SELECT id_docente, nombre, apellido "
               + "FROM docente ORDER BY nombre, apellido";

    try (Connection con = BDConexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Docente d = new Docente();
            d.setIdDocente(rs.getInt("id_docente"));
            d.setNombre(rs.getString("nombre"));
            d.setApellido(rs.getString("apellido"));
            lista.add(d);
        }

    } catch (SQLException e) {
        System.err.println("Error al listar docentes: " + e.getMessage());
    }
    return lista;
}
}
