package com.uniajc.dao;

import com.uniajc.modelo.EstadoMatricula;
import com.uniajc.modelo.Materia;
import com.uniajc.modelo.Matricula;
import com.uniajc.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAOImpl implements IMatriculaDAO {

    @Override
    public boolean guardar(Matricula matricula) {
        String sql = "INSERT INTO matriculas (id_matricula, codigo_estudiante, codigo_materia, fecha_matricula, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula.getIdMatricula());
            pstmt.setString(2, matricula.getCodigoEstudiantil());
            pstmt.setString(3, matricula.getMateria() != null ? matricula.getMateria().getCodigoMateria() : null);
            pstmt.setDate(4, Date.valueOf(matricula.getFechaMatricula()));
            pstmt.setString(5, matricula.getEstado().name());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al guardar matrícula: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Matricula buscarPorId(String idMatricula) {
        String sql = "SELECT m.id_matricula, m.codigo_estudiante, m.fecha_matricula, m.estado, " +
                     "mat.codigo_materia, mat.nombre_materia, mat.creditos, mat.semestre " +
                     "FROM matriculas m JOIN materias mat ON m.codigo_materia = mat.codigo_materia " +
                     "WHERE m.id_matricula = ?";
        try (Connection conn = ConexionBD.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idMatricula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Matricula mat = new Matricula();
                    mat.setIdMatricula(rs.getString("id_matricula"));
                    mat.setCodigoEstudiantil(rs.getString("codigo_estudiante"));
                    mat.setFechaMatricula(rs.getDate("fecha_matricula").toLocalDate());
                    mat.setEstado(EstadoMatricula.valueOf(rs.getString("estado")));

                    Materia m = new Materia();
                    m.setCodigoMateria(rs.getString("codigo_materia"));
                    m.setNombreMateria(rs.getString("nombre_materia"));
                    m.setCreditos(rs.getInt("creditos"));
                    m.setSemestre(rs.getInt("semestre"));
                    mat.setMateria(m);

                    return mat;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar matrícula por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Matricula> listarPorEstudiante(String codigoEstudiantil) {
        List<Matricula> lista = new ArrayList<>();
        String sql = "SELECT m.id_matricula, m.codigo_estudiante, m.fecha_matricula, m.estado, " +
                     "mat.codigo_materia, mat.nombre_materia, mat.creditos, mat.semestre " +
                     "FROM matriculas m JOIN materias mat ON m.codigo_materia = mat.codigo_materia " +
                     "WHERE m.codigo_estudiante = ?";
        try (Connection conn = ConexionBD.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigoEstudiantil);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Matricula mat = new Matricula();
                    mat.setIdMatricula(rs.getString("id_matricula"));
                    mat.setCodigoEstudiantil(rs.getString("codigo_estudiante"));
                    mat.setFechaMatricula(rs.getDate("fecha_matricula").toLocalDate());
                    mat.setEstado(EstadoMatricula.valueOf(rs.getString("estado")));

                    Materia m = new Materia();
                    m.setCodigoMateria(rs.getString("codigo_materia"));
                    m.setNombreMateria(rs.getString("nombre_materia"));
                    m.setCreditos(rs.getInt("creditos"));
                    m.setSemestre(rs.getInt("semestre"));
                    mat.setMateria(m);

                    lista.add(mat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar matrículas por estudiante: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizarEstado(String idMatricula, EstadoMatricula estado) {
        String sql = "UPDATE matriculas SET estado = ? WHERE id_matricula = ?";
        try (Connection conn = ConexionBD.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estado.name());
            pstmt.setString(2, idMatricula);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de matrícula: " + e.getMessage());
            return false;
        }
    }
}