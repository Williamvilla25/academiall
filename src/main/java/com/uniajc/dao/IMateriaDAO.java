package com.uniajc.dao;

import java.util.List;

import com.uniajc.modelo.Materia;

public interface IMateriaDAO {
    boolean guardar(Materia materia);
    Materia buscarPorCodigo(String codigoMateria);
    List<Materia> listarPorProfesor(String codigoProfesor);
    List<Materia> listarTodas();
}