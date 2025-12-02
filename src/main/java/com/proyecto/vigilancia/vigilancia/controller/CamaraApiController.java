package com.proyecto.vigilancia.vigilancia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    
    @GetMapping("/{id}")
    public ResponseEntity<Camara> obtenerCamara(@PathVariable Integer id) {
        try {
            // Implementar este método en el service
            Camara camara = camaraService.obtenerCamaraPorId(id);
            return ResponseEntity.ok(camara);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Camara> crearCamara(@RequestBody Camara camara) {
        try {
            Camara nuevaCamara = camaraService.guardarCamara(camara);
            return ResponseEntity.ok(nuevaCamara);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
public ResponseEntity<Camara> actualizarCamara(@PathVariable Integer id, @RequestBody Camara camara) {
    try {
        // Buscar cámara existente
        Camara camaraExistente = camaraService.obtenerCamaraPorId(id);
        
        // Actualizar solo los campos permitidos
        camaraExistente.setNombreCamara(camara.getNombreCamara());
        camaraExistente.setUbicacion(camara.getUbicacion());
        camaraExistente.setDireccionIp(camara.getDireccionIp());
        
        // Solo actualizar estado si viene en la solicitud
        if (camara.getEstado() != null) {
            camaraExistente.setEstado(camara.getEstado());
        }
        
        // Guardar cambios
        Camara camaraActualizada = camaraService.guardarCamara(camaraExistente);
        return ResponseEntity.ok(camaraActualizada);
        
    } catch (Exception e) {
        System.err.println("❌ Error actualizando cámara " + id + ": " + e.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCamara(@PathVariable Integer id) {
        try {
            camaraService.eliminarCamara(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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
