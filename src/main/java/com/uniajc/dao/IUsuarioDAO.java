package com.uniajc.dao;

import java.util.List;

import com.uniajc.modelo.Usuario;

/**
 * Contrato de persistencia para el módulo de Usuarios y Autenticación.
 */
public interface IUsuarioDAO {
    Usuario buscarPorCorreo(String correo);
    Usuario buscarPorId(String idUsuario);
    Usuario autenticar(String correo, String contrasena);
    boolean insertar(Usuario usuario);
    boolean actualizar(Usuario usuario);
    boolean actualizarEstado(String idUsuario, com.uniajc.modelo.enums.EstadoUsuario estado);
    List<Usuario> listarTodos();
}

