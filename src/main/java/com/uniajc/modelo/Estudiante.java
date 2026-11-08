package com.uniajc.modelo;

import com.uniajc.modelo.enums.EstadoUsuario;
import com.uniajc.modelo.enums.RolUsuario;

public class Estudiante extends Usuario {
    private String codigoEstudiantil;
    private String programaAcademico;
    private int semestreActual;

    public Estudiante() {
        super();
        this.rol = RolUsuario.ESTUDIANTE;
    }

    public Estudiante(String idUsuario, String nombre, String apellido, String correo, String telefono,
                      String contrasena, EstadoUsuario estado, String codigoEstudiantil, String programaAcademico, int semestreActual) {
        super(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, RolUsuario.ESTUDIANTE);
        this.codigoEstudiantil = codigoEstudiantil;
        this.programaAcademico = programaAcademico;
        this.semestreActual = semestreActual;
    }

    public void consultarNotas() {
        // Implementado por el módulo del estudiante
    }

    public void verHistorialAcademico() {
        // Implementado por el módulo del estudiante
    }

    public String getCodigoEstudiantil() {
        return codigoEstudiantil;
    }

    public void setCodigoEstudiantil(String codigoEstudiantil) {
        this.codigoEstudiantil = codigoEstudiantil;
    }

    public String getProgramaAcademico() {
        return programaAcademico;
    }

    public void setProgramaAcademico(String programaAcademico) {
        this.programaAcademico = programaAcademico;
    }

    public int getSemestreActual() {
        return semestreActual;
    }

    public void setSemestreActual(int semestreActual) {
        this.semestreActual = semestreActual;
    }
}

