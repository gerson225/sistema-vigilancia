package com.proyecto.vigilancia.vigilancia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.proyecto.vigilancia.vigilancia.model.Camara;
import com.proyecto.vigilancia.vigilancia.model.EstadoCamara;

@Getter
@Setter
@Entity
@Table(name = "detecciones")
public class Deteccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deteccion")
    private Integer idDeteccion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @JsonIgnore
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_camara", referencedColumnName = "id_camara")
    private Camara camara;

    @Column(name = "revisado")
    private Boolean revisado = false;

    @Column(name = "fecha_revision")
    private LocalDateTime fechaRevision;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_deteccion")
    private TipoDeteccion tipoDeteccion;


    @Column(name = "resultado")
    private String resultado;

    @Column(name = "observaciones")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "id_usuario_revision")
    private Integer usuarioRevision;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoDeteccion estado;

}
