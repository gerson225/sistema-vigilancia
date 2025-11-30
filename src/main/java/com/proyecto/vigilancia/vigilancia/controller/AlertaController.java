package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.vigilancia.vigilancia.model.Alerta;
import com.proyecto.vigilancia.vigilancia.service.AlertaService;

import java.util.List;

@Controller
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping("/alertas")
    public String mostrarAlertas(Model model) {
        List<Alerta> alertas = alertaService.obtenerTodasLasAlertas();
        
        // Métricas para las cards
        long totalAlertas = alertas.size();
        long alertasPendientes = alertas.stream()
            .filter(a -> "PENDIENTE".equals(a.getEstadoAlerta()))
            .count();
        long alertasCriticas = alertas.stream()
            .filter(a -> "CRITICA".equals(a.getNivelCriticidad()))
            .count();
        long alertasResueltas = alertas.stream()
            .filter(a -> "RESUELTA".equals(a.getEstadoAlerta()))
            .count();
        
        model.addAttribute("alertas", alertas);
        model.addAttribute("totalAlertas", totalAlertas);
        model.addAttribute("alertasPendientes", alertasPendientes);
        model.addAttribute("alertasCriticas", alertasCriticas);
        model.addAttribute("alertasResueltas", alertasResueltas);
        
        return "alertas";
    }
}