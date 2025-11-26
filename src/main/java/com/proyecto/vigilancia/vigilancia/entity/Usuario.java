package com.proyecto.vigilancia.vigilancia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "usuario", nullable = false, unique = true)
    private String usuario;

    @Column(name = "rol", nullable = false)
    private String rol;

    @Column(name = "fecha_creacion", nullable = false)
    private Timestamp fechaCreacion; 

    @Column(name = "id_rol")
    private Integer idRol; // puede ser null hasta sincronizar

    @Column(name = "contraseña", nullable = false)
    private String contrasena; // mapea a la columna con ñ
}
