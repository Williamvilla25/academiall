package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uniajc.modelo.Materia;
import com.uniajc.util.ConexionBD;

public class MateriaDAOImpl implements IMateriaDAO {

    @Override
    public boolean guardar(Materia materia) {
        String sql = "INSERT INTO materias (codigo_materia, nombre_materia, creditos, semestre) VALUES (?, ?, ?, ?) " +
                     "ON CONFLICT (codigo_materia) DO UPDATE SET nombre_materia = EXCLUDED.nombre_materia, " +
                     "creditos = EXCLUDED.creditos, semestre = EXCLUDED.semestre";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, materia.getCodigoMateria());
                stmt.setString(2, materia.getNombreMateria());
                stmt.setInt(3, materia.getCreditos());
                stmt.setInt(4, materia.getSemestre());
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error en MateriaDAOImpl.guardar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Materia buscarPorCodigo(String codigoMateria) {
        String sql = "SELECT codigo_materia, nombre_materia, creditos, semestre FROM materias WHERE codigo_materia = ?";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, codigoMateria);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapearMateria(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en MateriaDAOImpl.buscarPorCodigo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Materia> listarPorProfesor(String codigoProfesor) {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT codigo_materia, nombre_materia, creditos, semestre FROM materias WHERE profesor_id = ?";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, codigoProfesor);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearMateria(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en MateriaDAOImpl.listarPorProfesor: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Materia> listarTodas() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT codigo_materia, nombre_materia, creditos, semestre FROM materias ORDER BY codigo_materia ASC";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMateria(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en MateriaDAOImpl.listarTodas: " + e.getMessage());
        }
        return lista;
    }

    private Materia mapearMateria(ResultSet rs) throws SQLException {
        return new Materia(
                rs.getString("codigo_materia"),
                rs.getString("nombre_materia"),
                rs.getInt("creditos"),
                rs.getInt("semestre")
        );
    }
}
