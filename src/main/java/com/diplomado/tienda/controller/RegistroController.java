package com.diplomado.tienda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistroController {

    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro() {
        return "registro";
    }
}
