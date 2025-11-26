package com.proyecto.vigilancia.vigilancia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String nombre;
    private String usuario;
    private String contrasena;
    private String rol; // opcional, por ejemplo ADMINISTRADOR o USUARIO
}
