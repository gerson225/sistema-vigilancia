package com.proyecto.vigilancia.vigilancia.repository;

import com.proyecto.vigilancia.vigilancia.model.Camara;
import com.proyecto.vigilancia.vigilancia.model.EstadoCamara;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CamaraRepository extends JpaRepository<Camara, Integer> {
    long countByEstado(EstadoCamara estado);
}
