package com.uniajc.dao;

import java.util.ArrayList;
import java.util.List;

import com.uniajc.modelo.Materia;

// Implementación temporal en memoria / lista para conectar con la BD
public class MateriaDAOImpl implements IMateriaDAO {

    @Override
    public boolean guardar(Materia materia) {
        // Aquí irá la sentencia SQL INSERT cuando el Integrante 1 entregue ConexionBD
        System.out.println("Guardando materia en la base de datos: " + materia.getNombreMateria());
        return true;
    }

    @Override
    public Materia buscarPorCodigo(String codigoMateria) {
        // Aquí irá la consulta SQL SELECT por código
        return null;
    }

    @Override
    public List<Materia> listarPorProfesor(String codigoProfesor) {
        // Aquí irá la consulta filtrada por profesor
        return new ArrayList<>();
    }

    @Override
    public List<Materia> listarTodas() {
        // Aquí irá el SELECT * FROM materias
        return new ArrayList<>();
    }
}