# Plan de Distribución del Proyecto: Academiall
### Integrantes: William Villa, Alejandro Ibarra, Juan Pablo Rubiano
---

## 📌 Arquitectura del Sistema (MVC + DAO + Tests)

El proyecto implementa una arquitectura por capas desacoplada mediante **Interfaces de Java (`<<interface>>`)** y validada con **Pruebas Unitarias en JUnit 5**:

```text
src/
├── main/java/com/academiall/
│   ├── model/               <- Entidades del dominio y Enums
│   │   ├── Usuario.java (Administrador, Profesor, Estudiante)
│   │   ├── Materia.java
│   │   ├── Matricula.java
│   │   ├── Calificacion.java
│   │   └── enums/ (EstadoUsuario, EstadoMatricula, RolUsuario)
│   ├── dao/                 <- Interfaces Java (Contratos de Persistencia)
│   │   ├── IUsuarioDAO.java
│   │   ├── IMateriaDAO.java
│   │   ├── IMatriculaDAO.java
│   │   └── ICalificacionDAO.java
│   ├── dao/impl/            <- Implementaciones JDBC conectadas a Neon Tech
│   │   ├── UsuarioDAOImpl.java
│   │   ├── MateriaDAOImpl.java
│   │   ├── MatriculaDAOImpl.java
│   │   └── CalificacionDAOImpl.java
│   ├── controller/          <- Controladores (Inyectan y dependen de las Interfaces DAO)
│   │   ├── ControladorLogin.java
│   │   ├── ControladorAcademico.java
│   │   └── ControladorNotas.java
│   ├── view/                <- Interfaz Gráfica (Swing / JavaFX)
│   │   ├── VistaLogin.java
│   │   ├── VistaAdminDashboard.java
│   │   ├── VistaEstudianteDashboard.java
│   │   └── VistaProfesorDashboard.java
│   └── util/
│       └── ConexionBD.java  <- Patrón Singleton para conexión JDBC a PostgreSQL (Neon)
└── test/java/com/academiall/ <- Pruebas Unitarias con JUnit 5
    ├── model/
    ├── dao/
    └── controller/
```

---

## 👥 Asignación de Responsabilidades por Integrante

### 👤 Integrante 1 (Alejandro Ibarra): Módulo de Seguridad, Autenticación y Administración
* **Contratos / Interfaces Java:**
  * Crear la interfaz `IUsuarioDAO`.
* **Implementación & Base de Datos:**
  * Implementar `UsuarioDAOImpl implements IUsuarioDAO` (consultas SQL contra Neon, autenticación, control de estados).
  * Diseñar la clase `ConexionBD` (Patrón Singleton, gestión de conexiones JDBC con Neon).
* **Controlador & Vistas:**
  * `ControladorLogin` (inyecta `IUsuarioDAO`, valida credenciales y redirecciona por `RolUsuario`).
  * `VistaLogin` (captura de credenciales y feedback de error).
  * `VistaAdminDashboard` (gestión de usuarios y panel global).
* **Pruebas Unitarias con JUnit (`src/test`):**
  * `UsuarioDAOTest`:
    * `testAutenticacionExitosa()`: valida login con credenciales correctas.
    * `testCorreoInexistente()`: asegura manejo seguro de usuarios no registrados.
  * `UsuarioTest`: prueba integridad de getters, setters y transición de `EstadoUsuario` (ACTIVO, BLOQUEADO).

---

### 👤 Integrante 2 (William Villa): Módulo Académico, Oferta y Matrículas
* **Contratos / Interfaces Java:**
  * Crear las interfaces `IMateriaDAO` e `IMatriculaDAO`.
* **Implementación & Base de Datos:**
  * Implementar `MateriaDAOImpl implements IMateriaDAO` (CRUD de materias y asignación a profesores).
  * Implementar `MatriculaDAOImpl implements IMatriculaDAO` (persistencia con tipo fuerte `LocalDate` para fechas).
* **Controlador & Vistas:**
  * `ControladorAcademico` (inyecta `IMatriculaDAO` e `IMateriaDAO`, valida que no se dupliquen asignaturas).
  * `VistaEstudianteDashboard` (consulta de materias inscritas).
  * Formulario / Modal para selección e inscripción de asignaturas disponibles.
* **Pruebas Unitarias con JUnit (`src/test`):**
  * `MatriculaTest`:
    * `testMatriculaFechaActual()`: valida registro con `LocalDate.now()`.
    * `testValidarEstadoInicial()`: comprueba que toda nueva matrícula inicie en `INSCRITA`.
  * `MateriaDAOTest`: listar materias asignadas a un profesor específico.

---

### 👤 Integrante 3 (Juan Pablo Rubiano): Módulo Docente, Calificaciones y Reportes
* **Contratos / Interfaces Java:**
  * Crear la interfaz `ICalificacionDAO`.
* **Implementación & Base de Datos:**
  * Implementar `CalificacionDAOImpl implements ICalificacionDAO` (garantiza que la nota cuelgue exclusivamente de `id_matricula` y use `BigDecimal` / `NUMERIC(4,2)`).
* **Controlador & Vistas:**
  * `ControladorNotas` (inyecta `ICalificacionDAO` e `IMatriculaDAO`, calcula promedios y actualiza estado de matrícula a `APROBADA` o `REPROBADA`).
  * `VistaProfesorDashboard` (tabla interactiva con estudiantes matriculados en sus cursos).
  * Formulario / Tabla editable para digitación de `notaParcial1`, `notaParcial2` y observaciones.
* **Pruebas Unitarias con JUnit (`src/test`):**
  * `CalificacionTest`:
    * `testCalcularPromedioFinalAprobado()`: con `BigDecimal("4.5")` y `BigDecimal("3.5")`, comprueba promedio $\ge 3.0$.
    * `testCalcularPromedioFinalReprobado()`: comprueba que promedio $< 3.0$ resulte reprobado.
    * `testPrecisionDecimalBigDecimal()`: comprueba exactitud decimal sin pérdida de precisión de punto flotante.
  * `ControladorNotasTest`:
    * `testRegistrarCalificacionRangoValido()`: valida rango permitido [0.0 - 5.0].
    * `testRegistrarCalificacionRangoInvalido()`: comprueba lanzamiento de excepción ante notas fuera de rango.

---

## 🎯 Beneficios de esta Estructura para la Entrega
1. **Desacoplamiento Real:** Los controladores dependen de **interfaces**, no de implementaciones concretas, cumpliendo el principio de Inversión de Dependencias (SOLID).
2. **Independencia en Git:** Cada integrante trabaja en sus propios archivos de interfaz, implementación y pruebas sin riesgo de conflictos al hacer merge.
3. **Facilidad de Testeo:** Los tests unitarios con JUnit pueden probar modelos y lógica de negocio de manera aislada antes de conectar a la base de datos de Neon.
