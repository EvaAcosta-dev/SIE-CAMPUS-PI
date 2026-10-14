package com.integrador.campus.Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncriptarPass {
    
    public static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encriptarContrasena(String contrasena) {
        return encoder.encode(contrasena);
    }

    public static boolean verificarContrasena(String contrasenaIngresada, String hashGuardado) {
        return encoder.matches(contrasenaIngresada, hashGuardado);
    }
}
