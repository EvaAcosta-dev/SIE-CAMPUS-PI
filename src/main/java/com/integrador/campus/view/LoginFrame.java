
package com.integrador.campus.view;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;
import com.integrador.campus.dao.UsuarioDao;
import com.integrador.campus.model.Usuario;

public class LoginFrame extends JFrame {
   //instancia de usuario y dao
    Usuario usuario=new Usuario();
    UsuarioDao usuarios_dao=new UsuarioDao();

    private final Color azul = new Color(25, 45, 80);
    private final Color fondo = new Color(245, 247, 251);

    public JTextField txtCorreo;
    public JPasswordField txtContrasena;
    public JButton btnIngresar;

    public LoginFrame() {
        setTitle("Campus | Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(760, 500));

        JPanel principal = new JPanel(new GridLayout(1, 2));
        principal.setBackground(fondo);

        // Panel de presentación 
        
        JPanel presentacion = new JPanel() {
            private Image imagenFondo;

            {
                try {
                    // Carga la imagen 
                    java.net.URL imgUrl = getClass().getResource("/image/logocentral.jpg");
                    if (imgUrl != null) {
                        imagenFondo = javax.imageio.ImageIO.read(imgUrl);
                    } else {
                        System.out.println("ERROR: No se encontró el archivo en '/image/logocentral.jpg'");
                    }
                } catch (Exception e) {
                    System.out.println("Error al leer la imagen: " + e.getMessage());
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    // Habilitar renderizado de alta calidad para evitar pixelado
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                    int panelWidth = getWidth();
                    int panelHeight = getHeight();
                    int imageWidth = imagenFondo.getWidth(this);
                    int imageHeight = imagenFondo.getHeight(this);

                    // Calcular escalas para mantener la proporción sin deformar (Efecto "Cover")
                    double scaleX = (double) panelWidth / imageWidth;
                    double scaleY = (double) panelHeight / imageHeight;
                    double scale = Math.max(scaleX, scaleY); 

                    int newWidth = (int) (imageWidth * scale);
                    int newHeight = (int) (imageHeight * scale);

                    // Centrar la imagen en el panel
                    int x = (panelWidth - newWidth) / 2;
                    int y = (panelHeight - newHeight) / 2;

                    g2d.drawImage(imagenFondo, x, y, newWidth, newHeight, this);
                } else {
                    // Color de respaldo por si la imagen no carga
                    g.setColor(azul);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        presentacion.setLayout(new BoxLayout(presentacion, BoxLayout.Y_AXIS));
        presentacion.setBorder(BorderFactory.createEmptyBorder(65, 35, 35, 35));

        
        presentacion.setLayout(new BoxLayout(presentacion, BoxLayout.Y_AXIS));
        presentacion.setBorder(BorderFactory.createEmptyBorder(65, 35, 35, 35));


        // Panel de inicio de sesión
        JPanel formulario = new JPanel();
        formulario.setBackground(fondo);
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBorder(BorderFactory.createEmptyBorder(65, 45, 40, 45));

        JLabel titulo = new JLabel("¡Bienvenido!");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 27));
        titulo.setForeground(azul);

        JLabel subtitulo = new JLabel("Ingresa tus datos para continuar");
        subtitulo.setForeground(new Color(100, 110, 125));

        txtCorreo = new JTextField();
        txtContrasena = new JPasswordField();

        estilizarCampo(txtCorreo);
        estilizarCampo(txtContrasena);

        // Añadir placeholders y activar el botón del OJO nativo de FlatLaf
        txtCorreo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "usuario@correo.com");
        txtContrasena.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "**********");
        txtContrasena.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");

        btnIngresar = new JButton("Iniciar sesión");
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setBackground(azul);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnIngresar.setAlignmentX(Component.LEFT_ALIGNMENT);


        formulario.add(titulo);
        formulario.add(Box.createVerticalStrut(10));
        formulario.add(subtitulo);
        formulario.add(Box.createVerticalStrut(35));

        formulario.add(crearEtiqueta("Correo electrónico"));
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(txtCorreo);
        formulario.add(Box.createVerticalStrut(20));

        formulario.add(crearEtiqueta("Contraseña"));
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(txtContrasena);
        formulario.add(Box.createVerticalStrut(28));
        formulario.add(btnIngresar);

        principal.add(presentacion);
        principal.add(formulario);

        setContentPane(principal);
        // Permitir iniciar sesión pulsando Enter
        getRootPane().setDefaultButton(btnIngresar);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 13));
        etiqueta.setForeground(azul);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
    }

    private void estilizarCampo(JComponent campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(300, 43));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // borde del botón del ojo y el enfoque azul.
        campo.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc: 8;" // Bordes 
                + "margin: 0,10,0,10;"); // Margen interno izquierdo/derecho
    }
}