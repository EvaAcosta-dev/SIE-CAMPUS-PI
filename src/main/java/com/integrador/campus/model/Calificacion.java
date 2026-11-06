package com.integrador.campus.model;

/**
 * Entidad que representa una calificación (nota) de un estudiante en una materia de un curso.
 */
public class Calificacion {
    private int idCalificacion;
    private int idEstudiante;
    private int idMateria;
    private int idCurso;
    private double nota;
    private String periodo; // PRIMER_TRIMESTRE, SEGUNDO_TRIMESTRE, TERCER_TRIMESTRE, FINAL
    private int anioLectivo;
    private String observacion;
    private java.sql.Timestamp fechaRegistro;

    public Calificacion() { }

    public Calificacion(int idCalificacion, int idEstudiante, int idMateria, int idCurso,
                        double nota, String periodo, int anioLectivo, String observacion,
                        java.sql.Timestamp fechaRegistro) {
        this.idCalificacion = idCalificacion;
        this.idEstudiante = idEstudiante;
        this.idMateria = idMateria;
        this.idCurso = idCurso;
        this.nota = nota;
        this.periodo = periodo;
        this.anioLectivo = anioLectivo;
        this.observacion = observacion;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdCalificacion() { return idCalificacion; }
    public void setIdCalificacion(int idCalificacion) { this.idCalificacion = idCalificacion; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdMateria() { return idMateria; }
    public void setIdMateria(int idMateria) { this.idMateria = idMateria; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public int getAnioLectivo() { return anioLectivo; }
    public void setAnioLectivo(int anioLectivo) { this.anioLectivo = anioLectivo; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public java.sql.Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(java.sql.Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}