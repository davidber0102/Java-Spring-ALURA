package com.aluracurso.screnmatch_frases.Controller;

import com.aluracurso.screnmatch_frases.Servicios.FraseService;
import com.aluracurso.screnmatch_frases.dto.FraseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FraseController {
    @Autowired
    FraseService service;

    @GetMapping("/series/frases")
    public FraseDTO obtenerFrase(){
            return service.obtenerFraseAleatoria();
    }
}
