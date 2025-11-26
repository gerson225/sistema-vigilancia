package com.proyecto.vigilancia.vigilancia.controller;

import com.proyecto.vigilancia.vigilancia.dto.LoginRequest;
import com.proyecto.vigilancia.vigilancia.dto.LoginResponse;
import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuario = usuarioService.validarLogin(request.getUsuario(), request.getContrasena());
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            return ResponseEntity.ok(
                new LoginResponse(true, "Acceso correcto", u.getRol(), u.getIdUsuario())
            );
        }
        return ResponseEntity.status(401).body(
            new LoginResponse(false, "Credenciales incorrectas", null, null)
        );
    }
}

