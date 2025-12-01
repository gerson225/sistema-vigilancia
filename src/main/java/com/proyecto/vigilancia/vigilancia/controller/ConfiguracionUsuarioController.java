package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.service.UsuarioService;

import java.util.List;

@Controller
@RequestMapping("/configuracion/usuarios")
public class ConfiguracionUsuarioController {

    private final UsuarioService usuarioService;

    public ConfiguracionUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String gestionUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "configuracion-usuarios";
    }
}