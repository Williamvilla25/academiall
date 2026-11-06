package com.uniajc.dao.impl;

import com.uniajc.dao.IUsuarioDAO;
import com.uniajc.modelo.Administrador;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Profesor;
import com.uniajc.modelo.Usuario;
import com.uniajc.modelo.enums.EstadoUsuario;
import com.uniajc.modelo.enums.RolUsuario;
import com.uniajc.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements IUsuarioDAO {

    public UsuarioDAOImpl() {
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id_usuario VARCHAR(50) PRIMARY KEY, " +
                "nombre VARCHAR(100) NOT NULL, " +
                "apellido VARCHAR(100) NOT NULL, " +
                "correo VARCHAR(150) UNIQUE NOT NULL, " +
                "telefono VARCHAR(30), " +
                "contrasena VARCHAR(100) NOT NULL, " +
                "estado VARCHAR(20) NOT NULL, " +
                "rol VARCHAR(30) NOT NULL, " +
                "campo_extra_1 VARCHAR(100), " +
                "campo_extra_2 VARCHAR(100), " +
                "semestre_actual INT" +
                ");";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.execute();
            }
        } catch (Exception e) {
            System.err.println("Aviso al verificar/crear tabla usuarios: " + e.getMessage());
        }
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, correo);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapearUsuario(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en buscarPorCorreo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario buscarPorId(String idUsuario) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, idUsuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapearUsuario(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en buscarPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuario = buscarPorCorreo(correo);
        if (usuario != null && usuario.getContrasena() != null && usuario.getContrasena().equals(contrasena)) {
            return usuario;
        }
        return null;
    }

    @Override
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (id_usuario, nombre, apellido, correo, telefono, contrasena, estado, rol, campo_extra_1, campo_extra_2, semestre_actual) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usuario.getIdUsuario());
                stmt.setString(2, usuario.getNombre());
                stmt.setString(3, usuario.getApellido());
                stmt.setString(4, usuario.getCorreo());
                stmt.setString(5, usuario.getTelefono());
                stmt.setString(6, usuario.getContrasena());
                stmt.setString(7, usuario.getEstado() != null ? usuario.getEstado().name() : EstadoUsuario.ACTIVO.name());
                stmt.setString(8, usuario.getRol() != null ? usuario.getRol().name() : RolUsuario.ESTUDIANTE.name());

                if (usuario instanceof Administrador admin) {
                    stmt.setString(9, admin.getNivelAcceso());
                    stmt.setString(10, admin.getDepartamento());
                    stmt.setObject(11, null);
                } else if (usuario instanceof Profesor prof) {
                    stmt.setString(9, prof.getEspecialidad());
                    stmt.setString(10, prof.getCodigoProfesor());
                    stmt.setObject(11, null);
                } else if (usuario instanceof Estudiante est) {
                    stmt.setString(9, est.getCodigoEstudiantil());
                    stmt.setString(10, est.getProgramaAcademico());
                    stmt.setInt(11, est.getSemestreActual());
                } else {
                    stmt.setString(9, null);
                    stmt.setString(10, null);
                    stmt.setObject(11, null);
                }

                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error en insertar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, telefono = ?, contrasena = ?, estado = ?, campo_extra_1 = ?, campo_extra_2 = ?, semestre_actual = ? " +
                     "WHERE id_usuario = ?";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usuario.getNombre());
                stmt.setString(2, usuario.getApellido());
                stmt.setString(3, usuario.getTelefono());
                stmt.setString(4, usuario.getContrasena());
                stmt.setString(5, usuario.getEstado() != null ? usuario.getEstado().name() : EstadoUsuario.ACTIVO.name());

                if (usuario instanceof Administrador admin) {
                    stmt.setString(6, admin.getNivelAcceso());
                    stmt.setString(7, admin.getDepartamento());
                    stmt.setObject(8, null);
                } else if (usuario instanceof Profesor prof) {
                    stmt.setString(6, prof.getEspecialidad());
                    stmt.setString(7, prof.getCodigoProfesor());
                    stmt.setObject(8, null);
                } else if (usuario instanceof Estudiante est) {
                    stmt.setString(6, est.getCodigoEstudiantil());
                    stmt.setString(7, est.getProgramaAcademico());
                    stmt.setInt(8, est.getSemestreActual());
                } else {
                    stmt.setString(6, null);
                    stmt.setString(7, null);
                    stmt.setObject(8, null);
                }

                stmt.setString(9, usuario.getIdUsuario());
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error en actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarEstado(String idUsuario, EstadoUsuario estado) {
        String sql = "UPDATE usuarios SET estado = ? WHERE id_usuario = ?";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, estado.name());
                stmt.setString(2, idUsuario);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error en actualizarEstado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id_usuario ASC";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en listarTodos: " + e.getMessage());
        }
        return usuarios;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        String idUsuario = rs.getString("id_usuario");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String correo = rs.getString("correo");
        String telefono = rs.getString("telefono");
        String contrasena = rs.getString("contrasena");
        EstadoUsuario estado = EstadoUsuario.valueOf(rs.getString("estado"));
        RolUsuario rol = RolUsuario.valueOf(rs.getString("rol"));

        String extra1 = rs.getString("campo_extra_1");
        String extra2 = rs.getString("campo_extra_2");
        int semestre = rs.getInt("semestre_actual");

        switch (rol) {
            case ADMINISTRADOR:
                return new Administrador(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, extra1, extra2);
            case PROFESOR:
                return new Profesor(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, extra1, extra2);
            case ESTUDIANTE:
                return new Estudiante(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, extra1, extra2, semestre);
            default:
                return new Usuario(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, rol);
        }
    }
}

