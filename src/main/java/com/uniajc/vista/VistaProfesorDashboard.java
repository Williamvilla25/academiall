package com.uniajc.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaProfesorDashboard extends JFrame {
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;
    private JTextField txtNota1;
    private JTextField txtNota2;
    private JTextField txtObservaciones;
    private JButton btnGuardar;

    public VistaProfesorDashboard() {
        setTitle("Academiall - Panel Docente");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("Gestión de Calificaciones de Estudiantes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"ID Matrícula", "Estudiante", "Materia", "Parcial 1", "Parcial 2", "Promedio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEstudiantes = new JTable(modeloTabla);
        add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);

        JPanel panelFormulario = new JPanel(new GridLayout(2, 4, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Digitar Calificaciones"));

        txtNota1 = new JTextField();
        txtNota2 = new JTextField();
        txtObservaciones = new JTextField();
        btnGuardar = new JButton("Guardar Nota");

        panelFormulario.add(new JLabel("Nota Parcial 1:"));
        panelFormulario.add(txtNota1);
        panelFormulario.add(new JLabel("Nota Parcial 2:"));
        panelFormulario.add(txtNota2);
        panelFormulario.add(new JLabel("Observaciones:"));
        panelFormulario.add(txtObservaciones);
        panelFormulario.add(new JLabel());
        panelFormulario.add(btnGuardar);

        add(panelFormulario, BorderLayout.SOUTH);
    }

    public JTable getTablaEstudiantes() { return tablaEstudiantes; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JTextField getTxtNota1() { return txtNota1; }
    public JTextField getTxtNota2() { return txtNota2; }
    public JTextField getTxtObservaciones() { return txtObservaciones; }
    public JButton getBtnGuardar() { return btnGuardar; }
}