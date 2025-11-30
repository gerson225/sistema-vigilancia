package com.proyecto.vigilancia.vigilancia.model;

import com.proyecto.vigilancia.vigilancia.entity.Usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "camaras")
public class Camara {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_camara")
    private Integer idCamara;

    @Column(name = "nombre_camara")
    private String nombreCamara;
    
    private String ubicacion;

    @Column(name = "direccion_ip")
    private String direccionIp;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCamara estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

}
