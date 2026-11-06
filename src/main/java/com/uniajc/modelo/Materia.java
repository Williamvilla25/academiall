package com.uniajc.modelo;

public class Materia {
    private String codigoMateria;
    private String nombreMateria;
    private int creditos;
    private int semestre;

    public Materia() {}

    public Materia(String codigoMateria, String nombreMateria, int creditos, int semestre) {
        this.codigoMateria = codigoMateria;
        this.nombreMateria = nombreMateria;
        this.creditos = creditos;
        this.semestre = semestre;
    }

    public String getCodigoMateria() { return codigoMateria; }
    public void setCodigoMateria(String codigoMateria) { this.codigoMateria = codigoMateria; }

    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { this.nombreMateria = nombreMateria; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
}