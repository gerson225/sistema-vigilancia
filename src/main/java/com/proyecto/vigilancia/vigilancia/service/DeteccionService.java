package com.proyecto.vigilancia.vigilancia.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.proyecto.vigilancia.vigilancia.entity.Deteccion;
import com.proyecto.vigilancia.vigilancia.repository.DeteccionRepository;

@Service
public class DeteccionService {
    private final DeteccionRepository repo;

    public DeteccionService(DeteccionRepository repo) {
        this.repo = repo;
    }

 public List<Deteccion> listarDetecciones() {
    List<Deteccion> detecciones = repo.findAll();
    
    for (Deteccion det : detecciones) {
        if (det.getImagenUrl() == null || det.getImagenUrl().isEmpty()) {
            // Asignar una imagen por defecto que SÍ existe
            det.setImagenUrl("/images/persona1.png");
        } else if (!det.getImagenUrl().startsWith("/images/")) {
            // Corregir rutas incorrectas
            det.setImagenUrl("/images/" + det.getImagenUrl());
        }
    }
    
    return detecciones;
}

public List<Deteccion> filtrarDetecciones(Map<String, String> filtros) {
    System.out.println("🔍 Filtros recibidos: " + filtros);

    List<Deteccion> todasDetecciones = listarDetecciones();
    System.out.println("📊 Total detecciones: " + todasDetecciones.size());
    
    List<Deteccion> filtradas = todasDetecciones.stream()
        .filter(det -> filtrarPorPersona(det, filtros.get("persona")))
        .filter(det -> filtrarPorCamara(det, filtros.get("camara")))
        .filter(det -> filtrarPorTipo(det, filtros.get("tipo")))
        .filter(det -> filtrarPorFecha(det, filtros.get("fecha")))
        .collect(Collectors.toList());

        System.out.println("✅ Detecciones filtradas: " + filtradas.size());
        return filtradas;
}

private boolean filtrarPorPersona(Deteccion deteccion, String filtroPersona) {
    if (filtroPersona == null || filtroPersona.isEmpty()) return true;
    
    String nombrePersona = deteccion.getPersona() != null ? 
        deteccion.getPersona().getNombre() : "";
    return nombrePersona.toLowerCase().contains(filtroPersona.toLowerCase());
}

private boolean filtrarPorCamara(Deteccion deteccion, String filtroCamara) {
    if (filtroCamara == null || filtroCamara.isEmpty()) return true;
    if (deteccion.getCamara() == null) return false;
    
    return filtroCamara.equals(deteccion.getCamara().getIdCamara().toString());
}

private boolean filtrarPorTipo(Deteccion deteccion, String filtroTipo) {
    if (filtroTipo == null || filtroTipo.isEmpty()) return true;
    
    return filtroTipo.equals(deteccion.getTipoDeteccion().toString());
}

private boolean filtrarPorFecha(Deteccion deteccion, String filtroFecha) {
    if (filtroFecha == null || filtroFecha.isEmpty()) return true;
    
    // Convertir LocalDateTime a LocalDate para comparar solo la fecha
    return deteccion.getFechaHora().toLocalDate().toString().equals(filtroFecha);
}
}
