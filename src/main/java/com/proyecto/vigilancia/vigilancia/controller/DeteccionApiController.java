package com.proyecto.vigilancia.vigilancia.controller;

import com.proyecto.vigilancia.vigilancia.entity.Deteccion;
import com.proyecto.vigilancia.vigilancia.entity.Persona;
import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.model.Alerta;
import com.proyecto.vigilancia.vigilancia.service.DeteccionService;
import com.proyecto.vigilancia.vigilancia.service.PersonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/detecciones")
public class DeteccionApiController {

    private final DeteccionService deteccionService;
    private final PersonaService personaService;

    public DeteccionApiController(DeteccionService deteccionService, 
                                 PersonaService personaService) {
        this.deteccionService = deteccionService;
        this.personaService = personaService;
    }

    // 🔥 NUEVO: Eliminar detección (solo ADMINISTRADOR)
    @DeleteMapping("/{idDeteccion}")
    public ResponseEntity<?> eliminarDeteccion(@PathVariable Integer idDeteccion,
                                               HttpSession session) {
        try {
            // Verificar si es ADMINISTRADOR
            String rol = (String) session.getAttribute("rol");
            if (!"ADMINISTRADOR".equals(rol)) {
                return ResponseEntity.status(403).body("Solo los administradores pueden eliminar detecciones");
            }
            
            deteccionService.eliminarDeteccion(idDeteccion);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // 🔥 NUEVO: Vincular persona
    @PostMapping("/{idDeteccion}/vincular-persona")
    public ResponseEntity<Deteccion> vincularPersona(@PathVariable Integer idDeteccion,
                                                     @RequestBody Map<String, Integer> request,
                                                     HttpSession session) {
        try {
            // Verificar sesión
            String rol = (String) session.getAttribute("rol");
            if (rol == null) {
                return ResponseEntity.status(401).build();
            }
            
            Integer idPersona = request.get("idPersona");
            Deteccion deteccion = deteccionService.vincularPersona(idDeteccion, idPersona);
            return ResponseEntity.ok(deteccion);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // 🔥 NUEVO: Marcar como revisado
    @PostMapping("/{idDeteccion}/marcar-revisado")
    public ResponseEntity<Deteccion> marcarComoRevisado(@PathVariable Integer idDeteccion,
                                                        @RequestBody Map<String, String> request,
                                                        HttpSession session) {
        try {
            // Verificar sesión
            String rol = (String) session.getAttribute("rol");
            if (rol == null) {
                return ResponseEntity.status(401).build();
            }
            
            String observaciones = request.get("observaciones");
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");
            
            Deteccion deteccion = deteccionService.marcarComoRevisado(idDeteccion, observaciones, idUsuario);
            return ResponseEntity.ok(deteccion);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // 🔥 NUEVO: Generar alerta con formulario
    @PostMapping("/{idDeteccion}/generar-alerta")
    public ResponseEntity<Alerta> generarAlerta(@PathVariable Integer idDeteccion,
                                                @RequestBody Map<String, String> request,
                                                HttpSession session) {
        try {
            // Verificar sesión
            String rol = (String) session.getAttribute("rol");
            if (rol == null) {
                return ResponseEntity.status(401).build();
            }
            
            String tipoAlerta = request.get("tipoAlerta");
            String descripcion = request.get("descripcion");
            String nivelCriticidad = request.get("nivelCriticidad");
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");
            
            Alerta alerta = deteccionService.generarAlertaDesdeDeteccion(
                idDeteccion, tipoAlerta, descripcion, nivelCriticidad, idUsuario);
            
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // 🔥 NUEVO: Escalar detección a alerta crítica (solo ADMINISTRADOR)
    @PostMapping("/{idDeteccion}/escalar-a-alerta")
    public ResponseEntity<Alerta> escalarAAleraCritica(@PathVariable Integer idDeteccion,
                                                       @RequestBody Map<String, String> request,
                                                       HttpSession session) {
        try {
            // Verificar si es ADMINISTRADOR
            String rol = (String) session.getAttribute("rol");
            if (!"ADMINISTRADOR".equals(rol)) {
                return ResponseEntity.status(403).body(null);
            }
            
            String motivo = request.get("motivo");
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");
            
            Alerta alerta = deteccionService.escalarAAleraCritica(idDeteccion, motivo, idUsuario);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // 🔥 NUEVO: Obtener lista de personas para vincular
    @GetMapping("/personas")
    public ResponseEntity<List<Persona>> listarPersonas(HttpSession session) {
        try {
            // Verificar sesión
            String rol = (String) session.getAttribute("rol");
            if (rol == null) {
                return ResponseEntity.status(401).build();
            }
            
            List<Persona> personas = personaService.obtenerPersonasActivas();
            return ResponseEntity.ok(personas);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 🔥 NUEVO: Obtener tipos de alerta disponibles
    @GetMapping("/tipos-alerta")
    public ResponseEntity<Map<String, String>> obtenerTiposAlerta() {
        try {
            Map<String, String> tipos = new HashMap<>();
            tipos.put("ACCESO_NO_AUTORIZADO", "Acceso no autorizado");
            tipos.put("PERSONA_SOSPECHOSA", "Persona sospechosa");
            tipos.put("ZONA_RESTRINGIDA", "Zona restringida");
            tipos.put("COMPORTAMIENTO_ANORMAL", "Comportamiento anormal");
            tipos.put("OBJETO_SOSPECHOSO", "Objeto sospechoso");
            tipos.put("FUERA_HORARIO", "Fuera de horario permitido");
            tipos.put("VIOLENCIA", "Posible acto violento");
            tipos.put("INTRUSION", "Intrusión en área segura");
            
            return ResponseEntity.ok(tipos);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}