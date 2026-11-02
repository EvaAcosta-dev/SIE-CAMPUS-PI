package com.integrador.campus.dao;

import com.integrador.campus.Util.EncriptarPass;
import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Padre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PadreDao {

    // Lista todos, o filtra si 'texto' no está vacío
    public List<Padre> listar(String texto) throws SQLException {
        boolean filtrar = texto != null && !texto.trim().isEmpty();

        String sql = "SELECT p.id_padre, p.id_usuario, p.nombre, p.apellido, "
                   + "p.telefono, COALESCE(u.Correo, p.correo) AS correo "
                   + "FROM padre p "
                   + "LEFT JOIN usuario u ON u.id_usuario = p.id_usuario ";
        if (filtrar) {
            sql += "WHERE p.nombre LIKE ? OR p.apellido LIKE ? "
                 + "OR p.telefono LIKE ? OR u.Correo LIKE ? ";
        }
        sql += "ORDER BY p.apellido, p.nombre";

        List<Padre> lista = new ArrayList<>();

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
                    Padre p = new Padre();
                    p.setIdPadre(rs.getInt("id_padre"));
                    p.setIdUsuario(rs.getInt("id_usuario")); // NULL -> 0
                    p.setNombre(rs.getString("nombre"));
                    p.setApellido(rs.getString("apellido"));
                    p.setTelefono(rs.getString("telefono"));
                    p.setCorreo(rs.getString("correo"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }

        // Crea el usuario (rol PADRE), el acudiente y su primer vínculo
    // con un estudiante, todo en una sola transacción
    public void registrar(Padre padre, int idEstudiante, String parentesco) throws SQLException {
        try (Connection con = BDConexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                int idRol = obtenerIdRol(con, "PADRE");
                int idUsuario;
                int idPadre;

                String sqlUsuario = "INSERT INTO usuario "
                    + "(Nombre, Apellido, Correo, Contrasena, id_rol, activo) "
                    + "VALUES (?, ?, ?, ?, ?, 1)";

                try (PreparedStatement ps = con.prepareStatement(
                        sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, padre.getNombre());
                    ps.setString(2, padre.getApellido());
                    ps.setString(3, padre.getCorreo());
                    ps.setString(4, EncriptarPass.encriptarContrasena(padre.getContrasena()));
                    ps.setInt(5, idRol);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        idUsuario = keys.getInt(1);
                    }
                }

                String sqlPadre = "INSERT INTO padre "
                    + "(id_usuario, nombre, apellido, telefono, correo) "
                    + "VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(
                        sqlPadre, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, idUsuario);
                    ps.setString(2, padre.getNombre());
                    ps.setString(3, padre.getApellido());
                    ps.setString(4, padre.getTelefono());
                    ps.setString(5, padre.getCorreo());
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        idPadre = keys.getInt(1);
                    }
                }

                // NUEVO: el vínculo se crea junto con el acudiente
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO rel_padre_estudiante "
                      + "(id_padre, id_estudiante, parentesco) VALUES (?, ?, ?)")) {
                    ps.setInt(1, idPadre);
                    ps.setInt(2, idEstudiante);
                    ps.setString(3, parentesco);
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    // Actualiza acudiente y usuario. Si la clave viene vacía, se conserva.
    public void actualizar(Padre padre) throws SQLException {
        try (Connection con = BDConexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                if (padre.getIdUsuario() > 0) {
                    boolean cambiaClave = padre.getContrasena() != null
                            && !padre.getContrasena().trim().isEmpty();

                    String sqlU = "UPDATE usuario SET Nombre = ?, Apellido = ?, Correo = ?"
                        + (cambiaClave ? ", Contrasena = ?" : "")
                        + " WHERE id_usuario = ?";

                    try (PreparedStatement ps = con.prepareStatement(sqlU)) {
                        int i = 1;
                        ps.setString(i++, padre.getNombre());
                        ps.setString(i++, padre.getApellido());
                        ps.setString(i++, padre.getCorreo());
                        if (cambiaClave) {
                            ps.setString(i++, EncriptarPass.encriptarContrasena(padre.getContrasena().trim()));
                        }
                        ps.setInt(i, padre.getIdUsuario());
                        ps.executeUpdate();
                    }
                }

                String sqlP = "UPDATE padre SET nombre = ?, apellido = ?, "
                    + "telefono = ?, correo = ? WHERE id_padre = ?";

                try (PreparedStatement ps = con.prepareStatement(sqlP)) {
                    ps.setString(1, padre.getNombre());
                    ps.setString(2, padre.getApellido());
                    ps.setString(3, padre.getTelefono());
                    ps.setString(4, padre.getCorreo());
                    ps.setInt(5, padre.getIdPadre());
                    ps.executeUpdate();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    // Borra los vínculos con estudiantes, luego el acudiente y su usuario
    public void eliminar(Padre padre) throws SQLException {
        try (Connection con = BDConexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM rel_padre_estudiante WHERE id_padre = ?")) {
                    ps.setInt(1, padre.getIdPadre());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM padre WHERE id_padre = ?")) {
                    ps.setInt(1, padre.getIdPadre());
                    ps.executeUpdate();
                }

                if (padre.getIdUsuario() > 0) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "DELETE FROM usuario WHERE id_usuario = ?")) {
                        ps.setInt(1, padre.getIdUsuario());
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

    // Estudiantes vinculados a un acudiente:
    // cada fila es {idRelacion, nombre completo, documento, parentesco}
    public List<Object[]> listarVinculos(int idPadre) throws SQLException {
        String sql = "SELECT r.id_relacion_padre_estudiante, e.nombre, e.apellido, "
                   + "e.num_documento, r.parentesco "
                   + "FROM rel_padre_estudiante r "
                   + "JOIN estudiante e ON e.id_estudiante = r.id_estudiante "
                   + "WHERE r.id_padre = ? "
                   + "ORDER BY e.apellido, e.nombre";

        List<Object[]> filas = new ArrayList<>();

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPadre);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new Object[] {
                        rs.getInt("id_relacion_padre_estudiante"),
                        rs.getString("nombre") + " " + rs.getString("apellido"),
                        rs.getString("num_documento"),
                        rs.getString("parentesco")
                    });
                }
            }
        }
        return filas;
    }

    // Devuelve false si ese estudiante ya estaba vinculado a este acudiente
    public boolean vincular(int idPadre, int idEstudiante, String parentesco) throws SQLException {
        try (Connection con = BDConexion.getConexion()) {

            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM rel_padre_estudiante "
                  + "WHERE id_padre = ? AND id_estudiante = ?")) {
                ps.setInt(1, idPadre);
                ps.setInt(2, idEstudiante);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO rel_padre_estudiante (id_padre, id_estudiante, parentesco) "
                  + "VALUES (?, ?, ?)")) {
                ps.setInt(1, idPadre);
                ps.setInt(2, idEstudiante);
                ps.setString(3, parentesco);
                ps.executeUpdate();
            }
            return true;
        }
    }

    public void desvincular(int idRelacion) throws SQLException {
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(
                "DELETE FROM rel_padre_estudiante WHERE id_relacion_padre_estudiante = ?")) {
            ps.setInt(1, idRelacion);
            ps.executeUpdate();
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