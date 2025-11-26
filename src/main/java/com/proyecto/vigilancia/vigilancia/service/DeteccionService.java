package com.proyecto.vigilancia.vigilancia.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.proyecto.vigilancia.vigilancia.entity.Deteccion;
import com.proyecto.vigilancia.vigilancia.repository.DeteccionRepository;

@Service
public class DeteccionService {
    private final DeteccionRepository repo;

    public DeteccionService(DeteccionRepository repo) {
        this.repo = repo;
    }

    public List<Deteccion> listarDetecciones() {
        return repo.findAll();
    }
}

