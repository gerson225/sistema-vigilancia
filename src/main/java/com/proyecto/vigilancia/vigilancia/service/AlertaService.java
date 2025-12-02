package com.proyecto.vigilancia.vigilancia.service;

import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.model.Alerta;
import com.proyecto.vigilancia.vigilancia.repository.AlertaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;

    public AlertaService(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    // Registrar/guardar una alerta
    public Alerta registrarAlerta(Alerta alerta) {
        if (alerta.getFechaAlerta() == null) {
            alerta.setFechaAlerta(LocalDateTime.now());
        }
        return alertaRepository.save(alerta);
    }

    // Obtener últimas 10 alertas (para una tabla)
    public List<Alerta> obtenerAlertasRecientes() {
        return alertaRepository.findTop10ByOrderByFechaAlertaDesc();
    }

    // Datos agregados para el gráfico de "Alertas recientes" del dashboard
    public Map<String, Integer> obtenerAlertasRecientesAgrupadas() {
        // Últimos 5 días (lunes-viernes, por simplicidad)
        LocalDateTime desde = LocalDate.now().minusDays(4).atStartOfDay();

        List<Object[]> datos = alertaRepository.contarAlertasPorDia(desde);

        // Guardamos el conteo por índice de día (1=domingo, 2=lunes,...)
        Map<Integer, Long> porIndice = new LinkedHashMap<>();
        for (Object[] fila : datos) {
            Integer indiceDia = ((Number) fila[0]).intValue();
            Long cantidad = (Long) fila[1];
            porIndice.put(indiceDia, cantidad);
        }
        // Mapeamos a los nombres que espera el front: lunes-viernes
        Map<String, Integer> resultado = new LinkedHashMap<>();
        resultado.put("domingo",   porIndice.getOrDefault(1, 0L).intValue());
        resultado.put("lunes",     porIndice.getOrDefault(2, 0L).intValue());
        resultado.put("martes",    porIndice.getOrDefault(3, 0L).intValue());
        resultado.put("miercoles", porIndice.getOrDefault(4, 0L).intValue());
        resultado.put("jueves",    porIndice.getOrDefault(5, 0L).intValue());
        resultado.put("viernes",   porIndice.getOrDefault(6, 0L).intValue());
        resultado.put("sabado",    porIndice.getOrDefault(7, 0L).intValue());

        return resultado;
    }

    public List<Alerta> obtenerTodasLasAlertas() {
    return alertaRepository.findAll();
    }

    public Alerta buscarPorId(Integer idAlerta) {
        return alertaRepository.findById(idAlerta)
            .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
    }
    
    // Método para asignar responsable a una alerta
    public Alerta asignarResponsable(Integer idAlerta, Usuario responsable) {
        Alerta alerta = alertaRepository.findById(idAlerta)
            .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
    
    alerta.setUsuarioResponsable(responsable);
    alerta.setEstadoAlerta("EN_PROCESO");
    
    return alertaRepository.save(alerta);
}

// Método para resolver alerta
    public Alerta resolverAlerta(Integer idAlerta, String accionesTomadas) {
        Alerta alerta = alertaRepository.findById(idAlerta)
            .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
    
    alerta.setEstadoAlerta("RESUELTA");
    alerta.setAccionesTomadas(accionesTomadas);
    alerta.setFechaResolucion(LocalDateTime.now());
    
    return alertaRepository.save(alerta);
}

// Método para filtrar alertas
public List<Alerta> filtrarAlertas(String estado, String nivelCriticidad) {
    List<Alerta> todasAlertas = obtenerTodasLasAlertas();
    
    return todasAlertas.stream()
        .filter(alerta -> filtrarPorEstado(alerta, estado))
        .filter(alerta -> filtrarPorCriticidad(alerta, nivelCriticidad))
        .collect(Collectors.toList());
}

private boolean filtrarPorEstado(Alerta alerta, String estado) {
    if (estado == null || estado.isEmpty()) return true;
    return estado.equals(alerta.getEstadoAlerta());
}

private boolean filtrarPorCriticidad(Alerta alerta, String nivelCriticidad) {
    if (nivelCriticidad == null || nivelCriticidad.isEmpty()) return true;
    return nivelCriticidad.equals(alerta.getNivelCriticidad());
}

    // 🔥 NUEVO: Métodos para las nuevas funcionalidades
    public Alerta escalarAlerta(Integer idAlerta, String motivo) {
        Alerta alerta = buscarPorId(idAlerta);
        alerta.setNivelCriticidad("CRITICA");
        agregarAlHistorial(alerta, "ESCALADA", "Escalada a crítica. Motivo: " + motivo);
        return alertaRepository.save(alerta);
    }

    public Alerta reasignarAlerta(Integer idAlerta, Usuario nuevoResponsable, String motivo) {
        Alerta alerta = buscarPorId(idAlerta);
        Usuario anteriorResponsable = alerta.getUsuarioResponsable();
        
        alerta.setUsuarioResponsable(nuevoResponsable);
        alerta.setEstadoAlerta("EN_PROCESO");
        
        String historico = String.format("Reasignada de %s a %s. Motivo: %s",
            anteriorResponsable != null ? anteriorResponsable.getNombre() : "Sin asignar",
            nuevoResponsable.getNombre(),
            motivo);
        
        agregarAlHistorial(alerta, "REASIGNADA", historico);
        return alertaRepository.save(alerta);
    }

    public Alerta marcarFalsaAlarma(Integer idAlerta, String motivo) {
        Alerta alerta = buscarPorId(idAlerta);
        alerta.setEsFalsaAlarma(true);
        alerta.setEstadoAlerta("DESCARTADA");
        alerta.setObservacionesResolucion("Marcada como falsa alarma: " + motivo);
        agregarAlHistorial(alerta, "FALSA_ALARMA", motivo);
        return alertaRepository.save(alerta);
    }

    public Alerta resolverAlertaConObservacion(Integer idAlerta, String accionesTomadas, String observaciones) {
        if (observaciones == null || observaciones.trim().isEmpty()) {
            throw new RuntimeException("Las observaciones son obligatorias");
        }
        
        Alerta alerta = buscarPorId(idAlerta);
        alerta.setEstadoAlerta("RESUELTA");
        alerta.setAccionesTomadas(accionesTomadas);
        alerta.setObservacionesResolucion(observaciones);
        alerta.setFechaResolucion(LocalDateTime.now());
        
        agregarAlHistorial(alerta, "RESUELTA", 
            "Acciones: " + accionesTomadas + " - Observaciones: " + observaciones);
        
        return alertaRepository.save(alerta);
    }

    public String obtenerHistorial(Integer idAlerta) {
        Alerta alerta = buscarPorId(idAlerta);
        return alerta.getHistorial() != null ? alerta.getHistorial() : "Sin historial disponible";
    }

    public String generarReporteAlerta(Integer idAlerta) {
        Alerta alerta = buscarPorId(idAlerta);
        
        // Formatear fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = alerta.getFechaAlerta().format(formatter);
        
        String reporte = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>Reporte Alerta #%d - SIVI</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 40px; }
                    h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }
                    .section { margin-bottom: 25px; border: 1px solid #ddd; padding: 20px; border-radius: 8px; }
                    .section h2 { color: #3498db; margin-top: 0; }
                    .label { font-weight: bold; color: #2c3e50; min-width: 150px; display: inline-block; }
                    .value { margin-left: 10px; }
                    .row { margin-bottom: 10px; }
                    .historial { background-color: #f8f9fa; padding: 15px; border-radius: 5px; font-family: 'Courier New', monospace; 
                               white-space: pre-wrap; font-size: 12px; max-height: 400px; overflow-y: auto; }
                    .badge { padding: 3px 10px; border-radius: 12px; color: white; font-size: 12px; font-weight: bold; }
                    .critica { background-color: #8b0000; }
                    .alta { background-color: #e74c3c; }
                    .media { background-color: #f39c12; }
                    .baja { background-color: #27ae60; }
                </style>
            </head>
            <body>
                <h1>🚨 Reporte de Alerta #%d</h1>
                <p><i>Generado el: %s</i></p>
                
                <div class="section">
                    <h2>📋 Información General</h2>
                    <div class="row"><span class="label">ID:</span><span class="value">%d</span></div>
                    <div class="row"><span class="label">Fecha y Hora:</span><span class="value">%s</span></div>
                    <div class="row"><span class="label">Tipo:</span><span class="value">%s</span></div>
                    <div class="row"><span class="label">Nivel de Criticidad:</span>
                        <span class="value"><span class="badge %s">%s</span></span>
                    </div>
                    <div class="row"><span class="label">Estado:</span><span class="value">%s</span></div>
                    <div class="row"><span class="label">Falsa Alarma:</span><span class="value">%s</span></div>
                </div>
                
                <div class="section">
                    <h2>👤 Responsables</h2>
                    <div class="row"><span class="label">Creada por:</span><span class="value">%s</span></div>
                    <div class="row"><span class="label">Responsable actual:</span><span class="value">%s</span></div>
                </div>
                
                <div class="section">
                    <h2>📝 Descripción y Resolución</h2>
                    <div class="row"><span class="label">Descripción:</span><span class="value">%s</span></div>
                    <div class="row"><span class="label">Acciones Tomadas:</span><span class="value">%s</span></div>
                    <div class="row"><span class="label">Observaciones:</span><span class="value">%s</span></div>
                    <div class="row"><span class="label">Fecha Resolución:</span><span class="value">%s</span></div>
                </div>
                
                <div class="section">
                    <h2>📋 Historial de Acciones</h2>
                    <div class="historial">%s</div>
                </div>
                
                <div style="margin-top: 40px; text-align: center; color: #7f8c8d; font-size: 12px;">
                    <hr>
                    <p>Sistema Inteligente de Vigilancia (SIVI) - Reporte generado automáticamente</p>
                </div>
                
                <script>
                    window.onload = function() {
                        // Auto-print option
                        setTimeout(function() {
                            if (confirm('¿Desea imprimir este reporte?')) {
                                window.print();
                            }
                        }, 1000);
                    }
                </script>
            </body>
            </html>
            """,
            alerta.getIdAlerta(),
            alerta.getIdAlerta(),
            LocalDateTime.now().format(formatter),
            alerta.getIdAlerta(),
            fechaFormateada,
            alerta.getTipoAlerta(),
            obtenerClaseCriticidad(alerta.getNivelCriticidad()),
            alerta.getNivelCriticidad(),
            alerta.getEstadoAlerta(),
            alerta.getEsFalsaAlarma() != null && alerta.getEsFalsaAlarma() ? "Sí" : "No",
            alerta.getUsuario() != null ? alerta.getUsuario().getNombre() : "N/A",
            alerta.getUsuarioResponsable() != null ? alerta.getUsuarioResponsable().getNombre() : "Sin asignar",
            alerta.getDescripcion() != null ? alerta.getDescripcion() : "N/A",
            alerta.getAccionesTomadas() != null ? alerta.getAccionesTomadas() : "N/A",
            alerta.getObservacionesResolucion() != null ? alerta.getObservacionesResolucion() : "N/A",
            alerta.getFechaResolucion() != null ? alerta.getFechaResolucion().format(formatter) : "No resuelta",
            alerta.getHistorial() != null ? alerta.getHistorial() : "Sin historial disponible"
        );
        
        return reporte;
    }
    
    private String obtenerClaseCriticidad(String nivel) {
        if (nivel == null) return "media";
        switch (nivel.toUpperCase()) {
            case "CRITICA": return "critica";
            case "ALTA": return "alta";
            case "BAJA": return "baja";
            default: return "media";
        }
    }
    
    private void agregarAlHistorial(Alerta alerta, String accion, String detalle) {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String entrada = String.format("[%s] %s: %s", 
            ahora.format(formatter), 
            accion, detalle);
        
        String historialActual = alerta.getHistorial();
        if (historialActual == null) {
            alerta.setHistorial(entrada);
        } else {
            alerta.setHistorial(historialActual + "\n" + entrada);
        }
    }
    
    // 🔥 NUEVO: Verificar si una detección tiene alertas asociadas
    public boolean tieneAlertasAsociadas(Integer idDeteccion) {
        List<Alerta> alertas = alertaRepository.findAll();
        return alertas.stream()
            .anyMatch(alerta -> alerta.getDeteccion() != null && 
                       alerta.getDeteccion().getIdDeteccion().equals(idDeteccion));
    }
} 

