package com.integrador.campus.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class UsuarioTest {

    @Test
    @DisplayName("Constructor vacío crea usuario con valores por defecto")
    void constructorVacioValoresDefecto() {
        Usuario u = new Usuario();
        assertNotNull(u);
        assertEquals(0, u.getIdUsuario());
        assertNull(u.getNombre());
        assertNull(u.getCorreo());
        assertFalse(u.isActivo());
    }

    @Test
    @DisplayName("Setters y getters de todos los campos")
    void settersYGetters() {
        Usuario u = new Usuario();
        Rol rol = new Rol(1, "Administrador");
        u.setIdUsuario(5);
        u.setNombre("Ana");
        u.setApellido("Gómez");
        u.setCorreo("ana@campus.edu");
        u.setContrasena("hash123");
        u.setIdRol(1);
        u.setRol(rol);
        u.setActivo(true);

        assertEquals(5, u.getIdUsuario());
        assertEquals("Ana", u.getNombre());
        assertEquals("Gómez", u.getApellido());
        assertEquals("ana@campus.edu", u.getCorreo());
        assertEquals("hash123", u.getContrasena());
        assertEquals(1, u.getIdRol());
        assertEquals(rol, u.getRol());
        assertTrue(u.isActivo());
    }

    @Test
    @DisplayName("Rol getters funcionan correctamente")
    void rolGetters() {
        Rol rol = new Rol(2, "Docente");
        assertEquals(2, rol.getIdRol());
        assertEquals("Docente", rol.getNombreRol());
    }
}
