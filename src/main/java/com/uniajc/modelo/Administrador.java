package com.uniajc.modelo;

import com.uniajc.modelo.enums.EstadoUsuario;
import com.uniajc.modelo.enums.RolUsuario;

public class Administrador extends Usuario {
    private String nivelAcceso;
    private String departamento;

    public Administrador() {
        super();
        this.rol = RolUsuario.ADMINISTRADOR;
    }

    public Administrador(String idUsuario, String nombre, String apellido, String correo, String telefono,
                         String contrasena, EstadoUsuario estado, String nivelAcceso, String departamento) {
        super(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, RolUsuario.ADMINISTRADOR);
        this.nivelAcceso = nivelAcceso;
        this.departamento = departamento;
    }

    public void gestionarUsuarios() {
        // Operaciones de gestión administrativa
    }

    public void configurarSistema() {
        // Configuraciones globales del sistema
    }

    public void generarReportesGlobales() {
        // Reportes globales
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}

