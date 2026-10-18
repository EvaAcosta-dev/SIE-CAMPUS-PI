package com.integrador.campus.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
        String sql = "SELECT id_periodo, nombre_periodo, id_anio, orden, fecha_inicio, fecha_fin, activo FROM periodos ORDER BY id_anio DESC, orden ASC";
        try (Connection con = BDConexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Periodo periodo = new Periodo();
                periodo.setIdPeriodo(rs.getInt("id_periodo"));
                periodo.setNombrePeriodo(rs.getString("nombre_periodo"));
                periodo.setIdAnio(rs.getInt("id_anio"));
                periodo.setOrden(rs.getInt("orden"));
                periodo.setFecha_inicio(rs.getDate("fecha_inicio").toLocalDate());
                periodo.setFecha_fin(rs.getDate("fecha_fin").toLocalDate());
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
// Método unificado para buscar periodos por múltiples criterios (Filtro Dinámico)
public List<Periodo> buscarPeriodos(String nombre, int orden, int idAnio, LocalDate inicio, LocalDate fin) {
    List<Periodo> lista = new ArrayList<>();
    
    // Una sola consulta capaz de filtrar por cualquier combinación de parámetros
    String sql = "SELECT id_periodo, nombre_periodo, id_anio, orden, fecha_inicio, fecha_fin, activo " +
                 "FROM periodos " +
                 "WHERE (? IS NULL OR nombre_periodo LIKE ?) " +
                 "  AND (? = 0 OR orden = ?) " +
                 "  AND (? = 0 OR id_anio = ?) " +
                 "  AND (? IS NULL OR fecha_inicio >= ?) " +
                 "  AND (? IS NULL OR fecha_fin <= ?) " +
                 "ORDER BY id_anio DESC, orden ASC";

    try (Connection con = BDConexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        // 1. Filtro de Nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            ps.setString(1, nombre);
            ps.setString(2, "%" + nombre + "%");
        } else {
            ps.setNull(1, java.sql.Types.VARCHAR);
            ps.setNull(2, java.sql.Types.VARCHAR);
        }
        
        // 2. Filtro de Orden
        ps.setInt(3, orden);
        ps.setInt(4, orden);
        
        // 3. Filtro de Año
        ps.setInt(5, idAnio);
        ps.setInt(6, idAnio);
        
        // 4. Filtro de Fecha Inicio
        if (inicio != null) {
            ps.setDate(7, Date.valueOf(inicio));
            ps.setDate(8, Date.valueOf(inicio));
        } else {
            ps.setNull(7, java.sql.Types.DATE);
            ps.setNull(8, java.sql.Types.DATE);
        }
        
        // 5. Filtro de Fecha Fin
        if (fin != null) {
            ps.setDate(9, Date.valueOf(fin));
            ps.setDate(10, Date.valueOf(fin));
        } else {
            ps.setNull(9, java.sql.Types.DATE);
            ps.setNull(10, java.sql.Types.DATE);
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Periodo periodo = new Periodo();
                periodo.setIdPeriodo(rs.getInt("id_periodo"));
                periodo.setNombrePeriodo(rs.getString("nombre_periodo"));
                periodo.setIdAnio(rs.getInt("id_anio"));
                periodo.setOrden(rs.getInt("orden"));
                periodo.setFecha_inicio(rs.getDate("fecha_inicio").toLocalDate());
                periodo.setFecha_fin(rs.getDate("fecha_fin").toLocalDate());
                periodo.setActivo(rs.getBoolean("activo"));
                lista.add(periodo);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al buscar periodos con filtro dinámico: " + e.getMessage());
    }
    return lista;
}
}