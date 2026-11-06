package com.uniajc.vista;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.uniajc.controlador.ControladorAcademico;
import com.uniajc.dao.MateriaDAOImpl;
import com.uniajc.dao.MatriculaDAOImpl;
import com.uniajc.modelo.Materia;

public class VistaAcademica extends JFrame {
    private final ControladorAcademico controlador;

    public VistaAcademica() {
       
        this.controlador = new ControladorAcademico(new MateriaDAOImpl(), new MatriculaDAOImpl());

        setTitle("Módulo Académico - UNIAJC (Integrante 2)");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel lblTitulo = new JLabel("Gestión de Matrículas y Materias");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblTitulo);

        JButton btnListarMaterias = new JButton("Ver Oferta Académica");
        add(btnListarMaterias);

        JTextArea txtAreaResultados = new JTextArea(12, 35);
        txtAreaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaResultados);
        add(scrollPane);

        btnListarMaterias.addActionListener(e -> {
            List<Materia> materias = controlador.obtenerTodasLasMaterias();
            txtAreaResultados.setText("--- Oferta de Materias ---\n");
            if (materias.isEmpty()) {
                txtAreaResultados.append("No hay materias registradas aún en la base de datos.\n");
            } else {
                for (Materia m : materias) {
                    txtAreaResultados.append("Código: " + m.getCodigoMateria() + " | " + m.getNombreMateria() + "\n");
                }
            }
        });
    }

    public static void main(String[] args) {
       
        SwingUtilities.invokeLater(() -> {
            new VistaAcademica().setVisible(true);
        });
    }
}