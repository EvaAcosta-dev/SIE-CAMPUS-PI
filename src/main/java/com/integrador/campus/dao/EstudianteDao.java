package com.integrador.campus.dao;

import com.integrador.campus.Util.EncriptarPass;
import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDao {

    private static final String SELECT_BASE =
        "SELECT e.id_estudiante, e.id_usuario, e.nombre, e.apellido, "
      + "e.num_documento, e.fecha_nacimiento, e.telefono, u.Correo, u.activo "
      + "FROM estudiante e "
      + "LEFT JOIN usuario u ON u.id_usuario = e.id_usuario ";

    // Lista todos, o filtra si 'texto' no está vacío
    public List<Estudiante> listar(String texto) throws SQLException {
        boolean filtrar = texto != null && !texto.trim().isEmpty();

        String sql = SELECT_BASE;
        if (filtrar) {
            sql += "WHERE e.nombre LIKE ? OR e.apellido LIKE ? "
                 + "OR e.num_documento LIKE ? OR u.Correo LIKE ? ";
        }
        sql += "ORDER BY e.apellido, e.nombre";

        List<Estudiante> lista = new ArrayList<>();

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
                    Estudiante e = new Estudiante();
                    e.setIdEstudiante(rs.getInt("id_estudiante"));
                    e.setIdUsuario(rs.getInt("id_usuario")); // NULL -> 0
                    e.setNombre(rs.getString("nombre"));
                    e.setApellido(rs.getString("apellido"));
                    e.setNumDocumento(rs.getString("num_documento"));
                    e.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
                    e.setTelefono(rs.getString("telefono"));
                    e.setCorreo(rs.getString("Correo"));
                    e.setActivo(rs.getBoolean("activo"));
                    lista.add(e);
                }
            }
        }
        return lista;
    }

    // Crea el usuario (rol ESTUDIANTE) y el estudiante en una sola transacción
    public void registrar(Estudiante est) throws SQLException {
        try (Connection con = BDConexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                int idRol = obtenerIdRol(con, "ESTUDIANTE");
                int idUsuario;

                String sqlUsuario = "INSERT INTO usuario "
                    + "(Nombre, Apellido, Correo, Contrasena, id_rol, activo) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(
                        sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, est.getNombre());
                    ps.setString(2, est.getApellido());
                    ps.setString(3, est.getCorreo());
                    ps.setString(4, EncriptarPass.encriptarContrasena(est.getContrasena()));
                    ps.setInt(5, idRol);
                    ps.setBoolean(6, est.isActivo());
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        idUsuario = keys.getInt(1);
                    }
                }

                String sqlEst = "INSERT INTO estudiante "
                    + "(id_usuario, nombre, apellido, num_documento, fecha_nacimiento, telefono) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sqlEst)) {
                    ps.setInt(1, idUsuario);
                    ps.setString(2, est.getNombre());
                    ps.setString(3, est.getApellido());
                    ps.setString(4, est.getNumDocumento());
                    ps.setDate(5, est.getFechaNacimiento());
                    ps.setString(6, est.getTelefono());
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    // Actualiza estudiante y su usuario. Si la contraseña viene vacía, se conserva.
    public void actualizar(Estudiante est) throws SQLException {
        try (Connection con = BDConexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                if (est.getIdUsuario() > 0) {
                    boolean cambiaClave = est.getContrasena() != null
                            && !est.getContrasena().trim().isEmpty();

                    String sqlU = "UPDATE usuario SET Nombre = ?, Apellido = ?, Correo = ?"
                        + (cambiaClave ? ", Contrasena = ?" : "")
                        + " WHERE id_usuario = ?";

                    try (PreparedStatement ps = con.prepareStatement(sqlU)) {
                        int i = 1;
                        ps.setString(i++, est.getNombre());
                        ps.setString(i++, est.getApellido());
                        ps.setString(i++, est.getCorreo());
                        if (cambiaClave) {
                            ps.setString(i++, EncriptarPass.encriptarContrasena(est.getContrasena().trim()));
                        }
                        ps.setInt(i, est.getIdUsuario());
                        ps.executeUpdate();
                    }
                }

                String sqlE = "UPDATE estudiante SET nombre = ?, apellido = ?, "
                    + "num_documento = ?, fecha_nacimiento = ?, telefono = ? "
                    + "WHERE id_estudiante = ?";

                try (PreparedStatement ps = con.prepareStatement(sqlE)) {
                    ps.setString(1, est.getNombre());
                    ps.setString(2, est.getApellido());
                    ps.setString(3, est.getNumDocumento());
                    ps.setDate(4, est.getFechaNacimiento());
                    ps.setString(5, est.getTelefono());
                    ps.setInt(6, est.getIdEstudiante());
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    // Borra el estudiante y luego su usuario (si tiene matrículas, MySQL lo impide)
    public void eliminar(Estudiante est) throws SQLException {
        try (Connection con = BDConexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM estudiante WHERE id_estudiante = ?")) {
                    ps.setInt(1, est.getIdEstudiante());
                    ps.executeUpdate();
                }

                if (est.getIdUsuario() > 0) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "DELETE FROM usuario WHERE id_usuario = ?")) {
                        ps.setInt(1, est.getIdUsuario());
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