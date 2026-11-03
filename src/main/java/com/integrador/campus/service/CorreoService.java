package com.integrador.campus.service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Servicio responsable de configurar, autenticar y enviar correos mediante
 * un servidor SMTP.
 *
 * Este servicio solo administra el envío electrónico. La creación de las
 * notificaciones internas corresponde al controlador y al DAO.
 */
public class CorreoService {

    // Nombre de usuario usado para autenticar la sesión SMTP.
    private final String usuario;
    // Dirección que aparecerá como remitente real del mensaje.
    private final String remitente;
    // Sesión de Jakarta Mail que contiene el servidor y las opciones SMTP.
    private final Session session;
    // Configuración cargada desde src/main/resources/smtp.properties.
    private final Properties configuracionArchivo = cargarConfiguracion();

    /**
     * Construye el servicio SMTP y valida la configuración necesaria.
     *
     * Primero busca el host, puerto, usuario y contraseña. Luego configura
     * autenticación, cifrado STARTTLS y tiempos máximos de conexión.
     */
    public CorreoService() {
        // Se obtiene el servidor: variable de entorno, propiedad JVM o Gmail.
        String host = obtenerConfiguracion(
                "CAMPUS_SMTP_HOST", "smtp.host", "smtp.gmail.com");
        // Se obtiene el puerto; 587 es el puerto habitual para STARTTLS.
        String puerto = obtenerConfiguracion(
                "CAMPUS_SMTP_PORT", "smtp.port", "587");
        // Credencial usada para iniciar sesión en el servidor SMTP.
        this.usuario = obtenerConfiguracion(
                "CAMPUS_SMTP_USER", "smtp.user", null);

        // El puerto debe ser un número, por ejemplo 587 o 465.
        try {
            Integer.parseInt(puerto);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "CAMPUS_SMTP_PORT debe ser un número.", e);
        }

        // Una contraseña de aplicación puede contener espacios visualmente.
        String contrasenaConfigurada = obtenerConfiguracion(
                "CAMPUS_SMTP_PASSWORD", "smtp.password", null);
        // Los elimina antes de autenticarla, evitando rechazos por espacios.
        String contrasena = contrasenaConfigurada.replaceAll("\\s+", "");
        // Gmail exige una contraseña de aplicación con al menos 16 caracteres.
        if (contrasena.length() < 16) {
            throw new IllegalStateException(
                    "CAMPUS_SMTP_PASSWORD debe contener una contraseña "
                            + "de aplicación válida.");
        }
        // Si no se configura FROM, se utiliza el mismo usuario SMTP.
        this.remitente = obtenerConfiguracion(
                "CAMPUS_SMTP_FROM", "smtp.from", usuario);

        // Contenedor de propiedades que Jakarta Mail usa para crear la sesión.
        Properties propiedades = new Properties();
        // Servidor SMTP al que se realizará la conexión.
        propiedades.put("mail.smtp.host", host);
        // Puerto TCP del servidor SMTP.
        propiedades.put("mail.smtp.port", puerto);
        // Indica que el servidor exige autenticación con usuario y contraseña.
        propiedades.put("mail.smtp.auth", "true");
        // Solicita cifrado STARTTLS para proteger la comunicación.
        propiedades.put("mail.smtp.starttls.enable", "true");
        // Impide continuar si el servidor no puede iniciar STARTTLS.
        propiedades.put("mail.smtp.starttls.required", "true");

        // Límites para evitar que una conexión SMTP quede esperando indefinidamente.
        propiedades.put("mail.smtp.connectiontimeout", "15000");
        propiedades.put("mail.smtp.timeout", "15000");
        propiedades.put("mail.smtp.writetimeout", "15000");

        // Crea la sesión SMTP una sola vez y proporciona sus credenciales.
        this.session = Session.getInstance(
                propiedades,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                            getPasswordAuthentication() {
                        // Jakarta Mail usa este método al necesitar autenticarse.
                        return new PasswordAuthentication(
                                usuario,
                                contrasena
                        );
                    }
                }
        );
    }

    /**
     * Construye un mensaje MIME y lo entrega al servidor SMTP.
     *
     * @param destinatario correo electrónico que recibirá el mensaje
     * @param asunto encabezado o título visible del correo
     * @param contenido texto del cuerpo del correo
     * @throws MessagingException si falta el destinatario, el mensaje es inválido
     *         o el servidor SMTP rechaza la conexión o el envío
     */
    public void enviar(
            String destinatario,
            String asunto,
            String contenido) throws MessagingException {

        // No intenta enviar si el usuario no tiene correo registrado.
        if (destinatario == null || destinatario.isBlank()) {
            throw new MessagingException(
                    "El usuario no tiene correo registrado.");
        }

        // Crea el mensaje MIME usando la sesión SMTP ya configurada.
        MimeMessage correo = new MimeMessage(session);
        // Establece la dirección configurada como remitente.
        correo.setFrom(new InternetAddress(remitente, true));
        // Establece el usuario como destinatario principal del correo.
        correo.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress(destinatario.trim(), true)
        );
        // Codifica el asunto como UTF-8 para aceptar caracteres especiales.
        correo.setSubject(asunto, "UTF-8");
        // Codifica el cuerpo como texto UTF-8.
        correo.setText(contenido, "UTF-8");

        // Conecta y entrega el mensaje mediante la sesión SMTP configurada.
        Transport.send(correo);
    }

    /**
     * Lee el archivo smtp.properties incluido dentro del recurso del proyecto.
     *
     * El archivo puede estar ausente; en ese caso se conservará una colección
     * vacía y obtenerConfiguracion() buscará las demás fuentes disponibles.
     *
     * @return propiedades cargadas desde el archivo, o un conjunto vacío
     */
    private Properties cargarConfiguracion() {
        Properties propiedades = new Properties();
        try (InputStream entrada = CorreoService.class
                .getResourceAsStream("/smtp.properties")) {
            if (entrada != null) {
                propiedades.load(entrada);
            }
        } catch (IOException e) {
            // Un archivo presente pero ilegible impide usar esa fuente de configuración.
            throw new IllegalStateException(
                    "No se pudo leer el archivo smtp.properties.", e);
        }
        return propiedades;
    }

    /**
     * Busca una opción de configuración siguiendo un orden de prioridad.
     *
     * El orden es:
     * 1. variable de entorno;
     * 2. propiedad del sistema Java;
     * 3. propiedad de smtp.properties;
     * 4. valor predeterminado recibido por el método.
     *
     * Si ninguna fuente contiene el dato obligatorio, lanza una excepción que
     * indica exactamente qué configuración falta.
     *
     * @param variableEntorno nombre de la variable del sistema operativo
     * @param claveArchivo nombre usado en smtp.properties y System properties
     * @param valorPredeterminado valor opcional usado cuando no se configura el dato
     * @return valor validado y sin espacios externos
     * @throws IllegalStateException si falta una configuración obligatoria
     */
    private String obtenerConfiguracion(String variableEntorno,
            String claveArchivo, String valorPredeterminado) {
        // La variable de entorno tiene prioridad sobre los demás métodos.
        String valor = System.getenv(variableEntorno);
        if (valor == null || valor.isBlank()) {
            // Permite probar o sobrescribir valores mediante -Dclave=valor.
            valor = System.getProperty(claveArchivo);
        }
        if (valor == null || valor.isBlank()) {
            // Alternativa local más sencilla para el proyecto Java.
            valor = configuracionArchivo.getProperty(claveArchivo);
        }
        if (valor == null || valor.isBlank()) {
            if (valorPredeterminado != null) {
                return valorPredeterminado.trim();
            }
            // No existe una alternativa válida para una configuración obligatoria.
            throw new IllegalStateException(
                    "Configura " + claveArchivo + ", la variable de entorno "
                            + variableEntorno + ", o la propiedad del sistema "
                            + claveArchivo + ".");
        }
        return valor.trim();
    }
}
