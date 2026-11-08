package com.uniajc.dao;

import com.uniajc.modelo.Calificacion;
import com.uniajc.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CalificacionDAOImpl implements ICalificacionDAO {

    @Override
    public boolean guardarOActualizar(Calificacion cal) {
        String sql = "INSERT INTO calificaciones (id_matricula, nota_parcial1, nota_parcial2, observaciones) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON CONFLICT (id_matricula) DO UPDATE SET " +
                     "nota_parcial1 = EXCLUDED.nota_parcial1, " +
                     "nota_parcial2 = EXCLUDED.nota_parcial2, " +
                     "observaciones = EXCLUDED.observaciones";

        try (Connection conn = ConexionBD.obtenerInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, cal.getIdMatricula());
            stmt.setBigDecimal(2, cal.getNotaParcial1());
            stmt.setBigDecimal(3, cal.getNotaParcial2());
            stmt.setString(4, cal.getObservaciones());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Calificacion> buscarPorMatriculaId(Long idMatricula) {
        String sql = "SELECT id_matricula, nota_parcial1, nota_parcial2, observaciones FROM calificaciones WHERE id_matricula = ?";
        
        try (Connection conn = ConexionBD.obtenerInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idMatricula);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Calificacion cal = new Calificacion(
                        rs.getLong("id_matricula"),
                        rs.getBigDecimal("nota_parcial1"),
                        rs.getBigDecimal("nota_parcial2"),
                        rs.getString("observaciones")
                    );
                    return Optional.of(cal);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Calificacion> listarTodas() {
        List<Calificacion> lista = new ArrayList<>();
        String sql = "SELECT id_matricula, nota_parcial1, nota_parcial2, observaciones FROM calificaciones";

        try (Connection conn = ConexionBD.obtenerInstancia().getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Calificacion cal = new Calificacion(
                    rs.getLong("id_matricula"),
                    rs.getBigDecimal("nota_parcial1"),
                    rs.getBigDecimal("nota_parcial2"),
                    rs.getString("observaciones")
                );
                lista.add(cal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}