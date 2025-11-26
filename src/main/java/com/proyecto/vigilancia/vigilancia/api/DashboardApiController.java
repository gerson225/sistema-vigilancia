package com.proyecto.vigilancia.vigilancia.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.vigilancia.vigilancia.model.EstadoCamara;
import com.proyecto.vigilancia.vigilancia.repository.CamaraRepository;
import com.proyecto.vigilancia.vigilancia.service.AlertaService;

import java.util.Map;

@RestController
public class DashboardApiController {

    private final CamaraRepository camaraRepository;
    private final AlertaService alertaService;

    public DashboardApiController(CamaraRepository camaraRepository,
                                  AlertaService alertaService) {
        this.camaraRepository = camaraRepository;
        this.alertaService = alertaService;
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
}
