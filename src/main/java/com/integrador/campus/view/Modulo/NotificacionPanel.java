package com.integrador.campus.view.Modulo;

import com.integrador.campus.dao.NotificacionDao;
import com.integrador.campus.model.CorreoEnvio;
import com.integrador.campus.model.Rol;
import com.integrador.campus.model.Usuario;
import com.integrador.campus.service.CorreoService;

import jakarta.mail.MessagingException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class NotificacionPanel extends JPanel {

    private static final String MANUAL = "Un destinatario";
    private static final String MASIVO = "Envío masivo por filtro";

    private final NotificacionDao dao = new NotificacionDao();
    private final JComboBox<String> modoEnvio = new JComboBox<>(new String[]{MANUAL, MASIVO});
    private final JTextField correoManual = new JTextField();
    private final JComboBox<Object> filtroRol = new JComboBox<>();
    private final JTextField asunto = new JTextField();
    private final JTextArea mensaje = new JTextArea(6, 30);
    private final JButton enviar = new JButton("Enviar correo");
    private final JButton actualizarHistorial = new JButton("Actualizar historial");
    private final JLabel estado = new JLabel(" ");
    private final JPanel destinatarioCards = new JPanel(new CardLayout());
    private final DefaultTableModel modeloHistorial = new DefaultTableModel(
            new Object[]{"Fecha", "Destinatario", "Asunto", "Estado", "Detalle"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaHistorial = new JTable(modeloHistorial);
    private final NotificacionInternaPanel panelInterno;
    private final boolean puedeEnviar;

    public NotificacionPanel(String rolUsuario, int idUsuario) {
        String rol = rolUsuario == null ? "" : rolUsuario.trim().toUpperCase(Locale.ROOT);
        puedeEnviar = rol.equals("ADMIN")
                || rol.equals("REGISTRO ACADEMICO")
                || rol.equals("DOCENTE");

        setLayout(new BorderLayout(12, 12));
        setBackground(new Color(245, 247, 251));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JTabbedPane pestañas = new JTabbedPane();
        panelInterno = new NotificacionInternaPanel(rolUsuario, idUsuario);
        pestañas.addTab("Notificaciones internas", panelInterno);
        if (puedeEnviar) {
            JPanel panelCorreo = new JPanel(new BorderLayout(12, 12));
            panelCorreo.setBackground(new Color(245, 247, 251));
            panelCorreo.add(crearFormulario(), BorderLayout.NORTH);
            panelCorreo.add(crearHistorial(), BorderLayout.CENTER);
            pestañas.addTab("Correo electrónico", panelCorreo);
            configurarEventos();
            cargarRoles();
            cargarHistorial();
        }
        add(pestañas, BorderLayout.CENTER);
    }

    public void mostrarBandejaInterna(Component propietario) {
        panelInterno.mostrarVentanaBandeja(propietario);
    }

    private JPanel crearFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(228, 233, 241)),
                new EmptyBorder(16, 18, 16, 18)));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;

        agregarCampo(formulario, c, 0, "Destinatarios:", modoEnvio);

        JPanel manual = new JPanel(new BorderLayout(8, 0));
        manual.setOpaque(false);
        manual.add(new JLabel("Correo:"), BorderLayout.WEST);
        manual.add(correoManual, BorderLayout.CENTER);
        JPanel masivo = new JPanel(new BorderLayout(8, 0));
        masivo.setOpaque(false);
        masivo.add(new JLabel("Rol:"), BorderLayout.WEST);
        masivo.add(filtroRol, BorderLayout.CENTER);
        destinatarioCards.add(manual, MANUAL);
        destinatarioCards.add(masivo, MASIVO);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        formulario.add(destinatarioCards, c);

        agregarCampo(formulario, c, 2, "Asunto:", asunto);

        mensaje.setLineWrap(true);
        mensaje.setWrapStyleWord(true);
        JScrollPane scrollMensaje = new JScrollPane(mensaje);
        scrollMensaje.setPreferredSize(new Dimension(400, 110));
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        formulario.add(scrollMensaje, c);
        c.gridx = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(new JLabel("Mensaje:"), c);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.setOpaque(false);
        acciones.add(enviar);
        acciones.add(estado);
        c.gridx = 1;
        c.gridy = 4;
        formulario.add(acciones, c);
        return formulario;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints c, int fila,
            String etiqueta, JComponent campo) {
        c.gridy = fila;
        c.gridx = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.weightx = 1;
        panel.add(campo, c);
    }

    private JPanel crearHistorial() {
        JPanel historial = new JPanel(new BorderLayout(8, 8));
        historial.setOpaque(false);
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(new JLabel("Historial de correos (últimos 100)"), BorderLayout.WEST);
        encabezado.add(actualizarHistorial, BorderLayout.EAST);
        tablaHistorial.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        historial.add(encabezado, BorderLayout.NORTH);
        historial.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        return historial;
    }

    private void configurarEventos() {
        modoEnvio.addActionListener(e -> mostrarModoSeleccionado());
        enviar.addActionListener(e -> enviarCorreos());
        actualizarHistorial.addActionListener(e -> cargarHistorial());
        filtroRol.addActionListener(e -> {
            if (MASIVO.equals(modoEnvio.getSelectedItem())) {
                actualizarCantidadDestinatarios();
            }
        });
    }

    private void mostrarModoSeleccionado() {
        CardLayout cards = (CardLayout) destinatarioCards.getLayout();
        cards.show(destinatarioCards, (String) modoEnvio.getSelectedItem());
        actualizarCantidadDestinatarios();
    }

    private void cargarRoles() {
        new SwingWorker<List<Rol>, Void>() {
            @Override
            protected List<Rol> doInBackground() throws SQLException {
                return dao.listarRoles();
            }

            @Override
            protected void done() {
                try {
                    filtroRol.addItem("Todos los roles activos");
                    for (Rol rol : get()) {
                        filtroRol.addItem(rol);
                    }
                    actualizarCantidadDestinatarios();
                } catch (Exception e) {
                    mostrarError("No se pudieron cargar los roles: " + causa(e));
                }
            }
        }.execute();
    }

    private void actualizarCantidadDestinatarios() {
        if (!MASIVO.equals(modoEnvio.getSelectedItem()) || filtroRol.getItemCount() == 0) {
            return;
        }
        enviar.setEnabled(false);
        estado.setText("Consultando destinatarios...");
        Object seleccion = filtroRol.getSelectedItem();
        Integer idRol = seleccion instanceof Rol ? ((Rol) seleccion).getIdRol() : null;

        new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() throws SQLException {
                return dao.listarDestinatarios(idRol).size();
            }

            @Override
            protected void done() {
                try {
                    int cantidad = get();
                    estado.setText(cantidad + " destinatario(s) activo(s)");
                    enviar.setEnabled(true);
                } catch (Exception e) {
                    estado.setText(" ");
                    mostrarError("No se pudieron consultar destinatarios: " + causa(e));
                }
            }
        }.execute();
    }

    private void enviarCorreos() {
        String titulo = asunto.getText().trim();
        String contenido = mensaje.getText().trim();
        boolean esMasivo = MASIVO.equals(modoEnvio.getSelectedItem());
        String correo = correoManual.getText().trim();

        if (titulo.isEmpty() || contenido.isEmpty()) {
            mostrarError("Escribe el asunto y el mensaje.");
            return;
        }
        if (!esMasivo && !correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            mostrarError("Escribe una dirección de correo válida.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                esMasivo ? "¿Enviar el correo a todos los usuarios activos del filtro seleccionado?"
                        : "¿Enviar el correo a " + correo + "?",
                "Confirmar envío", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        enviar.setEnabled(false);
        modoEnvio.setEnabled(false);
        estado.setText("Preparando envío...");
        Object seleccion = filtroRol.getSelectedItem();
        Integer idRol = esMasivo && seleccion instanceof Rol
                ? ((Rol) seleccion).getIdRol() : null;

        new SwingWorker<int[], Void>() {
            @Override
            protected int[] doInBackground() throws Exception {
                CorreoService servicio = new CorreoService();
                List<Usuario> destinatarios = esMasivo
                        ? dao.listarDestinatarios(idRol) : List.of();
                if (!esMasivo && destinatarios.isEmpty()) {
                    Usuario manual = new Usuario();
                    manual.setCorreo(correo);
                    destinatarios = List.of(manual);
                }
                if (destinatarios.isEmpty()) {
                    throw new IllegalStateException("No hay destinatarios activos con correo.");
                }

                int enviados = 0;
                int errores = 0;
                for (Usuario destinatario : destinatarios) {
                    CorreoEnvio registro = new CorreoEnvio();
                    registro.setIdUsuarioDestino(destinatario.getIdUsuario() > 0
                            ? (long) destinatario.getIdUsuario() : null);
                    registro.setCorreoDestino(destinatario.getCorreo());
                    registro.setAsunto(titulo);
                    registro.setMensaje(contenido);
                    try {
                        servicio.enviar(destinatario.getCorreo(), titulo, contenido);
                        registro.setEstado("ENVIADO");
                        enviados++;
                    } catch (MessagingException | IllegalArgumentException e) {
                        registro.setEstado("ERROR");
                        registro.setDetalleError(limitar(e.getMessage(), 1000));
                        errores++;
                    }
                    dao.registrarEnvio(registro);
                }
                return new int[]{enviados, errores};
            }

            @Override
            protected void done() {
                enviar.setEnabled(true);
                modoEnvio.setEnabled(true);
                try {
                    int[] resultado = get();
                    estado.setText("Enviados: " + resultado[0] + " | Errores: " + resultado[1]);
                    cargarHistorial();
                    if (resultado[1] > 0) {
                        JOptionPane.showMessageDialog(NotificacionPanel.this,
                                "El envío terminó con " + resultado[0] + " correo(s) enviado(s) y "
                                        + resultado[1] + " error(es). Revisa el historial.",
                                "Envío completado", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(NotificacionPanel.this,
                                "Correo(s) enviado(s): " + resultado[0],
                                "Envío completado", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    estado.setText("No se completó el envío.");
                    mostrarError("No se pudo completar el envío: " + causa(e));
                }
            }
        }.execute();
    }

    private void cargarHistorial() {
        actualizarHistorial.setEnabled(false);
        new SwingWorker<List<CorreoEnvio>, Void>() {
            @Override
            protected List<CorreoEnvio> doInBackground() throws SQLException {
                return dao.listarHistorial(100);
            }

            @Override
            protected void done() {
                actualizarHistorial.setEnabled(true);
                try {
                    modeloHistorial.setRowCount(0);
                    for (CorreoEnvio envio : get()) {
                        modeloHistorial.addRow(new Object[]{
                            envio.getFechaEnvio(), envio.getCorreoDestino(), envio.getAsunto(),
                            envio.getEstado(), envio.getDetalleError()
                        });
                    }
                } catch (Exception e) {
                    mostrarError("No se pudo cargar el historial. Verifica que ejecutaste "
                            + "database/escuela_correo_envio.sql. " + causa(e));
                }
            }
        }.execute();
    }

    private String causa(Exception e) {
        Throwable causa = e.getCause();
        return causa == null || causa.getMessage() == null ? e.getMessage() : causa.getMessage();
    }

    private String limitar(String texto, int maximo) {
        if (texto == null) {
            return "Error de envío sin detalle.";
        }
        return texto.length() <= maximo ? texto : texto.substring(0, maximo);
    }

    private void mostrarError(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Notificaciones", JOptionPane.ERROR_MESSAGE);
    }
}
