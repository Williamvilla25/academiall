package com.uniajc.controlador;

import com.uniajc.dao.IUsuarioDAO;
import com.uniajc.modelo.Usuario;
import com.uniajc.modelo.enums.EstadoUsuario;
import com.uniajc.modelo.enums.RolUsuario;

public class ControladorLogin {
    private final IUsuarioDAO usuarioDAO;

    public ControladorLogin(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Valida si las credenciales corresponden a un usuario activo.
     */
    public boolean autenticar(String correo, String contrasena) {
        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioDAO.buscarPorCorreo(correo.trim());
        if (usuario == null) {
            return false;
        }

        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            return false;
        }

        return contrasena.equals(usuario.getContrasena());
    }

    /**
     * Obtiene el usuario autenticado para fines de navegación de sesiones.
     */
    public Usuario obtenerUsuarioAutenticado(String correo, String contrasena) {
        if (autenticar(correo, contrasena)) {
            return usuarioDAO.buscarPorCorreo(correo.trim());
        }
        return null;
    }

    /**
     * Obtiene el rol del usuario especificado.
     */
    public RolUsuario validarRol(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return usuario.getRol();
    }
}

