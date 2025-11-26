package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}

