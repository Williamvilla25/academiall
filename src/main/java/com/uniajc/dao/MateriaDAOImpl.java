package com.uniajc.dao;

import com.uniajc.modelo.Materia;
import com.uniajc.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAOImpl implements IMateriaDAO {

    @Override
    public boolean guardar(Materia materia) {
        String sql = "INSERT INTO materias (codigo_materia, nombre_materia, creditos, semestre) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, materia.getCodigoMateria());
            pstmt.setString(2, materia.getNombreMateria());
            pstmt.setInt(3, materia.getCreditos());
            pstmt.setInt(4, materia.getSemestre());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al guardar materia: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Materia buscarPorCodigo(String codigoMateria) {
        String sql = "SELECT codigo_materia, nombre_materia, creditos, semestre FROM materias WHERE codigo_materia = ?";
        try (Connection conn = ConexionBD.obtenerInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigoMateria);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Materia m = new Materia();
                    m.setCodigoMateria(rs.getString("codigo_materia"));
                    m.setNombreMateria(rs.getString("nombre_materia"));
                    m.setCreditos(rs.getInt("creditos"));
                    m.setSemestre(rs.getInt("semestre"));
                    return m;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar materia por código: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Materia> listarPorProfesor(String codigoProfesor) {
        List<Materia> lista = new ArrayList<>();
       
        return lista;
    }

    @Override
    public List<Materia> listarTodas() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT codigo_materia, nombre_materia, creditos, semestre FROM materias";
        try (Connection conn = ConexionBD.obtenerInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Materia m = new Materia();
                m.setCodigoMateria(rs.getString("codigo_materia"));
                m.setNombreMateria(rs.getString("nombre_materia"));
                m.setCreditos(rs.getInt("creditos"));
                m.setSemestre(rs.getInt("semestre"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar materias: " + e.getMessage());
        }
        return lista;
    }
}