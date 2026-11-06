package com.uniajc.modelo;

import com.uniajc.modelo.enums.EstadoUsuario;
import com.uniajc.modelo.enums.RolUsuario;

public class Usuario {
    protected String idUsuario;
    protected String nombre;
    protected String apellido;
    protected String correo;
    protected String telefono;
    protected String contrasena;
    protected EstadoUsuario estado;
    protected RolUsuario rol;

    public Usuario() {
        this.estado = EstadoUsuario.ACTIVO;
    }

    public Usuario(String idUsuario, String nombre, String apellido, String correo, String telefono, String contrasena, EstadoUsuario estado, RolUsuario rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.estado = estado;
        this.rol = rol;
    }

    public boolean iniciarSesion(String contrasenaIngresada) {
        if (this.estado != EstadoUsuario.ACTIVO) {
            return false;
        }
        return this.contrasena != null && this.contrasena.equals(contrasenaIngresada);
    }

    public void cerrarSesion() {
        // Lógica de finalización de sesión si aplica
    }

    public void actualizarDatos(String nombre, String apellido, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    // Getters y Setters
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "").trim();
    }
}

