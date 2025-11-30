package com.proyecto.vigilancia.vigilancia.controller;

import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.model.Alerta;
import com.proyecto.vigilancia.vigilancia.service.AlertaService;
import com.proyecto.vigilancia.vigilancia.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
public class AlertaApiController {

    private final AlertaService alertaService;
    private final UsuarioService usuarioService;

    public AlertaApiController(AlertaService alertaService, UsuarioService usuarioService) {
        this.alertaService = alertaService;
        this.usuarioService = usuarioService;
    }

    // Listar últimas alertas (por ahora 10)
    @GetMapping
    public ResponseEntity<List<Alerta>> listarAlertas() {
        List<Alerta> alertas = alertaService.obtenerAlertasRecientes();
        return ResponseEntity.ok(alertas);
    }

    // Crear una alerta
    @PostMapping
    public ResponseEntity<Alerta> crearAlerta(@Valid @RequestBody Alerta alerta) {
        Alerta nueva = alertaService.registrarAlerta(alerta);
        return ResponseEntity.ok(nueva);
    }

    // Resolver alerta
    @PostMapping("/{idAlerta}/resolver")
    public ResponseEntity<Alerta> resolverAlerta(@PathVariable Integer idAlerta, 
                                                @RequestBody Map<String, String> request) {
        try {
            String accionesTomadas = request.get("accionesTomadas");
            Alerta alerta = alertaService.resolverAlerta(idAlerta, accionesTomadas);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // Obtener detalle de alerta
    @GetMapping("/{idAlerta}")
    public ResponseEntity<Alerta> obtenerAlerta(@PathVariable Integer idAlerta) {
        try {
            Alerta alerta = alertaService.buscarPorId(idAlerta);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
}
    @PostMapping("/{idAlerta}/asignar")
    public ResponseEntity<Alerta> asignarAlerta(@PathVariable Integer idAlerta, HttpSession session) {
        try {
            // Obtener el usuario de la sesión
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");
            if (idUsuario == null) {
                return ResponseEntity.status(401).build();
            }

            // Buscar el usuario
            Usuario usuario = usuarioService.buscarPorId(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Asignar la alerta
            Alerta alerta = alertaService.asignarResponsable(idAlerta, usuario);
            return ResponseEntity.ok(alerta);

        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
    // Endpoint para filtrar alertas
@PostMapping("/filtrar")
public ResponseEntity<List<Alerta>> filtrarAlertas(@RequestBody Map<String, String> filtros) {
    try {
        String estado = filtros.get("estado");
        String nivelCriticidad = filtros.get("nivelCriticidad");
        
        List<Alerta> alertasFiltradas = alertaService.filtrarAlertas(estado, nivelCriticidad);
        return ResponseEntity.ok(alertasFiltradas);
    } catch (Exception e) {
        return ResponseEntity.status(500).build();
    }
}
}