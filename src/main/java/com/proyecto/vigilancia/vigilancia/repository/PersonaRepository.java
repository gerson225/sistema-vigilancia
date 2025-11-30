package com.proyecto.vigilancia.vigilancia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.vigilancia.vigilancia.entity.Persona;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    
    List<Persona> findByNombreContainingIgnoreCase(String nombre);
    
    Optional<Persona> findByDni(String dni);
    
    List<Persona> findByTipoPersona(String tipoPersona);
}