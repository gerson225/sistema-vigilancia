package com.proyecto.vigilancia.vigilancia.service;

import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.model.Alerta;
import com.proyecto.vigilancia.vigilancia.repository.AlertaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

}
