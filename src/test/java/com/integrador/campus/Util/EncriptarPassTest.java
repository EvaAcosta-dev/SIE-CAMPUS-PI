package com.integrador.campus.Util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class EncriptarPassTest {

    @Test
    @DisplayName("encriptarContrasena genera un hash no vacío y diferente a la entrada")
    void encriptaGeneraHashDiferente() {
        String hash = EncriptarPass.encriptarContrasena("Clave123");
        assertNotEquals("Clave123", hash);
        assertFalse(hash.isBlank());
    }

    @Test
    @DisplayName("verificarContrasena devuelve true con contraseña correcta")
    void verificaContrasenaCorrecta() {
        String hash = EncriptarPass.encriptarContrasena("MiPass2026");
        assertTrue(EncriptarPass.verificarContrasena("MiPass2026", hash));
    }

    @Test
    @DisplayName("verificarContrasena devuelve false con contraseña incorrecta")
    void verificaContrasenaIncorrecta() {
        String hash = EncriptarPass.encriptarContrasena("MiPass2026");
        assertFalse(EncriptarPass.verificarContrasena("OtraClave", hash));
    }

    @Test
    @DisplayName("misma contraseña genera hashes diferentes (BCrypt usa salt)")
    void mismoTextoHashesDiferentes() {
        String h1 = EncriptarPass.encriptarContrasena("Igual");
        String h2 = EncriptarPass.encriptarContrasena("Igual");
        assertNotEquals(h1, h2);
        assertTrue(EncriptarPass.verificarContrasena("Igual", h1));
        assertTrue(EncriptarPass.verificarContrasena("Igual", h2));
    }
}
