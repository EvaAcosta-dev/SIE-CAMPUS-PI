package com.integrador.campus.controller;

import com.integrador.campus.dao.NotificacionDao;
import com.integrador.campus.model.Notificacion;
import com.integrador.campus.model.Rol;
import com.integrador.campus.model.Usuario;
import com.integrador.campus.service.CorreoService;

import jakarta.mail.MessagingException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de coordinar las operaciones del módulo de
 * notificaciones.
 *
 * Separa la interfaz gráfica de Swing del acceso a datos y del envío de
 * correos: el panel solicita una operación a este controlador, mientras que
 * el controlador usa el DAO para guardar y consultar notificaciones y
 * CorreoService para el envío opcional por correo electrónico.
 */
public class NotificacionController {

    // DAO utilizado para consultar usuarios y administrar las notificaciones.
    private final NotificacionDao dao;

    /**
     * Construye el controlador con una instancia real del DAO.
     */
    public NotificacionController() {
        this(new NotificacionDao());
    }

    /**
     * Permite proporcionar un DAO personalizado, principalmente para pruebas.
     *
     * @param dao implementación usada para acceder a los datos
     */
    public NotificacionController(NotificacionDao dao) {
        this.dao = dao;
    }

    /**
     * Obtiene los datos necesarios para mostrar los selectores de destinatarios:
     * la lista de usuarios activos y la lista de roles registrados.
     *
     * @return arreglo con la lista de usuarios y la lista de roles
     * @throws SQLException si ocurre un error al consultar la base de datos
     */
    public Object[] listarUsuariosYRoles() throws SQLException {
        return new Object[]{
            dao.listarUsuariosActivos(null),
            dao.listarRoles()
        };
    }

    /**
     * Obtiene los usuarios activos que corresponden al rol indicado.
     *
     * @param idRol identificador del rol, o {@code null} para consultar todos
     * @return lista de usuarios activos
     * @throws SQLException si ocurre un error al consultar la base de datos
     */
    public List<Usuario> listarUsuariosActivos(Integer idRol) throws SQLException {
        return dao.listarUsuariosActivos(idRol);
    }

    /**
     * Publica las notificaciones internas y, si está habilitado, envía una
     * copia por correo a cada destinatario.
     *
     * El correo es opcional: primero se guardan todas las notificaciones en la
     * base de datos y luego se intenta el envío SMTP. Un error de correo no
     * elimina ni impide la creación de las notificaciones internas.
     *
     * @param destinatarios usuarios que recibirán la notificación
     * @param titulo asunto de la notificación y del correo
     * @param mensaje contenido de la notificación y del correo
     * @param tipoEvento categoría usada para clasificar la notificación
     * @param enviarPorCorreo indica si también debe enviarse por correo
     * @return cantidades de publicaciones y resultado de los intentos de correo
     * @throws SQLException si la transacción interna no puede guardar los avisos
     */
    public ResultadoPublicacion publicar(List<Usuario> destinatarios,
            String titulo, String mensaje, String tipoEvento,
            boolean enviarPorCorreo) throws SQLException {

        // No hay operación que realizar si no existe ningún destinatario.
        if (destinatarios == null || destinatarios.isEmpty()) {
            return new ResultadoPublicacion(0, 0, 0, "");
        }

        // Convierte cada usuario en una notificación destinada a su id_usuario.
        List<Notificacion> notificaciones = new ArrayList<>();
        for (Usuario usuario : destinatarios) {
            notificaciones.add(new Notificacion(
                    usuario.getIdUsuario(),
                    titulo,
                    mensaje,
                    tipoEvento));
        }

        // Guarda la operación completa como un lote dentro del DAO.
        dao.crearNotificaciones(notificaciones);

        // Contadores usados para informar cuántos correos se enviaron y cuántos fallaron.
        int enviados = 0;
        int errores = 0;
        String detalleError = "";

        // El envío SMTP solo se ejecuta cuando el usuario seleccionó esa opción.
        if (enviarPorCorreo) {
            CorreoService correoService;
            try {
                correoService = new CorreoService();
            } catch (IllegalStateException e) {
                // La configuración incompleta impide usar SMTP para todos los destinatarios.
                correoService = null;
                detalleError = e.getMessage();
            }

            for (Usuario usuario : destinatarios) {
                if (correoService == null) {
                    errores++;
                    continue;
                }
                try {
                    // Envía el mismo título y mensaje al correo registrado del usuario.
                    correoService.enviar(usuario.getCorreo(), titulo, mensaje);
                    enviados++;
                } catch (MessagingException | IllegalArgumentException e) {
                    // Cuenta el fallo sin cancelar los demás envíos ni las notificaciones.
                    errores++;
                    detalleError = e.getMessage();
                }
            }
        }

        // Devuelve un resumen que la vista puede mostrar al usuario.
        return new ResultadoPublicacion(
                notificaciones.size(), enviados, errores, detalleError);
    }

    /**
     * Recupera todas las notificaciones internas de un usuario para mostrar su
     * bandeja.
     *
     * @param idUsuario identificador del propietario de la bandeja
     * @return lista de notificaciones recibidas por el usuario
     * @throws SQLException si ocurre un error al consultar la base de datos
     */
    public List<Notificacion> listarBandeja(int idUsuario) throws SQLException {
        return dao.listarNotificaciones(idUsuario);
    }

    /**
     * Marca una notificación como leída, siempre que pertenezca al usuario
     * indicado.
     *
     * @param idNotificacion identificador de la notificación
     * @param idUsuario identificador del usuario que intenta leerla
     * @return {@code true} si se modificó la fila; {@code false} si ya estaba
     *         leída o no pertenece al usuario
     * @throws SQLException si ocurre un error al actualizar la base de datos
     */
    public boolean marcarComoLeida(int idNotificacion, int idUsuario)
            throws SQLException {
        return dao.marcarComoLeida(idNotificacion, idUsuario);
    }

    /**
     * Representa el resultado de una publicación.
     *
     * Contiene el número de notificaciones internas creadas y los contadores
     * del intento de envío por correo, además de la última descripción de error
     * encontrada.
     */
    public static class ResultadoPublicacion {
        private final int cantidadInternas;
        private final int correosEnviados;
        private final int erroresCorreo;
        private final String detalleError;

        /**
         * Crea un resumen con los resultados de la publicación.
         *
         * @param cantidadInternas cantidad de avisos internos guardados
         * @param correosEnviados cantidad de correos entregados al servidor SMTP
         * @param erroresCorreo cantidad de intentos que finalizaron con error
         * @param detalleError descripción del último error encontrado
         */
        public ResultadoPublicacion(int cantidadInternas, int correosEnviados,
                int erroresCorreo, String detalleError) {
            this.cantidadInternas = cantidadInternas;
            this.correosEnviados = correosEnviados;
            this.erroresCorreo = erroresCorreo;
            this.detalleError = detalleError;
        }

        /**
         * @return cantidad de notificaciones internas creadas
         */
        public int getCantidadInternas() {
            return cantidadInternas;
        }

        /**
         * @return cantidad de correos enviados correctamente
         */
        public int getCorreosEnviados() {
            return correosEnviados;
        }

        /**
         * @return cantidad de errores presentados durante el envío
         */
        public int getErroresCorreo() {
            return erroresCorreo;
        }

        /**
         * @return mensaje de la última configuración o error SMTP encontrado
         */
        public String getDetalleError() {
            return detalleError;
        }
    }
}
