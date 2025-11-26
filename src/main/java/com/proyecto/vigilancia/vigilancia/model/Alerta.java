package com.proyecto.vigilancia.vigilancia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.proyecto.vigilancia.vigilancia.entity.Deteccion;
import com.proyecto.vigilancia.vigilancia.entity.Usuario;

@Getter
@Setter
@Entity
@Table(name = "alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Integer idAlerta;

    @ManyToOne
    @JoinColumn(name = "id_deteccion", referencedColumnName = "id_deteccion")
    private Deteccion deteccion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @NotNull
    @Column(name = "tipo_alerta")
    private String tipoAlerta;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "fecha_alerta")
    private LocalDateTime fechaAlerta; 
}
