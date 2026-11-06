package com.uniajc.dao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uniajc.modelo.Materia;

public class MateriaDAOImplTest {

    private IMateriaDAO materiaDAO;

    @BeforeEach
    public void setUp() {
        materiaDAO = new MateriaDAOImpl();
    }

    @Test
    public void testListarTodas() {
      
        List<Materia> materias = materiaDAO.listarTodas();
        
     
        assertNotNull(materias, "La lista de materias no debería ser nula");
    }

    @Test
    public void testBuscarPorCodigoInexistente() {
    
        Materia materia = materiaDAO.buscarPorCodigo("MAT_INEXISTENTE_999");
        
       
        assertNull(materia, "Una materia inexistente debería retornar null");
    }
}