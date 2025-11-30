package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import com.proyecto.vigilancia.vigilancia.entity.Deteccion;
import com.proyecto.vigilancia.vigilancia.service.DeteccionService;

@RestController
@RequestMapping("/api/detecciones")
public class DeteccionController {

    private final DeteccionService service;

    public DeteccionController(DeteccionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Deteccion> obtenerDetecciones() {
        return service.listarDetecciones();
    }

    // 🔥 AGREGAR ESTE NUEVO ENDPOINT (no borrar lo anterior)
    @PostMapping("/filtrar")
    public List<Deteccion> filtrarDetecciones(@RequestBody Map<String, String> filtros) {
        // Por ahora devuelve todas las detecciones
        // Luego implementarás la lógica de filtrado real
        return service.filtrarDetecciones(filtros);
    }
}