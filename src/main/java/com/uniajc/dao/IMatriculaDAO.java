package com.uniajc.dao;

import java.util.List;

import com.uniajc.modelo.EstadoMatricula;
import com.uniajc.modelo.Matricula;

public interface IMatriculaDAO {
    boolean guardar(Matricula matricula);
    Matricula buscarPorId(String idMatricula);
    List<Matricula> listarPorEstudiante(String codigoEstudiantil);
    boolean actualizarEstado(String idMatricula, EstadoMatricula estado);
}