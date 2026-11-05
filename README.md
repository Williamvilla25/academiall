# Integrantes 
### William Villa
### Alejandro Ibarra
### Juan Pablo Rubiano
---

# Diagrama de clase Mermaid

```mermaid
classDiagram
    direction TB

    %% -----------------------------------------
    %% 0. ENUMS (Tipos Fuertes)
    %% -----------------------------------------
    class EstadoUsuario {
        <<enumeration>>
        ACTIVO
        INACTIVO
        BLOQUEADO
    }

    class EstadoMatricula {
        <<enumeration>>
        INSCRITA
        CANCELADA
        APROBADA
        REPROBADA
    }

    class RolUsuario {
        <<enumeration>>
        ADMINISTRADOR
        PROFESOR
        ESTUDIANTE
    }

    %% -----------------------------------------
    %% 1. JERARQUÍA DE MODELO: USUARIOS
    %% -----------------------------------------
    class Usuario {
        #String idUsuario
        #String nombre
        #String apellido
        #String correo
        #String telefono
        #String contrasena
        #EstadoUsuario estado
        +Usuario()
        +boolean iniciarSesion()
        +void cerrarSesion()
        +void actualizarDatos()
    }

    class Administrador {
        -String nivelAcceso
        -String departamento
        +Administrador()
        +void gestionarUsuarios()
        +void configurarSistema()
        +void generarReportesGlobales()
    }

    class Profesor {
        -String especialidad
        -String codigoProfesor
        +Profesor()
        +void registrarNotas()
        +void verMateriasAsignadas()
    }

    class Estudiante {
        -String codigoEstudiantil
        -String programaAcademico
        -int semestreActual
        +Estudiante()
        +void consultarNotas()
        +void verHistorialAcademico()
    }

    Usuario <|-- Administrador
    Usuario <|-- Profesor
    Usuario <|-- Estudiante
    Usuario o-- EstadoUsuario

    %% -----------------------------------------
    %% 2. MODELO: GESTIÓN ACADÉMICA Y MATRÍCULAS
    %% -----------------------------------------
    class Materia {
        -String codigoMateria
        -String nombreMateria
        -int creditos
        -int semestre
        +Materia()
        +String getCodigoMateria()
        +void setCodigoMateria(String codigoMateria)
        +String getNombreMateria()
        +void setNombreMateria(String nombreMateria)
        +void imprimirInformacion()
    }

    class Matricula {
        -String idMatricula
        -LocalDate fechaMatricula
        -EstadoMatricula estado
        +Matricula()
        +void registrarMatricula()
        +void cancelarMatricula()
        +Calificacion getCalificacion()
    }

    Matricula o-- EstadoMatricula

    %% -----------------------------------------
    %% 3. MODELO: CALIFICACIÓN (Cuelga de Matrícula)
    %% -----------------------------------------
    class Calificacion {
        -String idCalificacion
        -BigDecimal notaParcial1
        -BigDecimal notaParcial2
        -BigDecimal notaFinal
        -String observaciones
        +Calificacion()
        +BigDecimal calcularPromedioFinal()
        +boolean verificarAprobacion()
    }

    %% -----------------------------------------
    %% 4. CAPA DE PERSISTENCIA Y ACCESO A DATOS (DAO)
    %% -----------------------------------------
    class ConexionBD {
        -String url
        -String usuario
        -String contrasena
        -ConexionBD instancia
        -ConexionBD()
        +static ConexionBD obtenerInstancia()
        +Connection getConexion()
        +void desconectar()
    }

    class UsuarioDAO {
        +Usuario buscarPorCorreo(String correo)
        +boolean insertar(Usuario usuario)
        +boolean actualizar(Usuario usuario)
        +List~Usuario~ listarTodos()
    }

    class MatriculaDAO {
        +boolean guardar(Matricula matricula)
        +Matricula buscarPorId(String idMatricula)
        +List~Matricula~ listarPorEstudiante(String codigoEstudiantil)
        +boolean actualizarEstado(String idMatricula, EstadoMatricula estado)
    }

    class MateriaDAO {
        +boolean guardar(Materia materia)
        +Materia buscarPorCodigo(String codigoMateria)
        +List~Materia~ listarPorProfesor(String codigoProfesor)
        +List~Materia~ listarTodas()
    }

    class CalificacionDAO {
        +boolean guardar(Calificacion calificacion, String idMatricula)
        +Calificacion buscarPorMatricula(String idMatricula)
        +boolean actualizar(Calificacion calificacion)
    }

    UsuarioDAO ..> ConexionBD : usa
    MatriculaDAO ..> ConexionBD : usa
    MateriaDAO ..> ConexionBD : usa
    CalificacionDAO ..> ConexionBD : usa

    UsuarioDAO ..> Usuario : manipula
    MatriculaDAO ..> Matricula : manipula
    MateriaDAO ..> Materia : manipula
    CalificacionDAO ..> Calificacion : manipula

    %% -----------------------------------------
    %% 5. CAPA DE CONTROLADORES
    %% -----------------------------------------
    class ControladorLogin {
        -UsuarioDAO usuarioDAO
        +boolean autenticar(String correo, String contrasena)
        +RolUsuario validarRol(Usuario usuario)
    }

    class ControladorAcademico {
        -MatriculaDAO matriculaDAO
        -MateriaDAO materiaDAO
        +boolean matricularMateria(Estudiante estudiante, Materia materia)
        +void gestionarMaterias(Materia materia)
    }

    class ControladorNotas {
        -CalificacionDAO calificacionDAO
        -MatriculaDAO matriculaDAO
        +boolean registrarCalificacion(Matricula matricula, BigDecimal p1, BigDecimal p2)
        +boolean actualizarCalificacion(Calificacion calificacion)
    }

    ControladorLogin --> UsuarioDAO : invoca
    ControladorAcademico --> MatriculaDAO : invoca
    ControladorAcademico --> MateriaDAO : invoca
    ControladorNotas --> CalificacionDAO : invoca
    ControladorNotas --> MatriculaDAO : invoca

    %% -----------------------------------------
    %% 6. CAPA DE VISTAS (UI)
    %% -----------------------------------------
    class VistaLogin {
        -ControladorLogin controlador
        +void mostrarVentana()
        +void capturarCredenciales()
    }

    class VistaAdminDashboard {
        -ControladorAcademico controladorAcademico
        +void mostrarPanelAdmin()
        +void desplegarReportesGlobales()
    }

    class VistaProfesorDashboard {
        -ControladorNotas controladorNotas
        +void mostrarPanelNotas()
        +void actualizarTablaEstudiantes()
    }

    class VistaEstudianteDashboard {
        -ControladorNotas controladorNotas
        -ControladorAcademico controladorAcademico
        +void mostrarCalificaciones()
        +void filtrarMaterias()
    }

    VistaLogin --> ControladorLogin : delega a
    VistaAdminDashboard --> ControladorAcademico : delega a
    VistaProfesorDashboard --> ControladorNotas : delega a
    VistaEstudianteDashboard --> ControladorNotas : delega a
    VistaEstudianteDashboard --> ControladorAcademico : delega a

    %% -----------------------------------------
    %% 7. RELACIONES ESTRUCTURALES DEL DOMINIO
    %% -----------------------------------------
    Estudiante "1" --> "*" Matricula : realiza
    Profesor "1" --> "*" Materia : dicta
    Matricula "*" --> "1" Materia : incluye
    Matricula "1" *-- "0..1" Calificacion : contiene
```
