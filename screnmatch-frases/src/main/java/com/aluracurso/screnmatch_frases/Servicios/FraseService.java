package com.aluracurso.screnmatch_frases.Servicios;

import com.aluracurso.screnmatch_frases.Model.Frase;
import com.aluracurso.screnmatch_frases.Repositorio.FraseRepository;
import com.aluracurso.screnmatch_frases.dto.FraseDTO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class FraseService {
    @Autowired
    private FraseRepository repository;

    public FraseDTO obtenerFraseAleatoria() {
        Frase frase = repository.obtenerFraseAleatoria();
        return new FraseDTO(frase.getTitulo(), frase.getFrase(), frase.getPersonaje(), frase.getPoster());
    }
}

