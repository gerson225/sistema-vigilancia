package com.proyecto.vigilancia.vigilancia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // Esto busca login.html en /templates
    }
}
