package com.integrador.campus.model;

public class Nivel {
    private int idNivel;
    private String nombreNivel;

    public Nivel() {
    }

    // Constructor
    public Nivel(int idNivel, String nombreNivel) {
        this.idNivel = idNivel;
        this.nombreNivel = nombreNivel;
    }

    // Getters and Setters
    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public String getNombreNivel() {
        return nombreNivel;
    }

    public void setNombreNivel(String nombreNivel) {
        this.nombreNivel = nombreNivel;
    }
     @Override
    public String toString() {
        return nombreNivel;
    }
}
