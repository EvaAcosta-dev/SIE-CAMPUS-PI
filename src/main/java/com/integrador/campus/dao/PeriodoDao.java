package com.integrador.campus.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.integrador.campus.config.BDConexion;
import com.integrador.campus.model.Periodo;
public class PeriodoDao {
    // Registrar periodo
    public boolean registrarPeriodo(Periodo periodo) {
        String sql = "INSERT INTO periodos (nombre_periodo, id_anio, orden, fecha_inicio, fecha_fin, activo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, periodo.getNombrePeriodo());
            ps.setInt(2, periodo.getIdAnio());
            ps.setInt(3, periodo.getOrden());
            ps.setDate(4, Date.valueOf(periodo.getFecha_inicio()));
            ps.setDate(5, Date.valueOf(periodo.getFecha_fin()));
            ps.setBoolean(6, periodo.isActivo());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar periodo: " + e.getMessage());
            return false;
        }
    }

    // Métodos para listar los periodos
    public List<Periodo> listarPeriodos() {
        List<Periodo> periodos = new ArrayList<>();
        String sql = "SELECT p.id_periodo, p.nombre_periodo, p.id_anio, a.anio, p.orden, p.fecha_inicio, p.fecha_fin, p.activo " +
                     "FROM periodos p " +
                     "INNER JOIN anio_lectivo a ON p.id_anio = a.id_anio " +
                     "ORDER BY a.anio DESC, p.orden ASC";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Periodo periodo = new Periodo();
                periodo.setIdPeriodo(rs.getInt("id_periodo"));
                periodo.setNombrePeriodo(rs.getString("nombre_periodo"));
                periodo.setIdAnio(rs.getInt("id_anio"));
                periodo.setAnio(rs.getInt("anio"));
                periodo.setOrden(rs.getInt("orden"));
                
                java.sql.Date fInicio = rs.getDate("fecha_inicio");
                periodo.setFecha_inicio(fInicio != null ? fInicio.toLocalDate() : null);

                java.sql.Date fFin = rs.getDate("fecha_fin");
                periodo.setFecha_fin(fFin != null ? fFin.toLocalDate() : null);

                periodo.setActivo(rs.getBoolean("activo"));
                periodos.add(periodo);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar periodos: " + e.getMessage());
        }
        return periodos;
    }

    // Método para actualizar periodo
    public boolean actualizarPeriodo(Periodo periodo) {
        String sql = "UPDATE periodos SET nombre_periodo=?, id_anio=?, orden=?, fecha_inicio=?, fecha_fin=?, activo=? WHERE id_periodo=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, periodo.getNombrePeriodo());
            ps.setInt(2, periodo.getIdAnio());
            ps.setInt(3, periodo.getOrden());
            ps.setDate(4, Date.valueOf(periodo.getFecha_inicio()));
            ps.setDate(5, Date.valueOf(periodo.getFecha_fin()));
            ps.setBoolean(6, periodo.isActivo());
            ps.setInt(7, periodo.getIdPeriodo());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar periodo: " + e.getMessage());
            return false;
        }
    }

    // Método para eliminar periodo
    public boolean eliminarPeriodo(int idPeriodo) {
        String sql = "DELETE FROM periodos WHERE id_periodo=?";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPeriodo);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar periodo: " + e.getMessage());
            return false;
        }
    }
    //buscar periodo segun filtro aplicado
    
    public List<Periodo> buscarPeriodos(String nombre, int orden, int anio) {
        List<Periodo> lista = new ArrayList<>();
        String sql = "SELECT p.id_periodo, p.nombre_periodo, p.id_anio, a.anio, p.orden, p.fecha_inicio, p.fecha_fin, p.activo " +
                     "FROM periodos p " +
                     "INNER JOIN anio_lectivo a ON p.id_anio = a.id_anio " +
                     "WHERE (? IS NULL OR p.nombre_periodo LIKE ?) " +
                     "  AND (? = 0 OR p.orden = ?) " +
                     "  AND (? = 0 OR a.anio = ?) " +
                     "ORDER BY a.anio DESC, p.orden ASC";

        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (nombre != null && !nombre.trim().isEmpty()) {
                ps.setString(1, nombre.trim());
                ps.setString(2, "%" + nombre.trim() + "%");
            } else {
                ps.setNull(1, java.sql.Types.VARCHAR);
                ps.setNull(2, java.sql.Types.VARCHAR);
            }

            ps.setInt(3, orden);
            ps.setInt(4, orden);
            ps.setInt(5, anio);
            ps.setInt(6, anio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Periodo periodo = new Periodo();
                    periodo.setIdPeriodo(rs.getInt("id_periodo"));
                    periodo.setNombrePeriodo(rs.getString("nombre_periodo"));
                    periodo.setIdAnio(rs.getInt("id_anio"));
                    periodo.setAnio(rs.getInt("anio"));
                    periodo.setOrden(rs.getInt("orden"));
                    java.sql.Date fInicio = rs.getDate("fecha_inicio");
                    periodo.setFecha_inicio(fInicio != null ? fInicio.toLocalDate() : null);
                    java.sql.Date fFin = rs.getDate("fecha_fin");
                    periodo.setFecha_fin(fFin != null ? fFin.toLocalDate() : null);
                    periodo.setActivo(rs.getBoolean("activo"));
                    lista.add(periodo);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar periodos: " + e.getMessage());
        }
        return lista;
    }
}