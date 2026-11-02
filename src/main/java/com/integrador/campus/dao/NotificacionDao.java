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

/**
 * Acceso a datos del módulo de notificaciones.
 *
 * Esta clase contiene las sentencias SQL necesarias para consultar usuarios,
 * crear notificaciones internas, cargar la bandeja, marcar avisos como leídos
 * y, opcionalmente, administrar el historial de correos enviados.
 *
 * Las clases de interfaz y la lógica SMTP no ejecutan SQL directamente; cada
 * operación pasa primero por este DAO.
 */
public class NotificacionDao {

    /**
     * Lista los roles registrados para mostrar las opciones de destinatarios.
     */
    public List<Rol> listarRoles() throws SQLException {
        // Consulta los roles y los ordena alfabéticamente.
        String sql = "SELECT id_rol, nombre_rol FROM rol ORDER BY nombre_rol";
        // Lista temporal donde se convertirán las filas SQL en objetos Rol.
        List<Rol> roles = new ArrayList<>();

        // try-with-resources abre los recursos necesarios y los cierra
        // automáticamente cuando termina la consulta.
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Cada fila recibida se transforma en un objeto de modelo.
                roles.add(new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol")));
            }
        }
        return roles;
    }

    /**
     * Lista los usuarios activos. Si se recibe un rol, filtra por ese rol.
     */
    public List<Usuario> listarUsuariosActivos(Integer idRol) throws SQLException {
        // Una consulta base selecciona usuarios activos y su correo.
        String sql = "SELECT u.id_usuario, u.Nombre, u.Apellido, u.Correo, "
                + "u.id_rol, r.nombre_rol "
                + "FROM usuario u JOIN rol r ON r.id_rol = u.id_rol "
                + "WHERE u.activo = 1 "
                + (idRol == null ? "" : "AND u.id_rol = ? ")
                + "ORDER BY u.Apellido, u.Nombre";
        // Lista que agrupará todos los usuarios devueltos por la consulta.
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (idRol != null) {
                // Reemplaza el marcador ? cuando la publicación fue filtrada por rol.
                ps.setInt(1, idRol);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Convierte cada fila del ResultSet en un Usuario completo.
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

    /**
     * Guarda una sola notificación interna asociada a un usuario.
     */
    public void crearNotificacion(Notificacion notificacion) throws SQLException {
        String sql = "INSERT INTO notificaciones "
                + "(id_usuario_destino, titulo, mensaje, tipo_evento) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            // Reemplaza los marcadores ? con los datos del objeto recibido.
            ps.setInt(1, notificacion.getIdUsuarioDestino());
            ps.setString(2, notificacion.getTitulo());
            ps.setString(3, notificacion.getMensaje());
            ps.setString(4, notificacion.getTipoEvento());
            // Inserta una sola fila de la tabla notificaciones.
            ps.executeUpdate();
        }
    }

    /**
     * Guarda varias notificaciones como una transacción por lotes.
     * significa ejecutar varias operaciones SQL como un único grupo lógico.
     */
    public void crearNotificaciones(List<Notificacion> notificaciones) throws SQLException {
        String sql = "INSERT INTO notificaciones "
                + "(id_usuario_destino, titulo, mensaje, tipo_evento) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            // Desactiva la confirmación automática para que todas se guarden juntas.
            con.setAutoCommit(false);
            try {
                for (Notificacion notificacion : notificaciones) {
                    // Carga los datos de una notificación dentro del siguiente elemento del lote.
                    ps.setInt(1, notificacion.getIdUsuarioDestino());
                    ps.setString(2, notificacion.getTitulo());
                    ps.setString(3, notificacion.getMensaje());
                    ps.setString(4, notificacion.getTipoEvento());
                    // Agrega los datos al lote en lugar de ejecutar uno por uno.
                    ps.addBatch();
                }//ejecuta todas las inserciones juntas.
                ps.executeBatch();
                con.commit();// si todas funcionan,confirma todas las notificaciones.
            } catch (SQLException e) {
                // Si una inserción falla, deshace todas las notificaciones del lote.
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    /**
     * Obtiene la bandeja interna ordenada por estado y fecha de envío.
     */
    public List<Notificacion> listarNotificaciones(int idUsuarioDestino) throws SQLException {
        String sql = "SELECT id_notificacion, id_usuario_destino, "
                + "titulo, mensaje, fecha_envio, leido, tipo_evento "
                + "FROM notificaciones WHERE id_usuario_destino = ? "
                + "ORDER BY leido ASC, fecha_envio DESC";
        // Lista que representa las notificaciones mostradas en la bandeja.
        List<Notificacion> notificaciones = new ArrayList<>();
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            // Limita los resultados al propietario de la bandeja actual.
            ps.setInt(1, idUsuarioDestino);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Convierte una fila SQL en el objeto usado por la interfaz.
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

    /**
     * Marca una notificación como leída solo si pertenece al usuario indicado.
     */
    public boolean marcarComoLeida(int idNotificacion, int idUsuarioDestino)
            throws SQLException {
        String sql = "UPDATE notificaciones SET leido = TRUE "
                + "WHERE id_notificacion = ? AND id_usuario_destino = ? AND leido = FALSE";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            // Solo permite marcar una fila cuando coincide notificación y usuario.
            ps.setInt(1, idNotificacion);
            ps.setInt(2, idUsuarioDestino);
            // executeUpdate() indica cuántas filas fueron modificadas.
            // True significa que esta vez se cambió el estado de la notificación.
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lista usuarios activos que tienen correo y pueden recibir correo masivo.
     */
    public List<Usuario> listarDestinatarios(Integer idRol) throws SQLException {
        String sql = "SELECT u.id_usuario, u.Nombre, u.Apellido, u.Correo, "
                + "u.id_rol, r.nombre_rol "
                + "FROM usuario u JOIN rol r ON r.id_rol = u.id_rol "
                + "WHERE u.activo = 1 AND TRIM(u.Correo) <> '' "
                + (idRol == null ? "" : "AND u.id_rol = ? ")
                + "ORDER BY u.id_usuario";
        // Lista exclusivamente para destinatarios que pueden recibir correo.
        List<Usuario> destinatarios = new ArrayList<>();

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (idRol != null) {
                // Agrega el filtro de rol al marcador de la consulta.
                ps.setInt(1, idRol);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Construye el rol asociado al usuario.
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

    /**
     * Guarda el resultado de un intento de envío SMTP.
     * Sirve para saber posteriormente si un correo fue enviado, falló o quedó pendiente.
     */
    public void registrarEnvio(CorreoEnvio envio) throws SQLException {
        String sql = "INSERT INTO correo_envio "
                + "(id_usuario_destino, correo_destino, asunto, mensaje, estado, detalle_error) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (envio.getIdUsuarioDestino() == null) {
                // Permite registros manuales que no estén asociados a un usuario.
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                // Guarda la relación opcional con el usuario destinatario.
                ps.setLong(1, envio.getIdUsuarioDestino());
            }
            // Completa el correo, texto, estado y posible explicación de error.
            ps.setString(2, envio.getCorreoDestino());
            ps.setString(3, envio.getAsunto());
            ps.setString(4, envio.getMensaje());
            ps.setString(5, envio.getEstado());
            ps.setString(6, envio.getDetalleError());
            ps.executeUpdate();
        }
    }

}