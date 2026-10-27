package com.integrador.campus.dao;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.CorreoEnvio;
import com.integrador.campus.model.Notificacion;
import com.integrador.campus.model.Rol;
import com.integrador.campus.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDao {

    public List<Rol> listarRoles() throws SQLException {
        String sql = "SELECT id_rol, nombre_rol FROM rol ORDER BY nombre_rol";
        List<Rol> roles = new ArrayList<>();

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roles.add(new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol")));
            }
        }
        return roles;
    }

    public List<Usuario> listarUsuariosActivos(Integer idRol) throws SQLException {
        String sql = "SELECT u.id_usuario, u.Nombre, u.Apellido, u.Correo, "
                + "u.id_rol, r.nombre_rol "
                + "FROM usuario u JOIN rol r ON r.id_rol = u.id_rol "
                + "WHERE u.activo = 1 "
                + (idRol == null ? "" : "AND u.id_rol = ? ")
                + "ORDER BY u.Apellido, u.Nombre";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (idRol != null) {
                ps.setInt(1, idRol);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("Nombre"));
                    usuario.setApellido(rs.getString("Apellido"));
                    usuario.setCorreo(rs.getString("Correo"));
                    usuario.setIdRol(rs.getInt("id_rol"));
                    usuario.setRol(new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol")));
                    usuarios.add(usuario);
                }
            }
        }
        return usuarios;
    }

    public void crearNotificacion(Notificacion notificacion) throws SQLException {
        String sql = "INSERT INTO notificaciones "
                + "(id_usuario_destino, titulo, mensaje, tipo_evento) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, notificacion.getIdUsuarioDestino());
            ps.setString(2, notificacion.getTitulo());
            ps.setString(3, notificacion.getMensaje());
            ps.setString(4, notificacion.getTipoEvento());
            ps.executeUpdate();
        }
    }

    public void crearNotificaciones(List<Notificacion> notificaciones) throws SQLException {
        String sql = "INSERT INTO notificaciones "
                + "(id_usuario_destino, titulo, mensaje, tipo_evento) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            try {
                for (Notificacion notificacion : notificaciones) {
                    ps.setInt(1, notificacion.getIdUsuarioDestino());
                    ps.setString(2, notificacion.getTitulo());
                    ps.setString(3, notificacion.getMensaje());
                    ps.setString(4, notificacion.getTipoEvento());
                    ps.addBatch();
                }
                ps.executeBatch();
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<Notificacion> listarNotificaciones(int idUsuarioDestino) throws SQLException {
        String sql = "SELECT id_notificacion, id_usuario_destino, "
                + "titulo, mensaje, fecha_envio, leido, tipo_evento "
                + "FROM notificaciones WHERE id_usuario_destino = ? "
                + "ORDER BY leido ASC, fecha_envio DESC";
        List<Notificacion> notificaciones = new ArrayList<>();
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioDestino);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notificacion notificacion = new Notificacion();
                    notificacion.setIdNotificacion(rs.getInt("id_notificacion"));
                    notificacion.setIdUsuarioDestino(rs.getInt("id_usuario_destino"));
                    notificacion.setTitulo(rs.getString("titulo"));
                    notificacion.setMensaje(rs.getString("mensaje"));
                    notificacion.setFechaEnvio(rs.getTimestamp("fecha_envio"));
                    notificacion.setLeido(rs.getBoolean("leido"));
                    notificacion.setTipoEvento(rs.getString("tipo_evento"));
                    notificaciones.add(notificacion);
                }
            }
        }
        return notificaciones;
    }

    public boolean marcarComoLeida(int idNotificacion, int idUsuarioDestino)
            throws SQLException {
        String sql = "UPDATE notificaciones SET leido = TRUE "
                + "WHERE id_notificacion = ? AND id_usuario_destino = ? AND leido = FALSE";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idNotificacion);
            ps.setInt(2, idUsuarioDestino);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Usuario> listarDestinatarios(Integer idRol) throws SQLException {
        String sql = "SELECT u.id_usuario, u.Nombre, u.Apellido, u.Correo, "
                + "u.id_rol, r.nombre_rol "
                + "FROM usuario u JOIN rol r ON r.id_rol = u.id_rol "
                + "WHERE u.activo = 1 AND TRIM(u.Correo) <> '' "
                + (idRol == null ? "" : "AND u.id_rol = ? ")
                + "ORDER BY u.id_usuario";
        List<Usuario> destinatarios = new ArrayList<>();

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (idRol != null) {
                ps.setInt(1, idRol);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rol rol = new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"));
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("Nombre"));
                    usuario.setApellido(rs.getString("Apellido"));
                    usuario.setCorreo(rs.getString("Correo"));
                    usuario.setIdRol(rol.getIdRol());
                    usuario.setRol(rol);
                    destinatarios.add(usuario);
                }
            }
        }
        return destinatarios;
    }

    public void registrarEnvio(CorreoEnvio envio) throws SQLException {
        String sql = "INSERT INTO correo_envio "
                + "(id_usuario_destino, correo_destino, asunto, mensaje, estado, detalle_error) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (envio.getIdUsuarioDestino() == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setLong(1, envio.getIdUsuarioDestino());
            }
            ps.setString(2, envio.getCorreoDestino());
            ps.setString(3, envio.getAsunto());
            ps.setString(4, envio.getMensaje());
            ps.setString(5, envio.getEstado());
            ps.setString(6, envio.getDetalleError());
            ps.executeUpdate();
        }
    }

    public List<CorreoEnvio> listarHistorial(int limite) throws SQLException {
        String sql = "SELECT id_usuario_destino, correo_destino, asunto, mensaje, "
                + "fecha_envio, estado, detalle_error "
                + "FROM correo_envio ORDER BY fecha_envio DESC LIMIT ?";
        List<CorreoEnvio> historial = new ArrayList<>();

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CorreoEnvio envio = new CorreoEnvio();
                    long idUsuario = rs.getLong("id_usuario_destino");
                    if (!rs.wasNull()) {
                        envio.setIdUsuarioDestino(idUsuario);
                    }
                    envio.setCorreoDestino(rs.getString("correo_destino"));
                    envio.setAsunto(rs.getString("asunto"));
                    envio.setMensaje(rs.getString("mensaje"));
                    envio.setFechaEnvio(rs.getTimestamp("fecha_envio"));
                    envio.setEstado(rs.getString("estado"));
                    envio.setDetalleError(rs.getString("detalle_error"));
                    historial.add(envio);
                }
            }
        }
        return historial;
    }
}
