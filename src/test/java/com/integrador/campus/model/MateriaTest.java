package com.integrador.campus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class MateriaTest {

    @Test
    @DisplayName("Constructor vacía crea objeto con valores por defecto")
    void constructorVacioValoresDefecto() {
        Materia m = new Materia();
        assertNotNull(m);
        assertEquals(0, m.getIdMateria());
        assertNull(m.getNombreMateria());
    }

    @Test
    @DisplayName("Constructor con argumentos asigna los campos")
    void constructorConArgumentos() {
        Materia m = new Materia(1, "Matemáticas");
        assertEquals(1, m.getIdMateria());
        assertEquals("Matemáticas", m.getNombreMateria());
    }

    @Test
    @DisplayName("Setters y getters de idMateria y nombreMateria")
    void settersYGetters() {
        Materia m = new Materia();
        m.setIdMateria(42);
        m.setNombreMateria("Física");
        assertEquals(42, m.getIdMateria());
        assertEquals("Física", m.getNombreMateria());
    }

    @Test
    @DisplayName("toString devuelve el nombre de la materia")
    void toStringDevuelveNombre() {
        Materia m = new Materia(5, "Química");
        assertEquals("Química", m.toString());
    }
}
