package com.uniajc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uniajc.controlador.ControladorNotas;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ControladorNotasTest {

    private ControladorNotas controladorNotas;

    @BeforeEach
    void setUp() {
        controladorNotas = new ControladorNotas(null, null);
    }

    @Test
    void testRegistrarCalificacionRangoValido() {
        BigDecimal notaValida = new BigDecimal("4.8");
        assertDoesNotThrow(() -> controladorNotas.calcularPromedio(notaValida, BigDecimal.valueOf(3.0)));
    }

    @Test
    void testRegistrarCalificacionRangoInvalidoSuperior() {
        BigDecimal notaInvalida = new BigDecimal("5.5");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            if (notaInvalida.compareTo(BigDecimal.ZERO) < 0 || notaInvalida.compareTo(new BigDecimal("5.0")) > 0) {
                throw new IllegalArgumentException("La nota debe estar entre 0.0 y 5.0");
            }
        });
        assertEquals("La nota debe estar entre 0.0 y 5.0", exception.getMessage());
    }

    @Test
    void testRegistrarCalificacionRangoInvalidoNegativo() {
        BigDecimal notaInvalida = new BigDecimal("-0.5");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            if (notaInvalida.compareTo(BigDecimal.ZERO) < 0 || notaInvalida.compareTo(new BigDecimal("5.0")) > 0) {
                throw new IllegalArgumentException("La nota debe estar entre 0.0 y 5.0");
            }
        });
        assertEquals("La nota debe estar entre 0.0 y 5.0", exception.getMessage());
    }
}