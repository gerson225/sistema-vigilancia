package com.proyecto.vigilancia.vigilancia.service;

import org.springframework.stereotype.Service;
import com.proyecto.vigilancia.vigilancia.model.Camara;
import com.proyecto.vigilancia.vigilancia.model.EstadoCamara;
import com.proyecto.vigilancia.vigilancia.repository.CamaraRepository;

import java.util.List;

@Service
public class CamaraService {

    private final CamaraRepository camaraRepository;

    public CamaraService(CamaraRepository camaraRepository) {
        this.camaraRepository = camaraRepository;
    }

    public List<Camara> obtenerTodasLasCamaras() {
        return camaraRepository.findAll();
    }

    public Camara cambiarEstadoCamara(Integer idCamara, String nuevoEstado) {
        Camara camara = camaraRepository.findById(idCamara)
            .orElseThrow(() -> new RuntimeException("Cámara no encontrada"));
        
        // Aquí implementarías la lógica para cambiar estado
        return camaraRepository.save(camara);
    }
    public Camara toggleEstadoCamara(Integer idCamara) {
    Camara camara = camaraRepository.findById(idCamara)
        .orElseThrow(() -> new RuntimeException("Cámara no encontrada"));
    
    // Cambiar estado (ACTIVA ↔ INACTIVA)
    if (camara.getEstado() == EstadoCamara.ACTIVA) {
        camara.setEstado(EstadoCamara.INACTIVA);
    } else {
        camara.setEstado(EstadoCamara.ACTIVA);
    }
    
    return camaraRepository.save(camara);
}
}