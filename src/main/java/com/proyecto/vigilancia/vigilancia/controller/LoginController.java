package com.proyecto.vigilancia.vigilancia.controller;

import com.proyecto.vigilancia.vigilancia.dto.LoginRequest;
import com.proyecto.vigilancia.vigilancia.dto.LoginResponse;
import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        Optional<Usuario> usuario = usuarioService.validarLogin(request.getUsuario(), request.getContrasena());
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            
            // 🔐 GUARDAR EN SESIÓN
            session.setAttribute("usuario", u.getUsuario());
            session.setAttribute("rol", u.getRol());
            session.setAttribute("idUsuario", u.getIdUsuario());
            session.setAttribute("nombre", u.getNombre());
            
            // Configurar tiempo de expiración de sesión (30 minutos)
            session.setMaxInactiveInterval(30 * 60);
            
            System.out.println("✅ Login exitoso para: " + u.getUsuario() + " - Sesión ID: " + session.getId());
            
            return ResponseEntity.ok(
                new LoginResponse(true, "Acceso correcto", u.getRol(), u.getIdUsuario(), u.getNombre())
            );
        }
        
        System.out.println("❌ Login fallido para: " + request.getUsuario());
        return ResponseEntity.status(401).body(
            new LoginResponse(false, "Credenciales incorrectas", null, null, null)
        );
    }

    // 🔓 Endpoint para logout
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(
            new LoginResponse(true, "Sesión cerrada correctamente", null, null, null)
        );
    }

    // 🔍 Endpoint para verificar sesión activa
    @GetMapping("/check-session")
    public ResponseEntity<LoginResponse> checkSession(HttpSession session) {
        String usuario = (String) session.getAttribute("usuario");
        if (usuario != null) {
            return ResponseEntity.ok(
                new LoginResponse(true, "Sesión activa", 
                    (String) session.getAttribute("rol"), 
                    (Integer) session.getAttribute("idUsuario"),
                    (String) session.getAttribute("nombre"))
            );
        }
        return ResponseEntity.status(401).body(
            new LoginResponse(false, "No hay sesión activa", null, null, null)
        );
    }
}