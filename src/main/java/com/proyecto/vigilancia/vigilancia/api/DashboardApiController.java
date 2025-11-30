package com.proyecto.vigilancia.vigilancia.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.vigilancia.vigilancia.model.EstadoCamara;
import com.proyecto.vigilancia.vigilancia.repository.CamaraRepository;
import com.proyecto.vigilancia.vigilancia.service.AlertaService;
import com.proyecto.vigilancia.vigilancia.service.UsuarioService;

import java.util.Map;

@RestController
public class DashboardApiController {

    private final CamaraRepository camaraRepository;
    private final AlertaService alertaService;
    private final UsuarioService usuarioService; 

    public DashboardApiController(CamaraRepository camaraRepository,
                                  AlertaService alertaService,
                                  UsuarioService usuarioService) { 
        this.camaraRepository = camaraRepository;
        this.alertaService = alertaService;
        this.usuarioService = usuarioService; // 
    }

    @GetMapping("/api/camaras/estado")
    public Map<String, Long> getEstadoCamaras() {
        long activas = camaraRepository.countByEstado(EstadoCamara.ACTIVA);
        long inactivas = camaraRepository.countByEstado(EstadoCamara.INACTIVA);
        return Map.of("activas", activas, "inactivas", inactivas);
    }

    @GetMapping("/api/alertas/recientes")
    public Map<String, Integer> getAlertasRecientes() {
        return alertaService.obtenerAlertasRecientesAgrupadas();
    }


    @GetMapping("/api/dashboard/detecciones")
    public Map<String, Integer> getDeteccionesPorHora() {
        // Datos de ejemplo - puedes conectar esto a tu DeteccionService después
        return Map.of(
            "08:00", 12,
            "10:00", 18, 
            "12:00", 25,
            "14:00", 15,
            "16:00", 20
        );
    }

    @GetMapping("/api/usuarios/activos")
    public Map<String, Long> getUsuariosActivos() {
        // Datos de ejemplo - puedes conectar esto a tu UsuarioService después
        long administradores = 2L;  // Ejemplo: contar usuarios con rol ADMINISTRADOR
        long operadores = 5L;       // Ejemplo: contar usuarios con rol OPERADOR
        
        return Map.of(
            "administradores", administradores,
            "operadores", operadores
        );
    }
}