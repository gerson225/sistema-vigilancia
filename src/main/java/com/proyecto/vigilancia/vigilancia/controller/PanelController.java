package com.proyecto.vigilancia.vigilancia.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelController {

    /* @GetMapping("/alertas")
    public String alertas() {
        return "alertas"; // busca alertas.html en templates
    }
    */
    /* @GetMapping("/camaras")
    public String camaras() {
        return "camaras"; // busca camaras.html en templates
    }
    */
    @GetMapping("/personas")
    public String personas() {
        return "personas"; // busca personas.html en templates
    }

    @GetMapping("/detecciones")
    public String detecciones() {
        return "detecciones"; // busca detecciones.html en templates
    }
    @GetMapping("/configuracion")
    public String configuracion() {
        return "configuracion"; // busca configuracion.html en templates
    }
}
