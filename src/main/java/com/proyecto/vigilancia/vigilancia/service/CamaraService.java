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

    public Camara obtenerCamaraPorId(Integer id) {
        return camaraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cámara no encontrada con ID: " + id));
    }

    public Camara guardarCamara(Camara camara) {
        // Validaciones básicas
        if (camara.getNombreCamara() == null || camara.getNombreCamara().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la cámara es obligatorio");
        }
        if (camara.getDireccionIp() == null || camara.getDireccionIp().trim().isEmpty()) {
            throw new RuntimeException("La dirección IP es obligatoria");
        }
        
        return camaraRepository.save(camara);
    }

    public void eliminarCamara(Integer id) {
        if (!camaraRepository.existsById(id)) {
            throw new RuntimeException("Cámara no encontrada con ID: " + id);
        }
        camaraRepository.deleteById(id);
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