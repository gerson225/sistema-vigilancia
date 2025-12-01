package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/configuracion/parametros")
public class ConfiguracionParametroController {

    @GetMapping
    public String gestionParametros(Model model) {
        return "configuracion-parametros";
    }
}