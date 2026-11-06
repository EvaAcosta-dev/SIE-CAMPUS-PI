package com.integrador.campus.model;

/**
 * Entidad que representa un boletín académico de un estudiante.
 */
public class Boletin {
    private int idBoletin;
    private int idEstudiante;
    private int idCurso;
    private int anioLectivo;
    private String periodo;
    private double promedioGeneral;
    private int aprobadas;
    private int reprobadas;
    private String estadoBoletin; // GENERADO, EN_REVISION, PUBLICADO, ARCHIVADO
    private java.sql.Timestamp fechaGeneracion;

    public Boletin() { }

    public Boletin(int idBoletin, int idEstudiante, int idCurso, int anioLectivo,
                   String periodo, double promedioGeneral, int aprobadas, int reprobadas,
                   String estadoBoletin, java.sql.Timestamp fechaGeneracion) {
        this.idBoletin = idBoletin;
        this.idEstudiante = idEstudiante;
        this.idCurso = idCurso;
        this.anioLectivo = anioLectivo;
        this.periodo = periodo;
        this.promedioGeneral = promedioGeneral;
        this.aprobadas = aprobadas;
        this.reprobadas = reprobadas;
        this.estadoBoletin = estadoBoletin;
        this.fechaGeneracion = fechaGeneracion;
    }

    public int getIdBoletin() { return idBoletin; }
    public void setIdBoletin(int idBoletin) { this.idBoletin = idBoletin; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public int getAnioLectivo() { return anioLectivo; }
    public void setAnioLectivo(int anioLectivo) { this.anioLectivo = anioLectivo; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public double getPromedioGeneral() { return promedioGeneral; }
    public void setPromedioGeneral(double promedioGeneral) { this.promedioGeneral = promedioGeneral; }

    public int getAprobadas() { return aprobadas; }
    public void setAprobadas(int aprobadas) { this.aprobadas = aprobadas; }

    public int getReprobadas() { return reprobadas; }
    public void setReprobadas(int reprobadas) { this.reprobadas = reprobadas; }

    public String getEstadoBoletin() { return estadoBoletin; }
    public void setEstadoBoletin(String estadoBoletin) { this.estadoBoletin = estadoBoletin; }

    public java.sql.Timestamp getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(java.sql.Timestamp fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
}