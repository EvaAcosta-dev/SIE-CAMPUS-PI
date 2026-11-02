package com.integrador.campus.view.Modulo;

import com.integrador.campus.controller.NotificacionController;
import com.integrador.campus.controller.NotificacionController.ResultadoPublicacion;
import com.integrador.campus.model.Notificacion;
import com.integrador.campus.model.Rol;
import com.integrador.campus.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Vista Swing del módulo de notificaciones internas.
 *
 * Esta clase pertenece a la capa View de MVC. Muestra formularios, validaciones
 * visuales, mensajes, confirmaciones y la bandeja. La lógica de acceso a datos
 * se delega a NotificacionController y las operaciones SQL se realizan por
 * medio de NotificacionDao.
 */
public class NotificacionInternaPanel extends JPanel {

    // Opciones disponibles para seleccionar el alcance de la publicación.
    private static final String UNA_PERSONA = "Un usuario";
    private static final String POR_ROL = "Usuarios por rol";
    private static final String TODOS = "Todos los usuarios activos";
    private static final String SIN_FILTRO = "Sin filtro";

    // Controlador que conecta esta vista con los datos y servicios del sistema.
    private final NotificacionController controlador = new NotificacionController();
    private final int idUsuarioActual;
    private final boolean puedeCrear;
    private final JComboBox<String> alcance = new JComboBox<>(new String[]{UNA_PERSONA, POR_ROL, TODOS});
    private final JComboBox<Usuario> usuarioDestino = new JComboBox<>();
    private final JComboBox<Object> rolDestino = new JComboBox<>();
    private final JComboBox<String> tipo = new JComboBox<>( new String[]{"CONVIVENCIA", "ASISTENCIA", "ACADEMICO", "CIRCULAR"});
    private final JTextField titulo = new JTextField();
    private final JTextArea mensaje = new JTextArea(4, 30);
    private final JCheckBox enviarCorreo = new JCheckBox("Enviar también por correo electrónico", true);
    private final JButton crear = new JButton("Publicar notificación");
    private final JButton actualizar = new JButton("Actualizar bandeja");
    private final JButton abrirBandeja = new JButton("Abrir mis notificaciones");
    private final JLabel estado = new JLabel(" ");
    private final JPanel selectorDestino = new JPanel(new CardLayout());
    private JDialog ventanaBandeja;
    private JPanel listaTarjetas;
    private JLabel estadoBandeja;

    /**
     * Construye el panel según los permisos del usuario.
     *
     * Los roles administradores, de registro académico y docentes pueden
     * publicar notificaciones. Todos los usuarios pueden consultar y marcar
     * como leídas las notificaciones de su propia bandeja.
     *
     * @param rolUsuario rol del usuario conectado
     * @param idUsuarioActual identificador usado para filtrar la bandeja
     */
    public NotificacionInternaPanel(String rolUsuario, int idUsuarioActual) {
        this.idUsuarioActual = idUsuarioActual;
        String rol = rolUsuario == null ? "" : rolUsuario.trim().toUpperCase(Locale.ROOT);
        puedeCrear = rol.equals("ADMIN")
                || rol.equals("REGISTRO ACADEMICO")
                || rol.equals("DOCENTE");

        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(12, 12, 12, 12));
        usuarioDestino.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                String texto = value instanceof Usuario
                        ? ((Usuario) value).getNombre() + " "
                                + ((Usuario) value).getApellido() + " - "
                                + ((Usuario) value).getCorreo()
                        : "";
                return super.getListCellRendererComponent(
                        list, texto, index, isSelected, cellHasFocus);
            }
        });

        if (puedeCrear) {
            add(crearFormulario(), BorderLayout.NORTH);
            cargarDestinatarios();
        }
        add(crearBandeja(), BorderLayout.CENTER);
        configurarEventos();
    }

    public void mostrarBandejaInterna(Component propietario) {
        mostrarVentanaBandeja(propietario);
    }

    /**
     * Construye visualmente el formulario usado para publicar avisos.
     *
     * Permite seleccionar el destinatario, el tipo, el título, el mensaje y
     * si también debe enviarse una copia por correo electrónico.
     *
     * @return panel Swing listo para agregarse al dashboard
     */
    private JPanel crearFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder( BorderFactory.createLineBorder(new Color(228, 233, 241)),
                new EmptyBorder(12, 15, 12, 15)));

        JPanel individual = new JPanel(new BorderLayout(8, 0));
        individual.setOpaque(false);
        individual.add(new JLabel("Usuario:"), BorderLayout.WEST);
        individual.add(usuarioDestino, BorderLayout.CENTER);

        JPanel grupo = new JPanel(new BorderLayout(8, 0));
        grupo.setOpaque(false);
        grupo.add(new JLabel("Rol:"), BorderLayout.WEST);
        grupo.add(rolDestino, BorderLayout.CENTER);
        selectorDestino.add(individual, UNA_PERSONA);
        selectorDestino.add(grupo, POR_ROL);
        selectorDestino.add(new JLabel("Se enviará a todos los usuarios activos."),
                SIN_FILTRO);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 5, 4, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        agregarCampo(formulario, c, 0, "Destinatarios:", alcance);
        agregarCampo(formulario, c, 1, "Seleccionar:", selectorDestino);
        agregarCampo(formulario, c, 2, "Tipo:", tipo);
        agregarCampo(formulario, c, 3, "Título:", titulo);

        mensaje.setLineWrap(true);
        mensaje.setWrapStyleWord(true);
        JScrollPane contenido = new JScrollPane(mensaje);
        contenido.setPreferredSize(new Dimension(400, 75));
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        formulario.add(contenido, c);
        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(new JLabel("Mensaje:"), c);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.setOpaque(false);
        enviarCorreo.setOpaque(false);
        acciones.add(enviarCorreo);
        acciones.add(crear);
        acciones.add(estado);
        c.gridx = 1;
        c.gridy = 5;
        formulario.add(acciones, c);
        return formulario;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints c, int fila,
            String etiqueta, JComponent campo) {
        // Coloca la etiqueta en la primera columna y el componente en la segunda.
        c.gridx = 0;
        c.gridy = fila;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.weightx = 1;
        panel.add(campo, c);
    }

    private JPanel crearBandeja() {
        // Construye la tarjeta visible desde el dashboard para abrir la bandeja.
        JPanel bandeja = new JPanel(new GridBagLayout());
        bandeja.setOpaque(false);
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(228, 233, 241)),
                new EmptyBorder(24, 28, 24, 28)));
        JLabel encabezado = new JLabel("Tu bandeja de notificaciones");
        encabezado.setFont(encabezado.getFont().deriveFont(Font.BOLD, 18f));
        JLabel descripcion = new JLabel(
                "<html>Abre la ventana para consultar tus avisos y revisar cuáles siguen sin leer.</html>");
        descripcion.setBorder(new EmptyBorder(10, 0, 16, 0));
        tarjeta.add(encabezado);
        tarjeta.add(descripcion);
        tarjeta.add(abrirBandeja);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        bandeja.add(tarjeta, c);
        return bandeja;
    }

    private void configurarEventos() {
        // Actualización manual, apertura de la ventana y cambio de selector.
        actualizar.addActionListener(e -> cargarBandeja());
        abrirBandeja.addActionListener(e ->
                mostrarVentanaBandeja(SwingUtilities.getWindowAncestor(this)));
        alcance.addActionListener(e -> mostrarSelectorDestino());
        if (puedeCrear) {
            crear.addActionListener(e -> publicar());
        }
    }

    private void mostrarSelectorDestino() {
        // Cambia visualmente el campo de selección entre usuario, rol o todos.
        String seleccion = (String) alcance.getSelectedItem();
        String tarjeta = TODOS.equals(seleccion) ? SIN_FILTRO : seleccion;
        ((CardLayout) selectorDestino.getLayout()).show(selectorDestino, tarjeta);
    }

    private void cargarDestinatarios() {
        // Carga usuarios y roles en segundo plano para no bloquear la interfaz.
        crear.setEnabled(false);
        estado.setText("Cargando usuarios y roles...");
        new SwingWorker<Object[], Void>() {
            @Override
            protected Object[] doInBackground() throws SQLException {
                return controlador.listarUsuariosYRoles();
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void done() {
                try {
                    Object[] resultado = get();
                    List<Usuario> usuarios = (List<Usuario>) resultado[0];
                    List<Rol> roles = (List<Rol>) resultado[1];
                    for (Usuario usuario : usuarios) {
                        usuarioDestino.addItem(usuario);
                    }
                    rolDestino.addItem("Todos los roles activos");
                    for (Rol rol : roles) {
                        rolDestino.addItem(rol);
                    }
                    estado.setText(usuarios.isEmpty()
                            ? "No hay usuarios activos." : " ");
                    crear.setEnabled(!usuarios.isEmpty());
                } catch (Exception e) {
                    estado.setText(" ");
                    mostrarError("No se pudieron cargar los destinatarios: " + causa(e));
                }
            }
        }.execute();
    }

    private void publicar() {
        // Valida los datos visibles antes de delegar la operación al controlador.
        String asunto = titulo.getText().trim();
        String texto = mensaje.getText().trim();
        if (asunto.isEmpty() || texto.isEmpty()) {
            mostrarError("Escribe el título y el mensaje de la notificación.");
            return;
        }
        if (asunto.length() > 100) {
            mostrarError("El título no puede superar los 100 caracteres.");
            return;
        }

        String seleccion = (String) alcance.getSelectedItem();
        Usuario destinoUnico = (Usuario) usuarioDestino.getSelectedItem();
        Object seleccionRol = rolDestino.getSelectedItem();
        // Resuelve el rol seleccionado solo para pedir al controlador sus usuarios.
        Integer idRol = seleccionRol instanceof Rol
                ? ((Rol) seleccionRol).getIdRol() : null;
        if (UNA_PERSONA.equals(seleccion) && destinoUnico == null) {
            mostrarError("No hay un usuario activo seleccionado.");
            return;
        }

        String confirmacion = UNA_PERSONA.equals(seleccion)
                ? "¿Publicar esta notificación para " + nombreUsuario(destinoUnico) + "?"
                : POR_ROL.equals(seleccion)
                        ? "¿Publicar para los usuarios activos del rol seleccionado?"
                        : "¿Publicar para todos los usuarios activos?";
        if (JOptionPane.showConfirmDialog(this, confirmacion, "Confirmar publicación",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        crear.setEnabled(false);
        estado.setText("Publicando...");
        String tipoSeleccionado = (String) tipo.getSelectedItem();
        boolean enviarPorCorreo = enviarCorreo.isSelected();
        // SwingWorker ejecuta la operación fuera del hilo gráfico.
        new SwingWorker<ResultadoPublicacion, Void>() {
            @Override
            protected ResultadoPublicacion doInBackground() throws SQLException {
                List<Usuario> destinatarios;
                if (UNA_PERSONA.equals(seleccion)) {
                    destinatarios = List.of(destinoUnico);
                } else {
                    destinatarios = controlador.listarUsuariosActivos(
                            POR_ROL.equals(seleccion) ? idRol : null);
                }
                return controlador.publicar(destinatarios, asunto, texto,
                        tipoSeleccionado, enviarPorCorreo);
            }

            @Override
            protected void done() {
                crear.setEnabled(true);
                try {
                    ResultadoPublicacion resultado = get();
                    int cantidad = resultado.getCantidadInternas();
                    if (cantidad == 0) {
                        estado.setText(" ");
                        mostrarError("No hay usuarios activos para ese destinatario o filtro.");
                        return;
                    }
                    if (enviarPorCorreo) {
                        estado.setText("Interna: " + cantidad
                                + " | Correos enviados: " + resultado.getCorreosEnviados()
                                + " | Errores: " + resultado.getErroresCorreo());
                        if (!resultado.getDetalleError().isBlank()) {
                            mostrarError("La notificación interna se publicó, pero "
                                    + "el correo no pudo enviarse: "
                                    + resultado.getDetalleError());
                        }
                    } else {
                        estado.setText("Publicada internamente para "
                                + cantidad + " usuario(s).");
                    }
                    titulo.setText("");
                    mensaje.setText("");
                    cargarBandeja();
                } catch (Exception e) {
                    estado.setText("No se pudo publicar.");
                    mostrarError("No se pudo guardar la notificación: " + causa(e));
                }
            }
        }.execute();
    }

    public void mostrarVentanaBandeja(Component propietario) {
        // Crea la ventana una sola vez y la reutiliza mientras siga abierta.
        if (ventanaBandeja == null || !ventanaBandeja.isDisplayable()) {
            Window owner = propietario instanceof Window
                    ? (Window) propietario : SwingUtilities.getWindowAncestor(this);
            ventanaBandeja = new JDialog(owner, "Mis notificaciones",
                    Dialog.ModalityType.MODELESS);
            ventanaBandeja.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            ventanaBandeja.setMinimumSize(new Dimension(560, 420));
            ventanaBandeja.setSize(720, 560);
            ventanaBandeja.setLayout(new BorderLayout(12, 12));
            ventanaBandeja.getRootPane().setBorder(new EmptyBorder(14, 14, 14, 14));

            JPanel encabezado = new JPanel(new BorderLayout(8, 8));
            JLabel tituloVentana = new JLabel("Mis notificaciones");
            tituloVentana.setFont(tituloVentana.getFont().deriveFont(Font.BOLD, 20f));
            encabezado.add(tituloVentana, BorderLayout.WEST);
            JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            acciones.add(estadoBandeja = new JLabel(" "));
            acciones.add(actualizar);
            encabezado.add(acciones, BorderLayout.EAST);

            listaTarjetas = new JPanel();
            listaTarjetas.setLayout(new BoxLayout(listaTarjetas, BoxLayout.Y_AXIS));
            listaTarjetas.setBackground(new Color(245, 247, 251));
            ventanaBandeja.add(encabezado, BorderLayout.NORTH);
            ventanaBandeja.add(new JScrollPane(listaTarjetas), BorderLayout.CENTER);
        }

        ventanaBandeja.setLocationRelativeTo(propietario);
        ventanaBandeja.setVisible(true);
        ventanaBandeja.toFront();
        cargarBandeja();
    }

    private void cargarBandeja() {
        // Obtiene las notificaciones mediante el controlador sin bloquear Swing.
        if (ventanaBandeja == null || !ventanaBandeja.isDisplayable()) {
            return;
        }
        actualizar.setEnabled(false);
        estadoBandeja.setText("Cargando...");
        listaTarjetas.removeAll();
        listaTarjetas.revalidate();
        listaTarjetas.repaint();

        new SwingWorker<List<Notificacion>, Void>() {
            @Override
            protected List<Notificacion> doInBackground() throws SQLException {
                return controlador.listarBandeja(idUsuarioActual);
            }

            @Override
            protected void done() {
                actualizar.setEnabled(true);
                try {
                    List<Notificacion> notificaciones = get();
                    int noLeidas = 0;
                    if (notificaciones.isEmpty()) {
                        JLabel vacio = new JLabel("No tienes notificaciones todavía.");
                        vacio.setBorder(new EmptyBorder(20, 10, 10, 10));
                        listaTarjetas.add(vacio);
                    }
                    for (Notificacion notificacion : notificaciones) {
                        listaTarjetas.add(crearTarjetaNotificacion(notificacion));
                        listaTarjetas.add(Box.createVerticalStrut(10));
                        if (!notificacion.isLeido()) {
                            noLeidas++;
                        }
                    }
                    estadoBandeja.setText(noLeidas + " sin leer");
                    listaTarjetas.revalidate();
                    listaTarjetas.repaint();
                } catch (Exception e) {
                    estadoBandeja.setText("Error al cargar");
                    JOptionPane.showMessageDialog(ventanaBandeja,
                            "No se pudo cargar tu bandeja: " + causa(e)
                                    + ". Verifica que exista la tabla notificaciones.",
                            "Notificaciones", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private JPanel crearTarjetaNotificacion(Notificacion notificacion) {
        // Presenta una notificación como una tarjeta con estado, fecha y mensaje.
        JPanel tarjeta = new JPanel(new BorderLayout(12, 8));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(notificacion.isLeido()
                        ? new Color(225, 230, 238) : new Color(75, 120, 185)),
                new EmptyBorder(12, 14, 12, 14)));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel tituloTarjeta = new JLabel(notificacion.getTitulo());
        tituloTarjeta.setFont(tituloTarjeta.getFont().deriveFont(Font.BOLD, 15f));
        JLabel metadatos = new JLabel(notificacion.getTipoEvento() + "  |  "
                + notificacion.getFechaEnvio() + "  |  "
                + (notificacion.isLeido() ? "Leída" : "Nueva"));
        metadatos.setForeground(new Color(100, 110, 125));
        JTextArea resumen = new JTextArea(notificacion.getMensaje());
        resumen.setEditable(false);
        resumen.setLineWrap(true);
        resumen.setWrapStyleWord(true);
        resumen.setOpaque(false);
        resumen.setRows(2);
        textos.add(tituloTarjeta);
        textos.add(Box.createVerticalStrut(4));
        textos.add(metadatos);
        textos.add(Box.createVerticalStrut(5));
        textos.add(resumen);

        JButton ver = new JButton("Ver detalle");
        ver.addActionListener(e -> mostrarDetalle(notificacion));
        tarjeta.add(textos, BorderLayout.CENTER);
        tarjeta.add(ver, BorderLayout.EAST);
        return tarjeta;
    }

    private void mostrarDetalle(Notificacion notificacion) {
        // Abre una ventana modal para leer el contenido completo del aviso.
        JDialog detalle = new JDialog(ventanaBandeja,
                notificacion.getTitulo(), Dialog.ModalityType.APPLICATION_MODAL);
        detalle.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        detalle.setSize(520, 360);
        detalle.setLocationRelativeTo(ventanaBandeja);
        detalle.setLayout(new BorderLayout(12, 12));
        detalle.getRootPane().setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel metadatos = new JLabel(notificacion.getTipoEvento() + "  |  "
                + notificacion.getFechaEnvio());
        JTextArea contenido = new JTextArea(notificacion.getMensaje());
        contenido.setEditable(false);
        contenido.setLineWrap(true);
        contenido.setWrapStyleWord(true);
        contenido.setCaretPosition(0);
        detalle.add(metadatos, BorderLayout.NORTH);
        detalle.add(new JScrollPane(contenido), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cerrar = new JButton("Cerrar");
        cerrar.addActionListener(e -> detalle.dispose());
        acciones.add(cerrar);
        if (!notificacion.isLeido()) {
            JButton marcar = new JButton("Marcar como leída");
            marcar.addActionListener(e -> marcarComoLeida(notificacion, detalle, marcar));
            acciones.add(marcar);
        }
        detalle.add(acciones, BorderLayout.SOUTH);
        detalle.setVisible(true);
    }

    private void marcarComoLeida(Notificacion notificacion, JDialog detalle, JButton boton) {
        // Usa el controlador para actualizar el estado sin ejecutar SQL en la vista.
        boton.setEnabled(false);
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws SQLException {
                return controlador.marcarComoLeida(
                        notificacion.getIdNotificacion(), idUsuarioActual);
            }

            @Override
            protected void done() {
                try {
                    if (!get()) {
                        JOptionPane.showMessageDialog(detalle,
                                "La notificación ya estaba leída o ya no está disponible.",
                                "Notificaciones", JOptionPane.INFORMATION_MESSAGE);
                    }
                    detalle.dispose();
                    cargarBandeja();
                } catch (Exception e) {
                    boton.setEnabled(true);
                    JOptionPane.showMessageDialog(detalle,
                            "No se pudo marcar como leída: " + causa(e),
                            "Notificaciones", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private String causa(Exception e) {
        // Extrae la causa real de una excepción para mostrar un mensaje útil.
        Throwable causa = e.getCause();
        return causa == null || causa.getMessage() == null ? e.getMessage() : causa.getMessage();
    }

    private String nombreUsuario(Usuario usuario) {
        // Forma el nombre completo mostrado en confirmaciones y listas.
        return usuario.getNombre() + " " + usuario.getApellido();
    }

    private String limitar(String texto, int maximo) {
        // Devuelve un texto seguro o limita su longitud para presentarlo.
        if (texto == null || texto.isBlank()) {
            return "Error de envío sin detalle.";
        }
        return texto.length() <= maximo ? texto : texto.substring(0, maximo);
    }

    private void mostrarError(String texto) {
        // Presenta una ventana gráfica de error con el mensaje recibido.
        JOptionPane.showMessageDialog(this, texto, "Notificaciones internas",
                JOptionPane.ERROR_MESSAGE);
    }
}
