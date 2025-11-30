package com.proyecto.vigilancia.vigilancia.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PersonaDTO {
    private Integer idPersona;
    private String nombre;
    private String dni;
    private String foto;
    private String descripcion;
    private LocalDateTime fechaRegistro;
    private String tipoPersona;
    private LocalDateTime ultimaDeteccion;
    private String estado;
    
    // Solo IDs de detecciones, no los objetos completos
    private List<Integer> deteccionesIds;
}