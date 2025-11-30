package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.proyecto.vigilancia.vigilancia.entity.Persona;
import com.proyecto.vigilancia.vigilancia.service.PersonaService;
import com.proyecto.vigilancia.vigilancia.dto.PersonaDTO;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vistas/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping("")
    public String mostrarPersonas(Model model) {
        // Este controller solo maneja la vista HTML
        return "personas"; // Devuelve el template personas.html
    }

    // Endpoint para obtener datos de una persona (para editar)
    @GetMapping("/api/personas/{id}")
    @ResponseBody
    public PersonaDTO obtenerPersonaParaEditar(@PathVariable Integer id) {
        return personaService.obtenerPersonaPorId(id)
            .map(persona -> {
                PersonaDTO dto = new PersonaDTO();
                dto.setIdPersona(persona.getIdPersona());
                dto.setNombre(persona.getNombre());
                dto.setDni(persona.getDni());
                dto.setFoto(persona.getFoto());
                dto.setDescripcion(persona.getDescripcion());
                dto.setTipoPersona(persona.getTipoPersona() != null ? persona.getTipoPersona().name() : null);
                dto.setEstado(persona.getEstado());
                return dto;
            })
            .orElse(null);
    }

    // Endpoint para actualizar persona
    @PutMapping("/api/personas/{id}")
    @ResponseBody
    public String actualizarPersona(@PathVariable Integer id, @RequestBody PersonaDTO personaDTO) {
        try {
            Persona persona = personaService.obtenerPersonaPorId(id).orElseThrow();
            persona.setNombre(personaDTO.getNombre());
            persona.setDni(personaDTO.getDni());
            persona.setFoto(personaDTO.getFoto());
            persona.setDescripcion(personaDTO.getDescripcion());
            persona.setTipoPersona(personaDTO.getTipoPersona() != null ? 
                com.proyecto.vigilancia.vigilancia.entity.TipoPersona.valueOf(personaDTO.getTipoPersona()) : null);
            persona.setEstado(personaDTO.getEstado());
            
            personaService.guardarPersona(persona);
            return "{\"success\": true, \"message\": \"Persona actualizada correctamente\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error al actualizar persona: " + e.getMessage() + "\"}";
        }
    }
}