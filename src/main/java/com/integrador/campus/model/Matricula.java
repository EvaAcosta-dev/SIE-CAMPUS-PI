package com.integrador.campus.model;

/**
 * Entidad que representa una matrícula de un estudiante en un curso.
 */
public class Matricula {
    private int idMatricula;
    private int idEstudiante;
    private int idCurso;
    private String estado; // ACTIVA, RETIRADA, TRASLADO
    private String observacion;
    private java.sql.Timestamp fechaMatricula;

    public Matricula() { }

    public Matricula(int idMatricula, int idEstudiante, int idCurso,
                     String estado, String observacion, java.sql.Timestamp fechaMatricula) {
        this.idMatricula = idMatricula;
        this.idEstudiante = idEstudiante;
        this.idCurso = idCurso;
        this.estado = estado;
        this.observacion = observacion;
        this.fechaMatricula = fechaMatricula;
    }

    public int getIdMatricula() { return idMatricula; }
    public void setIdMatricula(int idMatricula) { this.idMatricula = idMatricula; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public java.sql.Timestamp getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(java.sql.Timestamp fechaMatricula) { this.fechaMatricula = fechaMatricula; }
}