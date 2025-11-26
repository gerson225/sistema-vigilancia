package com.proyecto.vigilancia.vigilancia.service;

import com.proyecto.vigilancia.vigilancia.model.Alerta;
import com.proyecto.vigilancia.vigilancia.repository.AlertaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
}
