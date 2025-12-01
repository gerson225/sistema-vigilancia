package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.proyecto.vigilancia.vigilancia.service.ParametroSistemaService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/parametros")
public class ParametroSistemaApiController {

    private final ParametroSistemaService parametroService;

    public ParametroSistemaApiController(ParametroSistemaService parametroService) {
        this.parametroService = parametroService;
    }

    @GetMapping
    public Map<String, String> obtenerTodosLosParametros() {
        Map<String, String> parametros = new HashMap<>();
        
        // 🔍 Detección y Reconocimiento
        parametros.put("confianza_minima", String.valueOf(parametroService.obtenerConfianzaMinima()));
        
        // 💾 Almacenamiento y Retención
        parametros.put("dias_retencion_detecciones", String.valueOf(parametroService.obtenerDiasRetencionDetecciones()));
        parametros.put("dias_retencion_alertas", String.valueOf(parametroService.obtenerDiasRetencionAlertas()));
        
        // 🔔 Notificaciones
        parametros.put("notificaciones_email", String.valueOf(parametroService.obtenerNotificacionesEmail()));
        parametros.put("notificaciones_sms", String.valueOf(parametroService.obtenerNotificacionesSMS()));
        
        // 🌐 Configuración General
        parametros.put("idioma", parametroService.obtenerIdioma());
        parametros.put("zona_horaria", parametroService.obtenerZonaHoraria());
        
        return parametros;
    }

    @PostMapping("/{nombreParametro}")
    public ResponseEntity<?> actualizarParametro(
            @PathVariable String nombreParametro, 
            @RequestBody Map<String, String> request,
            @SessionAttribute("idUsuario") Integer idUsuario) {
        
        try {
            String nuevoValor = request.get("valor");
            parametroService.actualizarParametro(nombreParametro, nuevoValor, idUsuario);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🔥 ENDPOINT PARA INICIALIZAR PARÁMETROS POR PRIMERA VEZ
    @PostMapping("/inicializar")
    public ResponseEntity<String> inicializarParametros(@SessionAttribute("idUsuario") Integer idUsuario) {
        try {
            // Parámetros por defecto
            parametroService.actualizarParametro("confianza_minima", "0.8", idUsuario);
            parametroService.actualizarParametro("dias_retencion_detecciones", "30", idUsuario);
            parametroService.actualizarParametro("dias_retencion_alertas", "90", idUsuario);
            parametroService.actualizarParametro("notificaciones_email", "true", idUsuario);
            parametroService.actualizarParametro("notificaciones_sms", "false", idUsuario);
            parametroService.actualizarParametro("idioma", "es", idUsuario);
            parametroService.actualizarParametro("zona_horaria", "America/Lima", idUsuario);
            
            return ResponseEntity.ok("Parámetros del sistema inicializados correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al inicializar parámetros: " + e.getMessage());
        }
    }
}