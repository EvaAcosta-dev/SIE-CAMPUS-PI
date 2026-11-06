package com.integrador.campus.model;

/**
 * Entidad que representa el registro de asistencia de un estudiante en una clase.
 */
public class Asistencia {
    private int idAsistencia;
    private int idEstudiante;
    private int idMateria;
    private int idCurso;
    private java.sql.Date fechaClase;
    private String estado; // PRESENTE, AUSENTE, JUSTIFICADA
    private String observacion;
    private java.sql.Timestamp fechaRegistro;

    public Asistencia() { }

    public Asistencia(int idAsistencia, int idEstudiante, int idMateria, int idCurso,
                      java.sql.Date fechaClase, String estado, String observacion,
                      java.sql.Timestamp fechaRegistro) {
        this.idAsistencia = idAsistencia;
        this.idEstudiante = idEstudiante;
        this.idMateria = idMateria;
        this.idCurso = idCurso;
        this.fechaClase = fechaClase;
        this.estado = estado;
        this.observacion = observacion;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdAsistencia() { return idAsistencia; }
    public void setIdAsistencia(int idAsistencia) { this.idAsistencia = idAsistencia; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdMateria() { return idMateria; }
    public void setIdMateria(int idMateria) { this.idMateria = idMateria; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public java.sql.Date getFechaClase() { return fechaClase; }
    public void setFechaClase(java.sql.Date fechaClase) { this.fechaClase = fechaClase; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public java.sql.Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(java.sql.Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}