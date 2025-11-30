package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyecto.vigilancia.vigilancia.model.Camara;
import com.proyecto.vigilancia.vigilancia.service.CamaraService;

import java.util.List;

@Controller
public class CamaraController {

    private final CamaraService camaraService;

    public CamaraController(CamaraService camaraService) {
        this.camaraService = camaraService;
    }

    @GetMapping("/camaras")
    public String mostrarCamaras(Model model) {
        List<Camara> camaras = camaraService.obtenerTodasLasCamaras();
        model.addAttribute("camaras", camaras);
        return "camaras";
    }
}