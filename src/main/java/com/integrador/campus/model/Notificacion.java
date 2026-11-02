package com.integrador.campus.model;
import java.sql.Timestamp;

/**
 * Modelo que representa una notificación almacenada para un usuario.
 *
 * Contiene los datos mostrados en la bandeja interna y el estado leído/no
 * leído. El objeto sirve como puente entre las filas SQL del DAO, la lógica
 * del controlador y la vista Swing.
 */
public class Notificacion {


    // Identificador único generado por la tabla notificaciones.
    private int idNotificacion;
    // Usuario que debe recibir la notificación.
    private int idUsuarioDestino;
    // Título resumido mostrado en la bandeja.
    private String titulo;
    // Texto completo del aviso.
    private String mensaje;
    // Fecha y hora en que fue creada la notificación.
    private Timestamp fechaEnvio;
    // Indica si el usuario ya abrió o marcó el aviso como leído.
    private boolean leido;
    // Categoría visual o académica del aviso.
    private String tipoEvento;

    // Constructor vacío requerido para leer una fila SQL y asignar sus datos.
    public Notificacion() {}

    /**
     * Constructor usado al publicar una nueva notificación.
     *
     * @param idUsuarioDestino usuario destinatario
     * @param titulo encabezado del aviso
     * @param mensaje contenido completo
     * @param tipoEvento categoría del aviso
     */
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
