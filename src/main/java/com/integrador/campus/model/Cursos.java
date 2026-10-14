package com.integrador.campus.model;

import java.sql.Timestamp;

public class Cursos {
 
    private int idCurso;
    private String nombreCurso;
    private int idGrado;
    private int idAnio;
    private Integer idDocenteDirector;   // null = sin director de grupo
    private int idJornada;
    private Timestamp fechaCreacion;
    private Timestamp fechaActualizacion;

 
    // Campos de solo lectura que llegan de los JOIN (para mostrar en la tabla)
    private String nombreGrado;      // grado.nombre_visible  (ej. "Sexto")
    private int anio;                // anio_lectivo.anio     (ej. 2026)
    private String nombreDirector;   // docente.nombre + apellido
    private String nombreJornada;    // jornada.nombre
    private String nombreNivel;      // nivel.nombre
 
    public Cursos() { }
 

    public Cursos(int idCurso, int idGrado,String nombreCurso, int idAnio, Integer idDocenteDirector, int idJornada,
            Timestamp fechaCreacion, Timestamp fechaActualizacion) {
        this.idCurso = idCurso;
        this.idGrado = idGrado;
        this.nombreCurso = nombreCurso;
        this.idAnio = idAnio;
        this.idDocenteDirector = idDocenteDirector;
        this.idJornada = idJornada;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public int getIdAnio() {
        return idAnio;
    }

    public void setIdAnio(int idAnio) {
        this.idAnio = idAnio;
    }

    public Integer getIdDocenteDirector() {
        return idDocenteDirector;
    }

    public void setIdDocenteDirector(Integer idDocenteDirector) {
        this.idDocenteDirector = idDocenteDirector;
    }

    public int getIdJornada() {
        return idJornada;
    }

    public void setIdJornada(int idJornada) {
        this.idJornada = idJornada;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Timestamp fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getNombreGrado() {
        return nombreGrado;
    }

    public void setNombreGrado(String nombreGrado) {
        this.nombreGrado = nombreGrado;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getNombreDirector() {
        return nombreDirector;
    }

    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }

    public String getNombreJornada() {
        return nombreJornada;
    }

    public void setNombreJornada(String nombreJornada) {
        this.nombreJornada = nombreJornada;
    }

    public String getNombreNivel() {
        return nombreNivel;
    }

    public void setNombreNivel(String nombreNivel) {
        this.nombreNivel = nombreNivel;
    }

   @Override 
   public String toString(){
         return nombreCurso + " - " + nombreGrado + " - " + anio;   
   }
}