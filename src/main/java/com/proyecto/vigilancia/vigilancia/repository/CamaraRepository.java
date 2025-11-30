package com.proyecto.vigilancia.vigilancia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.vigilancia.vigilancia.model.Camara;
import com.proyecto.vigilancia.vigilancia.model.EstadoCamara;

@Repository
public interface CamaraRepository extends JpaRepository<Camara, Integer> {
    long countByEstado(EstadoCamara estado);
}