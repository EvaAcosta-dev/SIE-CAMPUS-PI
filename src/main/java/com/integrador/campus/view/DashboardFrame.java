package com.integrador.campus.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
//import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import com.integrador.campus.Util.Permisos;
import com.integrador.campus.view.Modulo.UsuarioPanel;
import com.integrador.campus.view.Modulo.CursoMateriaPanel;
import com.integrador.campus.view.Modulo.EstudiantePanel;
import com.integrador.campus.view.Modulo.DocentePanel;

public class DashboardFrame extends JFrame {
    private final Color azulOscuro = new Color(25, 45, 80);
    private final Color azulMedio = new Color(44, 78, 125);
    private final Color fondo = new Color(245, 247, 251);
    private final Color texto = new Color(45, 55, 72);
    private final Color blanco = Color.WHITE;

    private final JPanel contenido;
    private final JLabel lblTitulo;
    private final JLabel lblUsuario;
    private final Map<String, JButton> botonesMenu = new LinkedHashMap<>();
    private final Map<String, Component> espacios = new LinkedHashMap<>();
    private final String rol;
    
    //instancia de los paneles de módulos
    private final EstudiantePanel estudiantePanel = new EstudiantePanel();
    private final UsuarioPanel usuarioPanel = new UsuarioPanel();
    private final CursoMateriaPanel cursosMateriasPanel = new CursoMateriaPanel();
    private final DocentePanel docentePanel = new DocentePanel();


    public DashboardFrame(String nombreUsuario, String rol) {
        this.rol = rol;
        
        setTitle("Campus | Panel principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new Dimension(950, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Menú lateral
        JPanel menu = crearMenu(rol);
        add(menu, BorderLayout.WEST);

        // Contenedor central
        JPanel principal = new JPanel(new BorderLayout(0, 22));
        principal.setBackground(fondo);
        principal.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Encabezado
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        lblTitulo = new JLabel("Panel principal");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 27));
        lblTitulo.setForeground(azulOscuro);

        
        textos.add(lblTitulo);
        textos.add(Box.createVerticalStrut(7));

        lblUsuario = new JLabel(
                "<html><b>" + escapar(nombreUsuario)
                + "</b><br><span style='font-size:10px;color:#777777;'>"
                + escapar(rol) + "</span></html>"
        );
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblUsuario.setForeground(azulOscuro);
        lblUsuario.setBorder(new EmptyBorder(5, 15, 5, 5));

        encabezado.add(textos, BorderLayout.WEST);
        encabezado.add(lblUsuario, BorderLayout.EAST);

        // Área que cambia según la opción seleccionada
        contenido = new JPanel(new BorderLayout(0, 20));
        contenido.setOpaque(false);

        mostrarInicio();

        principal.add(encabezado, BorderLayout.NORTH);
        principal.add(contenido, BorderLayout.CENTER);

        add(principal, BorderLayout.CENTER);
    }

    private JPanel crearMenu(String rol) {
        JPanel menu = new JPanel();
        menu.setBackground(azulOscuro);
        menu.setPreferredSize(new Dimension(235, 0));
        menu.setBorder(new EmptyBorder(15, 15, 20, 15));//MARGEN SUPERIOR
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("CAMPUS");
        logo.setFont(new Font("SansSerif", Font.BOLD, 25));
        logo.setForeground(blanco);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lema = new JLabel("Gestión escolar");
        lema.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lema.setForeground(new Color(200, 215, 235));
        lema.setAlignmentX(Component.LEFT_ALIGNMENT);

        menu.add(logo);
        menu.add(Box.createVerticalStrut(3));//(Espacio entre CAMPUS y Gestión escolar)
        menu.add(lema);
        menu.add(Box.createVerticalStrut(25));// (Espacio antes de MENÚ PRINCIPAL)

        JLabel etiqueta = new JLabel("MENÚ PRINCIPAL");
        etiqueta.setForeground(new Color(170, 190, 220));
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 10));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);

        menu.add(etiqueta);
        menu.add(Box.createVerticalStrut(8));//(Espacio antes del primer botón)

        agregarOpcion(menu, "Inicio");
        agregarOpcion(menu, "Usuarios");
        agregarOpcion(menu, "Estudiantes");
        agregarOpcion(menu, "Docentes");
        agregarOpcion(menu, "Acudientes");//padres y relacion estudiante
        agregarOpcion(menu, "Academico");//ano escolar, grados, jornadas, niveles
        agregarOpcion(menu, "Cursos y materias");
        agregarOpcion(menu, "Matrículas");
        agregarOpcion(menu, "Asignaciones docentes");
        agregarOpcion(menu, "Notas");
        agregarOpcion(menu, "Boletines");
        agregarOpcion(menu, "Asistencia");
        agregarOpcion(menu, "Convivencia");
        agregarOpcion(menu, "Notificaciones");

        menu.add(Box.createVerticalGlue());

        JButton salir = crearBotonMenu("Cerrar sesión");
        salir.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas cerrar la sesión?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                dispose();
                // Aquí se abrirá el LoginFrame al integrar
                // el flujo de autenticación.
            }
        });

        menu.add(salir);

        // El menú inicial muestra únicamente Inicio.
        // Los permisos reales deben verificarse también
        // en el controlador y en las operaciones de datos.
        aplicarPermisosVisuales(rol);

        return menu;
    }

        private void agregarOpcion(JPanel menu, String nombre) {
        JButton boton = crearBotonMenu(nombre);

        boton.addActionListener(e -> {
                if ("Inicio".equals(nombre)) {
                lblTitulo.setText("Panel principal");
                mostrarInicio();
                } else {
                mostrarModulo(nombre);
                }
        });

        Component espacio = Box.createVerticalStrut(5);
        espacios.put(nombre, espacio);

        botonesMenu.put(nombre, boton);
        menu.add(boton);
        menu.add(espacio);
        }

    private JButton crearBotonMenu(String nombre) {
        JButton boton = new JButton(nombre);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        boton.setForeground(blanco);
        boton.setBackground(azulOscuro);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setMaximumSize(new Dimension(
                Integer.MAX_VALUE, 32
        ));
        boton.setPreferredSize(new Dimension(200, 32));
        boton.setBorder(new EmptyBorder(6, 12, 6, 8));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(azulMedio);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(azulOscuro);
            }
        });

        return boton;
    }

    private void mostrarInicio() {
        contenido.removeAll();

        JPanel tarjetas = new JPanel(new GridLayout(2, 2, 18, 18));
        tarjetas.setOpaque(false);

        tarjetas.add(crearTarjeta(
                "Estudiantes", "—",
                "Total registrado en el sistema"
        ));
        tarjetas.add(crearTarjeta(
                "Docentes", "—",
                "Personal docente registrado"
        ));
        tarjetas.add(crearTarjeta(
                "Cursos", "—",
                "Cursos académicos configurados"
        ));
        tarjetas.add(crearTarjeta(
                "Asignaturas", "—",
                "Materias registradas"
        ));

        JPanel bienvenida = new JPanel();
        bienvenida.setLayout(new BoxLayout(
                bienvenida, BoxLayout.Y_AXIS
        ));
        bienvenida.setBackground(blanco);
        bienvenida.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(228, 233, 241)
                ),
                new EmptyBorder(22, 25, 22, 25)
        ));

        JLabel tituloBienvenida = new JLabel(
                "Bienvenido a Campus"
        );
        tituloBienvenida.setFont(
                new Font("SansSerif", Font.BOLD, 20)
        );
        tituloBienvenida.setForeground(azulOscuro);

        JLabel descripcion = new JLabel(
                "<html>Administra la información escolar desde un "
                + "solo lugar.<br>Selecciona un módulo en el menú "
                + "lateral para comenzar.</html>"
        );
        descripcion.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );
        descripcion.setForeground(texto);

        bienvenida.add(tituloBienvenida);
        bienvenida.add(Box.createVerticalStrut(12));
        bienvenida.add(descripcion);

        contenido.add(tarjetas, BorderLayout.CENTER);
        contenido.add(bienvenida, BorderLayout.SOUTH);

        contenido.revalidate();
        contenido.repaint();
    }

    private JPanel crearTarjeta(
            String titulo, String valor, String descripcion) {

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(
                tarjeta, BoxLayout.Y_AXIS
        ));
        tarjeta.setBackground(blanco);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(228, 233, 241)
                ),
                new EmptyBorder(20, 22, 20, 22)
        ));

        JLabel lblNombre = new JLabel(titulo);
        lblNombre.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );
        lblNombre.setForeground(texto);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(
                new Font("SansSerif", Font.BOLD, 32)
        );
        lblValor.setForeground(azulMedio);

        JLabel lblDescripcion = new JLabel(
                "<html>" + descripcion + "</html>"
        );
        lblDescripcion.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        lblDescripcion.setForeground(
                new Color(115, 125, 140)
        );

        tarjeta.add(lblNombre);
        tarjeta.add(Box.createVerticalStrut(18));
        tarjeta.add(lblValor);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblDescripcion);

        return tarjeta;
    }

    private void mostrarModulo(String nombre) {
        
        // Segunda barrera, por si alguien llega sin pasar por el botón
        if (!Permisos.puedeAcceder(rol, nombre)) {
            JOptionPane.showMessageDialog(this,
                    "No tienes permiso para acceder a este módulo.",
                    "Acceso denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        lblTitulo.setText(nombre);
        contenido.removeAll();

        switch (nombre) {
            case "Usuarios":
                contenido.add(usuarioPanel, BorderLayout.CENTER);
                break;
            case "Cursos y materias":
                contenido.add(cursosMateriasPanel, BorderLayout.CENTER);
                break;
                case "Estudiantes":
                contenido.add(estudiantePanel, BorderLayout.CENTER);
                break;
            case "Docentes":
                contenido.add(docentePanel, BorderLayout.CENTER);
                break;
            default:
                mostrarInicio();
                return;
        }

        contenido.revalidate();
        contenido.repaint();
    }


    private void aplicarPermisosVisuales(String rol) {
        for (Map.Entry<String, JButton> entrada : botonesMenu.entrySet()) {
            boolean permitido = Permisos.puedeAcceder(rol, entrada.getKey());
            entrada.getValue().setVisible(permitido);

            Component espacio = espacios.get(entrada.getKey());
            if (espacio != null) {
                espacio.setVisible(permitido);
            }
        }
}

    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    public UsuarioPanel getUsuarioPanel() {
        return usuarioPanel;
    }
    public EstudiantePanel getEstudiantePanel() {
        return estudiantePanel;
    }
    public DocentePanel getDocentePanel() {
        return docentePanel;
    }

}