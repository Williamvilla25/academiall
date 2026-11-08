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

    private String ultimoError = null;

    public String getUltimoError() {
        return ultimoError;
    }

    private static final String SELECT_BASE =
            "SELECT u.id_usuario, u.nombre, u.apellido, u.correo, u.telefono, u.contrasena, " +
            "       u.rol::text AS rol, u.estado::text AS estado, " +
            "       a.nivel_acceso, a.departamento, " +
            "       p.codigo_profesor, p.especialidad, " +
            "       e.codigo_estudiantil, e.programa_academico, e.semestre_actual " +
            "FROM usuarios u " +
            "LEFT JOIN administradores a ON u.id_usuario = a.id_usuario " +
            "LEFT JOIN profesores p ON u.id_usuario = p.id_usuario " +
            "LEFT JOIN estudiantes e ON u.id_usuario = e.id_usuario ";

    @Override
    public Usuario buscarPorCorreo(String correo) {
        String sql = SELECT_BASE + "WHERE u.correo = ?";
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
            ultimoError = e.getMessage();
            System.err.println("Error en buscarPorCorreo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario buscarPorId(String idUsuario) {
        String sql = SELECT_BASE + "WHERE u.id_usuario = ?";
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
            ultimoError = e.getMessage();
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
        ultimoError = null;
        String sqlUsuario = "INSERT INTO usuarios (id_usuario, nombre, apellido, correo, telefono, contrasena, rol, estado) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?::rol_usuario, ?::estado_usuario)";
        Connection conn = null;
        try {
            conn = ConexionBD.obtenerInstancia().getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setString(1, usuario.getIdUsuario());
                stmt.setString(2, usuario.getNombre());
                stmt.setString(3, usuario.getApellido());
                stmt.setString(4, usuario.getCorreo());
                stmt.setString(5, usuario.getTelefono());
                stmt.setString(6, usuario.getContrasena());
                stmt.setString(7, usuario.getRol() != null ? usuario.getRol().name().toUpperCase() : "ESTUDIANTE");
                stmt.setString(8, usuario.getEstado() != null ? usuario.getEstado().name().toUpperCase() : "ACTIVO");
                stmt.executeUpdate();
            }

            if (usuario instanceof Administrador admin) {
                String sqlAdmin = "INSERT INTO administradores (id_usuario, nivel_acceso, departamento) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlAdmin)) {
                    stmt.setString(1, admin.getIdUsuario());
                    stmt.setString(2, admin.getNivelAcceso());
                    stmt.setString(3, admin.getDepartamento());
                    stmt.executeUpdate();
                }
            } else if (usuario instanceof Profesor prof) {
                String sqlProf = "INSERT INTO profesores (id_usuario, codigo_profesor, especialidad) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlProf)) {
                    stmt.setString(1, prof.getIdUsuario());
                    stmt.setString(2, prof.getCodigoProfesor());
                    stmt.setString(3, prof.getEspecialidad());
                    stmt.executeUpdate();
                }
            } else if (usuario instanceof Estudiante est) {
                String sqlEst = "INSERT INTO estudiantes (id_usuario, codigo_estudiantil, programa_academico, semestre_actual) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlEst)) {
                    stmt.setString(1, est.getIdUsuario());
                    stmt.setString(2, est.getCodigoEstudiantil());
                    stmt.setString(3, est.getProgramaAcademico());
                    stmt.setInt(4, est.getSemestreActual());
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            ultimoError = e.getMessage();
            System.err.println("Error en insertar usuario: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.err.println("Error en rollback: " + ex.getMessage());
                }
            }
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        ultimoError = null;
        String sqlUsuario = "UPDATE usuarios SET nombre = ?, apellido = ?, telefono = ?, contrasena = ?, " +
                            "rol = ?::rol_usuario, estado = ?::estado_usuario WHERE id_usuario = ?";
        Connection conn = null;
        try {
            conn = ConexionBD.obtenerInstancia().getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlUsuario)) {
                stmt.setString(1, usuario.getNombre());
                stmt.setString(2, usuario.getApellido());
                stmt.setString(3, usuario.getTelefono());
                stmt.setString(4, usuario.getContrasena());
                stmt.setString(5, usuario.getRol() != null ? usuario.getRol().name().toUpperCase() : "ESTUDIANTE");
                stmt.setString(6, usuario.getEstado() != null ? usuario.getEstado().name().toUpperCase() : "ACTIVO");
                stmt.setString(7, usuario.getIdUsuario());
                stmt.executeUpdate();
            }

            if (usuario instanceof Administrador admin) {
                String sqlAdmin = "INSERT INTO administradores (id_usuario, nivel_acceso, departamento) VALUES (?, ?, ?) " +
                                  "ON CONFLICT (id_usuario) DO UPDATE SET nivel_acceso = EXCLUDED.nivel_acceso, departamento = EXCLUDED.departamento";
                try (PreparedStatement stmt = conn.prepareStatement(sqlAdmin)) {
                    stmt.setString(1, admin.getIdUsuario());
                    stmt.setString(2, admin.getNivelAcceso());
                    stmt.setString(3, admin.getDepartamento());
                    stmt.executeUpdate();
                }
            } else if (usuario instanceof Profesor prof) {
                String sqlProf = "INSERT INTO profesores (id_usuario, codigo_profesor, especialidad) VALUES (?, ?, ?) " +
                                 "ON CONFLICT (id_usuario) DO UPDATE SET codigo_profesor = EXCLUDED.codigo_profesor, especialidad = EXCLUDED.especialidad";
                try (PreparedStatement stmt = conn.prepareStatement(sqlProf)) {
                    stmt.setString(1, prof.getIdUsuario());
                    stmt.setString(2, prof.getCodigoProfesor());
                    stmt.setString(3, prof.getEspecialidad());
                    stmt.executeUpdate();
                }
            } else if (usuario instanceof Estudiante est) {
                String sqlEst = "INSERT INTO estudiantes (id_usuario, codigo_estudiantil, programa_academico, semestre_actual) VALUES (?, ?, ?, ?) " +
                                "ON CONFLICT (id_usuario) DO UPDATE SET codigo_estudiantil = EXCLUDED.codigo_estudiantil, programa_academico = EXCLUDED.programa_academico, semestre_actual = EXCLUDED.semestre_actual";
                try (PreparedStatement stmt = conn.prepareStatement(sqlEst)) {
                    stmt.setString(1, est.getIdUsuario());
                    stmt.setString(2, est.getCodigoEstudiantil());
                    stmt.setString(3, est.getProgramaAcademico());
                    stmt.setInt(4, est.getSemestreActual());
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            ultimoError = e.getMessage();
            System.err.println("Error en actualizar usuario: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.err.println("Error en rollback: " + ex.getMessage());
                }
            }
            return false;
        }
    }

    @Override
    public boolean actualizarEstado(String idUsuario, EstadoUsuario estado) {
        ultimoError = null;
        String sql = "UPDATE usuarios SET estado = ?::estado_usuario WHERE id_usuario = ?";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, estado.name().toUpperCase());
                stmt.setString(2, idUsuario);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            ultimoError = e.getMessage();
            System.err.println("Error en actualizarEstado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY u.id_usuario ASC";
        try {
            Connection conn = ConexionBD.obtenerInstancia().getConexion();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            ultimoError = e.getMessage();
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
        
        String rolStr = rs.getString("rol");
        RolUsuario rol = parseRol(rolStr);

        String estadoStr = rs.getString("estado");
        EstadoUsuario estado = parseEstado(estadoStr);

        if (rol == RolUsuario.ADMINISTRADOR) {
            String nivelAcceso = rs.getString("nivel_acceso");
            String departamento = rs.getString("departamento");
            return new Administrador(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, nivelAcceso, departamento);
        } else if (rol == RolUsuario.PROFESOR) {
            String especialidad = rs.getString("especialidad");
            String codigoProfesor = rs.getString("codigo_profesor");
            return new Profesor(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, especialidad, codigoProfesor);
        } else if (rol == RolUsuario.ESTUDIANTE) {
            String codigoEstudiantil = rs.getString("codigo_estudiantil");
            String programaAcademico = rs.getString("programa_academico");
            int semestre = rs.getInt("semestre_actual");
            return new Estudiante(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, codigoEstudiantil, programaAcademico, semestre);
        } else {
            return new Usuario(idUsuario, nombre, apellido, correo, telefono, contrasena, estado, rol);
        }
    }

    private RolUsuario parseRol(String rolStr) {
        if (rolStr == null) return RolUsuario.ESTUDIANTE;
        try {
            return RolUsuario.valueOf(rolStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return RolUsuario.ESTUDIANTE;
        }
    }

    private EstadoUsuario parseEstado(String estadoStr) {
        if (estadoStr == null) return EstadoUsuario.ACTIVO;
        try {
            return EstadoUsuario.valueOf(estadoStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return EstadoUsuario.ACTIVO;
        }
    }
}
