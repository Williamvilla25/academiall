package com.uniajc.controlador;

import java.util.List;

import com.uniajc.dao.IMateriaDAO;
import com.uniajc.dao.IMatriculaDAO;
import com.uniajc.modelo.EstadoMatricula;
import com.uniajc.modelo.Materia;
import com.uniajc.modelo.Matricula;

public class ControladorAcademico {
    private IMateriaDAO materiaDAO;
    private IMatriculaDAO matriculaDAO;

  
    public ControladorAcademico(IMateriaDAO materiaDAO, IMatriculaDAO matriculaDAO) {
        this.materiaDAO = materiaDAO;
        this.matriculaDAO = matriculaDAO;
    }

  
    public boolean matricularMateria(String idMatricula, String codigoEstudiantil, Materia materia) {
        List<Matricula> matriculasEstudiante = matriculaDAO.listarPorEstudiante(codigoEstudiantil);
        
        for (Matricula m : matriculasEstudiante) {
            if (m.getMateria().getCodigoMateria().equals(materia.getCodigoMateria()) 
                && m.getEstado() == EstadoMatricula.INSCRITA) {
                System.out.println("Error: El estudiante ya está matriculado en esta materia.");
                return false;
            }
        }

        Matricula nuevaMatricula = new Matricula(idMatricula, codigoEstudiantil, materia);
        return matriculaDAO.guardar(nuevaMatricula);
    }

   
    public List<Materia> obtenerTodasLasMaterias() {
        return materiaDAO.listarTodas();
    }

    
    public List<Matricula> obtenerMateriasEstudiante(String codigoEstudiantil) {
        return matriculaDAO.listarPorEstudiante(codigoEstudiantil);
    }
}