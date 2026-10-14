package com.integrador.campus.model;

public class Jornada {
    private int idJornada;
    private String nombreJornada;

    public Jornada() {}

    public Jornada(int idJornada, String nombreJornada) {
        this.idJornada = idJornada;
        this.nombreJornada = nombreJornada;
    }

    // Getters y Setters
    public int getIdJornada() { 
        return idJornada; }

    public void setIdJornada(int idJornada) { 
        this.idJornada = idJornada; }

    public String getNombreJornada() { 
        return nombreJornada; }
        
    public void setNombreJornada(String nombreJornada) { 
        this.nombreJornada = nombreJornada; }

    @Override
    public String toString() {
        return nombreJornada; // Muestra el nombre de la jornada en el JComboBox 
    }
}