package com.integrador.campus.model;
import java.sql.Timestamp;

public class Notificacion {


    private int idNotificacion;
    private int idUsuarioDestino;
    private String titulo;
    private String mensaje;
    private Timestamp fechaEnvio;
    private boolean leido;
    private String tipoEvento;

    // Constructor 
    public Notificacion() {}

    public Notificacion(int idUsuarioDestino, String titulo, String mensaje, String tipoEvento) {
        this.idUsuarioDestino = idUsuarioDestino;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipoEvento = tipoEvento;
        this.leido = false; // Por defecto inicia como no leída
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public int getIdUsuarioDestino() {
        return idUsuarioDestino;
    }

    public void setIdUsuarioDestino(int idUsuarioDestino) {
        this.idUsuarioDestino = idUsuarioDestino;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Timestamp getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Timestamp fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    // Getters y Setters
    
}
