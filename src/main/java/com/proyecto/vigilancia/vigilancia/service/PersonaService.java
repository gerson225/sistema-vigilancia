package com.proyecto.vigilancia.vigilancia.service;

import org.springframework.stereotype.Service;
import com.proyecto.vigilancia.vigilancia.entity.Persona;
import com.proyecto.vigilancia.vigilancia.entity.TipoPersona;
import com.proyecto.vigilancia.vigilancia.repository.PersonaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

     public List<Persona> filtrarPersonas(Map<String, String> filtros) {
        try {
            System.out.println("🔍 Filtros recibidos en service: " + filtros);
            
            String nombre = filtros.get("nombre");
            String dni = filtros.get("dni");
            String tipoPersonaStr = filtros.get("tipoPersona");
            
            // Preparar filtros para el query
            String nombreLike = null;
            String dniLike = null;
            TipoPersona tipoPersona = null;
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                nombreLike = "%" + nombre + "%";
            }
            
            if (dni != null && !dni.trim().isEmpty()) {
                dniLike = "%" + dni + "%";
            }
            
            if (tipoPersonaStr != null && !tipoPersonaStr.trim().isEmpty()) {
                try {
                    // Convertir String a Enum
                    tipoPersona = TipoPersona.valueOf(tipoPersonaStr);
                } catch (IllegalArgumentException e) {
                    System.err.println("❌ Tipo de persona no válido: " + tipoPersonaStr);
                    // Si el tipo no es válido, tratarlo como null
                }
            }
            
            System.out.println("🔍 Filtros procesados - nombre: " + nombreLike + 
                              ", dni: " + dniLike + ", tipoPersona: " + tipoPersona);
            
            // Si todos los filtros son null, devolver todas las personas
            if (nombreLike == null && dniLike == null && tipoPersona == null) {
                return personaRepository.findAll();
            }
            
            // Ejecutar query con los filtros
            List<Persona> resultados = personaRepository.filtrarPersonas(nombreLike, dniLike, tipoPersona);
            System.out.println("✅ Resultados encontrados: " + resultados.size());
            
            return resultados;
            
        } catch (Exception e) {
            System.err.println("❌ ERROR en filtrarPersonas: " + e.getMessage());
            e.printStackTrace();
            // En caso de error, devolver todas las personas
            return personaRepository.findAll();
        }
    }
    // 🔥 NUEVO: Método alternativo más simple
    public List<Persona> filtrarPersonasSimple(String nombre, String dni, String tipoPersonaStr) {
        try {
            // Obtener todas las personas primero
            List<Persona> todasPersonas = personaRepository.findAll();
            
            // Aplicar filtros manualmente
            return todasPersonas.stream()
                .filter(persona -> {
                    boolean cumple = true;
                    
                    // Filtrar por nombre (búsqueda parcial, case-insensitive)
                    if (nombre != null && !nombre.trim().isEmpty()) {
                        cumple = cumple && persona.getNombre().toLowerCase()
                            .contains(nombre.toLowerCase());
                    }
                    
                    // Filtrar por DNI (búsqueda parcial)
                    if (dni != null && !dni.trim().isEmpty()) {
                        cumple = cumple && persona.getDni().contains(dni);
                    }
                    
                    // Filtrar por tipo de persona
                    if (tipoPersonaStr != null && !tipoPersonaStr.trim().isEmpty()) {
                        try {
                            TipoPersona tipoFiltro = TipoPersona.valueOf(tipoPersonaStr);
                            cumple = cumple && persona.getTipoPersona() == tipoFiltro;
                        } catch (IllegalArgumentException e) {
                            // Si el tipo no es válido, no filtrar por tipo
                            System.err.println("Tipo de persona no válido: " + tipoPersonaStr);
                        }
                    }
                    
                    return cumple;
                })
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            System.err.println("❌ ERROR en filtrarPersonasSimple: " + e.getMessage());
            e.printStackTrace();
            return personaRepository.findAll();
        }
    }

    // Tus métodos existentes...
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
    // 🔥 NUEVO: Método para obtener personas activas (para vincular)
    public List<Persona> obtenerPersonasActivas() {
        // Si no tienes campo 'estado', simplemente devuelve todas
        return personaRepository.findAll();
    }
    
    // O si quieres filtrar por estado ACTIVO:
    public List<Persona> obtenerPersonasActivasConFiltro() {
        return personaRepository.findAll().stream()
            .filter(p -> "ACTIVO".equals(p.getEstado()))
            .collect(Collectors.toList());
    }
}

