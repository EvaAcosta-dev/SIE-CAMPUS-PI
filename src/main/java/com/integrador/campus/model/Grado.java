package com.integrador.campus.model;

public class Grado {
    private int idGrado;
    private short numeroGrado;
    private String nombreVisible;
    private int idNivel;
    
    public Grado() {
    }

    public Grado(int idGrado, short numeroGrado, String nombreVisible, int idNivel) {
        this.idGrado = idGrado;
        this.numeroGrado = numeroGrado;
        this.nombreVisible = nombreVisible;
        this.idNivel = idNivel;
    }

    

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public short getNumeroGrado() {
        return numeroGrado;
    }

    public void setNumeroGrado(short numeroGrado) {
        this.numeroGrado = numeroGrado;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }

    public void setNombreVisible(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }
    @Override
    public String toString() {
        return nombreVisible;
    }
}
