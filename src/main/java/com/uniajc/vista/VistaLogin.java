package com.uniajc.vista;

import com.uniajc.controlador.ControladorLogin;
import com.uniajc.dao.IUsuarioDAO;
import com.uniajc.dao.impl.UsuarioDAOImpl;
import com.uniajc.modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class VistaLogin extends JFrame {
    private final ControladorLogin controladorLogin;
    private final IUsuarioDAO usuarioDAO;

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JLabel lblMensaje;

    public VistaLogin() {
        this(new UsuarioDAOImpl());
    }

    public VistaLogin(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
        this.controladorLogin = new ControladorLogin(usuarioDAO);
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("Academiall - Inicio de Sesión");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panelPrincipal.setBackground(new Color(245, 247, 250));

        // Encabezado
        JLabel lblTitulo = new JLabel("Sistema Academiall", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 43, 54));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(4, 1, 8, 8));
        panelFormulario.setOpaque(false);

        JLabel lblCorreo = new JLabel("Correo Institucional:");
        lblCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCorreo = new JTextField();

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtContrasena = new JPasswordField();

        panelFormulario.add(lblCorreo);
        panelFormulario.add(txtCorreo);
        panelFormulario.add(lblContrasena);
        panelFormulario.add(txtContrasena);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Panel Inferior (Botón y Mensaje)
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        panelInferior.setOpaque(false);

        btnIngresar = new JButton("Iniciar Sesión");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setBackground(new Color(37, 99, 235));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensaje.setForeground(Color.RED);

        panelInferior.add(lblMensaje, BorderLayout.NORTH);
        panelInferior.add(btnIngresar, BorderLayout.SOUTH);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Eventos
        btnIngresar.addActionListener(e -> capturarCredenciales());
        txtContrasena.addActionListener(e -> capturarCredenciales());
    }

    public void capturarCredenciales() {
        String correo = txtCorreo.getText().trim();
        String pass = new String(txtContrasena.getPassword());

        if (correo.isEmpty() || pass.isEmpty()) {
            mostrarError("Por favor ingrese correo y contraseña");
            return;
        }

        boolean autenticado = controladorLogin.autenticar(correo, pass);
        if (autenticado) {
            Usuario usuario = controladorLogin.obtenerUsuarioAutenticado(correo, pass);
            mostrarExito("¡Bienvenido " + usuario.getNombre() + "!");
            abrirDashboardSegunRol(usuario);
        } else {
            Usuario existe = usuarioDAO.buscarPorCorreo(correo);
            if (existe != null && existe.getEstado() != com.uniajc.modelo.enums.EstadoUsuario.ACTIVO) {
                mostrarError("El usuario se encuentra " + existe.getEstado());
            } else {
                mostrarError("Credenciales inválidas o correo no registrado");
            }
        }
    }

    private void abrirDashboardSegunRol(Usuario usuario) {
        if (usuario.getRol() == null) {
            JOptionPane.showMessageDialog(this, "El usuario no tiene un rol asignado.");
            return;
        }

        switch (usuario.getRol()) {
            case ADMINISTRADOR:
                new VistaAdminDashboard(usuarioDAO, usuario).mostrarPanelAdmin();
                dispose();
                break;
            case ESTUDIANTE:
                new VistaAcademica(usuario).mostrarVentana();
                dispose();
                break;
            case PROFESOR:
                new VistaAcademica(usuario).mostrarVentana();
                dispose();
                break;
        }
    }

    public void mostrarError(String mensaje) {
        lblMensaje.setForeground(new Color(220, 38, 38));
        lblMensaje.setText(mensaje);
    }

    public void mostrarExito(String mensaje) {
        lblMensaje.setForeground(new Color(22, 163, 74));
        lblMensaje.setText(mensaje);
    }

    public void mostrarVentana() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}
