package com.proyecto.vigilancia.vigilancia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.vigilancia.vigilancia.entity.ParametroSistema;

import java.util.Optional;

@Repository
public interface ParametroSistemaRepository extends JpaRepository<ParametroSistema, Integer> {
    Optional<ParametroSistema> findByNombreParametro(String nombreParametro);
}