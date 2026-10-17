package com.integrador.campus.Util;

import java.util.Map;
import java.util.Set;

public class Permisos {

    private static final Set<String> TODOS = Set.of(
        "Inicio", "Usuarios", "Estudiantes", "Docentes", "Acudientes",
        "Academico", "Cursos y materias", "Matrículas",
        "Asignaciones docentes", "Notas", "Boletines",
        "Asistencia", "Convivencia", "Notificaciones"
    );

    private static final Map<String, Set<String>> MODULOS_POR_ROL = Map.of(
        "ADMIN", TODOS,

        "REGISTRO ACADEMICO", Set.of(
            "Inicio", "Estudiantes", "Docentes", "Acudientes", "Academico",
            "Cursos y materias", "Matrículas", "Asignaciones docentes",
            "Notas", "Boletines", "Asistencia", "Convivencia", "Notificaciones"
        ),

        "DOCENTE", Set.of(
            "Inicio", "Estudiantes", "Notas", "Asistencia",
            "Convivencia", "Notificaciones"
        ),

        "PADRE", Set.of(
            "Inicio", "Notas", "Boletines", "Asistencia",
            "Convivencia", "Notificaciones"
        ),

        "ESTUDIANTE", Set.of(
            "Inicio", "Notas", "Boletines", "Asistencia", "Notificaciones"
        )
    );

    public static boolean puedeAcceder(String rol, String modulo) {
        if (rol == null) {
            return false;
        }
        Set<String> modulos = MODULOS_POR_ROL.get(rol.trim().toUpperCase());
        return modulos != null && modulos.contains(modulo);
    }
}