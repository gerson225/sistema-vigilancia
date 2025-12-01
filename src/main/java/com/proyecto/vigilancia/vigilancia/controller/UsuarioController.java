package com.proyecto.vigilancia.vigilancia.controller;

import com.proyecto.vigilancia.vigilancia.dto.RegisterRequest;
import com.proyecto.vigilancia.vigilancia.dto.LoginResponse;
import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 🚀 Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        Usuario nuevo = usuarioService.registrarUsuario(
            request.getNombre(),
            request.getUsuario(),
            request.getContrasena(),
            request.getRol()
        );

        return ResponseEntity.ok(
            new LoginResponse(true, "Usuario registrado correctamente", nuevo.getRol(), nuevo.getIdUsuario(), nuevo.getNombre())
        );
    }

    // 🚀 Endpoint para corregir contraseñas inseguras en la BD
    @PostMapping("/fix-passwords")
    public ResponseEntity<String> fixPasswords() {
        usuarioService.encriptarContraseñasInseguras();
        return ResponseEntity.ok("Contraseñas inseguras corregidas correctamente");
    }
}
