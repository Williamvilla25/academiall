package com.uniajc.modelo;

import java.time.LocalDate;

public class Matricula {
    private String idMatricula;
    private LocalDate fechaMatricula;
    private EstadoMatricula estado;
    private String codigoEstudiantil;
    private Materia materia;

    public Matricula() {
        this.fechaMatricula = LocalDate.now(); 
        this.estado = EstadoMatricula.INSCRITA; 
    }

    public Matricula(String idMatricula, String codigoEstudiantil, Materia materia) {
        this.idMatricula = idMatricula;
        this.codigoEstudiantil = codigoEstudiantil;
        this.materia = materia;
        this.fechaMatricula = LocalDate.now();
        this.estado = EstadoMatricula.INSCRITA;
    }

  
    public String getIdMatricula() { return idMatricula; }
    public void setIdMatricula(String idMatricula) { this.idMatricula = idMatricula; }

    public LocalDate getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(LocalDate fechaMatricula) { this.fechaMatricula = fechaMatricula; }

    public EstadoMatricula getEstado() { return estado; }
    public void setEstado(EstadoMatricula estado) { this.estado = estado; }

    public String getCodigoEstudiantil() { return codigoEstudiantil; }
    public void setCodigoEstudiantil(String codigoEstudiantil) { this.codigoEstudiantil = codigoEstudiantil; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }
}