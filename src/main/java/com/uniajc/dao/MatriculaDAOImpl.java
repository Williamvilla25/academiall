package com.uniajc.dao;

import java.util.ArrayList;
import java.util.List;

import com.uniajc.modelo.EstadoMatricula;
import com.uniajc.modelo.Matricula;

public class MatriculaDAOImpl implements IMatriculaDAO {

    @Override
    public boolean guardar(Matricula matricula) {
        // Aquí se usará Matricula.getFechaMatricula() para guardarla como LocalDate en la BD
        System.out.println("Guardando matrícula para el estudiante: " + matricula.getCodigoEstudiantil());
        return true;
    }

    @Override
    public Matricula buscarPorId(String idMatricula) {
        return null;
    }

    @Override
    public List<Matricula> listarPorEstudiante(String codigoEstudiantil) {
        // Retorna la lista de matrículas del estudiante para validar duplicados en el controlador
        return new ArrayList<>();
    }

    @Override
    public boolean actualizarEstado(String idMatricula, EstadoMatricula estado) {
        System.out.println("Actualizando estado de matrícula a: " + estado);
        return true;
    }

    @Override
    public boolean actualizarEstado1(Long idMatricula, EstadoMatricula estado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarEstado1'");
    }

    @Override
    public void actualizarEstado(Long idMatricula, EstadoMatricula nuevoEstado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarEstado'");
    }
}