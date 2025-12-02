package com.proyecto.vigilancia.vigilancia.controller;

import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.model.Alerta;
import com.proyecto.vigilancia.vigilancia.service.AlertaService;
import com.proyecto.vigilancia.vigilancia.service.UsuarioService;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
public class AlertaApiController {

    private final AlertaService alertaService;
    private final UsuarioService usuarioService;

    public AlertaApiController(AlertaService alertaService, UsuarioService usuarioService) {
        this.alertaService = alertaService;
        this.usuarioService = usuarioService;
    }

    // Listar últimas alertas (por ahora 10)
    @GetMapping
    public ResponseEntity<List<Alerta>> listarAlertas() {
        List<Alerta> alertas = alertaService.obtenerAlertasRecientes();
        return ResponseEntity.ok(alertas);
    }

    // Crear una alerta
    @PostMapping
    public ResponseEntity<Alerta> crearAlerta(@Valid @RequestBody Alerta alerta) {
        Alerta nueva = alertaService.registrarAlerta(alerta);
        return ResponseEntity.ok(nueva);
    }

    // Resolver alerta
    @PostMapping("/{idAlerta}/resolver")
    public ResponseEntity<Alerta> resolverAlerta(@PathVariable Integer idAlerta, 
                                                @RequestBody Map<String, String> request) {
        try {
            String accionesTomadas = request.get("accionesTomadas");
            Alerta alerta = alertaService.resolverAlerta(idAlerta, accionesTomadas);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // Obtener detalle de alerta
    @GetMapping("/{idAlerta}")
    public ResponseEntity<Alerta> obtenerAlerta(@PathVariable Integer idAlerta) {
        try {
            Alerta alerta = alertaService.buscarPorId(idAlerta);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
}
    @PostMapping("/{idAlerta}/asignar")
    public ResponseEntity<Alerta> asignarAlerta(@PathVariable Integer idAlerta, HttpSession session) {
        try {
            // Obtener el usuario de la sesión
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");
            if (idUsuario == null) {
                return ResponseEntity.status(401).build();
            }

            // Buscar el usuario
            Usuario usuario = usuarioService.buscarPorId(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Asignar la alerta
            Alerta alerta = alertaService.asignarResponsable(idAlerta, usuario);
            return ResponseEntity.ok(alerta);

        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/usuarios-activos")
    public ResponseEntity<List<Usuario>> obtenerUsuariosActivos(HttpSession session) {
        try {
            String rol = (String) session.getAttribute("rol");
            if (rol == null) {
                return ResponseEntity.status(401).build();
            }
            
            List<Usuario> usuarios = usuarioService.obtenerUsuariosActivos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 🔥 NUEVO: Escalar alerta (solo ADMINISTRADOR)
    @PostMapping("/{idAlerta}/escalar")
    public ResponseEntity<Alerta> escalarAlerta(@PathVariable Integer idAlerta,
                                                @RequestBody Map<String, String> request,
                                                HttpSession session) {
        try {
            // Verificar si es ADMINISTRADOR
            String rol = (String) session.getAttribute("rol");
            if (!"ADMINISTRADOR".equals(rol)) {
                return ResponseEntity.status(403).body(null);
            }
            
            String motivo = request.get("motivo");
            Alerta alerta = alertaService.escalarAlerta(idAlerta, motivo);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
    @PostMapping("/{idAlerta}/reasignar")
    public ResponseEntity<Alerta> reasignarAlerta(@PathVariable Integer idAlerta,
                                                  @RequestBody Map<String, Object> request,
                                                  HttpSession session) {
        try {
            // Verificar si es ADMINISTRADOR
            String rol = (String) session.getAttribute("rol");
            if (!"ADMINISTRADOR".equals(rol)) {
                return ResponseEntity.status(403).body(null);
            }
            
            Integer idNuevoResponsable = (Integer) request.get("idNuevoResponsable");
            String motivo = (String) request.get("motivo");
            
            Usuario nuevoResponsable = usuarioService.buscarPorId(idNuevoResponsable)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            Alerta alerta = alertaService.reasignarAlerta(idAlerta, nuevoResponsable, motivo);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // 🔥 NUEVO: Marcar como falsa alarma (todos pueden)
    @PostMapping("/{idAlerta}/marcar-falsa")
    public ResponseEntity<Alerta> marcarFalsaAlarma(@PathVariable Integer idAlerta,
                                                    @RequestBody Map<String, String> request,
                                                    HttpSession session) {
        try {
            // Verificar sesión
            String rol = (String) session.getAttribute("rol");
            if (rol == null) {
                return ResponseEntity.status(401).build();
            }
            
            String motivo = request.get("motivo");
            Alerta alerta = alertaService.marcarFalsaAlarma(idAlerta, motivo);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // 🔥 NUEVO: Resolver con observación obligatoria
    @PostMapping("/{idAlerta}/resolver-con-observacion")
    public ResponseEntity<Alerta> resolverAlertaConObservacion(@PathVariable Integer idAlerta,
                                                               @RequestBody Map<String, String> request,
                                                               HttpSession session) {
        try {
            // Verificar sesión
            String rol = (String) session.getAttribute("rol");
            if (rol == null) {
                return ResponseEntity.status(401).build();
            }
            
            String accionesTomadas = request.get("accionesTomadas");
            String observaciones = request.get("observaciones");
            
            if (observaciones == null || observaciones.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            
            Alerta alerta = alertaService.resolverAlertaConObservacion(idAlerta, accionesTomadas, observaciones);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // 🔥 NUEVO: Obtener historial
    @GetMapping("/{idAlerta}/historial")
    public ResponseEntity<String> obtenerHistorial(@PathVariable Integer idAlerta,
                                                   HttpSession session) {
        try {
            // Verificar sesión
            String rol = (String) session.getAttribute("rol");
            if (rol == null) {
                return ResponseEntity.status(401).build();
            }
            
            String historial = alertaService.obtenerHistorial(idAlerta);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // 🔥 NUEVO: Exportar/Imprimir (solo ADMINISTRADOR)
    @GetMapping("/{idAlerta}/exportar")
    public ResponseEntity<String> exportarAlerta(@PathVariable Integer idAlerta,
                                                 HttpSession session) {
        try {
            // Verificar si es ADMINISTRADOR
            String rol = (String) session.getAttribute("rol");
            if (!"ADMINISTRADOR".equals(rol)) {
                return ResponseEntity.status(403).body(null);
            }
            
            String reporte = alertaService.generarReporteAlerta(idAlerta);
            
            // Configurar headers para descarga
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);
            headers.setContentDisposition(
                ContentDisposition.attachment()
                    .filename("reporte-alerta-" + idAlerta + ".html")
                    .build()
            );
            
            return new ResponseEntity<>(reporte, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // 🔥 NUEVO: Escalar a administrador (OPERADOR -> ADMINISTRADOR)
    @PostMapping("/{idAlerta}/escalar-a-admin")
    public ResponseEntity<Alerta> escalarAAdmin(@PathVariable Integer idAlerta,
                                                @RequestBody Map<String, String> request,
                                                HttpSession session) {
        try {
            // Verificar sesión
            String rol = (String) session.getAttribute("rol");
            if (rol == null) {
                return ResponseEntity.status(401).build();
            }
            
            String motivo = request.get("motivo");
            
            // Buscar un administrador disponible
            Usuario admin = usuarioService.buscarAdministrador()
                .orElseThrow(() -> new RuntimeException("No hay administrador disponible"));
            
            Integer idUsuarioActual = (Integer) session.getAttribute("idUsuario");
            Usuario operadorActual = usuarioService.buscarPorId(idUsuarioActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Reasignar al admin
            String motivoCompleto = String.format("Escalada por %s (OPERADOR). Motivo: %s", 
                operadorActual.getNombre(), motivo);
            
            Alerta alerta = alertaService.reasignarAlerta(idAlerta, admin, motivoCompleto);
            alerta.setNivelCriticidad("ALTA"); // Subir criticidad al escalar
            
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    // Endpoint para filtrar alertas
@PostMapping("/filtrar")
public ResponseEntity<List<Alerta>> filtrarAlertas(@RequestBody Map<String, String> filtros) {
    try {
        String estado = filtros.get("estado");
        String nivelCriticidad = filtros.get("nivelCriticidad");
        
        List<Alerta> alertasFiltradas = alertaService.filtrarAlertas(estado, nivelCriticidad);
        return ResponseEntity.ok(alertasFiltradas);
    } catch (Exception e) {
        return ResponseEntity.status(500).build();
    }
}
}