package com.proyecto.vigilancia.vigilancia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer idPersona;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @Column(name = "foto")
    private String foto;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_persona")
    private TipoPersona tipoPersona;

    @Column(name = "ultima_deteccion")
    private LocalDateTime ultimaDeteccion;

    @Column(name = "estado")
    private String estado; // ACTIVO, INACTIVO

    // Relación con detecciones
    @OneToMany(mappedBy = "persona")
    @JsonIgnore
    private List<Deteccion> detecciones;
}
