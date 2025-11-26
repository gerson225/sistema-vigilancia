// dto/LoginRequest.java
package com.proyecto.vigilancia.vigilancia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String usuario;
    private String contrasena;
}
