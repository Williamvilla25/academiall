package com.uniajc.vista;

import com.uniajc.dao.IUsuarioDAO;
import com.uniajc.dao.impl.UsuarioDAOImpl;
import com.uniajc.modelo.Administrador;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Profesor;
import com.uniajc.modelo.Usuario;
import com.uniajc.modelo.enums.EstadoUsuario;
import com.uniajc.modelo.enums.RolUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaAdminDashboard extends JFrame {
    private final IUsuarioDAO usuarioDAO;
    private final Usuario usuarioActual;

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalUsuarios;

    public VistaAdminDashboard(IUsuarioDAO usuarioDAO, Usuario usuarioActual) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioActual = usuarioActual;
        inicializarUI();
        cargarUsuarios();
    }

    private void inicializarUI() {
        setTitle("Academiall - Panel Administrativo (" + (usuarioActual != null ? usuarioActual.getNombreCompleto() : "Admin") + ")");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Cabecera
        JPanel panelCabecera = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión Global de Usuarios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalUsuarios = new JLabel("Total usuarios: 0");
        lblTotalUsuarios.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        panelCabecera.add(lblTitulo, BorderLayout.WEST);
        panelCabecera.add(lblTotalUsuarios, BorderLayout.EAST);
        panelPrincipal.add(panelCabecera, BorderLayout.NORTH);

        // Tabla de Usuarios
        String[] columnas = {"ID", "Nombre Completo", "Correo", "Teléfono", "Rol", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(24);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelPrincipal.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        // Panel de Acciones Inferior
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnNuevo = new JButton("Nuevo Usuario");
        JButton btnCambiarEstado = new JButton("Cambiar Estado");
        JButton btnReportes = new JButton("Reportes Globales");
        JButton btnRefrescar = new JButton("Refrescar");

        panelAcciones.add(btnNuevo);
        panelAcciones.add(btnCambiarEstado);
        panelAcciones.add(btnReportes);
        panelAcciones.add(btnRefrescar);

        panelPrincipal.add(panelAcciones, BorderLayout.SOUTH);
        add(panelPrincipal);

        // Acciones
        btnRefrescar.addActionListener(e -> cargarUsuarios());
        btnReportes.addActionListener(e -> desplegarReportesGlobales());
        btnCambiarEstado.addActionListener(e -> cambiarEstadoSeleccionado());
        btnNuevo.addActionListener(e -> abrirDialogoNuevoUsuario());
    }

    public void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> lista = usuarioDAO.listarTodos();
        for (Usuario u : lista) {
            modeloTabla.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombreCompleto(),
                    u.getCorreo(),
                    u.getTelefono(),
                    u.getRol(),
                    u.getEstado()
            });
        }
        lblTotalUsuarios.setText("Total usuarios: " + lista.size());
    }

    private void cambiarEstadoSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) modeloTabla.getValueAt(fila, 0);
        EstadoUsuario nuevoEstado = (EstadoUsuario) JOptionPane.showInputDialog(
                this,
                "Seleccione el nuevo estado:",
                "Cambiar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                EstadoUsuario.values(),
                modeloTabla.getValueAt(fila, 5)
        );

        if (nuevoEstado != null) {
            boolean exito = usuarioDAO.actualizarEstado(id, nuevoEstado);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Estado actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarUsuarios();
            } else {
                String errorMsg = "No se pudo actualizar el estado.";
                if (usuarioDAO instanceof UsuarioDAOImpl impl && impl.getUltimoError() != null) {
                    errorMsg += "\nDetalle: " + impl.getUltimoError();
                }
                JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirDialogoNuevoUsuario() {
        JDialog dialog = new JDialog(this, "Crear Nuevo Usuario", true);
        dialog.setSize(440, 460);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(9, 2, 8, 8));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtTelefono = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JComboBox<RolUsuario> cbRol = new JComboBox<>(RolUsuario.values());

        JLabel lblDinamico1 = new JLabel("Nivel Acceso:");
        JTextField txtDinamico1 = new JTextField("NIVEL_1");
        JLabel lblDinamico2 = new JLabel("Departamento:");
        JTextField txtDinamico2 = new JTextField("Sistemas");

        panelCampos.add(new JLabel("ID Usuario (*):")); panelCampos.add(txtId);
        panelCampos.add(new JLabel("Nombre (*):")); panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Apellido (*):")); panelCampos.add(txtApellido);
        panelCampos.add(new JLabel("Correo (*):")); panelCampos.add(txtCorreo);
        panelCampos.add(new JLabel("Teléfono:")); panelCampos.add(txtTelefono);
        panelCampos.add(new JLabel("Contraseña (*):")); panelCampos.add(txtPass);
        panelCampos.add(new JLabel("Rol (*):")); panelCampos.add(cbRol);
        panelCampos.add(lblDinamico1); panelCampos.add(txtDinamico1);
        panelCampos.add(lblDinamico2); panelCampos.add(txtDinamico2);

        // Dinamizar etiquetas según el rol seleccionado
        cbRol.addActionListener(e -> {
            RolUsuario seleccionado = (RolUsuario) cbRol.getSelectedItem();
            if (seleccionado == RolUsuario.ADMINISTRADOR) {
                lblDinamico1.setText("Nivel Acceso:");
                txtDinamico1.setText("NIVEL_1");
                lblDinamico2.setText("Departamento:");
                txtDinamico2.setText("Sistemas");
            } else if (seleccionado == RolUsuario.PROFESOR) {
                lblDinamico1.setText("Código Profesor:");
                txtDinamico1.setText(txtId.getText().isEmpty() ? "PROF-01" : "PROF-" + txtId.getText().trim());
                lblDinamico2.setText("Especialidad:");
                txtDinamico2.setText("Ingeniería de Software");
            } else {
                lblDinamico1.setText("Código Estudiante:");
                txtDinamico1.setText(txtId.getText().isEmpty() ? "EST-01" : "EST-" + txtId.getText().trim());
                lblDinamico2.setText("Programa Académico:");
                txtDinamico2.setText("Ingeniería de Sistemas");
            }
        });

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.setBackground(new Color(37, 99, 235));
        btnGuardar.setForeground(Color.WHITE);

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        dialog.add(panelCampos, BorderLayout.CENTER);
        dialog.add(panelBotones, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dialog.dispose());

        btnGuardar.addActionListener(e -> {
            String id = txtId.getText().trim();
            String nom = txtNombre.getText().trim();
            String ape = txtApellido.getText().trim();
            String cor = txtCorreo.getText().trim();
            String tel = txtTelefono.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            RolUsuario rol = (RolUsuario) cbRol.getSelectedItem();

            if (id.isEmpty() || nom.isEmpty() || cor.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor complete todos los campos obligatorios (*).", "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar si el ID ya existe antes de insertar
            if (usuarioDAO.buscarPorId(id) != null) {
                JOptionPane.showMessageDialog(dialog, "El ID de usuario '" + id + "' ya está registrado. Ingrese un ID diferente.", "ID Duplicado", JOptionPane.WARNING_MESSAGE);
                txtId.requestFocus();
                return;
            }

            // Validar si el Correo ya existe antes de insertar
            if (usuarioDAO.buscarPorCorreo(cor) != null) {
                JOptionPane.showMessageDialog(dialog, "El correo '" + cor + "' ya se encuentra registrado. Ingrese otro correo.", "Correo Duplicado", JOptionPane.WARNING_MESSAGE);
                txtCorreo.requestFocus();
                return;
            }

            Usuario nuevo;
            String extra1 = txtDinamico1.getText().trim();
            String extra2 = txtDinamico2.getText().trim();

            switch (rol) {
                case ADMINISTRADOR:
                    nuevo = new Administrador(id, nom, ape, cor, tel, pass, EstadoUsuario.ACTIVO, extra1.isEmpty() ? "NIVEL_1" : extra1, extra2.isEmpty() ? "General" : extra2);
                    break;
                case PROFESOR:
                    nuevo = new Profesor(id, nom, ape, cor, tel, pass, EstadoUsuario.ACTIVO, extra2.isEmpty() ? "General" : extra2, extra1.isEmpty() ? "PROF-" + id : extra1);
                    break;
                case ESTUDIANTE:
                default:
                    nuevo = new Estudiante(id, nom, ape, cor, tel, pass, EstadoUsuario.ACTIVO, extra1.isEmpty() ? "EST-" + id : extra1, extra2.isEmpty() ? "Ingeniería" : extra2, 1);
                    break;
            }

            boolean insertado = usuarioDAO.insertar(nuevo);
            if (insertado) {
                JOptionPane.showMessageDialog(dialog, "¡Usuario creado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose(); // Solo se cierra si fue exitoso
                cargarUsuarios();
            } else {
                String errorMsg = "No se pudo crear el usuario en la base de datos.";
                if (usuarioDAO instanceof UsuarioDAOImpl impl && impl.getUltimoError() != null) {
                    errorMsg += "\n\nCausa exacta:\n" + impl.getUltimoError();
                }
                JOptionPane.showMessageDialog(dialog, errorMsg, "Error de Inserción", JOptionPane.ERROR_MESSAGE);
                // NO se hace dialog.dispose(): se queda en la modal para que el usuario corrija los datos sin perderlos
            }
        });

        dialog.setVisible(true);
    }

    public void desplegarReportesGlobales() {
        List<Usuario> lista = usuarioDAO.listarTodos();
        long activos = lista.stream().filter(u -> u.getEstado() == EstadoUsuario.ACTIVO).count();
        long bloqueados = lista.stream().filter(u -> u.getEstado() == EstadoUsuario.BLOQUEADO).count();
        long inactivos = lista.stream().filter(u -> u.getEstado() == EstadoUsuario.INACTIVO).count();

        long admins = lista.stream().filter(u -> u.getRol() == RolUsuario.ADMINISTRADOR).count();
        long profes = lista.stream().filter(u -> u.getRol() == RolUsuario.PROFESOR).count();
        long estudiantes = lista.stream().filter(u -> u.getRol() == RolUsuario.ESTUDIANTE).count();

        String reporte = String.format("""
                === REPORTE GLOBAL ACADEMIALL ===
                Total Usuarios: %d
                
                Por Estado:
                - Activos: %d
                - Bloqueados: %d
                - Inactivos: %d
                
                Por Rol:
                - Administradores: %d
                - Profesores: %d
                - Estudiantes: %d
                """, lista.size(), activos, bloqueados, inactivos, admins, profes, estudiantes);

        JOptionPane.showMessageDialog(this, reporte, "Reporte Global del Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarPanelAdmin() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}
