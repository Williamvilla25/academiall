package com.uniajc.modelo;

import com.uniajc.modelo.enums.EstadoUsuario;
import com.uniajc.modelo.enums.RolUsuario;

public class Profesor extends Usuario {
    private String especialidad;
    private String codigoProfesor;

    public Profesor() {
        super();
        this.rol = RolUsuario.PROFESOR;
    }

    public Profesor(String idUsuario, String nombre, String apellido, String correo, String telefono,
                    String contrasena, EstadoUsuario estado, String especialidad, String codigoProfesor) {
        super(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, RolUsuario.PROFESOR);
        this.especialidad = especialidad;
        this.codigoProfesor = codigoProfesor;
    }

    public void registrarNotas() {
        // Implementado por el módulo del profesor
    }

    public void verMateriasAsignadas() {
        // Implementado por el módulo del profesor
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCodigoProfesor() {
        return codigoProfesor;
    }

    public void setCodigoProfesor(String codigoProfesor) {
        this.codigoProfesor = codigoProfesor;
    }
}

