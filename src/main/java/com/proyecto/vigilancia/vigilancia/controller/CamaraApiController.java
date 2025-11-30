package com.proyecto.vigilancia.vigilancia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.vigilancia.vigilancia.model.Camara;
import com.proyecto.vigilancia.vigilancia.service.CamaraService;

@RestController
@RequestMapping("/api/camaras")
public class CamaraApiController {
    
    private final CamaraService camaraService;
    
    public CamaraApiController(CamaraService camaraService) {
        this.camaraService = camaraService;
    }
    
    @GetMapping
    public List<Camara> obtenerCamaras() {
        return camaraService.obtenerTodasLasCamaras();
    }
     @PostMapping("/{idCamara}/toggle")
    public ResponseEntity<Camara> toggleCamara(@PathVariable Integer idCamara) {
        try {
            Camara camara = camaraService.toggleEstadoCamara(idCamara);
            return ResponseEntity.ok(camara);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
}
