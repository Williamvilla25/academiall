package com.uniajc.vista;

import com.uniajc.controlador.ControladorAcademico;
import com.uniajc.dao.MateriaDAOImpl;
import com.uniajc.dao.MatriculaDAOImpl;
import com.uniajc.modelo.Materia;
import com.uniajc.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VistaAcademica extends JFrame {
    private final ControladorAcademico controlador;
    private final Usuario usuarioActual;

    public VistaAcademica() {
        this(null);
    }

   
    public VistaAcademica(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.controlador = new ControladorAcademico(new MateriaDAOImpl(), new MatriculaDAOImpl());

        setTitle("Módulo Académico - UNIAJC" + (usuarioActual != null ? " (" + usuarioActual.getNombre() + ")" : ""));
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

       
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        String nombreUsuario = (usuarioActual != null) ? usuarioActual.getNombre() : "Invitado";
        String rolUsuario = (usuarioActual != null && usuarioActual.getRol() != null) ? usuarioActual.getRol().name() : "";
        
        JLabel lblBienvenido = new JLabel("Bienvenido, " + nombreUsuario + " [" + rolUsuario + "]");
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelNorte.add(lblBienvenido);
        add(panelNorte, BorderLayout.NORTH);

       
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        JButton btnListarMaterias = new JButton("Ver Oferta Académica");
        panelCentro.add(btnListarMaterias, BorderLayout.NORTH);

        JTextArea txtAreaResultados = new JTextArea();
        txtAreaResultados.setEditable(false);
        txtAreaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtAreaResultados);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        
        btnListarMaterias.addActionListener(e -> {
            List<Materia> materias = controlador.obtenerTodasLasMaterias();
            txtAreaResultados.setText("--- Oferta de Materias ---\n\n");
            if (materias.isEmpty()) {
                txtAreaResultados.append("No hay materias registradas en la base de datos.\n");
            } else {
                for (Materia m : materias) {
                    txtAreaResultados.append("Código: " + m.getCodigoMateria() + 
                                             " | Nombre: " + m.getNombreMateria() + 
                                             " | Créditos: " + m.getCreditos() + 
                                             " | Semestre: " + m.getSemestre() + "\n");
                }
            }
        });
    }

    public void mostrarVentana() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}