package com.proyecto.vigilancia.vigilancia.controller;

import com.proyecto.vigilancia.vigilancia.model.Alerta;
import com.proyecto.vigilancia.vigilancia.service.AlertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaApiController {

    private final AlertaService alertaService;

    public AlertaApiController(AlertaService alertaService) {
        this.alertaService = alertaService;
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
}
