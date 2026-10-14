package com.integrador.campus.dao;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Rol;
import com.integrador.campus.model.Usuario;
import com.integrador.campus.Util.EncriptarPass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    public Usuario login(String correo, String contrasena) {
        String sql = "SELECT u.id_usuario, u.Nombre, u.Apellido, "
        + "u.Correo, u.Contrasena, u.id_rol, r.nombre_rol "
        + "FROM usuario u "
        + "JOIN rol r ON u.id_rol = r.id_rol "
        + "WHERE u.Correo = ? AND u.activo = 1";

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGuardado = rs.getString("Contrasena");
                    /*System.out.println("--> Correo buscado: " + correo);
                    System.out.println("--> Contraseña tipeada: [" + contrasena + "]");
                    System.out.println("--> Contraseña en BD:     [" + hashGuardado + "]");*/
                    // Verificación: primero intenta BCrypt, si falla compara directo (para datos sin encriptar)
                    boolean passwordMatch = EncriptarPass.verificarContrasena(contrasena, hashGuardado) 
                        || contrasena.equals(hashGuardado);
                    if (passwordMatch) {
                    Rol rol = new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"));
                        Usuario usuario = new Usuario();
                        usuario.setIdUsuario(rs.getInt("id_usuario"));
                        usuario.setNombre(rs.getString("Nombre"));
                        usuario.setApellido(rs.getString("Apellido"));
                        usuario.setCorreo(rs.getString("Correo"));
                        usuario.setIdRol(rol.getIdRol());
                        usuario.setRol(rol);
                        usuario.setActivo(true);

                        return usuario;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en Login SQL: " + e.getMessage());
        }

        return null;
    }

    public boolean registrarUsuario(Usuario u) {
        String sql = "INSERT INTO usuario (Nombre, Apellido, Correo, Contrasena, id_rol,activo) VALUES (?, ?, ?, ?, ?, ?)";

        if (u.getContrasena() == null || u.getContrasena().trim().isEmpty()) {
            return false;
        }

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            
            // Cifrar antes de guardar en la BD
            String contrasenaHash = EncriptarPass.encriptarContrasena(u.getContrasena());
            ps.setString(4, contrasenaHash);
            ps.setInt(5, u.getIdRol());
            ps.setBoolean(6, u.isActivo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }//lista para buscar en tabla
       
public List<Usuario> listaUsuario(String value) throws SQLException {
    List<Usuario> lista = new ArrayList<>();

    String sql = "SELECT u.id_usuario, u.Nombre, u.Apellido, "
            + "u.Correo, u.id_rol, u.activo, r.nombre_rol "
            + "FROM usuario u "
            + "INNER JOIN rol r ON u.id_rol = r.id_rol "
            + "WHERE u.Nombre LIKE ? "
            + "OR u.Apellido LIKE ? "
            + "OR u.Correo LIKE ? "
            + "OR r.nombre_rol LIKE ? "
            + "ORDER BY u.id_usuario ASC";

    try (Connection con = BDConexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        String buscar = "%" + value + "%";

        ps.setString(1, buscar);
        ps.setString(2, buscar);
        ps.setString(3, buscar);
        ps.setString(4, buscar);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Rol rol = new Rol(
                        rs.getInt("id_rol"),
                        rs.getString("nombre_rol")
                );

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setApellido(rs.getString("Apellido"));
                usuario.setCorreo(rs.getString("Correo"));
                usuario.setIdRol(rol.getIdRol());
                usuario.setRol(rol);
                usuario.setActivo(rs.getBoolean("activo"));

                lista.add(usuario);
            }
        }
    }

    return lista;
}
         public List<Usuario> listaUsuario() {
    List<Usuario> lista = new ArrayList<>();

    String sql = "SELECT u.id_usuario, u.Nombre, u.Apellido, u.Correo, u.id_rol,u.activo, r.nombre_rol " +
                 "FROM usuario u " +
                 "INNER JOIN rol r ON u.id_rol = r.id_rol " +
                 "ORDER BY u.id_usuario DESC";

    try (Connection con = BDConexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Rol rol = new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"));

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(rs.getInt("id_usuario"));
            usuario.setNombre(rs.getString("Nombre"));
            usuario.setApellido(rs.getString("Apellido"));
            usuario.setCorreo(rs.getString("Correo"));
            usuario.setIdRol(rol.getIdRol());
            usuario.setRol(rol);
            usuario.setActivo(rs.getBoolean("activo"));

            lista.add(usuario);
        }

    } catch (SQLException e) {
        System.err.println("Error al listar usuarios: " + e.getMessage());
    }

    return lista;
}

    public List<Rol> listaRoles() {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT id_rol, nombre_rol FROM rol ORDER BY id_rol ASC";

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"));
                lista.add(rol);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar roles: " + e.getMessage());
        }

        return lista;
    }
    //  ACTUALIZAR 
    public boolean actualizarUsuario(Usuario u) {
        String sql = "UPDATE usuario SET Nombre = ?, Apellido = ?, Correo = ?, id_rol = ?,activo = ?"
                   + (u.getContrasena() != null && !u.getContrasena().trim().isEmpty() ? ", Contrasena = ?" : "")
                   + " WHERE id_usuario = ?";

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            ps.setInt(4, u.getIdRol());
            ps.setBoolean(5, u.isActivo());


            int paramIndex = 6;
            if (u.getContrasena() != null && !u.getContrasena().trim().isEmpty()) {
                String hash = EncriptarPass.encriptarContrasena(u.getContrasena().trim());
                ps.setString(paramIndex++, hash);
            }
            ps.setInt(paramIndex, u.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    //  ELIMINAR 
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    public boolean cambiarEstadoUsuario(
            int idUsuario, boolean activo) {

        String sql =
            "UPDATE usuario SET activo = ? "
            + "WHERE id_usuario = ?";

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, activo);
            ps.setInt(2, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                "Error al cambiar estado: " + e.getMessage()
            );
            return false;
        }
    }
    
}  


