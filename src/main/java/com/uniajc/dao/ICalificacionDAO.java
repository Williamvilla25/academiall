package com.uniajc.dao;

import com.uniajc.modelo.Calificacion;
import java.util.List;
import java.util.Optional;

public interface ICalificacionDAO {
    boolean guardarOActualizar(Calificacion calificacion);
    Optional<Calificacion> buscarPorMatriculaId(Long idMatricula);
    List<Calificacion> listarTodas();
}