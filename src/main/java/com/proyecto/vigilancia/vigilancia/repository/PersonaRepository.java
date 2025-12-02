package com.proyecto.vigilancia.vigilancia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.vigilancia.vigilancia.entity.Persona;
import com.proyecto.vigilancia.vigilancia.entity.TipoPersona;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    
    List<Persona> findByNombreContainingIgnoreCase(String nombre);
    
    Optional<Persona> findByDni(String dni);
    
    List<Persona> findByTipoPersona(String tipoPersona);

    // 🔥 NUEVO: Método para filtrar personas
    @Query("SELECT p FROM Persona p WHERE " +
           "(:nombre IS NULL OR p.nombre LIKE %:nombre%) AND " +
           "(:dni IS NULL OR p.dni LIKE %:dni%) AND " +
           "(:tipoPersona IS NULL OR p.tipoPersona = :tipoPersona)")
    List<Persona> filtrarPersonas(
            @Param("nombre") String nombre,
            @Param("dni") String dni,
            @Param("tipoPersona") TipoPersona tipoPersona);

}