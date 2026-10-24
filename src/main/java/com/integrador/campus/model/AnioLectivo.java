package com.integrador.campus.model;

import java.time.LocalDate;

public class AnioLectivo {
    private int idAnio;
    private int anio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;

    public AnioLectivo() {}

    public AnioLectivo(int idAnio, int anio, LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.idAnio = idAnio;
        this.anio = anio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdAnio() { return idAnio; }
    public void setIdAnio(int idAnio) { this.idAnio = idAnio; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.valueOf(anio); // Esto permite que el JComboBox muestre el año(ej: 2026) 
    }
}
