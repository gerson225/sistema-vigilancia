package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.vigilancia.vigilancia.dto.PersonaDTO;
import com.proyecto.vigilancia.vigilancia.entity.Persona;
import com.proyecto.vigilancia.vigilancia.service.PersonaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personas")
public class PersonaApiController {

    private final PersonaService personaService;

    public PersonaApiController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public List<Persona> obtenerPersonas() {
        List<Persona> personas = personaService.obtenerTodasLasPersonas();
        
        // 🔥 LIMPIAR RELACIONES CIRCULARES para evitar JSON inválido
        for (Persona persona : personas) {
            if (persona.getDetecciones() != null) {
                // Limpiar relaciones recursivas
                persona.getDetecciones().forEach(deteccion -> {
                    deteccion.setPersona(null); // Romper ciclo
                    if (deteccion.getCamara() != null) {
                        deteccion.getCamara().setUsuario(null);
                    }
                });
            }
        }
        
        return personas;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPersona(@PathVariable Integer id) {
        try {
            Optional<Persona> personaOpt = personaService.obtenerPersonaPorId(id);
            
            if (personaOpt.isPresent()) {
                Persona persona = personaOpt.get();
                
                // Crear DTO manualmente
                PersonaDTO dto = new PersonaDTO();
                dto.setIdPersona(persona.getIdPersona());
                dto.setNombre(persona.getNombre());
                dto.setDni(persona.getDni());
                dto.setFoto(persona.getFoto());
                dto.setDescripcion(persona.getDescripcion());
                dto.setTipoPersona(persona.getTipoPersona() != null ? persona.getTipoPersona().name() : null);
                dto.setEstado(persona.getEstado());
                
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public Persona crearPersona(@RequestBody Persona persona) {
        return personaService.guardarPersona(persona);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPersona(@PathVariable Integer id, @RequestBody Persona persona) {
        try {
            // Verificar que la persona existe
            Optional<Persona> personaExistente = personaService.obtenerPersonaPorId(id);
            if (personaExistente.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            persona.setIdPersona(id);
            Persona personaActualizada = personaService.guardarPersona(persona);
            return ResponseEntity.ok(personaActualizada);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPersona(@PathVariable Integer id) {
        try {
            personaService.eliminarPersona(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar: " + e.getMessage());
        }
    }
}