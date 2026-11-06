package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.uniajc.controlador.ControladorAcademico;
import com.uniajc.dao.MateriaDAOImpl;
import com.uniajc.dao.MatriculaDAOImpl;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Materia;
import com.uniajc.modelo.Usuario;

public class VistaAcademica extends JFrame {
    private final ControladorAcademico controlador;
    private final Usuario usuarioActual;

    private JTable tablaMaterias;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;

    public VistaAcademica() {
        this(null);
    }

    public VistaAcademica(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.controlador = new ControladorAcademico(new MateriaDAOImpl(), new MatriculaDAOImpl());

        String rolTexto = usuarioActual != null ? " - " + usuarioActual.getNombreCompleto() + " (" + usuarioActual.getRol() + ")" : "";
        setTitle("Módulo Académico - UNIAJC" + rolTexto);
        setSize(700, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        cargarOfertaAcademica();
    }

    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Cabecera
        JPanel panelCabecera = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Gestión Académica y Oferta de Materias");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        lblTotal = new JLabel("Total asignaturas: 0");
        lblTotal.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        panelCabecera.add(lblTitulo, BorderLayout.WEST);
        panelCabecera.add(lblTotal, BorderLayout.EAST);
        panelPrincipal.add(panelCabecera, BorderLayout.NORTH);

        // Tabla de Materias
        String[] columnas = {"Código Materia", "Nombre de la Materia", "Créditos", "Semestre"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMaterias = new JTable(modeloTabla);
        tablaMaterias.setRowHeight(24);
        tablaMaterias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panelPrincipal.add(new JScrollPane(tablaMaterias), BorderLayout.CENTER);

        // Panel de Botones Inferior
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        
        JButton btnMatricular = new JButton("Matricular Asignatura");
        btnMatricular.setBackground(new Color(37, 99, 235));
        btnMatricular.setForeground(Color.WHITE);

        JButton btnRefrescar = new JButton("Refrescar Oferta");

        panelBotones.add(btnMatricular);
        panelBotones.add(btnRefrescar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Acciones
        btnRefrescar.addActionListener(e -> cargarOfertaAcademica());
        btnMatricular.addActionListener(e -> matricularAsignaturaSeleccionada());
    }

    public void cargarOfertaAcademica() {
        modeloTabla.setRowCount(0);
        List<Materia> materias = controlador.obtenerTodasLasMaterias();
        for (Materia m : materias) {
            modeloTabla.addRow(new Object[]{
                m.getCodigoMateria(),
                m.getNombreMateria(),
                m.getCreditos(),
                m.getSemestre()
            });
        }
        lblTotal.setText("Total asignaturas: " + materias.size());
    }

    private void matricularAsignaturaSeleccionada() {
        int fila = tablaMaterias.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia de la tabla para matricular.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cod = (String) modeloTabla.getValueAt(fila, 0);
        String nom = (String) modeloTabla.getValueAt(fila, 1);
        int creditos = (int) modeloTabla.getValueAt(fila, 2);
        int sem = (int) modeloTabla.getValueAt(fila, 3);

        Materia seleccionada = new Materia(cod, nom, creditos, sem);

        String codEstudiante = "EST-001";
        if (usuarioActual instanceof Estudiante est && est.getCodigoEstudiantil() != null) {
            codEstudiante = est.getCodigoEstudiantil();
        } else if (usuarioActual != null) {
            codEstudiante = usuarioActual.getIdUsuario();
        }

        String idMatricula = "MAT-" + System.currentTimeMillis() % 100000;
        boolean exito = controlador.matricularMateria(idMatricula, codEstudiante, seleccionada);

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Matrícula exitosa en la asignatura " + nom + "!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo realizar la matrícula (posible duplicado o error en BD).", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void mostrarVentana() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaAcademica().setVisible(true);
        });
    }
}
