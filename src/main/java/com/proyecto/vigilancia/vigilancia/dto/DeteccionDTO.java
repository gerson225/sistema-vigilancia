package com.proyecto.vigilancia.vigilancia.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class DeteccionDTO {
    private Integer idDeteccion;
    private Integer idPersona; // Solo el ID, no el objeto completo
    private String nombrePersona; // Para mostrar en frontend
    private Integer idCamara;
    private String nombreCamara; // Para mostrar en frontend
    private LocalDateTime fechaHora;
    private String tipoDeteccion;
    private String resultado;
    private String observaciones;
    private Integer idUsuario;
    private String imagenUrl;
    private String estado;
}