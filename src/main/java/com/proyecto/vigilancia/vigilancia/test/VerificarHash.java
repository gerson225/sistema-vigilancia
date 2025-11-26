package com.proyecto.vigilancia.vigilancia.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class VerificarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String nuevoHash = encoder.encode("admin123");
        System.out.println("Hash generado: " + nuevoHash);
    }
}
