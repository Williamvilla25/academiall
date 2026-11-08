package com.uniajc.model;

import com.uniajc.controlador.ControladorNotas; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalificacionTest {

    private ControladorNotas controladorNotas;

    @BeforeEach
    void setUp() {
        controladorNotas = new ControladorNotas(null, null);
    }

    @Test
    void testCalcularPromedioFinalAprobado() {
        BigDecimal nota1 = new BigDecimal("4.5");
        BigDecimal nota2 = new BigDecimal("3.5");
        
        BigDecimal promedio = controladorNotas.calcularPromedio(nota1, nota2);
        
        assertEquals(0, new BigDecimal("4.00").compareTo(promedio));
        assertTrue(controladorNotas.esAprobado(promedio));
    }

    @Test
    void testCalcularPromedioFinalReprobado() {
        BigDecimal nota1 = new BigDecimal("2.0");
        BigDecimal nota2 = new BigDecimal("2.5");
        
        BigDecimal promedio = controladorNotas.calcularPromedio(nota1, nota2);
        
        assertEquals(0, new BigDecimal("2.25").compareTo(promedio));
        assertFalse(controladorNotas.esAprobado(promedio));
    }

    @Test
    void testPrecisionDecimalBigDecimal() {
        BigDecimal nota1 = new BigDecimal("3.33");
        BigDecimal nota2 = new BigDecimal("3.34");
        
        BigDecimal promedio = controladorNotas.calcularPromedio(nota1, nota2);
        
        assertEquals(0, new BigDecimal("3.34").compareTo(promedio));
    }
}