package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.proyecto.vigilancia.vigilancia.model.Camara;
import com.proyecto.vigilancia.vigilancia.service.CamaraService;

import java.util.List;

@Controller
@RequestMapping("/configuracion/camaras")
public class ConfiguracionCamaraController {

    private final CamaraService camaraService;

    public ConfiguracionCamaraController(CamaraService camaraService) {
        this.camaraService = camaraService;
    }

    @GetMapping
    public String gestionCamaras(Model model) {
        List<Camara> camaras = camaraService.obtenerTodasLasCamaras();
        model.addAttribute("camaras", camaras);
        model.addAttribute("nuevaCamara", new Camara()); // Para el formulario
        return "configuracion-camaras";
    }
}