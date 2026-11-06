package com.uniajc.dao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uniajc.modelo.Matricula;

public class MatriculaDAOImplTest {

    private IMatriculaDAO matriculaDAO;

    @BeforeEach
    public void setUp() {
        matriculaDAO = new MatriculaDAOImpl();
    }

    @Test
    public void testListarPorEstudianteInexistente() {

        List<Matricula> matriculas = matriculaDAO.listarPorEstudiante("EST_INEXISTENTE_999");
        
   
        assertNotNull(matriculas, "La lista de matrículas no debe ser nula");
        assertTrue(matriculas.isEmpty(), "Debería retornar una lista vacía para un estudiante sin registros");
    }

    @Test
    public void testBuscarPorIdInexistente() {
        
        Matricula matricula = matriculaDAO.buscarPorId("MAT_INEXISTENTE_999");
        
        
        assertNull(matricula, "Una matrícula inexistente debería retornar null");
    }
}