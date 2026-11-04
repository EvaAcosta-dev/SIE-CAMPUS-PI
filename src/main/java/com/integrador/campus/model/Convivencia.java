package com.integrador.campus.model;

/**
 * Entidad que representa un reporte de convivencia escolar.
 */
public class Convivencia {
    private int idConvivencia;
    private int idEstudiante;
    private int idDocente; // quién registra
    private java.sql.Date fechaReporte;
    private String tipoFalta; // LEVE, GRAVE, GRAVISIMA
    private String observacion;
    private String medidaDisciplinaria;
    private String estado; // PENDIENTE, EN_SEGUIMIENTO, RESUELTO, ARCHIVADO
    private java.sql.Timestamp fechaRegistro;

    public Convivencia() { }

    public Convivencia(int idConvivencia, int idEstudiante, int idDocente,
                       java.sql.Date fechaReporte, String tipoFalta, String observacion,
                       String medidaDisciplinaria, String estado, java.sql.Timestamp fechaRegistro) {
        this.idConvivencia = idConvivencia;
        this.idEstudiante = idEstudiante;
        this.idDocente = idDocente;
        this.fechaReporte = fechaReporte;
        this.tipoFalta = tipoFalta;
        this.observacion = observacion;
        this.medidaDisciplinaria = medidaDisciplinaria;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdConvivencia() { return idConvivencia; }
    public void setIdConvivencia(int idConvivencia) { this.idConvivencia = idConvivencia; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdDocente() { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }

    public java.sql.Date getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(java.sql.Date fechaReporte) { this.fechaReporte = fechaReporte; }

    public String getTipoFalta() { return tipoFalta; }
    public void setTipoFalta(String tipoFalta) { this.tipoFalta = tipoFalta; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getMedidaDisciplinaria() { return medidaDisciplinaria; }
    public void setMedidaDisciplinaria(String medidaDisciplinaria) { this.medidaDisciplinaria = medidaDisciplinaria; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public java.sql.Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(java.sql.Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}