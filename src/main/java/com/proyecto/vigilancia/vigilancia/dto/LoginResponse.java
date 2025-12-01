package com.proyecto.vigilancia.vigilancia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private boolean success;
    private String mensaje;
    private String rol;
    private Integer idUsuario;
    private String nombre;

    public LoginResponse(boolean success, String mensaje, String rol, Integer idUsuario, String nombre) {
        this.success = success;
        this.mensaje = mensaje;
        this.rol = rol;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
    }
}
