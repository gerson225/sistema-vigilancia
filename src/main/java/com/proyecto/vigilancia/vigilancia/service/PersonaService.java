package com.proyecto.vigilancia.vigilancia.service;

import org.springframework.stereotype.Service;
import com.proyecto.vigilancia.vigilancia.entity.Persona;
import com.proyecto.vigilancia.vigilancia.repository.PersonaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<Persona> obtenerTodasLasPersonas() {
        return personaRepository.findAll();
    }

    public Optional<Persona> obtenerPersonaPorId(Integer id) {
        return personaRepository.findById(id);
    }

    public Persona guardarPersona(Persona persona) {
        if (persona.getFechaRegistro() == null) {
            persona.setFechaRegistro(LocalDateTime.now());
        }
        if (persona.getEstado() == null) {
            persona.setEstado("ACTIVO");
        }
        return personaRepository.save(persona);
    }

    public void eliminarPersona(Integer id) {
        personaRepository.deleteById(id);
    }

    public List<Persona> buscarPorNombre(String nombre) {
        return personaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<Persona> buscarPorDni(String dni) {
        return personaRepository.findByDni(dni);
    }
}