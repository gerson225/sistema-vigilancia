package com.proyecto.vigilancia.vigilancia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.vigilancia.vigilancia.entity.Deteccion;
import com.proyecto.vigilancia.vigilancia.entity.EstadoDeteccion;

@Repository
public interface DeteccionRepository extends JpaRepository<Deteccion, Integer> {
    long countByEstado(EstadoDeteccion estado);
}
