package com.uniajc.modelo;

import java.math.BigDecimal;

public class Calificacion {
    private Long idMatricula;
    private BigDecimal notaParcial1;
    private BigDecimal notaParcial2;
    private String observaciones;

    public Calificacion() {}

    public Calificacion(Long idMatricula, BigDecimal notaParcial1, BigDecimal notaParcial2, String observaciones) {
        this.idMatricula = idMatricula;
        this.notaParcial1 = notaParcial1;
        this.notaParcial2 = notaParcial2;
        this.observaciones = observaciones;
    }

    public Long getIdMatricula() { return idMatricula; }
    public void setIdMatricula(Long idMatricula) { this.idMatricula = idMatricula; }

    public BigDecimal getNotaParcial1() { return notaParcial1; }
    public void setNotaParcial1(BigDecimal notaParcial1) { this.notaParcial1 = notaParcial1; }

    public BigDecimal getNotaParcial2() { return notaParcial2; }
    public void setNotaParcial2(BigDecimal notaParcial2) { this.notaParcial2 = notaParcial2; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}