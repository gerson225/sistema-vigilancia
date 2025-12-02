package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proyecto.vigilancia.vigilancia.dto.PersonaDTO;
import com.proyecto.vigilancia.vigilancia.entity.Persona;
import com.proyecto.vigilancia.vigilancia.service.PersonaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/personas")
public class PersonaApiController {

    private final PersonaService personaService;

    public PersonaApiController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping("/filtrar-simple")
    public ResponseEntity<?> filtrarPersonasSimple(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String tipoPersona) {
        
        try {
            System.out.println("🔍 Filtros simples - nombre: " + nombre + ", dni: " + dni + ", tipoPersona: " + tipoPersona);
            
            List<Persona> personas = personaService.filtrarPersonasSimple(nombre, dni, tipoPersona);
            
            // Limpiar relaciones circulares
            for (Persona persona : personas) {
                if (persona.getDetecciones() != null) {
                    persona.getDetecciones().forEach(deteccion -> {
                        deteccion.setPersona(null);
                    });
                }
            }
            
            return ResponseEntity.ok(personas);
            
        } catch (Exception e) {
            System.err.println("❌ Error en filtrarPersonasSimple: " + e.getMessage());
            
            // Fallback: devolver todas las personas
            List<Persona> todasPersonas = personaService.obtenerTodasLasPersonas();
            
            // Limpiar relaciones
            for (Persona persona : todasPersonas) {
                if (persona.getDetecciones() != null) {
                    persona.getDetecciones().forEach(deteccion -> {
                        deteccion.setPersona(null);
                    });
                }
            }
            
            return ResponseEntity.ok(todasPersonas);
        }
    }

    @PostMapping("/filtrar")
    public ResponseEntity<?> filtrarPersonas(@RequestBody Map<String, String> filtros) {
        try {
            System.out.println("🔍 Filtros recibidos en controller: " + filtros);
            
            String nombre = filtros.getOrDefault("nombre", "");
            String dni = filtros.getOrDefault("dni", "");
            String tipoPersona = filtros.getOrDefault("tipoPersona", "");
            
            // Usar el método simple que filtra en memoria
            List<Persona> personasFiltradas = personaService.filtrarPersonasSimple(nombre, dni, tipoPersona);
            
            System.out.println("✅ Personas encontradas: " + personasFiltradas.size());
            
            // Limpiar relaciones circulares
            for (Persona persona : personasFiltradas) {
                if (persona.getDetecciones() != null) {
                    persona.getDetecciones().forEach(deteccion -> {
                        deteccion.setPersona(null);
                        if (deteccion.getCamara() != null) {
                            deteccion.getCamara().setUsuario(null);
                        }
                    });
                }
            }
            
            return ResponseEntity.ok(personasFiltradas);
            
        } catch (Exception e) {
            System.err.println("❌ ERROR en filtrarPersonas: " + e.getMessage());
            e.printStackTrace();
            
            // Devolver todas las personas como fallback
            List<Persona> todasPersonas = personaService.obtenerTodasLasPersonas();
            
            // Limpiar relaciones
            for (Persona persona : todasPersonas) {
                if (persona.getDetecciones() != null) {
                    persona.getDetecciones().forEach(deteccion -> {
                        deteccion.setPersona(null);
                    });
                }
            }
            
            return ResponseEntity.ok(todasPersonas);
        }
    }
    @GetMapping("/test-filtrar")
    public ResponseEntity<?> testFiltrar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String tipoPersona) {
        
        System.out.println("🧪 Test filtro - nombre: " + nombre + ", dni: " + dni + ", tipo: " + tipoPersona);
        
        List<Persona> resultados = personaService.filtrarPersonasSimple(nombre, dni, tipoPersona);
        
        // Limpiar relaciones
        for (Persona persona : resultados) {
            if (persona.getDetecciones() != null) {
                persona.getDetecciones().forEach(deteccion -> {
                    deteccion.setPersona(null);
                });
            }
        }
        
        return ResponseEntity.ok(resultados);
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