package com.uniajc.controlador;

import com.uniajc.dao.ICalificacionDAO;
import com.uniajc.dao.IMatriculaDAO;
import com.uniajc.modelo.Calificacion;
import com.uniajc.modelo.EstadoMatricula;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ControladorNotas {
    private final ICalificacionDAO calificacionDAO;
    private final IMatriculaDAO matriculaDAO;

    public ControladorNotas(ICalificacionDAO calificacionDAO, IMatriculaDAO matriculaDAO) {
        this.calificacionDAO = calificacionDAO;
        this.matriculaDAO = matriculaDAO;
    }

    public void registrarCalificacion(Long idMatricula, BigDecimal nota1, BigDecimal nota2, String observaciones) {
        validarRango(nota1);
        validarRango(nota2);

        Calificacion calificacion = new Calificacion(idMatricula, nota1, nota2, observaciones);
        boolean guardado = calificacionDAO.guardarOActualizar(calificacion);

        if (guardado) {
            actualizarEstadoMatriculaSegunPromedio(idMatricula, nota1, nota2);
        }
    }

    public BigDecimal calcularPromedio(BigDecimal nota1, BigDecimal nota2) {
        if (nota1 == null || nota2 == null) {
            throw new IllegalArgumentException("Las notas no pueden ser nulas");
        }
        return nota1.add(nota2).divide(new BigDecimal("2.0"), 2, RoundingMode.HALF_UP);
    }

    public boolean esAprobado(BigDecimal promedio) {
        return promedio.compareTo(new BigDecimal("3.0")) >= 0;
    }

    private void validarRango(BigDecimal nota) {
        if (nota == null) {
            throw new IllegalArgumentException("La nota no puede ser nula");
        }
        BigDecimal min = new BigDecimal("0.0");
        BigDecimal max = new BigDecimal("5.0");
        if (nota.compareTo(min) < 0 || nota.compareTo(max) > 0) {
            throw new IllegalArgumentException("La nota debe estar entre 0.0 y 5.0");
        }
    }

    private void actualizarEstadoMatriculaSegunPromedio(Long idMatricula, BigDecimal nota1, BigDecimal nota2) {
        BigDecimal promedio = calcularPromedio(nota1, nota2);
        EstadoMatricula nuevoEstado = esAprobado(promedio) ? EstadoMatricula.APROBADA : EstadoMatricula.REPROBADA;
        
        matriculaDAO.actualizarEstado(idMatricula, nuevoEstado);
    }
}