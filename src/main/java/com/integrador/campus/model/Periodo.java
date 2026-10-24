package com.integrador.campus.model;

import java.time.LocalDate;

public class Periodo {
    private int idPeriodo;
    private String nombrePeriodo;
    private int orden;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private int idAnio;
    private boolean activo;

    private int anio;


    public Periodo(){}

    public Periodo(int idPeriodo, String nombrePeriodo, int orden, LocalDate fecha_inicio, LocalDate fecha_fin,boolean activo) {
        this.idPeriodo = idPeriodo;
        this.nombrePeriodo = nombrePeriodo;
        this.orden = orden;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.activo=activo;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getNombrePeriodo() {
        return nombrePeriodo;
    }

    public void setNombrePeriodo(String nombrePeriodo) {
        this.nombrePeriodo = nombrePeriodo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getIdAnio() {
        return idAnio;
    }

    public void setIdAnio(int idAnio) {
        this.idAnio = idAnio;
    }
    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public boolean isActivo(){
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    

    @Override
    public String toString() {
        return nombrePeriodo;
    }


}
