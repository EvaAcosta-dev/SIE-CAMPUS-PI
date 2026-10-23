package com.integrador.campus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.integrador.campus.Util.EncriptarPass;
import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Docente;

public class DocenteDao {

    // comboBox (lo usa el panel de Cursos, no se toca)
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

    // Lista completa, o filtrada si 'texto' no está vacío
    public List<Docente> listar(String texto) throws SQLException {
        boolean filtrar = texto != null && !texto.trim().isEmpty();

        String sql = "SELECT d.id_docente, d.id_usuario, d.nombre, d.apellido, "
                   + "d.documento, COALESCE(u.Correo, d.correo) AS correo "
                   + "FROM docente d "
                   + "LEFT JOIN usuario u ON u.id_usuario = d.id_usuario ";
        if (filtrar) {
            sql += "WHERE d.nombre LIKE ? OR d.apellido LIKE ? "
                 + "OR d.documento LIKE ? OR u.Correo LIKE ? ";
        }
        sql += "ORDER BY d.apellido, d.nombre";

        List<Docente> lista = new ArrayList<>();

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (filtrar) {
                String b = "%" + texto.trim() + "%";
                for (int i = 1; i <= 4; i++) {
                    ps.setString(i, b);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Docente d = new Docente();
                    d.setIdDocente(rs.getInt("id_docente"));
                    d.setIdUsuario(rs.getInt("id_usuario")); // NULL -> 0
                    d.setNombre(rs.getString("nombre"));
                    d.setApellido(rs.getString("apellido"));
                    d.setDocumento(rs.getString("documento"));
                    d.setCorreo(rs.getString("correo"));
                    lista.add(d);
                }
            }
        }
        return lista;
    }

    // Crea el usuario (rol DOCENTE) y el docente en una transacción
    public void registrar(Docente doc) throws SQLException {
        try (Connection con = BDConexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                int idRol = obtenerIdRol(con, "DOCENTE");
                int idUsuario;

                String sqlUsuario = "INSERT INTO usuario "
                    + "(Nombre, Apellido, Correo, Contrasena, id_rol, activo) "
                    + "VALUES (?, ?, ?, ?, ?, 1)";

                try (PreparedStatement ps = con.prepareStatement(
                        sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, doc.getNombre());
                    ps.setString(2, doc.getApellido());
                    ps.setString(3, doc.getCorreo());
                    ps.setString(4, EncriptarPass.encriptarContrasena(doc.getContrasena()));
                    ps.setInt(5, idRol);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        idUsuario = keys.getInt(1);
                    }
                }

                String sqlDoc = "INSERT INTO docente "
                    + "(id_usuario, nombre, apellido, documento, correo) "
                    + "VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sqlDoc)) {
                    ps.setInt(1, idUsuario);
                    ps.setString(2, doc.getNombre());
                    ps.setString(3, doc.getApellido());
                    ps.setString(4, doc.getDocumento());
                    ps.setString(5, doc.getCorreo());
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    // Actualiza docente y usuario. Si la clave viene vacía, se conserva.
    public void actualizar(Docente doc) throws SQLException {
        try (Connection con = BDConexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                if (doc.getIdUsuario() > 0) {
                    boolean cambiaClave = doc.getContrasena() != null
                            && !doc.getContrasena().trim().isEmpty();

                    String sqlU = "UPDATE usuario SET Nombre = ?, Apellido = ?, Correo = ?"
                        + (cambiaClave ? ", Contrasena = ?" : "")
                        + " WHERE id_usuario = ?";

                    try (PreparedStatement ps = con.prepareStatement(sqlU)) {
                        int i = 1;
                        ps.setString(i++, doc.getNombre());
                        ps.setString(i++, doc.getApellido());
                        ps.setString(i++, doc.getCorreo());
                        if (cambiaClave) {
                            ps.setString(i++, EncriptarPass.encriptarContrasena(doc.getContrasena().trim()));
                        }
                        ps.setInt(i, doc.getIdUsuario());
                        ps.executeUpdate();
                    }
                }

                String sqlD = "UPDATE docente SET nombre = ?, apellido = ?, "
                    + "documento = ?, correo = ? WHERE id_docente = ?";

                try (PreparedStatement ps = con.prepareStatement(sqlD)) {
                    ps.setString(1, doc.getNombre());
                    ps.setString(2, doc.getApellido());
                    ps.setString(3, doc.getDocumento());
                    ps.setString(4, doc.getCorreo());
                    ps.setInt(5, doc.getIdDocente());
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    // Borra el docente y luego su usuario.
    // Si tiene materias asignadas, MySQL lo impide y se hace rollback.
    public void eliminar(Docente doc) throws SQLException {
        try (Connection con = BDConexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM docente WHERE id_docente = ?")) {
                    ps.setInt(1, doc.getIdDocente());
                    ps.executeUpdate();
                }

                if (doc.getIdUsuario() > 0) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "DELETE FROM usuario WHERE id_usuario = ?")) {
                        ps.setInt(1, doc.getIdUsuario());
                        ps.executeUpdate();
                    }
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    private int obtenerIdRol(Connection con, String nombreRol) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT id_rol FROM rol WHERE nombre_rol = ?")) {
            ps.setString(1, nombreRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_rol");
                }
            }
        }
        throw new SQLException("No existe el rol " + nombreRol + " en la base de datos.");
    }
}