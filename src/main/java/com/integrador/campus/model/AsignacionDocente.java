package com.integrador.campus.model;

/**
 * Entidad que representa la asignación de un docente a una materia en un curso específico.
 */
public class AsignacionDocente {
    private int idAsignacion;
    private int idDocente;
    private int idMateria;
    private int idCurso;
    private int anioLectivo;
    private String estado; // ACTIVA, SUSPENDIDA, FINALIZADA
    private java.sql.Timestamp fechaAsignacion;

    public AsignacionDocente() { }

    public AsignacionDocente(int idAsignacion, int idDocente, int idMateria, int idCurso,
                             int anioLectivo, String estado, java.sql.Timestamp fechaAsignacion) {
        this.idAsignacion = idAsignacion;
        this.idDocente = idDocente;
        this.idMateria = idMateria;
        this.idCurso = idCurso;
        this.anioLectivo = anioLectivo;
        this.estado = estado;
        this.fechaAsignacion = fechaAsignacion;
    }

    public int getIdAsignacion() { return idAsignacion; }
    public void setIdAsignacion(int idAsignacion) { this.idAsignacion = idAsignacion; }

    public int getIdDocente() { return idDocente; }
    public void setIdDocente(int idDocente) { this.idDocente = idDocente; }

    public int getIdMateria() { return idMateria; }
    public void setIdMateria(int idMateria) { this.idMateria = idMateria; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public int getAnioLectivo() { return anioLectivo; }
    public void setAnioLectivo(int anioLectivo) { this.anioLectivo = anioLectivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public java.sql.Timestamp getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(java.sql.Timestamp fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }
}