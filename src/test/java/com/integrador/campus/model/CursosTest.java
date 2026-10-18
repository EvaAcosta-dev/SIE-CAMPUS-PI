package com.integrador.campus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Timestamp;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class CursosTest {

    @Test
    @DisplayName("Constructor vacío crea objeto con valores por defecto")
    void constructorVacioValoresDefecto() {
        Cursos c = new Cursos();
        assertNotNull(c);
        assertEquals(0, c.getIdCurso());
        assertNull(c.getNombreCurso());
        assertEquals(0, c.getIdGrado());
        assertEquals(0, c.getIdAnio());
        assertEquals(0, c.getIdJornada());
        assertNull(c.getIdDocenteDirector());
    }

    @Test
    @DisplayName("Constructor con argumentos asigna todos los campos")
    void constructorConArgumentos() {
        Timestamp ahora = Timestamp.from(Instant.now());
        Cursos c = new Cursos(1, 2, "Sexto A", 2026, 10, 3, ahora, ahora);
        assertEquals(1, c.getIdCurso());
        assertEquals(2, c.getIdGrado());
        assertEquals("Sexto A", c.getNombreCurso());
        assertEquals(2026, c.getIdAnio());
        assertEquals(10, c.getIdDocenteDirector());
        assertEquals(3, c.getIdJornada());
        assertEquals(ahora, c.getFechaCreacion());
        assertEquals(ahora, c.getFechaActualizacion());
    }

    @Test
    @DisplayName("Setters y getters de todos los campos")
    void settersYGetters() {
        Cursos c = new Cursos();
        c.setIdCurso(7);
        c.setNombreCurso("Primero B");
        c.setIdGrado(1);
        c.setIdAnio(2025);
        c.setIdDocenteDirector(null);
        c.setIdJornada(2);
        Timestamp ts = Timestamp.from(Instant.now());
        c.setFechaCreacion(ts);
        c.setFechaActualizacion(ts);
        c.setNombreGrado("Primero");
        c.setAnio(2025);
        c.setNombreDirector("Juan Pérez");
        c.setNombreJornada("Mañana");
        c.setNombreNivel("Nivel Inicial");

        assertEquals(7, c.getIdCurso());
        assertEquals("Primero B", c.getNombreCurso());
        assertEquals(1, c.getIdGrado());
        assertEquals(2025, c.getIdAnio());
        assertNull(c.getIdDocenteDirector());
        assertEquals(2, c.getIdJornada());
        assertEquals(ts, c.getFechaCreacion());
        assertEquals(ts, c.getFechaActualizacion());
        assertEquals("Primero", c.getNombreGrado());
        assertEquals(2025, c.getAnio());
        assertEquals("Juan Pérez", c.getNombreDirector());
        assertEquals("Mañana", c.getNombreJornada());
        assertEquals("Nivel Inicial", c.getNombreNivel());
    }

    @Test
    @DisplayName("toString combina nombre, grado y anio")
    void toStringCombinaCampos() {
        Cursos c = new Cursos();
        c.setNombreCurso("Matemáticas");
        c.setNombreGrado("Sexto");
        c.setAnio(2026);
        assertEquals("Matemáticas - Sexto - 2026", c.toString());
    }

    @Test
    @DisplayName("idDocenteDirector acepta null")
    void idDocenteDirectorAceptaNull() {
        Cursos c = new Cursos();
        c.setIdDocenteDirector(null);
        assertNull(c.getIdDocenteDirector());
    }
}
